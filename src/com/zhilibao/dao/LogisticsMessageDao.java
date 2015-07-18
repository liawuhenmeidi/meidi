package com.zhilibao.dao;

import java.util.List;

import user.User;

import com.zhilibao.model.LogisticsMessage;

public interface LogisticsMessageDao {
	// 保存数据 
   public int sava(LogisticsMessage lm);
   public int delete(String ids); 
   public int updateLocate(String locate,String id);
   public int updatestatues(User user ,String id );
   public int updateAgree(User user);
   public List<LogisticsMessage> getlist();
   public List<LogisticsMessage> getlist( String statues);
   public List<LogisticsMessage> getAdvancePrince(String statues);
   public List<LogisticsMessage> getlist(int uid,String statues);
   public List<LogisticsMessage> getlist(int uid);
   public LogisticsMessage	getByid(int id);
   public List<LogisticsMessage> getlist(User user ,int statues);
   public int updateAdvancePrince(String ids,String statues); 
   public int updatecharge(String ids,String statues);
   public List<LogisticsMessage>	getlistByIds(String ids);
}
