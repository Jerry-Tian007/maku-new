package com.ruoyi;

import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;
import com.ruoyi.gpt.service.IKbManagerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbManagerTest {

    @Autowired
    private IKbManagerService iKbManagerService;

    @Test
    public void list() {
        KbManagerPageDto kbManagerPageDto = new KbManagerPageDto();
        List<KbManagerPageVo> list = iKbManagerService.list(kbManagerPageDto);
        System.out.println("list = " + list);
    }
}
