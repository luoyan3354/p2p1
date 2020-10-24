package com.hust.p2p.service.loan;

import com.hust.p2p.model.loan.LoanInfo;

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
}
