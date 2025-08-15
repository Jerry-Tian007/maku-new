package com.ruoyi.gpt.controller.oa;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.AsposeUtils;
import com.ruoyi.gpt.core.blob.JedisClientUtils;
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.model.domain.KbTrainFile;
import com.ruoyi.gpt.model.domain.OaFile;
import com.ruoyi.gpt.model.domain.OaParams;
import com.ruoyi.gpt.service.IDeptKbConfigService;
import com.ruoyi.gpt.service.IKbTrainFileService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/with/oa")
public class OaController extends BaseController {

    @Value("${train.url}")
    private String trainUrl;

    @Resource
    private IKbTrainFileService iKbTrainFileService;
    @Resource
    private IDeptKbConfigService deptKbConfigService;
    @Resource
    private ISysUserService sysUserService;

    @PostMapping("/accept/file/operate")
    public AjaxResult acceptDocuments(@Validated @RequestBody OaParams oaParams) {
        logger.info("接收到OA文件推送");
//        HashMap<Long, Object> map = new HashMap<>();
//        map.put(1580L,"1");
//        map.put(1581L,"1");
//        map.put(1582L,"1");
//        if (!map.containsKey(oaParams.getBusinessId())){
//            return AjaxResult.success();
//        }
        KbTrainFile kbTrainFile = new KbTrainFile();
        kbTrainFile.setBusinessName(oaParams.getBusinessName());
        kbTrainFile.setBusinessId(oaParams.getBusinessId());
        List<KbTrainFile> kbTrainFiles = iKbTrainFileService.selectKbTrainFileList(kbTrainFile);
        List<OaFile> files = oaParams.getFiles();

        Map<String, KbTrainFile> oldFilesMap = new HashMap<>();
        for (KbTrainFile item : kbTrainFiles) {
            oldFilesMap.put(item.getFileId(), item);
        }
        List<OaFile> toAdd = new ArrayList<>();
        List<OaFile> toTrain = new ArrayList<>();
        for (OaFile file : files) {
            //文件类型转换
            KbTrainFile old = oldFilesMap.get(file.getFileId());
            if (old != null) {
                if (!old.getTrainStatus().equals("success")) {
                    toTrain.add(file);
                }
                if (!old.getFileUrl().equals(file.getFileUrl())) {
                    //更新fileUrl
                    KbTrainFile kbUpdateUrl = new KbTrainFile();
                    kbUpdateUrl.setFileUrl(file.getFileUrl());
                    kbUpdateUrl.setFileId(old.getFileId());
                    iKbTrainFileService.updateKbTrainFile(kbUpdateUrl);
                }
                oldFilesMap.remove(file.getFileId());
            } else {
                toAdd.add(file);
            }
        }
        List<KbTrainFile> toDelete = new ArrayList<>(oldFilesMap.values());
        for (KbTrainFile item : toDelete) {
            iKbTrainFileService.deleteKbTrainFileByFileId(item.getFileId());
            deleteRedisVector(item, oaParams.getBusinessId());
            //删除redis
        }
        for (OaFile item : toAdd) {
            KbTrainFile add = new KbTrainFile();
            add.setFileId(item.getFileId());
            add.setFileName(item.getFileName());
            add.setFileSuffix(item.getFileSuffix());
            add.setBusinessId(oaParams.getBusinessId());
            add.setBusinessName(oaParams.getBusinessName());
            add.setTrainStatus("untrained");
            add.setOwnerId(item.getOwnerId());
            add.setOwnerName(item.getOwnerName());
            add.setFileUrl(item.getFileUrl());
            add.setIsIdentifyForm(item.getIsIdentifyForm());
            iKbTrainFileService.insertKbTrainFile(add);
        }
        toTrain.addAll(toAdd);
        //推送训练接口到python
        oaParams.setFiles(toTrain);
        WebClient webClient = WebClient.create();
        Mono<ResponseEntity<String>> responseMono = webClient.post()
                .uri(trainUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(oaParams)
                .retrieve()
                .toEntity(String.class);

        responseMono.subscribe(
                result -> {
                    logger.info("train success");
                    // 请求成功的处理逻辑
                },
                ex -> {
                    // 请求失败的处理逻辑
                    logger.info("train error");
                });
        return AjaxResult.success();
    }

    private void deleteRedisVector(KbTrainFile kbTrainFile, Long businessId) {
        DeptKbConfig deptKbConfig = deptKbConfigService.selectDeptKbConfigById(1L);
        String pattern = "document:"+businessId + ":" + kbTrainFile.getFileName()+"*";
        Jedis jedisClient = JedisClientUtils.getJedisClient(deptKbConfig.getRedisHost(),
                deptKbConfig.getRedisPort(),
                deptKbConfig.getRedisPassword(),
                deptKbConfig.getRedisDb());
        JedisClientUtils.scanKeysAndDelete(pattern, jedisClient);
        jedisClient.close();
    }

    @GetMapping("/file/wordToPdf")
    public void wordToPdf(@RequestParam("fileUrl") String fileUrl, HttpServletResponse httpResponse) throws IOException {
        URL url = new URL(fileUrl);
        String fileName = URLEncoder.encode(FilenameUtils.getName(url.getPath()), "UTF-8"); // 获取文件名
        String baseName = FilenameUtils.getBaseName(fileName);
        // 下载文件并保存到临时目录
        File tempDir = new File(System.getProperty("user.dir") + "/converter2pdf");
        File tempFile = File.createTempFile(baseName, ".docx", tempDir);

        downloadFile(url, tempFile);
        String pdfPath = tempFile.getCanonicalPath().replace(".docx", ".pdf");
        AsposeUtils.wordToPdf(tempFile.getCanonicalPath(), pdfPath);
        tempFile.delete();
        File pdfFile = new File(pdfPath);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentLength((int) pdfFile.length());
        httpResponse.setContentType(MediaType.APPLICATION_PDF_VALUE);
        httpResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(baseName, "UTF-8").replaceAll("\\+", "%20") + ".pdf\"");
        try (InputStream pdfInputStream = new FileInputStream(pdfFile);
             OutputStream responseOutputStream = httpResponse.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                responseOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfFile.delete();
    }

    @GetMapping("/file/pptToPdf")
    public void pptToPdf(@RequestParam("fileUrl") String fileUrl, HttpServletResponse httpResponse) throws IOException {
        URL url = new URL(fileUrl);
        String fileName = URLEncoder.encode(FilenameUtils.getName(url.getPath()), "UTF-8"); // 获取文件名
        String baseName = FilenameUtils.getBaseName(fileName);
        // 下载文件并保存到临时目录
        File tempDir = new File(System.getProperty("user.dir") + "/converter2pdf");
        File tempFile = File.createTempFile(baseName, ".pptx", tempDir);

        downloadFile(url, tempFile);
        String pdfPath = tempFile.getCanonicalPath().replace(".pptx", ".pdf");
        AsposeUtils.pptToPdf(tempFile.getCanonicalPath(), pdfPath);
        tempFile.delete();
        File pdfFile = new File(pdfPath);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setContentLength((int) pdfFile.length());
        httpResponse.setContentType(MediaType.APPLICATION_PDF_VALUE);
        httpResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(baseName, "UTF-8").replaceAll("\\+", "%20") + ".pdf\"");

        try (InputStream pdfInputStream = new FileInputStream(pdfFile);
             OutputStream responseOutputStream = httpResponse.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pdfInputStream.read(buffer)) != -1) {
                responseOutputStream.write(buffer, 0, bytesRead);
            }
        }
        pdfFile.delete();
    }

    private void downloadFile(URL url, File saveFile) throws IOException {
        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream out = new FileOutputStream(saveFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    @PostMapping("/accept/user")
    public CompletableFuture<AjaxResult> acceptDocuments(@RequestBody JSONArray params) {
//        params.forEach(item -> {
//            JSONObject user = (JSONObject) item;
//            if ("1".equals(user.getStr("isDelete"))) {
//                SysUser sysUser = sysUserService.selectUserByUserName(user.getStr("adAccount"));
//                if (sysUser != null) {
//                    sysUserService.deleteUserById(sysUser.getUserId());
//                }
//            } else {
//                SysUser sysUser = sysUserService.selectUserByUserName(user.getStr("adAccount"));
//                if (sysUser != null) {
//                    sysUser.setRemark(user.getStr("userId"));
//                    sysUser.setNickName(user.getStr("userName"));
//                    sysUserService.updateUser(sysUser);
//                }else {
//                    SysUser addSysUser = new SysUser();
//                    addSysUser.setUserId(Long.valueOf(user.getStr("userId")));
//                    addSysUser.setUserName(user.getStr("adAccount"));
//                    addSysUser.setNickName(user.getStr("userName"));
//                    addSysUser.setDeptId(100L);
//                    Long[] arr = new Long[1];
//                    arr[0] = 129L;
//                    addSysUser.setRoleIds(arr);
//                    addSysUser.setPassword(SecurityUtils.encryptPassword(user.getStr("adAccount")));
//                    sysUserService.insertUser(sysUser);
//                }
//            }
//        });
//        return AjaxResult.success();
// Create an executor service with a fixed thread pool

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            params.forEach(item -> {
                JSONObject user = (JSONObject) item;
                if (1 == user.getInt("isDelete")) {
                    SysUser sysUser = sysUserService.selectUserByUserName(user.getStr("adAccount"));
                    if (sysUser != null) {
                        sysUserService.deleteUserRecordById(sysUser.getUserId());
                    }
                } else {
                    SysUser sysUser = sysUserService.selectUserByUserName(user.getStr("adAccount"));
                    //如果正常的adAccount查出来为空
                    //那我就直接搞全部小写的adAccount插入
                    //如果查出来不为空，直接更新adAccount为小写的
                    if (sysUser != null) {
                        sysUser.setUserId(Long.valueOf(user.getStr("userId")));
                        sysUser.setNickName(user.getStr("userName"));
                        sysUser.setUserName(user.getStr("adAccount").toLowerCase());
                        sysUser.setPassword(SecurityUtils.encryptPassword(user.getStr("adAccount").toLowerCase()));
                        sysUser.setUpdateTime(DateUtils.getNowDate());
                        sysUser.setUpdateBy("OA定时推送");
                        sysUserService.updateUser(sysUser);
                    } else {
                        SysUser addSysUser = new SysUser();
                        addSysUser.setUserId(Long.valueOf(user.getStr("userId")));
//                        addSysUser.setWxUserId(user.getStr("wxUserId"));
                        addSysUser.setUserName(user.getStr("adAccount").toLowerCase());
                        addSysUser.setNickName(user.getStr("userName"));
                        addSysUser.setDeptId(129L);
                        Long[] arr = new Long[1];
                        arr[0] = 129L;
                        addSysUser.setRoleIds(arr);
                        addSysUser.setUpdateBy("OA定时推送");
                        addSysUser.setPassword(SecurityUtils.encryptPassword(user.getStr("adAccount").toLowerCase()));
                        sysUserService.insertUser(addSysUser);
                    }
                }
            });
        }, executorService);

        // Return a CompletableFuture that will be completed when the processing is finished
        return completableFuture.thenApply(result -> AjaxResult.success());
    }

}


