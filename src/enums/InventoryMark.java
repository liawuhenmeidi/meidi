package enums;

public class InventoryMark {
    public static int sale = 1;  // 常规
    public static int returngoods = 2;  //   
   
    public enum Model{
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    	销售库房未销账(sale), 退货库房未销账(returngoods); 
          
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
