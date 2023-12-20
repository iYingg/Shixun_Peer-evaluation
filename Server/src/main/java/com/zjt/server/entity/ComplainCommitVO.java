package com.zjt.server.entity;

import com.zjt.server.util.DateForm;
import lombok.Data;

@Data
public class ComplainCommitVO {
    private Integer complainid;
    private String sno;
    private String cno;
    private String tname;
    private String commitcontent;
    private String respond;
    private String commitdate;
    private String responddate;
    private String respondcontent;


    public ComplainCommitVO(Complaincommit complaincommit,String tname) {
        this.complainid = complaincommit.getComplainid();
        this.sno = complaincommit.getSno();
        this.tname = tname;
        this.cno = complaincommit.getCno();
        this.commitcontent = complaincommit.getContent();
        this.respond = complaincommit.getRespond();
        this.commitdate = DateForm.DatetoString(complaincommit.getDate());
    }

    public ComplainCommitVO(Complaincommit complaincommit,Complainhandle complainhandle,String tname) {
        this.complainid = complaincommit.getComplainid();
        this.sno = complaincommit.getSno();
        this.tname = tname;
        this.cno = complaincommit.getCno();
        this.commitcontent = complaincommit.getContent();
        this.respond = complaincommit.getRespond();
        this.commitdate = DateForm.DatetoString(complaincommit.getDate());
        this.respondcontent = complainhandle.getHandlecontent();
        this.responddate = DateForm.DatetoString(complainhandle.getHandledate());
    }
}
