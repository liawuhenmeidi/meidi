package wilson.upload;

public class UploadOrder {
	
	private String shop = ""; //销售门店
	
	private String saleNo = ""; //销售订单号
	
	private String posNo = ""; //pos(厂送)单号
	
	private String saleTime = ""; //销售日期

	private String dealTime = ""; //交货日期

	private String type = ""; //票面型号

	private String num = ""; //票面数量
	
	private String salePrice = ""; //供价
	
	private String backPoint = ""; //扣点
	
	private String fileName = ""; //来自的文件名

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

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getBackPoint() {
		return backPoint;
	}

	public void setBackPoint(String backPoint) {
		this.backPoint = backPoint;
	}

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "UploadOrder [shop=" + shop + ", saleNo=" + saleNo + ", posNo="
				+ posNo + ", saleTime=" + saleTime + ", dealTime=" + dealTime
				+ ", type=" + type + ", num=" + num + ", salePrice="
				+ salePrice + ", backPoint=" + backPoint + ", fileName="
				+ fileName + "]";
	}

	
}
