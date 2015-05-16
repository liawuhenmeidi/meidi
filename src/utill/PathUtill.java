package utill;

import httpClient.download.SaleDownLoad;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class PathUtill {
	protected static Log logger = LogFactory.getLog(PathUtill.class);
	public static String getXMLpath() {
		String os = "";
	   
	//	logger.info(File.separator);  
		  
		if ("//".equals(File.separator) || "\\".equals(File.separator)) {
			String tempPath = new File("").getAbsolutePath();
			os = tempPath.replace("bin", "webapps") + File.separator + "meidi"
					+ File.separator;   
			/*os = tempPath.replace("bin", "webapps")
					+ File.separator; */    
                        
		} else if ("/".equals(File.separator)) {  
			String classPath = PathUtill.class.getClassLoader().getResource("/").getPath();
			//logger.info(classPath);       
			//logger.info(System.getProperty("catalina.home"));    // 不行
			//logger.info(System.getProperty("user.dir"));   // 不行
			//logger.info("classPath"+classPath);    
			//logger.info(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))); 
		    //logger.info(new File(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))).getAbsolutePath());
			//logger.info(new File(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))).getPath());
		    String tempPath = new File(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))).getPath();  
		   // String tempPath = classPath.substring(1,classPath.indexOf("/WEB-INF/classes")); 
			os = File.separator+tempPath                       
					+ File.separator;           
			logger.info("os:"+os);     
		} 
		return os;
	}

}