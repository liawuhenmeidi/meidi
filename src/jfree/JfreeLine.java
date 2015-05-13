package jfree;


import java.awt.Font;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
   
public class JfreeLine {
	protected static Log logger = LogFactory.getLog(JfreeLine.class);
    public static String getlineName(){
	   	 
	 String str = "";  
	    
	 TimeSeries timeSeries = new TimeSeries("", Day.class); 
	 //时间曲线数据集合   
	 TimeSeriesCollection lineDataset = new TimeSeriesCollection(); 
	 //构造数据集合 
	 
	 timeSeries.add(new Day(1,1, 2007), 1); 
	 timeSeries.add(new Day(2,1, 2007), 3); 
	 timeSeries.add(new Day(3,1, 2007), -3); 
	 timeSeries.add(new Day(4,1, 2007), 2); 
	 timeSeries.add(new Day(5,1, 2007), -1); 
	 timeSeries.add(new Day(6,1, 2007), 0); 
	 timeSeries.add(new Day(7,1, 2007), 7); 
	 timeSeries.add(new Day(8,1, 2007), 8); 
	 timeSeries.add(new Day(9,1, 2007), 1); 
	 timeSeries.add(new Day(10,1, 2007), -5);  
	 timeSeries.add(new Day(11,1, 2007), 4); 
	 timeSeries.add(new Day(12,1, 2007), 5); 
	 lineDataset.addSeries(timeSeries); 
	 
	 
	 JFreeChart chart = ChartFactory.createTimeSeriesChart("line", "month", "visit amount", lineDataset, true, true, true); 
	
	 TextTitle textTitle = chart.getTitle();
	 	textTitle.setFont(new Font("黑体",Font.BOLD,15));
	 		 
	 //设置子标题 
	 TextTitle subtitle = new TextTitle("2007年度", new Font("黑体", Font.BOLD, 12)); 
	 chart.addSubtitle(subtitle); 
	 //设置主标题  
	 chart.setTitle(new TextTitle("阿蜜果blog访问量统计", new Font("黑体", Font.ITALIC, 15))); 
	 chart.setAntiAlias(true);   
	  
	 
 	
	 try {  
		str = ServletUtilities.saveChartAsPNG(chart, 500, 300, null);
	} catch (IOException e) {
		logger.info(e); 
		
	}  
	   return str ;
	   
   }
} 
