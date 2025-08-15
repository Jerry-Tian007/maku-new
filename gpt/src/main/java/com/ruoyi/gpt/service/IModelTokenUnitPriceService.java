package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.ModelTokenUnitPrice;

/**
 * 模型单位token价格Service接口
 * 
 * @author yecao
 * @date 2023-04-28
 */
public interface IModelTokenUnitPriceService 
{
    /**
     * 查询模型单位token价格
     * 
     * @param id 模型单位token价格主键
     * @return 模型单位token价格
     */
    public ModelTokenUnitPrice selectModelTokenUnitPriceById(Long id);

    /**
     * 查询模型单位token价格列表
     * 
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 模型单位token价格集合
     */
    public List<ModelTokenUnitPrice> selectModelTokenUnitPriceList(ModelTokenUnitPrice modelTokenUnitPrice);

    /**
     * 新增模型单位token价格
     * 
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 结果
     */
    public int insertModelTokenUnitPrice(ModelTokenUnitPrice modelTokenUnitPrice);

    /**
     * 修改模型单位token价格
     * 
     * @param modelTokenUnitPrice 模型单位token价格
     * @return 结果
     */
    public int updateModelTokenUnitPrice(ModelTokenUnitPrice modelTokenUnitPrice);

    /**
     * 批量删除模型单位token价格
     * 
     * @param ids 需要删除的模型单位token价格主键集合
     * @return 结果
     */
    public int deleteModelTokenUnitPriceByIds(Long[] ids);

    /**
     * 删除模型单位token价格信息
     * 
     * @param id 模型单位token价格主键
     * @return 结果
     */
    public int deleteModelTokenUnitPriceById(Long id);
}
