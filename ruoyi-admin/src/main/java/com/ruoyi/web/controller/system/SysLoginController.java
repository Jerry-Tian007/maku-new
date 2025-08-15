package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.constant.DeptConstants;
import com.ruoyi.common.constant.RoleConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.model.vo.LoginBodyDeptVo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.gpt.core.ldap.LdapUtils;
import com.ruoyi.gpt.model.domain.DeptAdInfo;
import com.ruoyi.gpt.service.IDeptAdInfoService;
import com.ruoyi.gpt.service.IModelResourcesService;
import com.ruoyi.gpt.service.IWeconService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.impl.SysDeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysMenuService;

import javax.naming.directory.Attributes;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
@Slf4j
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IDeptAdInfoService deptAdInfoService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IWeconService weconService;

    @Autowired
    private IModelResourcesService modelResourcesService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername().toLowerCase(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/thirdParty/login")
    public AjaxResult thirdPartyLogin(@RequestBody LoginBodyDeptVo loginBodyDeptVo) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.thirdPartyLogin(loginBodyDeptVo.getUsername(), loginBodyDeptVo.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/oa/login")
    public AjaxResult oaLogin(@RequestBody JSONObject params) {
        String token = "";
        String identityId = params.get("identityId").toString().toLowerCase();
        String identityName = params.getStr("identityName");
        System.out.println(identityId+"--"+identityName);
        SysUser sysUser1 = iSysUserService.selectUserByUserName(identityId);
        if (StringUtils.isNotNull(sysUser1)) {
            token = loginService.ldapLogin(identityId, identityId);
        } else {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(identityId.toString());
            sysUser.setNickName(identityName);
            sysUser.setDeptId(100L);
            Long[] arr = new Long[1];
            arr[0] = 129L;
            sysUser.setRoleIds(arr);
            sysUser.setPassword(SecurityUtils.encryptPassword(identityId.toString()));
            iSysUserService.insertUser(sysUser);
            token = loginService.ldapLogin(identityId.toString(), identityId.toString());
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    @PostMapping("/ldapLogin")
    public AjaxResult ldapLogin(@RequestBody LoginBody loginBody) {
        String username = loginBody.getUsername();
        // 验证登录错误次数
        String ldapErrorKey = "ldap:error:" + username;
        if (redisCache.getCacheObject(ldapErrorKey) != null && (Integer) redisCache.getCacheObject(ldapErrorKey) >= 3) {
            return AjaxResult.error("AD域登录账号密码错误超过三次，请在三分钟后重试");
        }
        String password = loginBody.getPassword();
        String[] split = username.split("@");
        DeptAdInfo deptAdInfo = deptAdInfoService.selectDeptAdInfoByAdUsernameSuffix(split[1]);
        if (StringUtils.isNull(deptAdInfo)) {
            return AjaxResult.error("当前AD域暂未在平台开通，请联系管理员");
        }
        try {
            LdapAuthenticator ldapAuthenticator = LdapUtils.getldapAuthenticator(
                    deptAdInfo.getAdUrl(),
                    deptAdInfo.getAdAdminAccount(),
                    deptAdInfo.getAdAdminSecret(),
                    deptAdInfo.getAdSearchBase(),
                    deptAdInfo.getAdSearchFilter());
            DirContextOperations result = ldapAuthenticator.authenticate(new UsernamePasswordAuthenticationToken(split[0], password));
            if (deptAdInfo.getDeptId() == null) {
                Attributes attributes = result.getAttributes();
                String distinguishedName = (String) attributes.get("distinguishedName").get();
                String[] parts = distinguishedName.split("(?<!\\\\),");
                //获取部门名称
                String deptName = "";
                for (int i = parts.length - 1; i >= 0; i--) {
                    String part = parts[i];
                    int index = part.indexOf("=");
                    if (index != -1) {
                        String name = part.substring(index + 1);
                        if (part.startsWith("OU")) {
                            deptName = name;
                        }
                    }
                }
                SysDept sysDept = sysDeptService.selectDeptNameBy(deptName);
                if (sysDept == null) {
                    return AjaxResult.error("当前账号AD域部门（" + deptName + "）未在平台授权，请联系管理员");
                }
                deptAdInfo.setDeptId(sysDept.getDeptId());
            }
            // 鉴权成功
            String token = "";
            if (StringUtils.isNotNull(result)) {
                SysUser sysUser = iSysUserService.selectUserByUserName(username);
                if (StringUtils.isNotNull(sysUser)) {
                    if (SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
                        token = loginService.ldapLogin(username, password);
                    } else {
                        sysUser.setPassword(SecurityUtils.encryptPassword(password));
                        if (iSysUserService.updateUser(sysUser) > 0) {
                            token = loginService.ldapLogin(username, password);
                        }
                    }
                } else {
                    SysUser addSysUser = new SysUser();
                    addSysUser.setUserName(username);
                    addSysUser.setNickName(username);
                    addSysUser.setDeptId(deptAdInfo.getDeptId());
                    String deptAdInfoRoleIds = deptAdInfo.getRoleIds();
                    String[] strings = deptAdInfoRoleIds.substring(1, deptAdInfoRoleIds.length() - 1).split(",");
                    Long[] roleIds = Arrays.stream(strings).map(Long::parseLong).toArray(Long[]::new);
                    addSysUser.setRoleIds(roleIds);
                    addSysUser.setEmail(username);
                    addSysUser.setStatus("0");
                    addSysUser.setPassword(SecurityUtils.encryptPassword(loginBody.getPassword()));
                    if (iSysUserService.insertUser(addSysUser) > 0) {
                        token = loginService.ldapLogin(username, password);
                    }
                }
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } catch (BadCredentialsException e) {
            Integer cacheObject = (Integer) redisCache.getCacheObject(ldapErrorKey);
            if (cacheObject == null) {
                redisCache.setCacheObject(ldapErrorKey, 1, 3, TimeUnit.MINUTES);
            } else {
                redisCache.setCacheObject(ldapErrorKey, cacheObject + 1, 3, TimeUnit.MINUTES);
            }
            return AjaxResult.error("用户名或密码错误");
        } catch (CommunicationException e) {
            return AjaxResult.error("域信息错误，请联系管理员");
        } catch (UsernameNotFoundException e) {
            return AjaxResult.error("账号错误");
        } catch (Exception e) {
            return AjaxResult.error("AD域登录失败");
        }

    }

//    @PostMapping("/weconLogin")
//    public AjaxResult code(@RequestBody JSONObject param) {
//        try {
//            String code = param.getStr("code");
//            log.info("Code: " + code);
//            String wxUserId = weconService.getUserIdByCode(code);
//            log.info("wxuserid :" + wxUserId);
//            if (StringUtils.isEmpty(wxUserId)) {
//                log.info("code: " + code);
//                return AjaxResult.error("账号不存在或登录受限");
//            }
//
//            SysUser sysUser = iSysUserService.selectUserBywxId(wxUserId);
//            if (StringUtils.isNull(sysUser)) {
//                return AjaxResult.error("账号不存在或登录受限");
//        }
//
//        String identityId = sysUser.getUserName();
//        String identityName = sysUser.getNickName();
//
//        log.info("identityId: " + identityId);
//        log.info("identityName: " + identityName);
//        if (StringUtils.isEmpty(identityId)) {
//            return AjaxResult.error("未查询到账号");
//        }
//
//        String token = loginService.ldapLogin(identityId, identityId);
//        log.info("token: " + token);
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put(Constants.TOKEN, token);
//            ajax.put("identityId", identityId);
//            ajax.put("identityName", identityName);
//            return ajax;
//        } catch (Exception e) {
//            return AjaxResult.error("未知错误，请联系管理员");
//        }
//    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

//        Set<String> modelResources = SecurityUtils.getLoginUser().getModelResources();

//        List<ModelResourcesTokenUseVo> modelResourcesToken = new ArrayList<>();
//        if (modelResources.contains("all")){
//            modelResourcesToken = modelResourcesService.getModelResourcesToken(modelResources);
//        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
//        ajax.put("modelResources", modelResourcesToken);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
//        ajax.put("modelTypes", SecurityUtils.getLoginUser().getModelResources());
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    public void incrementRedisCache(String key) {
        Integer cacheObject = redisCache.getCacheObject(key);
        if (cacheObject == null) {
            redisCache.setCacheObject(key, 1, 600, TimeUnit.SECONDS);
        } else {
            redisCache.setCacheObject(key, cacheObject + 1, Math.toIntExact(redisCache.getExpire(key)), TimeUnit.SECONDS);
        }
    }
}
