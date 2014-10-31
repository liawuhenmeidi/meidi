package wilson.matchOrder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import branch.Branch;
import branch.BranchManager;
import branch.BranchService;
import branchtype.BranchTypeManager;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import order.Order;
import order.OrderManager;
import user.UserManager;
import wilson.salaryCalc.ExcelPrintResultModel;
import wilson.salaryCalc.SalaryCalcManager;
import wilson.salaryCalc.SalaryResult;
import wilson.upload.UploadManager;
import wilson.upload.UploadOrder;
import wilson.upload.UploadSalaryModel;


public class MatchOrderExportServlet extends HttpServlet {
	private static final long serialVersionUID = -3093078939187798780L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		
		MatchOrder mo = new MatchOrder();
		List<AfterMatchOrder> afterMatchOrders = (List<AfterMatchOrder>)request.getSession().getAttribute("afterMatchOrders");
		List <Order> unCheckedDBOrders = (List <Order>)request.getSession().getAttribute("unCheckedDBOrders");
		List <UploadOrder> unCheckedUploadOrders = (List <UploadOrder>)request.getSession().getAttribute("unCheckedUploadOrders");
		
		String selectBranchType = request.getParameter("branchtype");
		String selectBranch = request.getParameter("branch");
		String selectOrderName = request.getParameter("uploadorder");
		
		
		
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
        Label label1  =   new  Label( 3 ,  1 ,  selectBranchType.equals("all")?"全部":BranchTypeManager.getNameById(Integer.parseInt(selectBranchType)));
        Label label2  =   new  Label( 4 ,  1 ,  selectBranch.equals("all")?"全部":BranchManager.getNameById(Integer.parseInt(selectBranch)));
              
        Label label3  =   new  Label( 9 ,  0 ,  " 上传的订单 " );
        Label label4  =   new  Label( 9 ,  1 ,  selectOrderName.equals("all")?"全部":selectOrderName );

        //  将定义好的单元格添加到工作表中 
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        
        label0  =   new  Label( 0 ,  2 ,  " 序号 " );
        sheet.addCell(label0);
        label0  =   new  Label( 1 ,  2 ,  " 销售门店 " );
        sheet.addCell(label0);
        label0  =   new  Label( 2 ,  2 ,  " pos(厂送)单号 " );
        sheet.addCell(label0);
        label0  =   new  Label( 3 ,  2 ,  " 销售日期 " );
        sheet.addCell(label0);
        label0  =   new  Label( 4 ,  2 ,  " 票面型号 " );
        sheet.addCell(label0);
        label0  =   new  Label( 5 ,  2 ,  " 票面数量 " );
        sheet.addCell(label0);
        label0  =   new  Label( 6 ,  2 ,  "  " );
        sheet.addCell(label0);
        label0  =   new  Label( 7 ,  2 ,  " 销售门店 " );
        sheet.addCell(label0);
        label0  =   new  Label( 8 ,  2 ,  " pos(厂送)单号 " );
        sheet.addCell(label0);
        label0  =   new  Label( 9 ,  2 ,  " 销售日期 " );
        sheet.addCell(label0);
        label0  =   new  Label( 10 ,  2 ,  " 票面型号 " );
        sheet.addCell(label0);
        label0  =   new  Label( 11 ,  2 ,  " 票面数量 " );
        sheet.addCell(label0);

        int row = 3 ;
        for(int i = 0 ; i < afterMatchOrders.size() ; i++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 5.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ; i < afterMatchOrders.size() ; i ++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 4.0 && afterMatchOrders.get(i).getCompareLevel()< 5.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ; i < afterMatchOrders.size() ; i ++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 3.0 && afterMatchOrders.get(i).getCompareLevel()< 4.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ; i < afterMatchOrders.size() ; i ++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 2.0 && afterMatchOrders.get(i).getCompareLevel()< 3.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ; i < afterMatchOrders.size() ; i ++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 1.0 && afterMatchOrders.get(i).getCompareLevel()< 2.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ; i < afterMatchOrders.size() ; i ++){
        	if(afterMatchOrders.get(i).getCompareLevel() >= 0.0 && afterMatchOrders.get(i).getCompareLevel()< 1.0){
        		//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  afterMatchOrders.get(i).getDBOrder().getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  afterMatchOrders.get(i).getDBOrder().getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  afterMatchOrders.get(i).getDBOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  afterMatchOrders.get(i).getDBOrder().getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(afterMatchOrders.get(i).getDBOrder().getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  afterMatchOrders.get(i).getUploadOrder().getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  afterMatchOrders.get(i).getUploadOrder().getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  afterMatchOrders.get(i).getUploadOrder().getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  afterMatchOrders.get(i).getUploadOrder().getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(afterMatchOrders.get(i).getUploadOrder().getNum()));
        		sheet.addCell(label0);
        		
        		row ++;
        	}
        }
        
        for(int i = 0 ;;){
        	//序号
    		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
    		sheet.addCell(label0);
    		
			if(unCheckedDBOrders != null && unCheckedDBOrders.size() > 0 && i< unCheckedDBOrders.size()){					
				//序号
        		label0  =   new  Label( 0 , row,  String.valueOf(row-2) );
        		sheet.addCell(label0);
        		//左侧
        		//销售门店
        		label0  =   new  Label( 1 ,  row,  unCheckedDBOrders.get(i).getbranchName(afterMatchOrders.get(i).getDBOrder().getBranch())  );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 2 ,  row,  unCheckedDBOrders.get(i).getPos() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 3 ,  row,  unCheckedDBOrders.get(i).getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 4 ,  row,  unCheckedDBOrders.get(i).getSendType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 5 ,  row,  String.valueOf(unCheckedDBOrders.get(i).getSendCount()) );
        		sheet.addCell(label0);
        		
        		label0  =   new  Label( 6 ,  2 ,  "  " );
                sheet.addCell(label0);
                
        		
        		
			}
			if(unCheckedUploadOrders != null && unCheckedUploadOrders.size() > 0 && i< unCheckedUploadOrders.size()){
				//右侧
        		//销售门店
        		label0  =   new  Label( 7 ,  row,  unCheckedUploadOrders.get(i).getShop() );
        		sheet.addCell(label0);
        		//pos(厂送)单号
        		label0  =   new  Label( 8 ,  row,  unCheckedUploadOrders.get(i).getPosNo() );
        		sheet.addCell(label0);
        		//销售日期
        		label0  =   new  Label( 9 ,  row,  unCheckedUploadOrders.get(i).getSaleTime() );
        		sheet.addCell(label0);
        		//票面型号
        		label0  =   new  Label( 10 ,  row,  unCheckedUploadOrders.get(i).getType() );
        		sheet.addCell(label0);
        		//票面数量
        		label0  =   new  Label( 11 ,  row ,  String.valueOf(unCheckedUploadOrders.get(i).getNum()));
        		sheet.addCell(label0);
        		
			}
			i ++ ;
    		row ++;
			if(i >= unCheckedDBOrders.size() && i >=unCheckedUploadOrders.size()){
				break;
			}
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		doGet(request, response);
	}
    
}
