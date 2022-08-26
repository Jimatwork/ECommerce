package com.kubian.mode;

import javax.persistence.*;
import java.util.List;

/**
 * 商品表
 *
 * @author WangW
 */
@Entity
@Table(name = "Product")
public class Product {

    //商品ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品名称
    private String name;

    //商品描述
    @Column(length = 500000000)
    private String detail;

    //商品图片
    private String image;

    //商家ID
    private Long bid;

    //商品最低价格
    private String priceInterval;

    @Transient
    private Business business;

    //商品底部介绍
    @Column(length = 500000000)
    private String introduction;

    //商品规格
    @Transient
    private List<Specs> specsList;

    //商品介绍轮播图
    @Transient
    private List<MediaFile> introductionPhoto;

    //是否上下架(0:上架 1:下架)
    private Integer isClose;

    //商品状态(0:正常 1:禁用)
    private Integer status;

    //快递费用
    private Double expressMoney;

    //销量
    private Integer soldcount = 0;

    // 1.热销商品 null普通商品
    private Integer slhc;

    // 1.促销商品   null普通商品
    private Integer sales;

    //推荐商品 0:是 1:否
    private Integer isSuggest=1;
    // 二级栏目id
    private Long sIds;
    // 一级栏目id
    private Long sId;
    //评论列表
    @Transient
    private List<ProductComment> productCommentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public Long getsIds() {
        return sIds;
    }

    public void setsIds(Long sIds) {
        this.sIds = sIds;
    }

    public Integer getSlhc() {
        return slhc;
    }

    public void setSlhc(Integer slhc) {
        this.slhc = slhc;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<Specs> getSpecsList() {
        return specsList;
    }

    public void setSpecsList(List<Specs> specsList) {
        this.specsList = specsList;
    }

    public List<MediaFile> getIntroductionPhoto() {
        return introductionPhoto;
    }

    public void setIntroductionPhoto(List<MediaFile> introductionPhoto) {
        this.introductionPhoto = introductionPhoto;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getExpressMoney() {
        return expressMoney;
    }

    public void setExpressMoney(Double expressMoney) {
        this.expressMoney = expressMoney;
    }

    public Integer getSoldcount() {
        return soldcount;
    }

    public void setSoldcount(Integer soldcount) {
        this.soldcount = soldcount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ProductComment> getProductCommentList() {
        return productCommentList;
    }

    public void setProductCommentList(List<ProductComment> productCommentList) {
        this.productCommentList = productCommentList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPriceInterval() {
        return priceInterval;
    }

    public void setPriceInterval(String priceInterval) {
        this.priceInterval = priceInterval;
    }

    public Integer getIsSuggest() {
        return isSuggest;
    }

    public void setIsSuggest(Integer isSuggest) {
        this.isSuggest = isSuggest;
    }
}
