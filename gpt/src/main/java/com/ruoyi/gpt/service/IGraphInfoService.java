package com.ruoyi.gpt.service;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.gpt.model.domain.GraphInfo;
import com.ruoyi.gpt.model.domain.GraphRole;
import com.ruoyi.gpt.model.dto.graph.GraphAuthDto;
import com.ruoyi.gpt.model.dto.graph.GraphInfoListDto;
import com.ruoyi.gpt.model.vo.GraphInfoListVo;
import com.ruoyi.gpt.model.vo.GraphRoleVo;

import java.util.List;

/**
 * 知识图谱信息Service接口
 *
 * @author yecao
 * @date 2025-04-09
 */
public interface IGraphInfoService {

    /**
     * 查询所有的图谱id
     * @return 图谱id集合
     */
    public List<String> selectAllSpaceIds();

    public List<GraphRoleVo> graphRole(Long roleId);


    public List<GraphInfo> userGraphRole(Long userId);

    /**
     * 查询知识图谱信息
     *
     * @param spaceId 知识图谱空间id
     * @return 结果
     */
    public GraphInfo selectGraphInfoBySpaceId(String spaceId);

    public List<GraphInfo> selectGraphInfoByRoleId(Long roleId);

    /**
     * 平台查询知识图谱信息列表
     *
     * @param graphInfoListDto 知识图谱信息
     * @return 知识图谱信息集合
     */
    public List<GraphInfo> selectGraphInfoListForPlatform(GraphInfoListDto graphInfoListDto);


    /**
     * 查询知识图谱信息列表
     *
     * @param graphInfo 知识图谱信息
     * @return 知识图谱信息集合
     */
    public List<GraphInfo> selectGraphInfoList(GraphInfo graphInfo);

    /**
     * 同步知识图谱数据
     *
     * @return 结果
     */
    public int syncGraphInfo();


    public int auth(GraphAuthDto graphAuthDto);

    /**
     * 新增知识图谱信息
     *
     * @param graphInfo 知识图谱信息
     * @return 结果
     */
    public int insertGraphInfo(GraphInfo graphInfo);

    /**
     * 修改知识图谱信息
     *
     * @param graphInfo 知识图谱信息
     * @return 结果
     */
    public int updateGraphInfo(GraphInfo graphInfo);


    public int deleteGraphInfosBySpaceIds(List<String> ids);
}
