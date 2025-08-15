package com.ruoyi.framework.web.service;

import javax.annotation.Resource;

import com.ruoyi.common.constant.*;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import jodd.time.TimeUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            if (!"admin".equals(username)) {
                Collection<String> keys = redisCache.keys("login_tokens:*");
                for (String key : keys) {
                    LoginUser token = redisCache.getCacheObject(key);
                    //redis查询是否有当前用户的token
                    String tokenName = token.getUsername();
                    if (username.equals(tokenName)) {
                        //如果有，删除
                        redisCache.deleteObject(key);
                    }
                }
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Long> longs = iSysRoleService.selectRoleListByUserId(loginUser.getUserId());
        Long[] array = longs.toArray(new Long[longs.size()]);
        loginUser.getUser().setRoleIds(array);
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }


    /**
     * ldap登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String ldapLogin(String username, String password) {

        String redisLoginKey = CacheConstants.LOGIN_TOKEN_KEY + "check:" + username;

        RLock lock = redissonClient.getLock(redisLoginKey);
        try {
            boolean tryResult = lock.tryLock(8, 2, TimeUnit.SECONDS);
            if (tryResult) {
                // 登录前置校验
                loginPreCheck(username, password);
                if (!"admin".equals(username)) {
                    Collection<String> keys = redisCache.keys("login_tokens:*");
                    for (String key : keys) {
                        LoginUser token = redisCache.getCacheObject(key);
                        //redis查询是否有当前用户的token
                        String tokenName = token.getUsername();
                        if (username.equals(tokenName)) {
                            // 如果有，返回对应的token信息
                            // redisCache.deleteObject(key);
                            // 获取jwtToken的值
                            String jwtTokenRedisKey = CacheConstants.LOGIN_JWT_TOKEN + username;
                            String jwtTokenValue = redisCache.getCacheObject(jwtTokenRedisKey);
                            if (StringUtils.isNotEmpty(jwtTokenValue)) {
                                // 不为空返回token的值
                                return jwtTokenValue;
                            }
                        }
                    }
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                AuthenticationContextHolder.setContext(authenticationToken);
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                // 用户验证
                Authentication authentication = authenticationManager.authenticate(authenticationToken);

                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                recordLoginInfo(loginUser.getUserId());
                // 生成token
                return tokenService.createToken(loginUser);
            }
            return "";
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
            if (lock.isLocked()){
                lock.unlock();
            }
        }
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 第三方登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String thirdPartyLogin(String username, String password) {
        String token = "";
//        String thirdPartyKey = "thirdParty:login:" + loginBodyDeptVo.getUsername();
//        if (redisCache.getCacheObject(thirdPartyKey) != null && (Integer) redisCache.getCacheObject(thirdPartyKey) >= 3) {
//            return AjaxResult.error("十分钟内仅能获取三次token");
//        }

        SysUser sysUserTemp = iSysUserService.selectUserByUserName(username);
        if (StringUtils.isNotNull(sysUserTemp)) {
            token = ldapLogin(username, password);
        } else {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(username);
            sysUser.setNickName(username);
//            if (loginBodyDeptVo.getDeptName() == null) {
//                return AjaxResult.error("部门名不能为空");
//            }
//            String deptName = loginBodyDeptVo.getDeptName();
//            SysDept sysDept = sysDeptService.selectDeptNameBy(deptName);
//            if (sysDept == null) {
//                return AjaxResult.error("没有该部门");
//            }
//            Long deptId = sysDept.getDeptId();
            sysUser.setDeptId(DeptConstants.WO_TAI_DEPT_ID);
            //这里注册用户给部门id为100，沃太
            Long[] arr = new Long[1];
            arr[0] = RoleConstants.OUTSIDE_ROLE_ID;



            //给一个外部用户的role
            sysUser.setRoleIds(arr);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            iSysUserService.insertUser(sysUser);
            // 生成令牌
            token = ldapLogin(username, password);
        }
//        incrementRedisCache(thirdPartyKey);
        return token;
    }
}
