package inventory;

/**
 * Created by liaowuhen on 2017/8/1.
 */
public class InventoryBranchMessageCount {
    private Integer count;
    private InventoryBranchMessage inventoryBranchMessage;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public InventoryBranchMessage getInventoryBranchMessage() {
        return inventoryBranchMessage;
    }

    public void setInventoryBranchMessage(InventoryBranchMessage inventoryBranchMessage) {
        this.inventoryBranchMessage = inventoryBranchMessage;
    }

    public String check(){
        String typeId = getInventoryBranchMessage().getTypeid();
        Integer operate = getInventoryBranchMessage().getOperatortype().getIndex();
        Integer count = getCount();

        StringBuffer sb = new StringBuffer();
        sb.append("单号:");
        sb.append(getInventoryBranchMessage().getInventoryString());
        sb.append("---型号:");
        sb.append(typeId);
        sb.append("---操作");
        sb.append(operate);
        sb.append("---count");
        sb.append(count);

        return sb.toString();
    }
}
