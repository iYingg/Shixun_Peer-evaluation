package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Complaincommit;
import com.zjt.server.service.ComplaincommitService;
import com.zjt.server.mapper.ComplaincommitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【complaincommit】的数据库操作Service实现
* @createDate 2023-11-18 00:45:12
*/
@Service
public class ComplaincommitServiceImpl extends ServiceImpl<ComplaincommitMapper, Complaincommit>
    implements ComplaincommitService{

    @Autowired
    ComplaincommitMapper complaincommitMapper;

    @Override
    public void updaterespond(Integer complainid) {
        UpdateWrapper<Complaincommit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("complainid",complainid).set("respond","Y");
        complaincommitMapper.update(null,updateWrapper);
    }

    @Override
    public void updaterespond2(Integer complainid) {
        UpdateWrapper<Complaincommit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("complainid",complainid).set("respond","N");
        complaincommitMapper.update(null,updateWrapper);
    }
}




