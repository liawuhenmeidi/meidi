package product;

public class Product {
  public static int sale = 0;
   public static int unsale = 1;
   
   private int id ;
   private String type ; // 型号
   private String name ;
   private int categoryID; // 类别
   private int statues ;  //  0  在销售   1  已无货
   
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getCategoryID() {
	return categoryID;
}
public void setCategoryID(int categoryID) {
	this.categoryID = categoryID;
}


   
   
}
