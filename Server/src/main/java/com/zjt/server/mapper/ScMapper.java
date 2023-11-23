package com.zjt.server.mapper;

import com.zjt.server.entity.Sc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tao
* @description 针对表【sc】的数据库操作Mapper
* @createDate 2023-11-16 10:47:23
* @Entity com.zjt.server.entity.Sc
*/
@Mapper
public interface ScMapper extends BaseMapper<Sc> {

    void insertByCnoandName(String cno, String sno);

    void dropByCnoandName(String cno, String sno);
}




