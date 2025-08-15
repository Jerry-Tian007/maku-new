package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.common.utils.encrypt.AESUtils;
import com.ruoyi.gpt.mapper.ModelResourceClientMapper;
import com.ruoyi.gpt.mapper.ModelResourceRoleMapper;
import com.ruoyi.gpt.mapper.ModelResourcesMapper;
import com.ruoyi.gpt.model.domain.ModelResourceClient;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesInsertDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesListDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesUpdateDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesListVo;
import com.ruoyi.gpt.service.IModelResourceClientService;
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
public class ModelResourceClientServiceImpl implements IModelResourceClientService
{

    @Autowired
    private ModelResourceClientMapper modelResourceClientMapper;
    @Override
    public Set<String> selectModelResourceClientList() {
        return modelResourceClientMapper.selectModelResourceClientList();
    }
}
