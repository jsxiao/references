package com.pux.wwd.quartz.account;

import java.util.Calendar;
import java.util.Date;
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
import com.pux.wwd.svc.loan.IAutoRunService;
import com.px.utils.DateUtils;

/**
 * 每日资金流水文件下载-入库
 * @author ghy
 *
 */
public class AccountLogJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(AccountLogJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (context != null) {
			// MyDate startTime = new MyDate();
			JobKey jobKey = context.getJobDetail().getKey();
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			BasTcTaskInfo objBasTcTaskInfo = (BasTcTaskInfo) jobDataMap.get("objBasTcTaskInfo");

			String str = ((int) (Math.random() * 100000)) + " - "
					+ objBasTcTaskInfo.getTaskName() + " ";
			try {
				System.out.println("执行全流水文件下载任务 开始 " + str + new MyDate() + " by " + jobKey);
				logger.info("执行全流水文件下载任务 开始: {} by: {} ", str + new MyDate(), jobKey);
				
				IAutoRunService autoRunService = ManagerMain.getCtx().getBean(IAutoRunService.class);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				System.out.println(DateUtils.dateStr2(calendar.getTime()));
				autoRunService.download(DateUtils.dateStr3(calendar.getTime()));
				
				System.out.println("执行全流水文件下载任务结束" + str + new MyDate() + " by " + jobKey);
				logger.info("执行全流水文件下载任务 结束: {} by: {}", str + new MyDate(), jobKey);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
