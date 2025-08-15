package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * region对象 sys_region
 * 
 * @author lijw
 * @date 2023-12-02
 */
public class SysRegion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 国家id */
    @Excel(name = "国家id")
    private Long regionId;

    /** 国家 */
    @Excel(name = "国家")
    private String region;

    private Long parentId;

    private String ancestors;


    /** 子地区 */
    private List<SysRegion> children = new ArrayList<SysRegion>();

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }



    public List<SysRegion> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysRegion> children)
    {
        this.children = children;
    }
}
