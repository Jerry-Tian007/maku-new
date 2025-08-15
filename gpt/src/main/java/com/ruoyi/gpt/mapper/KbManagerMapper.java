package com.ruoyi.gpt.mapper;


import com.ruoyi.gpt.model.domain.KbManager;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerInfoVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库管理Mapper接口
 *
 */
public interface KbManagerMapper {

    Integer insertKbManager(@Param("kbManager") KbManager kbManager);

    KbManager selectInfoById(@Param("id") Long id);

    KbManagerInfoVo checkByKbName(@Param("kbName")String kbName);

    Integer delete(@Param("kbManager") KbManager kbManager);

    List<KbManagerPageVo> list(@Param("kbManagerPageDto") KbManagerPageDto kbManagerPageDto,
                               @Param("roleIds") Long[] roleIds,
                               @Param("userName") String userName);

    Integer updateKbManager(@Param("kbManager") KbManager kbManager);

    List<KbManager> selectKbManagerListByIds(@Param("ids") List<Long> ids);

    Integer removeBath(List<Long> ids);
}
