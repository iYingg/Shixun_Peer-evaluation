package com.zjt.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.ChatService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commu/chat")
public class ChatController {


    @Autowired
    ChatService chatService;

    /**
     * 查找所有发送记录
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();

        Page<Chat> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = chatService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Chat>
                    ().inSql("ChatID","select ChatID from chat"));
        }else {
            pageResult = chatService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Chat>
                    ().inSql("ChatID","select ChatID from chat WHERE Content like '%"+query+"%'"));
        }

        List<Chat> chatList = pageResult.getRecords();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("chatList",chatList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 发送
     * @param sender
     * @param content
     * @return
     */
    @PostMapping("/send")
    public R send(@RequestParam String sender, @RequestParam String content){
        chatService.send(sender,content);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestParam int hid){
        chatService.delete(hid);
        return R.ok();
    }
}
