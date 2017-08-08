package com.pux.wwd.quartz.loan;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LoanJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("execute");
		System.out.println(arg0.getJobDetail().getKey().getName());
		System.out.println(arg0.getJobDetail().getJobDataMap().get("objBasTcTaskInfo"));
	}

}
