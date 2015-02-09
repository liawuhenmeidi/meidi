package change;

import java.util.HashSet;
import java.util.Set;

public class UploadChangeAll {
     private String filename; 
     private Set<String> branch = new HashSet<String>();
     private Set<String> types =  new HashSet<String>();
       
     private Set<UploadChange> branchO = new HashSet<UploadChange>();
     private Set<UploadChange> typesO =  new HashSet<UploadChange>(); 
      
     
	public Set<UploadChange> getBranchO() {
		return branchO;
	}
	public void setBranchO(Set<UploadChange> branchO) {
		this.branchO = branchO;
	}
	public Set<UploadChange> getTypesO() {
		return typesO;
	}
	public void setTypesO(Set<UploadChange> typesO) {
		this.typesO = typesO;
	}
	public String getFilename() {
		return filename; 
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Set<String> getBranch() {
		return branch;
	}
	public void setBranch(Set<String> branch) {
		this.branch = branch;
	}
	public Set<String> getTypes() {
		return types;
	}
	public void setTypes(Set<String> types) {
		this.types = types;
	}
    
}
