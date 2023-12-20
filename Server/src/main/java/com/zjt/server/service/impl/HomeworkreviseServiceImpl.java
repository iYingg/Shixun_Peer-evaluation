package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Homeworkrevise;
import com.zjt.server.service.HomeworkreviseService;
import com.zjt.server.mapper.HomeworkreviseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【homeworkrevise】的数据库操作Service实现
* @createDate 2023-11-16 22:42:52
*/
@Service
public class HomeworkreviseServiceImpl extends ServiceImpl<HomeworkreviseMapper, Homeworkrevise>
    implements HomeworkreviseService{


    @Autowired
    HomeworkreviseMapper homeworkreviseMapper;

    @Override
    public void updateRevise(int hid, String reviser, String commitsno, int score, String remark) {

        homeworkreviseMapper.updateByRevise(hid,reviser,commitsno,score,remark);
    }

    @Override
    public void revoke(int hid, String reviser, String commitsno) {
        homeworkreviseMapper.revoke(hid,reviser,commitsno);
    }

    @Override
    public void insert(Homeworkrevise homeworkrevise) {
        homeworkreviseMapper.insertByHomeworkrevise(homeworkrevise.getHid(),homeworkrevise.getReviser(),homeworkrevise.getCommitsno());
    }
}




