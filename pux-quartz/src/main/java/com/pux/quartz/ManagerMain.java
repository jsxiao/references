package com.pux.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.pux.utils.MyDate;

public class ManagerMain {

	private static Logger logger = LoggerFactory.getLogger(ManagerMain.class);
	
	/**
	 * 通知程序要停止Job
	 */
	public final static String NOTIFICATION_STOP = "NotificationStop";
	/**
	 * 是否可以停止Job
	 */
	public final static String CAN_STOP = "CanStop";

	private static ApplicationContext ctx;

	private static Scheduler scheduler = null;
	
	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		ManagerMain.ctx = ctx;
	}
	
	public static Scheduler getScheduler() {
		return scheduler;
	}

	public static void setScheduler(Scheduler scheduler) {
		ManagerMain.scheduler = scheduler;
	}
	
	public static void main(String[] args) {
		try{
			String[] configs = new String[]{"spring/root-context.xml", "spring/redis-context.xml"};
			ctx = new org.springframework.context.support.ClassPathXmlApplicationContext(configs);
			setCtx(ctx);
			
			scheduler = (Scheduler) ctx.getBean("scheduleReportFactory");
			
			logger.info("开始: {}", new MyDate());
			
			String name = "scanJob-1";
			String group = "groupMan-1";
			
			if(!isExitJob(name, group)){
				// 定义作业，并把它绑在我们的ScanJob类
				JobDetail job = JobBuilder.newJob(ScanJob.class)
						.withIdentity(name, group).build();

				// 从当前时间开始启动任务
				Trigger trigger = TriggerBuilder
						.newTrigger()
						// 创建一个Trigger
						.withIdentity(name, group)
						// 设置Trigger的名称和组名
						.withPriority(10)
						// 设置优先级
						.startAt(new MyDate())
						// 从现在开始
						.withSchedule(
								SimpleScheduleBuilder.simpleSchedule()
										.withRepeatCount(-1)
										.withIntervalInSeconds(5)) // 定义一个触发规则，每5秒运行一次
						.build();

				// 按照调度器启动任务
				scheduler.scheduleJob(job, trigger);
				logger.info("启动主调度器: {}", name);
			}
			
			scheduler.start();
			logger.info("结束: {}", new MyDate());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查Job是否存在
	 * 
	 * @param name
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public static boolean isExitJob(String name, String group) {
		JobKey jobKey = getJobKey(name, group);
		return jobKey == null ? false : true;
	}

	/**
	 * 获得 jobKey
	 * 
	 * @param name
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public static JobKey getJobKey(String name, String group) {
		try {
			// 迭代搜索job
			for (String groupSearch : scheduler.getJobGroupNames()) {
				GroupMatcher.anyJobGroup();
				// for (JobKey jobKey :
				// scheduler.getJobKeys(GroupMatcher.groupEquals(group).anyJobGroup()))
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
						.jobGroupEquals(groupSearch))) {
					// System.out.println("Found job identified by: " + jobKey);
					if (group.equals(jobKey.getGroup())
							&& name.equals(jobKey.getName())) {
						return jobKey;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查Job是否存在
	 * 
	 * @param name
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Job> getJobClass(String className) {
		Class<Job> jobClass = null;
		try {
			// 一般尽量采用这种形式
			jobClass = (Class<Job>) Class.forName(className);
			// Job job = demo1.newInstance();
			// job.execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobClass;
	}
}
