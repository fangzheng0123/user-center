package com.fz.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fz.usercenter.common.ErrorCode;
import com.fz.usercenter.exception.BusinessException;
import com.fz.usercenter.mapper.TeamMapper;
import com.fz.usercenter.model.domain.Team;
import com.fz.usercenter.model.domain.User;
import com.fz.usercenter.model.domain.UserTeam;
import com.fz.usercenter.model.enums.TeamStatusEnum;
import com.fz.usercenter.service.TeamService;
import com.fz.usercenter.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
* @author fang
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-02-21 17:05:39
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Resource
    private UserTeamService userTeamService;
    @Override
    @Transactional(noRollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
//        请求参数是否为空
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍信息为空");
        }
//        是否登录，未登录不允许创建
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        final long userId = loginUser.getId();
        /*
        校验信息
         */
//        队伍人数 > 1 且 <= 20
        Integer maxNum = team.getMaxNum();
        if (maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数只能大于1且小于20");
        }
//        队伍标题 <= 20
        String TeamName = team.getName();
        if (StringUtils.isBlank(TeamName) || TeamName.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不能超过20");
        }
//        描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"描述信息不能超过512");
        }
//        status是否公开（int）不传默认是0（公开）
        /*
        此处方法的作用是，如果传递的是空值就使用0代替
         */
        Integer status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态不满足要求");
        }
//        如果status是加密状态,一定要有密码,且密码<=32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum) && (StringUtils.isBlank(password) || password.length() > 32)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密密码设置错误");
        }
//        超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已经超时");
        }
//        校验用户最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long count = this.count(queryWrapper);
        if (count >= 5 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不能超过5");
        }
//        插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建队伍失败");
        }
//        插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        return userId;
    }
}




