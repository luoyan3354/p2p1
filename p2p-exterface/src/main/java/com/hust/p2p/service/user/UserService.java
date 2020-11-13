package com.hust.p2p.service.user;

import com.hust.p2p.model.user.User;
import com.hust.p2p.model.vo.ResultObject;

public interface UserService {

    /**
     * UserService接口中：获取平台注册总人数
     * @return
     */
    Long queryAllUserCount();

    /**
     * 根据手机号查询是否有这个用户信息
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObject register(String phone, String loginPassword);
}
