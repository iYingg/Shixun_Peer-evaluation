package com.zjt.server.service;

import com.zjt.server.entity.Homeworkrevise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tao
* @description 针对表【homeworkrevise】的数据库操作Service
* @createDate 2023-11-16 22:42:52
*/
public interface HomeworkreviseService extends IService<Homeworkrevise> {

    void updateRevise(int hid, String resiver, String commitsno, int score, String remark);

    void revoke(int hid, String reviser, String commitsno);

    void insert(Homeworkrevise homeworkrevise);
}
