package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.UserKbHistoryMapper;
import com.ruoyi.gpt.model.domain.UserKbHistory;
import com.ruoyi.gpt.model.dto.user_kb_history.UserKbHistoryInsertDto;
import com.ruoyi.gpt.model.vo.user_kb_history.UserKbHistoryPageVo;
import com.ruoyi.gpt.service.IUserKbHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户历史记录Service业务层处理
 */
@Service
public class UserKbHistoryServiceImpl implements IUserKbHistoryService {

    @Autowired
    private UserKbHistoryMapper userKbHistoryMapper;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Override
    public List<UserKbHistoryPageVo> selectUserKbHistoryPageList(String username) {
        return userKbHistoryMapper.selectUserKbHistoryPageList(username);
    }

    @Override
    public int deleteUserKbHistoryById(Long id) {
        return userKbHistoryMapper.deleteUserKbHistoryById(id);
    }

    @Override
    public int insertUserKbHistory(UserKbHistoryInsertDto userKbHistoryInsertDto) {
        UserKbHistory check = userKbHistoryMapper.selectBySessionMark(userKbHistoryInsertDto.getSessionMark());
        if (check != null) {
            return 0;
        }
        UserKbHistory userKbHistory = BeanConvertUtil.conver(userKbHistoryInsertDto, UserKbHistory.class);
        userKbHistory.setCreateBy(SecurityUtils.getUsername());
        userKbHistory.setCreateTime(new Date());
        userKbHistory.setStatus(1);
        return userKbHistoryMapper.insertUserKbHistory(userKbHistory);
    }

    @Override
    public Integer batchDeleteUserKbHistoryById(List<Long> ids) {
        return userKbHistoryMapper.batchDeleteUserKbHistoryById(ids);
    }
}
