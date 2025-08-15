package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbUserModelParameterMapper;
import com.ruoyi.gpt.model.domain.UserModelParameterEntity;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterAddDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterGetDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterPageQryDto;
import com.ruoyi.gpt.model.dto.user_model_parameter.UserModelParameterUpdateDto;
import com.ruoyi.gpt.model.vo.user_model_parameter.UserModelParameterInfoVO;
import com.ruoyi.gpt.model.vo.user_model_parameter.UserModelParameterPageVO;
import com.ruoyi.gpt.service.IKbUserModelParameterService;
import com.ruoyi.system.service.impl.SysDictDataServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Eric
 */
@Service
public class KbUserModelParameterServiceImpl implements IKbUserModelParameterService {

    @Resource
    private KbUserModelParameterMapper kbUserModelParameterMapper;

    @Resource
    private SysDictDataServiceImpl sysDictDataService;

    /**
     * 创建
     *
     * @param userModelParameterAddDto
     * @return
     */
    @Override
    public Integer add(UserModelParameterAddDto userModelParameterAddDto) {
        UserModelParameterPageVO userModelParameterPageVO = kbUserModelParameterMapper.selectByUserIdAndModeName(SecurityUtils.getUserId(), userModelParameterAddDto.getModel());
        if (userModelParameterPageVO == null) {
            UserModelParameterEntity userModelParameterEntity = BeanConvertUtil.conver(userModelParameterAddDto, UserModelParameterEntity.class);
            userModelParameterEntity.setCreateBy(SecurityUtils.getUsername());
            userModelParameterEntity.setCreateTime(DateUtils.getNowDate());
            return kbUserModelParameterMapper.add(userModelParameterEntity);
        } else {
            UserModelParameterEntity userModelParameterEntity = BeanConvertUtil.conver(userModelParameterAddDto, UserModelParameterEntity.class);
            if (userModelParameterEntity.getPromptId() == null) {
                userModelParameterEntity.setPromptId(-1L);
            }
            userModelParameterEntity.setUserId(SecurityUtils.getUserId());
            userModelParameterEntity.setUpdateBy(SecurityUtils.getUsername());
            userModelParameterEntity.setUpdateTime(DateUtils.getNowDate());
            return kbUserModelParameterMapper.updateByUserIdAndModel(userModelParameterEntity);
        }
    }

    /**
     * 更新
     *
     * @param userModelParameterUpdateDto
     * @return
     */
    @Override
    public Integer update(UserModelParameterUpdateDto userModelParameterUpdateDto) {
        UserModelParameterEntity userModelParameterEntity = BeanConvertUtil.conver(userModelParameterUpdateDto, UserModelParameterEntity.class);
        userModelParameterEntity.setUpdateBy(SecurityUtils.getUsername());
        userModelParameterEntity.setUpdateTime(DateUtils.getNowDate());
        return kbUserModelParameterMapper.updateById(userModelParameterEntity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        return kbUserModelParameterMapper.deleteById(id);
    }

    /**
     * 查询分页信息
     *
     * @param userModelParameterPageQryDto
     * @return
     */
    @Override
    public List<UserModelParameterPageVO> pageList(UserModelParameterPageQryDto userModelParameterPageQryDto) {
        return kbUserModelParameterMapper.pageList(userModelParameterPageQryDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public UserModelParameterInfoVO getById(Long id) {
        return kbUserModelParameterMapper.getById(id);
    }

    @Override
    public UserModelParameterPageVO getParameter(UserModelParameterGetDto userModelParameterGetDto) {
        UserModelParameterPageVO userModelParameterPageVO = kbUserModelParameterMapper.selectByUserIdAndModeName(SecurityUtils.getUserId(), userModelParameterGetDto.getModelName());
        if (userModelParameterPageVO != null) {
            return userModelParameterPageVO;
        } else {
            UserModelParameterPageVO userModelParameterPageVO1 = new UserModelParameterPageVO();
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictLabel(userModelParameterGetDto.getModelName());
            sysDictData.setDictValue(userModelParameterGetDto.getModelName());
            sysDictData.setDictType("model_parameter_template");
            List<SysDictData> userModelParameter = sysDictDataService.selectDictDataList(sysDictData);
            if (userModelParameter == null && userModelParameter.isEmpty()) {
                throw new RuntimeException("模型参数模板不存在,请联系管理员");
            }
            JSONObject promptSetting = new JSONObject();
            promptSetting.set("prompt", "");
            promptSetting.set("Name", "");
            userModelParameterPageVO1.setPromptSetting(promptSetting.toString());
            userModelParameterPageVO1.setConfigureSetting(userModelParameter.get(0).getRemark());
            userModelParameterPageVO1.setModel(userModelParameterGetDto.getModelName());
            userModelParameterPageVO1.setUserId(SecurityUtils.getUserId());
            return userModelParameterPageVO1;
        }
    }
}
