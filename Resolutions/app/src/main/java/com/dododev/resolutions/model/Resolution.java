package com.dododev.resolutions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dodo on 2015-12-14.
 */
public class Resolution implements Serializable{
    private Long id;
    private Date startDate;
    private Date endDate;
    private String title;
    private String description;

    private ResolutionStatusDict status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResolutionStatusDict getStatus() {
        return status;
    }

    public void setStatus(ResolutionStatusDict status) {
        this.status = status;
    }
}
