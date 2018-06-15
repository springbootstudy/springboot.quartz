package com.ctsi.springboot.quartz.service;

import com.ctsi.springboot.quartz.bean.Task;

public interface TaskSpi {
	
	void addTask(Task task);

}
