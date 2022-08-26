package com.kubian.mode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 轮播图
 *
 * @author WangW
 */
@Entity
@Table(name = "BannerService")
public class BannerService implements Serializable {

    private Long id;
    // Banner图片地址
    private String img;
    // 链接商品的ID
    private Long linkId;
    // 备注
    private String remark;
    // 链接地址
    private String linkUrl;
    //类型(1:店铺轮播图 2:首页轮播图)
    private Integer type;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
