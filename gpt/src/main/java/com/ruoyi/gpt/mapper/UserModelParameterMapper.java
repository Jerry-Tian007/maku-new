package com.ruoyi.gpt.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.dto.user_model_parameter.*;
import com.ruoyi.gpt.model.vo.user_model_parameter.*;
import com.ruoyi.gpt.model.domain.UserModelParameterEntity;

import java.util.List;

/**
 * @author Eric
 */
public interface UserModelParameterMapper {

    /**
     * 创建
     *
     * @param userModelParameterEntity
     * @return
     */
    @GenerateUniqueId
    Integer add(UserModelParameterEntity userModelParameterEntity);

    /**
     * 更新
     *
     * @param userModelParameterEntity
     * @return
     */
    Integer updateById(UserModelParameterEntity userModelParameterEntity);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

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

    UserModelParameterPageVO selectByUserIdAndModeName(@Param("userId") Long userId, @Param("modelName") String modelName);

    Integer updateByUserIdAndModel(UserModelParameterEntity userModelParameterEntity);
}
