package com.pux.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pux.entities.task.BasTcTaskInfo;

public class BasTcTaskInfoRowMapper implements RowMapper<BasTcTaskInfo>{

	@Override
	public BasTcTaskInfo mapRow(ResultSet rs, int arg1) throws SQLException {
		return new BasTcTaskInfo(rs);
	}
}
