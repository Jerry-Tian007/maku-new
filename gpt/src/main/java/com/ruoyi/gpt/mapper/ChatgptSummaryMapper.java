package com.ruoyi.gpt.mapper;

import java.util.List;
import com.ruoyi.gpt.model.domain.ChatgptSummary;

/**
 * gpt总结Mapper接口
 * 
 * @author Eric
 * @date 2023-09-26
 */
public interface ChatgptSummaryMapper 
{
    /**
     * 查询gpt总结
     * 
     * @param id gpt总结主键
     * @return gpt总结
     */
    public ChatgptSummary selectChatgptSummaryById(Long id);

    /**
     * 查询gpt总结列表
     * 
     * @param chatgptSummary gpt总结
     * @return gpt总结集合
     */
    public List<ChatgptSummary> selectChatgptSummaryList(ChatgptSummary chatgptSummary);

    /**
     * 查询gpt近24小时列表
     *
     * @param chatgptSummary gpt总结
     * @return gpt近24小时集合
     */
    public List<ChatgptSummary> selectUser24HourChatHistoryList(ChatgptSummary chatgptSummary);

    /**
     * 新增gpt总结
     * 
     * @param chatgptSummary gpt总结
     * @return 结果
     */
    public int insertChatgptSummary(ChatgptSummary chatgptSummary);

    /**
     * 修改gpt总结
     * 
     * @param chatgptSummary gpt总结
     * @return 结果
     */
    public int updateChatgptSummary(ChatgptSummary chatgptSummary);

    /**
     * 删除gpt总结
     * 
     * @param id gpt总结主键
     * @return 结果
     */
    public int deleteChatgptSummaryById(Long id);

    /**
     * 批量删除gpt总结
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatgptSummaryByIds(Long[] ids);

    ChatgptSummary selectChatgptSummaryByIdentityId(Long identityId);
}
