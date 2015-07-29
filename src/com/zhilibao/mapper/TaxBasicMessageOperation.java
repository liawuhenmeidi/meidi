package com.zhilibao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

import com.zhilibao.model.tax.TaxBasicMessage;
@Service       
public interface TaxBasicMessageOperation {   
	@Delete("delete from mdtaxbasicmessage where name = #{name}") 
	public int delete(TaxBasicMessage Configs); 
	@Insert("insert into mdtaxbasicmessage (id,name,message) " +
	   		"values (null,#{name},#{message}) ;") 
	public int save(TaxBasicMessage Configs);
       
	@Select("select * from  mdtaxbasicmessage where name = #{name}") 
	public TaxBasicMessage getByName(String name); 
	
	@Select("select * from  mdtaxbasicmessage") 
	public List<TaxBasicMessage> getList(); 
	
}
