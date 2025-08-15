package com.ruoyi.common.core.domain;



import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRegion;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
public class RegionTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<RegionTreeSelect> children;

    public RegionTreeSelect()
    {

    }
//
//    public RegionTreeSelect(SysDept dept)
//    {
//        this.id = dept.getDeptId();
//        this.label = dept.getDeptName();
//        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
//    }
//
//    public RegionTreeSelect(SysMenu menu)
//    {
//        this.id = menu.getMenuId();
//        this.label = menu.getMenuName();
//        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
//    }


    public RegionTreeSelect(SysRegion sysRegion)
    {
        this.id = sysRegion.getRegion();
        this.label = sysRegion.getRegion();
        this.children = sysRegion.getChildren().stream().map(RegionTreeSelect::new).collect(Collectors.toList());
    }


    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<RegionTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<RegionTreeSelect> children)
    {
        this.children = children;
    }
}

