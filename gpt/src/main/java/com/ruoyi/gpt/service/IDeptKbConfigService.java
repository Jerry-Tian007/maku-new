package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.model.vo.DeptKbConfigVo;

/**
 * 部门知识库配置Service接口
 * 
 * @author Eric
 * @date 2023-05-18
 */
public interface IDeptKbConfigService 
{
    /**
     * 查询部门知识库配置
     * 
     * @param id 部门知识库配置主键
     * @return 部门知识库配置
     */
    public DeptKbConfig selectDeptKbConfigById(Long id);

    /**
     * 查询部门知识库配置列表
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 部门知识库配置集合
     */
    public List<DeptKbConfig> selectDeptKbConfigList(DeptKbConfig deptKbConfig);

    /**
     * 新增部门知识库配置
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 结果
     */
    public int insertDeptKbConfig(DeptKbConfig deptKbConfig);

    /**
     * 修改部门知识库配置
     * 
     * @param deptKbConfig 部门知识库配置
     * @return 结果
     */
    public int updateDeptKbConfig(DeptKbConfig deptKbConfig);

    /**
     * 批量删除部门知识库配置
     * 
     * @param ids 需要删除的部门知识库配置主键集合
     * @return 结果
     */
    public int deleteDeptKbConfigByIds(Long[] ids);

    /**
     * 删除部门知识库配置信息
     * 
     * @param id 部门知识库配置主键
     * @return 结果
     */
    public int deleteDeptKbConfigById(Long id);

    DeptKbConfig getDeptKbConfigByDeptIdAndUserId(DeptKbConfigVo deptKbConfigVo);
}
