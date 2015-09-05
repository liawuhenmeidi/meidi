package enums;
   
public class ProductSaleModel { 
    public static int Routine = 1;  // 常规
    public static int special = 2;  // 特价 
    public static int model = 3;  // 样机 
    public static int change = 4;  // 样机 
    public static int gift = 5;  // 样机   
    public static int outreturn = 6;  // 样机 
    public static int inRoutinereturn = 7;  // 样机 
    public static int inspecialreturn = 8;  // 样机 
    public static int inmodelreturn = 9;  // 样机 
    public static int inmodelreturnout = 10;  // 库位样机退货
    public static int RoutineOnlyOrder = 11;  // 常规只生成订单不发货
    public static int specialOnlyOrder = 12;  // 特价只生成订单不发货
     
    public enum Model{   
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错 
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
    	常规机订货(Routine), 特价机订货(special),样机订货(model),换货订货(change), 赠品(gift ),店外退货 (outreturn),已入库常规退货(inRoutinereturn), 已入库特价退货(inspecialreturn),
    	已入库样机退货 (inmodelreturn),库外样机退货 (inmodelreturnout), 
    	常规只生成订单不发货(RoutineOnlyOrder), 特价只生成订单不发货(specialOnlyOrder);
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
