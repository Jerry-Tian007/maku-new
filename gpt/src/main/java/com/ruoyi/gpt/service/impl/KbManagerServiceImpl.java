package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbManagerMapper;
import com.ruoyi.gpt.mapper.KbPersonalPromptMapper;
import com.ruoyi.gpt.model.domain.KbManager;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerInsertDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerUpdateDto;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbIdVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerInfoVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;
import com.ruoyi.gpt.service.IKbFileManagerRelService;
import com.ruoyi.gpt.service.IKbFileService;
import com.ruoyi.gpt.service.IKbManagerService;
import com.ruoyi.gpt.service.IKbAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库管理Service业务层处理
 */
@Service
public class KbManagerServiceImpl implements IKbManagerService {

    @Autowired
    private KbManagerMapper kbManagerMapper;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private IKbFileManagerRelService kbFileManagerRelService;

    @Autowired
    private IKbAuthorizationService kbAuthorizationService;

    @Autowired
    private IKbFileService kbFileService;

    @Autowired
    private KbPersonalPromptMapper kbPersonalPromptMapper;

    /**
     * 新增知识库数据
     *
     * @param kbManagerInsertDto
     * @return
     */
    @Override
    @Transactional
    public Integer insertKbManager(KbManagerInsertDto kbManagerInsertDto) {
        KbManagerInfoVo check = kbManagerMapper.checkByKbName(kbManagerInsertDto.getKbName());
        if (check != null) {
            throw new ServiceException(ReturnMessageEnum.the_knowledge_base_name_already_exists_modification_failed.toString());
        }
        try {
            KbManager kbManager = new KbManager();
            kbManager.setId(snowflakeGenerator.next());
            kbManager.setKbName(kbManagerInsertDto.getKbName());
            kbManager.setKbDescription(kbManagerInsertDto.getKbDescription());
            kbManager.setKbStatus(1);
            kbManager.setCreateBy(SecurityUtils.getUsername());
            kbManager.setCreateTime(new Date());
            if (!CollectionUtils.isEmpty(kbManagerInsertDto.getPromptIds())) {
                PersonalPromptEntity promptEntity = new PersonalPromptEntity();
                promptEntity.setUpdateBy(SecurityUtils.getUsername());
                promptEntity.setUpdateTime(new Date());
                promptEntity.setKbManagerId(kbManager.getId());
                kbPersonalPromptMapper.updateByIds(promptEntity, kbManagerInsertDto.getPromptIds());
            }
            return kbManagerMapper.insertKbManager(kbManager);
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_add_knowledge_base.toString()).setDetailMessage(ex.getMessage());
        }
    }

    /**
     * 修改知识库数据
     *
     * @param kbManagerUpdateDto
     * @return
     */
    @Override
    @Transactional
    public Integer updateKbManager(KbManagerUpdateDto kbManagerUpdateDto) {
        KbManagerInfoVo check = kbManagerMapper.checkByKbName(kbManagerUpdateDto.getKbName());
        if (check != null) {
            if (!check.getId().equals(kbManagerUpdateDto.getId())) {
                throw new ServiceException(ReturnMessageEnum.the_knowledge_base_name_already_exists_modification_failed.toString());
            }
        }
        try {
            KbManager kbManager = new KbManager();
            kbManager.setId(kbManagerUpdateDto.getId());
            kbManager.setKbName(kbManagerUpdateDto.getKbName());
            kbManager.setKbDescription(kbManagerUpdateDto.getKbDescription());
            kbManager.setUpdateBy(SecurityUtils.getUsername());
            kbManager.setUpdateTime(new Date());
            if (!CollectionUtils.isEmpty(kbManagerUpdateDto.getPromptIds())) {
                PersonalPromptEntity promptEntity = new PersonalPromptEntity();
                promptEntity.setUpdateBy(SecurityUtils.getUsername());
                promptEntity.setUpdateTime(new Date());
                promptEntity.setKbManagerId(kbManager.getId());
                kbPersonalPromptMapper.updateByIds(promptEntity, kbManagerUpdateDto.getPromptIds());
            }
            return kbManagerMapper.updateKbManager(kbManager);
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_delete_knowledge_base.toString()).setDetailMessage(ex.getMessage());
        }
    }

    /**
     * 根据id删除知识库数据
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Integer deleteById(Long id) {
        try {
            // 查询该知识库下的文件关联的所有知识库
            List<FileIdToKbIdVo> fileIdToKbIdVoList = kbFileManagerRelService.selectFileIdToKbIdByDeletedKbId(id);

            Map<Long, List<Long>> fileIdToKbIdList = fileIdToKbIdVoList.stream()
                    .collect(Collectors.groupingBy(FileIdToKbIdVo::getFileId,
                            Collectors.mapping(FileIdToKbIdVo::getKbId, Collectors.toList()))
                    );

            // 过滤出不存在于其他知识库下的文件
            List<Long> fileIds = fileIdToKbIdList.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 1 && entry.getValue().contains(id))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // 级联删除文件
            if (!CollectionUtils.isEmpty(fileIds)) {
                kbFileService.deleteKbFileByIds(fileIds);
            }

            // 删除文件与知识库的所属关系
            kbFileManagerRelService.deleteKbFileManagerRelByKbId(id);

            // 删除角色与知识库的授权关系
            kbAuthorizationService.deleteKbAuthorizationByKbId(id);

            KbManager kbManager = new KbManager();
            kbManager.setId(id);
            kbManager.setUpdateBy(SecurityUtils.getUsername());
            kbManager.setUpdateTime(new Date());
            // 删除知识库
            return kbManagerMapper.delete(kbManager);

        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_delete_knowledge_base.toString()).setDetailMessage(ex.getMessage());
        }
    }

    /**
     * 根据id查询知识库数据详情
     *
     * @param id
     * @return
     */
    @Override
    public KbManagerInfoVo selectInfoById(Long id) {
        KbManager kbManager = kbManagerMapper.selectInfoById(id);
        return BeanConvertUtil.conver(kbManager, KbManagerInfoVo.class);
    }

    /**
     * 知识库数据列表查询
     *
     * @param kbManagerPageDto
     * @return
     */
    @Override
    public List<KbManagerPageVo> list(KbManagerPageDto kbManagerPageDto) {
        String username = SecurityUtils.getUsername();
        Long[] roles = SecurityUtils.getLoginUser().getUser().getRoleIds();
        if (SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            return kbManagerMapper.list(kbManagerPageDto, null, "");
        }

        // 管理员角色跟admin权限保持一致
        List<Long> roleIdList = Arrays.stream(roles).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(roleIdList) && roleIdList.contains(1003L)) {
            return kbManagerMapper.list(kbManagerPageDto, null, "");
        }
        return kbManagerMapper.list(kbManagerPageDto, roles, username);
    }

    @Override
    public Integer removeBath(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        } else {
            for (int i = 0; i < ids.size(); i++) {
                Long id = ids.get(i);
                deleteById(id);
            }
            return ids.size();
        }
    }
}
