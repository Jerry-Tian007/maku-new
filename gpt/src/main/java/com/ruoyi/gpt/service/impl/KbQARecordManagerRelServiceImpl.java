package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.mapper.KbQARecordManagerRelMapper;
import com.ruoyi.gpt.model.domain.KbQARecordManagerRel;
import com.ruoyi.gpt.service.IKbQARecordManagerRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KbQARecordManagerRelServiceImpl implements IKbQARecordManagerRelService {

    @Autowired
    private KbQARecordManagerRelMapper kbQARecordManagerRelMapper;

    @Override
    public Integer insertKbQARecordManagerRel(List<KbQARecordManagerRel> kbQARecordManagerRels) {
        return kbQARecordManagerRelMapper.insertKbQARecordManagerRel(kbQARecordManagerRels);
    }
}
