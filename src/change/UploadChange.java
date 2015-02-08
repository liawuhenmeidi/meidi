package change;

public class UploadChange {
     private  int id ;
     private String filename;
     private String name ;
     private int statues;   // 0 表示上传需转化   1 表示转化结果

	public int getId() { 
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatues() {
		return statues;
	}
	public void setStatues(int statues) {
		this.statues = statues;
	}
     
     
     
}
