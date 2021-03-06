package com.hust.p2p.service.loan;

import com.hust.p2p.model.loan.LoanInfo;
import com.hust.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {

    /**
     * service接口：返回历史平均年化收益率
     * @return
     */
    Double queryHistoryAverageRate();

    /**
     * 根据产品类型获取产品信息列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 分页查询产品信息列表和总记录数
     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据产品标识表示获取产品的详情
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}
