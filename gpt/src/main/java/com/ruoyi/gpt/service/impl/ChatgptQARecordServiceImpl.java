package com.ruoyi.gpt.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.ModelTokenConsumptionOverviewMapper;
import com.ruoyi.gpt.model.domain.ThirdPartyError;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.service.IChatgptQARecordService;

import javax.annotation.Resource;

/**
 * chatgptService业务层处理
 *
 * @author Eric
 * @date 2023-04-18
 */
@Service
public class ChatgptQARecordServiceImpl implements IChatgptQARecordService {
    @Resource
    private ChatgptQARecordMapper chatgptQARecordMapper;

    @Resource
    private ModelTokenConsumptionOverviewMapper modelTokenConsumptionOverviewMapper;

    /**
     * 查询chatgpt
     *
     * @param id chatgpt主键
     * @return chatgpt
     */
    @Override
    public ChatgptQARecord selectChatgptQARecordById(Long id) {
        return chatgptQARecordMapper.selectChatgptQARecordById(id);
    }

    /**
     * 查询chatgpt列表
     *
     * @param chatgptQARecord chatgpt
     * @return chatgpt
     */
    @Override
    @DataScope(deptAlias = "chat", userAlias = "chat")
    public List<ChatgptQARecord> selectChatgptQARecordList(ChatgptQARecord chatgptQARecord) {
        return chatgptQARecordMapper.selectChatgptQARecordList(chatgptQARecord);
    }

    /**
     * 新增chatgpt
     *
     * @param chatgptQARecord chatgpt
     * @return 结果
     */
    @Override
    public int insertChatgptQARecord(ChatgptQARecord chatgptQARecord) {
        chatgptQARecord.setDeptId(SecurityUtils.getDeptId());
        chatgptQARecord.setUserId(SecurityUtils.getUserId());
        chatgptQARecord.setCreateBy(SecurityUtils.getUsername());
        chatgptQARecord.setCreateTime(DateUtils.getNowDate());
        return chatgptQARecordMapper.insertChatgptQARecord(chatgptQARecord);
    }

    /**
     * 修改chatgpt
     *
     * @param chatgptQARecord chatgpt
     * @return 结果
     */
    @Override
    public int updateChatgptQARecord(ChatgptQARecord chatgptQARecord) {
        return chatgptQARecordMapper.updateChatgptQARecord(chatgptQARecord);
    }

    /**
     * 批量删除chatgpt
     *
     * @param ids 需要删除的chatgpt主键
     * @return 结果
     */
    @Override
    public int deleteChatgptQARecordByIds(Long[] ids) {
        return chatgptQARecordMapper.deleteChatgptQARecordByIds(ids);
    }

    /**
     * 删除chatgpt信息
     *
     * @param id chatgpt主键
     * @return 结果
     */
    @Override
    public int deleteChatgptQARecordById(Long id) {
        return chatgptQARecordMapper.deleteChatgptQARecordById(id);
    }

    @Override
    public void insertError(ThirdPartyError thirdPartyError) {
        chatgptQARecordMapper.insertError(thirdPartyError);
    }

    @Override
    public int updateChatgptQARecordDetail(ChatgptQARecord chatgptQARecord) {
        return chatgptQARecordMapper.updateChatgptQARecordDetail(chatgptQARecord);
    }
}
