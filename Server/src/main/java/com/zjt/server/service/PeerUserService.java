package com.zjt.server.service;

import com.zjt.server.entity.PeerUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【peer_user】的数据库操作Service
* @createDate 2023-10-31 23:39:31
*/

public interface PeerUserService extends IService<PeerUser> {

    PeerUser getByUsername(String username);

    String getUserAuthorityInfo(String no);

    void insertStudent(PeerUser peerUser);

    void removeStu(String sno);

    void insertTeacher(PeerUser peerUser);

    void removeTea(String sno);
}
