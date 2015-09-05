package com.zhilibao.base;

import java.util.Map;

import com.zhilibao.utill.QueryResult;


/**
 * 公共service类
 * @author clz
 * @param <T>
 */
public interface BaseService<T>{
	
	public int save(T t);
	
	public int delete(long id);
	
	public int update(T t);
	
	public T get(long id);
	
	public QueryResult<T> getList(Map<String,Object> params);
	
	

}



