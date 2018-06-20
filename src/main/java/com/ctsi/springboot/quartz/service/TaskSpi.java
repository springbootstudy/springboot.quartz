package com.ctsi.springboot.quartz.service;

import org.quartz.SchedulerException;

import com.ctsi.springboot.quartz.bean.Task;

public interface TaskSpi {
	
	void addTask(Task task) throws SchedulerException;
	
	void removeTask(Task task) throws SchedulerException;

}
