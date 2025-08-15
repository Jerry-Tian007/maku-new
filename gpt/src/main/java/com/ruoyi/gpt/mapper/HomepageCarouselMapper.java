package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.HomepageCarousel;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselEditDto;
import com.ruoyi.gpt.model.dto.homepage_carousel.HomepageCarouselInsertDto;

import java.util.List;

/**
 * 首页轮播图，存储首页展示的轮播图片及其链接Mapper接口
 * 
 * @author Liujr
 * @date 2024-08-13
 */
public interface HomepageCarouselMapper 
{
    /**
     * 查询首页轮播图，存储首页展示的轮播图片及其链接
     * 
     * @param id 首页轮播图，存储首页展示的轮播图片及其链接主键
     * @return 首页轮播图，存储首页展示的轮播图片及其链接
     */
    public HomepageCarousel selectHomepageCarouselById(Long id);

    /**
     * 查询首页轮播图，存储首页展示的轮播图片及其链接列表
     * 
     * @param homepageCarousel 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 首页轮播图，存储首页展示的轮播图片及其链接集合
     */
    public List<HomepageCarousel> selectHomepageCarouselList(HomepageCarousel homepageCarousel);
    /**
     * 查询首页轮播图（有展示信息）
     *
     * @param homepageCarousel 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 首页轮播图，存储首页展示的轮播图片及其链接集合
     */
    public List<HomepageCarousel> selectHomepageCarouselDisplayList(HomepageCarousel homepageCarousel);

    /**
     * 新增首页轮播图，存储首页展示的轮播图片及其链接
     * 
     * @param homepageCarouselInsertDto 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 结果
     */
    @GenerateUniqueId
    public int insertHomepageCarousel(HomepageCarouselInsertDto homepageCarouselInsertDto);

    /**
     * 修改首页轮播图，存储首页展示的轮播图片及其链接
     * 
     * @param homepageCarouselEditDto 首页轮播图，存储首页展示的轮播图片及其链接
     * @return 结果
     */
    public int updateHomepageCarousel(HomepageCarouselEditDto homepageCarouselEditDto);

    /**
     * 修改首页轮播图展示顺序
     *
     * @param homepageCarouselList 首页待展示轮播图信息列表
     * @return 结果
     */
    public int updateHomepageCarouselDisplayOrder(List<HomepageCarouselInsertDto> homepageCarouselList);

    /**
     * 重置首页轮播图展示顺序
     *
     * @return 结果
     */
    public int resetHomepageCarouselDisplayOrder();

    /**
     * 删除首页轮播图，存储首页展示的轮播图片及其链接
     * 
     * @param id 首页轮播图，存储首页展示的轮播图片及其链接主键
     * @return 结果
     */
    public int deleteHomepageCarouselById(Long id);

    /**
     * 批量删除首页轮播图，存储首页展示的轮播图片及其链接
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHomepageCarouselByIds(Long[] ids);
}
