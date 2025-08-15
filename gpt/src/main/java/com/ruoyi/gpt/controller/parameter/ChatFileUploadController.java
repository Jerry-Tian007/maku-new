package com.ruoyi.gpt.controller.parameter;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.gpt.model.dto.file.UploadDto;
import com.ruoyi.gpt.service.IFileCommonService;
import com.ruoyi.gpt.service.IFileSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/all-in-one/file")
public class ChatFileUploadController {
    @Autowired
    private IFileCommonService iFileCommonService;

    @PostMapping("/upload")
    public R<String> uploadFile(@RequestParam("file") MultipartFile file ) {
        return R.success(ReturnMessageEnum.upload_success.toString(), iFileCommonService.allInOneUpload(file));
    }


}
