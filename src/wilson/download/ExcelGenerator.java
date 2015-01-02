package wilson.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import wilson.salaryCalc.SalaryResult;

public class ExcelGenerator {
	public static boolean generateExcel(String path,String fileName,List<SalaryResult> lists){
		boolean result = false;
		OutputStream out = null;
		try{  
			File outputFile = new File(path+File.separator+fileName);
			out = new FileOutputStream(outputFile);
	        //  打开文件 
	        WritableWorkbook book  =  Workbook.createWorkbook(out);
	        //  生成名为“第一页”的工作表，参数0表示这是第一页 
	        WritableSheet sheet  =  book.createSheet( " 第一页 " ,  0 );
	        //  在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
	        //  以及单元格内容为test 
	        NumberFormat nf = new NumberFormat("#.##");
	        WritableCellFormat numberFormat = new WritableCellFormat(nf);
	        
	        //设置颜色
	        Colour red = Colour.RED;
	        WritableCellFormat wcf = new WritableCellFormat();
	        wcf.setBackground(red);
	        
	        Label label0  =   new  Label( 0 ,  0 ,  " 名称 " );
	        Label label1  =   new  Label( 1 ,  0 ,  " 门店 " );
	        Label label2  =   new  Label( 2 ,  0 ,  " POS单号 ");
	        Label label3  =   new  Label( 3 ,  0 ,  " 导购员姓名 " );
	        Label label4  =   new  Label( 4 ,  0 ,  " 销售日期 " );
	        Label label5  =   new  Label( 5 ,  0 ,  " 品类  " );
	        Label label6  =   new  Label( 6 ,  0 ,  " 销售型号  " );
	        Label label7  =   new  Label( 7 ,  0 ,  " 数量  " );
	        Label label8  =   new  Label( 8 ,  0 ,  " 单价 " );
	        Label label9  =   new  Label( 9 ,  0 ,  " 提成 " );

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
	        sheet.addCell(label9);
	        
	        jxl.write.Number num1;
	        jxl.write.Number num2;
	        jxl.write.Number num3;
	        
	        for(int i = 0 ; i < lists.size() ; i ++ ){
	        	label0 = new Label(0,i+1,lists.get(i).getUploadOrder().getName());
	        	label1 = new Label(1,i+1,lists.get(i).getUploadOrder().getShop());
	        	label2 = new Label(2,i+1,lists.get(i).getUploadOrder().getPosNo());
	        	label3 = new Label(3,i+1,lists.get(i).getUploadOrder().getSaleManName());
	        	label4 = new Label(4,i+1,lists.get(i).getUploadOrder().getSaleTime());
	        	label5 = new Label(5,i+1,lists.get(i).getSalaryModel().getCatergory());
	        	label6 = new Label(6,i+1,lists.get(i).getUploadOrder().getType());
	        	num1 = new jxl.write.Number(7, i+1,lists.get(i).getUploadOrder().getNum()); 
	        	num2 = new jxl.write.Number(8, i+1,lists.get(i).getUploadOrder().getSalePrice()); 
	        	if(lists.get(i).isFinished()){
	        		num3 = new jxl.write.Number(9, i+1,lists.get(i).getSalary()); 
	        		sheet.addCell(num3);
	        	}else{
	        		label9 = new Label(9,i+1,lists.get(i).getPrintSalary());
	        		sheet.addCell(label9);
	        	}
	        	
	        	
	  	
	        	sheet.addCell(label0);
	            sheet.addCell(label1);
	            sheet.addCell(label2);
	            sheet.addCell(label3);
	            sheet.addCell(label4);	
	            sheet.addCell(label5);	
	            sheet.addCell(label6);	
	            sheet.addCell(num1);	
	            sheet.addCell(num2);	
	        }    
	        
			// 将文件存到指定位置

			book.write();
			book.close();
			result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}finally{
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return result;
	}
}
