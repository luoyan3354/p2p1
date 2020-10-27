package com.hust.p2p.service.loan;

import com.hust.p2p.model.loan.BidInfo;

import java.util.List;

public interface BidInfoService {

    /**
     * service接口中：平台累计投资金额
     * @return
     */
    Double queryAllBidMoney();

    /**
     * 根据产品标识获取产品的所有投资记录（包含用户信息）
     * @param loanId
     * @return
     */
    List<BidInfo> queryBidInfoListByLoanId(Integer loanId);
}
