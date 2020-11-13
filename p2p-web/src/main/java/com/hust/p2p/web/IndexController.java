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

//一上来，先查询三个参数值和三种分类的产品信息。
// 重点在于select
//        <include refid="Base_Column_List"/>
//    from
//        b_loan_info
//    <where>
//      <if test="productType != null">
//        product_type = #{productType}
//      </if>
//    </where>
//    order by
//        release_time desc
//    limit #{currentPage},#{pageSize}
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
        //前端显示的是第几页，从1开始；后端sql语句中两个参数，第一个参数是从哪一个开始，第二个参是偏移量，其中第一个参是从0开始
        //loanInfoService.queryLoanInfoListByProductType(产品类型, 页码, 每页显示条数);

        //传递参数采用hashMap，将产品类型，页码和每页显示条数封装到map中
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage", 0);//从数据表中的第一个开始，mysql是从初始值为0开始为第一个。

        //获取新手宝产品：产品类型：0；显示第1页；每页显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);//取出一个
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取优选产品：产品类型：1；显示第1页；每页显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);//取出4个
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取散标产品：产品类型：2；显示第2页；每页显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);//取出8个
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("xLoanInfoList", xLoanInfoList);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        model.addAttribute("sLoanInfoList", sLoanInfoList);

        return "index";
    }

}
