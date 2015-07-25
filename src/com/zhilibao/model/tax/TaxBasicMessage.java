package com.zhilibao.model.tax;

public class TaxBasicMessage {
	private String djh;        //单据号（20字节）
    private String fpzl;       //发票种类（专用发票或普通发票）   
    private String lbdm;       //  类别代码（10字节）
    private String fphm;       //发票号码（8字节）
    private String kprq;       //开票日期（YYYYMMDD）
	
	private String gfmc;       //购方名称（100字节）
	private String gfsh;       //购方税号          
	private String gfyhzh;     //购方银行账号（100字节）
	private String gfdzdh;     //购方地址电话（100字节）
	private String xfmc;            //销方名称（100字节）
	private String xfsh;              //销方税号
	private String xfyhzh;           //销方银行账号（100字节）
	private String xfdzdh;            //销方地址电话（100字节）
	private String hjje;             //合计金额
	private String hjse;               //合计税额
	private String bz;                   //备注（240字节）
	private String kpr;                  //开票人（8字节） 
    private String fhr;             //复核人（8字节）
    private String skr;             //收款人（8字节）
    
    public String getLbdm() {
		return lbdm;
	}
	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	
	public String getDjh() {
		return djh;
	}
	public void setDjh(String djh) {
		this.djh = djh;
	}
	public String getFpzl() {
		return fpzl;
	}
	public void setFpzl(String fpzl) {
		this.fpzl = fpzl;
	}
	
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
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
	public String getXfmc() {
		return xfmc;
	}
	public void setXfmc(String xfmc) {
		this.xfmc = xfmc;
	}
	public String getXfsh() {
		return xfsh;
	}
	public void setXfsh(String xfsh) {
		this.xfsh = xfsh;
	}
	public String getXfyhzh() {
		return xfyhzh;
	}
	public void setXfyhzh(String xfyhzh) {
		this.xfyhzh = xfyhzh;
	}
	public String getXfdzdh() {
		return xfdzdh;
	}
	public void setXfdzdh(String xfdzdh) {
		this.xfdzdh = xfdzdh;
	}
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje) {
		this.hjje = hjje;
	}
	public String getHjse() {
		return hjse;
	}
	public void setHjse(String hjse) {
		this.hjse = hjse;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
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
