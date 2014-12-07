package wilson.upload;

public class UploadOrder {
	
	private int id = -1; //id
	
	private String name = ""; 
	
	private String shop = ""; //销售门店
	
	private String posNo = null; //pos(厂送)单号
	
	private String saleManName = "";//销售员姓名
	
	private String saleTime = ""; //销售日期

	private String type = ""; //票面型号

	private int num = 0; //票面数量
	
	private Double salePrice = 0.0; //供价
	
	private String fileName = ""; //来自的文件名
	
	private int checked = 1; //1为没有 check 0 为已经check 2为已经与提成标准匹配好  默认为1
	
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

	public String getType() {
		return type;
	}
	
	private String typeForCompare = null;
	
	public String getTypeForCompareWithOutCharactar(){
		if(typeForCompare == null){
			typeForCompare = getType().replaceAll("([\u4E00-\u9FA5]+)|([\u4E00-\u9FA5])", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "");
		}
		return typeForCompare;
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
		return Math.round(salePrice * 100)/100.0;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public String toString() {
		return "UploadOrder [shop=" + shop + ", posNo="
				+ posNo + ", saleTime=" + saleTime + ", type=" + type + ", num=" + num + ", salePrice="
				+ salePrice  + ", fileName="
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clear() {
		this.id = -1; //id
		this.name = ""; 
		this.shop = ""; //销售门店
		this.posNo = ""; //pos(厂送)单号
		this.saleManName = "";//销售员姓名
		this.saleTime = ""; //销售日
		this.type = ""; //票面型
		this.num = 0; //票面数量	
		this.salePrice = 0.0; //供价	
		this.fileName = ""; //来自的文件名	
		this.checked = 1; //1为没有 check 0 为已经check 2为已经与提成标准匹配好  默认为1	
		this.checkedTime = ""; //check的时间，默认为null	
		this.checkOrderId = 0; //check后对应的orderID
	}

	
}
