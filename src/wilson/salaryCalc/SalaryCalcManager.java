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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import wilson.catergory.CatergoryManager;
import wilson.catergory.CatergoryMaping;
import wilson.catergory.HiddenCatergoryMapingManager;
import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadSalaryModel;
import database.DB;

public class SalaryCalcManager {
	
	protected static Log logger = LogFactory.getLog(SalaryCalcManager.class);
	
	
	
	
	public static List<String> getCatergoryFromResult(ArrayList<SalaryResult> input){
		List<String> result = new ArrayList<String>();
		String tmp = "";
		for(int i = 0 ; i < input.size() ; i ++){
			tmp = input.get(i).getSalaryModel().getCatergory();
			if(!result.contains(tmp)){
				result.add(tmp);
			}
		}
		return result;
	}
	
	public static List<SalaryResult> sortSalaryResult(
			List<SalaryResult> input,boolean isLocalCatergoryMaping) {
		if(input.size() <= 0 ){
			return input;
		}
		String CatergoryMapingName = "";
		CatergoryMapingName = CatergoryManager.getCatergoryMapingByUploadOrderName(input.get(0).getUploadOrder().getName());
		return sortSalaryResult(input,CatergoryMapingName,isLocalCatergoryMaping);
	}
	
	public static List<SalaryResult> sortSalaryResult(
			List<SalaryResult> input) {
		if(input.size() <= 0 ){
			return input;
		}
		String CatergoryMapingName = "";
		CatergoryMapingName = CatergoryManager.getCatergoryMapingByUploadOrderName(input.get(0).getUploadOrder().getName());
		return sortSalaryResult(input,false);
	}
	
	public static List<SalaryResult> sortSalaryResult(
			List<SalaryResult> input,String CatergoryMapingName) {
		return sortSalaryResult(input,CatergoryMapingName,false);
	}
	
	public static List<SalaryResult> sortSalaryResult(
			List<SalaryResult> input,String CatergoryMapingName,boolean isLocalCatergoryMaping) {
		if(input.size() <= 0 ){
			return input;
		}
		
		SalaryResult total = new SalaryResult();
		
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		
		List<CatergoryMaping> cmMapingList = new ArrayList<CatergoryMaping>();
		
		if(isLocalCatergoryMaping){
			String tmp_filename = input.get(0).getUploadOrder().getName();
			cmMapingList = HiddenCatergoryMapingManager.getCatergoryMapings(tmp_filename);
			if(cmMapingList.size() <= 0 ){
				cmMapingList = CatergoryManager.getCatergory(CatergoryMapingName);
			}
		}else{
			cmMapingList = CatergoryManager.getCatergory(CatergoryMapingName);
		}

		if(cmMapingList.size() <= 0 ){
			return input;
		}
		
		while(input.size() != 0 ){
			List<SalaryResult> sameFileNameList = new ArrayList<SalaryResult>();
			String tempFileName = "";
			
			tempFileName = input.get(0).getUploadOrder().getName().trim();
			sameFileNameList.add(input.get(0));
			sameFileNameList.remove(0);
			for(int i = 0; i < input.size() ; i++){
				if(input.get(i).getUploadOrder().getName().trim().equals(tempFileName)){
					sameFileNameList.add(input.get(i));
					input.remove(i);
					i --;
				}
			}
			tempFileName = "";
		
			String tempName = "";
			String tempShop = "";
			List<SalaryResult> sameShopList = new ArrayList<SalaryResult>();
			List<SalaryResult> unFinishedList = new ArrayList<SalaryResult>();
			HashMap<String, SalaryResult> catergoryTotal = new HashMap<String, SalaryResult>();
			
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
				
				
				CatergoryMaping tempCatergoryMaping = new CatergoryMaping();
				CatergoryMaping catergoryMapingtemplate = new CatergoryMaping();
				//SalaryResult sameShopTotal = new SalaryResult();

				
				while(sameShopList.size() != 0){
					
					//取得总分组规则
					boolean hasTemplate = false;
					for(int i = 0 ; i < cmMapingList.size() ; i ++){
						if(cmMapingList.get(i).getShop().trim().equals("")){
							catergoryMapingtemplate = cmMapingList.get(i);
							if(!catergoryMapingtemplate.getContent().equals("")){
								hasTemplate = true;
							}
							break;
						}
					}
					
					//取得对应的catergoryMaping
					boolean isFind = false;
					//找对应分组规则
					for(int i = 0 ; i < cmMapingList.size() ; i ++){
						if(sameShopList.get(0).getUploadOrder().getShop().trim().equals(cmMapingList.get(i).getShop().trim())){
							tempCatergoryMaping = cmMapingList.get(i);
							//保证规则不为空
							if(!tempCatergoryMaping.getContent().equals("")){
								isFind = true;
							}
							break;
						}
					}
					
					if(!isFind){
						if(hasTemplate){
							//使用总分组规则
							tempCatergoryMaping = catergoryMapingtemplate;
							isFind = true;
						}else{
							//添加到noMapingList
							//noMapingList.addAll(sameShopList);
							//sameShopList.clear();
						}
					}
					
					if(isFind){
						//通过对应的分组进行排序
						String rules[] = tempCatergoryMaping.getContent().split(",");
						List<String> items = new ArrayList<String>();
						SalaryResult sameGroupTotal = new SalaryResult();
						for(int i = 0 ; i < rules.length ; i ++){
							//初始化items
							String leftSide = rules[i].split(":")[0];
							String rightSide = "";
							if(!rules[i].endsWith(":")){
								rightSide = rules[i].split(":")[1];
							}
							for(int m = 0 ; m < leftSide.split("_").length ; m ++){
								items.add(leftSide.split("_")[m]);
							}
							
							for(int j = 0 ; j < sameShopList.size() ; j ++){
								//未完成单据，加入到未完成序列中
								if(!sameShopList.get(j).isFinished() ){
									unFinishedList.add(sameShopList.get(j));
									sameShopList.remove(j);
									j--;
									continue;
								}
								
								if(items.contains(sameShopList.get(j).getSalaryModel().getCatergory())){
									sameShopList.get(j).setSaleManName(rightSide);
									
									
									sameGroupTotal.setSalary(sameGroupTotal.getSalary() + sameShopList.get(j).getSalary());
									//设置店名，类别等
									sameGroupTotal.setStatus(SalaryResult.STATUS_TOTAL);
									sameGroupTotal.setUploadSalaryModelCatergory(leftSide);
									sameGroupTotal.setUploadOrderNum(sameGroupTotal.getUploadOrder().getNum() + sameShopList.get(j).getUploadOrder().getNum());
									sameGroupTotal.setUploadOrderSalePrice(sameGroupTotal.getUploadOrder().getSalePrice() + sameShopList.get(j).getUploadOrder().getTotal());
									sameGroupTotal.setUploadOrderPosNo("");
									sameGroupTotal.setUploadOrderShop(sameShopList.get(j).getUploadOrder().getShop());
									sameGroupTotal.setUploadOrderName(sameShopList.get(j).getUploadOrder().getName());
									sameGroupTotal.setSaleManName(rightSide);
									
									result.add(sameShopList.get(j));
									sameShopList.remove(j);
									j--;
								}
							}
							
							//如果有对应这个分组的数据
							if(sameGroupTotal.getStatus() == SalaryResult.STATUS_TOTAL){
								result.add(sameGroupTotal);
								
								//sameShopTotal.setUploadOrderShop(tempCatergoryMaping.getShop());
								//sameShopTotal.setUploadOrderNum(sameShopTotal.getUploadOrder().getNum() + sameGroupTotal.getUploadOrder().getNum());
								//sameShopTotal.setUploadOrderSalePrice(sameShopTotal.getUploadOrder().getSalePrice() + sameGroupTotal.getUploadOrder().getSalePrice());
								//sameShopTotal.setSalary(sameShopTotal.getSalary() + sameGroupTotal.getSalary());
								
								//类别总计开始
								if(catergoryTotal.containsKey(sameGroupTotal.getSalaryModel().getCatergory())){
									SalaryResult t = catergoryTotal.get(sameGroupTotal.getSalaryModel().getCatergory());
									t.setUploadOrderSalePrice(t.getUploadOrder().getSalePrice() + sameGroupTotal.getUploadOrder().getSalePrice());
									t.setSalary(t.getSalary() + sameGroupTotal.getSalary());
									t.setUploadOrderNum(t.getUploadOrder().getNum() + sameGroupTotal.getUploadOrder().getNum());
									catergoryTotal.put(sameGroupTotal.getSalaryModel().getCatergory(), t);
								}else{
									SalaryResult t = new SalaryResult();
									t.setStatus(SalaryResult.STATUS_TOTAL);
									
									t.setUploadOrderPosNo("");
									t.setUploadOrderShop(sameGroupTotal.getSalaryModel().getCatergory() + "-品类总计");
									t.setUploadOrderSalePrice(sameGroupTotal.getUploadOrder().getSalePrice());
									t.setSalary( sameGroupTotal.getSalary());
									t.setUploadOrderNum(sameGroupTotal.getUploadOrder().getNum());
									catergoryTotal.put(sameGroupTotal.getSalaryModel().getCatergory(), t);
								}						
								//类别总计结束
								
								total.setUploadOrderSalePrice(total.getUploadOrder().getSalePrice() + sameGroupTotal.getUploadOrder().getSalePrice());
								total.setSalary(total.getSalary() + sameGroupTotal.getSalary());
								total.setUploadOrderNum(total.getUploadOrder().getNum() + sameGroupTotal.getUploadOrder().getNum());
								
								//清空
								sameGroupTotal = new SalaryResult();
							}
							
						}
					}
					
					
					//剩余的
					SalaryResult othersTotal = new SalaryResult();
					if(sameShopList.size() > 0){
						
						//先保留下文件名和店名
						String fileName = sameShopList.get(0).getUploadOrder().getName();
						String shopName = sameShopList.get(0).getUploadOrder().getShop();
						
						for(int i = 0 ; i < sameShopList.size() ; i++){
							//未完成单据，加入到未完成序列中
							if(!sameShopList.get(i).isFinished() ){
								unFinishedList.add(sameShopList.get(i));
								sameShopList.remove(i);
								i--;
								continue;
							}
							
							othersTotal.setSalary(othersTotal.getSalary() + sameShopList.get(i).getSalary());
							othersTotal.setUploadOrderNum(othersTotal.getUploadOrder().getNum() + sameShopList.get(i).getUploadOrder().getNum());
							othersTotal.setUploadOrderSalePrice(othersTotal.getUploadOrder().getSalePrice() + sameShopList.get(i).getUploadOrder().getTotal());
						}
						//设置店名，类别等
						othersTotal.setUploadOrderName(fileName);
						othersTotal.setUploadOrderShop(shopName);
						othersTotal.setUploadOrderPosNo("");
						othersTotal.setStatus(SalaryResult.STATUS_TOTAL);
						
						
						//类别总计开始
						if(catergoryTotal.containsKey("其他")){
							SalaryResult t = catergoryTotal.get("其他");
							t.setUploadOrderSalePrice(t.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
							t.setSalary(t.getSalary() + othersTotal.getSalary());
							t.setUploadOrderNum(t.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
							catergoryTotal.put("其他", t);
						}else{
							SalaryResult t = new SalaryResult();
							t.setStatus(SalaryResult.STATUS_TOTAL);
							
							t.setUploadOrderPosNo("");
							t.setUploadOrderShop("其他-品类总计");
							t.setUploadOrderSalePrice(othersTotal.getUploadOrder().getSalePrice());
							t.setSalary( othersTotal.getSalary());
							t.setUploadOrderNum(othersTotal.getUploadOrder().getNum());
							catergoryTotal.put("其他", t);
						}						
						//类别总计结束
						
						total.setUploadOrderSalePrice(total.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
						total.setSalary(total.getSalary() + othersTotal.getSalary());
						total.setUploadOrderNum(total.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
						//sameShopTotal.setUploadOrderSalePrice(sameShopTotal.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
						//sameShopTotal.setSalary(sameShopTotal.getSalary() + othersTotal.getSalary());
						//sameShopTotal.setUploadOrderNum(sameShopTotal.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
						
						result.addAll(sameShopList);
						sameShopList.clear();
					}
					
					//未完成单据的
					if(unFinishedList.size() > 0){
						for(int i = 0 ; i < unFinishedList.size() ; i++){
							othersTotal.setUploadOrderNum(othersTotal.getUploadOrder().getNum() + unFinishedList.get(i).getUploadOrder().getNum());
							othersTotal.setUploadOrderSalePrice(othersTotal.getUploadOrder().getSalePrice() + unFinishedList.get(i).getUploadOrder().getTotal());
						}
						//设置店名，类别等
						othersTotal.setUploadOrderName(unFinishedList.get(0).getUploadOrder().getName());
						othersTotal.setUploadOrderShop(unFinishedList.get(0).getUploadOrder().getShop());
						othersTotal.setUploadOrderPosNo("");
						othersTotal.setStatus(SalaryResult.STATUS_TOTAL);
						
						//类别总计开始
						if(catergoryTotal.containsKey("其他")){
							SalaryResult t = catergoryTotal.get("其他");
							t.setUploadOrderSalePrice(t.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
							//t.setSalary(t.getSalary() + othersTotal.getSalary());
							t.setUploadOrderNum(t.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
							catergoryTotal.put("其他", t);
						}else{
							SalaryResult t = new SalaryResult();
							t.setStatus(SalaryResult.STATUS_TOTAL);

							t.setUploadOrderPosNo("");
							t.setUploadOrderShop("其他-品类总计");
							t.setUploadOrderSalePrice(othersTotal.getUploadOrder().getSalePrice());
							//t.setSalary( othersTotal.getSalary());
							t.setUploadOrderNum(othersTotal.getUploadOrder().getNum());
							catergoryTotal.put("其他", t);
						}						
						//类别总计结束
						
						total.setUploadOrderSalePrice(total.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
						total.setUploadOrderNum(total.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
						//sameShopTotal.setUploadOrderSalePrice(sameShopTotal.getUploadOrder().getSalePrice() + othersTotal.getUploadOrder().getSalePrice());
						//sameShopTotal.setSalary(sameShopTotal.getSalary() + othersTotal.getSalary());
						//sameShopTotal.setUploadOrderNum(sameShopTotal.getUploadOrder().getNum() + othersTotal.getUploadOrder().getNum());
						
						result.addAll(unFinishedList);
						unFinishedList.clear();
					}
					if(othersTotal.getStatus() == SalaryResult.STATUS_TOTAL){
						result.add(othersTotal);
					}
					
					
					//sameShopTotal.setUploadOrderPosNo("");
					//sameShopTotal.setUploadSalaryModelCatergory("总计");
					//sameShopTotal.setStatus(-1);					
					//result.add(sameShopTotal);
					
					
//					total.setUploadOrderSalePrice(total.getUploadOrder().getSalePrice() + sameShopTotal.getUploadOrder().getSalePrice());
//					total.setSalary(total.getSalary() + sameShopTotal.getSalary());
//					total.setUploadOrderNum(total.getUploadOrder().getNum() + sameShopTotal.getUploadOrder().getNum());
					//sameShopTotal = new SalaryResult();
				}
			
			}
			//分类统计total
			Iterator<String> it = catergoryTotal.keySet().iterator();
			String tempKey = "";
			while(it.hasNext()) {
				tempKey = it.next();
				if(!"其他".equals(tempKey)){
					result.add(catergoryTotal.get(tempKey));
				}
			}
			if(catergoryTotal.containsKey("其他")){
				result.add(catergoryTotal.get("其他"));
			}
			
			
			//总文件total
			total.setUploadOrderPosNo("");
			if(result.size() > 0){
				total.setUploadOrderShop(result.get(0).getUploadOrder().getName());
			}
			total.setStatus(SalaryResult.STATUS_TOTAL);
			result.add(total);
			total = new SalaryResult();
		}
		
		return result;
	}
	
	public static SalaryResult matchModel(UploadOrder tempOrder,List<UploadSalaryModel> salaryModels){
		SalaryResult result = null;
		tempOrder.removeCharecterFromType();
		
		List<UploadSalaryModel> matchedSalaryModels = new ArrayList<UploadSalaryModel>();
		UploadSalaryModel tempSalaryModel = new UploadSalaryModel();
		boolean matched = false;
		
		//如果type是空，返回空
		if(tempOrder.getType()==""||tempOrder.getType()==null||tempOrder.getType().equals("")){
			return result;
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
						
			if(tempOrder.getType().trim().toUpperCase().replaceAll(" ","").equals(tempSalaryModel.getType().trim().toUpperCase().replaceAll(" ",""))){
				matched = true;
				matchedSalaryModels.add(tempSalaryModel);
			}
		}
		
		//如果匹配了
		if(matched){
			//匹配数为1的时候
			if(matchedSalaryModels.size() == 1 ){
				result = new SalaryResult(tempOrder,matchedSalaryModels.get(0));
				//计算成功，就把结果实例加到result中，没成功，就直接仍入人工匹配列表
//				if(tempSalaryResult.calc()){
//					result.add(tempSalaryResult);
//				}else{
//					unCalcUploadOrders.add(tempOrder);
//				}
				return result;
			
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
							latestSalaryModel = matchedSalaryModels.get(k);
						}
						
						if(commitTime.after(tempTime)){
							tempTime = commitTime;
							latestSalaryModel = matchedSalaryModels.get(k);
						}
					}
				}catch (Exception e) {
					return result;
					//返回空
				}
				
				//计算成功，就把结果实例加到result中，没成功，就直接仍入人工匹配列表
				result = new SalaryResult(tempOrder,latestSalaryModel);
				
			//其他情况，一概当作没匹配
			}else{
				return result;
				//返回空
			}
		
		//如果没匹配
		}else{
			return result;
			//返回空
		}
		
		return result;
	}
	
	public static List<SalaryResult> calcSalary(List<UploadOrder> uploadOrders,List<UploadSalaryModel> salaryModels,HttpServletRequest request){
		List<UploadOrder> unCalcUploadOrders = new ArrayList<UploadOrder>();
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
			
			
			//不顶码的对比
			if(tempOrder.getSaleManName()==""||tempOrder.getSaleManName().equals("")){
				
				tempSalaryResult = matchModel(tempOrder,salaryModels);
				if(tempSalaryResult == null){
					unCalcUploadOrders.add(tempOrder);
				}else{
					if(tempSalaryResult.calc()){
						result.add(tempSalaryResult);
					}else{
						unCalcUploadOrders.add(tempOrder);
					}
				}
				
				
			//顶码的对比
				//salemanName -> type:num:price,type:num:price
			}else{
				try{
					String[] content = tempOrder.getSaleManName().split(",");
					List<SalaryResult> tempSRList = new ArrayList<SalaryResult>();
					
					for(int j =  0 ; j < content.length ; j ++){
						
						String type = content[j].split(":")[0];
						type = ProductService.getIDmap().get(Integer.parseInt(type)).getType();
						String num = content[j].split(":")[1];
						String price = content[j].split(":")[2];
						
						UploadOrder uo = tempOrder;
						SalaryResult sr = new SalaryResult();
						uo.setType(type);
						uo.setSalePrice(Double.parseDouble(price));
						uo.setNum(Integer.parseInt(num));
						sr = matchModel(uo,salaryModels);
						if(sr == null || !sr.calc()){
							throw new Exception();
						}
						tempSRList.add(sr);
						
					}
					
					result.addAll(tempSRList);
					tempSRList.clear();
					
				}catch(Exception e){
					
					unCalcUploadOrders.add(tempOrder);
				}
				
				
			}
				
			
			
			
			
		}
		request.getSession().setAttribute("calcResult", result);
		request.getSession().setAttribute("unCalcResult", unCalcUploadOrders);
		return result;
	}
	
	public static boolean saveSalaryResult(List<SalaryResult> salaryResult,String catergoryMapingName){
		boolean flag = false;
		if(salaryResult == null || salaryResult.size() <= 0){
			return false;
		}
		//存catergorymaping
		if(!HiddenCatergoryMapingManager.saveCatergoryMaping(salaryResult.get(0).getUploadOrder().getName(), catergoryMapingName)){
			return false;
		}
		
		Connection conn = DB.getConn();
		//暂时先这样。。。囧
		String sql = "update uploadorder set checked = ?,filename=?  where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		boolean autoCommit = false;
		try {
			//事务开始
			logger.info("事务开始");
			
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for(int i = 0 ; i < salaryResult.size() ; i ++){
				pstmt.setInt(1,2);
				pstmt.setString(2, catergoryMapingName);
				pstmt.setInt(3, salaryResult.get(i).getUploadOrder().getId());
				pstmt.executeUpdate();
			}
			
			sql = "insert into salaryresult VALUES(null,?,?,?,?,?)";
			pstmt = DB.prepare(conn, sql);
			for(int i = 0 ; i < salaryResult.size() ; i ++){
				pstmt.setInt(1,salaryResult.get(i).getUploadOrder().getId());
				pstmt.setInt(2,salaryResult.get(i).getSalaryModel().getId());
				pstmt.setString(3,salaryResult.get(i).getCalcTime());
				pstmt.setDouble(4,salaryResult.get(i).getSalary());
				//pstmt.setInt(5,salaryResult.get(i).getStatus());
				pstmt.setInt(5,0);
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
				tempOrder = UploadManager.getUploadOrderFromRS(rs);
				tempResult = new SalaryResult();
				if(tempSalaryResultMap.containsKey(tempOrder.getId())){
					tempResult = tempSalaryResultMap.get(tempOrder.getId());
					
					tempResult.setUploadOrder(tempOrder);
					
				}else{
					//千万别改这里
					tempResult.setSalary(null);
					if(tempOrder.getChecked() == UploadOrder.UNCHECK){
						tempResult.setStatus(SalaryResult.STATUS_UNCHECKOUT);
					}else if(tempOrder.getChecked() == UploadOrder.CHECKED){
						tempResult.setStatus(SalaryResult.STATUS_UNCALC);
					}
					tempResult.setUploadSalaryModelid(-1);
					tempResult.setUploadOrder(tempOrder);
				}
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
	
	//获取计算后的salaryResult
	public static List<SalaryResult> getCalcedSalaryResultByName(String name) {
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
					
					tempResult.setUploadOrder(tempOrder);
					result.add(tempResult);
				}else{
//					//千万别改这里
//					tempResult.setSalary(null);
//					if(tempOrder.getChecked() == UploadOrder.UNCHECK){
//						tempResult.setStatus(SalaryResult.STATUS_UNCHECKOUT);
//					}else if(tempOrder.getChecked() == UploadOrder.CHECKED){
//						tempResult.setStatus(SalaryResult.STATUS_UNCALC);
//					}
//					tempResult.setUploadSalaryModelid(-1);
//					tempResult.setUploadOrder(tempOrder);
				}
				
				

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
			
			sql = "update uploadorder set checked = 0 ,filename = '' where id in ( " + idSTR + ")";
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
				tempOrder = UploadManager.getUploadOrderFromRS(rs);
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
		//SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String sql = "select * from uploadorder where uploadtime >= '" + fmt.format(startDate) + "' and uploadtime <= '" + fmt.format(endDate) + "' order by name,shop";
		SimpleDateFormat fmt=new SimpleDateFormat("yyyyMMdd");
		String sql = "select * from uploadorder where saletime >= '" + fmt.format(startDate) + "' and saletime <= '" + fmt.format(endDate) + "' order by name,shop";
	
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
					}		
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

}
