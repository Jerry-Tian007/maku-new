package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ChatgptQARecordSceneSession;
import com.ruoyi.gpt.model.dto.chatgpt_q_a_record_scene_session.ChatgptQARecordSceneSessionAddDto;

import java.util.List;

/**
 * 场景问答会话Service接口
 * 
 * @author Eric
 * @date 2024-10-08
 */
public interface IChatgptQARecordSceneSessionService 
{
    /**
     * 查询场景问答会话
     * 
     * @param id 场景问答会话主键
     * @return 场景问答会话
     */
    public ChatgptQARecordSceneSession selectChatgptQARecordSceneSessionById(Long id);

    /**
     * 查询场景问答会话列表
     * 
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 场景问答会话集合
     */
    public List<ChatgptQARecordSceneSession> selectChatgptQARecordSceneSessionList(ChatgptQARecordSceneSession chatgptQARecordSceneSession);

    /**
     * 新增场景问答会话
     * 
     * @param chatgptQARecordSceneSessionAddDto 场景问答会话
     * @return 结果
     */
    public int insertChatgptQARecordSceneSession(ChatgptQARecordSceneSessionAddDto chatgptQARecordSceneSessionAddDto);

    /**
     * 修改场景问答会话
     * 
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 结果
     */
    public int updateChatgptQARecordSceneSession(ChatgptQARecordSceneSession chatgptQARecordSceneSession);

    /**
     * 批量删除场景问答会话
     * 
     * @param ids 需要删除的场景问答会话主键集合
     * @return 结果
     */
    public int deleteChatgptQARecordSceneSessionByIds(Long[] ids);

    /**
     * 删除场景问答会话信息
     * 
     * @param id 场景问答会话主键
     * @return 结果
     */
    public int deleteChatgptQARecordSceneSessionById(Long id);

    List<ChatgptQARecord> selectChatgptQARecordSceneSessionHistpry(ChatgptQARecordSceneSession chatgptQARecordSceneSession);
}
