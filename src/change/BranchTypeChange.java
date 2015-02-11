package change;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 
public class BranchTypeChange {
	private static BranchTypeChange instance;
    public  String name ; 
    private  Map<String,String> map = null; 
    private  Map<Integer,Change> mapO = null;
    public  Map<String,Set<String>> maplist = new HashMap<String,Set<String>>();
       
    public Map<Integer, Change> getMapO() {
		return mapO;
	}

	public void setMapO(Map<Integer, Change> mapO) {
		this.mapO = mapO;
	}

	public  static BranchTypeChange getinstance(){
		if(null == instance){
			instance = new BranchTypeChange();
		} 
		return instance;
	}
    
    public  void setMap(Map<String, String> map) {
		this.map = map;
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
	
	public Map<String, Set<String>> getMaplist() {
		
		return maplist;
	}
	public void setMaplist(Map<String, Set<String>> maplist) {
		this.maplist = maplist;  
	}
     
	public void init(){  
		this.setMap(ChangeManager.getmap()); 
		this.setMapO(ChangeManager.getmapO());
		 
	}
    
    
}
