package com.pux.entities.task;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.pux.utils.MyDate;

public class BasTcTaskInfo implements Serializable {
	private static final long serialVersionUID = -4080772942540009960L;

	private Integer taskId;
	private String taskCode;
	private String taskName;
	
	private String jobName;
	private String jobGroup;
	
	private String jobClassName;
	private String triggerName;
	private String triggerGroup;
	private String cronExpression;
	private Integer priority;
	
	private String content;
	private String parameter;
	
	private String creator;
	private Date createTime;
	private String updater;
	private Date updateTime;
	
	private Integer status; // 任务状态; 0: 停止, 1: 运行

	public BasTcTaskInfo(){}
	
	public BasTcTaskInfo(ResultSet rs) throws SQLException{
		setTaskId(rs.getInt("TASK_ID"));//调度任务ID{sequence}
		setTaskCode(rs.getString("TASK_CODE"));//调度任务标识
		setTaskName(rs.getString("TASK_NAME"));//调度任务名称
		setTriggerGroup(rs.getString("TRIGGER_GROUP"));//触发器组名
		setTriggerName(rs.getString("TRIGGER_NAME"));//触发器名
		setJobGroup(rs.getString("JOB_GROUP"));//任务组名
		setJobName(rs.getString("JOB_NAME"));//任务名
		setJobClassName(rs.getString("JOB_CLASS_NAME"));//任务类名
		setCronExpression(rs.getString("CRON_EXPRESSION"));//cron表达式
		setPriority(rs.getInt("PRIORITY"));//优先级别
		setStatus(rs.getInt("STATUS"));//状态：0-禁用、1-启用
		setContent(rs.getString("CONTENT"));//描述
		setParameter(rs.getString("PARAMETER"));//参数
		setCreator(rs.getString("CREATOR"));//创建人
		setCreateTime(MyDate.setDate(rs.getTimestamp("CREATE_TIME")));//创建时间
		setUpdater(rs.getString("UPDATER"));//更新人
		setUpdateTime(MyDate.setDate(rs.getTimestamp("UPDATE_TIME")));//更新时间
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public Integer getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}