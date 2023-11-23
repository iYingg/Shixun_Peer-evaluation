package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Sc;
import com.zjt.server.service.ScService;
import com.zjt.server.mapper.ScMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【sc】的数据库操作Service实现
* @createDate 2023-11-16 10:47:23
*/
@Service
public class ScServiceImpl extends ServiceImpl<ScMapper, Sc>
    implements ScService{

    @Autowired
    ScMapper scMapper;

    @Override
    public void insertByCnoandSno(String cno, String sno) {
        scMapper.insertByCnoandName(cno,sno);
    }

    @Override
    public void dropByCnoandSno(String cno, String sno) {
        scMapper.dropByCnoandName(cno,sno);
    }
}




