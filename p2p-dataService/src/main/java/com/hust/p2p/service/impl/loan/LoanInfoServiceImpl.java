package com.hust.p2p.service.impl.loan;

import com.hust.p2p.mapper.loan.LoanInfoMapper;
import com.hust.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//Autowired是从spring的容器中取出与之类型同源的对象，赋给它。
@Service("loanInfoServiceImpl")//这里是创建这个serviceImpl对象，并且给它取个名字。不取的话就是默认类名首字母小写。
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {

        //直接从redis缓存中通过键取值，如果有直接使用；没有的话再从mysql数据库中查询，查到之后把键值对放入到redis缓存中，并且返回这个值。
        //redisTemplate.opsForValue()就是拿到key和value这种类型的operation操作对象。
        //定义一个常量类
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get("his");

        //若redis缓存中为null，则去mapper层中的数据库中取
        if (historyAverageRate == null) {
            //从数据库中查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            //将该键值对存入到redis缓存中
            redisTemplate.opsForValue().set("his", historyAverageRate, 15, TimeUnit.MINUTES);
        }

        return historyAverageRate;
    }
}
