package servlet;

import gift.Gift;
import gift.GiftManager;
import group.Group;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.Order;
import order.OrderManager;
import order.OrderStatues;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;

import update.UpdateMessage;
import user.User;
import user.UserManager;
import utill.StringUtill;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import category.Category;
import category.CategoryManager;


/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */  
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(PrintServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */ 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
       // Date date1 = new Date();
		
		User user  = (User)request.getSession().getAttribute("user");
		request.setCharacterEncoding("utf-8");
        
		String filepath = request.getParameter("filepath");
		List<UpdateMessage> list = readXls(filepath);  
		
				// 第六步，将文件存到指定位置

	
    }
	/**
	 * 处理微信服务器发来的消息
	 */
	 
	
	private void readExcel(){
		
		
		
		
		
		
		
		
	}
	

	private List<UpdateMessage> readXls(String filepath) throws IOException {
        InputStream is = new FileInputStream(filepath); 
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        UpdateMessage UpdateMessage = null;
        List<UpdateMessage> list = new ArrayList<UpdateMessage>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                } 
                UpdateMessage = new UpdateMessage();
                // 循环列Cell
                // 0学号 1姓名 2学院 3课程名 4 成绩
                // for (int cellNum = 0; cellNum <=4; cellNum++) {
                for(int i=0;i<6;i++){
                	HSSFCell message = hssfRow.getCell(i++);
                    if (message == null) {
                        continue; 
                    }     
                    UpdateMessage.setBranch(getValue(message));  
                }
                list.add(UpdateMessage); 
            }
        }
        return list;
    }
	
	private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
  
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);

		// 响应消息
	}
    
}
