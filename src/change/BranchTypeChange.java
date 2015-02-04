package change;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchTypeChange {
	private static BranchTypeChange instance;
    public  String name ; 
    private  static Map<String,String> map = null;
    public  Map<String,List<String>> maplist = new HashMap<String,List<String>>();
     
    public  static BranchTypeChange getinstance(){
		if(null == instance){
			instance = new BranchTypeChange();
		} 
		return instance;
	}
    
    public static void setMap(Map<String, String> map) {
		BranchTypeChange.map = map;
	} 
	
	public String getName() { 
		return name; 
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getMap() {
		if(null == map){
			map = ChangeManager.getmap();
		} 
		return map;
	}
	
	public Map<String, List<String>> getMaplist() {
		
		return maplist;
	}
	public void setMaplist(Map<String, List<String>> maplist) {
		this.maplist = maplist;
	}
     
	
    
    
}
