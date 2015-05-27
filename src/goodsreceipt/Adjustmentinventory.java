package goodsreceipt;

public class Adjustmentinventory {
	private int branchnum;
	private int goodnum;
	private String time;  
	private int type; // 标记类型
	private int count; // 标记数量

	public int getBranchnum() {
		return branchnum;
	}

	public void setBranchnum(int branchnum) {
		this.branchnum = branchnum;
	}

	public int getGoodnum() {
		return goodnum;
	}

	public void setGoodnum(int goodnum) {
		this.goodnum = goodnum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
