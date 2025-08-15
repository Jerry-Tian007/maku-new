package com.ruoyi.gpt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ruoyi.gpt.model.dto.chat_model_session.*;
import com.ruoyi.gpt.model.vo.chat_model_session.*;
import com.ruoyi.gpt.mapper.ChatModelSessionMapper;
import java.util.List;

import com.ruoyi.gpt.model.domain.ChatModelSessionEntity;

/**
 * 
 *
 * @author Eric
 */
public interface IChatModelSessionService {

    /**
     * 创建
     *
     * @param chatModelSessionAddDto
     * @return
     */
    Integer add(ChatModelSessionAddDto chatModelSessionAddDto);

    /**
     * 更新
     *
     * @param chatModelSessionUpdateDto
     * @return
     */
    Integer update(ChatModelSessionUpdateDto chatModelSessionUpdateDto);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查询分页信息
     *
     * @param chatModelSessionPageQryDto
     * @return
     */
    List<ChatModelSessionPageVO> pageList(ChatModelSessionPageQryDto chatModelSessionPageQryDto);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    ChatModelSessionInfoVO getById(Long id);

    List<ModelTopSessionVo> getModelTopSession(ModelTopSessionDto modelTopSessionDto);

    List<SessionHistoryVo> getSessionHistory(SessionHistoryDto sessionHistoryDto);

    ChatModelSessionInfoVO selectChatModelSessionBySessionId(String sessionId);
}
