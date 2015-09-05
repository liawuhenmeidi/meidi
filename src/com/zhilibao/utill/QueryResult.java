package com.zhilibao.utill;

import java.util.List;

public class QueryResult<T> {
	public List<T> rows;//返回的结果
	public int total;//记录总数
	private int page;//当前页数
	private int totalPage;//总页数
	public List<T> getRows() {
		return rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
