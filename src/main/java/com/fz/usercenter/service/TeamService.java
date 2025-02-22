package com.fz.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.usercenter.model.domain.Team;
import com.fz.usercenter.model.domain.User;

/**
* @author fang
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-02-21 17:05:39
*/
public interface TeamService extends IService<Team> {

    long addTeam(Team user, User loginUser);

}
