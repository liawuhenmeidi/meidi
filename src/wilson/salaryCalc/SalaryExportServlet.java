package wilson.salaryCalc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.UserManager;
import wilson.matchOrder.AfterMatchOrder;
import wilson.upload.UploadManager;
import wilson.upload.UploadSalaryModel;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class SalaryExportServlet extends HttpServlet {
	private static final long serialVersionUID = -3093078939187798780L;
	private List<AfterMatchOrder> unMatchOrders =  new ArrayList<AfterMatchOrder>();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{  
		//inout -> 2014-10-16|2014-10-25
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		String startDateSTR = request.getParameter("startDate");
		String endDateSTR = request.getParameter("endDate");
		String name = request.getParameter("name");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
        Date endDate = new Date();
        String printName = df1.format(new Date());
        List<SalaryResult> lists = new ArrayList<SalaryResult>();
        
        
		if(type != null && !type.equals("")){
			if(type.equals("bydate")){
				
				
				startDate = df1.parse(startDateSTR);
		        endDate = df1.parse(endDateSTR);
		        lists = SalaryCalcManager.getSalaryResultByDate(startDate,endDate);
		        
			}else if(type.equals("byname")){
				
				if(name != null && !name.equals("")){
					if(!name.equals("all")){
						lists = SalaryCalcManager.getSalaryResultByName(name);
					}else{
						lists = (List<SalaryResult>)request.getSession().getAttribute("allOrders");
					}
					
				}else{
					return;
				}
				
			}
		}else{
			return;
		}
        
        
		//排序
        lists = sortExcelPrintResultModel(lists);
        
        //  打开文件 
        WritableWorkbook book  =  Workbook.createWorkbook(response.getOutputStream());
        //  生成名为“第一页”的工作表，参数0表示这是第一页 
        WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
        //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
        //  以及单元格内容为test 
        
        
        //设置颜色
        Colour red = Colour.RED;
        WritableCellFormat wcf = new WritableCellFormat();
        wcf.setBackground(red);
        
        Label label0  =   new  Label( 0 ,  0 ,  " 名称 " );
        Label label1  =   new  Label( 1 ,  0 ,  " 门店 " );
        Label label2  =   new  Label( 2 ,  0 ,  " POS单号 ");
        Label label3  =   new  Label( 3 ,  0 ,  " 导购员姓名 " );
        Label label4  =   new  Label( 4 ,  0 ,  " 销售日期 " );
        Label label5  =   new  Label( 5 ,  0 ,  " 销售型号  " );
        Label label6  =   new  Label( 6 ,  0 ,  " 数量  " );
        Label label7  =   new  Label( 7 ,  0 ,  " 单价 " );
        Label label8  =   new  Label( 8 ,  0 ,  " 提成 " );

        //  将定义好的单元格添加到工作表中 
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        sheet.addCell(label5);
        sheet.addCell(label6);
        sheet.addCell(label7);
        sheet.addCell(label8);
        
        
        Double tempSum = 0.0;
        //由于输出总计导致的空行数
        int tempSumBlank = 0 ;
        
        
        for(int i = 0 ; i < lists.size() ; i ++ ){
        	label0 = new Label(0,i+1+tempSumBlank,lists.get(i).getUploadOrder().getName());
        	label1 = new Label(1,i+1+tempSumBlank,lists.get(i).getUploadOrder().getShop());
        	label2 = new Label(2,i+1+tempSumBlank,lists.get(i).getUploadOrder().getPosNo());
        	label3 = new Label(3,i+1+tempSumBlank,lists.get(i).getUploadOrder().getSaleManName());
        	label4 = new Label(4,i+1+tempSumBlank,lists.get(i).getUploadOrder().getSaleTime());
        	label5 = new Label(5,i+1+tempSumBlank,lists.get(i).getUploadOrder().getType());
        	label6 = new Label(6,i+1+tempSumBlank,String.valueOf(lists.get(i).getUploadOrder().getNum()));
        	label7 = new Label(7,i+1+tempSumBlank,String.valueOf(lists.get(i).getUploadOrder().getSalePrice()));
        	label8 = new Label(8,i+1+tempSumBlank,String.valueOf(lists.get(i).getSalary()));
        	if(lists.get(i).getSalary() == null){
        		label8 = new Label(8,i+1+tempSumBlank,"");
        	}else{
        		tempSum += lists.get(i).getSalary();
        	}
        	
        	
        	
        	sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label4);	
            sheet.addCell(label5);	
            sheet.addCell(label6);	
            sheet.addCell(label7);	
            sheet.addCell(label8);	
            
            if((i+1) < lists.size() && !lists.get(i+1).getUploadOrder().getShop().equals(lists.get(i).getUploadOrder().getShop())){
            	tempSumBlank ++ ;
            	label0 = new Label(9,i+1+tempSumBlank,"总计");
        		label1 = new Label(10,i+1+tempSumBlank,String.valueOf(tempSum));
        		sheet.addCell(label0);
                sheet.addCell(label1);
                tempSum = 0.0;
        	}else if((i+1) == lists.size()){
        		tempSumBlank ++ ;
        		label0 = new Label(9,i+1+tempSumBlank,"总计");
        		label1 = new Label(10,i+1+tempSumBlank,String.valueOf(tempSum));
        		sheet.addCell(label0);
                sheet.addCell(label1);
                tempSum = 0.0;
        	}
        }    
        
      //以下单据没有解析成功
//        int rowNum = lists.size() + 1;
//        for(int i = 0 ;  i < unMatchOrders.size() ; i ++){
//        	label0 = new Label(0,rowNum + i,unMatchOrders.get(i).getUploadSideShop());
//        	label1 = new Label(1,rowNum + i,UserManager.getUsernameByOrderid(unMatchOrders.get(i).getDBSideOrderId()));
//        	label2 = new Label(2,rowNum + i,unMatchOrders.get(i).getUploadSideSaleTime());
//        	label3 = new Label(3,rowNum + i,String.valueOf(unMatchOrders.get(i).getUploadOrder().getSalePrice()));
//        	label4 = new Label(4,rowNum + i,"");
//        	
//        	sheet.addCell(label0);
//            sheet.addCell(label1);
//            sheet.addCell(label2);
//            sheet.addCell(label3);
//            sheet.addCell(label4);
//        }
        
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

	private List<SalaryResult> sortExcelPrintResultModel(
			List<SalaryResult> lists) {
		//目测貌似可以用递归搞一下？算了。。。免得以后看不懂
		
		String tempName = "";
		String tempShop = "";
		List<SalaryResult> sameShopList = new ArrayList<SalaryResult>();
		List<SalaryResult> result = new ArrayList<SalaryResult>();
		
		while(lists.size() != 0){
			tempShop = lists.get(0).getUploadOrder().getShop();
			sameShopList.add(lists.get(0));
			lists.remove(0);
			for(int i = 1 ; i < lists.size() ; i++){
				if(lists.get(i).getUploadOrder().getShop().equals(tempShop)){
					sameShopList.add(lists.get(i));
					lists.remove(i);
				}
			}
			tempShop = "";
			
			while(sameShopList.size() != 0){
				tempName = sameShopList.get(0).getUploadOrder().getSaleManName();
				result.add(sameShopList.get(0));
				sameShopList.remove(0);
				for(int i = 1 ; i < sameShopList.size() ; i++){
					if(sameShopList.get(i).getUploadOrder().getSaleManName().equals(tempName)){
						result.add(sameShopList.get(i));
						sameShopList.remove(i);
					}
				}
				tempName = "";
			}
				
			
		
		}
		return result;
	}

	//根据时间段，取出对应的ExcelPrintResultModel模型
	private List<ExcelPrintResultModel> calcSalary(Date startDate, Date endDate) {
		//取到对应时间段的AfterMatchOrder
		List<AfterMatchOrder> orders =  UploadManager.getCheckedAfterMatchOrder(startDate, endDate);
		//返回结果集
        List<ExcelPrintResultModel> result = new ArrayList<ExcelPrintResultModel>();
        //取到对应时间段的SalaryModel
        List<UploadSalaryModel> salaryModels = UploadManager.getSalaryModelList(startDate, endDate);
        
        
        
        ExcelPrintResultModel tempResult = new ExcelPrintResultModel();
        AfterMatchOrder tempOrder = new AfterMatchOrder();
        UploadSalaryModel tempSalaryModel = new UploadSalaryModel();
        //型号相同而且日期也对应的list
        List<UploadSalaryModel> tempSalaryModelList = new ArrayList<UploadSalaryModel>();
        
        
        Date tempOrderSaleDate = new Date();
        Date tempStartDate = new Date();
        Date tempEndDate = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        
        for(int i = 0 ; i < orders.size() ; i ++ ){
        	tempOrder = orders.get(i);
        	try {
        	//取出型号相对应的SalaryModel，放到temp
	        	for(int j = 0 ; j < salaryModels.size() ; j ++){
	        		if(tempOrder.getUploadSideType().equals(salaryModels.get(j).getType())){
	        			tempOrderSaleDate = df1.parse(tempOrder.getUploadSideSaleTime());
	        			tempStartDate = df2.parse(salaryModels.get(j).getStartTime());
						tempEndDate = df2.parse(salaryModels.get(j).getEndTime());
						if(tempOrderSaleDate.after(tempStartDate) && tempOrderSaleDate.before(tempEndDate)){
							tempSalaryModelList.add(salaryModels.get(j));
						}
	        		}
	        	}
        	
	        	if(tempSalaryModelList.size()== 0){
	        		//没有匹配项!!
	        		unMatchOrders.add(tempOrder);
	        		continue;
	        	}else{
	        		//取对应的salaryModel
	        		//取最新的commit Time 项的salaryModel
	    			//..
	        		tempSalaryModel = tempSalaryModelList.get(0);
	        		for(int k = 0 ; k < tempSalaryModelList.size() ; k ++){
	        			if(df2.parse(tempSalaryModelList.get(k).getCommitTime()).after( df2.parse(tempSalaryModel.getCommitTime())) ){
	        				tempSalaryModel = tempSalaryModelList.get(k);
	        			}
	        			
	        		}
	        		
	        		//解析salaryModel 的content并计算
	        		//生成ExcelPrintResultModel模型
	        		tempResult.setName(UserManager.getUsernameByOrderid(tempOrder.getDBSideOrderId()));
	        		tempResult.setShop(tempOrder.getUploadSideShop());
	        		tempResult.setSaleTime(tempOrder.getUploadSideSaleTime());
	        		tempResult.setSalePrice(tempOrder.getUploadOrder().getSalePrice());
	        		tempResult.setSalary(getSalaryByModel(tempOrder.getUploadOrder().getSalePrice(),tempSalaryModel));
	        		
	        		if(tempResult.getSalary() == 0.0){
	        			unMatchOrders.add(tempOrder);
	    				continue;
	        		}
	        		
	        		result.add(tempResult);
	        		
	        		
	        		//清空temp变量
	                
	                tempResult = new ExcelPrintResultModel();
	                tempOrder = new AfterMatchOrder();
	                tempSalaryModel = new UploadSalaryModel();
	                tempSalaryModelList = new ArrayList<UploadSalaryModel>();
	                tempOrderSaleDate = new Date();
	                tempStartDate = new Date();
	                tempEndDate = new Date();
	              
	        	}
        	}catch (ParseException e) {
				e.printStackTrace();
				unMatchOrders.add(tempOrder);
				continue;
			}
        }
		return result;
	}

	private Double getSalaryByModel(Double uploadOrderSalePrice,
			UploadSalaryModel tempSalaryModel) {
		Double result = 0.0;
		try{
			
			String content = tempSalaryModel.getContent();
			content = content.replace("{", "").replace("}", "").replace("\"", "");
			String[] model = content.split(",");
			Double tempStart = 0.0;
			Double tempEnd = 0.0;
			for(int i = 0 ; i < model.length ; i ++){
				tempStart = Double.parseDouble(model[i].split(":")[0].split("-")[0]);
				tempEnd = Double.parseDouble(model[i].split(":")[0].split("-")[1]);
				if(uploadOrderSalePrice >= tempStart && uploadOrderSalePrice < tempEnd){
					result = Double.parseDouble(model[i].split(":")[1]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = 0.0;
		}
		
		return result;
	}
	
	public void doGetDepereted(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{  
		//inout -> 2014-10-16|2014-10-25
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String startDateSTR = request.getParameter("startDate");
		String endDateSTR = request.getParameter("endDate");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = df1.parse(startDateSTR);
        Date endDate = df1.parse(endDateSTR);
        String printName = df1.format(new Date());
        
        //  打开文件 
        WritableWorkbook book  =  Workbook.createWorkbook(response.getOutputStream());
        //  生成名为“第一页”的工作表，参数0表示这是第一页 
        WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
        //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
        //  以及单元格内容为test 
        
        
        
        
        
        Label label0  =   new  Label( 0 ,  0 ,  " 门店 " );
        Label label1  =   new  Label( 1 ,  0 ,  " 导购员姓名 " );
        Label label2  =   new  Label( 2 ,  0 ,  " 销售日期 " );
        Label label3  =   new  Label( 3 ,  0 ,  " 供价 " );
        Label label4  =   new  Label( 4 ,  0 ,  " 提成 " );

        //  将定义好的单元格添加到工作表中 
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
		
        List<ExcelPrintResultModel> lists = calcSalary(startDate,endDate);
        //lists = sortExcelPrintResultModel(lists);
        
        Double tempSum = 0.0;
        
        for(int i = 0 ; i < lists.size() ; i ++ ){
        	label0 = new Label(0,i+1,lists.get(i).getShop());
        	label1 = new Label(1,i+1,lists.get(i).getName());
        	label2 = new Label(2,i+1,lists.get(i).getSaleTime());
        	label3 = new Label(3,i+1,String.valueOf(lists.get(i).getSalePrice()));
        	label4 = new Label(4,i+1,String.valueOf(lists.get(i).getSalary()));
        	tempSum += lists.get(i).getSalary();
        	
        	sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label4);	
            
            if((i+1) < lists.size() && !lists.get(i+1).getName().equals(lists.get(i).getName())){
        		label0 = new Label(5,i+1,"总计");
        		label1 = new Label(6,i+1,String.valueOf(tempSum));
        		sheet.addCell(label0);
                sheet.addCell(label1);
                tempSum = 0.0;
        	}else if((i+1) == lists.size()){
        		label0 = new Label(5,i+1,"总计");
        		label1 = new Label(6,i+1,String.valueOf(tempSum));
        		sheet.addCell(label0);
                sheet.addCell(label1);
                tempSum = 0.0;
        	}
        }    
        
      //以下单据没有解析成功
        int rowNum = lists.size() + 1;
        for(int i = 0 ;  i < unMatchOrders.size() ; i ++){
        	label0 = new Label(0,rowNum + i,unMatchOrders.get(i).getUploadSideShop());
        	label1 = new Label(1,rowNum + i,UserManager.getUsernameByOrderid(unMatchOrders.get(i).getDBSideOrderId()));
        	label2 = new Label(2,rowNum + i,unMatchOrders.get(i).getUploadSideSaleTime());
        	label3 = new Label(3,rowNum + i,String.valueOf(unMatchOrders.get(i).getUploadOrder().getSalePrice()));
        	label4 = new Label(4,rowNum + i,"");
        	
        	sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label4);
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
		doGet(request,response);

		// 响应消息
	}
    
}
