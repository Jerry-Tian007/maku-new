package com.ruoyi;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.gpt.model.dto.kb_file.KbFilePageDto;
import com.ruoyi.gpt.model.dto.kb_file.KbFileUpdateBatchDto;
import com.ruoyi.gpt.model.dto.kb_file.KbFileUpdateDto;
import com.ruoyi.gpt.service.IKbFileService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbFileTest {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private IKbFileService iKbFileService;

    @BeforeEach
    public void thirdPartyLoginTest() {
        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            ServletRequestAttributes attributes = new ServletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(attributes);
            String token = sysLoginService.login("tiankk", "123456", "", "");
            Claims claims = tokenService.parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = tokenService.getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            tokenService.verifyToken(user);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            System.out.println("模拟登录成功！");
        } catch (Exception ex) {

        }
    }

    /**
     * 取消训练
     */
    @Test
    public void cancelTrainingTest() {
        iKbFileService.cancelTrainingById(641536773386995L);
    }

    @Test
    public void selectKbFileListTest() {
        KbFilePageDto kbFilePageDto = new KbFilePageDto();
        List<Long> kbIdList = Arrays.asList(1821719437741395968L);
        kbFilePageDto.setKbIds(kbIdList);
//        kbFilePageDto.setFileName("猫");
//        kbFilePageDto.setSuffix("pdf");
//        String startTime = "2024-8-9";
//        String endTime = "2024-8-10";
//        kbFilePageDto.setStartTime(startTime);
//        kbFilePageDto.setEndTime(endTime);
//        kbFilePageDto.setTrainStartTime(startTime);
//        kbFilePageDto.setTrainEndTime(endTime);
        System.out.println(iKbFileService.selectKbFileList(kbFilePageDto));
    }

    @Test
    public void selectKbFileByIdTest() {
        System.out.println(iKbFileService.selectKbFileById(1825065154476249088L));
    }

    /**
     * 修改文件所属知识库
     */
    @Test
    public void updateKbFileTest() {
        KbFileUpdateDto kbFileUpdateDto = new KbFileUpdateDto();
        kbFileUpdateDto.setFileId(1821722440187777024L);
        Long[] kbIds = {1821718764308140032L, 1821719437741395968L, 1821719251019370496L};
        kbFileUpdateDto.setKbIds(kbIds);
        iKbFileService.updateKbFile(kbFileUpdateDto);
    }

    /**
     * 删除文件
     */
    @Test
    public void deleteKbFileTest() {
        iKbFileService.deleteKbFileById(1823988915103600640L);
    }

    /**
     * 文件训练
     */
    @Test
    public void kbFileTrainTest() {
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(1821721705198915584L);
        fileIds.add(1821722440187777024L);
        iKbFileService.train(fileIds);
    }

    /**
     * 批量修改文件所属知识库
     */
    @Test
    public void updateBatchKbFileManagerRelTest() {
        KbFileUpdateBatchDto kbFileUpdateBatchDto = new KbFileUpdateBatchDto();
        Long[] fileIds = {1821721705198915584L, 1821722037559758848L, 1821722440187777024L, 1821723220118605824L};
        Long[] kbIds = {1821719251019370496L, 1821795609502289920L};
        kbFileUpdateBatchDto.setFileIds(fileIds);
        kbFileUpdateBatchDto.setKbIds(kbIds);
        iKbFileService.updateBatchKbFile(kbFileUpdateBatchDto);
    }
}
