package com.ctsi.springboot.quartz.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctsi.springboot.quartz.bean.PrintParam;
import com.ctsi.springboot.quartz.service.PrintSpi;

@PersistJobDataAfterExecution // 用于数据
@DisallowConcurrentExecution
public class HelloJob2 implements Job {
	
	private static final Logger log = Logger.getLogger(HelloJob2.class);
	
	@Autowired
	private PrintSpi printSpi;
	
	// 模拟运行时间的长短
	private int time = 10;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.warn("### 耗时 " + time +" 秒 开始" + new Date());
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		// 对象传不过来，不知道为什么，待解决
//		PrintParam param = (PrintParam) map.get(TaskImpl.KEY_MAP_PARAM);
		Integer appId = (Integer) map.get(TaskImpl.KEY_MAP_ADD_ID);
		Integer projectId = (Integer) map.get(TaskImpl.KEY_MAP_PROJECT_ID);
		
		PrintParam t = new PrintParam();
		t.setAppId(appId);
		t.setProjectId(projectId);
		log.info("== " + t + ", " + t.getAppId() + ", " + t.getProjectId());
		
		PrintParam ret = printSpi.print(t);
		log.info("返回 == " + ret.getProjectId() + ", " + ret.getAppId());
		
//		Optional<PrintParam> opt = Optional.ofNullable(param);
//		opt.ifPresent(new Consumer<PrintParam>() {
//
//			@Override
//			public void accept(PrintParam t) {
//				log.info("-- " + t + ", " + t.getAppId() + ", " + t.getProjectId());
//				printSpi.print(t);
//			}
//			
//		});
		
		try {
			Thread.sleep(time * 1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		log.warn("### 耗时 " + time +" 秒 结束" + new Date());
		
	}

}
