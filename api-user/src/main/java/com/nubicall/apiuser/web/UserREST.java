/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nubicall.apiuser.web;



import com.nubicall.apiuser.dto.ResponseDTO;
import com.nubicall.apiuser.model.User;
import com.nubicall.apiuser.service.UserService;
import java.util.List;
import java.util.UUID;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



/**
 *
 * @author ASUS
 */

@RestController
@RequestMapping(path="/users")
public class UserREST {
    
    @Autowired
    private UserService userService;
    
    private enum status{
        Active,
        Blocked
    }
    
    private ResponseDTO responseDTO;
        
    /**
     * METHOD POST
     * @param user
     * @return 
     */
    @RequestMapping(path = "/" ,
                    method = RequestMethod.POST,                     
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)    
    public ResponseEntity<Object> addUser(@RequestBody User user){
                
        responseDTO = new ResponseDTO();
        ResponseEntity<Object> response;
        List<User> findUser = userService.findUser(user.getUserName());
        if(findUser.isEmpty()){            
            userService.saveUser(user);
            responseDTO.setUuid(UUID.randomUUID().toString());
            responseDTO.setMessage("User Created");
            response = new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }else{
            responseDTO.setUuid(UUID.randomUUID().toString());
            responseDTO.setMessage("Username "+user.getUserName()+" already exists");
            response = new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        
        return response;
    }
    
    
    /**
     * METHOD GET
     * @param username
     * @return 
     */
    @RequestMapping(path= "/{username}" , 
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findUser(@PathVariable("username") String username)
    {
            responseDTO = new ResponseDTO();
        
            List<User> user = userService.findUser(username);          
            ResponseEntity<Object> response;
            if(!user.isEmpty()){                
                response = new ResponseEntity<>(user.get(0), HttpStatus.OK );
            }else{                
                responseDTO.setUuid(UUID.randomUUID().toString());
                responseDTO.setMessage("User Not Found");
                response = new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND );
            }
            
            return response;
    }
    /**
     * METHOD PUT
     * @param user
     * @return 
     */
    @RequestMapping(path= "/{username}" , 
                    method = RequestMethod.PUT,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> ModifyUser(@RequestBody User user){
        
        ResponseEntity<Object> response;
        responseDTO = new ResponseDTO();        
        List<User> userDB = userService.findUser(user.getUserName());
                
        if(!userDB.isEmpty()){
            user.setId(userDB.get(0).getId());           
            userService.updateUser(user);
            responseDTO.setUuid(UUID.randomUUID().toString());
            responseDTO.setMessage("User Modified");
            response = new ResponseEntity<>(responseDTO, HttpStatus.OK);
            
        }else if(userDB.isEmpty()){
            responseDTO.setUuid(UUID.randomUUID().toString());
            responseDTO.setMessage("User Not Found");
            response = new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            
        }else{
            responseDTO.setUuid(UUID.randomUUID().toString());
            responseDTO.setMessage("Bad Request");
            response = new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        
        return response;
    }
    
    
    
    /**
     * METHOD DELETE
     * @param userName
     * @return 
     */
    @RequestMapping(path="/{username}",
                    method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)                    
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String userName){
        ResponseEntity<Object> response = null;
        int deleted;
        responseDTO = new ResponseDTO();
        deleted = userService.deleteByUserName(userName);
        
        if(deleted != 0){
            responseDTO.setMessage("User Deleted");
            responseDTO.setUuid(UUID.randomUUID().toString());
            response = new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }else{
            responseDTO.setMessage("User not found");
            responseDTO.setUuid(UUID.randomUUID().toString());
            response = new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        
        return response;
    }

    
    
    
        
}
