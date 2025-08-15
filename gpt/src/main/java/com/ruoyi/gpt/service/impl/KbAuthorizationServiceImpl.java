package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.KbAuthorizationMapper;
import com.ruoyi.gpt.model.domain.KbAuthorization;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationInsertDto;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationPageDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageSubVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationSelectVo;
import com.ruoyi.gpt.service.IKbAuthorizationService;
import com.ruoyi.gpt.service.IKbManagerService;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库授权Service业务处理层
 */
@Service
public class KbAuthorizationServiceImpl implements IKbAuthorizationService {

    @Autowired
    private KbAuthorizationMapper kbAuthorizationMapper;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private IKbManagerService kbManagerService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 知识库授权
     *
     * @param kbAuthorizationInsertDto
     * @return
     */
    @Override
    @Transactional
    public Integer insertKbAuthorization(KbAuthorizationInsertDto kbAuthorizationInsertDto) {
        Long roleId = kbAuthorizationInsertDto.getRoleId();
        Long[] kbIds = kbAuthorizationInsertDto.getKbIds();
        // 先删除
        deleteKbAuthorizationByRoleId(roleId);
        // 后新增
        List<KbAuthorization> kbAuthorizationList = Arrays.stream(kbIds).map(kbId -> {
            KbAuthorization kbAuthorization = new KbAuthorization();
            kbAuthorization.setId(snowflakeGenerator.next());
            kbAuthorization.setRoleId(roleId);
            kbAuthorization.setKbId(kbId);
            kbAuthorization.setCreateBy(SecurityUtils.getUsername());
            kbAuthorization.setCreateTime(new Date());
            return kbAuthorization;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(kbAuthorizationList)) {
            return kbAuthorizationMapper.insertKbAuthorization(kbAuthorizationList);
        }
        return 0;
    }

    /**
     * 已授权知识库列表
     *
     * @return
     */
    @Override
    public List<KbAuthorizationPageVo> list(KbAuthorizationPageDto kbAuthorizationPageDto) {
        // 如果是管理员查询所有
        Long[] roleIds = null;
        // 如果不是超级管理员，根据该账号的角色查询
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            roleIds = SecurityUtils.getLoginUser().getUser().getRoleIds();
        }

        // 查询KbAuthorizationPageVo -- 只查询了roleId和roleName，其余属性为空
        List<KbAuthorizationPageVo> kbAuthorizationPageVoList = kbAuthorizationMapper.selectKbAuthorizationPageVo(roleIds, kbAuthorizationPageDto);

        // 查询KbAuthorizationPageVo中除了roleId、roleName以外的其余属性
        List<KbAuthorizationPageSubVo> kbAuthorizationList = kbAuthorizationMapper.selectKbAuthorizationPageSubVo(roleIds);

        // roleId和kbName映射 -- 逗号拼接
        Map<Long, String> roleIdToKbName = kbAuthorizationList.stream().collect(
                Collectors.groupingBy(
                        KbAuthorizationPageSubVo::getRoleId,
                        Collectors.mapping(KbAuthorizationPageSubVo::getKbName, Collectors.joining(",")))
        );
        // roleId和createBy映射 -- 最大值
        Map<Long, String> roleIdToCreateBy = kbAuthorizationList.stream().collect(
                Collectors.groupingBy(
                        KbAuthorizationPageSubVo::getRoleId,
                        Collectors.collectingAndThen(
                                Collectors.mapping(
                                        KbAuthorizationPageSubVo::getCreateBy,
                                        Collectors.maxBy(Comparator.naturalOrder())
                                ),
                                optional -> optional.orElse(null)
                        )
                )
        );
        // roleId和createTime映射 -- 最大值
        Map<Long, Date> roleIdToCreateTime = kbAuthorizationList.stream().collect(
                Collectors.groupingBy(
                        KbAuthorizationPageSubVo::getRoleId,
                        Collectors.collectingAndThen(
                                Collectors.mapping(
                                        KbAuthorizationPageSubVo::getCreateTime,
                                        Collectors.maxBy(Comparator.naturalOrder())
                                ),
                                optional -> optional.orElse(null)
                        )
                )
        );

        // 封装kbAuthorizationPageVoList
        kbAuthorizationPageVoList.forEach(kbAuthorizationPageVo -> {
            kbAuthorizationPageVo.setKbName(roleIdToKbName.get(kbAuthorizationPageVo.getRoleId()));
            kbAuthorizationPageVo.setCreateBy(roleIdToCreateBy.get(kbAuthorizationPageVo.getRoleId()));
            kbAuthorizationPageVo.setCreateTime(roleIdToCreateTime.get(kbAuthorizationPageVo.getRoleId()));
        });

        return kbAuthorizationPageVoList;
    }

    /**
     * 根据角色id删除授权数据
     *
     * @param roleId
     * @return
     */
    public Integer deleteKbAuthorizationByRoleId(Long roleId) {
        return kbAuthorizationMapper.deleteKbAuthorizationByRoleId(roleId);
    }

    /**
     * 根据知识库id删除授权数据
     *
     * @param kbId
     * @return
     */
    @Override
    public Integer deleteKbAuthorizationByKbId(Long kbId) {
        return kbAuthorizationMapper.deleteKbAuthorizationByKbId(kbId);
    }

    @Override
    public List<KbAuthorizationSelectVo> getSelect() {
        // 如果是管理员查询所有
        Long[] roleIds = null;
        String userName = "";
        // 如果不是超级管理员，根据该账号的角色查询
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            roleIds = SecurityUtils.getLoginUser().getUser().getRoleIds();
            userName = SecurityUtils.getUsername();
        }
        return kbAuthorizationMapper.getSelect(roleIds, userName);
    }

    @Override
    public Set<String> getKbListByRoleId(Long roleId) {
        return kbAuthorizationMapper.getKbListByRoleId(roleId);
    }
}
