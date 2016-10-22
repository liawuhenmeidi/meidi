package timer;

import httpClient.MyMainClient;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
    
public class MyTimerTask extends TimerTask{
	protected static Log logger = LogFactory.getLog(MyTimerTask .class); 
        public void run() { 
        	logger.info("MyTimerTask");    
        	//MyMainClient.getinstance().run();
        } 
}
