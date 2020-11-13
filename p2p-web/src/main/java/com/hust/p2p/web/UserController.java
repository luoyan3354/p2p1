package com.hust.p2p.web;

import com.hust.p2p.common.constant.Constants;
import com.hust.p2p.model.user.User;
import com.hust.p2p.model.vo.ResultObject;
import com.hust.p2p.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/loan/checkPhone")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam(value = "phone", required = true) String phone){
        //查询手机号是否重复
        //根据查到的user对象是否为空
        //准备map作为返回的json格式
        Map<String, Object> retMap = new HashMap<String, Object>();

        User user = userService.queryUserByPhone(phone);

        if(user == null){
            retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
        }else{
            retMap.put(Constants.ERROR_MESSAGE, "该手机号码已被注册，请直接登录");
        }

        return retMap;
    }


    @RequestMapping("loan/checkCaptcha")
    @ResponseBody
    public Map<String, Object> checkCaptcha(HttpServletRequest request,
                                            @RequestParam(value = "captcha", required = true) String captcha){
        Map<String, Object> retMap = new HashMap<String, Object>();

        //获取session中的图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);

        //验证图形验证码是否一致
        if(!StringUtils.equalsIgnoreCase(sessionCaptcha, captcha)){
            retMap.put(Constants.ERROR_MESSAGE, "图形验证码输入错误");
        }else{
            retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
        }

        return retMap;
    }

    @RequestMapping(value = "loan/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "phone", required = true)String phone,
                           @RequestParam(value = "loginPassword", required = true)String loginPassword,
                           @RequestParam(value = "replayLoginPassword", required = true)String replayLoginPassword){
        Map<String, Object> retMap = new HashMap<String, Object>();

        //验证参数
        if(!Pattern.matches("^1[1-9]\\d{9}$", phone)){
            retMap.put(Constants.ERROR_MESSAGE, "后端验证：请输入正确的手机号");
            return retMap;
        }

        if(!StringUtils.equals(loginPassword, replayLoginPassword)){
            retMap.put(Constants.ERROR_MESSAGE, "后端验证：两次密码输入不一致");
            return retMap;
        }

        //用户注册，返回的是一个vo对象，自定义了错误码
        ResultObject resultObject = userService.register(phone, loginPassword);

        //判断是否注册成功
        if(!StringUtils.equals(Constants.SUCCESS, resultObject.getErrorCode())){
            retMap.put(Constants.ERROR_MESSAGE, "注册失败，请稍后重试...");
            return retMap;
        }

        //将用户的信息存放到session中
        request.getSession().setAttribute(Constants.SESSION_USER, userService.queryUserByPhone(phone));

        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;

    }

}
