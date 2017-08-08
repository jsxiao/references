package com.px.core.tx;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author jason
 *
 */
public interface IMybatisService<T> {
	
	
	List<T> selectByExample(Object arg0);
	
	List<T> selectByExampleAndRowBounds(Object arg0, RowBounds arg1);
	
	List<T> selectByRowBounds(T arg0, RowBounds arg1);
	
	int selectCountByExample(Object arg0);

	/**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
	T selectOne(T record) throws DataAccessException;
	
	/**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    List<T> select(T record) throws DataAccessException;
	
    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
	T selectByPrimaryKey(Serializable key) throws DataAccessException;
	
	/**
     * 查询全部结果
     *
     * @return
     */
	List<T> selectAll() throws DataAccessException;
	
	/**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
    int selectCount(T record) throws DataAccessException;
	
    /**
     * 保存一个实体
     *
     * @param record
     * @return
     */
    int insert(T record) throws DataAccessException;
    
    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record) throws DataAccessException;
    
    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record) throws DataAccessException;
    
    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
    int delete(T record) throws DataAccessException;
    
    /**
     * 根据主键字段进行删除
     *
     * @param key
     * @return
     */
    int deleteByPrimaryKey(Object key) throws DataAccessException;
}
