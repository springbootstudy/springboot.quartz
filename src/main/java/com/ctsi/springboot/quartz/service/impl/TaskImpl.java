package com.ctsi.springboot.quartz.service.impl;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
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
	public void addTask(Task task) throws SchedulerException {
		log.info("## " + scheduler);
		
		// 1.数据存储到数据库中
		
		// 2.配置 quartz 相关
		
		if ("enable".endsWith(task.getStatus())) {
			// 触发器设置
			String triggerName = task.getName() + "Trigger";
			String triggerGroup = task.getName() + "TriggerGroup";
			log.info("## " + triggerName + " - " + triggerGroup);
			
			CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(task.getCron());
		    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
		    		.withSchedule(csb).build();
		    
		    // job 设置
		    String jobName = task.getName() + "Job";
		    String jobGroup = task.getName() + "JobGroup";
		    log.info("### " + jobName + " - " + jobGroup);
		    
		    JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity(jobName, jobGroup).build();
		    
		    // scheduler 调度设置
		    scheduler.scheduleJob(job, trigger);
		    
//		    log.info("# " + scheduler.isShutdown() + ", " + scheduler.isStarted());
		    
		}
		
	}
	
	public void removeTask(Task task) throws SchedulerException {
		if ("disable".endsWith(task.getStatus())) {
			// 触发器设置
			String triggerName = task.getName() + "Trigger";
			String triggerGroup = task.getName() + "TriggerGroup";
			log.info("## " + triggerName + " - " + triggerGroup);

			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);

			// job 设置
			String jobName = task.getName() + "Job";
			String jobGroup = task.getName() + "JobGroup";
			log.info("### " + jobName + " - " + jobGroup);
			
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			scheduler.deleteJob(jobKey);
		}
	}

}
