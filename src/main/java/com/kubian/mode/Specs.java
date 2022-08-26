package com.kubian.mode;

import javax.persistence.*;

/**
 * 规格表
 *
 * @author WangW
 */
@Entity
@Table(name = "Specs")
public class Specs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long pid;

    //规格名称
    private String specsName;

    //规格价格
    private Double price;

    //团购价
    private Double discount;

    //库存
    private Integer inventory;

    //销量
    //private Integer soldcountSpecs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecsName() {
        return specsName;
    }

    public void setSpecsName(String specsName) {
        this.specsName = specsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

//    public Integer getSoldcountSpecs() {
//        return soldcountSpecs;
//    }
//
//    public void setSoldcountSpecs(Integer soldcountSpecs) {
//        this.soldcountSpecs = soldcountSpecs;
//    }
}
