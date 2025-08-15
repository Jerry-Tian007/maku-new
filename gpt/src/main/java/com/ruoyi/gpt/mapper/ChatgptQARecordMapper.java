package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ChatgptQARecordSceneSession;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.domain.ThirdPartyError;
import com.ruoyi.gpt.model.dto.chat_model_session.SessionHistoryDto;
import com.ruoyi.gpt.model.vo.chat_model_session.ChatModelSessionInfoVO;
import com.ruoyi.gpt.model.vo.chat_model_session.SessionHistoryVo;

import java.util.List;

/**
 * chatgptMapper接口
 *
 * @author Eric
 * @date 2023-04-18
 */
public interface ChatgptQARecordMapper {
    /**
     * 查询chatgpt
     *
     * @param id chatgpt主键
     * @return chatgpt
     */
    public ChatgptQARecord selectChatgptQARecordById(Long id);

    public List<ChatgptQARecord> selectChatgptQARecordByModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

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
    @GenerateUniqueId
    public int insertChatgptQARecord(ChatgptQARecord chatgptQARecord);

    /**
     * 修改chatgpt
     *
     * @param chatgptQARecord chatgpt
     * @return 结果
     */
    public int updateChatgptQARecord(ChatgptQARecord chatgptQARecord);

    /**
     * 删除chatgpt
     *
     * @param id chatgpt主键
     * @return 结果
     */
    public int deleteChatgptQARecordById(Long id);

    /**
     * 批量删除chatgpt
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatgptQARecordByIds(Long[] ids);

    void insertError(ThirdPartyError thirdPartyError);

    List<ChatgptQARecord> selectChatgptQARecordSceneSessionHistpry(ChatgptQARecordSceneSession chatgptQARecordSceneSession);

    List<SessionHistoryVo> getQARecordBySessionId(SessionHistoryDto sessionHistoryDto);


    int updateChatgptQARecordDetail(ChatgptQARecord chatgptQARecord);
}
