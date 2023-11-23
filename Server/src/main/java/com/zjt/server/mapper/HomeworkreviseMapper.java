package com.zjt.server.mapper;

import com.zjt.server.entity.Homeworkrevise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Tao
* @description 针对表【homeworkrevise】的数据库操作Mapper
* @createDate 2023-11-16 22:42:52
* @Entity com.zjt.server.entity.Homeworkrevise
*/
public interface HomeworkreviseMapper extends BaseMapper<Homeworkrevise> {

    void updateByRevise(int hid, String reviser, String commitsno, int score, String remark);

    void revoke(int hid, String reviser, String commitsno);
}




