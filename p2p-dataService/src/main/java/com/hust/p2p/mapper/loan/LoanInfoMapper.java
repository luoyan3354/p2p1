package com.hust.p2p.mapper.loan;

import com.hust.p2p.model.loan.LoanInfo;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKeyWithBLOBs(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 历史平均年化收益率
     * @return
     */
    Double selectHistoryAverageRate();

}