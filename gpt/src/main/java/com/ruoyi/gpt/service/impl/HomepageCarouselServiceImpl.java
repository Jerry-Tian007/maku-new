package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.gpt.mapper.HomepageCarouselMapper;
import com.ruoyi.gpt.model.domain.HomepageCarousel;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselEditDisplayOrderDto;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselEditDto;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselInsertDto;
import com.ruoyi.gpt.service.IFileCommonService;
import com.ruoyi.gpt.service.IHomepageCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 首页轮播图，存储首页展示的轮播图片及其链接Service业务层处理
 *
 * @author Liujr
 * @date 2024-08-13
 */
@Service
public class HomepageCarouselServiceImpl implements IHomepageCarouselService {
    @Autowired
    private HomepageCarouselMapper homepageCarouselMapper;
    @Autowired
    private IFileCommonService iFileCommonService;
    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @Override
    public JSONArray uploadFile(MultipartFile[] multipartFile) {
        StringBuilder result = new StringBuilder("[");
        if (multipartFile != null && multipartFile.length > 0) {
            for (int i = 0; i < multipartFile.length; i++) {
                String baseUrl = iFileCommonService.promptUpload(multipartFile[i]);
                JSONObject entries = new JSONObject();
                entries.set("fileUrl", baseUrl);
                result.append(entries + ",");
            }
        }
        String jsonString = "[" + result.substring(1, result.length() - 1) + "]";
        return new JSONArray(jsonString);
    }

    /**
     * 查询首页轮播图，存储首页展示的轮播图片及其链接
     *
     * @param id 首页轮播图，存储首页展示的轮播图片及其链接主键
     * @return 首页轮播图，存储首页展示的轮播图片及其链接
     */
    @Override
    public HomepageCarousel selectHomepageCarouselById(Long id) {
        return homepageCarouselMapper.selectHomepageCarouselById(id);
    }

    /**
     * 查询首页轮播图
     *
     * @param homepageCarousel 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 首页轮播图，存储首页展示的轮播图片及其链接
     */
    @Override
    public List<HomepageCarousel> selectHomepageCarouselList(HomepageCarousel homepageCarousel) {
        return homepageCarouselMapper.selectHomepageCarouselList(homepageCarousel);
    }

    /**
     * 查询首页轮播图（有展示信息）
     *
     * @param homepageCarousel 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 首页轮播图，存储首页展示的轮播图片及其链接集合
     */
    @Override
    public List<HomepageCarousel> selectHomepageCarouselDisplayList(HomepageCarousel homepageCarousel) {
        return homepageCarouselMapper.selectHomepageCarouselDisplayList(homepageCarousel);
    }

    /**
     * 新增首页轮播图，存储首页展示的轮播图片及其链接
     *
     * @param homepageCarouselInsertDto 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 结果
     */
    @Override
    public int insertHomepageCarousel(HomepageCarouselInsertDto homepageCarouselInsertDto) {
        homepageCarouselInsertDto.setCreateTime(DateUtils.getNowDate());
        homepageCarouselInsertDto.setCreateBy(SecurityUtils.getUsername());
        return homepageCarouselMapper.insertHomepageCarousel(homepageCarouselInsertDto);
    }

    /**
     * 修改首页轮播图，存储首页展示的轮播图片及其链接
     *
     * @param homepageCarouselEditDto 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 结果
     */
    @Override
    public int updateHomepageCarousel(HomepageCarouselEditDto homepageCarouselEditDto) {
        homepageCarouselEditDto.setUpdateTime(DateUtils.getNowDate());
        homepageCarouselEditDto.setUpdateBy(SecurityUtils.getUsername());
        return homepageCarouselMapper.updateHomepageCarousel(homepageCarouselEditDto);
    }

    /**
     * 修改首页轮播图展示顺序
     *
     * @param homepageCarouselEditDisplayOrderDto 首页轮播图信息列表
     * @return 结果
     */
    @Override
    public int updateHomepageCarouselDisplayOrder(HomepageCarouselEditDisplayOrderDto homepageCarouselEditDisplayOrderDto) {
        if (homepageCarouselEditDisplayOrderDto.getHomepageCarouselList().isEmpty()) {
            return homepageCarouselMapper.resetHomepageCarouselDisplayOrder();
        }
        homepageCarouselMapper.resetHomepageCarouselDisplayOrder();
        return homepageCarouselMapper.updateHomepageCarouselDisplayOrder(homepageCarouselEditDisplayOrderDto.getHomepageCarouselList());
    }

    /**
     * 批量删除首页轮播图，存储首页展示的轮播图片及其链接
     *
     * @param ids 需要删除的首页轮播图，存储首页展示的轮播图片及其链接主键
     * @return 结果
     */
    @Override
    public int deleteHomepageCarouselByIds(Long[] ids) {
        return homepageCarouselMapper.deleteHomepageCarouselByIds(ids);
    }

    /**
     * 删除首页轮播图，存储首页展示的轮播图片及其链接信息
     *
     * @param id 首页轮播图，存储首页展示的轮播图片及其链接主键
     * @return 结果
     */
    @Override
    public int deleteHomepageCarouselById(Long id) {
        return homepageCarouselMapper.deleteHomepageCarouselById(id);
    }


    private File MultipartFile2File(MultipartFile multipartFile) {
        // 获取原始文件名和扩展名
        String extension = FileNameUtils.getFileExtension(multipartFile.getOriginalFilename());
        // 使用当前时间戳生成新的文件名（不使用UUID）
        long timestamp = System.currentTimeMillis();
        String newFileName = timestamp + "." + extension;

        // 确保文件名使用UTF-8编码
        newFileName = new String(newFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        // Linux系统下的文件存储目录
        String directoryPath = "/home/carouse";
        File directory = new File(directoryPath);

        // 如果目录不存在，则创建目录
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                throw new RuntimeException("Failed to create directory: " + directoryPath);
            }
        }

        // 完整的文件路径
        String filePath = directoryPath + "/" + newFileName;
        File file = new File(filePath);

        // 将MultipartFile转换为File
        try (InputStream inputStream = multipartFile.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error occurred while processing the file.", ex);
        }

        return file;
    }
}
