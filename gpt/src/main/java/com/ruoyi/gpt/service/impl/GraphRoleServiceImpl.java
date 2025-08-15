package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.mapper.GraphRoleMapper;
import com.ruoyi.gpt.model.domain.GraphRole;
import com.ruoyi.gpt.model.vo.GraphRoleVo;
import com.ruoyi.gpt.service.IGraphRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GraphRoleServiceImpl implements IGraphRoleService {

    @Resource
    private GraphRoleMapper graphRoleMapper;

    @Override
    public GraphRole selectGraphRoleByGraphIdAndRoleId(String graphId, Long roleId) {
        return graphRoleMapper.selectGraphRoleByGraphIdAndRoleId(graphId, roleId);
    }

    @Override
    public int deleteByGraphId(String graphId) {
        return graphRoleMapper.deleteByGraphId(graphId);
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        return graphRoleMapper.deleteByRoleId(roleId);
    }

    @Override
    public int deleteByGraphIdAndRoleId(Long roleId, String graphId) {
        return graphRoleMapper.deleteByGraphIdAndRoleId(roleId, graphId);
    }

    @Override
    public int insert(GraphRole graphRole) {
        return graphRoleMapper.insert(graphRole);
    }

    @Override
    public List<GraphRoleVo> graphRole(Long roleId) {
        return graphRoleMapper.graphRole(roleId);
    }
}
