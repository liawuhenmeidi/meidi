package change;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;
import utill.StringCompare;
import utill.StringUtill;
import wilson.matchOrder.MatchOrder;
import database.DB;

public class UploadChangeManager {
	protected static Log logger = LogFactory.getLog(UploadChangeManager.class);
	public static int count  = 0 ;   
	public static void save(BranchTypeChange bt){
		List<String> sqls = new ArrayList<String>();
		String sqlall = "delete from mdchange ";
		sqls.add(sqlall); 
		Map<String, List<String>>  map = bt.getMaplist();
		Set<Map.Entry<String, List<String>>> setmap = map.entrySet();
		
		Iterator<Map.Entry<String, List<String>>> itmap = setmap.iterator();
		while(itmap.hasNext()){
			Map.Entry<String, List<String>> mape = itmap.next();
			String name = mape.getKey();
			List<String> list = mape.getValue();
			for(int i=0;i<list.size();i++){
				String real = list.get(i);
				String sql = " insert into mdchange (id,changes,bechange) values (null,'"+real+"','"+name+"')";
			    sqls.add(sql);
			} 
		} 
		DBUtill.sava(sqls); 
	}
  
	public static Map<Integer,UploadChange> getmap(){
		List<UploadChange> list = getUnCheckedUploadOrders();
		Map<Integer,UploadChange> map  = new HashMap<Integer,UploadChange>();
		for(int i=0;i<list.size();i++){
			UploadChange mc =list.get(i);
			map.put(mc.getId(), mc);
		}
		
	  return map ;
	}
	
	public static List<UploadChange> getUnCheckedUploadOrders() {
		List<UploadChange> list = new ArrayList<UploadChange>();
		
		Connection conn = DB.getConn();
		String sql = "select * from mduploadchange " ;
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  // cname,uname,phone,locate
			while (rs.next()) { 
				UploadChange g = new UploadChange();
				g.setFilename(rs.getString("filename"));
				g.setName(rs.getString("name"));
				g.setId(rs.getInt("id"));
				g.setStatues(rs.getInt("statues")); 
				list.add(g);
			}     
		} catch (SQLException e) { 
			e.printStackTrace();
		} finally { 
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}  
		return list;
	}
	 
	public static List<MathChange> getmatch(List<UploadChange> left,List<UploadChange> right,String statues){
		Map<Integer,UploadChange> mapletf = new HashMap<Integer,UploadChange>();
		Map<Integer,UploadChange> mapright = new HashMap<Integer,UploadChange>();
		 
		List<MathChange> list = new ArrayList<MathChange>(); 
		
		for(int i=0;i<left.size();i++){
			UploadChange mc =left.get(i);
			mapletf.put(mc.getId(), mc);
		}
		
		for(int i=0;i<right.size();i++){
			UploadChange mc =right.get(i);
			mapright.put(mc.getId(), mc);
		}  
		  
		//logger.info(mapletf);
		//logger.info(mapright); 
		String name = "-1"; 
		while(left.size() > 0 && !StringUtill.isNull(name)){
			MathChange m = new MathChange(); 
			name = getSimilarPosFromList(left,right,statues);
			//logger.info(name);
			if(!StringUtill.isNull(name)){
				String[] names = name.split(",");  
				m.setBechange(mapright.get(Integer.valueOf(names[1])));
				m.setChange(mapletf.get(Integer.valueOf(names[0])));  
			//	logger.info(StringUtill.GetJson(m));
				list.add(m);  
			}
			
		}  
		 
		//System.out.println(name);   
		return list; 
		
	}
	
	public static String getSimilarPosFromList(List<UploadChange> left,List<UploadChange> right) {
		String result = "";
		
		double distance = 0.0;
		String resultDBShopName = "";
		String resultUploadShopName="";

		double similarityRatio = 0.0;
		//找出相似度最高的店名组合  
		for(int i = 0 ; i < left.size() ; i ++){
			for(int j = 0 ; j < right.size() ; j ++){
				similarityRatio = (double)StringCompare.getSimilarityRatio(left.get(i).getName(), right.get(j).getName());
				//logger.info(similarityRatio); 
				//count ++; 
				if(similarityRatio > distance){
					if(similarityRatio > 0.8){ 
						distance = similarityRatio;
						resultDBShopName = left.get(i).getName();
						resultUploadShopName = right.get(j).getName();
					}
					
					if(similarityRatio == 1){
						break; 
					}
				}
			}   
		}
		//logger.info(count);
		//logger.info(resultDBShopName);
		//logger.info(resultUploadShopName); 
		//找出这个组合对应在DBList中的开始位置 
		for(int i = 0 ; i < left.size() ; i ++){
			
			if(left.get(i).getName().equals(resultDBShopName)){
				
				result += left.get(i).getId() + ",";
				left.remove(i);
				break;
			}
		}
		
		//没找到?
		if(!result.endsWith(",")){
			return "";
		}
		
		if(distance < MatchOrder.SHOPNAME_DISTANCE){
			return result + "-1";
		}
		 
		//找出这个组合对应在UploadList中的开始位置
		for(int i = 0 ; i < right.size() ; i ++){
			if(right.get(i).getName().equals(resultUploadShopName)){
				result += right.get(i).getId();
				 right.remove(i);
				break; 
			}
		} 
		
		//没找到?
		if(result.endsWith(",")){
			return "";
		}
	// logger.info(result);
		return result;
	}
	
	
	public static String getSimilarPosFromList(List<UploadChange> left,List<UploadChange> right,String statues) {
		String result = "";
		 
		double distance = 0.0;
		String resultDBShopName = "";
		String resultUploadShopName="";
		//logger.info(statues); 
		double similarityRatio = 0.0;
		//找出相似度最高的店名组合  
		for(int i = 0 ; i < left.size() ; i ++){
			for(int j = 0 ; j < right.size() ; j ++){
				String leftname = "";
				String rightname = "";
				if("all".equals(statues)){
					leftname = left.get(i).getName();
					rightname = right.get(j).getName();
				}else if("cn".equals(statues)){  
					leftname = StringUtill.getStringNocn(left.get(i).getName());
					rightname = StringUtill.getStringNocn(right.get(j).getName()); 
				}else if("en".equals(statues)){ 
					leftname = StringUtill.getNumbers(left.get(i).getName());
					rightname = StringUtill.getNumbers(right.get(j).getName()); 
				}   
				//logger.info(leftname);
				similarityRatio = (double)StringCompare.getSimilarityRatio(leftname,rightname );
				//logger.info(similarityRatio); 
				//count ++; 
				if(similarityRatio > distance){ 
					if(similarityRatio > 0.9){ 
						distance = similarityRatio;
						resultDBShopName = left.get(i).getName();
						resultUploadShopName = right.get(j).getName();
					}
					
					if(similarityRatio == 1){
						break; 
					}
				}
			}   
		}
		//logger.info(count);
		//logger.info(resultDBShopName);
		//logger.info(resultUploadShopName); 
		//找出这个组合对应在DBList中的开始位置 
		for(int i = 0 ; i < left.size() ; i ++){
			
			if(left.get(i).getName().equals(resultDBShopName)){
				
				result += left.get(i).getId() + ",";
				left.remove(i);
				break;
			}
		}
		
		//没找到?
		if(!result.endsWith(",")){
			return "";
		}
		
		if(distance < MatchOrder.SHOPNAME_DISTANCE){
			return result + "-1";
		}
		 
		//找出这个组合对应在UploadList中的开始位置
		for(int i = 0 ; i < right.size() ; i ++){
			if(right.get(i).getName().equals(resultUploadShopName)){
				result += right.get(i).getId();
				 right.remove(i);
				break; 
			}
		} 
		
		//没找到?
		if(result.endsWith(",")){
			return "";
		}
	// logger.info(result);
		return result;
	}
	
	
	public static List<String> getAllUploadOrderNames(List<UploadChange> uploadorders){
		List<String> result = new ArrayList<String>();
		if(uploadorders == null || uploadorders.size() == 0){
			return result;
		}
		for(int i = 0 ; i < uploadorders.size() ; i ++){
			if(uploadorders.get(i) == null){
				continue; 
			}
			if(!result.contains(uploadorders.get(i).getFilename())){
				result.add(uploadorders.get(i).getFilename()); 
			}
		}
		return result;
	}
	
	
}
