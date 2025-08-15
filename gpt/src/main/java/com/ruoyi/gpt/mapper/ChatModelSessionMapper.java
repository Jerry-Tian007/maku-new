package com.ruoyi.gpt.mapper;

import org.springframework.stereotype.Service;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.dto.chat_model_session.*;
import com.ruoyi.gpt.model.vo.chat_model_session.*;
import com.ruoyi.gpt.model.domain.ChatModelSessionEntity;

import java.util.List;

/**
 * @author Eric
 */
public interface ChatModelSessionMapper {

    /**
     * 创建
     *
     * @param chatModelSessionEntity
     * @return
     */
    @GenerateUniqueId
    Integer add(ChatModelSessionEntity chatModelSessionEntity);

    /**
     * 更新
     *
     * @param chatModelSessionEntity
     * @return
     */
    Integer updateById(ChatModelSessionEntity chatModelSessionEntity);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

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

    ChatModelSessionInfoVO selectChatModelSessionBySessionId(String sessionId);
}
