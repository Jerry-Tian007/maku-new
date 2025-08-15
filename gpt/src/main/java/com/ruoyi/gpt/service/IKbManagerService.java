package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.kb_manager.KbManagerInsertDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerUpdateDto;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerInfoVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;

import java.util.List;

/**
 * 知识库管理Service接口
 *
 */
public interface IKbManagerService {

    /**
     * 新增知识库数据
     *
     * @param kbManagerInsertDto
     * @return
     */
    Integer insertKbManager(KbManagerInsertDto kbManagerInsertDto);

    /**
     * 修改知识库数据
     *
     * @param kbManagerUpdateDto
     * @return
     */
    Integer updateKbManager(KbManagerUpdateDto kbManagerUpdateDto);

    /**
     * 根据id删除知识库数据
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 根据id查询知识库数据详情
     *
     * @param id
     * @return
     */
    KbManagerInfoVo selectInfoById(Long id);

    /**
     * 知识库数据列表查询
     *
     * @param kbManagerPageDto
     * @return
     */
    List<KbManagerPageVo> list(KbManagerPageDto kbManagerPageDto);

    Integer removeBath(List<Long> ids);
}
