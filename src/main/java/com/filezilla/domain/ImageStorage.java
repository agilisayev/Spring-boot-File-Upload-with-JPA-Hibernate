package com.filezilla.domain;

import javax.persistence.*;
import java.util.Date;

@Entity()
@Table(name = "t_image_storage",schema = "filezilla")
public class ImageStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private Integer viewCount=0;

    private Long fileSize=0L;

    public ImageStorage() {
    }

    public ImageStorage(String filePath, Date createDate, Integer viewCount, Long fileSize) {
        this.filePath = filePath;
        this.createDate = createDate;
        this.viewCount = viewCount;
        this.fileSize = fileSize;
    }

    @PrePersist
    void setCreateDate(){
        this.createDate=new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
