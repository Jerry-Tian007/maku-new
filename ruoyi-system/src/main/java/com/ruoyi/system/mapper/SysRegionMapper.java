package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysRegion;

import java.util.List;

/**
 * regionMapper接口
 * 
 * @author lijw
 * @date 2023-12-02
 */
public interface SysRegionMapper 
{
    /**
     * 查询region
     * 
     * @param regionId region主键
     * @return region
     */
    public SysRegion selectSysRegionByRegionId(Long regionId);


    /**
     * 查询region
     *
     * @param region
     * @return region
     */
    public SysRegion selectSysRegionByRegion(String region);

    /**
     * 查询region列表
     * 
     * @param sysRegion region
     * @return region集合
     */
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion);

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
     * 删除region
     * 
     * @param regionId region主键
     * @return 结果
     */
    public int deleteSysRegionByRegionId(Long regionId);

    /**
     * 批量删除region
     * 
     * @param regionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysRegionByRegionIds(Long[] regionIds);
}
