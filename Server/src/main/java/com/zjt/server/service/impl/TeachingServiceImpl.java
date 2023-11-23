package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Teaching;
import com.zjt.server.service.TeachingService;
import com.zjt.server.mapper.TeachingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【teaching】的数据库操作Service实现
* @createDate 2023-11-16 10:47:23
*/
@Service
public class TeachingServiceImpl extends ServiceImpl<TeachingMapper, Teaching>
    implements TeachingService{

    @Autowired
    TeachingMapper teachingMapper;

    @Override
    public void insertByTeaching(Teaching teaching) {
        teachingMapper.insertteacheing(teaching);
    }

    @Override
    public String getTnoByCno(String cno) {
        return teachingMapper.getTnoByCno(cno);
    }

    @Override
    public void deleteByTeaching(String  cno) {
        teachingMapper.deleteByCno(cno);
    }
}




