package com.ruoyi.gpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.model.vo.kb_error_info.KbErrorInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.KbErrorInfoMapper;
import com.ruoyi.gpt.model.domain.KbErrorInfo;
import com.ruoyi.gpt.service.IKbErrorInfoService;

/**
 * 私域错误日志Service业务层处理
 * 
 * @author Eric
 * @date 2023-07-11
 */
@Service
public class KbErrorInfoServiceImpl implements IKbErrorInfoService 
{
    @Autowired
    private KbErrorInfoMapper kbErrorInfoMapper;

    /**
     * 查询私域错误日志
     *
     * @param id 私域错误日志主键
     * @return 私域错误日志
     */
    @Override
    public KbErrorInfoVo selectKbErrorInfoById(Long id) {
        KbErrorInfo kbErrorInfo = kbErrorInfoMapper.selectKbErrorInfoById(id);
        KbErrorInfoVo kbErrorInfoVo = BeanConvertUtil.conver(kbErrorInfo, KbErrorInfoVo.class);
        return kbErrorInfoVo;
    }

    /**
     * 查询私域错误日志列表
     * 
     * @param kbErrorInfo 私域错误日志
     * @return 私域错误日志
     */
    @Override
    public List<KbErrorInfo> selectKbErrorInfoList(KbErrorInfo kbErrorInfo)
    {
        return kbErrorInfoMapper.selectKbErrorInfoList(kbErrorInfo);
    }

    /**
     * 新增私域错误日志
     *
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    @Override
    public Integer insertKbErrorInfo(KbErrorInfo kbErrorInfo)
    {
//        kbErrorInfo.setDeptId(SecurityUtils.getDeptId());
//        kbErrorInfo.setUserId(SecurityUtils.getUserId());
//        kbErrorInfo.setCreateBy("OA推送");
        kbErrorInfo.setCreateTime(DateUtils.getNowDate());
        return kbErrorInfoMapper.insertKbErrorInfo(kbErrorInfo);
    }

    /**
     * 修改私域错误日志
     *
     * @param kbErrorInfo 私域错误日志
     * @return 结果
     */
    @Override
    public Integer updateKbErrorInfo(KbErrorInfo kbErrorInfo)
    {
        return kbErrorInfoMapper.updateKbErrorInfo(kbErrorInfo);
    }

    /**
     * 批量删除私域错误日志
     *
     * @param ids 需要删除的私域错误日志主键
     * @return 结果
     */
    @Override
    public Integer deleteKbErrorInfoByIds(Long[] ids)
    {
        return kbErrorInfoMapper.deleteKbErrorInfoByIds(ids);
    }

    /**
     * 删除私域错误日志信息
     * 
     * @param id 私域错误日志主键
     * @return 结果
     */
    @Override
    public int deleteKbErrorInfoById(Long id)
    {
        return kbErrorInfoMapper.deleteKbErrorInfoById(id);
    }
}
