package com.ruoyi;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.model.domain.KbQARecordFileRel;
import com.ruoyi.gpt.service.IKbQARecordFileRelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbQARecordFileRelTest {
    @Autowired
    private IKbQARecordFileRelService iKbQARecordFileRelService;
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Test
    public void insert() {
        List<KbQARecordFileRel> kbQARecordManagerRels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            KbQARecordFileRel kbQARecordFileRel = new KbQARecordFileRel();
            kbQARecordFileRel.setId(snowflakeGenerator.next());
            kbQARecordFileRel.setKbQARecordId(1L);
            kbQARecordFileRel.setKbFileId((long) i);
            kbQARecordFileRel.setKbFileName("文件" + i);
            kbQARecordFileRel.setCreateTime(DateUtils.getNowDate());
            kbQARecordFileRel.setCreateBy("admin");
            kbQARecordManagerRels.add(kbQARecordFileRel);
        }
        iKbQARecordFileRelService.insertKbQARecordFileRel(kbQARecordManagerRels);
    }
}
