package com.ruoyi.gpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.ChatgptSummaryMapper;
import com.ruoyi.gpt.model.domain.ChatgptSummary;
import com.ruoyi.gpt.service.IChatgptSummaryService;

import javax.annotation.Resource;

/**
 * gpt总结Service业务层处理
 * 
 * @author Eric
 * @date 2023-09-26
 */
@Service
public class ChatgptSummaryServiceImpl implements IChatgptSummaryService 
{
    @Resource
    private ChatgptSummaryMapper chatgptSummaryMapper;

    /**
     * 查询gpt总结
     * 
     * @param id gpt总结主键
     * @return gpt总结
     */
    @Override
    public ChatgptSummary selectChatgptSummaryById(Long id)
    {
        return chatgptSummaryMapper.selectChatgptSummaryById(id);
    }

    /**
     * 查询gpt总结列表
     *
     * @param chatgptSummary gpt总结
     * @return gpt总结
     */
    @Override
    public List<ChatgptSummary> selectChatgptSummaryList(ChatgptSummary chatgptSummary)
    {
        return chatgptSummaryMapper.selectChatgptSummaryList(chatgptSummary);
    }

    /**
     * 查询gpt近24小时列表
     *
     * @param chatgptSummary gpt总结
     * @return gpt近24小时总结
     */
    @Override
    public List<ChatgptSummary> selectUser24HourChatHistoryList(ChatgptSummary chatgptSummary)
    {
        return chatgptSummaryMapper.selectUser24HourChatHistoryList(chatgptSummary);
    }

    /**
     * 新增gpt总结
     * 
     * @param chatgptSummary gpt总结
     * @return 结果
     */
    @Override
    public int insertChatgptSummary(ChatgptSummary chatgptSummary)
    {
        chatgptSummary.setCreateTime(DateUtils.getNowDate());
        return chatgptSummaryMapper.insertChatgptSummary(chatgptSummary);
    }

    /**
     * 修改gpt总结
     * 
     * @param chatgptSummary gpt总结
     * @return 结果
     */
    @Override
    public int updateChatgptSummary(ChatgptSummary chatgptSummary)
    {
        return chatgptSummaryMapper.updateChatgptSummary(chatgptSummary);
    }

    /**
     * 批量删除gpt总结
     * 
     * @param ids 需要删除的gpt总结主键
     * @return 结果
     */
    @Override
    public int deleteChatgptSummaryByIds(Long[] ids)
    {
        return chatgptSummaryMapper.deleteChatgptSummaryByIds(ids);
    }

    /**
     * 删除gpt总结信息
     * 
     * @param id gpt总结主键
     * @return 结果
     */
    @Override
    public int deleteChatgptSummaryById(Long id)
    {
        return chatgptSummaryMapper.deleteChatgptSummaryById(id);
    }

    @Override
    public ChatgptSummary selectChatgptSummaryByIdentityId(Long identityId) {
        return chatgptSummaryMapper.selectChatgptSummaryByIdentityId(identityId);
    }
}
