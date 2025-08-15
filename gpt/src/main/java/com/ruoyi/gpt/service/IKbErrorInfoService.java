package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.KbErrorInfo;
import com.ruoyi.gpt.model.vo.kb_error_info.KbErrorInfoVo;

/**
 * 私域错误日志Service接口
 * 
 * @author Eric
 * @date 2023-07-11
 */
public interface IKbErrorInfoService 
{
    /**
     * 查询私域错误日志
     *
     * @param id 私域错误日志主键
     * @return 私域错误日志
     */
    KbErrorInfoVo selectKbErrorInfoById(Long id);

    /**
     * 查询私域错误日志列表
     * 
     * @param kbErrorInfo 私域错误日志
     * @return 私域错误日志集合
     */
    List<KbErrorInfo> selectKbErrorInfoList(KbErrorInfo kbErrorInfo);

    /**
     * 新增私域错误日志
     *
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    Integer insertKbErrorInfo(KbErrorInfo kbErrorInfo);

    /**
     * 修改私域错误日志
     *
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    Integer updateKbErrorInfo(KbErrorInfo kbErrorInfo);

    /**
     * 批量删除私域错误日志
     *
     * @param ids 需要删除的私域错误日志主键集合
     * @return 结果
     */
    Integer deleteKbErrorInfoByIds(Long[] ids);

    /**
     * 删除私域错误日志信息
     * 
     * @param id 私域错误日志主键
     * @return 结果
     */
    int deleteKbErrorInfoById(Long id);
}
