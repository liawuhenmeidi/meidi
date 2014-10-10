package wilson.upload;

public class UploadOrder {
	
	private int id = -1; //id
	
	private String shop = ""; //销售门店
	
	private String posNo = ""; //pos(厂送)单号
	
	private String saleManName = "";//销售员姓名
	
	private String saleTime = ""; //销售日期

	private String dealTime = ""; //交货日期

	private String type = ""; //票面型号

	private int num = 0; //票面数量
	
	private Double salePrice = 0.0; //供价
	
	private Double backPoint = 0.0; //扣点
	
	private String fileName = ""; //来自的文件名
	
	private int checked = 1; //1为没有 check 0 为已经check 默认为1
	
	private String checkedTime = ""; //check的时间，默认为null
	
	private int checkOrderId = 0; //check后对应的orderID

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public String getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(String checkedTime) {
		this.checkedTime = checkedTime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getBackPoint() {
		return backPoint;
	}

	public void setBackPoint(Double backPoint) {
		this.backPoint = backPoint;
	}

	@Override
	public String toString() {
		return "UploadOrder [shop=" + shop + ", posNo="
				+ posNo + ", saleTime=" + saleTime + ", dealTime=" + dealTime
				+ ", type=" + type + ", num=" + num + ", salePrice="
				+ salePrice + ", backPoint=" + backPoint + ", fileName="
				+ fileName + ", checked=" + checked + ", checkedTime="
				+ checkedTime + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public String getSaleManName() {
		return saleManName;
	}

	public void setSaleManName(String saleManName) {
		this.saleManName = saleManName;
	}

	public int getCheckOrderId() {
		return checkOrderId;
	}

	public void setCheckOrderId(int checkOrderId) {
		this.checkOrderId = checkOrderId;
	}

	
}
