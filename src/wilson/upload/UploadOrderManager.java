package wilson.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DB;

public class UploadOrderManager {
	public static boolean saveFileToDB(String path,String fileName){
		XLSReader xlsreader = new XLSReader();
		List <UploadOrder> UploadOrders = new ArrayList<UploadOrder>();
		UploadOrders = xlsreader.readXLS(path, fileName);
		if(saveOrderList(UploadOrders)){
			System.out.println("上传订单保存成功");
			return true;
		}else{
			System.out.println("上传订单保存失败");
			return false;
		}
		
	}
	
	public static boolean checkOrder(int orderId){
		boolean flag = false;
		String sql = "update uploadorder set checked = ? ,checkedtime = ? where id = " + orderId;
		Connection conn = DB.getConn();
		
		PreparedStatement pstmt = DB.prepare(conn, sql);
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			pstmt.setInt(1,0);
			pstmt.setString(2, fmt.format(new Date()));
			int count = pstmt.executeUpdate();
			if(count > 0){
				flag = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	
	public static boolean saveOrderList(List <UploadOrder> UploadOrders){
		String sql = ""; 
		sql = "insert into uploadorder (id, shop,saleno,posno,saletime,dealtime,type,num,saleprice,backpoint,filename,uploadtime,checked,checkedtime) VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,null)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		UploadOrder uo = new UploadOrder();
		
		try {
			for(int i = 0 ; i < UploadOrders.size() ; i ++ ){
				uo = UploadOrders.get(i);
				pstmt.setString(1, uo.getShop());
				pstmt.setString(2, uo.getSaleNo());
				pstmt.setString(3, uo.getPosNo());
				pstmt.setString(4, uo.getSaleTime());
				pstmt.setString(5, uo.getDealTime());
				pstmt.setString(6, uo.getType());
				pstmt.setInt(7, uo.getNum());
				pstmt.setDouble(8, uo.getSalePrice());
				pstmt.setDouble(9, uo.getBackPoint());
				pstmt.setString(10, uo.getFileName());
				pstmt.setString(11, fmt.format(new Date()));
				pstmt.setInt(12, uo.getChecked());
				pstmt.executeUpdate();
				uo = new UploadOrder();
			}		
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return true;
	}
	
	
	public static List<UploadOrder> getUnCheckedUploadOrders(){
		List <UploadOrder> unCheckedUploadOrders = new ArrayList<UploadOrder>();

		Connection conn = DB.getConn(); 
		String sql = "select * from uploadorder where checked = 1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		UploadOrder uo = new UploadOrder();
		try {     
			while (rs.next()) {
				uo.setId(rs.getInt("id"));
				uo.setShop(rs.getString("shop"));
				uo.setSaleNo(rs.getString("saleno"));
				uo.setPosNo(rs.getString("posno"));
				uo.setSaleTime(rs.getString("saletime"));
				uo.setDealTime(rs.getString("dealtime"));
				uo.setType(rs.getString("type"));
				uo.setNum(rs.getInt("num"));
				uo.setSalePrice(rs.getDouble("saleprice"));
				uo.setBackPoint(rs.getDouble("backpoint"));
				uo.setFileName(rs.getString("filename"));
				uo.setChecked(rs.getInt("checked"));
				uo.setCheckedTime(rs.getString("checkedtime"));
				unCheckedUploadOrders.add(uo);
				uo = new UploadOrder();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return unCheckedUploadOrders;
	}
}
