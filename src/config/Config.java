package config;

public class Config {
	public static int addGift = 1;
	public static int addbarName = 2;
	public static int orderprince = 3 ;
	 
	public static int isok = 1;
	public static int isnotok = 0;
	
   private int id ;
   private int name ;
   private int statues ;
   
    
   
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}

public static int getAddGift() {
	return addGift;
}
public static void setAddGift(int addGift) {
	Config.addGift = addGift;
}
public int getName() {
	return name;
}
public void setName(int name) {
	this.name = name;
}
public int getStatues() {
	return statues;
}
public void setStatues(int statues) {
	this.statues = statues;
}
   
   
}
