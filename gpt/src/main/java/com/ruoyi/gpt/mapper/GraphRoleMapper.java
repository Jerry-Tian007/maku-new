package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.model.domain.GraphRole;
import com.ruoyi.gpt.model.vo.GraphRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GraphRoleMapper {

    public GraphRole selectGraphRoleByGraphIdAndRoleId(@Param("graphId") String graphId, @Param("roleId") Long roleId);

    public int deleteByGraphId(String graphId);

    public int deleteByRoleId(Long roleId);

    public int insert(GraphRole graphRole);

    public int deleteByGraphIdAndRoleId(@Param("roleId") Long roleId, @Param("graphId") String graphId);

    public List<GraphRoleVo> graphRole(Long roleId);

}
