package com.zjt.server.mapper;

import com.zjt.server.entity.PeerUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Tao
* @description 针对表【peer_user】的数据库操作Mapper
* @createDate 2023-10-31 23:39:31
* @Entity com.zjt.server.entity.PeerUser
*/
@Mapper
@Repository
public interface PeerUserMapper extends BaseMapper<PeerUser> {

    void insertStudent(PeerUser peerUser);

    void removeStu(String sno);

    void insertTeacher(PeerUser peerUser);
}




