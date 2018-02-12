/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nubicall.apiuser;


import com.nubicall.apiuser.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;


import static org.junit.Assert.*;
import org.springframework.core.annotation.Order;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author ASUS
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiUserApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiTest {
    
    private String  pathController = "/users/";
        
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));   
    
    
    private MockMvc mockMvc;
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
        
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();        
    }

        
    @Test    
    public void test1_createUser() throws Exception{
                        
        String user = this.json(new User("miguel@hotmail.com","Miguel","Sanchez","C0NtR4S3n4","3124562234","nacho","Active"));
        
        this.mockMvc.perform(post(pathController)
                .contentType(contentType)
                .content(user))
                .andExpect(status().isCreated());
        
    }
    
    @Test    
    public void test2_failCreatedUser() throws Exception{
        String user = this.json(new User("miguel@hotmail.com","Miguel","Sanchez","C0NtR4S3n4","3124562234","nacho","Active"));
        
        this.mockMvc.perform(post(pathController)
                .contentType(contentType)
                .content(user))
                .andExpect(status().isBadRequest());
    }
    
    @Test    
    public void test3_userFound() throws Exception {
        mockMvc.perform(get(pathController + "{username}", "nacho"))
               .andExpect(status().isOk());
    }
    
    @Test    
    public void test4_userNotFound() throws Exception {
        mockMvc.perform(get(pathController+"{username}", "elInnombrable"))
               .andExpect(status().isNotFound());
    }
        
    
    @Test    
    public void test5_updateUser() throws Exception{
        String user = this.json(new User("nacho@hotmail.com","Ignacio","Perez","C0NtR4S3n4","324453617","nacho","Active"));
        
        this.mockMvc.perform(put(pathController+"{username}", "nacho")
                .contentType(contentType)
                .content(user))
                .andExpect(status().isOk());
    }
    
    
    @Test    
    public void test6_updateUserNotFound() throws Exception{
        String user = this.json(new User("nacho@hotmail.com","Ignacio","Perez","C0NtR4S3n4","324453617","ElInnombrable","Active"));
        
        this.mockMvc.perform(put(pathController+"{username}", "ElInnombrable")
                .contentType(contentType)
                .content(user))
                .andExpect(status().isNotFound());
        
    }
    
    @Test    
    public void test7_deleteUser()throws Exception{
        mockMvc.perform(delete(pathController + "{username}", "nacho"))
               .andExpect(status().isOk());
    }
    
    @Test    
    public void test8_deleteUserNotFound()throws Exception{
        mockMvc.perform(delete(pathController + "{username}", "Elinmombrable"))
               .andExpect(status().isNotFound());
    }
          
    
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
