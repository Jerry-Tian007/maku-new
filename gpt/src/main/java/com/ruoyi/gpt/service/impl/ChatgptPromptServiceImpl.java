package com.ruoyi.gpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.ChatgptPromptMapper;
import com.ruoyi.gpt.model.domain.ChatgptPrompt;
import com.ruoyi.gpt.service.IChatgptPromptService;

/**
 * gpt提示语Service业务层处理
 * 
 * @author Eric
 * @date 2023-05-23
 */
@Service
public class ChatgptPromptServiceImpl implements IChatgptPromptService 
{
    @Autowired
    private ChatgptPromptMapper chatgptPromptMapper;

    /**
     * 查询gpt提示语
     * 
     * @param id gpt提示语主键
     * @return gpt提示语
     */
    @Override
    public ChatgptPrompt selectChatgptPromptById(Long id)
    {
        return chatgptPromptMapper.selectChatgptPromptById(id);
    }

    /**
     * 查询gpt提示语列表
     * 
     * @param chatgptPrompt gpt提示语
     * @return gpt提示语
     */
    @Override
    public List<ChatgptPrompt> selectChatgptPromptList(ChatgptPrompt chatgptPrompt)
    {
        return chatgptPromptMapper.selectChatgptPromptList(chatgptPrompt);
    }

    /**
     * 新增gpt提示语
     * 
     * @param chatgptPrompt gpt提示语
     * @return 结果
     */
    @Override
    public int insertChatgptPrompt(ChatgptPrompt chatgptPrompt)
    {
        chatgptPrompt.setCreateTime(DateUtils.getNowDate());
        return chatgptPromptMapper.insertChatgptPrompt(chatgptPrompt);
    }

    /**
     * 修改gpt提示语
     * 
     * @param chatgptPrompt gpt提示语
     * @return 结果
     */
    @Override
    public int updateChatgptPrompt(ChatgptPrompt chatgptPrompt)
    {
        chatgptPrompt.setUpdateTime(DateUtils.getNowDate());
        return chatgptPromptMapper.updateChatgptPrompt(chatgptPrompt);
    }

    /**
     * 批量删除gpt提示语
     * 
     * @param ids 需要删除的gpt提示语主键
     * @return 结果
     */
    @Override
    public int deleteChatgptPromptByIds(Long[] ids)
    {
        return chatgptPromptMapper.deleteChatgptPromptByIds(ids);
    }

    /**
     * 删除gpt提示语信息
     * 
     * @param id gpt提示语主键
     * @return 结果
     */
    @Override
    public int deleteChatgptPromptById(Long id)
    {
        return chatgptPromptMapper.deleteChatgptPromptById(id);
    }
}
