package com.zjt.server.service;

import com.zjt.server.entity.Teaching;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tao
* @description 针对表【teaching】的数据库操作Service
* @createDate 2023-11-16 10:47:23
*/
public interface TeachingService extends IService<Teaching> {

    void insertByTeaching(Teaching teaching);

    String getTnoByCno(String cno);

    void deleteByTeaching(String cno);
}
