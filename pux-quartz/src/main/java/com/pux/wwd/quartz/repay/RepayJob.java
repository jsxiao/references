package com.pux.wwd.quartz.repay;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pux.entities.task.BasTcTaskInfo;
import com.pux.utils.MyDate;
import com.pux.wwd.svc.loan.IAutoRunService;

public class RepayJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(RepayJob.class);
	
	@Autowired
	private IAutoRunService autoRunService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if(context != null){
			
			//MyDate startTime = new MyDate();
			JobKey jobKey = context.getJobDetail().getKey();
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			BasTcTaskInfo objBasTcTaskInfo =  (BasTcTaskInfo) jobDataMap.get("objBasTcTaskInfo");
			
			String str = ((int) (Math.random() * 100000)) + " - " + objBasTcTaskInfo.getTaskName() + " ";
			try{
				System.out.println("执行任务 开始 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行任务 开始: {} by: {} ", str + new MyDate(), jobKey);

				// here you code
				autoRunService.upload();
				autoRunService.download();
				
				
				System.out.println("执行任务 结束 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行任务 结束 : {} by: {}", str + new MyDate(), jobKey);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		}
	}

}
