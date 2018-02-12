/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nubicall.apiuser.data;

import com.nubicall.apiuser.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


/**
 *
 * @author ASUS
 */
public interface UserRepository extends CrudRepository<User, Long> {
          
    List<User> findByUserName(String userName);
        
    @Modifying
    @Query("delete from User u where u.userName = ?1")
    int deleteByUserName(String userName);
}
