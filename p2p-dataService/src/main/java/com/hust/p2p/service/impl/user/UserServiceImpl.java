package com.hust.p2p.service.impl.user;

import com.hust.p2p.common.constant.Constants;
import com.hust.p2p.mapper.user.FinanceAccountMapper;
import com.hust.p2p.mapper.user.UserMapper;
import com.hust.p2p.model.user.FinanceAccount;
import com.hust.p2p.model.user.User;
import com.hust.p2p.model.vo.ResultObject;
import com.hust.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Long queryAllUserCount() {

        //测试这个错误出在这里
        /*Long allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);

        //若redis缓存中为null，则去mapper层中的数据库中取
        if (allUserCount == null) {
            //从数据库中查询
            allUserCount = userMapper.selectAllUserCount();
            //将该键值对存入到redis缓存中
            redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT, allUserCount, 15, TimeUnit.MINUTES);
        }*/


        //首先从redis缓存中取，如果有则直接用；没有的话再从mysql数据库中取

        //修改redis中的key的名称，改成只有我存的key，不加前面的序列化码
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //这里拿到的是redis模板的已经说了key值的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);


        Long allUserCount = (Long) boundValueOps.get();

        if (allUserCount == null) {

            //去数据库查询
            allUserCount = userMapper.selectAllUserCount();

            //将该值放到redis缓存中
            boundValueOps.set(allUserCount, 15, TimeUnit.MINUTES);
        }
        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObject register(String phone, String loginPassword) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);

        //新增用户
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int insertUserCount = userMapper.insertSelective(user);

        if(insertUserCount > 0){
            //新增账户
            //新增账户时会用到用户的id，所以在新增账户之前会拿到用户对象
            User userInfo = userMapper.selectUserByPhone(phone);
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userInfo.getId());
            financeAccount.setAvailableMoney(888.0);
            int insertFinanceCount  = financeAccountMapper.insertSelective(financeAccount);

            if(insertFinanceCount < 0){
                resultObject.setErrorCode(Constants.FAIL);
            }

        }else{
            resultObject.setErrorCode(Constants.FAIL);
        }

        return resultObject;
    }
}
