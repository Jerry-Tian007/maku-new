package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationInsertDto;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationPageDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationSelectVo;

import java.util.List;
import java.util.Set;

/**
 * 知识库授权Service
 *
 */
public interface IKbAuthorizationService {

    /**
     * 知识库授权
     *
     * @param kbAuthorizationInsertDto
     * @return
     */
    Integer insertKbAuthorization(KbAuthorizationInsertDto kbAuthorizationInsertDto);

    /**
     * 已授权知识库列表
     *
     * @return
     */
    List<KbAuthorizationPageVo> list(KbAuthorizationPageDto kbAuthorizationPageDto);

    /**
     * 根据知识库id删除授权数据
     *
     * @param kbId
     * @return
     */
    Integer deleteKbAuthorizationByKbId(Long kbId);

    List<KbAuthorizationSelectVo> getSelect();

    Set<String> getKbListByRoleId(Long roleId);
}
