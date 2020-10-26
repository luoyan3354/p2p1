package com.hust.p2p.service.impl.loan;

import com.hust.p2p.common.constant.Constants;
import com.hust.p2p.mapper.loan.LoanInfoMapper;
import com.hust.p2p.model.loan.LoanInfo;
import com.hust.p2p.model.vo.PaginationVO;
import com.hust.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//Autowired是从spring的容器中取出与之类型同源的对象，赋给它。
@Service("loanInfoServiceImpl")//这里是创建这个serviceImpl对象，并且给它取个名字。不取的话就是默认类名首字母小写。
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    //获取历史年化收益率
    @Override
    public Double queryHistoryAverageRate() {

        //直接从redis缓存中通过键取值，如果有直接使用；没有的话再从mysql数据库中查询，查到之后把键值对放入到redis缓存中，并且返回这个值。

        //修改redis中的key的名称，改成只有我存的key，不加前面的序列化码
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //redisTemplate.opsForValue()就是拿到key和value这种类型的operation操作对象。
        //Constants定义一个常量类，里面存放所有的常量与对应的字符串的关系，方便管理
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);

        //若redis缓存中为null，则去mapper层中的数据库中取
        if (historyAverageRate == null) {
            //从数据库中查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            //将该键值对存入到redis缓存中
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.MINUTES);
        }

        return historyAverageRate;
    }

    //获取产品的列表，根据产品的类型、页码和每页展示几个
    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {

        PaginationVO<LoanInfo> paginationVO = new PaginationVO<>();

        //查询总记录数
        Long total = loanInfoMapper.selectTotal(paramMap);
        paginationVO.setTotal(total);//根据产品类型查出记录数

        //查询显示数据
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(paramMap);
        paginationVO.setDataList(loanInfoList);

        return paginationVO;
    }
}
