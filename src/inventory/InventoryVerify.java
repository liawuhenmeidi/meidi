package inventory;

import order.OrderManager;
import response.Response;

/**
 * Created by liaowuhen on 2017/7/27.
 */
public class InventoryVerify {
    public static final String COMPLETE = "complete";
    public static final String RETURN_ = "return_";
    /**
     * 订单id
     */
    private Integer id;
    // 上报状态
    private Integer oderStatus;
    private Integer dealSendid;
    private Integer sendId;
    private Integer installId;
    private Integer paperCount;
    private Integer realCount;
    private String inventoryString;
    private Integer type;
    private String ptype;
    private Integer deliveryStatues;
    private Integer count;
    // 派工门店
    private Integer branchid;
    // 订单门店
    private Integer orderbranch;

    public Integer getId() {
        return id;
    }

    public Integer getBranchid() {
        return branchid;
    }

    public void setBranchid(Integer branchid) {
        this.branchid = branchid;
    }

    public Integer getOrderbranch() {
        return orderbranch;
    }

    public void setOrderbranch(Integer orderbranch) {
        this.orderbranch = orderbranch;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(Integer paperCount) {
        this.paperCount = paperCount;
    }

    public Integer getRealCount() {
        return realCount;
    }

    public void setRealCount(Integer realCount) {
        this.realCount = realCount;
    }

    public String getInventoryString() {
        return inventoryString;
    }

    public void setInventoryString(String inventoryString) {
        this.inventoryString = inventoryString;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public Integer getDeliveryStatues() {
        return deliveryStatues;
    }

    public void setDeliveryStatues(Integer deliveryStatues) {
        this.deliveryStatues = deliveryStatues;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    // true 完成   false 退货
    public String isComplete(){
        if(1 == deliveryStatues || 2 == deliveryStatues || 8 == deliveryStatues  || 10 == deliveryStatues || 9 == deliveryStatues){
            return COMPLETE;
        }else if(3 == deliveryStatues || 4 == deliveryStatues || 5 == deliveryStatues || 11 == deliveryStatues || 12 == deliveryStatues || 13 == deliveryStatues){
            return RETURN_;
        }else{
            return deliveryStatues+"";
        }
    }

    public StringBuffer getBasicMsg(){
        StringBuffer sb = new StringBuffer();
        sb.append(getInventoryString());
        sb.append("-----id:");
        sb.append(getId());
        sb.append("-----上报状态:");
        sb.append(getOderStatus());
        sb.append("-----安装网点:");
        sb.append(getDealSendid());
        sb.append("-----送货人员:");
        sb.append(getSendId());
        sb.append("-----安装人员:");
        sb.append(getInstallId());
        sb.append("-----" + OrderManager.getDeliveryStatues(getDeliveryStatues(), null));
        sb.append("-----" + getType());
        sb.append("-----" + getPtype());
        sb.append("-----");
        return sb;
    }
    public Response check(){
        Response response = new Response();
        response.setSuccess(true);

        if(this.getInventoryString().startsWith("H")){
            return response;
        }
        // 已送货或者安装
        if(COMPLETE.equals(isComplete())){
            if(realCount == paperCount){
                if(!(realCount*-1 == count)){
                    response.setSuccess(false);
                    StringBuffer sb = this.getBasicMsg().append("正确值"+count+"---实际送货数["+realCount*-1+"]和报单送货数["+count+"]不符");
                    response.setMsg(sb.toString());
                }
            }else{
                response.setSuccess(false);
                StringBuffer sb = this.getBasicMsg().append("账面库存["+this.getPaperCount()+"]和实际库存["+this.getRealCount()+"]不符");
                response.setMsg(sb.toString());
            }
         // 退货
        }else if(RETURN_.equals(isComplete())){
            if(realCount == paperCount){
                if(!(realCount*-1 == 0)){
                    response.setSuccess(false);
                    StringBuffer sb = this.getBasicMsg().append("实际送货数["+realCount*-1+"] 不为0");
                    response.setMsg(sb.toString());
                }
            }else{
                response.setSuccess(false);
                StringBuffer sb = this.getBasicMsg().append("正确值[0]---账面库存["+this.getPaperCount()+"]和实际库存["+this.getRealCount()+"]不符");
                response.setMsg(sb.toString());
            }
        }else{
            response.setSuccess(false);
            StringBuffer sb = this.getBasicMsg().append("送货状态["+deliveryStatues+"] 未考虑");
            response.setMsg(sb.toString());
        }
        return response;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getInstallId() {
        return installId;
    }

    public void setInstallId(Integer installId) {
        this.installId = installId;
    }

    public Integer getDealSendid() {
        return dealSendid;
    }

    public void setDealSendid(Integer dealSendid) {
        this.dealSendid = dealSendid;
    }

    public Integer getOderStatus() {
        return oderStatus;
    }

    public void setOderStatus(Integer oderStatus) {
        this.oderStatus = oderStatus;
    }
}
