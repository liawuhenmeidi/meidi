package wilson.upload;

public class UploadSalaryModel {
	
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
	private int status = 0 ; //状态，留个备用，默认为0
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
	
	public boolean checkContent(){
		boolean result = false;
		try{
			
			if(this.getContent().startsWith("{") && this.getContent().endsWith("}")){
				String[] items = this.getContent().replace("\"","").replace("{","").replace("}","").split(",");
				double tempInt = 0 ;
				for(int i = 0 ; i < items.length ; i ++){
					if(tempInt == Double.parseDouble(items[i].split(":")[0].split("-")[0])){
						tempInt = Double.parseDouble(items[i].split(":")[1]);
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
}
