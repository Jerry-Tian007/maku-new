package com.ruoyi.gpt.mapper;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.GraphInfo;
import com.ruoyi.gpt.model.vo.GraphRoleVo;

import java.util.List;

/**
 * 知识图谱信息Mapper接口
 *
 * @author yecao
 * @date 2025-04-09
 */
public interface GraphInfoMapper {

    /**
     * 查询所有的图谱id
     * @return 图谱id集合
     */
    public List<String> selectAllSpaceIds();

    /**
     * 查询知识图谱信息列表
     *
     * @param graphInfo 知识图谱信息
     * @return 知识图谱信息集合
     */
    public List<GraphInfo> selectGraphInfoList(GraphInfo graphInfo);

    /**
     * 查询知识图谱信息
     * @param spaceId 知识图谱空间id
     * @return 结果
     */
    public GraphInfo selectGraphInfoBySpaceId(String spaceId);

    public List<GraphInfo> selectGraphInfoByRoleId(Long roleId);

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

    public int deleteGraphInfoBySpaceId(String spaceId);

}
