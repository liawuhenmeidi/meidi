package com.zhilibao.mapper;
 
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.zhilibao.model.tax.TaxBasicMessage;
@Repository    
public interface TaxBasicMessageOperation {   
	@Delete("delete from mdtaxbasicmessage where gfmc = #{gfmc}") 
	public int delete(TaxBasicMessage Configs); 
	 
	@Insert("insert into mdtaxbasicmessage (gfmc,gfsh,gfyhzh,gfdzdh,bz,fhr,skr) " +
	   		"values (#{gfmc},#{gfsh},#{gfyhzh},#{gfdzdh},#{bz},#{fhr},#{skr}) ;") 
	public int save(TaxBasicMessage Configs);
        
	@Select("select * from  mdtaxbasicmessage where gfmc = #{gfmc}") 
	public TaxBasicMessage getByName(String name); 
	 
	@Select("select * from  mdtaxbasicmessage") 
	public List<TaxBasicMessage> getList(); 
	
}
