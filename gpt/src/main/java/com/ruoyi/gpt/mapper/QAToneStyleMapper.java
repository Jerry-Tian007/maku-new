package com.ruoyi.gpt.mapper;

import java.util.List;
import com.ruoyi.gpt.model.domain.QAToneStyle;

/**
 * 语气风格Mapper接口
 * 
 * @author Eric
 * @date 2023-08-21
 */
public interface QAToneStyleMapper 
{
    /**
     * 查询语气风格
     * 
     * @param id 语气风格主键
     * @return 语气风格
     */
    public QAToneStyle selectQAToneStyleById(Long id);

    /**
     * 查询语气风格列表
     * 
     * @param qAToneStyle 语气风格
     * @return 语气风格集合
     */
    public List<QAToneStyle> selectQAToneStyleList(QAToneStyle qAToneStyle);

    /**
     * 新增语气风格
     * 
     * @param qAToneStyle 语气风格
     * @return 结果
     */
    public int insertQAToneStyle(QAToneStyle qAToneStyle);

    /**
     * 修改语气风格
     * 
     * @param qAToneStyle 语气风格
     * @return 结果
     */
    public int updateQAToneStyle(QAToneStyle qAToneStyle);

    /**
     * 删除语气风格
     * 
     * @param id 语气风格主键
     * @return 结果
     */
    public int deleteQAToneStyleById(Long id);

    /**
     * 批量删除语气风格
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQAToneStyleByIds(Long[] ids);

    QAToneStyle getQAToneStyleDetail(QAToneStyle qAToneStyle);
}
