package com.zhilibao.mapper;

import java.util.List;
 
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import user.User;
 
import com.zhilibao.model.LogisticsMessage;
    
public interface LogisticsMessageOperation { 
	    
	//  保存物流配送信息
	   @Insert("insert into mdlogistics (id,uid,bid,carid,submittime,statues,prince,sendtime,locates,remark,advancePrice,startlocate,pid) " +
					   		"values (null,#{uid},#{bid},#{carid},#{submittime},#{statues},#{prince},#{sendtime},#{locates},#{remark},#{advancePrice},#{startLocate},#{pid}) ;")
	   public int sava(LogisticsMessage lm); 
	    
	    
	   
	   @Update("update mdlogistics set statues = -1  where id in #{ids}")
	   public int delete(String ids); 
	     
	   @Update("update mdlogistics set locateMessage = CONCAT(locateMessage,',#{ti}::#{locate}') where id =#{id}")
	   public int updateLocate(@Param("locate")String locate,@Param("ti")String ti,@Param("id")String id);
	     
	   @Update("update mdlogistics set statues = 1  where id =#{id}")
	   public int updatestatues(String id );
	   
	   @Update("update mdlogistics set statues = 3  where statues = 2 and uid = #{id}")
	   public int updateAgree(User user);
	   
	   @Select("select * from  mdlogistics")
	   public List<LogisticsMessage> getlist();
	   
	   // 根据单据状态获取单据
	   @Select("select * from  mdlogistics where statues in (#{statues})")
	   public List<LogisticsMessage> getlistByStatues( String statues);
   
	   @Select("select * from  mdlogistics where  advanceprice != 0 and advancestatues in (#{statues})")
	   public List<LogisticsMessage> getAdvancePrince(String statues);
	   
	  @Select("select * from  mdlogistics where statues in (#{statues}) and uid=#{uid}")
	   
	   public List<LogisticsMessage> getChargelistUid(@Param("uid")int uid,@Param("statues")String statues);
	     
	   @Select("select * from  mdlogistics where  statues in (#{statues})")
	   
	   public List<LogisticsMessage> getChargelist(@Param("statues")String statues);
	      
	   
	   
	   
	   
	   @Select("select * from  mdlogistics where  uid=#{uid}")

	   public List<LogisticsMessage> getlistsUid(int uid);
	    
	   @Select("select * from  mdlogistics where  uid=#{user.id} and statues in (#{statues})" )
	   public List<LogisticsMessage> getlistUidStatues(@Param("user")User user,@Param("statues")String statues);
	   
	   
	    
	   @Select("select * from  mdlogistics where id = #{id}")
	    
	   
	   public LogisticsMessage	getByid(int id);
	   
	  /* @Select("select * from  mdlogistics where uid = #{user.id} and statues = #{statues}")
	   public List<LogisticsMessage> getlist(@Param("user")User user ,@Param("statues")int statues);
	   */
	   @Select("update mdlogistics set advancestatues = #{statues} where id in #{ids}")
	   
	   public int updateAdvancePrince(@Param("ids")String ids,@Param("statues")String statues); 
	   
	   @Select("update mdlogistics set statues = #{statues} where id in #{ids}")
	   public int updatecharge(@Param("ids")String ids,@Param("statues")String statues);
	   
	   @Select("select * from  mdlogistics where id in #{ids}")
	   public List<LogisticsMessage>	getlistByIds(String ids); 
}  
 