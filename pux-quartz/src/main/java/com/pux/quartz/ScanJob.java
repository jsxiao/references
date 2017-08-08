package com.pux.quartz;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pux.dal.BasTcTaskDAL;
import com.pux.entities.task.BasTcTaskInfo;
import com.pux.utils.MyDate;

public class ScanJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(ScanJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		//System.out.println("启动: " + new MyDate() + " 开始 " + context.getJobDetail().getKey());
		logger.info("启动: {},  开始: {}", new MyDate(), context.getJobDetail().getKey());
		
		
		Scheduler scheduler = ManagerMain.getScheduler();
		
		List<BasTcTaskInfo> getListBy = BasTcTaskDAL.GetListBy("");
		for(BasTcTaskInfo taskInfo : getListBy){
			String name = taskInfo.getJobName();
			String group = taskInfo.getJobGroup();
			
			if(ManagerMain.isExitJob(name, group) == false){
				if (taskInfo.getStatus() == 1){
					JobDataMap newJobDataMap = new JobDataMap();
					newJobDataMap.put("objBasTcTaskInfo", taskInfo);
					
					// 启动Job
					JobDetail job = JobBuilder.newJob(ManagerMain.getJobClass(taskInfo.getJobClassName()))// 初始化Job
							.withIdentity(name, group)// 设置组名和名称
							.setJobData(newJobDataMap).build();

					CronTrigger trigger = TriggerBuilder.newTrigger()// 初始化调度器
							.withIdentity(taskInfo.getTriggerName(), taskInfo.getTriggerGroup())// 设置组名和名称
							.withSchedule(CronScheduleBuilder.cronSchedule(taskInfo.getCronExpression()))// 调度表达式
							.withPriority(taskInfo.getPriority())// 设置优先级
							.build();
					
					try
					{
						// 按照调度器启动任务
						System.out.println("启动: " + new Date() + " 开始 " + job.getKey());
						logger.info("启动: {} 开始: {}", new MyDate(), job.getKey());
						scheduler.scheduleJob(job, trigger);
						System.out.println("启动: " + new Date() + " 结束 " + job.getKey());
						logger.info("启动: {} 结束: {}", new MyDate(), job.getKey());
					}
					catch (SchedulerException e)
					{
						e.printStackTrace();
					}
				}
			}else{
				if (taskInfo.getStatus() == 0)
				{
					try
					{
						JobKey jobKey = ManagerMain.getJobKey(name, group);
						if (jobKey != null)
						{
							System.out.println("停止: " + new MyDate() + " 开始 " + jobKey);
							logger.info("停止: {} 开始: {}", new MyDate(), jobKey);
							scheduler.deleteJob(jobKey);
							System.out.println("停止: " + new MyDate() + " 结束 " + jobKey);
							logger.info("停止: {}, 结束: {}", new MyDate(), jobKey);
						}
					}
					catch (SchedulerException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		//System.out.println("启动: " + new MyDate() + " 结束 " + context.getJobDetail().getKey());
		logger.info("启动: {}, 结束: {}", new MyDate(), context.getJobDetail().getKey());
	}
}
