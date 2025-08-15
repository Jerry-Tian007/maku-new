package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.mapper.DeptAdInfoMapper;
import com.ruoyi.gpt.model.domain.DeptAdInfo;
import com.ruoyi.gpt.service.IDeptAdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门ad域信息Service业务层处理
 * 
 * @author Eric
 * @date 2023-05-09
 */
@Service
public class DeptAdInfoServiceImpl implements IDeptAdInfoService 
{
    @Autowired
    private DeptAdInfoMapper deptAdInfoMapper;

    /**
     * 查询部门ad域信息
     * 
     * @param id 部门ad域信息主键
     * @return 部门ad域信息
     */
    @Override
    public DeptAdInfo selectDeptAdInfoById(Long id)
    {
        return deptAdInfoMapper.selectDeptAdInfoById(id);
    }

    /**
     * 查询部门ad域信息列表
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 部门ad域信息
     */
    @Override
    public List<DeptAdInfo> selectDeptAdInfoList(DeptAdInfo deptAdInfo)
    {
        return deptAdInfoMapper.selectDeptAdInfoList(deptAdInfo);
    }

    /**
     * 新增部门ad域信息
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 结果
     */
    @Override
    public int insertDeptAdInfo(DeptAdInfo deptAdInfo)
    {
        return deptAdInfoMapper.insertDeptAdInfo(deptAdInfo);
    }

    /**
     * 修改部门ad域信息
     * 
     * @param deptAdInfo 部门ad域信息
     * @return 结果
     */
    @Override
    public int updateDeptAdInfo(DeptAdInfo deptAdInfo)
    {
        return deptAdInfoMapper.updateDeptAdInfo(deptAdInfo);
    }

    /**
     * 批量删除部门ad域信息
     * 
     * @param ids 需要删除的部门ad域信息主键
     * @return 结果
     */
    @Override
    public int deleteDeptAdInfoByIds(Long[] ids)
    {
        return deptAdInfoMapper.deleteDeptAdInfoByIds(ids);
    }

    /**
     * 删除部门ad域信息信息
     * 
     * @param id 部门ad域信息主键
     * @return 结果
     */
    @Override
    public int deleteDeptAdInfoById(Long id)
    {
        return deptAdInfoMapper.deleteDeptAdInfoById(id);
    }

    @Override
    public DeptAdInfo selectDeptAdInfoByAdUsernameSuffix(String s) {
        return deptAdInfoMapper.selectDeptAdInfoByAdUsernameSuffix(s);
    }
}
