package com.food.fctfood.controller;

import com.food.fctfood.annotation.RateLimiter;
import com.food.fctfood.common.MessageCode;
import com.food.fctfood.model.TbFct;
import com.food.fctfood.model.TbUserInvication;
import com.food.fctfood.request.BaseRq;
import com.food.fctfood.request.UserBackRq;
import com.food.fctfood.request.UserInvicationRq;
import com.food.fctfood.request.UserRq;
import com.food.fctfood.response.*;
import com.food.fctfood.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
@Api(value = "/user", description = "fct综合接口")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    @ApiOperation("发送验证码邮件")
    public Response<String> sendEmail(@RequestBody UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.sendEmail(userRq.getEmail());
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(value = "sendFindPwdEmail", method = RequestMethod.POST)
    @ApiOperation("忘记密码验证码邮件")
    public Response<String> sendFindPwdEmail(@RequestBody UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.forgetPwd(userRq.getEmail());
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ApiOperation("用户注册")
    public Response<String> register(@RequestBody UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.register(userRq);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation("用户登录")
    public Response<UserResponseVo> login(@RequestBody UserRq userRq) {
        Response<UserResponseVo> response = ResponseUtil.ok();
        try {
            response = userService.login(userRq);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "loginout", method = RequestMethod.GET)
    @ApiOperation("用户退出")
    public Response<String> loginout(HttpServletRequest request) {
        Response<String> response = ResponseUtil.ok();
        try {
            String token = request.getHeader("token");
            response = userService.logout(token);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "vote", method = RequestMethod.POST)
    @ApiOperation("用户投资")
    public Response<String> vote(HttpServletRequest request, @RequestBody UserInvicationRq userInvicationRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            String token = request.getHeader("token");
            response = userService.vote(token, userInvicationRq);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @GetMapping
    @ApiOperation("用户的投资详情")
    public Response<List<TbUserInvication>> queryVoteDetail(HttpServletRequest request) {
        Response<List<TbUserInvication>> response = ResponseUtil.ok();
        try {
            String token = request.getHeader("token");
            response = userService.queryVoteDetail(token);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "voteload", method = RequestMethod.POST)
    @ApiOperation("投资进度")
    public Response<TbFct> voteload() {
        Response<TbFct> response = ResponseUtil.ok();
        try {
            response = userService.voteload();
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "importAddress", method = RequestMethod.POST)
    @ApiOperation("数字货币地址入库")
    public Response<String> importAddress(@RequestParam("file") MultipartFile file) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.importAddress(file);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ApiOperation("获取用户信息")
    public Response<UserResponseVo> getUserInfo(HttpServletRequest request) {
        Response<UserResponseVo> response = ResponseUtil.ok();
        try {
            String token = request.getHeader("token");
            response = userService.getUserInfo(token);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @RequestMapping(value = "forgetPassword", method = RequestMethod.POST)
    @ApiOperation("忘记密码")
    public Response<String> forgetPassword(@RequestBody UserRq userRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.updatePassword(userRq);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "loginBack", method = RequestMethod.POST)
    @ApiOperation("后台用户登录")
    public Response<String> loginBack(@RequestBody UserBackRq userBackRq) {
        Response<String> response = ResponseUtil.ok();
        try {
            response = userService.loginBack(userBackRq);
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


    @RequestMapping(value = "queryAllVoteDetail", method = RequestMethod.POST)
    @ApiOperation("查询投资流水")
    public Response<UserInvication> queryAllVoteDetail(@RequestBody BaseRq baseRq) {
        Response<UserInvication> response = ResponseUtil.ok();
        try {
            response = userService.queryAllVoteDetail(baseRq.getPageIndex(), baseRq.getPageRow());
        } catch (Exception e) {
            response.setCode(MessageCode.EXCEPTION_ERROR);
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }


}
