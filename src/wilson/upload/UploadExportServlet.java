package wilson.upload;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class UploadExportServlet extends HttpServlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6463564079706615120L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String fileName = "";
		String type = "uploadorder";
		
		fileName = request.getParameter("name");
		type = request.getParameter("type");
		
		List content;
		if(type!= null){
			if(type.equals("uploadorder")){
				content = new ArrayList<UploadOrder>();
				content = UploadManager.getOrdersByName(fileName);
			}else if(type.equals("salarymodel")){
				content = new ArrayList<UploadSalaryModel>();
				content = UploadManager.getSalaryModelsByName(fileName);
			}
		}else{
			return;
		}
		
		
		
		
		try{  
		
		
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String printName = df1.format(new Date());

        //  打开文件 
        WritableWorkbook book  =  Workbook.createWorkbook(response.getOutputStream());
        //  生成名为“第一页”的工作表，参数0表示这是第一页 
        WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
        //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
        //  以及单元格内容为test 
        
       
        
        Label label0  =   new  Label( 3 ,  0 ,  " 本地记录订单 " );
       
        //  将定义好的单元格添加到工作表中 
        sheet.addCell(label0);
       
        
        
        
		// 将文件存到指定位置
		  
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=\""+ printName + ".xls\"");
		book.write();
		book.close();
		response.getOutputStream().close();
	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
    }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		doGet(request, response);
	}
    
}
