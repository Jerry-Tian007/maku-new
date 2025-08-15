package com.ruoyi.gpt.model.dto.homepage_carousel;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 首页轮播图，存储首页展示的轮播图片及其链接对象 homepage_carousel
 * 
 * @author Liujr
 * @date 2024-08-13
 */
@Data
public class HomepageCarouselInsertDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键，唯一标识每个首页轮播项 */
    private Long id;

    /** 首页轮播图片url */
    @Excel(name = "首页轮播图片url")
    private String imageUrl;

    /** 点击图片时跳转的链接 */
    @Excel(name = "点击图片时跳转的链接")
    private String linkUrl;

    /** 显示顺序，决定轮播图的排列 */
    @Excel(name = "显示顺序，决定轮播图的排列")
    private Long displayOrder;

    /** 备注 */
    private String remark;

}
