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
             
		} else if ("/".equals(File.separator)) { 
			   
		    String classPath = PathUtill.class.getClassLoader().getResource("/").getPath();
			//logger.info(System.getProperty("catalina.home"));    
			//logger.info(System.getProperty("user.dir"));   
			//logger.info(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))); 
		    logger.info("classPath"+classPath);    
			String tempPath = new File(classPath.substring(1,classPath.indexOf("/WEB-INF/classes"))).getAbsolutePath();
		    //String tempPath = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
			os = tempPath  
					+ File.separator;    
			logger.info("tempPath"+tempPath);    
		} 
		return os;
	}

}