package utill;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigUtill {   
	public static ConfigUtill instance;
	public static ConfigUtill getInstance(){
		if(instance == null){
			instance = new ConfigUtill();
		}
		return instance;
	}
    
    public Properties initProperties(){
    	Properties pro = new Properties(); 
    	URL fileURL=this.getClass().getResource("config.properties");
    	System.out.println(fileURL.getPath()); 
		InputStream fileIn;	
		try {
			fileIn = this.getClass().getResourceAsStream("config.properties");
				pro.load(fileIn);
				
		    }catch (FileNotFoundException e) {
				e.printStackTrace();
			
			} catch (IOException e1) {
				e1.printStackTrace();
				
			}
		return pro; 
    }
	
   
}
