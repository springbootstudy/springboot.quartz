package com.ctsi.springboot.quartz.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HelloJob implements Job {
	
	private static final Logger log = Logger.getLogger(HelloJob.class);
	
	// 执行次数
	public static final String KEY_NUM_EXECUTIONS = "执行次数";
	
	// 模拟运行时间的长短
	private int time = 10;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.warn("### 耗时 " + time +" 秒 开始" + new Date());
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		
		int executeSum = 0;
		if (map.containsKey(KEY_NUM_EXECUTIONS)) {
			executeSum = map.getInt(KEY_NUM_EXECUTIONS);
		}
		executeSum++;
		log.warn("### 第 " + executeSum + " 次执行");
		
		map.put(KEY_NUM_EXECUTIONS, executeSum);
		
		try {
			Thread.sleep(time * 1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		log.warn("### 耗时 " + time +" 秒 结束" + new Date());
		
	}

}
