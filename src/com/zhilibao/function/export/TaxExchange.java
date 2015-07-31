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
import com.zhilibao.model.tax.Tax;
import com.zhilibao.model.tax.TaxBasicMessage;

import utill.StringUtill;
import utill.TimeUtill;
import wilson.upload.ExcelUpload;
import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;
import wilson.upload.XLSReader;

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

	public static void export(HttpServletResponse response,TaxBasicMessage cf,String orderName,String filename) {
		
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
 
    	//JSONObject object = (JSONObject)message; 
    	JSONObject jsObj = JSONObject.fromObject(cf);
    	   
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
    	
	    	String filePath = ExcelUpload.getTaxFilePath();   
	    List<Tax> li = new XLSReader().readTaxXML(filePath, filename);
    	  
    	  for(int i=0;i<li.size();i++){
    		  Tax uo = li.get(i); 
    		   
    		  Element Xh = new Element("Xh").setText((i+1)+"");  
    		  Element Spmc = new Element("Spmc").setText(uo.getPname());
    		  Element Ggxh = new Element("Ggxh").setText(uo.getPnum());
    		  Element Jldw = new Element("Jldw").setText(uo.getUnit());
    		  Element Dj = new Element("Dj").setText(uo.getPrince()+"");
    		  Element Sl = new Element("Sl").setText(uo.getNum()+"");
    		  Element Je = new Element("Je").setText(uo.getTotalMoney()+""); 
    		  Element Slv = new Element("Slv").setText(uo.getTaxRate()+"");
              
    		  Sph.addContent(Xh);
    		  Sph.addContent(Spmc);
    		  Sph.addContent(Ggxh);
    		  Sph.addContent(Jldw);
    		  Sph.addContent(Dj);
    		  Sph.addContent(Sl); 
    		  Sph.addContent(Je);
    		  Sph.addContent(Slv);
    		  
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
