package com.ruoyi.gpt.mapper;


import com.ruoyi.gpt.model.domain.KbAuthorization;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationPageDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageSubVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationSelectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 知识库授权Mapper接口
 */
public interface KbAuthorizationMapper {
    Integer insertKbAuthorization(@Param("kbAuthorizationList") List<KbAuthorization> kbAuthorizationList);

    Integer deleteKbAuthorizationByRoleId(@Param("roleId") Long roleId);

    Integer deleteKbAuthorizationByKbId(@Param("kbId") Long kbId);

    List<KbAuthorizationPageVo> selectKbAuthorizationPageVo(@Param("roleIds") Long[] roleIds, @Param("kbAuthorizationPageDto") KbAuthorizationPageDto kbAuthorizationPageDto);

    List<KbAuthorizationPageSubVo> selectKbAuthorizationPageSubVo(@Param("roleIds") Long[] roleIds);

    List<KbAuthorizationSelectVo> getSelect(@Param("roleIds") Long[] roleIds,@Param("userName") String userName);

    Set<String> getKbListByRoleId(Long roleId);
}
