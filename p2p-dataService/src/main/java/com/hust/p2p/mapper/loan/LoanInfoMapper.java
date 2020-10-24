package com.hust.p2p.mapper.loan;

import com.hust.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据页码和每页展示几个查询产品信息（分页查询产品信息）
     * @param paramMap
     * @return
     */
    List<LoanInfo> selectLoanInfoByPage(Map<String, Object> paramMap);
}