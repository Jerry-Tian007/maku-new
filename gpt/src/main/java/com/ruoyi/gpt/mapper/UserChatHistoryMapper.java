package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.UserChatHistory;

import java.util.List;

/**
 * chatMapper接口
 *
 * @author yecao
 * @date 2023-05-19
 */
public interface UserChatHistoryMapper
{
    /**
     * 查询chat
     *
     * @param id chat主键
     * @return chat
     */
    public UserChatHistory selectUserChatHistoryById(Long id);

    /**
     * 查询chat列表
     *
     * @param userChatHistory chat
     * @return chat集合
     */
    public List<UserChatHistory> selectUserChatHistoryList(UserChatHistory userChatHistory);

    /**
     * 新增chat
     *
     * @param userChatHistory chat
     * @return 结果
     */
    @GenerateUniqueId
    public int insertUserChatHistory(UserChatHistory userChatHistory);

    /**
     * 修改chat
     *
     * @param userChatHistory chat
     * @return 结果
     */
    public int updateUserChatHistory(UserChatHistory userChatHistory);

    /**
     * 删除chat
     *
     * @param id chat主键
     * @return 结果
     */
    public int deleteUserChatHistoryById(Long id);

    /**
     * 批量删除chat
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserChatHistoryByIds(Long[] ids);
}
