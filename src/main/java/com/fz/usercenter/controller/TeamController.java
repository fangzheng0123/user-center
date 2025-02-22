package com.fz.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.common.ErrorCode;
import com.fz.usercenter.common.ResultUtils;
import com.fz.usercenter.exception.BusinessException;
import com.fz.usercenter.model.domain.Team;
import com.fz.usercenter.model.domain.User;
import com.fz.usercenter.model.dto.TeamQuery;
import com.fz.usercenter.model.request.TeamRequest;
import com.fz.usercenter.service.TeamService;
import com.fz.usercenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author fang
 * @Date 2025/2/21 18:43
 * @注释
 */
@RequestMapping("/team")
@RestController
public class TeamController {

    @Resource
    private TeamService teamService;
    @Resource
    private UserService userService;

    //    增
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request) {
        if (teamRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamRequest,team);
        long userId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(userId);
    }

    //    删
    @PostMapping("delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    //    改
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"修改失败");
        }
        return ResultUtils.success(true);
    }
//    查
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id){
        if (id< 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"请求数据为空");
        }
        return ResultUtils.success(team);
    }

//    遍历
    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(team,teamQuery);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);
    }

//    分页
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamByPage(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(teamPage);
    }

}
