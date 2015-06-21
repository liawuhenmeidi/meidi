package makeInventory;

import product.Product;
import product.ProductService;
   
public class MakeInventory {
	public static int model = 1;
	public static int in = 2; 
	public static int out = 3;
     
	private int id; // 订单编号
	private int tid; // 产品id
	private int cid; // 产品类别id 
	private int bid ; // 门店编号 
	  
	private int submitid; // 提交盘点人
	private String submittime; // 提交盘点时间
	private String uuid ;
	  
	private Product product; 
    
	private int typestatues; // 1 样机 2 库内  3 库外 
 
	private String Branch;
	
	private int statues ;  //  0 未盘点   1 已盘点 
	 
	private int num ;  // 盘点数
	
	private String remark;
	  
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUuid() {
		return uuid;
	}
     
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
 
	public int getTypestatues() {
		return typestatues;
	}

	public void setTypestatues(int typestatues) {
		this.typestatues = typestatues;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public int getSubmitid() {
		return submitid;
	}

	public void setSubmitid(int submitid) {
		this.submitid = submitid;
	}

	public String getSubmittime() {
		return submittime;
	}

	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
  
  
	public int getStatues() {
//System.out.println(statues);
		return statues;
	}

	
	public void setStatues(int statues) {
		this.statues = statues;
	}

	
	

	public Product getProduct() {
		//System.out.println("tid"+tid);
		if (tid != 0 || tid != -1) {   
			//System.out.print("tid"+tid); 
			product = ProductService.getIDmap().get(tid);
		}
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
 
	
	public static String getStatuesName(int statues) {
		String statuesName = "";
		if (1 == statues) {
			statuesName = "常规机订货";
		} else if (2 == statues) {
			statuesName = "特价机订货";
		} else if (3 == statues) {
			statuesName = "样机订货";
		} 
		return statuesName;
	}

	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}


 
}
