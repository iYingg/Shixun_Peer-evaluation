package com.zjt.server.mapper;

import com.zjt.server.entity.Homeworkcommit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Tao
* @description 针对表【homeworkcommit】的数据库操作Mapper
* @createDate 2023-11-17 15:35:55
* @Entity com.zjt.server.entity.Homeworkcommit
*/
public interface HomeworkcommitMapper extends BaseMapper<Homeworkcommit> {

    void deleteByhidandSno(int hid, String sno);

    void insertByhomeworkCommit(Homeworkcommit homeworkcommit);
}




