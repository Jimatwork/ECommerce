package com.kubian.mode;

import javax.persistence.*;

@Entity
@Table(name = "ReceivingAddress")
public class ReceivingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //用户ID
    private Long appuserId;

    //收获地址
    private String address;

    //收货人
    private String consignee;

    //联系电话
    private String contactNumber;

    //是否是默认地址(默认:1 非默认:2)
    private Integer isDefault;

    //详细地址
    private String detailAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(Long appuserId) {
        this.appuserId = appuserId;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
