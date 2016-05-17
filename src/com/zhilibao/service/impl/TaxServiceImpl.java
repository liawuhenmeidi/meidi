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
import com.zhilibao.model.Tax;
import com.zhilibao.service.TaxService;
import com.zhilibao.utill.QueryResult;

@Service 
public class TaxServiceImpl implements TaxService {

	private static final Log log = LogFactory.getLog(TaxServiceImpl.class);

	@Override
	public int save(Tax t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Tax t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Tax get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryResult<Tax> getList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
