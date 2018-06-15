package com.ctsi.springboot.quartz.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctsi.springboot.quartz.bean.PrintParam;
import com.ctsi.springboot.quartz.service.PrintSpi;

@RestController
public class IndexController {
	
	private static final Logger log = Logger.getLogger(IndexController.class);
	
	@Autowired
	private PrintSpi printSpi;
	
	@RequestMapping("/index")
	public String index() {
		log.info("## Index");
		
		PrintParam param = new PrintParam();
		printSpi.print(param);
		
		return "OK";
	}
	
}

