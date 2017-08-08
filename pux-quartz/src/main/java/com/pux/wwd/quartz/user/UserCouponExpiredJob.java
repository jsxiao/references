package com.pux.wwd.quartz.user;

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
import com.pux.wwd.svc.loan.IAutomaticPaymentService;
import com.pux.wwd.svc.user.IUserCouponService;

/**
 * 用户加息卷过期任务
 * @author xiaojs
 * 2017年2月24日 上午9:09:12
 */
public class UserCouponExpiredJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(UserCouponExpiredJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if(context != null){
			//MyDate startTime = new MyDate();
			JobKey jobKey = context.getJobDetail().getKey();
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			BasTcTaskInfo objBasTcTaskInfo =  (BasTcTaskInfo) jobDataMap.get("objBasTcTaskInfo");
			
			String str = ((int) (Math.random() * 100000)) + " - " + objBasTcTaskInfo.getTaskName() + " ";
			try {
				System.out.println("执行任务 开始 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行任务 开始: {} by: {} ", str + new MyDate(), jobKey);
				
				// 将已过期的加息卷状态修改
				ManagerMain.getCtx().getBean(IUserCouponService.class).updateExpired();
				
				System.out.println("执行任务 结束 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行任务 结束 : {} by: {}", str + new MyDate(), jobKey);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
