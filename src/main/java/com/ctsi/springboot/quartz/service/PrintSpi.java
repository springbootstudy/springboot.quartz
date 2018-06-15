package com.ctsi.springboot.quartz.service;

import com.ctsi.springboot.quartz.bean.PrintParam;

public interface PrintSpi {
	
	PrintParam print(PrintParam param);

}
