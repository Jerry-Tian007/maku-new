package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterAddDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterGetDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterPageQryDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterUpdateDto;
import com.ruoyi.gpt.model.vo.user_model_parameter.UserModelParameterInfoVO;
import com.ruoyi.gpt.model.vo.user_model_parameter.UserModelParameterPageVO;

import java.util.List;

/**
 * 
 *
 * @author Eric
 */
public interface IKbUserModelParameterService {

    /**
     * 创建
     *
     * @param userModelParameterAddDto
     * @return
     */
    Integer add(UserModelParameterAddDto userModelParameterAddDto);

    /**
     * 更新
     *
     * @param userModelParameterUpdateDto
     * @return
     */
    Integer update(UserModelParameterUpdateDto userModelParameterUpdateDto);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查询分页信息
     *
     * @param userModelParameterPageQryDto
     * @return
     */
    List<UserModelParameterPageVO> pageList(UserModelParameterPageQryDto userModelParameterPageQryDto);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    UserModelParameterInfoVO getById(Long id);

    UserModelParameterPageVO getParameter(UserModelParameterGetDto userModelParameterGetDto);
}
