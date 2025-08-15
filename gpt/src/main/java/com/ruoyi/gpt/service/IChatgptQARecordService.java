package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ThirdPartyError;

/**
 * chatgptService接口
 * 
 * @author Eric
 * @date 2023-04-18
 */
public interface IChatgptQARecordService 
{
    /**
     * 查询chatgpt
     * 
     * @param id chatgpt主键
     * @return chatgpt
     */
    public ChatgptQARecord selectChatgptQARecordById(Long id);

    /**
     * 查询chatgpt列表
     * 
     * @param chatgptQARecord chatgpt
     * @return chatgpt集合
     */
    public List<ChatgptQARecord> selectChatgptQARecordList(ChatgptQARecord chatgptQARecord);

    /**
     * 新增chatgpt
     * 
     * @param chatgptQARecord chatgpt
     * @return 结果
     */
    public int insertChatgptQARecord(ChatgptQARecord chatgptQARecord);

    /**
     * 修改chatgpt
     * 
     * @param chatgptQARecord chatgpt
     * @return 结果
     */
    public int updateChatgptQARecord(ChatgptQARecord chatgptQARecord);

    /**
     * 批量删除chatgpt
     * 
     * @param ids 需要删除的chatgpt主键集合
     * @return 结果
     */
    public int deleteChatgptQARecordByIds(Long[] ids);

    /**
     * 删除chatgpt信息
     * 
     * @param id chatgpt主键
     * @return 结果
     */
    public int deleteChatgptQARecordById(Long id);

    void insertError(ThirdPartyError thirdPartyError);

    int updateChatgptQARecordDetail(ChatgptQARecord chatgptQARecord);
}
