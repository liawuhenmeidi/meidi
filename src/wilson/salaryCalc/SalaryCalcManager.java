package wilson.salaryCalc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadSalaryModel;
import wilson.upload.XLSReader;
import database.DB;

public class SalaryCalcManager {
	
	protected static Log logger = LogFactory.getLog(SalaryCalcManager.class);
	private static List<UploadOrder> unCalcUploadOrders = new ArrayList<UploadOrder>();
	
	public static List<SalaryResult> calcSalary(List<UploadOrder> uploadOrders,List<UploadSalaryModel> salaryModels){
		unCalcUploadOrders = new ArrayList<UploadOrder>();
		if(uploadOrders == null || salaryModels == null ){
			return new ArrayList<SalaryResult>();
		}
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		
		//匹配的SalaryModels列表(取committime最新的使用)
		List<UploadSalaryModel> matchedSalaryModels = new ArrayList<UploadSalaryModel>();
		
		UploadOrder tempOrder = new UploadOrder();
		UploadSalaryModel tempSalaryModel = new UploadSalaryModel();
		SalaryResult tempSalaryResult = new SalaryResult();
		boolean matched = false;
		
		for(int i = 0 ; i < uploadOrders.size() ; i ++){
			//清空temp变量
			matchedSalaryModels = new ArrayList<UploadSalaryModel>();
			tempOrder = new UploadOrder();
			tempSalaryModel = new UploadSalaryModel();
			tempSalaryResult = new SalaryResult();
			matched = false;
			
			
			
			tempOrder = uploadOrders.get(i);
			tempOrder.setType(tempOrder.getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")","").replace("）", "").replace("（", ""));
			//如果type是空，直接放到人工归类列表里面
			if(tempOrder.getType()==""||tempOrder.getType()==null||tempOrder.getType().equals("")){
				unCalcUploadOrders.add(tempOrder);
				continue;
			}
			
			//在salaryModels中，寻找对应的model
			for(int j = 0 ; j < salaryModels.size() ; j ++){
				tempSalaryModel = salaryModels.get(j);
				tempSalaryModel.setType(tempSalaryModel.getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")","").replace("）", "").replace("（", ""));
				//如果为空，直接跳过
				if(tempSalaryModel.getType()==""||tempSalaryModel.getType()==null||tempSalaryModel.getType().equals("")){
					continue;
				}
				
				//如果相等，则添加到匹配列表中
				
				//System.out.println("tempOrder =" + tempOrder.getType().trim() + " = tempSalaryModel " + tempSalaryModel.getType() + " ?  " + tempOrder.getType().trim().contains(tempSalaryModel.getType().trim()));
				
				if(tempOrder.getType().trim().equals(tempSalaryModel.getType().trim())){
					matched = true;
					matchedSalaryModels.add(tempSalaryModel);
				}
			}
			
			//如果匹配了
			if(matched){
				//匹配数为1的时候
				if(matchedSalaryModels.size() == 1 ){
					tempSalaryResult = new SalaryResult(tempOrder,matchedSalaryModels.get(0));
					//计算成功，就把结果实例加到result中，没成功，就直接仍入人工匹配列表
					if(tempSalaryResult.calc()){
						result.add(tempSalaryResult);
					}else{
						unCalcUploadOrders.add(tempOrder);
					}
				
				//匹配数大于1的时候
				}else if(matchedSalaryModels.size() > 1){
					
					//取commitTime为最新的一个lastesSalaryModel
					UploadSalaryModel latestSalaryModel = new UploadSalaryModel();
					try{
						for(int k = 0 ; k < matchedSalaryModels.size() ; k ++){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
							Date commitTime = sdf.parse(matchedSalaryModels.get(k).getCommitTime());
							Date tempTime = new Date();
							if(k == 0){
								tempTime = commitTime;
							}
							
							if(commitTime.after(tempTime)){
								tempTime = commitTime;
								latestSalaryModel = matchedSalaryModels.get(k);
							}
						}
					}catch (Exception e) {
						unCalcUploadOrders.add(tempOrder);
					}
					
					//计算成功，就把结果实例加到result中，没成功，就直接仍入人工匹配列表
					tempSalaryResult = new SalaryResult(tempOrder,latestSalaryModel);
					if(tempSalaryResult.calc()){
						result.add(tempSalaryResult);
					}else{
						unCalcUploadOrders.add(tempOrder);
					}
					
				//其他情况，一概当作没匹配
				}else{
					unCalcUploadOrders.add(tempOrder);
				}
			
			//如果没匹配
			}else{
				unCalcUploadOrders.add(tempOrder);
			}
			
		}
		return result;
	}

	public static List<UploadOrder> getUnCalcUploadOrders() {
		return unCalcUploadOrders;
	}
	
	public static boolean saveSalaryResult(List<SalaryResult> salaryResult){
		boolean flag = false;
		Connection conn = DB.getConn();
		String sql = "update uploadorder set checked = ? where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		boolean autoCommit = false;
		try {
			//事务开始
			logger.info("事务开始");
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for(int i = 0 ; i < salaryResult.size() ; i ++){
				pstmt.setInt(1,2);
				pstmt.setInt(2, salaryResult.get(i).getUploadOrder().getId());
				pstmt.executeUpdate();
			}
			
			sql = "insert into salaryresult VALUES(null,?,?,?,?,?)";
			pstmt = DB.prepare(conn, sql);
			for(int i = 0 ; i < salaryResult.size() ; i ++){
				pstmt.setInt(1,salaryResult.get(i).getUploadOrder().getId());
				pstmt.setInt(2,salaryResult.get(i).getSalaryModel().getId());
				pstmt.setString(3,salaryResult.get(i).getCalcTime());
				pstmt.setDouble(4,salaryResult.get(i).getSalary());
				pstmt.setInt(5,salaryResult.get(i).getStatus());
				pstmt.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(autoCommit);
			
			flag = true ;
			logger.info("事务结束");
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败，请注意!!!");
				e1.printStackTrace();
			}
			flag = false;
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}

	
	
	public static List<SalaryResult> getSalaryResult() {
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		Map<Integer,UploadOrder> orderMap = new HashMap<Integer,UploadOrder>();
		
		String sql = "select * from uploadorder where checked = 2";
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder tempOrder = new UploadOrder();
		SalaryResult tempResult = new SalaryResult();
		try {
			while(rs.next()){
				tempOrder.setId(rs.getInt("id"));
				tempOrder.setName(rs.getString("name"));
				tempOrder.setSaleManName(rs.getString("salesman"));
				tempOrder.setShop(rs.getString("shop"));
				tempOrder.setPosNo(rs.getString("posno"));
				tempOrder.setSaleTime(rs.getString("saletime"));
				tempOrder.setType(rs.getString("type"));
				tempOrder.setNum(rs.getInt("num"));
				tempOrder.setSalePrice(rs.getDouble("saleprice"));
				tempOrder.setFileName(rs.getString("filename"));
				tempOrder.setChecked(rs.getInt("checked"));
				tempOrder.setCheckedTime(rs.getString("checkedtime"));
				tempOrder.setCheckOrderId(rs.getInt("checkorderid"));
				orderMap.put(tempOrder.getId(), tempOrder);
				tempOrder = new UploadOrder();
			}
			
			sql = "select * from salaryresult where status = 0";
			logger.info(sql);
			stmt = DB.getStatement(conn); 
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				tempResult.setId(rs.getInt("id"));
				tempResult.setUploadOrderId(rs.getInt("uploadorderid"));
				tempResult.setUploadSalaryModelid(rs.getInt("uploadsalarymodelid"));
				tempResult.setCalcTime(rs.getString("calctime"));
				tempResult.setSalary(rs.getDouble("salary"));
				tempResult.setStatus(rs.getInt("status"));
				
				if(orderMap.containsKey(tempResult.getUploadOrderId())){
					tempResult.setUploadOrder(orderMap.get(tempResult.getUploadOrderId()));
					result.add(tempResult);
				}else{
					
				}
				tempResult = new SalaryResult();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	
	//取出salaryResult中的uploadorder，并返回
	public static List<UploadOrder> getUploadOrderFromSalaryResult(List<SalaryResult> salaryResult){
		List<UploadOrder> result = new ArrayList<UploadOrder>();
		for(int i = 0 ; i < salaryResult.size() ; i ++){
			result.add(salaryResult.get(i).getUploadOrder());
		}
		return result;
	}
	
	public static List<SalaryResult> getSalaryResultByName(String name) {
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		Map<Integer,SalaryResult> tempSalaryResultMap = new HashMap<Integer,SalaryResult>();
		
		String sql = "select * from salaryresult where status = 0";
		
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder tempOrder = new UploadOrder();
		SalaryResult tempResult = new SalaryResult();
		try {
			while(rs.next()){
				tempResult = new SalaryResult();
				tempResult.setId(rs.getInt("id"));
				tempResult.setUploadOrderId(rs.getInt("uploadorderid"));
				tempResult.setUploadSalaryModelid(rs.getInt("uploadsalarymodelid"));
				tempResult.setCalcTime(rs.getString("calctime"));
				tempResult.setSalary(rs.getDouble("salary"));
				tempResult.setStatus(rs.getInt("status"));
				tempSalaryResultMap.put(tempResult.getUploadOrderId(), tempResult);
			}
			
			sql = "select * from uploadorder where name = '" + name + "'";
			//sql += " and checked != -1";
			logger.info(sql);
			stmt = DB.getStatement(conn); 
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				tempOrder = new UploadOrder();
				tempOrder.setId(rs.getInt("id"));
				tempOrder.setName(rs.getString("name"));
				tempOrder.setSaleManName(rs.getString("salesman"));
				tempOrder.setShop(rs.getString("shop"));
				tempOrder.setPosNo(rs.getString("posno"));
				tempOrder.setSaleTime(rs.getString("saletime"));
				tempOrder.setType(rs.getString("type"));
				tempOrder.setNum(rs.getInt("num"));
				tempOrder.setSalePrice(rs.getDouble("saleprice"));
				tempOrder.setFileName(rs.getString("filename"));
				tempOrder.setChecked(rs.getInt("checked"));
				tempOrder.setCheckedTime(rs.getString("checkedtime"));
				tempOrder.setCheckOrderId(rs.getInt("checkorderid"));
				
				tempResult = new SalaryResult();
				if(tempSalaryResultMap.containsKey(tempOrder.getId())){
					tempResult = tempSalaryResultMap.get(tempOrder.getId());
					
				}else{
					tempResult.setSalary(null);
				}
				tempResult.setUploadOrder(tempOrder);
				result.add(tempResult);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	public static List<SalaryResult> getSalaryResultByDate(Date startDate,
			Date endDate) {
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		Map<Integer,UploadOrder> orderMap = new HashMap<Integer,UploadOrder>();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sql = "select * from uploadorder where uploadtime >= '" + fmt.format(startDate) + "' and uploadtime <= '" + fmt.format(endDate) + "'";
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder tempOrder = new UploadOrder();
		SalaryResult tempResult = new SalaryResult();
		try {
			while(rs.next()){
				tempOrder.setId(rs.getInt("id"));
				tempOrder.setName(rs.getString("name"));
				tempOrder.setSaleManName(rs.getString("salesman"));
				tempOrder.setShop(rs.getString("shop"));
				tempOrder.setPosNo(rs.getString("posno"));
				tempOrder.setSaleTime(rs.getString("saletime"));
				tempOrder.setType(rs.getString("type"));
				tempOrder.setNum(rs.getInt("num"));
				tempOrder.setSalePrice(rs.getDouble("saleprice"));
				tempOrder.setFileName(rs.getString("filename"));
				tempOrder.setChecked(rs.getInt("checked"));
				tempOrder.setCheckedTime(rs.getString("checkedtime"));
				tempOrder.setCheckOrderId(rs.getInt("checkorderid"));
				orderMap.put(tempOrder.getId(), tempOrder);
				tempOrder = new UploadOrder();
			}
			
			sql = "select * from salaryresult where status = 0";
			logger.info(sql);
			stmt = DB.getStatement(conn); 
			rs = DB.getResultSet(stmt, sql);
			while(rs.next()){
				tempResult.setId(rs.getInt("id"));
				tempResult.setUploadOrderId(rs.getInt("uploadorderid"));
				tempResult.setUploadSalaryModelid(rs.getInt("uploadsalarymodelid"));
				tempResult.setCalcTime(rs.getString("calctime"));
				tempResult.setSalary(rs.getDouble("salary"));
				tempResult.setStatus(rs.getInt("status"));
				if(orderMap.containsKey(tempResult.getUploadOrderId())){
					tempResult.setUploadOrder(orderMap.get(tempResult.getUploadOrderId()));
					result.add(tempResult);
					orderMap.remove(tempResult.getUploadOrderId());
				}
				tempResult = new SalaryResult();
			}
			
			

	        
			
			Set<Integer> keys = orderMap.keySet();
			for (Iterator<Integer> it = keys.iterator(); it.hasNext();) {
	            Integer key = (Integer) it.next();
	            tempResult = new SalaryResult();
				tempResult.setSalary(null);
				tempResult.setUploadOrder(orderMap.get(key));
				result.add(tempResult);
	        }
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}

}
