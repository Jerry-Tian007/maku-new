package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.enums.ExceptionStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.common.utils.encrypt.AESUtils;
import com.ruoyi.gpt.mapper.ModelResourceRoleMapper;
import com.ruoyi.gpt.mapper.ModelResourcesMapper;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesInsertDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesListDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesUpdateDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesAuthorizeUseVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesListVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
import com.ruoyi.gpt.service.IModelResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 模型资源管理Service业务层处理
 *
 * @author Eric
 * @date 2024-07-09
 */
@Service
public class ModelResourcesServiceImpl implements IModelResourcesService {
    @Autowired
    private ModelResourcesMapper modelResourcesMapper;

    @Autowired
    private ModelResourceRoleMapper modelResourceRoleMapper;

    /**
     * 查询模型资源管理
     *
     * @param id 模型资源管理主键
     * @return 模型资源管理
     */
    @Override
    public ModelResources selectModelResourcesById(Long id) {
        return modelResourcesMapper.selectModelResourcesById(id);
    }


    /**
     * 查询模型资源管理列表
     *
     * @param modelResources 模型资源管理
     * @return 模型资源管理
     */
    @Override
    public List<ModelResources> selectModelResourcesList(ModelResources modelResources) {
        return modelResourcesMapper.selectModelResourcesList(modelResources);
    }

    /**
     * 新增模型资源管理
     *
     * @param modelResourcesInsertDto 模型资源管理
     * @return 结果
     */
    @Override
    public int insertModelResources(ModelResourcesInsertDto modelResourcesInsertDto) {
        String encryptParameter = encrypt(modelResourcesInsertDto.getParameter());
        modelResourcesInsertDto.setParameter(encryptParameter);
        if (!this.verificationModelResources(modelResourcesInsertDto)) {
            throw new ServiceException(ExceptionStatusEnum.REPEAT);
        }
        ModelResources modelResources = BeanConvertUtil.conver(modelResourcesInsertDto, ModelResources.class);
        modelResources.setCreateTime(DateUtils.getNowDate());
        modelResources.setCreateBy(SecurityUtils.getUsername());
        return modelResourcesMapper.insertModelResources(modelResources);
    }

    /**
     * 修改模型资源管理
     *
     * @param modelResourcesUpdateDto 模型资源管理
     * @return 结果
     */
    @Override
    public int updateModelResources(ModelResourcesUpdateDto modelResourcesUpdateDto) {
        ModelResources modelResources1 = modelResourcesMapper.selectModelResourcesById(modelResourcesUpdateDto.getId());
        if (modelResources1 == null) {
            throw new ServiceException("数据不存在，修改失败");
        }
        String parameter = modelResourcesUpdateDto.getParameter();
        JSONArray entries = JSONUtil.parseArray(parameter);
        String encryptValue = "";
        for (int i = 0; i < entries.size(); i++) {
            JSONObject item = (JSONObject) entries.get(i);
            if (item.getInt("encrypt") == 1 && item.getJSONArray("value").size() > 0) {
                JSONArray value = item.getJSONArray("value");
                encryptValue = value.get(0).toString();
                break;
            }
        }
        String parameterOld = modelResources1.getParameter();
        JSONArray entriesOld = JSONUtil.parseArray(parameterOld);
        String encryptValueOld = "";
        for (int i = 0; i < entriesOld.size(); i++) {
            JSONObject item = (JSONObject) entriesOld.get(i);
            if (item.getInt("encrypt") == 1 && item.getJSONArray("value").size() > 0) {
                JSONArray value = item.getJSONArray("value");
                encryptValueOld = value.get(0).toString();
                break;
            }
        }

        ModelResources modelResources = BeanConvertUtil.conver(modelResourcesUpdateDto, ModelResources.class);
        modelResources.setUpdateTime(DateUtils.getNowDate());
        modelResources.setUpdateBy(SecurityUtils.getUsername());
        if (!encryptValue.equals(encryptValueOld)) {
            String encryptParameter = encrypt(parameter);
            modelResources.setParameter(encryptParameter);
            return modelResourcesMapper.updateModelResources(modelResources);
        } else {
            return modelResourcesMapper.updateModelResources(modelResources);
        }


    }

    /**
     * 批量删除模型资源管理
     *
     * @param ids 需要删除的模型资源管理主键
     * @return 结果
     */
    @Override
    public int deleteModelResourcesByIds(Long[] ids) {
        for (Long id : ids) {
            ModelResources modelResources = new ModelResources();
            modelResources.setDeleteBy(SecurityUtils.getUsername());
            modelResources.setDeleteTime(DateUtils.getNowDate());
            modelResources.setDelFlag("2");
            modelResources.setId(id);
            modelResourcesMapper.deleteModelResources(modelResources);
        }
        return 1;
    }

    @Override
    public List<ModelResourcesListVo> selectModelResourcesListForPlatform(ModelResourcesListDto modelResourcesListDto) {
        return modelResourcesMapper.selectModelResourcesListForPlatform(modelResourcesListDto);
    }

    @Override
    public boolean verificationModelResources(ModelResourcesInsertDto modelResourcesInsertDto) {
        ModelResources modelResources = modelResourcesMapper.verificationModelResources(modelResourcesInsertDto);
        if (modelResources == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String encrypt(String parameter) {
        JSONArray entries = JSONUtil.parseArray(parameter);

        entries.forEach(entryObj -> {
            JSONObject item = (JSONObject) entryObj;
            if (item.getInt("encrypt") == 1 && item.getJSONArray("value").size() > 0) {
                JSONArray value = item.getJSONArray("value");
                JSONArray rValue = new JSONArray();
                // 对 value 数组进行处理
                value.forEach(valueItem -> {
                    // 对 value 数组中的每个元素进行操作
                    try {
                        String encryptedValue = AESUtils.encrypt(String.valueOf(valueItem));
                        rValue.add(encryptedValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                item.set("value", rValue);
            }
        });
        return entries.toString();
    }

    @Override
    public List<ModelResourcesAuthorizeUseVo> getAuthorizeModelResourcesList() {
        Set<String> modelResources = SecurityUtils.getLoginUser().getModelResources();
        if (modelResources.contains("all")) {
            return modelResourcesMapper.getAllModelResourcesList();
        }
        return modelResourcesMapper.getAuthorizeModelResourcesList(modelResources);
    }

//    @Override
//    public List<ModelResourcesTokenUseVo> getModelResourcesToken(Set<String> modelResources) {
//        return modelResourcesMapper.getModelResourcesToken(modelResources);
//    }

    public List<Long> selectModelResourcesIdList(ModelResources modelResources) {
        return modelResourcesMapper.selectModelResourcesIdList(modelResources);
    }
}
