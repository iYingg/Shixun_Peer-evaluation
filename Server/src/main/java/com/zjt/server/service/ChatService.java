package com.zjt.server.service;

import com.zjt.server.entity.Chat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tao
* @description 针对表【chat】的数据库操作Service
* @createDate 2023-11-18 00:45:12
*/
public interface ChatService extends IService<Chat> {

    void send(String sender, String content);

    void delete(int hid);
}
