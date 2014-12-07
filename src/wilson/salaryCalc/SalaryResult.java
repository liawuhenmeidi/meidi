package wilson.salaryCalc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wilson.upload.UploadOrder;
import wilson.upload.UploadSalaryModel;
import branchtype.BranchType;
import branchtype.BranchTypeManager;

public class SalaryResult {
	private int id;
	private int uploadOrderId = 0;
	private int uploadSalaryModelid = 0;
	
	private UploadOrder uploadOrder = new UploadOrder();
	private UploadSalaryModel salaryModel = new UploadSalaryModel();
	private String calcTime = ""; //计算提成的时间
	private Double salary = 0.0 ; //提成数字
	private int status = 0 ; //预留状态位置
	
	public SalaryResult() {
		super();
	}
	public void setUploadOrderNum(int num){
		uploadOrder.setNum(num);
	}
	public void setUploadOrderSalePrice(double price){
		uploadOrder.setSalePrice(price);
	}
	public void setUploadOrderShop(String shop){
		uploadOrder.setShop(shop);
	}
	public void setUploadOrderPosNo(String posNo){
		uploadOrder.setPosNo(posNo);
	}
	public void setUploadSalaryModelCatergory(String catergory){
		salaryModel.setCatergory(catergory);
	}
	public SalaryResult(int uploadOrderId, int uploadSalaryModelid) {
		super();
		this.uploadOrderId = uploadOrderId;
		this.uploadSalaryModelid = uploadSalaryModelid;
	}
	public SalaryResult(UploadOrder uploadOrder, UploadSalaryModel salaryModel) {
		super();
		this.uploadOrder = uploadOrder;
		this.salaryModel = salaryModel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUploadOrderId() {
		return uploadOrderId;
	}
	public void setUploadOrderId(int uploadOrderId) {
		this.uploadOrderId = uploadOrderId;
	}
	public int getUploadSalaryModelid() {
		return uploadSalaryModelid;
	}
	public void setUploadSalaryModelid(int uploadSalaryModelid) {
		this.uploadSalaryModelid = uploadSalaryModelid;
	}
	public UploadOrder getUploadOrder() {
		return uploadOrder;
	}
	public void setUploadOrder(UploadOrder uploadOrder) {
		this.uploadOrder = uploadOrder;
	}
	public UploadSalaryModel getSalaryModel() {
		return salaryModel;
	}
	public void setSalaryModel(UploadSalaryModel salaryModel) {
		this.salaryModel = salaryModel;
	}
	public String getCalcTime() {
		return calcTime;
	}
	public void setCalcTime(String calcTime) {
		this.calcTime = calcTime;
	}
	public Double getSalary() {
		return Math.round(salary * 100)/100.0;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean calc(){
		if(this.uploadOrder == null || this.salaryModel == null){
			return false;
		}
		
		String content = this.salaryModel.getContent().replace("{","").replace("}", "");
		try{
			for(int i = 0 ; i < content.split(",").length ; i ++){
				Double startPrice = Double.parseDouble(content.split(",")[i].split(":")[0].replace("\"", "").split("-")[0]);
				//如果是199-/的形式
				if(content.split(",")[i].split(":")[0].replace("\"", "").split("-")[1].equals("/")){
					if(this.uploadOrder.getSalePrice() >= startPrice){
						//非百分比形式
						if(!content.split(",")[i].split(":")[1].replace("\"", "").contains("%")){
							this.setSalary(Double.parseDouble(content.split(",")[i].split(":")[1].replace("\"", "")));
						}
						//百分比形式
						else{
							this.setSalary(Double.parseDouble(content.split(",")[i].split(":")[1].replace("\"", "").replace("%", "")) * this.getUploadOrder().getSalePrice() * 0.01);
						}
						break;
					}else{
						return false;
					}
				}
				Double endPrice = Double.parseDouble(content.split(",")[i].split(":")[0].replace("\"", "").split("-")[1]);
				//如果是199-299形式
				if(this.uploadOrder.getSalePrice() >= startPrice && this.uploadOrder.getSalePrice() < endPrice){
					//非百分比形式
					if(!content.split(",")[i].split(":")[1].replace("\"", "").contains("%")){
						this.setSalary(Double.parseDouble(content.split(",")[i].split(":")[1].replace("\"", "")));
					}
					//百分比形式
					else{
						this.setSalary(Double.parseDouble(content.split(",")[i].split(":")[1].replace("\"", "").replace("%", "")) * this.getUploadOrder().getSalePrice() * 0.01);
					}
					
					break;
				}
			}
			this.setSalary(this.getSalary() * this.getUploadOrder().getNum());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.setCalcTime(sdf.format(new Date()));
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
}
