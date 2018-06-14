package com.hackathon2018.udaan.Models;

/**
 * Created by aniketvishal on 31/03/18.
 */

public class FindJobs {

    String title,desc,sector,salary;

    public FindJobs() {
    }

    public FindJobs(String title, String desc, String sector, String salary) {
        this.title = title;
        this.desc = desc;
        this.sector = sector;
        this.salary = salary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
