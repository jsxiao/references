package com.px.core.tx;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class MybatisBaseService<T> implements IMybatisService<T>{
	
	public abstract MybatisDao<T> getDao();

	@Override
	public List<T> selectByExample(Object arg0){
		return getDao().selectByExample(arg0);
	}
	
	@Override
	public List<T> selectByExampleAndRowBounds(Object arg0, RowBounds arg1){
		return getDao().selectByExampleAndRowBounds(arg0, arg1);
	}
	
	@Override
	public List<T> selectByRowBounds(T arg0, RowBounds arg1){
		return getDao().selectByRowBounds(arg0, arg1);
	}
	
	@Override
	public int selectCountByExample(Object arg0){
		return getDao().selectCountByExample(arg0);
	}
	
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
		return getDao().updateByPrimaryKeySelective(record);
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
