package com.fendo.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fendo.entity.User;
import com.fendo.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
//加载spring配置文件
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SpringJunit4 {
	
@Resource
private UserService userService;

@Test
public void testUser() throws Exception {
   User user = new User();
   user.setName("9632147");
   userService.save(user);
   
   user.setName("9999");
   userService.save(user);
}
}