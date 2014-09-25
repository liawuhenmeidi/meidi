package wilson.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DB;

public class UploadOrderManager {
	public boolean saveFileToDB(String path,String fileName){
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
	
	public boolean saveOrderList(List <UploadOrder> UploadOrders){
		String sql = ""; 
		sql = "insert into uploadorder (id, shop,saleno,posno,saletime,dealtime,type,num,saleprice,backpoint,filename,uploadtime) VALUES (null,?,?,?,?,?,?,?,?,?,?,?)";	
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
				pstmt.setInt(7, Integer.parseInt(uo.getNum()));
				pstmt.setDouble(8, Double.parseDouble(uo.getSalePrice()));
				pstmt.setDouble(9, Double.parseDouble(uo.getBackPoint()));
				pstmt.setString(10, uo.getFileName());
				pstmt.setString(11, fmt.format(new Date()));
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
}
