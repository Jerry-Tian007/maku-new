package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.model.domain.ModelTokenUnitPrice;
import com.ruoyi.gpt.mapper.ModelTokenUnitPriceMapper;
import com.ruoyi.gpt.service.IModelTokenUnitPriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模型单位token价格Service业务层处理
 *
 * @author yecao
 * @date 2023-04-28
 */
@Service
public class ModelTokenUnitPriceServiceImpl implements IModelTokenUnitPriceService
{
    @Resource
    private ModelTokenUnitPriceMapper modelTokenUnitPriceMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询模型单位token价格
     *
     * @param id 模型单位token价格主键
     * @return 模型单位token价格
     */
    @Override
    public ModelTokenUnitPrice selectModelTokenUnitPriceById(Long id)
    {
        return modelTokenUnitPriceMapper.selectModelTokenUnitPriceById(id);
    }

    /**
     * 查询模型单位token价格列表
     *
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 模型单位token价格
     */
    @Override
    public List<ModelTokenUnitPrice> selectModelTokenUnitPriceList(ModelTokenUnitPrice modelTokenUnitPrice)
    {
        return modelTokenUnitPriceMapper.selectModelTokenUnitPriceList(modelTokenUnitPrice);
    }

    /**
     * 新增模型单位token价格
     *
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 结果
     */
    @Override
    public int insertModelTokenUnitPrice(ModelTokenUnitPrice modelTokenUnitPrice)
    {
        modelTokenUnitPrice.setCreateTime(DateUtils.getNowDate());
        redisCache.setCacheObject(modelTokenUnitPrice.getModel(), modelTokenUnitPrice.getUnitModelTokenPrice().toString());
        return modelTokenUnitPriceMapper.insertModelTokenUnitPrice(modelTokenUnitPrice);
    }

    /**
     * 修改模型单位token价格
     *
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 结果
     */
    @Override
    public int updateModelTokenUnitPrice(ModelTokenUnitPrice modelTokenUnitPrice)
    {
        modelTokenUnitPrice.setUpdateTime(DateUtils.getNowDate());
        redisCache.setCacheObject(modelTokenUnitPrice.getModel(), modelTokenUnitPrice.getUnitModelTokenPrice().toString());
        return modelTokenUnitPriceMapper.updateModelTokenUnitPrice(modelTokenUnitPrice);
    }

    /**
     * 批量删除模型单位token价格
     *
     * @param ids 需要删除的模型单位token价格主键
     * @return 结果
     */
    @Override
    public int deleteModelTokenUnitPriceByIds(Long[] ids)
    {
        for (Long id : ids) {
            ModelTokenUnitPrice modelTokenUnitPrice = modelTokenUnitPriceMapper.selectModelTokenUnitPriceById(id);
            redisCache.deleteObject(modelTokenUnitPrice.getModel());
        }
        return modelTokenUnitPriceMapper.deleteModelTokenUnitPriceByIds(ids);
    }

    /**
     * 删除模型单位token价格信息
     *
     * @param id 模型单位token价格主键
     * @return 结果
     */
    @Override
    public int deleteModelTokenUnitPriceById(Long id)
    {
        ModelTokenUnitPrice modelTokenUnitPrice = modelTokenUnitPriceMapper.selectModelTokenUnitPriceById(id);
        redisCache.deleteObject(modelTokenUnitPrice.getModel());
        return modelTokenUnitPriceMapper.deleteModelTokenUnitPriceById(id);
    }
}
