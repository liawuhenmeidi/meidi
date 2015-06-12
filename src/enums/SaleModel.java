package enums;

public class SaleModel {
    public static int SuNing = 1;  // 苏宁系统
    public static int GuoMei = 2;  // 国美系统  
    public static int Other = 3;  // 其他系统  
    
    public static  String get(int statues){
    	String str = ""; 
    	if(SuNing == statues){
    		str = "SuNing"; 
    	}else if(GuoMei  == statues) {
    		str = "GuoMei";
    	}  
    	return str ;
    	
    }
    public enum Model{ 
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    	苏宁(SuNing), 国美(GuoMei),其他(3); 
          
        private final int value;  

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Model(int value) {
            this.value = value;
        } 
        
        public int getValue() {
            return value;
        }
    }
      
     
    public static void main(String args[]){
    	System.out.println(Model.values());  
    	
    }
    
    
}
