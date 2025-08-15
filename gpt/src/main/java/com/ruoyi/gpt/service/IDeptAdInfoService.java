package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.DeptAdInfo;

import java.util.List;

/**
 * 部门ad域信息Service接口
 * 
 * @author Eric
 * @date 2023-05-09
 */
public interface IDeptAdInfoService 
{
    /**
     * 查询部门ad域信息
     * 
     * @param id 部门ad域信息主键
     * @return 部门ad域信息
     */
    public DeptAdInfo selectDeptAdInfoById(Long id);

    /**
     * 查询部门ad域信息列表
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 部门ad域信息集合
     */
    public List<DeptAdInfo> selectDeptAdInfoList(DeptAdInfo deptAdInfo);

    /**
     * 新增部门ad域信息
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 结果
     */
    public int insertDeptAdInfo(DeptAdInfo deptAdInfo);

    /**
     * 修改部门ad域信息
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 结果
     */
    public int updateDeptAdInfo(DeptAdInfo deptAdInfo);

    /**
     * 批量删除部门ad域信息
     * 
     * @param ids 需要删除的部门ad域信息主键集合
     * @return 结果
     */
    public int deleteDeptAdInfoByIds(Long[] ids);

    /**
     * 删除部门ad域信息信息
     * 
     * @param id 部门ad域信息主键
     * @return 结果
     */
    public int deleteDeptAdInfoById(Long id);

    DeptAdInfo selectDeptAdInfoByAdUsernameSuffix(String s);
}
