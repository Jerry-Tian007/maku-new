package com.ruoyi.gpt.mapper;

import java.util.List;
import com.ruoyi.gpt.model.domain.KbErrorInfo;

/**
 * 私域错误日志Mapper接口
 * 
 * @author Eric
 * @date 2023-07-11
 */
public interface KbErrorInfoMapper 
{
    /**
     * 查询私域错误日志
     * 
     * @param id 私域错误日志主键
     * @return 私域错误日志
     */
    public KbErrorInfo selectKbErrorInfoById(Long id);

    /**
     * 查询私域错误日志列表
     * 
     * @param kbErrorInfo 私域错误日志
     * @return 私域错误日志集合
     */
    public List<KbErrorInfo> selectKbErrorInfoList(KbErrorInfo kbErrorInfo);

    /**
     * 新增私域错误日志
     * 
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    public int insertKbErrorInfo(KbErrorInfo kbErrorInfo);

    /**
     * 修改私域错误日志
     * 
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    public int updateKbErrorInfo(KbErrorInfo kbErrorInfo);

    /**
     * 删除私域错误日志
     * 
     * @param id 私域错误日志主键
     * @return 结果
     */
    public int deleteKbErrorInfoById(Long id);

    /**
     * 批量删除私域错误日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKbErrorInfoByIds(Long[] ids);
}
