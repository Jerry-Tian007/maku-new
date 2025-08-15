package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.user_kb_history.UserKbHistoryInsertDto;
import com.ruoyi.gpt.model.vo.user_kb_history.UserKbHistoryPageVo;

import java.util.List;

/**
 * 用户历史记录Service接口
 */
public interface IUserKbHistoryService {
    /**
     * 根据当前用户获取历史记录列表
     *
     * @param username
     * @return
     */
    List<UserKbHistoryPageVo> selectUserKbHistoryPageList(String username);

    /**
     * 根据id删除用户历史记录
     *
     * @param id
     * @return
     */
    int deleteUserKbHistoryById(Long id);

    /**
     * 新增用户历史记录
     * @param userKbHistoryInsertDto
     * @return
     */
    int insertUserKbHistory(UserKbHistoryInsertDto userKbHistoryInsertDto);

    Integer batchDeleteUserKbHistoryById(List<Long> ids);
}
