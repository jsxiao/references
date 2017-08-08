package com.pux.wwd.quartz.index;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pux.entities.task.BasTcTaskInfo;
import com.pux.quartz.ManagerMain;
import com.pux.utils.MyDate;
import com.pux.wwd.svc.loan.IInvestListService;

/**
 * 首页投资列表
 * @author xiaojs
 * 2016年10月14日 下午1:58:10
 */
public class IndexInvestListJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(IndexInvestListJob.class);
	
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
				
				ManagerMain.getCtx().getBean(IInvestListService.class).findWithCache();
				
				System.out.println("执行任务 结束 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行任务 结束 : {} by: {}", str + new MyDate(), jobKey);
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		}		
	}

}
