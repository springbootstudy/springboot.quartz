package com.ctsi.springboot.quartz.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 
 * @author lb
 *
 * @since 2018年6月15日
 *
 * @Copyright 软件共享研发中心
 *
 */
@Configuration
public class QuartzConfig2 {
	
	@Autowired
	private SpringJobFactory springJobFactory;
	
    private Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
	
	@Bean 
	public SchedulerFactoryBean schedulerFactoryBean()
			throws IOException {
		
        SchedulerFactoryBean factory = new SchedulerFactoryBean(); 
        // this allows to update triggers in DB when updating settings in config file: 
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了 
        factory.setOverwriteExistingJobs(true); 
        //用于quartz集群,加载quartz数据源 
        //factory.setDataSource(dataSource);   
        //QuartzScheduler 延时启动，应用启动完5秒后 QuartzScheduler 再启动 
        factory.setStartupDelay(5);
        //用于quartz集群,加载quartz数据源配置 
        factory.setQuartzProperties(quartzProperties());
        factory.setAutoStartup(true);
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        
        factory.setJobFactory(springJobFactory);
        
        //注册触发器 
//        factory.setTriggers(cronJobTrigger); 
        
        //直接使用配置文件
//        factory.setConfigLocation(new FileSystemResource(this.getClass().getResource("/quartz.properties").getPath()));
        return factory; 
    }
	
	@Bean(name = "scheduler")
	public Scheduler getScheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}
	
	/**
	 * 加载触发器
	 * 
	 * @param jobDetail
	 * @return
	 */
//	@Bean(name = "dialogJobTrigger")
	public CronTriggerFactoryBean dialogStatusJobTrigger(
			@Qualifier("updateDialogStatusJobDetail") JobDetail jobDetail) {
		return dialogStatusTrigger(jobDetail, "0 0 0/1 * * ?");
	}
	
	/**
     * 创建触发器工厂
     * @param jobDetail
     * @param cronExpression
     * @return
     */
    private static CronTriggerFactoryBean dialogStatusTrigger(JobDetail jobDetail, String cronExpression) { 
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean(); 
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression (cronExpression);
        return factoryBean; 
    }
    
    /**
     * 加载job
     * @return
     */
    @Bean 
    public JobDetailFactoryBean updateDialogStatusJobDetail() { 
        return createJobDetail(InvokingJobDetailDetailFactory.class, "updateDialogStatusGroup", "dialogJob"); 
    }  
    
    /**
     * 创建job工厂
     * @param jobClass
     * @param groupName
     * @param targetObject
     * @return
     */
    private static JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass, String groupName, String targetObject) { 
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean(); 
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true); 
        factoryBean.setRequestsRecovery(true);
        factoryBean.setGroup(groupName);
        Map<String, String> map = new HashMap<>();
        map.put("targetObject", targetObject);
        map.put("targetMethod", "execute");
        factoryBean.setJobDataAsMap(map);
        return factoryBean; 
    }

}
