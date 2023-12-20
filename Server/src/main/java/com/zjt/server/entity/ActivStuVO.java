package com.zjt.server.entity;

import lombok.Data;

@Data
public class ActivStuVO {
    private String sno;
    private String sname;
    private int finalscore;

    public ActivStuVO(PeerUser peerUser, Object finalscore) {
        this.sno = peerUser.getNo();
        this.sname = peerUser.getName();
        if(finalscore != null)
            this.finalscore = Integer.parseInt(finalscore.toString());
        else
            this.finalscore = -1;
    }
}
