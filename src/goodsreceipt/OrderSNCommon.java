package goodsreceipt;

import product.Product;
import product.ProductService;
import enums.SaleModel;
import utill.StringUtill;
import branch.Branch;
import branch.BranchService;

public class OrderSNCommon {
	private int id;
	private String goodsnum; // 商品代码
	private String goodsName; // 商品名称

	private String branchid; // 地点
	private String branchName; // 地点名称 || 地点(仓库)名称

	private int cid;
	private int tid;
	private int bid;

	private String uuid; // 唯一码

	private int disable; // 0 起作用 1 不起作用

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodsnum() {
		if (StringUtill.isNull(goodsnum)) {
			Branch b = BranchService.getNameMap().get(goodsName);
			if (null != b) {
				goodsnum = b.getEncoded();
			}
		}
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getBidSN() {
		// System.out.println("GoodsReceipt"+getBranchidSN());
		Branch b = BranchService.getNumMap(SaleModel.SuNing).get(
				getBranchidSN());
		if (null != b) {
			bid = b.getId();
		}
		return bid;
	}

	public int getBid() {
		Branch b = BranchService.getNumMap().get(getBranchid());
		if (null != b) {
			bid = b.getId();
		}
		return bid;
	}

	public int getBid(int saletype) {
		Branch b = BranchService.getNumMap(saletype).get(getBranchid());
		if (null != b) {
			bid = b.getId();
		}
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public String getBranchid() {
		if (StringUtill.isNull(branchid)) {
			Branch b = BranchService.getAnotherNameMap().get(branchName);
			if (null != b) {
				branchid = b.getEncoded();
			}
		}
		return branchid;
	}

	public String getBranchidSN() {
		if (StringUtill.isNull(branchid)) {
			Branch b = BranchService.getNameSNMap().get(branchName);
			if (null != b) {
				branchid = b.getEncoded();
			}
		}
		return branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getCid() {
		Product p = ProductService.gettypeNUmmap().get(goodsnum);
		if (null != p) {
			cid = p.getCategoryID();
		}
		return cid;
	}

	public void setCid(int cid) {

		this.cid = cid;
	}

	public int getTid() {
		Product p = ProductService.gettypeNUmmap().get(goodsnum);
		if (null != p) {
			tid = p.getId();
		}
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}
}
