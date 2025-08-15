package com.ruoyi.gpt.mapper;

import java.util.List;
import com.ruoyi.gpt.model.domain.ChatgptPrompt;

/**
 * gpt提示语Mapper接口
 * 
 * @author Eric
 * @date 2023-05-23
 */
public interface ChatgptPromptMapper 
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
     * 删除gpt提示语
     * 
     * @param id gpt提示语主键
     * @return 结果
     */
    public int deleteChatgptPromptById(Long id);

    /**
     * 批量删除gpt提示语
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatgptPromptByIds(Long[] ids);
}
