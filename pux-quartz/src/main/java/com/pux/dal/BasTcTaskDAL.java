package com.pux.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.pux.entities.task.BasTcTaskInfo;
import com.pux.quartz.ManagerMain;

@Transactional
public class BasTcTaskDAL {

	private static JdbcTemplate jdbc = ManagerMain.getCtx().getBean(JdbcTemplate.class);
	private static Logger logger = LoggerFactory.getLogger(BasTcTaskDAL.class);
	
	
	/**
	 * 数据库字段列表
	 */
	public static String[] ColumnNames = new String[] { 
			"TASK_ID", // 调度任务ID{sequence}
			"TASK_CODE", // 调度任务标识
			"TASK_NAME", // 调度任务名称
			"TRIGGER_GROUP", // 触发器组名
			"TRIGGER_NAME", // 触发器名
			"JOB_GROUP", // 任务组名
			"JOB_NAME", // 任务名
			"JOB_CLASS_NAME", // 任务类名
			"CRON_EXPRESSION", // cron表达式
			"PRIORITY", // 优先级别
			"STATUS", // 状态：0-禁用、1-启用
			"CONTENT", // 描述
			"PARAMETER", // 参数
			"CREATOR", // 创建人
			"CREATE_TIME", // 创建时间
			"UPDATER", // 更新人
			"UPDATE_TIME" // 更新时间
	};
	
	public static String GetListByReturnSQL(String OrderBy, String[] ColumnNames) {
		StringBuilder sbStr = new StringBuilder(1024);
		sbStr.append("SELECT \n");
		if (ColumnNames == null)
			ColumnNames = BasTcTaskDAL.ColumnNames;
		String LastColumnName = ColumnNames[ColumnNames.length - 1];
		for (String s : ColumnNames) {
			if (s.equals(LastColumnName))
				sbStr.append("		").append(s).append(" \n");
			else
				sbStr.append("		").append(s).append(", \n");
		}
		sbStr.append("FROM T_BAS_TC_TASK \n");
		if (OrderBy.trim().length() > 0) {
			sbStr.append("	ORDER BY\n");
			sbStr.append(OrderBy);
			sbStr.append("\n");
		} else {
			sbStr.append("	ORDER BY\n");
			sbStr.append("		\"TASK_ID\" DESC \n");// 调度任务ID{sequence}
		}
		return sbStr.toString();
	}
	
	public static List<BasTcTaskInfo> GetListBy(String OrderBy){
        String[] columnNames = BasTcTaskDAL.ColumnNames;
        try{
        	List<BasTcTaskInfo> list = jdbc.query(GetListByReturnSQL(OrderBy, columnNames), new BasTcTaskInfoRowMapper());
            return list;
        }catch(Exception e){
        	logger.error("执行SQL语句出现错误: {}", e.getMessage());
        	logger.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }
}
