package com.ruoyi;


import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.model.domain.KbQARecordManagerRel;
import com.ruoyi.gpt.service.IKbQARecordManagerRelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbQARecordManagerRelTest {
    @Autowired
    private IKbQARecordManagerRelService iKbQARecordManagerRelService;
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Test
    public void insert() {
        List<KbQARecordManagerRel> kbQARecordManagerRels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            KbQARecordManagerRel kbQARecordManagerRel = new KbQARecordManagerRel();
            kbQARecordManagerRel.setId(snowflakeGenerator.next());
            kbQARecordManagerRel.setKbQARecordId(1L);
            kbQARecordManagerRel.setKbManagerId(1L);
            kbQARecordManagerRel.setCreateTime(DateUtils.getNowDate());
            kbQARecordManagerRel.setCreateBy("admin");
            kbQARecordManagerRels.add(kbQARecordManagerRel);
        }
        iKbQARecordManagerRelService.insertKbQARecordManagerRel(kbQARecordManagerRels);
    }


}
