package com.zhilibao.model;

import java.io.Serializable;
  
public class Tax implements Serializable{
	private String pnum; //  商品编号
    private String pname ; //商品全名
    private String unit;  // 单位
    private int num ; // 数量
    private double prince;  //单价 
    private double totalMoney;  // 金额
    private double taxRate ; // 税率
    
    
	public double getPrince() {
		return prince;
	}
	public void setPrince(double prince) {
		this.prince = prince;
	}
	public String getPnum() {
		return pnum;
	}
	public void setPnum(String pnum) {
		this.pnum = pnum;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
    
    
    
    
} 
