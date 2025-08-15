package com.ruoyi.gpt.controller;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.gpt.mapper.ModelTokenConsumptionOverviewMapper;
import com.ruoyi.gpt.mapper.TranslateDataMapper;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.service.IModelTokenConsumptionOverviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/translate")
public class TranslateData {

    @Resource
    private TranslateDataMapper translateDataMapper;

    @Resource
    private ModelTokenConsumptionOverviewMapper modelTokenConsumptionOverviewMapper;

    @Resource
    private IModelTokenConsumptionOverviewService modelTokenConsumptionOverviewService;

    @Resource
    private RedisCache redisCache;

    @Value("${chatGPT.availableBalancePrefix}")
    private String availableBalancePrefix;

    @Value("${chatGPT.availableBalance}")
    private String availableBalance;

    @GetMapping("/data")
    @Transactional(rollbackFor = Exception.class)
    public void translateData() {
        //指定日期
//        LocalDate localDate = LocalDate.of(2023, 4, 20);
        //将LocalDate转换为Date
//        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 查询用户userId deptId
        List<SysUser> userList = translateDataMapper.selectUserList();
        userList.forEach(user -> {
            // 创建初始个人概览
//            ModelTokenConsumptionOverview build = ModelTokenConsumptionOverview.builder()
//                    .deptId(user.getDeptId())
//                    .userId(user.getUserId())
//                    .currentBillTokenConsumption(0)
//                    .currentBillConsumptionAmount(new BigDecimal(0))
//                    .totalTokenConsumption(0)
//                    .totalConsumptionAmount(new BigDecimal(0))
//                    .maxConsumptionAmount(new BigDecimal(this.availableBalance + ""))
//                    .lastGenerateBillTime(date)
//                    .type("user")
//                    .build();
//            build.setCreateTime(DateUtils.getNowDate());
//            build.setLastGenerateBillTime(DateUtils.getNowDate());
//            build.setCreateBy(SecurityUtils.getUsername());
//            modelTokenConsumptionOverviewMapper.insertModelTokenConsumptionOverview(build);
//            redisCache.setCacheObject(availableBalancePrefix + user.getUserId(), this.availableBalance + "");

            ModelTokenConsumptionOverview c = ModelTokenConsumptionOverview.builder()
                    .userId(user.getUserId())
                    .deptId(user.getDeptId())
                    .type("user")
                    .build();
            ModelTokenConsumptionOverview overview = modelTokenConsumptionOverviewMapper.selectModelTokenConsumptionOverview(c);
            // total-cost一下
            modelTokenConsumptionOverviewService.userTotalCost(overview);

             // 更新redis
//            BigDecimal totalConsumptionAmount = build.getTotalConsumptionAmount();
//            BigDecimal subtract = new BigDecimal(this.availableBalance + "").subtract(totalConsumptionAmount);
//            redisCache.setCacheObject(availableBalancePrefix + user.getUserId(), subtract.toString());
        });
    }
}
