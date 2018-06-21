package com.ctsi.springboot.quartz.service.impl;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ctsi.springboot.quartz.bean.PrintParam;
import com.ctsi.springboot.quartz.service.PrintSpi;

@Service
public class PrintImpl implements PrintSpi {
	
	private static final Logger log = Logger.getLogger(PrintImpl.class);

	@Override
	public PrintParam print(PrintParam param) {
		log.info("## service impl exec ");
		
		Optional<PrintParam> opt = Optional.ofNullable(param);
		opt.ifPresent(new Consumer<PrintParam>() {

			@Override
			public void accept(PrintParam t) {
				log.info("## " + t + ", " + t.getAppId() + ", " + t.getProjectId());
			}
			
		});
		
		return param;
	}

}
