package com.hust.p2p.web;

import com.hust.p2p.model.loan.LoanInfo;
import com.hust.p2p.model.vo.PaginationVO;
import com.hust.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

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

        return "loan";
    }
}
