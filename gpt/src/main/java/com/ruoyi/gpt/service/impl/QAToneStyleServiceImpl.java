package com.ruoyi.gpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.QAToneStyleMapper;
import com.ruoyi.gpt.model.domain.QAToneStyle;
import com.ruoyi.gpt.service.IQAToneStyleService;

/**
 * 语气风格Service业务层处理
 * 
 * @author Eric
 * @date 2023-08-21
 */
@Service
public class QAToneStyleServiceImpl implements IQAToneStyleService 
{
    @Autowired
    private QAToneStyleMapper qAToneStyleMapper;

    /**
     * 查询语气风格
     * 
     * @param id 语气风格主键
     * @return 语气风格
     */
    @Override
    public QAToneStyle selectQAToneStyleById(Long id)
    {
        return qAToneStyleMapper.selectQAToneStyleById(id);
    }

    /**
     * 查询语气风格列表
     * 
     * @param qAToneStyle 语气风格
     * @return 语气风格
     */
    @Override
    public List<QAToneStyle> selectQAToneStyleList(QAToneStyle qAToneStyle)
    {
        return qAToneStyleMapper.selectQAToneStyleList(qAToneStyle);
    }

    /**
     * 新增语气风格
     * 
     * @param qAToneStyle 语气风格
     * @return 结果
     */
    @Override
    public int insertQAToneStyle(QAToneStyle qAToneStyle)
    {
        qAToneStyle.setCreateTime(DateUtils.getNowDate());
        return qAToneStyleMapper.insertQAToneStyle(qAToneStyle);
    }

    /**
     * 修改语气风格
     * 
     * @param qAToneStyle 语气风格
     * @return 结果
     */
    @Override
    public int updateQAToneStyle(QAToneStyle qAToneStyle)
    {
        return qAToneStyleMapper.updateQAToneStyle(qAToneStyle);
    }

    /**
     * 批量删除语气风格
     * 
     * @param ids 需要删除的语气风格主键
     * @return 结果
     */
    @Override
    public int deleteQAToneStyleByIds(Long[] ids)
    {
        return qAToneStyleMapper.deleteQAToneStyleByIds(ids);
    }

    /**
     * 删除语气风格信息
     * 
     * @param id 语气风格主键
     * @return 结果
     */
    @Override
    public int deleteQAToneStyleById(Long id)
    {
        return qAToneStyleMapper.deleteQAToneStyleById(id);
    }

    @Override
    public QAToneStyle getQAToneStyleDetail(QAToneStyle qAToneStyle) {
        QAToneStyle qaToneStyleDetail = qAToneStyleMapper.getQAToneStyleDetail(qAToneStyle);
        if (qaToneStyleDetail == null ){
            QAToneStyle result = new QAToneStyle();
            result.setStyle(qAToneStyle.getStyle());
            result.setModel(qAToneStyle.getModel());
            result.setTemperature(qAToneStyle.getModel());
            result.setTemperature("0.7");
            return result;
        }
        return qaToneStyleDetail;
    }
}
