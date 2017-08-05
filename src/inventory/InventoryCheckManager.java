package inventory;

import database.DB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import response.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaowuhen on 2017/7/27.
 * <p/>
 * 库存校验
 */
public class InventoryCheckManager {

    protected static Log logger = LogFactory.getLog(InventoryCheckManager.class);

    private static String sql = "SELECT\n" +
            "\tib.*, mb.realcount,\n" +
            "\tmb.papercount,\n" +
            "\tmb.inventoryid,\n" +
            "  mdbranch.bname,\n" +
            "mdproduct.ptype\n" +
            "\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tmm.branchid branchId,\n" +
            "\t\t\tmm.type,\n" +
            "\t\t\tSUM(allotPapercount) pa,\n" +
            "\t\t\tSUM(allotRealcount) re\n" +
            "\t\tFROM\n" +
            "\t\t\tmdinventorybranchmessage mm\n" +
            "\t\tWHERE\n" +
            "\t\t\tbranchid != 1\n" +
            "\t\tGROUP BY\n" +
            "\t\t\tbranchid,\n" +
            "\t\t\ttype\n" +
            "\t) ib\n" +
            "\n" +
            "LEFT JOIN mdbranch on mdbranch.id = ib.branchId\n" +
            "LEFT JOIN mdproduct on mdproduct.id = ib.type\n" +
            "LEFT JOIN mdinventorybranch mb ON ib.branchid = mb.branchid\n" +
            "AND ib.type = mb.type\n" +
            "\n" +
            ";\n" +
            "\n";
    public static void main(String[] args) {
        List<InventoryCheck> li = InventoryCheckManager.getInventoryVerify();
        int count = 0;
        for(InventoryCheck inventoryCheck:li){
            Response response = inventoryCheck.check();
            StringBuffer sb = new StringBuffer();
            if(!response.isSuccess()){
                count ++;
                sb.append("型号:"+inventoryCheck.getType()+"["+inventoryCheck.getPtype()+"]");
                sb.append("-----门店:"+inventoryCheck.getBranchId()+"["+inventoryCheck.getBname()+"]");
                sb.append("-----:"+response.getMsg());
                logger.info(sb.toString());
            }
        }

        logger.info(count);
    }


    private static List<InventoryCheck> getInventoryVerify() {
        List<InventoryCheck> li = new ArrayList<InventoryCheck>();

        Connection conn = DB.getConn();
        PreparedStatement stmt = DB.prepare(conn,sql);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryCheck orders = getInventoryCheckFromRs(rs);
                li.add(orders);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.close(stmt);
            DB.close(rs);
            DB.close(conn);
        }

        return li;
    }


    private static InventoryCheck getInventoryCheckFromRs(ResultSet rs) throws SQLException {
        InventoryCheck c = new InventoryCheck();

        c.setBranchId(rs.getInt("branchId"));
        c.setPa(rs.getInt("pa"));
        c.setPaperCount(rs.getInt("paperCount"));
        c.setRe(rs.getInt("re"));
        c.setRealCount(rs.getInt("realCount"));
        c.setType(rs.getInt("type"));
        c.setBname(rs.getString("bname"));
        c.setPtype(rs.getString("ptype"));
        return c;
    }

}
