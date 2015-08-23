package com.zhilibao.mapper;


  
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import com.zhilibao.config.Configs;
@Repository      
public interface ConfigsOperation {  
	@Delete("delete from mdconfigs where name = #{name}") 
	public int delete(Configs Configs); 
	@Insert("insert into mdconfigs (id,name,message) " +
	   		"values (null,#{name},#{message}) ;") 
	public int save(Configs Configs);
      
	@Select("select * from  mdconfigs where name = #{name}") 
	public Configs getByName(String name); 
}
