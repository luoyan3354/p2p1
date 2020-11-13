package com.hust.p2p.mapper.user;

import com.hust.p2p.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * mapper接口：获取平台注册总人数
     * @return
     */
    Long selectAllUserCount();

    /**
     * 根据手机号，返回用户的整个信息
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);
}