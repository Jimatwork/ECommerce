package com.kubian.mode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 商家表
 *
 * @author WangW
 */
@Entity
@Table(name = "Business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商家名
    private String name;

    //商家描述
    private String description;

    //运营状态(1:待审核 2:已上线 3:已下线)
    private Integer status;

    //商家联系电话
    private String phone;

    //店铺地址
    private String address;

    //店铺logo
    private String logo;

    //注册时间
    private Date regTime;

    //余额
    private Double money;

    //置顶(否:0 是:1)
    private Integer sort = 0;

    // 交易明细
    @Transient
    private List<RunningTab> runningTabs;

    //所属分类ID
    private Long bid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RunningTab> getRunningTabs() {
        return runningTabs;
    }

    public void setRunningTabs(List<RunningTab> runningTabs) {
        this.runningTabs = runningTabs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }
}
