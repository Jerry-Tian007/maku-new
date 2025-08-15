package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.mapper.KbQARecordFileRelMapper;
import com.ruoyi.gpt.model.domain.KbQARecordFileRel;
import com.ruoyi.gpt.service.IKbQARecordFileRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KbQARecordFileRelServiceImpl implements IKbQARecordFileRelService {

    @Autowired
    private KbQARecordFileRelMapper kbQARecordFileRelMapper;

    @Override
    public Integer insertKbQARecordFileRel(List<KbQARecordFileRel> kbQARecordFileRels) {
        return kbQARecordFileRelMapper.insertKbQARecordFileRel(kbQARecordFileRels);
    }
}
