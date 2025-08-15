package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.mapper.ModelTokenConsumptionOverviewMapper;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.ModelTokenConsumptionOverviewDto;
import com.ruoyi.gpt.model.dto.UpdateMaxConsumptionAmountDto;
import com.ruoyi.gpt.model.vo.ModelTokenConsumptionOverviewVo;
import com.ruoyi.gpt.service.IGptBillService;
import com.ruoyi.gpt.service.IModelTokenConsumptionOverviewService;
import com.ruoyi.system.domain.event.CreateUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.dev.BiffViewer;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * GPT消费概览Service业务层处理
 *
 * @author yecao
 * @date 2023-04-28
 */
@Slf4j
@Service
public class ModelTokenConsumptionOverviewServiceImpl implements IModelTokenConsumptionOverviewService {

    public static final String MONTH_DEVICE_POWER_LOCK = "MONTH_DEVICE_POWER_LOCK:";

    @Resource
    private ModelTokenConsumptionOverviewMapper modelTokenConsumptionOverviewMapper;

    @Resource
    private ChatgptQARecordMapper chatgptQARecordMapper;

    @Resource
    private RedisCache redisCache;

    @Value("${chatGPT.availableBalancePrefix}")
    private String availableBalancePrefix;

    @Value("${chatGPT.availableBalance}")
    private String availableBalance;

    @Resource
    private IGptBillService gptBillService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 查询GPT消费概览
     *
     * @param id GPT消费概览主键
     * @return GPT消费概览
     */
    @Override
    public ModelTokenConsumptionOverview selectModelTokenConsumptionOverviewById(Long id) {
        return modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverviewById(id);
    }

    /**
     * 查询GPT消费概览列表
     *
     * @param modelTokenConsumptionOverviewDto GPT消费概览
     * @return GPT消费概览
     */
    @Override
    @DataScope(deptAlias = "mtco", userAlias = "mtco")
    public List<ModelTokenConsumptionOverviewVo> selectModelTokenConsumptionOverviewList(ModelTokenConsumptionOverviewDto modelTokenConsumptionOverviewDto) {
        return modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverviewList(modelTokenConsumptionOverviewDto);
    }

    /**
     * 核算GPT消费概览列表
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return GPT消费概览集合
     */
    @Override
    public int totalCost(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        modelTokenConsumptionOverview.setUserId(null);
        modelTokenConsumptionOverview.setType("dept");
        List<ChatgptQARecord> chatgptQARecords = chatgptQARecordMapper.selectChatgptQARecordByModelTokenConsumptionOverview(modelTokenConsumptionOverview);
        ModelTokenConsumptionOverview deptOverview = modelTokenConsumptionOverviewMapper.selectDeptOverviewByDeptId(modelTokenConsumptionOverview.getDeptId());
        if (deptOverview == null){
            ModelTokenConsumptionOverview deptBuild = ModelTokenConsumptionOverview.builder()
                    .currentBillTokenConsumption(0)
                    .currentBillConsumptionAmount(new BigDecimal(0))
                    .totalTokenConsumption(0)
                    .totalConsumptionAmount(new BigDecimal(0))
                    .deptId(SecurityUtils.getDeptId())
                    .userId(SecurityUtils.getUserId())
                    .type("dept")
                    .build();
            deptBuild.setCreateTime(DateUtils.getNowDate());
            deptBuild.setLastGenerateBillTime(DateUtils.getNowDate());
            deptBuild.setCreateBy("system");
            modelTokenConsumptionOverviewMapper.insertModelTokenConsumptionOverview(deptBuild);
            return this.totalByChatRecords(chatgptQARecords, deptBuild);
        }
        return this.totalByChatRecords(chatgptQARecords, deptOverview);
    }

    @Override
    public int userTotalCost(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        modelTokenConsumptionOverview.setType("user");
        List<ChatgptQARecord> chatgptQARecords = chatgptQARecordMapper.selectChatgptQARecordByModelTokenConsumptionOverview(modelTokenConsumptionOverview);
        ModelTokenConsumptionOverview userOverview = modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverview(modelTokenConsumptionOverview);
        return this.totalByChatRecords(chatgptQARecords, userOverview);
    }

    /**
     * 修改最大消费金额 用于个人消费概览
     *
     * @param updateMaxConsumptionAmountDto 修改最大消费金额 用于个人消费概览
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMaxConsumptionAmount(UpdateMaxConsumptionAmountDto updateMaxConsumptionAmountDto) {
        Long userId = updateMaxConsumptionAmountDto.getUserId();
        String availableBalance = redisCache.getCacheObject(availableBalancePrefix + userId);

        BigDecimal updatedAvailableBalance = BigDecimal.ZERO;
        ModelTokenConsumptionOverview overview = modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverviewById(
                updateMaxConsumptionAmountDto.getOverviewId());

        if ("increase".equals(updateMaxConsumptionAmountDto.getOperationType())) {
            // 增加
            updatedAvailableBalance = new BigDecimal(availableBalance).add(new BigDecimal(updateMaxConsumptionAmountDto.getConsumptionAmount()));
            overview.setMaxConsumptionAmount(overview.getMaxConsumptionAmount().add(new BigDecimal(updateMaxConsumptionAmountDto.getConsumptionAmount())));
        } else if ("decrease".equals(updateMaxConsumptionAmountDto.getOperationType())) {
            // 减少
            updatedAvailableBalance = new BigDecimal(availableBalance).subtract(new BigDecimal(updateMaxConsumptionAmountDto.getConsumptionAmount()));
            if (updatedAvailableBalance.compareTo(BigDecimal.ZERO) < 0) {
                return -1;
            }
            overview.setMaxConsumptionAmount(overview.getMaxConsumptionAmount().subtract(new BigDecimal(updateMaxConsumptionAmountDto.getConsumptionAmount())));
        }

        redisCache.setCacheObject(availableBalancePrefix + userId, updatedAvailableBalance.toString());
        // 更新数overview最大可用额度
        overview.setUpdateTime(DateUtils.getNowDate());
        overview.setUpdateBy(SecurityUtils.getUsername());
        return modelTokenConsumptionOverviewMapper.updateModelTokenConsumptionOverview(overview);
    }

    private int totalByChatRecords(List<ChatgptQARecord> chatgptQARecords, ModelTokenConsumptionOverview overview) {
        if (StringUtils.isEmpty(chatgptQARecords)) {
            return 2;
        }
        Date nowDate = DateUtils.getNowDate();
        // chatgptQARecords根据model进行分组
        Map<String, List<ChatgptQARecord>> collect = chatgptQARecords.stream().collect(Collectors.groupingBy(ChatgptQARecord::getModel));

        // 遍历collect，计算每个model的总消费
        BigDecimal currentBillConsumptionAmount = BigDecimal.ZERO;
        Integer currentBillTokenConsumption = 0;
        JSONObject currentBillDetail = new JSONObject();
        for (String model : collect.keySet()) {
            // 模型token单价
            String cacheObject = redisCache.getCacheObject(model);
            BigDecimal unitPrice = new BigDecimal(cacheObject);
            // 模型token总数
            int currentModelTokenSum = collect.get(model).stream().mapToInt(ChatgptQARecord::getTokenUsage).sum();
            currentBillTokenConsumption += currentModelTokenSum;

            // 当前帐单模型token总价
            BigDecimal currentModelBillPrice = unitPrice.multiply(new BigDecimal(currentModelTokenSum));
            currentBillConsumptionAmount = currentBillConsumptionAmount.add(currentModelBillPrice);

            String overviewDetail = overview.getOverviewDetail();
            if (StringUtils.isEmpty(overviewDetail)) {
                JSONObject modelDetail = new JSONObject();
                modelDetail.set("tokenConsumption", currentModelTokenSum);
                modelDetail.set("tokenUnitPrice", unitPrice);
                modelDetail.set("amount", currentModelBillPrice);
                currentBillDetail.set(model, modelDetail);
            } else {
                JSONObject entries = new JSONObject(overviewDetail);
                JSONObject jsonObject = entries.getJSONObject(model);

                JSONObject modelDetail = new JSONObject();
                modelDetail.set("tokenConsumption", currentModelTokenSum + (jsonObject == null ? 0 : jsonObject.getInt("tokenConsumption")));
                modelDetail.set("tokenUnitPrice", unitPrice);
                modelDetail.set("amount", currentModelBillPrice.add(jsonObject == null ? BigDecimal.ZERO : jsonObject.getBigDecimal("amount")));
                currentBillDetail.set(model, modelDetail);
            }
        }

        // 总token数量
        overview.setTotalTokenConsumption(overview.getTotalTokenConsumption() + currentBillTokenConsumption);
        // 总消费金额
        overview.setTotalConsumptionAmount(overview.getTotalConsumptionAmount().add(currentBillConsumptionAmount));

        // 当前帐单消费总金额
        overview.setCurrentBillConsumptionAmount(overview.getCurrentBillConsumptionAmount().add(currentBillConsumptionAmount));
        // 当前帐单token总数
        overview.setCurrentBillTokenConsumption(overview.getCurrentBillTokenConsumption() + currentBillTokenConsumption);
        overview.setOverviewDetail(StringEscapeUtils.unescapeJava(currentBillDetail.toString()));
        overview.setLastGenerateBillTime(nowDate);
        return modelTokenConsumptionOverviewMapper.updateModelTokenConsumptionOverview(overview);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateBill(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        // 刷新总消费
        this.totalCost(modelTokenConsumptionOverview);

        // 生成账单
        ModelTokenConsumptionOverview overview = modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverviewById(modelTokenConsumptionOverview.getId());
        gptBillService.insertGptBill(overview);

        // 重置当前账单
        overview.setCurrentBillConsumptionAmount(new BigDecimal(0));
        overview.setCurrentBillTokenConsumption(0);
        overview.setOverviewDetail("");
        return modelTokenConsumptionOverviewMapper.updateModelTokenConsumptionOverview(overview);
    }

    /**
     * 初始化GPT消费概览
     *
     * @return 结果
     */
    @Override
    @EventListener
    public int modelTokenConsumptionOverviewInit(CreateUserEvent createUserEvent) {
        SysUser user = createUserEvent.getUser();
        // 创建个人的
        ModelTokenConsumptionOverview build = ModelTokenConsumptionOverview.builder()
                .currentBillTokenConsumption(0)
                .currentBillConsumptionAmount(new BigDecimal(0))
                .totalTokenConsumption(0)
                .maxConsumptionAmount(new BigDecimal(this.availableBalance + ""))
                .totalConsumptionAmount(new BigDecimal(0))
                .deptId(createUserEvent.getUser().getDeptId())
                .userId(createUserEvent.getUser().getUserId())
                .type("user")
                .build();
        build.setCreateTime(DateUtils.getNowDate());
        build.setLastGenerateBillTime(DateUtils.getNowDate());
//        build.setCreateBy(SecurityUtils.getUsername());
        modelTokenConsumptionOverviewMapper.insertModelTokenConsumptionOverview(build);
        redisCache.setCacheObject(this.availableBalancePrefix + user.getUserId(), this.availableBalance + "");

        // roleIds[0] == 100 为GPT-部门管理员
        if (user.getRoleIds()[0] == 100) {
            Long deptId = user.getDeptId();
            ModelTokenConsumptionOverview deptOverview = modelTokenConsumptionOverviewMapper.selectDeptOverviewByDeptId(deptId);
            if (StringUtils.isNull(deptOverview)) {
                ModelTokenConsumptionOverview deptBuild = ModelTokenConsumptionOverview.builder()
                        .currentBillTokenConsumption(0)
                        .currentBillConsumptionAmount(new BigDecimal(0))
                        .totalTokenConsumption(0)
                        .totalConsumptionAmount(new BigDecimal(0))
                        .deptId(deptId)
                        .userId(user.getUserId())
                        .type("dept")
                        .build();
                build.setCreateTime(DateUtils.getNowDate());
                build.setLastGenerateBillTime(DateUtils.getNowDate());
                build.setCreateBy("system");
                modelTokenConsumptionOverviewMapper.insertModelTokenConsumptionOverview(deptBuild);
            }
        }
        return 1;
    }

    /**
     * 重置最大消费金额 每月月底执行
     *
     * @return 结果
     */
    @Override
    public void resetMaximumLimitJob() {
        String monthFormat = DateFormatUtils.format(DateUtils.getThisHourStartTimeStamp(), "yyyy-MM");
        RLock lock = redissonClient.getLock(MONTH_DEVICE_POWER_LOCK);
        try {
            boolean isGet = lock.tryLock(12, 8, TimeUnit.SECONDS);
            Boolean hadBeenRun = redisCache.getCacheObject(MONTH_DEVICE_POWER_LOCK + monthFormat + ":" + "hadBeenRun");
            if (isGet && hadBeenRun == null) {
                List<SysUser> sysUsers = modelTokenConsumptionOverviewMapper.selectGptSysUserList();
                if (StringUtils.isEmpty(sysUsers)) {
                    return;
                }
                sysUsers.stream().forEach(user -> {
                    try {
                        ModelTokenConsumptionOverview build = ModelTokenConsumptionOverview.builder()
                                .userId(user.getUserId())
                                .deptId(user.getDeptId())
                                .type("user")
                                .build();
                        ModelTokenConsumptionOverview overview = modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverview(build);
                        userTotalCost(overview);
                        BigDecimal maxConsumptionAmount = overview.getMaxConsumptionAmount();
                        redisCache.setCacheObject(this.availableBalancePrefix + user.getUserId(), maxConsumptionAmount.toString());
                    } catch (Exception e) {
                        log.error("重置{}最大消费金额失败", user.getUserId(), e);
                    }
                });
            }
            redisCache.setCacheObject(MONTH_DEVICE_POWER_LOCK + monthFormat + ":" + "hadBeenRun", true);
            log.warn("resetMaximumLimitJob() 结束");
        } catch (Exception e) {
            log.error("重置最大消费金额失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 新增GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    @Override
    public int insertModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        modelTokenConsumptionOverview.setCreateTime(DateUtils.getNowDate());
        return modelTokenConsumptionOverviewMapper.insertModelTokenConsumptionOverview(modelTokenConsumptionOverview);
    }

    /**
     * 修改GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    @Override
    public int updateModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        modelTokenConsumptionOverview.setUpdateTime(DateUtils.getNowDate());
        return modelTokenConsumptionOverviewMapper.updateModelTokenConsumptionOverview(modelTokenConsumptionOverview);
    }

    /**
     * 批量删除GPT消费概览
     *
     * @param ids 需要删除的GPT消费概览主键
     * @return 结果
     */
    @Override
    public int deleteModelTokenConsumptionOverviewByIds(Long[] ids) {
        return modelTokenConsumptionOverviewMapper.deleteModelTokenConsumptionOverviewByIds(ids);
    }

    /**
     * 删除GPT消费概览信息
     *
     * @param id GPT消费概览主键
     * @return 结果
     */
    @Override
    public int deleteModelTokenConsumptionOverviewById(Long id) {
        return modelTokenConsumptionOverviewMapper.deleteModelTokenConsumptionOverviewById(id);
    }
}
