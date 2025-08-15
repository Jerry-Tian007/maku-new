package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.GraphInfoMapper;
import com.ruoyi.gpt.model.domain.GraphInfo;
import com.ruoyi.gpt.model.domain.GraphRole;
import com.ruoyi.gpt.model.dto.graph.GraphAuthDto;
import com.ruoyi.gpt.model.dto.graph.GraphInfoListDto;
import com.ruoyi.gpt.model.vo.GraphRoleVo;
import com.ruoyi.gpt.service.IGraphInfoService;
import com.ruoyi.gpt.service.IGraphRoleService;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识图谱信息Service业务层处理
 *
 * @author yecao
 * @date 2025-04-09
 */
@Slf4j
@Service
public class GraphInfoServiceImpl implements IGraphInfoService {

    @Resource
    private GraphInfoMapper graphInfoMapper;

    @Resource
    private IGraphRoleService graphRoleService;

    @Resource
    private GraphInfoServiceImpl graphInfoService;

    @Resource
    private SysUserServiceImpl sysUserService;

    /**
     * 查询所有的图谱id
     *
     * @return 图谱id集合
     */
    @Override
    public List<String> selectAllSpaceIds() {
        return graphInfoMapper.selectAllSpaceIds();
    }

    @Override
    public List<GraphRoleVo> graphRole(Long roleId) {
        return graphRoleService.graphRole(roleId);
    }

    @Override
    public List<GraphInfo> userGraphRole(Long userId) {
        SysUser sysUser = sysUserService.selectUserById(userId);
        List<Long> roleIds = sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList());
        List<GraphInfo> dataList = new ArrayList<>();
        if (StringUtils.isNotEmpty(roleIds)) {
            roleIds.forEach(roleId -> {
                List<GraphInfo> graphInfos = graphInfoService.selectGraphInfoByRoleId(roleId);
                dataList.addAll(graphInfos);
            });

            // 去重
            return dataList.stream().distinct()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 查询知识图谱信息
     *
     * @param spaceId 知识图谱空间id
     * @return 结果
     */
    @Override
    public GraphInfo selectGraphInfoBySpaceId(String spaceId) {
        return graphInfoMapper.selectGraphInfoBySpaceId(spaceId);
    }

    @Override
    public List<GraphInfo> selectGraphInfoByRoleId(Long roleId) {
        return graphInfoMapper.selectGraphInfoByRoleId(roleId);
    }

    /**
     * 平台查询知识图谱信息列表
     *
     * @param graphInfoListDto 知识图谱信息
     * @return 知识图谱信息
     */
    @Override
    public List<GraphInfo> selectGraphInfoListForPlatform(GraphInfoListDto graphInfoListDto) {
        GraphInfo graphInfo = BeanConvertUtil.conver(graphInfoListDto, GraphInfo.class);
        return graphInfoMapper.selectGraphInfoList(graphInfo);
    }

    /**
     * 查询知识图谱信息列表
     *
     * @param graphInfo 知识图谱信息
     * @return 知识图谱信息
     */
    @Override
    public List<GraphInfo> selectGraphInfoList(GraphInfo graphInfo) {
        return graphInfoMapper.selectGraphInfoList(graphInfo);
    }

    /**
     * 同步知识图谱数据
     *
     * @return 结果
     */
    @Override
    public int syncGraphInfo() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                DictUtils.getDictValue("graph_conf", "url"),
                String.class
        );

        if (response.getStatusCode().value() != 200) {
            return -1;
        }

        // 获取接口返回的所有spaceId
        JSONArray datalist = new JSONArray(response.getBody());
        Set<String> apiSpaceIds = new HashSet<>();

        // 处理新增、更新逻辑
        datalist.forEach(graph -> {
            try {
                JSONObject graphJSON = new JSONObject(graph);
                GraphInfo graphInfo = JSONUtil.toBean(graphJSON, GraphInfo.class);

                // 收集接口返回的spaceId
                apiSpaceIds.add(graphInfo.getSpaceId());

                // 原有更新逻辑
                GraphInfo temp = selectGraphInfoBySpaceId(graphInfo.getSpaceId());
                if (StringUtils.isNull(temp)) {
                    insertGraphInfo(graphInfo);
                } else {
                    graphInfo.setUpdateTime(DateUtils.getNowDate());
                    updateGraphInfo(graphInfo);
                }
            } catch (Exception e) {
                log.error("同步图数据异常: {}", e.getMessage());
            }
        });

        // 删除逻辑
        try {
            // 1. 获取数据库所有现有spaceId
            List<String> dbSpaceIds = selectAllSpaceIds();

            // 2. 计算需要删除的spaceId（数据库存在但接口不包含的）
            List<String> toDelete = dbSpaceIds.stream()
                    .filter(spaceId -> !apiSpaceIds.contains(spaceId))
                    .collect(Collectors.toList());

            // 3. 执行删除操作
            if (!toDelete.isEmpty()) {
                log.info("需要删除的过期spaceIds: {}", toDelete);

                // 删除知识图谱同时删除授权关系
                graphInfoService.deleteGraphInfosBySpaceIds(toDelete);
            }
        } catch (Exception e) {
            log.error("删除过期数据异常: {}", e.getMessage());
            return -1;
        }

        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int auth(GraphAuthDto graphAuthDto) {
        if (StringUtils.isNull(graphAuthDto.getRoleId())) {
            return -1;
        }

        graphRoleService.deleteByRoleId(graphAuthDto.getRoleId());

        graphAuthDto.getGraphIdList().forEach(graphId -> {
            GraphRole graphRole = new GraphRole();
            graphRole.setRoleId(graphAuthDto.getRoleId());
            graphRole.setGraphId(graphId);
            graphRoleService.insert(graphRole);
        });
        return 1;
    }

    /**
     * 新增知识图谱信息
     *
     * @param graphInfo 知识图谱信息
     * @return 结果
     */
    @Override
    public int insertGraphInfo(GraphInfo graphInfo) {
        graphInfo.setSyncTime(DateUtils.getNowDate());
        return graphInfoMapper.insertGraphInfo(graphInfo);
    }

    /**
     * 修改知识图谱信息
     *
     * @param graphInfo 知识图谱信息
     * @return 结果
     */
    @Override
    public int updateGraphInfo(GraphInfo graphInfo) {
        graphInfo.setSyncTime(DateUtils.getNowDate());
        return graphInfoMapper.updateGraphInfo(graphInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteGraphInfosBySpaceIds(List<String> spaceIds) {
        spaceIds.forEach(spaceId -> {
            graphInfoMapper.deleteGraphInfoBySpaceId(spaceId);
            // 删除角色授权
            graphRoleService.deleteByGraphId(spaceId);
        });

        return 1;
    }
}
