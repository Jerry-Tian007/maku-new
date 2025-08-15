package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.RegionTreeSelect;
import com.ruoyi.common.core.domain.entity.SysRegion;

import java.util.List;

/**
 * regionService接口
 * 
 * @author lijw
 * @date 2023-12-02
 */
public interface ISysRegionService 
{
    /**
     * 查询region
     * 
     * @param regionId region主键
     * @return region
     */
    public SysRegion selectSysRegionByRegionId(Long regionId);

    public SysRegion selectSysRegionByRegion(String region);

    /**
     * 查询region列表
     * 
     * @param sysRegion region
     * @return region集合
     */
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion);


    List<RegionTreeSelect> selectRegionTreeList(SysRegion region);


    List<SysRegion> buildRegionTree(List<SysRegion> regions);

    List<RegionTreeSelect> buildRegionRegionTreeSelect(List<SysRegion> regions);

    /**
     * 新增region
     * 
     * @param sysRegion region
     * @return 结果
     */
    public int insertSysRegion(SysRegion sysRegion);

    /**
     * 修改region
     * 
     * @param sysRegion region
     * @return 结果
     */
    public int updateSysRegion(SysRegion sysRegion);

    /**
     * 批量删除region
     * 
     * @param regionIds 需要删除的region主键集合
     * @return 结果
     */
    public int deleteSysRegionByRegionIds(Long[] regionIds);

    /**
     * 删除region信息
     * 
     * @param regionId region主键
     * @return 结果
     */
    public int deleteSysRegionByRegionId(Long regionId);
}
