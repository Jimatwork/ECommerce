package com.kubian.mode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 商品评论表
 *
 * @author WangW
 */
@Entity
@Table(name = "ProductComment")
public class ProductComment {

    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //评论类型(1:好评 2:中评 3:差评)
    private Integer type;

    //评论文字
    private String text;

    @Transient
    private List<MediaFile> mediaFileList;

    //评论人
    private Long userId;

    @Transient
    private AppUser appUser;

    //评论商品ID
    private Long pid;

    //评论商品规格ID
    private Long sid;

    @Transient
    private Specs specs;

    //评论时间
    private Date date = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    public void setMediaFileList(List<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Specs getSpecs() {
        return specs;
    }

    public void setSpecs(Specs specs) {
        this.specs = specs;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
