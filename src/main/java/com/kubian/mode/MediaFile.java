package com.kubian.mode;

import javax.persistence.*;

/**
 * 　　* @Description: TODO
 * 　　* @param ${tags}
 * 　　* @return ${return_type}
 * 　　* @throws
 * 　　* @author HD
 * 　　* @date 2018/8/23/023 15:52
 *
 */
@Entity
@Table(name = "MediaFile")
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品ID
    private Long pid;

    //地址
    private String location;

    //类型(1:商品文件 2:评论文件)
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
