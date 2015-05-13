package jfree;


import httpClient.download.Inventory;
import httpClient.download.InventoryChange;
import httpClient.download.SaleDownLoad;

import java.awt.Color;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.TextAnchor;

import utill.StringUtill;
  
public class JfreeBar {
	protected static Log logger = LogFactory.getLog(JfreeBar.class);
	
    public static String getlineName(){
	   	  
	 String str = "";  
	     
	 DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
	 dataset.addValue(610, "Guangzhou", "Pig");
	 dataset.addValue(220, "Guangzhou", "Beef");
	 dataset.addValue(530, "Guangzhou", "Chicken");
	 dataset.addValue(340, "Guangzhou", "Fish");

	 JFreeChart chart = ChartFactory.createBarChart3D("肉类销量统计图", 
	 "type", 
	 "amount", 
	 dataset, 
	 PlotOrientation.VERTICAL, 
	 false, 
	 false, 
	 false); 
	
	
	 
 	 
	 try {  
		str = ServletUtilities.saveChartAsPNG(chart, 500, 300, null);
	} catch (IOException e) {
		logger.info(e);  
		
	}  
	   return str ;
	   
   }
    
    
    public static String getbar(){
	   	  
   	 String str = "";  
   	     
   	double[][] data = new double[][] {{1310}, {720}, {1130}, {440}}; 
   	String[] rowKeys = { "pig", "beef", "chicken", "fish" };
   	String[] columnKeys = {""}; 
   	CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
   	JFreeChart chart = ChartFactory.createBarChart3D("广州肉类销量统计图", 
   	"type", 
   	"amount", 
   	dataset, 
   	PlotOrientation.VERTICAL, 
   	true, 
   	false, 
   	false); 
   	
   	  
    	  
   	 try {  
   		str = ServletUtilities.saveChartAsPNG(chart, 1000, 300, null);
   	} catch (IOException e) {
   		logger.info(e); 
   		
   	}  
   	   return str ;
   	   
      }
     
    
    public static String getDynamicsales(String starttime, String endtime,String branch,String type){
    	 
    	String str = ""; 
    	
        List<Inventory> sale = SaleDownLoad.get(starttime, endtime);
    	
    	Collection<Inventory> inven = InventoryChange.get(endtime);  
    	
    	Map<String,Inventory> mapsale = InventoryChange.changeMap(sale, branch, type);
    	Map<String,Inventory> mapinven = InventoryChange.changeMap(inven, branch, type);
    	  
    	logger.info(mapinven.size());   
    	double[][] data = new double[51][1];  
      	StringBuffer rowKeys = new StringBuffer();
      	String[] columnKeys = {""};     
      	DecimalFormat    df   = new DecimalFormat("######0.00");   
    	int count = -1 ;
    	if(!mapinven.isEmpty()){ 
    		Set<Map.Entry<String,Inventory>> set = mapinven.entrySet();
    		Iterator<Map.Entry<String,Inventory>> it = set.iterator();
    		while(it.hasNext() && count <50){
    			double salecount = 0 ;
    			double incount = 0 ;  
    			double dynamic = 0 ;  
    			count ++;
    			
    			Map.Entry<String,Inventory> mapent = it.next();
    			String key = mapent.getKey();
    			Inventory in = mapent.getValue();
    			  
    			rowKeys.append(in.getGoodpName()+"_");  
    			 
    			incount = in.getATP();
    			 
    			Inventory insale = mapsale.get(key);
    			
    			if(null != insale){
    				salecount = insale.getSaleNum(); 
    			}
    			
    			if(0 == incount){
    				dynamic = 0 ;
    			}else {  
    				dynamic = salecount/incount ;
    			} 
    			
    			
    			//logger.info(1- dynamic); 
    			data[count][0] = Double.valueOf(df.format(1- dynamic));
    			 
    			
    			
    		}
    	}
          
    	  logger.info(count); 
    	logger.info(StringUtill.GetJson(data));
    	//logger.info(rowKeys);  
    	 String strs = rowKeys.substring(0,rowKeys.length()-1 );
    	// logger.info(strs);
    	//logger.info(StringUtill.GetJson(strs.split("_"))); 
    	 String[] strss = strs.split("_");
    	logger.info(data[0].length);
    	logger.info(strs.split("_").length);
    	 
    	
    	
    	
    	
      	
      	     
      	//double[][] datas = new double[][] {{1310}, {720}, {1130}, {440}}; 
      	//logger.info(StringUtill.GetJson(datas));    
      	//String[] rowKeys = { "pig", "beef", "chicken", "fish" };
      //	String[] columnKeys = {""};  
      	CategoryDataset dataset = DatasetUtilities.createCategoryDataset(strss, columnKeys, data); 
      	JFreeChart chart = ChartFactory.createBarChart3D("产品销量统计图", 
      	"type",  
      	"amount",   
      	dataset, 
      	PlotOrientation.VERTICAL, 
      	true, 
      	false,  
      	false); 
      	
      	   
       	  
      	 try {  
      		str = ServletUtilities.saveChartAsPNG(chart, 300, 500, null);
      	} catch (IOException e) {
      		logger.info(e); 
      		
      	}  
      	   return str ;
      	   
         }
       
    
    public static String getbardetail(){
	   	  
      	 String str = "";  
      	      
      	double[][] data = new double[][] {{1310, 1220, 1110, 1000}, 
      			{720, 700, 680, 640}, 
      			{1130, 1020, 980, 800}, 
      			{440, 400, 360, 300}}; 
      			String[] rowKeys = { "pig", "beef", "chicken", "fish" };
      			  String[] columnKeys = { "Guangzhou", "Shenzhen", "Dongguan", "Foshan" }; 
      			CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
      			JFreeChart chart = ChartFactory.createBarChart3D("肉类销量统计图", 
      			"type", 
      			"amount", 
      			dataset, 
      			PlotOrientation.VERTICAL, 
      			true, 
      			true, 
      			false); 
      			CategoryPlot plot = chart.getCategoryPlot(); 
      			//设置网格背景颜色 
      			plot.setBackgroundPaint(Color.white); 
      			//设置网格竖线颜色 
      			plot.setDomainGridlinePaint(Color.pink); 
      			//设置网格横线颜色 
      			plot.setRangeGridlinePaint(Color.pink); 
      			//显示每个柱的数值，并修改该数值的字体属性 
      			BarRenderer3D renderer = new BarRenderer3D(); 
      			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
      			renderer.setBaseItemLabelsVisible(true); 
      			//默认的数字显示在柱子中，通过如下两句可调整数字的显示 
      			//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题 
      			renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT)); 
      			renderer.setItemLabelAnchorOffset(10D); 
      			//设置每个地区所包含的平行柱的之间距离 
      			//renderer.setItemMargin(0.3); 
      			plot.setRenderer(renderer); 
      			//设置地区、销量的显示位置 
      			//将下方的“肉类”放到上方 
      			plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT); 
      			//将默认放在左边的“销量”放到右方 
      			plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT); 
      	
      	 
       	
      	 try {  
      		str = ServletUtilities.saveChartAsPNG(chart, 500, 300, null);
      	} catch (IOException e) {
      		logger.info(e); 
      		
      	}  
      	   return str ;
      	   
         }
    
    
} 
