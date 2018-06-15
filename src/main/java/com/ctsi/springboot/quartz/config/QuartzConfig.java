package com.ctsi.springboot.quartz.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 
 * @author lb
 *
 * @since 2018年6月15日
 *
 * @Copyright 软件共享研发中心
 *
 */
//@Component
@Configuration
public class QuartzConfig {
	
	@Value("${org.quartz.scheduler.instanceName}")
	private String instanceName;
	
	@Value("${org.quartz.scheduler.instanceId}")
	private String instanceId;
	
	@Value("${org.quartz.threadPool.class}")
	private String tpClass;
	
	@Value("${org.quartz.threadPool.threadCount}")
	private String tpThreadCount;
	
	@Value("${org.quartz.threadPool.threadPriority}")
	private String tpThreadPriority;
	
	@Value("${org.quartz.jobStore.misfireThreshold}")
	private String jsMisfireThreshold;
	
	@Value("${org.quartz.jobStore.class}")
	private String jsClass;
	
	@Value("${org.quartz.jobStore.driverDelegateClass}")
	private String jsDriverDelegateClass;
	
	@Value("${org.quartz.jobStore.useProperties}")
	private String jsUseProperties;
	
	@Value("${org.quartz.jobStore.dataSource}")
	private String jsDataSource;
	
	@Value("${org.quartz.jobStore.tablePrefix}")
	private String jsTablePrefix;
	
	@Value("${org.quartz.jobStore.isClustered}")
	private String jsIsClustered;
	
	@Value("${org.quartz.jobStore.clusterCheckinInterval}")
	private String jsClusterCheckinInterval;
	
	@Value("${org.quartz.dataSource.myDS.driver}")
	private String dbDriver;
	
	@Value("${org.quartz.dataSource.myDS.URL}")
	private String dbUrl;
	
	@Value("${org.quartz.dataSource.myDS.user}")
	private String dbUser;
	
	@Value("${org.quartz.dataSource.myDS.password}")
	private String dbPassword;
	
	@Value("${org.quartz.dataSource.myDS.maxConnections}")
	private String dbMaxConnections;
	
	@Value("${org.quartz.dataSource.myDS.validationQuery}")
	private String dbValidationQuery;
	
	private Properties quartzProperties() throws IOException {
        Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", instanceName);
        prop.put("org.quartz.scheduler.instanceId", instanceId);
         
        prop.put("org.quartz.jobStore.class", jsClass);
        prop.put("org.quartz.jobStore.driverDelegateClass", jsDriverDelegateClass);
        prop.put("org.quartz.jobStore.useProperties", jsUseProperties);
        prop.put("org.quartz.jobStore.dataSource", jsDataSource);
        prop.put("org.quartz.jobStore.tablePrefix", jsTablePrefix);
        prop.put("org.quartz.jobStore.isClustered", jsIsClustered);
         
        prop.put("org.quartz.jobStore.clusterCheckinInterval", jsClusterCheckinInterval);
        prop.put("org.quartz.jobStore.misfireThreshold", jsMisfireThreshold);
         
        prop.put("org.quartz.threadPool.class", tpClass);
        prop.put("org.quartz.threadPool.threadCount", tpThreadCount);
        prop.put("org.quartz.threadPool.threadPriority", tpThreadPriority);
         
        prop.put("org.quartz.dataSource.myDS.driver", dbDriver);
        prop.put("org.quartz.dataSource.myDS.URL", dbUrl);
        prop.put("org.quartz.dataSource.myDS.user", dbUser);
        prop.put("org.quartz.dataSource.myDS.password", dbPassword);
        prop.put("org.quartz.dataSource.myDS.maxConnections", dbMaxConnections);
        prop.put("org.quartz.dataSource.myDS.validationQuery", dbValidationQuery);
         
        return prop;
    }
	
	@Bean 
	public SchedulerFactoryBean schedulerFactoryBean(
			@Qualifier("dialogJobTrigger") Trigger cronJobTrigger)
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
        
        //注册触发器 
        factory.setTriggers(cronJobTrigger); 
        
        //直接使用配置文件
//        factory.setConfigLocation(new FileSystemResource(this.getClass().getResource("/quartz.properties").getPath()));
        return factory; 
    }
	
	/**
	 * 加载触发器
	 * 
	 * @param jobDetail
	 * @return
	 */
	@Bean(name = "dialogJobTrigger")
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

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTpClass() {
		return tpClass;
	}

	public void setTpClass(String tpClass) {
		this.tpClass = tpClass;
	}

	public String getTpThreadCount() {
		return tpThreadCount;
	}

	public void setTpThreadCount(String tpThreadCount) {
		this.tpThreadCount = tpThreadCount;
	}

	public String getTpThreadPriority() {
		return tpThreadPriority;
	}

	public void setTpThreadPriority(String tpThreadPriority) {
		this.tpThreadPriority = tpThreadPriority;
	}

	public String getJsMisfireThreshold() {
		return jsMisfireThreshold;
	}

	public void setJsMisfireThreshold(String jsMisfireThreshold) {
		this.jsMisfireThreshold = jsMisfireThreshold;
	}

	public String getJsClass() {
		return jsClass;
	}

	public void setJsClass(String jsClass) {
		this.jsClass = jsClass;
	}

	public String getJsDriverDelegateClass() {
		return jsDriverDelegateClass;
	}

	public void setJsDriverDelegateClass(String jsDriverDelegateClass) {
		this.jsDriverDelegateClass = jsDriverDelegateClass;
	}

	public String getJsUseProperties() {
		return jsUseProperties;
	}

	public void setJsUseProperties(String jsUseProperties) {
		this.jsUseProperties = jsUseProperties;
	}

	public String getJsDataSource() {
		return jsDataSource;
	}

	public void setJsDataSource(String jsDataSource) {
		this.jsDataSource = jsDataSource;
	}

	public String getJsTablePrefix() {
		return jsTablePrefix;
	}

	public void setJsTablePrefix(String jsTablePrefix) {
		this.jsTablePrefix = jsTablePrefix;
	}

	public String getJsIsClustered() {
		return jsIsClustered;
	}

	public void setJsIsClustered(String jsIsClustered) {
		this.jsIsClustered = jsIsClustered;
	}

	public String getJsClusterCheckinInterval() {
		return jsClusterCheckinInterval;
	}

	public void setJsClusterCheckinInterval(String jsClusterCheckinInterval) {
		this.jsClusterCheckinInterval = jsClusterCheckinInterval;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbMaxConnections() {
		return dbMaxConnections;
	}

	public void setDbMaxConnections(String dbMaxConnections) {
		this.dbMaxConnections = dbMaxConnections;
	}

	public String getDbValidationQuery() {
		return dbValidationQuery;
	}

	public void setDbValidationQuery(String dbValidationQuery) {
		this.dbValidationQuery = dbValidationQuery;
	}  

}
