package com.zhilibao.service.impl;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.zhilibao.dao.LogisticsMessageDao;
import com.zhilibao.model.LogisticsMessage;
import com.zhilibao.service.LogisticsMessageService;
import com.zhilibao.utill.QueryResult;

@Service
public class LogisticsMessageServiceImpl implements LogisticsMessageService {
 
	private static final Log log = LogFactory.getLog(LogisticsMessageServiceImpl.class);
    
	private LogisticsMessageDao logisticsMessageDao;
	
	
	
	@Override
	public int save(LogisticsMessage t) {
		// TODO Auto-generated method stub
		return logisticsMessageDao.save(t); 
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(LogisticsMessage t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LogisticsMessage get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryResult<LogisticsMessage> getList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
 
	

}
