package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.mapper.KbQARecordMapper;
import com.ruoyi.gpt.service.IChatModelSessionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.chat_model_session.*;
import com.ruoyi.gpt.model.vo.chat_model_session.*;
import com.ruoyi.gpt.mapper.ChatModelSessionMapper;
import com.ruoyi.gpt.model.domain.ChatModelSessionEntity;
import java.util.List;

/**
 * 
 *
 * @author Eric
 */
@Service
public class ChatModelSessionServiceImpl implements IChatModelSessionService {

    @Autowired
    private ChatModelSessionMapper chatModelSessionMapper;

    @Autowired
    private ChatgptQARecordMapper chatgptQARecordMapper;

    /**
    * 创建
    *
    * @param chatModelSessionAddDto
    * @return
    */
    @Override
    public Integer add(ChatModelSessionAddDto chatModelSessionAddDto) {
        ChatModelSessionEntity chatModelSessionEntity = BeanConvertUtil.conver(chatModelSessionAddDto, ChatModelSessionEntity.class);
        chatModelSessionEntity.setCreateBy(SecurityUtils.getUsername());
        chatModelSessionEntity.setCreateTime(DateUtils.getNowDate());
        chatModelSessionEntity.setUpdateTime(DateUtils.getNowDate());
        chatModelSessionEntity.setUserId(SecurityUtils.getUserId());
        chatModelSessionEntity.setDelFlag("0");
        return chatModelSessionMapper.add(chatModelSessionEntity);
    }

    /**
     * 更新
     *
     * @param chatModelSessionUpdateDto
     * @return
     */
    @Override
    public Integer update(ChatModelSessionUpdateDto chatModelSessionUpdateDto) {
        ChatModelSessionEntity chatModelSessionEntity = BeanConvertUtil.conver(chatModelSessionUpdateDto, ChatModelSessionEntity.class);
        chatModelSessionEntity.setUpdateBy(SecurityUtils.getUsername());
        chatModelSessionEntity.setUpdateTime(DateUtils.getNowDate());
        return chatModelSessionMapper.updateById(chatModelSessionEntity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        return chatModelSessionMapper.deleteById(id);
    }

    /**
     * 查询分页信息
     *
     * @param chatModelSessionPageQryDto
     * @return
     */
    @Override
    public List<ChatModelSessionPageVO> pageList(ChatModelSessionPageQryDto chatModelSessionPageQryDto) {
        return chatModelSessionMapper.pageList(chatModelSessionPageQryDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public ChatModelSessionInfoVO getById(Long id) {
        return chatModelSessionMapper.getById(id);
    }

    @Override
    public List<ModelTopSessionVo> getModelTopSession(ModelTopSessionDto modelTopSessionDto) {
        return chatModelSessionMapper.getModelTopSession(modelTopSessionDto);
    }

    @Override
    public List<SessionHistoryVo> getSessionHistory(SessionHistoryDto sessionHistoryDto) {
        return chatgptQARecordMapper.getQARecordBySessionId(sessionHistoryDto);
    }

    @Override
    public ChatModelSessionInfoVO selectChatModelSessionBySessionId(String sessionId) {
        return chatModelSessionMapper.selectChatModelSessionBySessionId(sessionId);
    }
}
