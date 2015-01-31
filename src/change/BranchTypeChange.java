package change;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchTypeChange {
    public  String name ;
    public  static Map<String,String> map = new HashMap<String,String>();
    public  Map<String,List<String>> maplist = new HashMap<String,List<String>>();
	public String getName() { 
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getMap() {
		return map;
	}
	
	public Map<String, List<String>> getMaplist() {
		return maplist;
	}
	public void setMaplist(Map<String, List<String>> maplist) {
		this.maplist = maplist;
	}
     
	
    
    
}
