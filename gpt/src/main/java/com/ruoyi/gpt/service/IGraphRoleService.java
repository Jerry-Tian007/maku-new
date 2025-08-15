package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.GraphRole;
import com.ruoyi.gpt.model.vo.GraphRoleVo;

import java.util.List;

public interface IGraphRoleService {

    public GraphRole selectGraphRoleByGraphIdAndRoleId(String graphId, Long roleId);

    public int deleteByGraphId(String graphId);

    public int deleteByRoleId(Long roleId);

    public int deleteByGraphIdAndRoleId(Long roleId, String graphId);

    public int insert(GraphRole graphRole);

    public List<GraphRoleVo> graphRole(Long roleId);

}
