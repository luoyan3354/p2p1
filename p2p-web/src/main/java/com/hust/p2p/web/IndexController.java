package com.hust.p2p.web;


import com.hust.p2p.common.constant.Constants;
import com.hust.p2p.model.loan.LoanInfo;
import com.hust.p2p.service.loan.BidInfoService;
import com.hust.p2p.service.loan.LoanInfoService;
import com.hust.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidInfoService bidInfoService;

    //controller层只会面向service接口层取数据这件事，不会涉及到service层中的取redis缓存或mapper层数据这样的事。
    /*private RedisTemplate<Object, Object> redisTemplate;*/

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model){

        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);

        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);

        //获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);

        //将以下三个查询看成是一个分页，通过产品类型查出产品的List列表。再通过页码和每页显示几个 显示出
        //loanInfoService.queryLoanInfoListByProductType(产品类型, 页码, 每页显示条数);

        //传递参数采用hashMap，将产品类型，页码和每页显示条数封装到map中
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage", 0);//将公共相同的参数页码为0（limit关键字为0开始）put到map中

        //获取新手宝产品：产品类型：0；显示第1页；每页显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取优选产品：产品类型：1；显示第1页；每页显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取散标产品：产品类型：2；显示第2页；每页显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("xLoanInfoList", xLoanInfoList);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        model.addAttribute("sLoanInfoList", sLoanInfoList);

        return "index";
    }

}
