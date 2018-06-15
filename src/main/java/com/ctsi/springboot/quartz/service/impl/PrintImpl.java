package com.ctsi.springboot.quartz.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ctsi.springboot.quartz.bean.PrintParam;
import com.ctsi.springboot.quartz.service.PrintSpi;

@Service
public class PrintImpl implements PrintSpi {
	
	private static final Logger log = Logger.getLogger(PrintImpl.class);

	@Override
	public PrintParam print(PrintParam param) {
		log.info("## servie impl exec" );
		
		return null;
	}

}
