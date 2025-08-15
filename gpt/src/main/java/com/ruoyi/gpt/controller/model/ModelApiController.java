package com.ruoyi.gpt.controller.model;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.gpt.core.annotation.ModelResourcesAuthentication;
import com.ruoyi.gpt.model.dto.model_api.ModelResourceGetDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.model.vo.model_api.ModelResourceGetVo;
import com.ruoyi.gpt.service.IModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/model/model-api")
public class ModelApiController {
    @Autowired
    private IModelApiService modelApiService;

    /**
     * 查询模型对应的可用资源
     *
     * @param modelResourceGetDto
     * @return
     */
    @ModelResourcesAuthentication
    @PostMapping("/model-resource/get")
    public R<ModelResourceGetVo> getModelResources(@RequestBody ModelResourceGetDto modelResourceGetDto) {
        return R.success(modelApiService.getModelResources(modelResourceGetDto));
    }

    @PostMapping("/model-resource/tpm")
    public R<Integer> ModelResourcesTpmUse(@RequestBody ModelResourcesTpmUseDto modelResourceGetDto) {
        return R.success(modelApiService.ModelResourcesTpmUse(modelResourceGetDto));
    }

}
