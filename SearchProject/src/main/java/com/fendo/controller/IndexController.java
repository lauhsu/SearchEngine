package com.fendo.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.es.elasticsearch.ElasticsearchUtil;
import com.fendo.dto.ModelDto;
import com.fendo.entity.User;
import com.fendo.service.UserService;

@Controller
public class IndexController {

	@Resource
	private UserService userService;
	
	 @RequestMapping("/addUser")  
	    public ModelAndView addPerson(User user){  
	        System.out.println("页面数据："+user.toString());  
	          
	        //加入数据  
	        userService.save(user);  
	          
	        //查数据  
	        User users=userService.get(1);
	        System.out.println(users.toString()); 
	        
	        List<User> ls=userService.getAll();
	        
	        //存起来  
	        ModelAndView modelAndView=new ModelAndView();  
	        modelAndView.setViewName("success");  
	        modelAndView.addObject("user", ls);
	        return modelAndView;  
	    }
	 
	 
	 @RequestMapping("/searchIndex")  
	 public String indexPage(HttpServletRequest req, HttpServletResponse resp){

		 return "index";
	 }
	 
	 @RequestMapping("/searchData")  
	 public String searchData(HttpServletRequest req, HttpServletResponse resp){
		 String keyWord = req.getParameter("keyWord");
		 String pageNo = req.getParameter("pageNo");
		 int pNo = 0;
		 if(pageNo != null ) {
			 pNo = Integer.parseInt(pageNo);
		 }
		 List<ModelDto> list = null;
		try {
			String field = "title";
			list = ElasticsearchUtil.queryData(field, keyWord,pNo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("list", list);
		 return "result";
	 }
}
