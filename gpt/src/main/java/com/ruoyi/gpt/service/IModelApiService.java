package com.ruoyi.gpt.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourceGetDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.model.vo.model_api.ModelResourceGetVo;


public interface IModelApiService {

    /**
     * 查询模型对应的可用资源
     *
     * @param modelResourceGetDto
     * @return
     */
    ModelResourceGetVo getModelResources(ModelResourceGetDto modelResourceGetDto);

    ModelResources calTpmMax(ModelChatApiDto modelChatApiDto);

    int ModelResourcesTpmUse(ModelResourcesTpmUseDto modelResourceGetDto);
}
