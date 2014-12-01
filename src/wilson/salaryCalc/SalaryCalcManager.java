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
import org.apache.poi.util.StringUtil;

import utill.StringUtill;
import wilson.catergory.CatergoryManager;
import wilson.catergory.CatergoryMaping;
import wilson.upload.SalaryModelUpload;
import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadSalaryModel;
import wilson.upload.XLSReader;
import database.DB;

public class SalaryCalcManager {
	
	protected static Log logger = LogFactory.getLog(SalaryCalcManager.class);
	private static List<UploadOrder> unCalcUploadOrders = new ArrayList<UploadOrder>();
	
	public static List<SalaryResult> sortSalaryResult(
			List<SalaryResult> input,String CatergoryMapingName) {
		if(input.size() <= 0 ){
			return input;
		}
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		
		List<SalaryResult> noMapingList = new ArrayList<SalaryResult>();
		List<CatergoryMaping> cmMapingList = CatergoryManager.getCatergory(CatergoryMapingName);
		
		while(input.size() != 0 ){
			List<SalaryResult> sameFileNameList = new ArrayList<SalaryResult>();
			String tempFileName = "";
			
			tempFileName = input.get(0).getUploadOrder().getName().trim();
			sameFileNameList.add(input.get(0));
			sameFileNameList.remove(0);
			for(int i = 0; i < input.size() ; i++){
				if(input.get(i).getUploadOrder().getShop().trim().equals(tempFileName)){
					sameFileNameList.add(input.get(i));
					input.remove(i);
					i --;
				}
			}
			tempFileName = "";
		
			String tempName = "";
			String tempShop = "";
			List<SalaryResult> sameShopList = new ArrayList<SalaryResult>();
			
			
			while(sameFileNameList.size() != 0){
				tempShop = sameFileNameList.get(0).getUploadOrder().getShop().trim();
				sameShopList.add(sameFileNameList.get(0));
				sameFileNameList.remove(0);
				for(int i = 0; i < sameFileNameList.size() ; i++){
					
					if(sameFileNameList.get(i).getUploadOrder().getShop().trim().equals(tempShop)){
						sameShopList.add(sameFileNameList.get(i));
						sameFileNameList.remove(i);
						
						i --;
					}
				}
				tempShop = "";
				
				while(sameShopList.size() != 0){
					
					//正在写
					tempName = sameShopList.get(0).getSalaryModel().getCatergory().trim();
					if(StringUtill.isNull(tempName)){
						noMapingList.add(sameShopList.get(0));
						continue;
					}
					
					
					result.add(sameShopList.get(0));
					sameShopList.remove(0);
					for(int i = 0 ; i < sameShopList.size() ; i++){
						if(sameShopList.get(i).getUploadOrder().getSaleManName().trim().equals(tempName)){
							result.add(sameShopList.get(i));
							sameShopList.remove(i);
							
							i --;
						}
					}
					tempName = "";
				}
					
				
			
			}

		}
		return result;
	}
	
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
	
	public static boolean saveSalaryResult(SalaryResult input){
		if(input.getId() > 0){
			boolean flag = false;
			Connection conn = DB.getConn();
			boolean autoCommit = false;
			String sql = "update uploadorder set name = ?,shop=?,posno=?,salesman=?,saletime=?,type=?,num=?,saleprice=? where id =" + input.getUploadOrderId();
			PreparedStatement pstmt = DB.prepare(conn, sql);
			
			try {
				//事务开始
				logger.info("事务开始");
				autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);

				logger.info(sql);
				UploadOrder uo = input.getUploadOrder();
				
				pstmt.setString(1, uo.getName());
				pstmt.setString(2, uo.getShop());
				pstmt.setString(3,uo.getPosNo());
				pstmt.setString(4, uo.getSaleManName());
				pstmt.setString(5, uo.getSaleTime());
				pstmt.setString(6, uo.getType());
				pstmt.setInt(7, uo.getNum());
				pstmt.setDouble(8, uo.getSalePrice());
				pstmt.executeUpdate();
				
				sql = "update salaryresult set salary = ? where id = " + input.getId();
				logger.info(sql);
				pstmt = DB.prepare(conn, sql);
				pstmt.setDouble(1, input.getSalary());
				pstmt.executeUpdate();
				
				
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
			
			
			
		}else{
			return false;
		}
		
		
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
			
			
			sql = "select * from uploadorder where name = '" + name + "' order by shop";
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
					//千万别改这里
					tempResult.setSalary(null);
					
					tempResult.setUploadSalaryModelid(-1);
				}
				tempResult.setUploadOrder(tempOrder);
				result.add(tempResult);

			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = new ArrayList<SalaryResult>();
			return result;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		//初始化salaryModel
		result = initSalaryModel(result);
		return result;
	}
	
	public static boolean resetSalaryResultByName(String name) {
		boolean result =false;
		String idSTR = "";
		
		String sql = "select id from uploadorder where name = '" + name + "' and checked = 2";
		
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while(rs.next()){
				idSTR+=rs.getInt("id") + ",";
			}
			if(idSTR.endsWith(",")){
				idSTR = idSTR.substring(0,idSTR.length()-1);
			}
			logger.info(sql);
			
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			sql = "update uploadorder set checked = 1 where id in ( " + idSTR + ")";
			stmt = DB.getStatement(conn); 
			stmt.executeUpdate(sql);
			
			sql = "delete from salaryresult where uploadorderid in ( " + idSTR + ")";
			stmt = DB.getStatement(conn); 
			stmt.executeUpdate(sql);
			
			conn.commit();
			conn.setAutoCommit(autoCommit);
			
			
			
		} catch (SQLException e) {
			try {
				logger.info("回滚!");
				conn.rollback();
			} catch (SQLException e1) {
				logger.info("回滚失败!!请注意!!");
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	public static SalaryResult getSalaryResultById(int id) {
		SalaryResult result = new SalaryResult();
		String sql = "select * from salaryresult where id = " + id;
		
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder tempOrder = new UploadOrder();
		try {
			rs.next();
			result.setId(rs.getInt("id"));
			result.setUploadOrderId(rs.getInt("uploadorderid"));
			result.setUploadSalaryModelid(rs.getInt("uploadsalarymodelid"));
			result.setCalcTime(rs.getString("calctime"));
			result.setSalary(rs.getDouble("salary"));	
			result	.setStatus(rs.getInt("status"));		
			
			sql = "select * from uploadorder where id = " + result.getUploadOrderId();
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
				result.setUploadOrder(tempOrder);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = new SalaryResult();
			return result;
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		
		List<SalaryResult> tmp = new ArrayList<SalaryResult>();
		tmp.add(result);
		tmp = initSalaryModel(tmp);
		return tmp.get(0);
	}
	
	public static List<SalaryResult> getSalaryResultByDate(Date startDate,
			Date endDate) {
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		Map<Integer,UploadOrder> orderMap = new HashMap<Integer,UploadOrder>();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sql = "select * from uploadorder where uploadtime >= '" + fmt.format(startDate) + "' and uploadtime <= '" + fmt.format(endDate) + "' order by name,shop";
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
				tempResult.setUploadSalaryModelid(-1);
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
		//初始化salaryModel
		result = initSalaryModel(result);
		return result;
	}
	
	public static List<SalaryResult> initSalaryModel(List<SalaryResult> input){
		List<SalaryResult> result = input;
		try{

			List<Integer> uploadSalaryModelIdList = new ArrayList<Integer>();
			String uploadSalaryModelIdListSTR = "";
			
			for(int i = 0 ; i < input.size() ; i++){
				if(!uploadSalaryModelIdList.contains(input.get(i).getUploadSalaryModelid())){
					uploadSalaryModelIdList.add(input.get(i).getUploadSalaryModelid());
				}
			}
			
			for(int i = 0 ; i < uploadSalaryModelIdList.size(); i ++){
				uploadSalaryModelIdListSTR += uploadSalaryModelIdList.get(i) + ",";
			}
			
			if(uploadSalaryModelIdListSTR.endsWith(",")){
				uploadSalaryModelIdListSTR = uploadSalaryModelIdListSTR.substring(0,uploadSalaryModelIdListSTR.length()-1);
			}

			if("".equals(uploadSalaryModelIdListSTR)){
				return result;
			}
			List<UploadSalaryModel> salaryModelList = new ArrayList<UploadSalaryModel>();
			
			String sql = "select * from uploadsalarymodel where id in (" + uploadSalaryModelIdListSTR + ")";
			//sql += " and checked != -1";
			logger.info(sql);
			Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);

			UploadSalaryModel usm = new UploadSalaryModel();
			while(rs.next()){
				usm.setName(rs.getString("name"));
				usm.setShop(rs.getString("shop"));
				usm.setId(rs.getInt("id"));
				usm.setStartTime(rs.getString("starttime"));
				usm.setEndTime(rs.getString("endtime"));
				usm.setCatergory(rs.getString("catergory"));
				usm.setType(rs.getString("type"));
				usm.setContent(rs.getString("content"));
				usm.setCommitTime(rs.getString("committime"));
				usm.setFileName(rs.getString("filename"));
				usm.setStatus(rs.getInt("status"));
				salaryModelList.add(usm);
				usm = new UploadSalaryModel();
			}
			
			//将salaryModel放到对应的salaryresult中去
			for(int i = 0 ; i < salaryModelList.size() ; i ++){
				for(int j = 0 ; j < result.size() ; j ++){
					if(salaryModelList.get(i).getId() == result.get(j).getUploadSalaryModelid()){
						result.get(j).setSalaryModel(salaryModelList.get(i));
						break;
					}		
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

}
