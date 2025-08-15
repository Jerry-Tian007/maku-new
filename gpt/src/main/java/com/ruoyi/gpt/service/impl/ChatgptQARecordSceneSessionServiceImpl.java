package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.mapper.ChatgptQARecordSceneSessionMapper;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ChatgptQARecordSceneSession;
import com.ruoyi.gpt.model.dto.chatgpt_q_a_record_scene_session.ChatgptQARecordSceneSessionAddDto;
import com.ruoyi.gpt.service.IChatgptQARecordSceneSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场景问答会话Service业务层处理
 * 
 * @author Eric
 * @date 2024-10-08
 */
@Service
public class ChatgptQARecordSceneSessionServiceImpl implements IChatgptQARecordSceneSessionService 
{
    @Autowired
    private ChatgptQARecordSceneSessionMapper chatgptQARecordSceneSessionMapper;

    @Autowired
    private ChatgptQARecordMapper chatgptQARecordMapper;

    /**
     * 查询场景问答会话
     * 
     * @param id 场景问答会话主键
     * @return 场景问答会话
     */
    @Override
    public ChatgptQARecordSceneSession selectChatgptQARecordSceneSessionById(Long id)
    {
        return chatgptQARecordSceneSessionMapper.selectChatgptQARecordSceneSessionById(id);
    }

    /**
     * 查询场景问答会话列表
     * 
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 场景问答会话
     */
    @Override
    public List<ChatgptQARecordSceneSession> selectChatgptQARecordSceneSessionList(ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        chatgptQARecordSceneSession.setUserId(SecurityUtils.getUserId());
        chatgptQARecordSceneSession.setDeptId(SecurityUtils.getDeptId());
        return chatgptQARecordSceneSessionMapper.selectChatgptQARecordSceneSessionList(chatgptQARecordSceneSession);
    }

    /**
     * 新增场景问答会话
     * 
     * @param chatgptQARecordSceneSessionAddDto 场景问答会话
     * @return 结果
     */
    @Override
    public int insertChatgptQARecordSceneSession(ChatgptQARecordSceneSessionAddDto chatgptQARecordSceneSessionAddDto) {
        ChatgptQARecordSceneSession chatgptQARecordSceneSession = new ChatgptQARecordSceneSession();
        return chatgptQARecordSceneSessionMapper.insertChatgptQARecordSceneSession(chatgptQARecordSceneSession);
    }

    /**
     * 修改场景问答会话
     * 
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 结果
     */
    @Override
    public int updateChatgptQARecordSceneSession(ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        chatgptQARecordSceneSession.setUpdateTime(DateUtils.getNowDate());
        return chatgptQARecordSceneSessionMapper.updateChatgptQARecordSceneSession(chatgptQARecordSceneSession);
    }

    /**
     * 批量删除场景问答会话
     * 
     * @param ids 需要删除的场景问答会话主键
     * @return 结果
     */
    @Override
    public int deleteChatgptQARecordSceneSessionByIds(Long[] ids)
    {
        return chatgptQARecordSceneSessionMapper.deleteChatgptQARecordSceneSessionByIds(ids);
    }

    /**
     * 删除场景问答会话信息
     * 
     * @param id 场景问答会话主键
     * @return 结果
     */
    @Override
    public int deleteChatgptQARecordSceneSessionById(Long id)
    {
        return chatgptQARecordSceneSessionMapper.deleteChatgptQARecordSceneSessionById(id);
    }

//    @Override
//    public List<ChatgptQARecord> selectChatgptQARecordSceneSessionHistpry(ChatgptQARecordSceneSession chatgptQARecordSceneSession) {
//        return null;
//    }

    @Override
    @DataScope(userAlias = "chat",deptAlias = "chat")
    public List<ChatgptQARecord> selectChatgptQARecordSceneSessionHistpry(ChatgptQARecordSceneSession chatgptQARecordSceneSession) {
        return chatgptQARecordMapper.selectChatgptQARecordSceneSessionHistpry(chatgptQARecordSceneSession);
    }
}
