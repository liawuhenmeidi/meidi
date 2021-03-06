package product;

import utill.StringUtill;
import category.CategoryManager;
import category.CategoryService;

public class Product {
	public static int sale = 0;
	public static int unsale = 1;
 
	private int id;
	private String type; // 型号
	private String name;
	private int categoryID; // 类别
	private String cname;
	private int statues; // 0 在销售 1 已无货
	private double size;
	private int mataintime; // 单位是天
	private String matainids;
	private double stockprice;
	private String encoded; 
    private int saleType; // 1 常规   2  特价 
    private String saletypeName ;
    
	public int getSaleType() {
		return saleType;
	}

	public void setSaleType(int saleType) {
		this.saleType = saleType;
	}
    
	
	public String getSaletypeName() {
		if(saleType == 1){
			saletypeName = "常规";
		}else if(saleType == 2){
			saletypeName = "特价";
		}else { 
			saletypeName = "";
		}
		return saletypeName;
	}

	public void setSaletypeName(String saletypeName) {
		this.saletypeName = saletypeName;
	}

	public int getMataintime() {
		return mataintime;
	}

	public void setMataintime(int mataintime) {
		this.mataintime = mataintime;
	}

	public String getMatainids() {
		return matainids;
	}

	public void setMatainids(String matainids) {
		this.matainids = matainids;
	} 
 
	public String getCname() {
		if (this.getCategoryID() == 0) {
			cname = "";
		} else { 
			cname = CategoryService.getmap().get(this.getCategoryID())
					.getName();
		}
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public double getStockprice() {
		return stockprice;
	}

	public void setStockprice(double stockprice) {
		this.stockprice = stockprice;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getEncoded() {
		if(StringUtill.isNull(encoded)){
			encoded = "";
		}
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

}
