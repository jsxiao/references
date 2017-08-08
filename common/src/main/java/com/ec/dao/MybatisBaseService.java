package com.ec.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.ec.plugin.MybatisDao;

@Transactional
public abstract class MybatisBaseService<T> implements IMybatisService<T>{
	
	public abstract MybatisDao<T> getDao();
	
	@Override
	public List<T> select(T record) throws DataAccessException {
		return getDao().select(record);
	}
	
	@Override
	public T selectOne(T record) throws DataAccessException {
		return getDao().selectOne(record);
	}
	
	@Override
	public List<T> selectAll() throws DataAccessException {
		return getDao().selectAll();
	}
	
	@Override
	public T selectByPrimaryKey(Serializable key) throws DataAccessException {
		return getDao().selectByPrimaryKey(key);
	}
	
	@Override
	public int selectCount(T record) throws DataAccessException {
		return getDao().selectCount(record);
	}
	
	@Override
	public int insert(T record) throws DataAccessException {
		return getDao().insertSelective(record);
	}
	
	@Override
	public int updateByPrimaryKey(T record) throws DataAccessException {
		return getDao().updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(T record) throws DataAccessException {
		return updateByPrimaryKeySelective(record);
	}
	
	@Override
	public int delete(T record) throws DataAccessException {
		return getDao().delete(record);
	}
	
	@Override
	public int deleteByPrimaryKey(Object key) throws DataAccessException {
		return getDao().deleteByPrimaryKey(key);
	}
}
