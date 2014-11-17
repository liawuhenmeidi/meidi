package order;

public class OrderStatues {
	public static int i= 1;
	private int id ;
	  
	  private int shipmentStatues;   //出货状态     0 出货  1 未出货
	  private int deliveryStatues;    //送货状态  0 表示未送货  1 表示正在送  2 送货成功
	  private int sendId;          // 送货员
	  private String installTime; //安装日期
	 
	  public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	  public String getInstallTime() {
			return installTime;
		}
		public void setInstallTime(String installTime) {
			this.installTime = installTime;
		}
	  
	  public int getShipmentStatues() {
			return shipmentStatues;
		}

	  public void setShipmentStatues(int shipmentStatues) {
			this.shipmentStatues = shipmentStatues;
		}
		public int getDeliveryStatues() {
			return deliveryStatues;
		}
		public void setDeliveryStatues(int deliveryStatues) {
			this.deliveryStatues = deliveryStatues;
		}
		public int getSendId() {
			return sendId;
		}
		public void setSendId(int sendId) {
			this.sendId = sendId;
		}
}
