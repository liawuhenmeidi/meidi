import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.zhilibao.mapper.LogisticsMessageOperation;
import com.zhilibao.model.LogisticsMessage;

import utill.StringUtill; 

 
public class Test {
	protected static Log logger = LogFactory.getLog(Test.class);
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader; 

    static{   
        try{  
            reader    = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().addMapper(LogisticsMessageOperation.class); 
        }catch(Exception e){
            e.printStackTrace();
        }  
    } 

    public static SqlSessionFactory getSession(){
        return sqlSessionFactory;
    }
    
   /* public static void main(String[] args) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
        LogisticsMessage lm = (LogisticsMessage) session.selectOne("com.yihaomen.mybatis.models.UserMapper.selectUserByID",23);
        logger.info(StringUtill.GetJson(lm));       
        } finally { 
        session.close(); 
        }  
    } */ 
       
    public static void main(String[] args) { 
    	  
        SqlSession session = sqlSessionFactory.openSession();
        try {       
        	   
        	LogisticsMessageOperation userOperation=session.getMapper(LogisticsMessageOperation.class);
        	
 
        	
        	//LogisticsMessage lm = userOperation.selectUserByID(23);
            
        } finally {
            session.close();
        }
    }
}