package inventory;

import response.Response;

/**
 * Created by liaowuhen on 2017/7/27.
 */
public class InventoryCheck {

    private static final String mm = "明细账面库存(库存流水,正确)";
    private Integer branchId;
    private String bname;
    private Integer type;
    private String ptype;
    /**
     * 明细计算账面
     */
    private Integer pa;

    /**
     * 明细计算实际
     */
    private Integer re;

    /**
     * 实际库存
     */
    private Integer realCount;

    /**
     * 账面库存
     */
    private Integer paperCount;


    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPa() {
        return pa;
    }

    public void setPa(Integer pa) {
        this.pa = pa;
    }

    public Integer getRe() {
        return re;
    }

    public void setRe(Integer re) {
        this.re = re;
    }

    public Integer getPaperCount() {
        return paperCount;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
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

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public Response check(){
        Response response = new Response();
        response.setSuccess(true);

        if(pa != paperCount){
            response.setSuccess(false);
            response.setMsg(mm+"["+pa+"]与库存账面库存["+paperCount+"]不符");
        }

        if(re != realCount){
            response.setSuccess(false);
            response.setMsg(mm+"["+re+"]与库存实际库存["+realCount+"]不符");
        }

        return  response;
    }
}
