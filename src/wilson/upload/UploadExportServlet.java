package wilson.upload;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.StringUtil;

import utill.StringUtill;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class UploadExportServlet extends HttpServlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6463564079706615120L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String name = "";
		String type = "uploadorder";
		
		name = request.getParameter("name");
		type = request.getParameter("type");
		if(StringUtill.isNull(type) || StringUtill.isNull(name)){
			return ;
		}
		name = URLDecoder.decode(name,"utf-8");
		
		try{  
		
		
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String printName = df1.format(new Date());

        //  打开文件 
        WritableWorkbook book  =  Workbook.createWorkbook(response.getOutputStream());
        //  生成名为“第一页”的工作表，参数0表示这是第一页 
        WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
        //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
        //  以及单元格内容为test 
        
        if(type.equals("uploadorder")){
        	exportUploadOrder(sheet,name);
        }else if(type.equals("salarymodel")){
        	exportSalaryModel(sheet,name);
        }else{
        	return;
        }
        
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

	private void exportUploadOrder(WritableSheet sheet,String name) {
       try {
        	
    		Label label0  =   new  Label( 0 ,  0 ,  " 名称 " );
			sheet.addCell(label0);
			label0  =   new  Label( 1 ,  0 ,  name );
			sheet.addCell(label0);
			List<UploadOrder> list = UploadManager.getOrdersByName(name);
			UploadOrder temp = new UploadOrder();
			
			if(UploadManager.isSalesOrder(list)){
				//销售单
				//第二行
				label0  =   new  Label( 0 ,  1 ,  " 门店 " );
				sheet.addCell(label0);
				label0  =   new  Label( 1 ,  1 ,  " 销售时间 " );
				sheet.addCell(label0);
				label0  =   new  Label( 2 ,  1 ,  " 型号 " );
				sheet.addCell(label0);
				label0  =   new  Label( 3 ,  1 ,  " 数量 " );
				sheet.addCell(label0);
				label0  =   new  Label( 4 ,  1 ,  " 零售价 " ); 
				sheet.addCell(label0);
				label0  =   new  Label( 5 ,  1 ,  " 扣点  " );
				sheet.addCell(label0);
				
				//其他行
				for(int i = 0 ; i < list.size() ; i ++){
					temp = list.get(i);
					label0  =   new  Label( 0 ,  i + 2 ,  temp.getShop() );
					sheet.addCell(label0);
					label0  =   new  Label( 1 ,  i + 2 ,  temp.getSaleTime() );
					sheet.addCell(label0);
					label0  =   new  Label( 2 ,  i + 2 ,  temp.getType() );
					sheet.addCell(label0);
					label0  =   new  Label( 3 ,  i + 2 ,  String.valueOf(temp.getNum()) );
					sheet.addCell(label0);
					label0  =   new  Label( 4 ,  i + 2 ,  String.valueOf(temp.getSalePrice()) ); 
					sheet.addCell(label0);
					label0  =   new  Label( 5 ,  i + 2 ,   String.valueOf(temp.getBackPoint()) );
					sheet.addCell(label0);

				}
			}else{
				//系统对比单据
				//第二行
				
				label0  =   new  Label( 0 ,  1 ,  " 门店名称 " );
				sheet.addCell(label0);
				label0  =   new  Label( 1 ,  1 ,  " POS订单号 " );
				sheet.addCell(label0);
				label0  =   new  Label( 2 ,  1 ,  " 销售日期 " );
				sheet.addCell(label0);
				label0  =   new  Label( 3 ,  1 ,  " 票面型号 " );
				sheet.addCell(label0);
				label0  =   new  Label( 4 ,  1 ,  " 票面数量 " ); 
				sheet.addCell(label0);
				label0  =   new  Label( 5 ,  1 ,  " 供价 " );
				sheet.addCell(label0);
				label0  =   new  Label( 6 ,  1 ,  " 扣点 " );
				sheet.addCell(label0);
				
				//其他行
				for(int i = 0 ; i < list.size() ; i ++){
					temp = list.get(i);
					label0  =   new  Label( 0 ,  i + 2 ,  temp.getShop() );
					sheet.addCell(label0);
					label0  =   new  Label( 1 ,  i + 2 ,  temp.getPosNo());
					sheet.addCell(label0);
					label0  =   new  Label( 2 ,  i + 2 ,  temp.getSaleTime() );
					sheet.addCell(label0);
					label0  =   new  Label( 3 ,  i + 2 ,  temp.getType() );
					sheet.addCell(label0);
					label0  =   new  Label( 4 ,  i + 2 ,  String.valueOf(temp.getNum()) ); 
					sheet.addCell(label0);
					label0  =   new  Label( 5 ,  i + 2 ,  String.valueOf(temp.getSalePrice()) );
					sheet.addCell(label0);
					label0  =   new  Label( 6 ,  i + 2 ,  String.valueOf(temp.getBackPoint()) );
					sheet.addCell(label0);
				}
			}
			
			
			
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
       
	}

	private void exportSalaryModel(WritableSheet sheet,String name) {
        try {
        	List<UploadSalaryModel> list = UploadManager.getSalaryModelsByName(name);
        	
        	//第一行
        	Label label0  =   new  Label( 0 ,  0 ,  " 名称 " );
			sheet.addCell(label0);
			label0  =   new  Label( 1 ,  0 ,  name );
			sheet.addCell(label0);
			
			//第二行
			label0  =   new  Label( 0 ,  1 ,  " 品类 " );
			sheet.addCell(label0);
			label0  =   new  Label( 1 ,  1 ,  " 型号 " );
			sheet.addCell(label0);
			label0  =   new  Label( 2 ,  1 ,  " 零售价 " );
			sheet.addCell(label0);
			label0  =   new  Label( 3 ,  1 ,  " 提成 " );
			sheet.addCell(label0);
			label0  =   new  Label( 4 ,  1 ,  " 零售价 " );
			sheet.addCell(label0);
			label0  =   new  Label( 5 ,  1 ,  " 提成 " );
			sheet.addCell(label0);
			
			//其他行
        	for(int i = 0 ; i < list.size() ; i ++){
        		label0  =   new  Label( 0 ,  i+2 ,  list.get(i).getCatergory() );
    			sheet.addCell(label0);
        		label0  =   new  Label( 1 ,  i+2 ,  list.get(i).getType() );
    			sheet.addCell(label0);
        		for(int j = 0 ; j < list.get(i).getExportContent().split(",").length ; j ++){
        			label0  =   new  Label( j+2 ,  i + 2 ,  list.get(i).getExportContent().split(",")[j] );
        			sheet.addCell(label0);
        		}
        	}
        	
			
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
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
