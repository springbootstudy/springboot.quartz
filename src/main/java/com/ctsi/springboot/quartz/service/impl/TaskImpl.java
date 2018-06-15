package com.ctsi.springboot.quartz.service.impl;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctsi.springboot.quartz.bean.Task;
import com.ctsi.springboot.quartz.service.TaskSpi;

@Service
public class TaskImpl implements TaskSpi {
	
	private static final Logger log = Logger.getLogger(TaskImpl.class);
	
	@Autowired
	private Scheduler scheduler;

	@Override
	public void addTask(Task task) {
		log.info("## " + scheduler);
	}

}
