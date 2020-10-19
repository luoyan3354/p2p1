package com.hust.p2p.web;


import com.hust.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;

    //controller层只会面向service接口层取数据这件事，不会涉及到service层中的取redis缓存或mapper层数据这样的事。
    /*private RedisTemplate<Object, Object> redisTemplate;*/

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model){

        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute("historyAverageRate", historyAverageRate);

        //获取平台累计投资金额


        //获取新手宝产品


        //获取优选产品


        //获取散标产品


        return "test";
    }

}