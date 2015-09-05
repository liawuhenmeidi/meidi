package com.zhilibao.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zhilibao.model.TaxBasicMessage;
import com.zhilibao.service.TaxBasicMessageService;
import com.zhilibao.utill.QueryResult;

@Service
public class TaxBasicMessageServiceImpl implements TaxBasicMessageService {

	private static final Log log = LogFactory.getLog(TaxBasicMessageServiceImpl.class);

	@Override
	public int save(TaxBasicMessage t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(TaxBasicMessage t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaxBasicMessage get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryResult<TaxBasicMessage> getList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
