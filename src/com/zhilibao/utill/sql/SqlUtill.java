package com.zhilibao.utill.sql;

public interface SqlUtill {
	String getCreateSQL(Object bean);
	String getInsertSQL(Object bean);
	String getUpdateSQL(Object bean); 
     
}
