package com.zhilibao.function.export;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
 
import com.zhilibao.config.Configs;

import utill.StringUtill;
import utill.TimeUtill;
import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;

/** 
 * 核心请求处理类 
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class TaxExchange {
	
	protected static Log logger = LogFactory.getLog(TaxExchange.class);
 
	/** 
	 * 确认请求来自微信服务器 
	 */  

	public static void export(HttpServletResponse response,Configs cf,String orderName) {
		
		//Element root = new Element("Kp").setAttribute("count", "4");    
		
		Element root = new Element("Kp"); 
		
		Document Doc = new Document(root); 
		
		Element Fpxx = new Element("Fpxx"); 

		root.addContent(Fpxx);
		 
		Element Zsl = new Element("Zsl").setText("1");
		
		Fpxx.addContent(Zsl);  
		
		Element Fpsj = new Element("Fpsj"); 
		
		Fpxx.addContent(Fpsj);
		
        Element Fp = new Element("Fp"); 
		
        Fpsj.addContent(Fp);

        String message = cf.getMessage();    
    	//JSONObject object = (JSONObject)message; 
    	JSONObject jsObj = JSONObject.fromObject(message);
    	  
    	Iterator<String> it = jsObj.keys();  
    	while(it.hasNext()){
        	String pro = it.next();
            String value = jsObj.getString(pro);
            if(StringUtill.isNull(value)){
            	value = " "; 
            }
        	//logger.info(pro); 
        		Element e = new Element(pro).setText(value);  
            	Fp.addContent(e); 
        } 
        
		
    	
    	  Element Spxx = new Element("Spxx"); 
  		
    	  Fp.addContent(Spxx);
    	
    	  Element Sph = new Element("Sph"); 
    		
    	  Spxx.addContent(Sph);
    	
		  
    	  List<UploadOrder> list = UploadManager.getTotalUploadOrders(orderName); 
    	  
    	  for(int i=0;i<list.size();i++){
    		  UploadOrder uo = list.get(i); 
    		  
    		  Element Xh = new Element("Xh").setText((i+1)+"");  
    		  Element Spmc = new Element("Spmc").setText(uo.getName());
    		  Element Ggxh = new Element("Ggxh").setText(uo.getType());
    		  Element Jldw = new Element("Jldw").setText("");
    		  Element Dj = new Element("Dj").setText((i+1)+"");
    		  Element Sl = new Element("Sl").setText((i+1)+"");
    		  Element Je = new Element("Je").setText((i+1)+""); 
    		  Element Slv = new Element("Slv").setText((i+1)+"");
    		  Element Se = new Element("Se").setText((i+1)+"");
              
    		  Sph.addContent(Xh);
    		  Sph.addContent(Spmc);
    		  Sph.addContent(Ggxh);
    		  Sph.addContent(Jldw);
    		  Sph.addContent(Dj);
    		  Sph.addContent(Sl); 
    		  Sph.addContent(Je);
    		  Sph.addContent(Slv);
    		  Sph.addContent(Se); 
    		  
    	  }
    	   
    	   
    	  
		 Format format = Format.getPrettyFormat();
		 
	     XMLOutputter XMLOut = new XMLOutputter(format); 
	        
	        
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ TimeUtill.getdateString() + ".xml\"");
			// FileOutputStream fout = new  
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			  XMLOut.output(Doc, response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
		logger.info(e); 
		}

	}

	
	
	

}
