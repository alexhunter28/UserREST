/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nubicall.apiuser.service;

import com.nubicall.apiuser.model.User;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface UserService {
    
    List<User> getUsers();
    List<User> findUser(String userName);
    User saveUser( User user );
    void updateUser( User user );
    void deleteUser( User user );
    int deleteByUserName(String userName);
    
    
}
