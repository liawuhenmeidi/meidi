package com.zhilibao.dao.impl;

import org.springframework.stereotype.Repository;
import com.zhilibao.base.LivesBaseDao;
import com.zhilibao.dao.LogisticsMessageDao;
import com.zhilibao.model.Cars;
import com.zhilibao.model.LogisticsMessage;
     
@Repository
public class LogisticsMessageDaoImpl extends LivesBaseDao<LogisticsMessage> implements LogisticsMessageDao {
 
	@Override 
	public int save(LogisticsMessage logisticsMessage) {
		// TODO Auto-generated method stub
		logger.info("insertLogisticsMessage"); 
		return this.save("insertLogisticsMessage", logisticsMessage);
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Cars cars) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cars getLogisticsMessage(long id) {
		// TODO Auto-generated method stub
		return null;
	}
  
	

	 
}
