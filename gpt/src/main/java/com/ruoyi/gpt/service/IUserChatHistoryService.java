package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.UserChatHistory;

import java.util.List;

/**
 * chatService接口
 *
 * @author yecao
 * @date 2023-05-19
 */
public interface IUserChatHistoryService {
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
    public UserChatHistory insertUserChatHistory(UserChatHistory userChatHistory);

    /**
     * 修改chat
     *
     * @param userChatHistory chat
     * @return 结果
     */
    public int updateUserChatHistory(UserChatHistory userChatHistory);

    /**
     * 批量删除chat
     *
     * @param ids 需要删除的chat主键集合
     * @return 结果
     */
    public int deleteUserChatHistoryByIds(Long[] ids);

    /**
     * 删除chat信息
     *
     * @param id chat主键
     * @return 结果
     */
    public int deleteUserChatHistoryById(Long id);
}
