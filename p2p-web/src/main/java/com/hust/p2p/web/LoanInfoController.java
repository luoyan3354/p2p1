package com.hust.p2p.web;

import com.hust.p2p.model.loan.BidInfo;
import com.hust.p2p.model.loan.LoanInfo;
import com.hust.p2p.model.vo.PaginationVO;
import com.hust.p2p.service.loan.BidInfoService;
import com.hust.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    //显示更多的产品和投资排行榜
    @RequestMapping(value = "/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "currentPage",required = false) Integer currentPage){

        //准备参数：产品类型、前端的页码(第几页)转换成数据库的起始下标；每页显示的条数
        Map<String, Object> paramMap = new HashMap<String, Object>();

        //如果产品类型不为null，则将产品类型放到参数中去。如果为null了，就不放。
        if (ptype != null) {
            paramMap.put("productType", ptype);
        }

        //第几页（考虑到uri的复用）重复提交产品类型和第几页到这个uri上面
        if(currentPage == null){//一开始点进去时（查看更多优选产品）：这个参是没有传的，是null
            currentPage = 1;//前端显示的第几页
        }

        int pageSize = 9;

        paramMap.put("currentPage", (currentPage - 1) * pageSize);
        paramMap.put("pageSize", pageSize);

        //返回分页模型对象（总记录条数，LoanInfo的List集合）
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        //再次求余，如果余数大于0，则将总页数+1
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if(mod > 0){
            totalPage++;
        }

        //总记录数
        model.addAttribute("totalRows", paginationVO.getTotal());
        //总页数
        model.addAttribute("totalPage", totalPage);
        //loanInfo的List集合
        model.addAttribute("loanInfoList", paginationVO.getDataList());
        //当前页码
        model.addAttribute("currentPage", currentPage);

        if (ptype != null) {
            //产品类型
            model.addAttribute("ptype", ptype);
        }

        //TODO
        //投资排行榜

        return "loan";
    }


    //根据单个产品的id查看产品的完整信息，和其关联的投资信息，一个产品是有多条投资记录的
    //投资记录中显示用户表的电话号码（做left join左连接）显示投资记录的完整信息和与之匹配的用户的信息
    @RequestMapping(value = "loan/loanInfo")
    public String loanInfo(HttpServletRequest request, Model model,
                           @RequestParam(value = "id", required = true) Integer id){

        //根据产品标识表示获取产品的详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);

        //根据产品id获取这个产品的所有投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoListByLoanId(id);

        model.addAttribute("loanInfo", loanInfo);
        model.addAttribute("bidInfoList", bidInfoList);

        //TODO
        //获取当前用户的账户可用余额

        return "loanInfo";
    }
}
