package change;

import database.DB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import utill.DBUtill;
import utill.StringCompare;
import utill.StringUtill;
import wilson.matchOrder.MatchOrder;
import wilson.upload.SendType;
import wilson.upload.UploadOrder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class UploadChangeManager {
	protected static Log logger = LogFactory.getLog(UploadChangeManager.class);
	public static int count = 0;
    
	public static void save(String name ,String filename ,int statues){
		if(!StringUtill.isNull(name) && !StringUtill.isNull(filename)){
			String sql = " insert ignore into  mduploadchange (id,filename,name,statues) values (null,'"
					+ filename + "','" + name + "','"+statues+"');";
			DBUtill.sava(sql);
		} 
		
		
	}
	public static void save(UploadChangeAll bt) {
		List<String> sqls = new ArrayList<String>();
		Set<String> branchs = bt.getBranch();
		Set<String> types = bt.getTypes();
 
		Iterator<String> itt = types.iterator();
		Iterator<String> itb = branchs.iterator();

		while (itt.hasNext()) {
			String real = itt.next(); 
			String sql = " insert ignore into  mduploadchange (id,filename,name,statues) values (null,'"
					+ bt.getFilename() + "','" + real + "',1);";
			sqls.add(sql);

		}

		while (itb.hasNext()) {
			String real = itb.next();
			String sql = " insert ignore into  mduploadchange (id,filename,name,statues) values (null,'"
					+ bt.getFilename() + "','" + real + "',0);";
			sqls.add(sql);
		}

		DBUtill.sava(sqls);
	}

	public static Map<Integer, UploadChange> getmap() {
		List<UploadChange> list = getUnCheckedUploadOrders();
		Map<Integer, UploadChange> map = new HashMap<Integer, UploadChange>();
		for (int i = 0; i < list.size(); i++) {
			UploadChange mc = list.get(i);
			map.put(mc.getId(), mc);
		}

		return map;
	}

	public static UploadChangeAll getUnCheckedUploadOrders(String filename) {
		UploadChangeAll all = new UploadChangeAll();

		Connection conn = DB.getConn();
		String sql = "select * from mduploadchange where filename = '"
				+ filename + "'";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { // cname,uname,phone,locate
			while (rs.next()) {
				UploadChange g = getUploadChangeFromRs(rs);
				if (0 == g.getStatues()) {
					all.getBranchO().add(g);
				} else if (1 == g.getStatues()) {
					all.getTypesO().add(g);
				}
			}
			logger.info(all.getBranchO().size());
			logger.info(all.getTypesO().size());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return all;
	}
   
	public static UploadChangeAll getUnCheckedString(String filename) {
		UploadChangeAll all = new UploadChangeAll();
 
		Connection conn = DB.getConn();
		String sql = "select * from mduploadchange where filename = '"
				+ filename + "'";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { // cname,uname,phone,locate
			while (rs.next()) {
				UploadChange g = getUploadChangeFromRs(rs);
				if (0 == g.getStatues()) {
					all.getBranch().add(g.getName());
				} else if (1 == g.getStatues()) { 
					all.getTypes().add(g.getName());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return all;
	}
	
	public static List<UploadChange> getUnCheckedUploadOrders() {
		List<UploadChange> list = new ArrayList<UploadChange>();

		Connection conn = DB.getConn();
		String sql = "select * from mduploadchange ";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { // cname,uname,phone,locate
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
    
	public static Set<String> getsetbranch(List<UploadOrder> right,int type){
		Map<String,String> mapnew  = BranchTypeChange.getinstance().getMap();
		
		Set<String> rightb = new HashSet<String>();
		Set<String> rightt = new HashSet<String>();
		
		Iterator<UploadOrder> it = right.iterator();
		while (it.hasNext()) {
			UploadOrder up = it.next();
			
			if(null != mapnew){
		    	Set<String> set = mapnew.keySet();
		    	if(!set.contains(up.getShop())){
		    		rightb.add(up.getShop());
		    	}

				List<SendType> sendTypes = up.getSendType();

				for (int j = 0; j < sendTypes.size(); j++) {
					SendType sendType = sendTypes.get(j);
					if(!set.contains(sendType.getType())){
						rightt.add(sendType.getType());
					}
				}
		    }else {
		    	rightb.add(up.getShop());

				List<SendType> sendTypes = up.getSendType();

				for (int j = 0; j < sendTypes.size(); j++) {
					SendType sendType = sendTypes.get(j);
					rightt.add(sendType.getType());
				}
		    }
		}
		
		if(0  == type){
			return rightb;
		}else if(1 == type){
			return rightt ;
		} 
		return null;
	}
	
	public static Map<String, String> getmatch(UploadChangeAll left,
			Set<String> right, String statues, String type) {
		
		int ts = Integer.valueOf(type);
		
		logger.info(ts);
		Map<String, String> map = new HashMap<String, String>();

		Map<String, UploadChange> mapletfb = new HashMap<String, UploadChange>();
		Map<String, UploadChange> mapletft = new HashMap<String, UploadChange>();
		
		Set<UploadChange> leftb = left.getBranchO();
		Set<UploadChange> leftt = left.getTypesO();

		logger.info("leftb.size()" + leftb.size());
		logger.info("leftt.size()" + leftt.size());

        
		Iterator<UploadChange> itb = leftb.iterator();
		Iterator<UploadChange> itt = leftt.iterator();

		if (ts == 0) {

			while (itb.hasNext()) {
				UploadChange mc = itb.next();
				String realname = "";
				// logger.info(statues);
				if ("all".equals(statues)) {
					// logger.info(mc.getName());
					realname = mc.getName();
				} else if ("cn".equals(statues)) {
					realname =StringUtill.getStringNocn(mc.getName());
				} else if ("en".equals(statues)) { 
					realname =StringUtill.getNumbers(mc.getName());
				}
				
				if(!StringUtill.isNull(realname)){
					mapletfb.put(realname, mc);
				}
			}
            
			Iterator<String> itbr = right.iterator();
			while (itbr.hasNext()) {
				String realname = "";
				String br = itbr.next();
				if ("all".equals(statues)) {
					realname = br;
				} else if ("cn".equals(statues)) {
					realname = StringUtill.getStringNocn(br);
				} else if ("en".equals(statues)) {
					realname = StringUtill.getNumbers(br);
				}
				// logger.info(realname);
				UploadChange up = mapletfb.get(realname);
				if (null != up) {
					map.put(br, up.getName());
					itbr.remove();
				}
			}
            
			if ("all".equals(statues)){
				String name = "-1";
	
				Iterator<String> ittype = right.iterator();
				while (ittype.hasNext()) { 
					String rightbb = ittype.next();
					name = getSimilarPosFromList(leftb, rightbb , statues,ts);
					// logger.info(name);
					if (!StringUtill.isNull(name)) {
						String[] names = name.split(",");
						map.put(names[0], names[1]); 
					}
				}
			}
		} else if (ts == 1) {
			while (itt.hasNext()) {
				UploadChange mc = itt.next();
				String realtype = ""; 
				if ("all".equals(statues)) {
					realtype =mc.getName();
				} else if ("cn".equals(statues)) {
					realtype = StringUtill.getStringNocn(mc.getName());
				} else if ("en".equals(statues)) {
					realtype =StringUtill.getNumbers(mc.getName()); 
				} 
				
				//logger.info(realtype);  
				if(!StringUtill.isNull(realtype)){
					mapletft.put(realtype, mc); 
				} 
			} 
            
			logger.info(right.size());
			
			Iterator<String> ittr = right.iterator();
			logger.info("mapletft" + mapletft);
			// logger.info("list.size()"+list.size()+StringUtill.GetJson(list));

			while (ittr.hasNext()) {
				String realname = "";
				String br = ittr.next();
				if ("all".equals(statues)) {
					realname = br;
				} else if ("cn".equals(statues)) {
					realname = StringUtill.getStringNocn(br);
				} else if ("en".equals(statues)) {
					realname = StringUtill.getNumbers(br);
				}
				//logger.info(realname);
				UploadChange up = mapletft.get(realname);
				if (null != up) { 
					map.put(br, up.getName());
					ittr.remove();
				}
			}
			logger.info("map" + map);
			if ("all".equals(statues)){
				String name = "-1";
				Iterator<String> ittype = right.iterator();
				while (ittype.hasNext()) {
					String righttt = ittype.next(); 
					name = getSimilarPosFromList(leftt, righttt, statues,ts);
					//logger.info(name);
					if (!StringUtill.isNull(name)) { 
						String[] names = name.split(",");
						map.put(names[0], names[1]);
					} 
				}
			} 
		}
 
		logger.info("map.size()" + StringUtill.GetJson(map));

		logger.info("map" + map);
		// System.out.println(name);
		return map;

	}

	public static String getSimilarPosFromList(List<UploadChange> left,
			List<UploadChange> right) {
		String result = "";

		double distance = 0.0;
		String resultDBShopName = "";
		String resultUploadShopName = "";

		double similarityRatio = 0.0;
		// 找出相似度最高的店名组合
		for (int i = 0; i < left.size(); i++) {
			for (int j = 0; j < right.size(); j++) {
				similarityRatio = (double) StringCompare.getSimilarityRatio(
						left.get(i).getName(), right.get(j).getName());
				// logger.info(similarityRatio);
				// count ++;
				if (similarityRatio > distance) {
					if (similarityRatio > 0.8) {
						distance = similarityRatio;
						resultDBShopName = left.get(i).getName();
						resultUploadShopName = right.get(j).getName();
					}

					if (similarityRatio == 1) {
						break;
					}
				}
			}
		}
		// logger.info(count);
		// logger.info(resultDBShopName);
		// logger.info(resultUploadShopName);
		// 找出这个组合对应在DBList中的开始位置
		for (int i = 0; i < left.size(); i++) {

			if (left.get(i).getName().equals(resultDBShopName)) {

				result += left.get(i).getId() + ",";
				left.remove(i);
				break;
			}
		}

		// 没找到?
		if (!result.endsWith(",")) {
			return "";
		}

		if (distance < MatchOrder.SHOPNAME_DISTANCE) {
			return result + "-1";
		}

		// 找出这个组合对应在UploadList中的开始位置
		for (int i = 0; i < right.size(); i++) {
			if (right.get(i).getName().equals(resultUploadShopName)) {
				result += right.get(i).getId();
				right.remove(i);
				break;
			}
		}

		// 没找到?
		if (result.endsWith(",")) {
			return "";
		}
		// logger.info(result);
		return result;
	}

	public static String getSimilarPosFromList(Set<UploadChange> left,String right, String statues,int type) {
		String result = "";
		double distance = 0.0;
		double index = 1.0;
		if(1 == type){ 
			index = 0.6;
		}else { 
			index = 0.2;
		} 
		String realleft = "";
		String realright = "";
		// logger.info(statues);
		double similarityRatio = 0.0;

		Iterator<UploadChange> itl = left.iterator();
		// logger.info("开始对比");
		count++;
		String rightstr = right;
		String leftname = "";
		String rightname = "";
		
		while (itl.hasNext()) {
			String leftstr = itl.next().getName();
			if ("all".equals(statues)) {
				leftname = leftstr;
				rightname = rightstr;
			} else if ("cn".equals(statues)) {
				leftname = StringUtill.getStringNocn(leftstr);
				rightname = StringUtill.getStringNocn(rightstr);
			} else if ("en".equals(statues)) {
				leftname = StringUtill.getNumbers(leftstr);
				rightname = StringUtill.getNumbers(rightstr);
			} 
			// logger.info(leftname+"****"+rightname);
			similarityRatio = (double) StringCompare.getSimilarityRatio(
					leftname, rightname);
		 
			if (similarityRatio > distance) {
				if (similarityRatio >index) {
					distance = similarityRatio;
					realleft = leftstr;  
					realright = rightstr;
				} 
				if (similarityRatio == 1) {
					break;
				}
			}
		}  
		
		if(!StringUtill.isNull(realleft) && !StringUtill.isNull(realright)){
			result += realright+","+realleft;
		}
		// 找出相似度最高的店名组合

		// logger.info(count);
		// logger.info(resultDBShopName);
		// logger.info(resultUploadShopName);
		// 找出这个组合对应在DBList中的开始位置

		// 没找到?
		if (result.startsWith(",")) {
			return "";
		}

		if (distance < MatchOrder.SHOPNAME_DISTANCE) {
			return "";
		} 

		// 找出这个组合对应在UploadList中的开始位置
		// 没找到?
		if (result.endsWith(",")) {
			return "";
		}
		// logger.info(result);
		return result;
	}

	public static List<String> getAllUploadOrderNames(
			List<UploadChange> uploadorders) {
		List<String> result = new ArrayList<String>();
		if (uploadorders == null || uploadorders.size() == 0) {
			return result;
		}
		for (int i = 0; i < uploadorders.size(); i++) {
			if (uploadorders.get(i) == null) {
				continue;
			}
			if (!result.contains(uploadorders.get(i).getFilename())) {
				result.add(uploadorders.get(i).getFilename());
			}
		}
		return result;
	}
    
	public static void delete(String[] str){
		List<String> list = new ArrayList<String>();
		String sqlstr = "";
		if(str != null){
			for(int i =0 ;i<str.length;i++){
				sqlstr += "'"+str[i]+"',";
			}
			sqlstr = "("+sqlstr.substring(0,sqlstr.length()-1)+")";
		} 
		
		String sql = "delete from mduploadchange where name in " +sqlstr;
		String sql2 = "delete from mdchange where changes in " +sqlstr ;
		list.add(sql);
		list.add(sql2);
		DBUtill.sava(list); 
		 
		BranchTypeChange.getinstance().init();
		
	} 
	
	private static UploadChange getUploadChangeFromRs(ResultSet rs) {
		UploadChange g = new UploadChange();
		try {
			g.setFilename(rs.getString("filename"));
			g.setName(rs.getString("name"));
			g.setId(rs.getInt("id"));
			g.setStatues(rs.getInt("statues"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return g;
	}

}
