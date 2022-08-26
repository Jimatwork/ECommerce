package com.kubian.mode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 商品二级级分类
 * @author HD
 * @Title: ShopClassify
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/10/16/01615:19
 */
@Entity
@Table(name = "shopClassifySon")
public class ShopClassifySon implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 分类名称
    private String name;
    // 分类图标
    private String img;
    // 分类顺序
    private Integer sequence;
    // 状态 0.显示  1.不显示
    private Integer state;
    // 一级分类id
    private Long sId;
    // 推荐的商品
    @Transient
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }
}
