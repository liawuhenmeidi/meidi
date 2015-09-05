package com.zhilibao.base;



import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;



public abstract class LivesBaseDao<T extends Serializable> extends BaseDao<T> {

	@Resource(name="sqlSessionFactory")
	public void setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionTemplate(new SqlSessionTemplate(sqlSessionFactory));
	}

	
}
