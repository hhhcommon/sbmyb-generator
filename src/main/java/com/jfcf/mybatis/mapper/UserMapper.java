package com.jfcf.mybatis.mapper;
/**
 *
 * @author wuchp
 * @createDate 2018年5月4日下午2:03:03
 */
import java.util.List;

import com.jfcf.mybatis.model.UserBean;

public interface UserMapper {
    /**
     * 新增用戶
     * @param user
     * @return
     * @throws Exception
     */
    public int insertUser(UserBean user) throws Exception;
    /**
     * 修改用戶
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public int updateUser (UserBean user,Long id) throws Exception;
     /**
      * 刪除用戶
      * @param id
      * @return
      * @throws Exception
      */
    public int deleteUser(Long id) throws Exception;
    /**
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public UserBean selectUserById(Long id) throws Exception;
     /**
      * 查询所有的用户信息
      * @return
      * @throws Exception
      */
    public List<UserBean> selectAllUser() throws Exception;
}