package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.UserChatHistoryMapper;
import com.ruoyi.gpt.model.domain.UserChatHistory;
import com.ruoyi.gpt.service.IUserChatHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * chatService业务层处理
 *
 * @author yecao
 * @date 2023-05-19
 */
@Service
public class UserChatHistoryServiceImpl implements IUserChatHistoryService
{
    @Resource
    private UserChatHistoryMapper userChatHistoryMapper;

    /**
     * 查询chat
     *
     * @param id chat主键
     * @return chat
     */
    @Override
    public UserChatHistory selectUserChatHistoryById(Long id)
    {
        return userChatHistoryMapper.selectUserChatHistoryById(id);
    }

    /**
     * 查询chat列表
     *
     * @param userChatHistory chat
     * @return chat
     */
    @Override
    @DataScope(deptAlias = "uch", userAlias = "uch")
    public List<UserChatHistory> selectUserChatHistoryList(UserChatHistory userChatHistory)
    {
        return userChatHistoryMapper.selectUserChatHistoryList(userChatHistory);
    }

    /**
     * 新增chat
     *
     * @param userChatHistory chat
     * @return 结果
     */
    @Override
    public UserChatHistory insertUserChatHistory(UserChatHistory userChatHistory)
    {
        userChatHistory.setDeptId(SecurityUtils.getDeptId());
        userChatHistory.setUserId(SecurityUtils.getUserId());
        userChatHistory.setCreateTime(DateUtils.getNowDate());
        userChatHistoryMapper.insertUserChatHistory(userChatHistory);
        return userChatHistory;
    }

    /**
     * 修改chat
     *
     * @param userChatHistory chat
     * @return 结果
     */
    @Override
    public int updateUserChatHistory(UserChatHistory userChatHistory)
    {
        userChatHistory.setUpdateTime(DateUtils.getNowDate());
        return userChatHistoryMapper.updateUserChatHistory(userChatHistory);
    }

    /**
     * 批量删除chat
     *
     * @param ids 需要删除的chat主键
     * @return 结果
     */
    @Override
    public int deleteUserChatHistoryByIds(Long[] ids)
    {
        return userChatHistoryMapper.deleteUserChatHistoryByIds(ids);
    }

    /**
     * 删除chat信息
     *
     * @param id chat主键
     * @return 结果
     */
    @Override
    public int deleteUserChatHistoryById(Long id)
    {
        return userChatHistoryMapper.deleteUserChatHistoryById(id);
    }
}
