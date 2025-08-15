package com.ruoyi.gpt.model.dto.homepage_carousel;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 首页轮播图，存储首页展示的轮播图片及其链接对象 homepage_carousel
 * 
 * @author Liujr
 * @date 2024-08-13
 */
@Data
public class HomepageCarouselEditDisplayOrderDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图信息列表
     */
    private List<HomepageCarouselInsertDto> homepageCarouselList;

}
