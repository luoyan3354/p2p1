package com.hust.p2p.service.impl.loan;

import com.hust.p2p.common.constant.Constants;
import com.hust.p2p.mapper.loan.BidInfoMapper;
import com.hust.p2p.service.loan.BidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryAllBidMoney() {

        //获取指定key的操作对象
        BoundValueOperations<Object,Object> boundValueOperations = redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);

        //获取指定key的value值
        Double allBidMoney = (Double) boundValueOperations.get();

        //判断是否有值
        if (allBidMoney == null) {
            //去数据库中查询
            allBidMoney = bidInfoMapper.selectAllBidMoney();

            //存放到redis缓存中
            boundValueOperations.set(allBidMoney, 15, TimeUnit.MINUTES);
        }

        return allBidMoney;
    }
}
