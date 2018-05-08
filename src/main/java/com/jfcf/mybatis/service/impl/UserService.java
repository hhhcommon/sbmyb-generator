package com.jfcf.mybatis.service.impl;
/**
 *
 * @author wuchp
 * @createDate 2018年5月4日下午2:13:44
 */

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.jfcf.mybatis.mapper.UserMapper;
import com.jfcf.mybatis.model.UserBean;
import com.jfcf.mybatis.tools.DBTools;

public class UserService {

   
	public static void main(String[] args) {
          insertUser();
//        deleteUser();
//        selectUserById();
//        selectAllUser();
    }

    
    /**
     * 新增用户
     */
    private static void insertUser() {
        SqlSession session = DBTools.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        UserBean user = new UserBean(1L, "18070147019", 1, "mybatis");
        try {
            mapper.insertUser(user);
            System.out.println(user.getId());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
        	session.close();
        }
    }
    
    
    /**
     * 删除用户
     */
    private static void deleteUser(){
        SqlSession session=DBTools.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
            mapper.deleteUser(1L);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
        	session.close();
        }
    }
    
    
    /**
     * 根据id查询用户
     */
    private static void selectUserById(){
        SqlSession session=DBTools.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
        UserBean user=    mapper.selectUserById(2L);
        System.out.println(user.toString());
            
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
        	session.close();
        }
    }
    
    /**
     * 查询所有的用户
     */
    private static void selectAllUser(){
        SqlSession session=DBTools.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
        List<UserBean> user=mapper.selectAllUser();
        System.out.println(user.toString());
        session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
        	session.close();
        }
    }
    

}
