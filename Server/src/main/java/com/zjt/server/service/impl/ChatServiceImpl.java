package com.zjt.server.service.impl;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Chat;
import com.zjt.server.service.ChatService;
import com.zjt.server.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
* @author Tao
* @description 针对表【chat】的数据库操作Service实现
* @createDate 2023-11-18 00:45:12
*/
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService{


    @Autowired
    ChatMapper chatMapper;

    @Override
    public void send(String sender, String content) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Chat chat = new Chat();
        chat.setDate(calendar.getTime());
        chat.setSender(sender);
        chat.setContent(content);
        this.save(chat);
    }

    @Override
    public void delete(int hid) {
        chatMapper.deleteById(hid);
    }
}




