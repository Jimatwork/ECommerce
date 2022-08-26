package com.kubian.mode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 交易明细表
 * @author HD
 * @Title: RunningTab
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/7/00714:46
 */
@Entity
@Table(name = "runningTab")
public class RunningTab implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 商家id
    private Long bid;
    // 交易金额
    private String money;
    // 交易时间
    private Date createDate = new Date();
    // 状态 (0.支出 1.收入)
    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
