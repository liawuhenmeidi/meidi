import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhilibao.mapper.LogisticsMessageOperation;

public class MybatisSprintTest { 
	protected static Log logger = LogFactory.getLog(MybatisSprintTest.class);
    private static ApplicationContext ctx;  
      
    static      
    {    
        ctx = new ClassPathXmlApplicationContext("config/applicationContext.xml"); 
    }         
         
    public static void main(String[] args)    
    {  
    	 logger.info(ctx); 
    	LogisticsMessageOperation mapper = (LogisticsMessageOperation)ctx.getBean("LogisticsMessage"); 
        //测试id=1的用户查询，根据数据库中的情况，可以改成你自己的.
        System.out.println("得到用户id=1的用户信息");
        //List<LogisticsMessage> users = mapper.selectUsers("");
       // logger.info(users);
    }  

    
}
