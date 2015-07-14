package mybatis.inter;

import java.util.List;

import logistics.LogisticsMessage;
import org.apache.ibatis.annotations.Select;
  
public interface LogisticsMessageOperation {  
	 @Select("select * from mdlogistics where id = #{id}") 
	 public LogisticsMessage selectUserByID(int id);
	  
	 @Select("select * from mdlogistics") 
	 public List<LogisticsMessage> selectUsers(String userName);
}  
 