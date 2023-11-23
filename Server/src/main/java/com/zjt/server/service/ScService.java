package com.zjt.server.service;

import com.zjt.server.entity.Sc;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tao
* @description 针对表【sc】的数据库操作Service
* @createDate 2023-11-16 10:47:23
*/
public interface ScService extends IService<Sc> {



    void insertByCnoandSno(String cno, String sno);

    void dropByCnoandSno(String cno, String sno);
}
