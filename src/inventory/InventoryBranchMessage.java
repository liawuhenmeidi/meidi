package inventory;

import java.io.Serializable;

public class InventoryBranchMessage implements Serializable{

    public enum OperatorType{

        OUT("出货", 0),IN("入库", 1), CLERK_SEND_ORDERS("文员派单", 2), ACCOUNT_REGULATION("账面调货", 3),INSTALL_COMPANY_RELEASE("安装公司释放", 4),
        SEND_RELEASE("送货员释放", 6),SALES_RETURN("退货员拉回", 7),CLERK_AGREE_SALES_RETURN("文员同意退货", 8),SALES_RETURN_RELEASE("退货员释放", 9),check("盘点记录", 10),
        INSTALL_COMPANY_SEND_ORDERS("安装公司派送货员", 11),EXCHANGE_QUERY("换货员拉回次品", 12),RELEASE("释放", 20);

        private String name;
        private int index;
        // 构造方法
        OperatorType(String name, int index) {
            this.name = name;
            this.index = index;
        }
        // 普通方法
        public static OperatorType getOperatorType(int index) {
            for (OperatorType c : OperatorType.values()) {
                if (c.getIndex() == index) {
                    return c;
                }
            }
            return null;
        }

        // 普通方法
        public static String getName(int index) {
            for (OperatorType c : OperatorType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
    }

    private int id;
    private int inventoryid;  // 所包含的产品入库信息
    private int branchid;
    private String time;


    private String type;

    private int allotRealcount;

    private int allotPapercount;

    private OperatorType operatortype;

    private int realcount = 0 ;  //  实际库存

    private int papercount = 0 ;  //  虚拟库存

    private String typeid;

    private int sendUser;

    private int receiveuser;

    private int devidety;

    private String inventoryString;


    private int oldrealcount;
    private int oldpapercount;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOldrealcount() {
        return oldrealcount;
    }

    public void setOldrealcount(int oldrealcount) {
        this.oldrealcount = oldrealcount;
    }

    public int getOldpapercount() {
        return oldpapercount;
    }

    public void setOldpapercount(int oldpapercount) {
        this.oldpapercount = oldpapercount;
    }

    public String getInventoryString() {
        return inventoryString;
    }

    public void setInventoryString(String inventoryString) {
        this.inventoryString = inventoryString;
    }


    public int getSendUser() {
        return sendUser;
    }

    public void setSendUser(int sendUser) {
        this.sendUser = sendUser;
    }

    public int getReceiveuser() {
        return receiveuser;
    }

    public void setReceiveuser(int receiveuser) {
        this.receiveuser = receiveuser;
    }

    public int getDevidety() {
        return devidety;
    }

    public void setDevidety(int devidety) {
        this.devidety = devidety;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public int getRealcount() {
        return realcount;
    }

    public void setRealcount(int realcount) {
        this.realcount = realcount;
    }

    public int getPapercount() {
        return papercount;
    }

    public void setPapercount(int papercount) {
        this.papercount = papercount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public OperatorType getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(OperatorType operatortype) {
        this.operatortype = operatortype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(int inventoryid) {
        this.inventoryid = inventoryid;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAllotRealcount() {
        return allotRealcount;
    }

    public void setAllotRealcount(int allotRealcount) {
        this.allotRealcount = allotRealcount;
    }

    public int getAllotPapercount() {
        return allotPapercount;
    }

    public void setAllotPapercount(int allotPapercount) {
        this.allotPapercount = allotPapercount;
    }


}
