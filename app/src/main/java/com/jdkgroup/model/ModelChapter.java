package com.jdkgroup.model;

import java.util.Date;

public class ModelChapter
{
    private int uuidchpater;
    private int no;
    private String name;
    private int counttopic;
    private Date date;

    public ModelChapter(int no, String name, int counttopic) {
        this.no = no;
        this.name = name;
        this.counttopic = counttopic;
    }

    public int getUuidchpater() {
        return uuidchpater;
    }

    public void setUuidchpater(int uuidchpater) {
        this.uuidchpater = uuidchpater;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounttopic() {
        return counttopic;
    }

    public void setCounttopic(int counttopic) {
        this.counttopic = counttopic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
