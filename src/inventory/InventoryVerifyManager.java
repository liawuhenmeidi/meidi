package inventory;

import database.DB;
import order.Order;
import order.OrderManager;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import response.Response;
import utill.DBUtill;
import utill.TimeUtill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by liaowuhen on 2017/7/27.
 * <p/>
 * 库存校验
 */
public class InventoryVerifyManager {

    protected static Log logger = LogFactory.getLog(InventoryVerifyManager.class);

    private static String sql = "SELECT inv.* , mdorderproduct.count,mdproduct.ptype  from (SELECT\n" +
            "\n" +
            "            SUM(allotPapercount) paperCount,\n" +
            "            SUM(allotRealcount) realCount,\n" +
            "            inventoryString,\n" +
            "            type,\n" +
            "              mdorder.id,\n" +
            "            mdorder.oderStatus,\n" +
            "            mdorder.dealSendid,\n" +
            "              mdorder.sendId,\n" +
            "             mdorder.installId,\n" +
            "              deliveryStatues,\n" +
            "              branchid ,\n" +
            "              mdorder.orderbranch\n" +
            "            FROM\n" +
            "            mdorder\n" +
            "            LEFT JOIN mdinventorybranchmessage mb ON mb.inventoryid = mdorder.id\n" +
            "            WHERE\n" +
            "             1 =1 \n" +
            "             and mb.operatortype not in (0,1)\n" +
            "             and deliveryStatues not in (0)\n" +
            "             and mdorder.submittime >= ?\n" +
            "             and mdorder.submittime <= ?\n" +
            "            GROUP BY\n" +
            "            inventoryid,\n" +
            "            type) inv \n" +
            "            \n" +
            "            LEFT JOIN mdproduct on mdproduct.id = inv.type\n" +
            "            LEFT JOIN mdorderproduct on mdorderproduct.orderid = inv.id and mdorderproduct.sendtype = inv.type;\n";

    public static void main(String[] args) {
        List<InventoryVerify> li = InventoryVerifyManager.getInventoryVerify("2013-01-01", null);
        List<Order> orders = getRepetOrder();
        int count = 0;
        Set<String> ids = new HashSet<String>();
        for (InventoryVerify iv : li) {
            Response response = iv.check();
            if (!response.isSuccess()) {
                count++;
                //logger.info("**********start********");

                boolean flag = isRepetOrder(orders, iv);
                if (flag) {
                    logger.info("重复单据***********");
                    logger.info(response.getMsg());
                    checkMessageDetail(ids, iv);
                } else {
                    logger.info(response.getMsg());
                    checkMessageDetail(ids, iv);
                }
                //logger.info("**********end********");

            }
        }
        logger.info(count);
    }

    private static void deal_1(InventoryVerify iv, InventoryBranchMessageCount ic) {
        logger.info("deal_1");
        if (InventoryVerify.COMPLETE.equals(iv.isComplete())) {
            if (InventoryBranchMessage.OperatorType.INSTALL_COMPANY_SEND_ORDERS.equals(ic.getInventoryBranchMessage().getOperatortype())) {
                InventoryBranchMessage clone = (InventoryBranchMessage) SerializationUtils.clone(ic.getInventoryBranchMessage());
                clone.setOperatortype(InventoryBranchMessage.OperatorType.CLERK_SEND_ORDERS);
                clone.setAllotPapercount(clone.getAllotRealcount());
                clone.setAllotRealcount(0);
                clone.setReceiveuser(clone.getSendUser());
                clone.setSendUser(311);
                clone.setRemark("人工添加");
                String sql = InventoryBranchMessageManager.getsql(clone);
                logger.info(sql);
                DBUtill.sava(sql);

            } else {
                logger.info(iv.isComplete() + "----" + ic.check());
            }

        } else if (InventoryVerify.RETURN_.equals(iv.isComplete())) {
            InventoryBranchMessage clone = (InventoryBranchMessage) SerializationUtils.clone(ic.getInventoryBranchMessage());
            clone.setAllotRealcount(0);
            clone.setReceiveuser(clone.getReceiveuser());
            clone.setSendUser(clone.getSendUser());
            clone.setRemark("人工添加");

            boolean flag = false;
            if (InventoryBranchMessage.OperatorType.CLERK_SEND_ORDERS.equals(clone.getOperatortype())) {
                clone.setOperatortype(InventoryBranchMessage.OperatorType.CLERK_AGREE_SALES_RETURN);
                clone.setAllotPapercount(1);
                flag = true;
            } else if (InventoryBranchMessage.OperatorType.CLERK_AGREE_SALES_RETURN.equals(clone.getOperatortype())) {
                clone.setOperatortype(InventoryBranchMessage.OperatorType.CLERK_SEND_ORDERS);
                clone.setAllotPapercount(-1);
                flag = true;
            } else {
                logger.info(iv.isComplete() + "----" + ic.check());
            }

            if (flag) {
                String sql = InventoryBranchMessageManager.getsql(clone);
                DBUtill.sava(sql);
                logger.info(sql);
            }
        } else {
            logger.info(iv.isComplete() + "----" + ic.check());
        }
    }

    public static List<Order> getRepetOrder() {
        String sql = "SELECT * from mdorder GROUP BY printlnid HAVING COUNT(*) > 1 ;";
        return OrderManager.getOrdersBySql(sql);
    }

    public static boolean isRepetOrder(List<Order> list, InventoryVerify iv) {
        if (list.size() > 0) {
            for (Order or : list) {
                if (or.getPrintlnid().equals(iv.getInventoryString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void deal2(InventoryVerify iv, List<InventoryBranchMessageCount> li) {
        logger.info("deal2");

        if (li.get(0).getInventoryBranchMessage().getTypeid().equals(li.get(1).getInventoryBranchMessage().getTypeid())) {
            logger.info("相同型号");
            Map<Integer, InventoryBranchMessageCount> map = new HashMap<Integer, InventoryBranchMessageCount>();
            for (InventoryBranchMessageCount in : li) {
                map.put(in.getInventoryBranchMessage().getOperatortype().getIndex(), in);
                logger.info(in.check());
            }

            if (InventoryVerify.COMPLETE.equals(iv.isComplete())) {
                if (map.keySet().contains(11) && map.keySet().contains(6)) {
                    logger.info("11_6------6 改为2");
                    //TODO
                }
            } else if (InventoryVerify.RETURN_.equals(iv.isComplete())) {
                // 7,8 缺少2，11  或者 6.8 缺少2,11
                if (map.keySet().contains(7) && map.keySet().contains(8) || map.keySet().contains(6) && map.keySet().contains(8)) {
                    InventoryBranchMessage clone = (InventoryBranchMessage) SerializationUtils.clone(map.get(8).getInventoryBranchMessage());
                    clone.setOperatortype(InventoryBranchMessage.OperatorType.CLERK_SEND_ORDERS);
                    clone.setAllotPapercount(-1);
                    clone.setReceiveuser(clone.getReceiveuser());
                    clone.setSendUser(clone.getSendUser());
                    clone.setRemark("人工添加");
                    String sql = InventoryBranchMessageManager.getsql(clone);
                    DBUtill.sava(sql);
                    logger.info(sql);
                    if (map.keySet().contains(6)) {
                        clone = (InventoryBranchMessage) SerializationUtils.clone(map.get(6).getInventoryBranchMessage());
                    } else {
                        clone = (InventoryBranchMessage) SerializationUtils.clone(map.get(7).getInventoryBranchMessage());
                    }
                    clone.setOperatortype(InventoryBranchMessage.OperatorType.INSTALL_COMPANY_SEND_ORDERS);
                    clone.setAllotRealcount(-1);
                    clone.setPapercount(0);
                    clone.setReceiveuser(clone.getReceiveuser());
                    clone.setSendUser(clone.getSendUser());
                    clone.setRemark("人工添加");
                    sql = InventoryBranchMessageManager.getsql(clone);
                    DBUtill.sava(sql);
                    logger.info(sql);
                }

            } else {
                logger.info(iv.isComplete());
            }

        } else {
            logger.info("不同型号");
            for (InventoryBranchMessageCount in : li) {
                deal_1(iv, in);
                logger.info(iv.isComplete() + "----" + in.check());
            }
        }
    }

    private static void checkMessage(InventoryVerify iv, List<InventoryBranchMessageCount> li) {
        if (1 == li.size()) {
            InventoryBranchMessageCount ic = li.get(0);
            deal_1(iv, ic);
        } else if (2 == li.size()) {
            deal2(iv, li);
            //logger.info(iv.isComplete() + "----" );
        } else if (3 == li.size()) {
            logger.info("other3----");
            //logger.info(iv.isComplete() + "333333333333" );
            //logger.info(iv.isComplete() + "----" );
        } else {
            logger.info("other----" + li.size());
            for (InventoryBranchMessageCount in : li) {
                /* map.put(in.getInventoryBranchMessage().getOperatortype().getIndex(),in);*/
                logger.info(in.check());
            }


        }



        /*for (InventoryBranchMessageCount ib : li) {
            logger.info(ib.check());

        }*/
    }

    private static void checkMessageDetail(Set<String> ids, InventoryVerify iv) {
        if (!ids.contains(iv.getInventoryString())) {
            ids.add(iv.getInventoryString());
            List<InventoryBranchMessageCount> li = InventoryBranchMessageManager.getByPrintldId(iv.getInventoryString());
            Map<String, List<InventoryBranchMessageCount>> map = new HashMap<String, List<InventoryBranchMessageCount>>();

            for (InventoryBranchMessageCount ic : li) {
                List<InventoryBranchMessageCount> list = map.get(ic.getInventoryBranchMessage().getTypeid());
                if (null == list) {
                    list = new ArrayList<InventoryBranchMessageCount>();
                    map.put(ic.getInventoryBranchMessage().getTypeid(), list);
                }
                list.add(ic);
            }

            Collection<List<InventoryBranchMessageCount>> collection = map.values();

            for (List<InventoryBranchMessageCount> list : collection) {
                checkMessage(iv, list);
            }

        }


    }

    private static List<InventoryVerify> getInventoryVerify(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime)) {
            startTime = TimeUtill.dataAdd(null, -1);
        }

        if (StringUtils.isEmpty(endTime)) {
            endTime = TimeUtill.getdateString();
        }
        List<InventoryVerify> li = new ArrayList<InventoryVerify>();

        Connection conn = DB.getConn();
        PreparedStatement stmt = DB.prepare(conn, sql);
        ResultSet rs = null;
        try {
            stmt.setString(1, startTime);
            stmt.setString(2, endTime);
            rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryVerify orders = getInventoryVerifyFromRs(rs);
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


    private static InventoryVerify getInventoryVerifyFromRs(ResultSet rs) throws SQLException {
        InventoryVerify c = new InventoryVerify();
        c.setId(rs.getInt("id"));
        c.setPaperCount(rs.getInt("paperCount"));
        c.setRealCount(rs.getInt("realCount"));
        c.setInventoryString(rs.getString("inventoryString"));
        c.setType(rs.getInt("type"));
        c.setPtype(rs.getString("ptype"));
        c.setDeliveryStatues(rs.getInt("deliveryStatues"));
        c.setCount(rs.getInt("count"));
        c.setSendId(rs.getInt("sendId"));
        c.setInstallId(rs.getInt("installId"));
        c.setDealSendid(rs.getInt("dealSendid"));
        c.setOderStatus(rs.getInt("oderStatus"));
        return c;
    }

}
