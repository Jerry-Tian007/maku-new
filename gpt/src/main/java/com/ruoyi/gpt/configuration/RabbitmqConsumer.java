package com.ruoyi.gpt.configuration;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.model.domain.KbFile;
import com.ruoyi.gpt.model.dto.kb_file.KbFileUpdateTrainStatusDto;
import com.ruoyi.gpt.model.vo.kb_file.KbFileInfoVo;
import com.ruoyi.gpt.service.IKbFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * spring rabbitmq 生产者
 **/
@Component
@Slf4j
public class RabbitmqConsumer {
    @Autowired
    private IKbFileService kbFileService;


//    /**
//     * 监听更新队列的消息
//     *
//     * @param message 接收到的消息
//     */
//    @RabbitListener(queues = "update_queue")
//    public void updateQueueListener(String message) {
//        System.out.println("received from update_queue: " + message);
//    }
//
//    /**
//     * 监听删除队列的消息
//     *
//     * @param message 接收到的消息
//     */
//    @RabbitListener(queues = "delete_queue")
//    public void deleteQueueListener(String message) {
//        System.out.println("received from delete_queue: " + message);
//    }
//
//    /**
//     * 监听训练队列的消息
//     *
//     * @param message 接收到的消息
//     */
//    @RabbitListener(queues = "train_queue")
//    public void trainQueueListener(String message) {
//        System.out.println("received from train_queue: " + message);
//

    /**
     * 监听结果队列的消息
     *
     * @param message 接收到的消息
     */
    @RabbitListener(queues = "result_queue")
    public void resultQueueListener(String message) {
        try {
            log.info("received from result_queue: " + message);

            // 解析message
            JSONObject jsonObject = JSONUtil.parseObj(message);
            System.out.println("message====: " + jsonObject.toString());

            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
            String messageType = jsonObject.getStr("messageType");
            Long fileId = Long.parseLong(jsonObject.getStr("fileId"));
            Date operateTime = DateUtils.parseDate(jsonObject.getStr("operateTime"));
            Integer code = jsonObject.getInt("code");
            String errorReason = jsonObject.getStr("errorReason");
            Long[] ids = new Long[1];
            ids[0] = fileId;
            // 修改文件训练状态
            kbFileUpdateTrainStatusDto.setId(ids);
            if ("train".equals(messageType)) {
                if (code == 200) {
                    String jsonCosUrl = jsonObject.getStr("jsonCosUrl");
                    kbFileUpdateTrainStatusDto.setJsonCosUrl(jsonCosUrl);
                    String downloadUrlMd = jsonObject.getStr("downloadUrlMd");
                    kbFileUpdateTrainStatusDto.setDownloadUrlMd(downloadUrlMd);
                    KbFileInfoVo kbFileInfoVo = kbFileService.selectKbFileById(fileId);
                    kbFileUpdateTrainStatusDto.setTrainStatus("success");
                    kbFileUpdateTrainStatusDto.setTrainFinishTime(operateTime);
                    long i = calculateTimeDifferenceInMinutes(kbFileInfoVo.getTrainTime(), operateTime);
                    kbFileUpdateTrainStatusDto.setTrainDuration(i + "");
                    kbFileUpdateTrainStatusDto.setFileToken(jsonObject.get("tokenUsage").toString());
                } else if (code == 201) {
                    kbFileUpdateTrainStatusDto.setTrainStatus("training");
                    kbFileUpdateTrainStatusDto.setTrainTime(operateTime);
                } else if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainStatus("error");
                }
                kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
            } else if ("update".equals(messageType)) {
                String status = jsonObject.getStr("status");
                if (code == 200) {
                    KbFileInfoVo kbFileInfoVo = kbFileService.selectKbFileById(fileId);
                    kbFileUpdateTrainStatusDto.setTrainStatus(status);
                    kbFileUpdateTrainStatusDto.setTrainFinishTime(operateTime);
                    long i = calculateTimeDifferenceInMinutes(kbFileInfoVo.getTrainTime(), operateTime);
                    kbFileUpdateTrainStatusDto.setTrainDuration(i + "");
                } else if (code == 201) {
                    kbFileUpdateTrainStatusDto.setTrainStatus(status);
                    kbFileUpdateTrainStatusDto.setTrainTime(operateTime);
                } else if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainStatus(status);
                }
                kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
            } else if ("delete".equals(messageType)) {
                if (code == 200) {
                    kbFileService.deleteKbFileById(fileId);
                } else if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainTime(new Date());
                    kbFileUpdateTrainStatusDto.setTrainStatus("deleteFailed");
                    kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                    kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
                }
            } else if ("cancel".equals(messageType)) {
                if (code == 200) {
                    kbFileUpdateTrainStatusDto.setTrainTime(new Date());
                    kbFileUpdateTrainStatusDto.setTrainStatus("untrained");
                    kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                    kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
                } else if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainTime(new Date());
                    kbFileUpdateTrainStatusDto.setTrainStatus("cancelFailed");
                    kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                    kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
                }
            } else if ("logical_delete".equals(messageType)) {
                if (code == 200) {
                    KbFile kbFile = new KbFile();
                    kbFile.setId(fileId);
                    kbFile.setStatus("unable");
                    kbFileService.updateKbFileStatus(kbFile);
                }
                if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                    kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
                }
            } else if ("active".equals(messageType)) {
                if (code == 200) {
                    KbFile kbFile = new KbFile();
                    kbFile.setId(fileId);
                    kbFile.setStatus("enable");
                    kbFileService.updateKbFileStatus(kbFile);
                }
                if (code == 500) {
                    kbFileUpdateTrainStatusDto.setTrainErrorReason(errorReason);
                    kbFileService.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
                }
            } else {
                log.error("unknown messageType: " + messageType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 计算两个时间之间的差值（分钟）
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 差值（分钟）
     */
    private long calculateTimeDifferenceInMinutes(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        long diff = end.getTime() - start.getTime();
        System.out.println("diff====: " + diff);
        return diff / 1000;
    }
}
