package ordersgoods;

import product.Product;
import product.ProductService;
import utill.StringUtill;

public class OrderGoods {
	public static int common = 1;
	public static int special = 2;
	public static int prototype = 3;
	public static int change = 4;
	public static int gift = 5;
	public static int audit = 1;

	private int id; // 订单编号
	private String oid; // 订单号
	private int submitid; // 提交订单人
	private String submittime; // 提交订单时间
	private int mid;
	private int tid; // 产品id
	private int cid; // 产品类别id 
 
	private String tname; 
	private Product product;
  
	private int statues; // 1常规 2 特价 3 样机 4 换货 5 赠品 6 店外退货 7 已入库常规退货 8 已入库特价退货 9
							// 已入库样机退货     10 店外样机退货      11 常规只生成订单不发货   12 特价只生成订单不发货
	
	 
	private int realstatues; //   
	private int ordernum;     
	private int realnum; // 送货数量  
	private int opstatues; // 0 未生成订单 1 生成订单 2 已导出订单
	private int billingstatues; // 0 未发货 1 已发货  2 已修改实发数量   3 已入库 
	private String uuidtime;
	private String uuid;
	private String statuesName; 
	private String Branch;
	private String billingtime;
	private int realsendnum;
	private int Instoragenum;  //  卖场入库数量   
	private int returnrealsendnum;
	private String effectiveendtime;
	private String serialnumber;   
	private String exportuuid;    // 导出标识符  
	private int exportmodel;    // 导出类别   苏宁系统/国美系统
	
	private int exportstatues ; // 退货单、订货单等
	
	private String realsendnumName;
	private String returnrealsendnumName;
 
	public int getExportstatues() {
		return exportstatues;
	} 

	public void setExportstatues(int exportstatues) {
		this.exportstatues = exportstatues;
	}

	public int getExportmodel() {
		return exportmodel;
	}

	public void setExportmodel(int exportmodel) {
		this.exportmodel = exportmodel;
	}

	public int getInstoragenum() {
		return Instoragenum;
	} 

	public void setInstoragenum(int instoragenum) {
		Instoragenum = instoragenum;
	}

	public String getRealsendnumName() {
		if (2 == billingstatues || 3 == billingstatues) {
 
			realsendnumName = realsendnum + "";

		} else { 
			realsendnumName = "";
		}
		return realsendnumName;
	}

	public String getReturnrealsendnumName() {
		if (2 == billingstatues || 3 == billingstatues) {

			returnrealsendnumName = returnrealsendnum + "";

		} else {
			returnrealsendnumName = "";
		}
		return returnrealsendnumName;
	}

	public void setReturnrealsendnumName(String returnrealsendnumName) {
		this.returnrealsendnumName = returnrealsendnumName;
	}

	public int getRealsendnum() {
		return realsendnum;
	}

	public void setRealsendnum(int realsendnum) {
		this.realsendnum = realsendnum;
	}

	public String getOpstatuesName() {
		// System.out.println(opstatues);
		String str = "";
		if (1 == opstatues) {
			str = "未导出";
		} else if (2 == opstatues) {
			str = "已导出";
		}
		return str;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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
   
	public int getRealstatues() {
	    	Product p = getProduct();    
		  //System.out.println(statues);
		if (null != p && p.getSaleType() != -1) {
			if (1 == statues) {
				realstatues = p.getSaleType();
			} else if (7 == statues) {
				realstatues = p.getSaleType() + 6;
			} else if(11 == statues){
				realstatues = p.getSaleType() + 10;
			}else {   
				realstatues = statues;
			}
		} else {
			realstatues = 0;
		}

		return realstatues;
	}

	public void setRealstatues(int realstatues) {
		this.realstatues = realstatues;
	}

	public int getrealStatues() {  
        if(statues == 7 || statues == 8 ||statues == 9){
        	return  statues - 6 ;   
        }else if(statues == 6 || statues ==4){ 
        	return getProduct().getSaleType(); 
        }else if(statues == 10){
        	 return 3;
        }     
		return statues; 
	}
  
	public int getStatues() {
//System.out.println(statues);
		return statues;
	}

	
	public void setStatues(int statues) {
		this.statues = statues;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public int getRealnum() {
		return realnum;
	}

	public void setRealnum(int realnum) {
		this.realnum = realnum;
	}

	public int getOpstatues() {
		return opstatues;
	}

	public void setOpstatues(int opstatues) {
		this.opstatues = opstatues;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTname() { 
		if (tid != 0 || tid != -1) {
			tname = ProductService.getIDmap().get(tid).getType();
		}
		return tname; 
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
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
 
	public String getBranch() {
		if (1 == statues) {
			Branch = "0001";
		} else if (2 == statues) {
			Branch = "0002";
		} else if (3 == statues) {
			Branch = "样机";
		} else if (7 == statues) {
			Branch = "0001";
		} else if (8 == statues) {
			Branch = "0002";
		} else if (9 == statues) {
			Branch = "样机";
		} else if(11 == statues){
			Branch = "0001"; 
		}else if(12 == statues){
			Branch = "0002";
		}else{
			Branch = "";
		}
		return Branch;
	}

	public String getBranchGM() {
		if (1 == statues || 7 == statues) {
			Branch = "正常";
		} else if (2 == statues || 8 == statues) {
			Branch = "一步到位机";
		} else {
			Branch = "";
		}
		return Branch;
	}

	public String getStatuesName() {
		if (1 == statues) {
			statuesName = "常规机订货";
		} else if (2 == statues) {
			statuesName = "特价机订货";
		} else if (3 == statues) {
			statuesName = "样机订货";
		} else if (4 == statues) {
			statuesName = "换货订货";
		} else if (5 == statues) {
			statuesName = "赠品订货";
		} else if (6 == statues) {
			statuesName = "店外退货";
		} else if (7 == statues) {
			statuesName = "已入库常规退货";
		} else if (8 == statues) {
			statuesName = "已入库特价退货";
		} else if (9 == statues) {
			statuesName = "已入库样机退货";
		}else if (10 == statues) {
			statuesName = "店外样机退货";
		}else if (11 == statues) {
			statuesName = "常规只生成订单不发货";
		} else if (12 == statues) { 
			statuesName = "特价只生成订单不发货";
		}   
		return statuesName;
	}

	public static String getStatuesName(int statues) {
		String statuesName = "";
		if (1 == statues) {
			statuesName = "常规机订货";
		} else if (2 == statues) {
			statuesName = "特价机订货";
		} else if (3 == statues) {
			statuesName = "样机订货";
		} else if (4 == statues) {
			statuesName = "换货订货";
		} else if (5 == statues) {
			statuesName = "赠品订货";
		} else if (6 == statues) {
			statuesName = "店外退货";
		} else if (7 == statues) {
			statuesName = "已入库常规退货";
		} else if (8 == statues) {
			statuesName = "已入库特价退货";
		} else if (9 == statues) {
			statuesName = "已入库样机退货";
		}else if (10 == statues) { 
			statuesName = "店外样机退货";
		}else if (11 == statues) {
			statuesName = "常规只生成订单不发货";
		} else if (12 == statues) { 
			statuesName = "特价只生成订单不发货";
		} 
		return statuesName;
	}

	public void setStatuesName(String statuesName) {
		this.statuesName = statuesName;
	}

	public int getBillingstatues() {
		return billingstatues;
	}

	public void setBillingstatues(int billingstatues) {
		this.billingstatues = billingstatues;
	}

	public String getUuidtime() {
		return uuidtime;
	}

	public void setUuidtime(String uuidtime) {
		this.uuidtime = uuidtime;
	}

	public String getBillingtime() {
		if (StringUtill.isNull(billingtime)) {
			billingtime = "";
		}
		return billingtime;
	}

	public void setBillingtime(String billingtime) {
		this.billingtime = billingtime;
	}

	public String getEffectiveendtime() {
		return effectiveendtime;
	}

	public void setEffectiveendtime(String effectiveendtime) {
		this.effectiveendtime = effectiveendtime;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public int getReturnrealsendnum() {
		return returnrealsendnum;
	}

	public void setReturnrealsendnum(int returnrealsendnum) {
		this.returnrealsendnum = returnrealsendnum;
	}

	public String getExportuuid() {
		return exportuuid;
	}

	public void setExportuuid(String exportuuid) {
		this.exportuuid = exportuuid;
	}

}
