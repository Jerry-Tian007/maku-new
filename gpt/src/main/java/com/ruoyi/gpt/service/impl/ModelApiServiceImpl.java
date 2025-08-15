package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ExceptionStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.ModelApiMapper;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourceGetDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.model.vo.model_api.ModelResourceGetVo;
import com.ruoyi.gpt.service.IModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class ModelApiServiceImpl implements IModelApiService {

    @Autowired
    private ModelResourcesServiceImpl modelResourcesService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询模型对应的可用资源
     *
     * @param modelResourceGetDto
     * @return
     */
    @Override
    public ModelResourceGetVo getModelResources(ModelResourceGetDto modelResourceGetDto) {
        ModelChatApiDto modelChatApiDto = new ModelChatApiDto();
        modelChatApiDto.setModelType(modelResourceGetDto.getModelType());
        modelChatApiDto.setScene(modelResourceGetDto.getScene());
        ModelResources modelResources = calTpmMax(modelChatApiDto);
        ModelResourceGetVo modelResourceGetVo = new ModelResourceGetVo();
        modelResourceGetVo.setId(modelResources.getId());
        modelResourceGetVo.setParameter(modelResources.getParameter());
        modelResourceGetVo.setType(modelResources.getType());
        modelResourceGetVo.setName(modelResources.getName());
        return modelResourceGetVo;
    }

    @Override
    public int ModelResourcesTpmUse(ModelResourcesTpmUseDto modelResourceGetDto) {
        redisCache.setCacheObject("model-resource:use:" + modelResourceGetDto.getModelId(), "use", 60, TimeUnit.SECONDS);
        redisCache.setCacheObject("model-resource:" + modelResourceGetDto.getModelId() + ":" + System.currentTimeMillis(), modelResourceGetDto.getToken(), 60, TimeUnit.SECONDS);
        return 1;
    }

    @Override
    public ModelResources calTpmMax(ModelChatApiDto modelChatApiDto) {
        ModelResources modelResources = new ModelResources();
        modelResources.setType(modelChatApiDto.getModelType());
        modelResources.setScene(modelChatApiDto.getScene());
        List<Long> modelResourcesIdList = modelResourcesService.selectModelResourcesIdList(modelResources);
        Collection<String> keys1 = redisCache.keys("model-resource:use:*");

        if (CollectionUtils.isEmpty(keys1)) {
            return modelResourcesService.selectModelResourcesById(modelResourcesIdList.get((int) (Math.random() * modelResourcesIdList.size())));
        }
        List<Long> diff = modelResourcesIdList.stream().filter(ele -> !keys1.contains(String.valueOf("model-resource:use:" + ele))).collect(Collectors.toList());
        if (!diff.isEmpty()) {
            return modelResourcesService.selectModelResourcesById(diff.get((int) (Math.random() * diff.size())));
        }
        String key = "model-resource:";
        HashMap<Long, Integer> map = new HashMap<>();
        for (Long item : modelResourcesIdList) {
            Collection<String> keys = redisCache.keys(key + item + ":*");
            Integer tokens = 0;
            for (String rkey : keys) {
                Integer token = redisCache.getCacheObject(rkey);
                if (token == null) {
                    token = 0;
                }
                tokens += token;
            }
            map.put(item, tokens);
        }
        List<Map.Entry<Long, Integer>> list = new ArrayList(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        Long key1 = list.get(0).getKey();
        return modelResourcesService.selectModelResourcesById(key1);
    }

}
