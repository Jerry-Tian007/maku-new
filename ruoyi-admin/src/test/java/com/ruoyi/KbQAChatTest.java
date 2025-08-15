package com.ruoyi;


import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQAChatDto;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.service.IKbQAChatService;
import com.ruoyi.gpt.service.IModelApiService;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbQAChatTest {
    @Autowired
    private IKbQAChatService iKbQAChatService;
    @Autowired
    private IModelApiService iModelApiService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

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

    @Test
    public void chat() {
        String str = "{\"history\":[\n" +
                "            {\n" +
                "                \"role\":\"user\",\n" +
                "                    \"content\":\"介绍数据分析进阶指南的信息吧\",\n" +
                "                    \"type\":\"text\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"role\":\"assistant\",\n" +
                "                    \"content\":\n" +
                "                \"您好！我是一名严谨、权威的智能助手，我的回答仅基于私有领域知识库中的参考内容。如果您有任何与私有领域知识库相关的问题，请随时提问。\",\n" +
                "                        \"type\":\"text\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"role\":\"user\",\n" +
                "                    \"content\":\"如果我想联系Alpha ESS在我所在地区的办公室，我应该如何做？\",\n" +
                "                    \"type\":\"text\"\n" +
                "            }\n" +
                "    ]}";
        JSONObject jsonObject = new JSONObject(str);
        JSONArray history = jsonObject.getJSONArray("history");
        String str2 = "{\"ragParams\":{\n" +
                "            \"kbId\": [1821718764308140032, 1821719437741395968],\n" +
                "            \"topK\":4,\n" +
                "                    \"search\":\"vector\",\n" +
                "                    \"needHidden\":true\n" +
                "        }}";
        JSONObject jsonObject2 = new JSONObject(str2);
        JSONObject ragParams = jsonObject2.getJSONObject("ragParams");

        Long[] kbManagerIds = {1821718764308140032L, 1821719437741395968L};
        KbQAChatDto kbQAChatDto = new KbQAChatDto();
        kbQAChatDto.setChatTitle("介绍数据分析进阶指南的信息吧");
        kbQAChatDto.setModelType("gpt-4o");
        kbQAChatDto.setModelTypeId("1");
        kbQAChatDto.setScene("vision");
        kbQAChatDto.setSessionId(snowflakeGenerator.next().toString());
        kbQAChatDto.setQuestion("介绍数据分析进阶指南的信息吧");
        kbQAChatDto.setKbManagerIds(kbManagerIds);
        kbQAChatDto.setHistory(history);
        kbQAChatDto.setRagParams(ragParams);

        ModelChatApiDto modelChatApiDto = BeanConvertUtil.conver(kbQAChatDto, ModelChatApiDto.class);
        ModelResources modelResources2 = iModelApiService.calTpmMax(modelChatApiDto);
        JSONObject entries = new JSONObject();
        entries.set("modelId", modelResources2.getId());
        entries.set("modelParams", modelResources2.getParameter());
        kbQAChatDto.setModelInfo(entries);

        iKbQAChatService.chat(kbQAChatDto);
    }
}
