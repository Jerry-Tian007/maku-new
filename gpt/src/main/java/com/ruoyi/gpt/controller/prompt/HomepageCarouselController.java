package com.ruoyi.gpt.controller.prompt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.gpt.model.domain.HomepageCarousel;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselEditDisplayOrderDto;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselEditDto;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselInsertDto;
import com.ruoyi.gpt.service.IHomepageCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 首页轮播图，存储首页展示的轮播图片及其链接Controller
 *
 * @author Liujr
 * @date 2024-08-13
 */
@RestController
@RequestMapping("/gpt/carousel")
public class HomepageCarouselController extends BaseController {
    @Autowired
    private IHomepageCarouselService homepageCarouselService;

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return 文件信息
     */
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile[] multipartFile) {
        List<String> range = new ArrayList<>(Arrays.asList("jpg", "png","jpeg"));
        for (MultipartFile file : multipartFile) {
            if (!range.contains(FileNameUtils.getFileExtension(file.getOriginalFilename()))) {
                return AjaxResult.error("操作失败","文件格式不支持");
            }
        }
        return AjaxResult.success(homepageCarouselService.uploadFile(multipartFile));
    }

    /**
     * 查询首页轮播图，存储首页展示的轮播图片及其链接列表
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:list')")
    @GetMapping("/list")
    public TableDataInfo listNoDisplayOrder(HomepageCarousel homepageCarousel) {
        startPage();
        List<HomepageCarousel> list = homepageCarouselService.selectHomepageCarouselList(homepageCarousel);
        return getDataTable(list);
    }

    /**
     * 查询首页轮播图，存储首页展示的轮播图片及其链接列表
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:list')")
    @GetMapping("/list-display")
    public AjaxResult listDisplayOrder(HomepageCarousel homepageCarousel) {
        List<HomepageCarousel> list = homepageCarouselService.selectHomepageCarouselDisplayList(homepageCarousel);
        return AjaxResult.success(list);
    }

    /**
     * 新增首页轮播图，存储首页展示的轮播图片及其链接
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:add')")
    @Log(title = "首页轮播图，存储首页展示的轮播图片及其链接", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody HomepageCarouselInsertDto homepageCarouselInsertDto) {
        homepageCarouselService.insertHomepageCarousel(homepageCarouselInsertDto);
        return AjaxResult.success("新增成功", homepageCarouselInsertDto);
    }

    /**
     * 修改首页轮播图，存储首页展示的轮播图片及其链接
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:edit')")
    @Log(title = "首页轮播图，存储首页展示的轮播图片及其链接", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody HomepageCarouselEditDto homepageCarouselEditDto) {
        homepageCarouselService.updateHomepageCarousel(homepageCarouselEditDto);
        return AjaxResult.success("修改成功",homepageCarouselEditDto);
    }
    /**
     * 修改首页轮播图展示状态
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:edit')")
    @Log(title = "首页轮播图，存储首页展示的轮播图片及其链接", businessType = BusinessType.UPDATE)
    @PostMapping("/display-order")
    public AjaxResult edit(@RequestBody HomepageCarouselEditDisplayOrderDto homepageCarouselEditDisplayOrderDto) {
        homepageCarouselService.updateHomepageCarouselDisplayOrder(homepageCarouselEditDisplayOrderDto);
        return AjaxResult.success("修改成功",homepageCarouselEditDisplayOrderDto);
    }

    /**
     * 删除首页轮播图，存储首页展示的轮播图片及其链接
     */
    @PreAuthorize("@ss.hasPermi('carousel:carousel:remove')")
    @Log(title = "首页轮播图，存储首页展示的轮播图片及其链接", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids) {
        return success(homepageCarouselService.deleteHomepageCarouselByIds(ids));
    }


}
