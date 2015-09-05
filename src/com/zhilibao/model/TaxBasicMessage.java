package com.zhilibao.model;

import java.io.Serializable;
  
public class TaxBasicMessage implements Serializable{
	private String djh;        //单据号（20字节）
  //  private String fpzl;       //发票种类（专用发票或普通发票）   
  //  private String lbdm;       //  类别代码（10字节）
  //  private String fphm;       //发票号码（8字节）
 //   private String kprq;       //开票日期（YYYYMMDD）
	  
	private String gfmc;       //购方名称（100字节）
	private String gfsh;       //购方税号          
	private String gfyhzh;     //购方银行账号（100字节）
	private String gfdzdh;     //购方地址电话（100字节）
//	private String xfmc;            //销方名称（100字节）
//	private String xfsh;              //销方税号
//	private String xfyhzh;           //销方银行账号（100字节）
//	private String xfdzdh;            //销方地址电话（100字节）
//	private String hjje;             //合计金额
//	private String hjse;               //合计税额
	private String bz;                   //备注（240字节）
//	private String kpr;                  //开票人（8字节） 
    private String fhr;             //复核人（8字节）
    private String skr;             //收款人（8字节）
    

	
	public String getDjh() {
		return djh;
	}
	public void setDjh(String djh) {
		this.djh = djh;
	}
	
	
	public String getGfmc() {
		return gfmc;
	}
	public void setGfmc(String gfmc) {
		this.gfmc = gfmc;
	}
	public String getGfsh() {
		return gfsh;
	}
	public void setGfsh(String gfsh) {
		this.gfsh = gfsh;
	}
	public String getGfyhzh() {
		return gfyhzh;
	}
	public void setGfyhzh(String gfyhzh) {
		this.gfyhzh = gfyhzh;
	}
	public String getGfdzdh() {
		return gfdzdh;
	}
	public void setGfdzdh(String gfdzdh) {
		this.gfdzdh = gfdzdh;
	}
	
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	public String getSkr() {
		return skr;
	}
	public void setSkr(String skr) {
		this.skr = skr;
	}
    
    
    
    
    
    

} 
