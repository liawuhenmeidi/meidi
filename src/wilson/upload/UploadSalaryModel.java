package wilson.upload;

import utill.StringUtill;

public class UploadSalaryModel {
	
	public static final int TEMP_OBJ = -2;
	private int id = 0 ;//id
	private String name = ""; //文件内部的命名
	private String endTime = "3014-01-01 00:00:00"; //生效时间
	private String startTime = "2014-01-01 00:00:00"; //截至时间
	private String catergory = ""; //类别
	private String type = ""; //型号
	private String content = ""; //内容，JSON格式吧
	
	private String commitTime = ""; //存储的时间
	private String fileName = ""; //存储的文件名
	private String shop = "";
	
	public static final int DELETE = -1;
	public static final int DEFAULT = 0;
	private int status = DEFAULT ; //状态，留个备用，默认为0
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "UploadSalaryModel [id=" + id + ", name=" + name
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", catergory=" + catergory + ", type=" + type + ", content="
				+ content + ", commitTime=" + commitTime + ", fileName="
				+ fileName + ", status=" + status + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	public String getCatergory() {
		return catergory;
	}
	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	
	/**
	 * @param 输入的是saveMode的content
	 * @return
	 */
	public boolean checkContent(){
		boolean result = false;
		try{
			
			if(this.getContent().startsWith("{") && this.getContent().endsWith("}")){
				String[] items = this.getContent().replace("\"","").replace("{","").replace("}","").split(",");
				double tempInt = 0 ;
				for(int i = 0 ; i < items.length ; i ++){
					if(tempInt == Double.parseDouble(items[i].split(":")[0].split("-")[0])){
						tempInt = Double.parseDouble(items[i].split(":")[1].replace("%", ""));
						if(items[i].split(":")[0].split("-")[1].equals("以上") || items[i].split(":")[0].split("-")[1].equals("/")){
							this.setContent(this.getContent().replace("以上", "/"));
						}else{
							tempInt = Double.parseDouble(items[i].split(":")[0].split("-")[1]);
						}	
					}else{
						throw new Exception();
					}
				}
				
				result = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param //{"0.0-1899.0":"60","1899.0-1999.0":"70","1999.0-/":"70"}
	 * @return // 0.0-1898.0:60,1899.0-1998.0:70,1999.0-/:70
	 * 将存储的格式转化为显示给用户的格式
	 */
	public String getPrintContent(){
		
		String result = "";
		String tempContent = this.content.replace("{", "").replace("}", "").replace("\"", "");
		for(int i = 0 ; i < tempContent.split(",").length ; i ++){
			result += "\"";
			result += tempContent.split(",")[i].split(":")[0].split("-")[0];
			result += "-";
			if(i == tempContent.split(",").length -1){
				result += tempContent.split(",")[i].split(":")[0].split("-")[1];
			}else{
				result += Double.parseDouble(tempContent.split(",")[i].split(":")[0].split("-")[1]) - 1;
			}
			result += "\"";
			result += ":";
			result += "\"";
			result += tempContent.split(",")[i].split(":")[1];
			result += "\"";
			result += ",";
		}
		if(result.endsWith(",")){
			result = result.substring(0,result.length()-1);
		}
				
		return result;
	}
	
	/**
	 * @return  //{"0.0-1899.0":"60","1899.0-1999.0":"70","1999.0-/":"70"}
	 * @param // 0.0-1898.0:60,1899.0-1998.0:70,1999.0-/:70
	 * 将显示给用户的格式转化为存储的格式
	 */
	public String getSaveContent(String inputString){
		
		String result = "";
		//回头再写吧...
		return result;
	}
	
	/**
	 * @param  //{"0.0-1001.0":"-1%","1001.0-1699.0":"3.50%","1699.0-/":"3.50%"}  {"0-/":"40"}
	 * @return // 1000,-1%,1698,3.50%       /,40
	 * 将显示给用户的格式转化为存储的格式
	 */
	public String getExportContent(){
		
		String result = "";
		String tempContent = this.content.replace("{", "").replace("}", "").replace("\"", "");
		
		if(StringUtill.isNull(tempContent)){
			return result;
		}
		
		if(tempContent.split(",").length == 1 && (tempContent.split(",")[0].split(":")[0].equals("0-/")||tempContent.split(",")[0].split(":")[0].equals("0.0-/"))){
			result += "/";
			result += ",";
			result += tempContent.split(",")[0].split(":")[1];
			return result;
		}
		
		for(int i = 0 ; i < tempContent.split(",").length ; i++){
			if(tempContent.split(",")[i].contains("-/")){
				break;
			}
			result += (Double.parseDouble(tempContent.split(",")[i].split(":")[0].split("-")[1]) - 1);
			result += ",";
			result += tempContent.split(",")[i].split(":")[1];
			result += ",";
		}
		
		if(result.endsWith(",")){
			result = result.substring(0,result.length() - 1);
		}
		return result;
	}
}
