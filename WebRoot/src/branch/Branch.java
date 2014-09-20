package branch;

public class Branch {
   private String locateName;
   private int id ;
   private int pid ;
   private String message ;   // 门店订单需要的号，是否需要pos号，等
    
public int getId() {
	return id;
}

public int getPid() {
	return pid;
}

public void setPid(int pid) {
	this.pid = pid;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public void setId(int id) {
	this.id = id;
}

public String getLocateName() {
	return locateName;
}

public void setLocateName(String locateName) {
	this.locateName = locateName;
}
}
