package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ChatgptQARecordSceneSession;

import java.util.List;

/**
 * 场景问答会话Mapper接口
 * 
 * @author Eric
 * @date 2024-10-08
 */
public interface ChatgptQARecordSceneSessionMapper 
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
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 结果
     */
    @GenerateUniqueId
    public int insertChatgptQARecordSceneSession(ChatgptQARecordSceneSession chatgptQARecordSceneSession);

    /**
     * 修改场景问答会话
     * 
     * @param chatgptQARecordSceneSession 场景问答会话
     * @return 结果
     */
    public int updateChatgptQARecordSceneSession(ChatgptQARecordSceneSession chatgptQARecordSceneSession);

    /**
     * 删除场景问答会话
     * 
     * @param id 场景问答会话主键
     * @return 结果
     */
    public int deleteChatgptQARecordSceneSessionById(Long id);

    /**
     * 批量删除场景问答会话
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatgptQARecordSceneSessionByIds(Long[] ids);

    ChatgptQARecordSceneSession selectChatgptQARecordSceneSessionBySessionId(String sessionId);
}
