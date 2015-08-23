package com.zhilibao.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.zhilibao.model.Cars;



public interface CarsOperation { 
	@Insert("insert into mdcars (id,uid,num) " +
	   		"values (null,#{uid},#{num}) ;")
	public int save(Cars cars);
	 
	@Select("select * from  mdcars") 
	public List<Cars> getlist(); 
}
