package com.ruoyi.gpt.service.impl.util;

import com.ruoyi.common.core.domain.entity.SysRegion;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.system.mapper.SysRegionMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleRegionServiceImpl {

    @Resource
    private SysRegionMapper sysRegionMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    public Long getRoleId(String role){

        Long roleId = sysRoleMapper.selectRoleAll()
                .stream()
                .filter(roleTemp -> roleTemp.getRoleName().equals(role))
                .findFirst()
                .map(SysRole::getRoleId)
                .orElseThrow(() -> new NoSuchElementException("角色不存在，请校验"));
        return roleId;
    }

    public Long getRegionId(String region) {
        Long regionId = Optional.ofNullable(sysRegionMapper.selectSysRegionByRegion(region))
                .map(SysRegion::getRegionId)
                .orElseThrow(() -> new NoSuchElementException("区域不存在，请校验"));

        return regionId;
    }


}
