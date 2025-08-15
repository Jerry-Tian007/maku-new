package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.core.domain.RegionTreeSelect;
import com.ruoyi.common.core.domain.entity.SysRegion;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysRegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.ISysRegionService;

/**
 * regionService业务层处理
 * 
 * @author lijw
 * @date 2023-12-02
 */
@Service
public class SysRegionServiceImpl implements ISysRegionService 
{
    @Autowired
    private SysRegionMapper sysRegionMapper;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    /**
     * 查询region
     * 
     * @param regionId region主键
     * @return region
     */
    @Override
    public SysRegion selectSysRegionByRegionId(Long regionId)
    {
        return sysRegionMapper.selectSysRegionByRegionId(regionId);
    }


    /**
     * 查询region
     *
     * @param region
     * @return region
     */
    @Override
    public SysRegion selectSysRegionByRegion(String region)
    {
        return sysRegionMapper.selectSysRegionByRegion(region);
    }

    /**
     * 查询region列表
     * 
     * @param sysRegion region
     * @return region
     */
    @Override
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion)
    {
        return sysRegionMapper.selectSysRegionList(sysRegion);
    }

    @Override
    public List<RegionTreeSelect> selectRegionTreeList(SysRegion region)
    {
        List<SysRegion> regions = sysRegionMapper.selectSysRegionList(region);

        return buildRegionRegionTreeSelect(regions);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param regions 地区列表
     * @return 树结构列表
     */
    @Override
    public List<SysRegion> buildRegionTree(List<SysRegion> regions)
    {
        List<SysRegion> returnList = new ArrayList<SysRegion>();
        List<Long> tempList = regions.stream().map(SysRegion::getRegionId).collect(Collectors.toList());
        for (SysRegion region : regions)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(region.getParentId()))
            {
                recursionFn(regions, region);
                returnList.add(region);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = regions;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param regions 地区列表
     * @return 下拉树结构列表
     */
    @Override
    public List<RegionTreeSelect> buildRegionRegionTreeSelect(List<SysRegion> regions)

    {
        List<SysRegion> regionTrees = buildRegionTree(regions);

        List<RegionTreeSelect> collect = regionTrees.stream().map(RegionTreeSelect::new).collect(Collectors.toList());

        return collect;
    }

    private void recursionFn(List<SysRegion> list, SysRegion t)
    {
        // 得到子节点列表
        List<SysRegion> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysRegion tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysRegion> getChildList(List<SysRegion> list, SysRegion t)
    {
        List<SysRegion> tlist = new ArrayList<SysRegion>();
        Iterator<SysRegion> it = list.iterator();
        while (it.hasNext())
        {
            SysRegion n = (SysRegion) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getRegionId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysRegion> list, SysRegion t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 新增region
     * 
     * @param sysRegion region
     * @return 结果
     */
    @Override
    public int insertSysRegion(SysRegion sysRegion)
    {
        sysRegion.setRegionId(snowflakeGenerator.next());
        return sysRegionMapper.insertSysRegion(sysRegion);
    }

    /**
     * 修改region
     * 
     * @param sysRegion region
     * @return 结果
     */
    @Override
    public int updateSysRegion(SysRegion sysRegion)
    {
        return sysRegionMapper.updateSysRegion(sysRegion);
    }

    /**
     * 批量删除region
     * 
     * @param regionIds 需要删除的region主键
     * @return 结果
     */
    @Override
    public int deleteSysRegionByRegionIds(Long[] regionIds)
    {
        return sysRegionMapper.deleteSysRegionByRegionIds(regionIds);
    }

    /**
     * 删除region信息
     * 
     * @param regionId region主键
     * @return 结果
     */
    @Override
    public int deleteSysRegionByRegionId(Long regionId)
    {
        return sysRegionMapper.deleteSysRegionByRegionId(regionId);
    }



}
