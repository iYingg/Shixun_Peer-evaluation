package com.zjt.server.mapper;

import com.zjt.server.entity.Teaching;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tao
* @description 针对表【teaching】的数据库操作Mapper
* @createDate 2023-11-16 10:47:23
* @Entity com.zjt.server.entity.Teaching
*/
@Mapper
public interface TeachingMapper extends BaseMapper<Teaching> {

    String getTnoByCno(String cno);

    void deleteByCno(String cno);

    void insertteacheing(Teaching teaching);
}




