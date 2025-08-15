package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.UserKbHistory;
import com.ruoyi.gpt.model.vo.user_kb_history.UserKbHistoryPageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户历史记录Mapper接口
 */
public interface UserKbHistoryMapper {
    List<UserKbHistoryPageVo> selectUserKbHistoryPageList(@Param("username") String username);

    int deleteUserKbHistoryById(@Param("id") Long id);

    @GenerateUniqueId
    int insertUserKbHistory(@Param("userKbHistory") UserKbHistory userKbHistory);

    UserKbHistory selectBySessionMark(@Param("sessionMark")String sessionMark);

    UserKbHistory selectById(@Param("id")Long id);

    Integer batchDeleteUserKbHistoryById(List<Long> ids);
}
