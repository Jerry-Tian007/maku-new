package com.ruoyi;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.file.AsposeUtils;
import com.ruoyi.common.utils.file.FilePageUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.gpt.configuration.RabbitmqProducer;
import com.ruoyi.gpt.model.dto.file.UploadDto;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationInsertDto;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationPageDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelInsertDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerInsertDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerUpdateDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;
import com.ruoyi.gpt.service.IKbAuthorizationService;
import com.ruoyi.gpt.service.IKbFileManagerRelService;
import com.ruoyi.gpt.service.IKbManagerService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class RuoYiApplicationTest {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IKbManagerService iKbManagerService;
    @Autowired
    private IKbFileManagerRelService kbFileManagerRelService;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Autowired
    private IKbAuthorizationService kbAuthorizationService;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void thirdPartyLoginTest() {
        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            ServletRequestAttributes attributes = new ServletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(attributes);
            String token = sysLoginService.thirdPartyLogin("admin", "admin123");
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
    public void sendMessageTest() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime trainTime = LocalDateTime.now();
        String trainTimeStr = trainTime.format(dateTimeFormatter);

        JSONObject message = new JSONObject();
        message.set("id", "1821723592543440896");
        message.set("trainTime", trainTimeStr);
        message.set("trainStatus", "success");
        message.set("trainErrorReason", "");

        rabbitmqProducer.saveAndSendMessage("messageExchange", "result", message.toString());
    }

    @Test
    public void getPathTest() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void getPdfPageCountTest() {
        FileOutputStream fos = null;
        try {
            /**
             * 获取字节流数据
             */
            byte[] byteData = Files.readAllBytes(Paths.get("D:\\fileTest\\WindowsDefenderConfig15672port.docx"));

            /**
             * 将字节流数据转成pdf文件
             */
            String baseName = FilenameUtils.getBaseName("WindowsDefenderConfig15672port.docx");

            String fileTemplatePath = System.getProperty("user.dir") + "/zip/word2pdf";

            File fileTemplateSaveDir = new File(fileTemplatePath);

            if (!fileTemplateSaveDir.exists()) {

                fileTemplateSaveDir.mkdirs();
            }
            String newFileName = baseName + ".docx";
            File tempFile = new File(fileTemplatePath + File.separator + newFileName);
            fos = new FileOutputStream(tempFile);
            fos.write(byteData);
            String pdfPath = tempFile.getCanonicalPath().replace(".docx", ".pdf");
            AsposeUtils.wordToPdf(tempFile.getCanonicalPath(), pdfPath);
            tempFile.delete();
            File file = new File(pdfPath);


            /**
             * 将pdf文件转换成MultipartFile
             */
            MultipartFile multipartFile = FileUtils.fileToMultipartFile(file);
            System.out.println("文件大小" + multipartFile.getSize());

            /**
             * 将MultipartFile转换成InputStream
             */
            byte[] fileBytes = multipartFile.getBytes();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            Integer pdfPage = FilePageUtils.countPage(inputStream, "pdf");
            System.out.println("文件页数" + pdfPage);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Test
    public void getExcelPageCountTest() {
        try {
            FileInputStream fis = new FileInputStream("D:\\fileTest\\2024.6.22-2024.7.21.xlsx");
            Integer excelPage = FilePageUtils.countPage(fis, "xlsx");
            System.out.println(excelPage);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertKbManagerTest() {
        KbManagerInsertDto insertDto = new KbManagerInsertDto();
        insertDto.setKbName("四条腿");
        insertDto.setKbDescription("四条腿动物知识库");
        iKbManagerService.insertKbManager(insertDto);
    }

    @Test
    public void selectKbManagerInfoByIdTest() {
        System.out.println(iKbManagerService.selectInfoById(1821718764308140032L));
    }

    @Test
    public void deleteKbManagerByIdTest() {
        iKbManagerService.deleteById(1821795609502289920L);
    }

    @Test
    public void listKbManagerTest() {
        KbManagerPageDto pageDto = new KbManagerPageDto();
//        pageDto.setKbName("物");
        List<KbManagerPageVo> list = iKbManagerService.list(pageDto);
        System.out.println("list = " + list);
    }

    @Test
    public void updateManagerTest() {
        KbManagerUpdateDto updateDto = new KbManagerUpdateDto();
        updateDto.setId(1821795609502289920L);
        updateDto.setKbName("修改测试");
        updateDto.setKbDescription("这是修改后的知识库");
        iKbManagerService.updateKbManager(updateDto);
    }

    @Test
    public void saveKbFileTest() {
        UploadDto uploadDto = new UploadDto();
        String kbIds = "1821795609502289920,1821795609502289921,1821795609502289922";
        uploadDto.setKbIds(kbIds);
//        fileService.saveKbFile("https://nygpt.blob.core.windows.net/demo8blobsource/gptFile/253/221/253-221-1716125923963.pdf?sv=2022-11-02&se=2034-05-19T13%3A38%3A43Z&sr=b&sp=r&sig=BuZ7%2Bxq8M14JXlEelEziFPv9Lqtf3U7t%2FcPsQUz9Tig%3D", "这是fileName", "这是fileNameURL", "这是新文件名", "pdf", 600L, 20, "这是modelType", uploadDto);
//        fileService.saveKbFile(, 800L, 60, uploadDto);
    }


    @Test
    public void insertKbFileManagerRelTest() {
        KbFileManagerRelInsertDto kbFileManagerRelInsertDto = new KbFileManagerRelInsertDto();
        kbFileManagerRelInsertDto.setFileId(123L);
        Long[] kbIds = {456L, 789L};
        kbFileManagerRelInsertDto.setKbIds(kbIds);
        kbFileManagerRelService.insertKbFileManagerRel(kbFileManagerRelInsertDto);
    }


    @Test
    public void insertKbAuthorizationTest() {
        KbAuthorizationInsertDto kbAuthorizationInsertDto = new KbAuthorizationInsertDto();
        Long roleId = 10000L;
        Long[] kbIds = {1821718764308140032L, 1821719035369230336L};
        kbAuthorizationInsertDto.setRoleId(roleId);
        kbAuthorizationInsertDto.setKbIds(kbIds);
        kbAuthorizationService.insertKbAuthorization(kbAuthorizationInsertDto);
    }

    @Test
    public void kbAuthorizationListTest() {
        KbAuthorizationPageDto kbAuthorizationPageDto = new KbAuthorizationPageDto();
        List<KbAuthorizationPageVo> kbAuthorizationPageVoList = kbAuthorizationService.list(kbAuthorizationPageDto);
        System.out.println(kbAuthorizationPageVoList);
    }

    @Test
    public void test() {
        String str = "{\n" +
                "\"code\": 200, \n" +
                "\"data\": {\n" +
                "\"answer\": \"百万医疗险的投保年龄为出生满30天至60周岁(含)。\", \n" +
                "\"blobUrl\": [\n" +
                "\"http://127.0.0.1:5000\",\n" +
                "\"http://127.0.0.1:5000\",\n" +
                "\"http://127.0.0.1:5000\"\n" +
                "], \n" +
                "\"hiddenQuesion\": [\n" +
                "\"百万医疗险的保险期限是多久？\",\n" +
                "\"百万医疗险有哪些主要保障内容？\",\n" +
                "\"百万医疗险的等待期是多少天？\"\n" +
                "], \n" +
                "\"originalFileNames\": [\n" +
                "\"xxxx_12.pdf\",\n" +
                "\"xxxx_12.pdf\",\n" +
                "\"xxxx_12.pdf\"\n" +
                "], \n" +
                "\"questionVector\": [\n" +
                "0.020983517169952393,\n" +
                "-0.028013799339532852,\n" +
                "0.03321943059563637,\n" +
                "-0.03923005238175392,\n" +
                "-0.04891681671142578] \n" +
                "}, \n" +
                "\"msg\": \"回答生成成功\", \n" +
                "\"modelId\": \"232321321321\", \n" +
                "\"tokenUsage\": \"211\" \n" +
                "}";

        JSONObject jsonObject = new JSONObject(str);
        System.out.println(jsonObject.getStr("data"));
        JSONObject data = jsonObject.getJSONObject("data");
        System.out.println(data.getStr("answer"));
        System.out.println(data.getJSONArray("blobUrl").toString());
        System.out.println(data.getJSONArray("hiddenQuesion"));
        System.out.println(data.getJSONArray("originalFileNames"));
        System.out.println(data.getJSONArray("questionVector"));

        // JSONArray转String，使用逗号隔开
        String questionVector = String.join(",", (String[]) data.getJSONArray("questionVector").toArray(String.class));
        System.out.println("questionVector = " + questionVector);

    }

}
