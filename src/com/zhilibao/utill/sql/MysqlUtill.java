package com.zhilibao.utill.sql;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhilibao.model.tax.TaxBasicMessage;
import com.zhilibao.utill.bean.BeanUtill;
 
public class MysqlUtill implements SqlUtill{
	protected static Log logger = LogFactory.getLog(SqlUtill.class);   
    public static void main(String args[]){
    	MysqlUtill my = new MysqlUtill();
    	Object bean = new TaxBasicMessage();
    	//logger.info(my.getCreateSQL(bean));
    	logger.info(my.getInsertSQL(bean));
    }
 
	@Override
	public  String getCreateSQL(Object bean) { 
		List<String> beanPropertyList =  BeanUtill.getBeanPropertyList(bean);

        StringBuffer sb = new StringBuffer("create table wnk_pdt_"+BeanUtill.getBeanName(bean)+"(\n");  
        for (String string : beanPropertyList) {   
            String[] propertys = string.split("`");  
            if (!propertys[1].equals("tableName")&&!propertys[1].equals("param")&&!propertys[0].equals("List")) {  
                if (propertys[1].equals("id")) {  
                    sb.append("   id bigint primary key auto_increment,\n");  
                } else {  
                    if (propertys[0].equals("int")) {  
                        sb.append("   " + propertys[1] + " int default 0 comment '',\n");  
                    } else if (propertys[0].equals("String")) {  
                        sb.append("   " + propertys[1] + " varchar(2000) default '' comment '',\n");  
                    } else if (propertys[0].equals("double")) {  
                        sb.append("   " + propertys[1] + " double(10,2) default 0.0 comment '',\n");  
                    } else if (propertys[0].equals("Date")) {  
                        sb.append("   " + propertys[1] + " datetime comment '',\n");  
                    }  
                }  
            }  
        }  
        sb.append(")");  
        sb.deleteCharAt(sb.lastIndexOf(","));  
        return sb.toString();  
	}
 
	@Override 
	public String getInsertSQL(Object bean) {
		String filesList =  BeanUtill.getBeanFilesList(bean);  
        int fl = filesList.split(",").length+1;  
        String wenhao = "";   
        for (int i = 0; i < fl; i++) {  
            if(i==fl-1){  
                wenhao = wenhao+"?";  
            }else{   
                wenhao = wenhao+"?,";   
            }  
        }  
        return "insert into wnk_pdt_"+BeanUtill.getBeanName(bean)+"("+filesList+") values("+wenhao+")";  
    }  


	@Override
	public String getUpdateSQL(Object bean) {
		// TODO Auto-generated method stub
		return null;
	}

}
