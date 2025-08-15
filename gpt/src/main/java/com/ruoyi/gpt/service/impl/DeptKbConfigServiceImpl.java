package com.ruoyi.gpt.service.impl;

import java.util.List;

import com.ruoyi.gpt.model.vo.DeptKbConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.DeptKbConfigMapper;
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.service.IDeptKbConfigService;

/**
 * 部门知识库配置Service业务层处理
 * 
 * @author Eric
 * @date 2023-05-18
 */
@Service
public class DeptKbConfigServiceImpl implements IDeptKbConfigService 
{
    @Autowired
    private DeptKbConfigMapper deptKbConfigMapper;

    /**
     * 查询部门知识库配置
     * 
     * @param id 部门知识库配置主键
     * @return 部门知识库配置
     */
    @Override
    public DeptKbConfig selectDeptKbConfigById(Long id)
    {
        return deptKbConfigMapper.selectDeptKbConfigById(id);
    }

    /**
     * 查询部门知识库配置列表
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 部门知识库配置
     */
    @Override
    public List<DeptKbConfig> selectDeptKbConfigList(DeptKbConfig deptKbConfig)
    {
        return deptKbConfigMapper.selectDeptKbConfigList(deptKbConfig);
    }

    /**
     * 新增部门知识库配置
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 结果
     */
    @Override
    public int insertDeptKbConfig(DeptKbConfig deptKbConfig)
    {
        return deptKbConfigMapper.insertDeptKbConfig(deptKbConfig);
    }

    /**
     * 修改部门知识库配置
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 结果
     */
    @Override
    public int updateDeptKbConfig(DeptKbConfig deptKbConfig)
    {
        return deptKbConfigMapper.updateDeptKbConfig(deptKbConfig);
    }

    /**
     * 批量删除部门知识库配置
     * 
     * @param ids 需要删除的部门知识库配置主键
     * @return 结果
     */
    @Override
    public int deleteDeptKbConfigByIds(Long[] ids)
    {
        return deptKbConfigMapper.deleteDeptKbConfigByIds(ids);
    }

    /**
     * 删除部门知识库配置信息
     * 
     * @param id 部门知识库配置主键
     * @return 结果
     */
    @Override
    public int deleteDeptKbConfigById(Long id)
    {
        return deptKbConfigMapper.deleteDeptKbConfigById(id);
    }

    @Override
    public DeptKbConfig getDeptKbConfigByDeptIdAndUserId(DeptKbConfigVo deptKbConfigVo) {
        return deptKbConfigMapper.getDeptKbConfigByDeptIdAndUserId(deptKbConfigVo);
    }
}
