package enums;

public class ProductModel {
    public static int Routine = 1;  // 常规
    public static int special = 2;  //   
  
      
    public enum Model{
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    	标准采购订单(Routine), 特价机采购订单(special);
          
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
