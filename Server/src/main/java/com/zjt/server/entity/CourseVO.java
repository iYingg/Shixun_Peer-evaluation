package com.zjt.server.entity;

import lombok.Data;

/**
 * 传到前端的课程
 */
@Data
public class CourseVO {

    private String cno;

    private String cname;

    private Integer hours;

    private String tname;

    private String tno;

    private Integer count;//多少人选修了课程

    private Integer isChosen;//学生是否选修了该课

    private Integer hascommit;//已经完成作业

    private Integer allcommit;//所有作业

    private String homeworkstatus;//学生完成作业情况

    private Integer hasrevise;//已经批改

    private Integer allrevise;//全部需要修改

    private String revisestatus;//学生互评作业情况
    public CourseVO(Course course, String tname,String tno) {
        this.tname = tname;
        this.cno = course.getCno();
        this.cname = course.getCname();
        this.hours = course.getHours();
        this.tno = tno;
        this.isChosen = 0;
    }




}
