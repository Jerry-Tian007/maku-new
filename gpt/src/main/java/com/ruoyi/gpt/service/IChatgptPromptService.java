package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.ChatgptPrompt;

/**
 * gpt提示语Service接口
 * 
 * @author Eric
 * @date 2023-05-23
 */
public interface IChatgptPromptService 
{
    /**
     * 查询gpt提示语
     * 
     * @param id gpt提示语主键
     * @return gpt提示语
     */
    public ChatgptPrompt selectChatgptPromptById(Long id);

    /**
     * 查询gpt提示语列表
     * 
     * @param chatgptPrompt gpt提示语
     * @return gpt提示语集合
     */
    public List<ChatgptPrompt> selectChatgptPromptList(ChatgptPrompt chatgptPrompt);

    /**
     * 新增gpt提示语
     * 
     * @param chatgptPrompt gpt提示语
     * @return 结果
     */
    public int insertChatgptPrompt(ChatgptPrompt chatgptPrompt);

    /**
     * 修改gpt提示语
     * 
     * @param chatgptPrompt gpt提示语
     * @return 结果
     */
    public int updateChatgptPrompt(ChatgptPrompt chatgptPrompt);

    /**
     * 批量删除gpt提示语
     * 
     * @param ids 需要删除的gpt提示语主键集合
     * @return 结果
     */
    public int deleteChatgptPromptByIds(Long[] ids);

    /**
     * 删除gpt提示语信息
     * 
     * @param id gpt提示语主键
     * @return 结果
     */
    public int deleteChatgptPromptById(Long id);
}
