package com.ruoyi.gpt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ruoyi.gpt.model.dto.user_model_parameter.*;
import com.ruoyi.gpt.model.vo.user_model_parameter.*;
import com.ruoyi.gpt.mapper.UserModelParameterMapper;
import java.util.List;

import com.ruoyi.gpt.model.domain.UserModelParameterEntity;

/**
 * 
 *
 * @author Eric
 */
public interface IUserModelParameterService {

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
