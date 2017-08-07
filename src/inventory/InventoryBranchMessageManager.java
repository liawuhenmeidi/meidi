package inventory;


import database.DB;
import ordersgoods.OrderGoods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import product.ProductService;
import utill.StringUtill;
import utill.TimeUtill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class InventoryBranchMessageManager {
    protected static Log logger = LogFactory.getLog(InventoryBranchManager.class);
    public static String getsql(InventoryBranchMessage im){
        String sql = "INSERT INTO `liaowuhen`.`mdinventorybranchmessage` (" +
                "`id`," +
                "`branchid`," +
                "`inventoryid`," +
                "`time`," +
                "`type`," +
                "`allotRealcount`," +
                "`allotPapercount`," +
                "`operatortype`," +
                "`realcount`," +
                "`papercount`," +
                "`sendUser`," +
                "`receiveuser`," +
                "`devidety`," +
                "`inventoryString`," +
                "`oldrealcount`," +
                "`oldpapercount`," +
                "`remark`" +
                ")" +
                "VALUES" +
                "(" +
                null +","+
                im.getBranchid() +","+
                im.getInventoryid() +","+
                null +","+
                im.getTypeid()+","+
                im.getAllotRealcount() +","+
                im.getAllotPapercount() +","+
                im.getOperatortype().getIndex()+","+
                "0," +
                "0," +
                im.getSendUser() +","+
                im.getReceiveuser() +","+
                "0," +
                "'"+im.getInventoryString() +"',"+
                "0," +
                "0," +
                "'"+im.getRemark()+"'"+
                ");";

        return  sql;
    }
    public static List<String> save(Inventory inve, int branchid, int type, String time) {
        List<InventoryMessage> orders = inve.getInventory();
        List<String> sqls = new ArrayList<String>();

        for (int i = 0; i < orders.size(); i++) {
            InventoryMessage order = orders.get(i);
            String sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,inventcount)" +
                    "  values ( null, '" + branchid + "', '" + inve.getId() + "','" + time + "','" + order.getProductId() + "'," + order.getCount() + "," + type + ",select count from mdinventorybranch where branchid = " + branchid + " and  type = '" + order.getProductId() + "' )";
            logger.info(sql);
            sqls.add(sql);
        }
        return sqls;
    }

    public static List<InventoryBranchMessage> getMap(String sql ) {

        List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
        Connection conn = DB.getConn();

        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                categorys.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        //logger.info(categorys.size());
        return categorys;
    }

    public static String update(InventoryBranchMessage in) {
        String sql = "update  mdinventorybranchmessage set branchid = '"+in.getBranchid()+"',inventoryid = '"+in.getInventoryid()+"' , inventoryString = '"+in.getInventoryString()+"' ,time= '"+in.getTime()+"',type= '"+in.getTypeid()+"',allotRealcount= '"+in.getAllotRealcount()+"',allotPapercount= '"+in.getAllotPapercount()+"',operatortype= '"+in.getOperatortype()+"',realcount= '"+in.getRealcount()+"',papercount= '"+in.getPapercount()+"',sendUser= '"+in.getSendUser()+"',receiveuser= '"+ in.getReceiveuser()+"',devidety= '-1',oldrealcount= '"+in.getOldrealcount()+"',oldpapercount= '"+in.getOldpapercount()+"',isoverstatues = "+in.getIsOverStatues()+"  where id = "+in.getId();

        return sql;
    }

    public static String update(List<OrderGoods> list) {
        //logger.info(list);
        List<Integer> listi = new ArrayList<Integer>();
        if(!list.isEmpty()){
            Iterator<OrderGoods> it = list.iterator();
            while(it.hasNext()){
                OrderGoods og = it.next();
                listi.add(og.getTid());

            }
        }
        //logger.info(listi);
        String sql = "update  mdinventorybranch set activetime = '"+TimeUtill.getdateString()+"'  where type in " +StringUtill.getStr(listi);

        return sql;
    }

    public static List<String> update(
            Map<String, InventoryBranchMessage> map) {
		/*
		 * String sql = "UPDATE categories" + " SET display_order = CASE id " +
		 * " WHEN 1 THEN 3 " + " WHEN 2 THEN 4 " + " WHEN 3 THEN 5 " + " END" +
		 * "WHERE id IN (1,2,3)";
		 */
        List<String> list = new ArrayList<String>();
        if (null != map) {
            Set<Map.Entry<String, InventoryBranchMessage>> set = map
                    .entrySet();
            Iterator<Map.Entry<String, InventoryBranchMessage>> it = set
                    .iterator();
            while (it.hasNext()) {
                Map.Entry<String, InventoryBranchMessage> mapen = it
                        .next();

                InventoryBranchMessage inm = mapen.getValue();
                String sql = update(inm);
                list.add(sql);
            }
        }
        return list;
    }

    public static List<InventoryBranchMessage> getCategory(String type,
                                                           String branchid, String time) {

        List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
        Connection conn = DB.getConn();
        String sql = "";
        if (!StringUtill.isNull(branchid)) {
            sql = "select * from mdinventorybranchmessage where type = '"
                    + type
                    + "' and branchid not in (select id from mdbranch where statues = 1 ) and branchid = "
                    + branchid + time;
        } else {
            sql = "select * from mdinventorybranchmessage where type = '"
                    + type
                    + "' and branchid not in (select id from mdbranch where statues = 1 ) "
                    + time;
        }

        logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                categorys.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        logger.info(categorys.size());
        return categorys;
    }

    public static List<InventoryBranchMessage> getCategory(String type,
                                                           String branchid, String starttime, String endtime) {
        String time = TimeUtill.getsearchtime(starttime, endtime);

        List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
        Connection conn = DB.getConn();
        String sql = "";
        if (!StringUtill.isNull(branchid)) {
            branchid = "(" + branchid + ")";
            sql = "select * from mdinventorybranchmessage where type = '"
                    + type
                    + "' and branchid not in (select id from mdbranch where statues = 1 ) and branchid in "
                    + branchid + time +" order by time";
        } else {
            sql = "select * from mdinventorybranchmessage where type = '"
                    + type
                    + "' and branchid not in (select id from mdbranch where statues = 1 ) "
                    + time +" order by time";
        }

        logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                categorys.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        // logger.info(categorys.size());
        return categorys;
    }

    public static Map<String, Map<String,List<InventoryBranchMessage>>> getmap(int branchid,int inventoryid) {

        Map<String, Map<String,List<InventoryBranchMessage>>> categorys = new HashMap<String, Map<String,List<InventoryBranchMessage>>>();
        Connection conn = DB.getConn();
        String sql = "select * from mdinventorybranchmessage where  inventoryid = "
                + inventoryid + " and branchid = "+branchid+" order by id ";
        logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                Map<String,List<InventoryBranchMessage>> maps = categorys.get(u.getTypeid());
                if(null == maps){
                    maps = new HashMap<String,List<InventoryBranchMessage>>();
                    categorys.put(u.getTypeid(), maps);
                }

                List<InventoryBranchMessage> list = maps.get(u.getTypeStatues()+"");
                if (null == list) {
                    list = new ArrayList<InventoryBranchMessage>();
                    maps.put(u.getTypeStatues()+"", list);
                }
                list.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        logger.info(categorys.size());
        return categorys;

    }

    public static Map<String, Integer> getMapAnalyze(String branchid, String starttime, String endtime) {
        Map<String, Integer> mapAnalyze = new HashMap<String, Integer>();
        Map<String, List<InventoryBranchMessage>> map = getMap(branchid, starttime, endtime);

        Set<Map.Entry<String, List<InventoryBranchMessage>>> set = map.entrySet();

        Iterator<Map.Entry<String, List<InventoryBranchMessage>>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<String, List<InventoryBranchMessage>> maps = it.next();
            String type = maps.getKey();
            List<InventoryBranchMessage> list = maps.getValue();
            Iterator<InventoryBranchMessage> itL = list.iterator();
            int count = 0;
            while (itL.hasNext()) {
                InventoryBranchMessage message = itL.next();
                if (message.getOperatortype().getIndex() != 1 && message.getOperatortype().getIndex() != 2 && message.getOperatortype().getIndex() != 0) {
                    count += message.getAllotRealcount();
                }

            }

            mapAnalyze.put(type, count);
        }
        return mapAnalyze;
    }

    public static Map<String, List<InventoryBranchMessage>> getMap(String branchid, String starttime, String endtime) {
        String time = TimeUtill.getsearchtime(starttime, endtime);

        Map<String, List<InventoryBranchMessage>> categorys = new HashMap<String, List<InventoryBranchMessage>>();
        Connection conn = DB.getConn();
        String sql;
        if (!StringUtill.isNull(branchid)) {
            branchid = "(" + branchid + ")";
            sql = "select * from mdinventorybranchmessage where branchid not in (select id from mdbranch where statues = 1 ) and branchid in " + branchid + time;
        } else {
            sql = "select * from mdinventorybranchmessage where  branchid not in (select id from mdbranch where statues = 1 ) " + time;
        }


        logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                List<InventoryBranchMessage> list = categorys.get(u.getType());
                if (null == list) {
                    list = new ArrayList<InventoryBranchMessage>();
                    categorys.put(u.getType(), list);
                }
                list.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        logger.info(categorys.size());
        return categorys;
    }


    public static List<InventoryBranchMessage> getCategory(String type, String branchid) {

        String sql = "select * from mdinventorybranchmessage where type = '" + type + "' and branchid = " + branchid;

        logger.info(sql);
        return getBySql(sql);

    }

    public static List<InventoryBranchMessageCount> getByPrintldId(String printId) {
        String sql ="SELECT " +
                "*, COUNT(*) count " +
                "FROM " +
                "mdinventorybranchmessage " +
                "WHERE " +
                "inventoryString = '"+printId+"' " +
                "GROUP BY " +
                "type," +
                "operatortype " +
                "ORDER BY " +
                "type," +
                "id;";


        List<InventoryBranchMessageCount> categorys = new ArrayList<InventoryBranchMessageCount>();
        Connection conn = DB.getConn();
        //logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                InventoryBranchMessageCount uc = new InventoryBranchMessageCount();
                uc.setInventoryBranchMessage(u);
                getCategoryFromRs(uc,rs);
                categorys.add(uc);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        return categorys;

    }

    public static List<InventoryBranchMessage> getBySql(String sql) {

        List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
        Connection conn = DB.getConn();
        logger.info(sql);
        Statement stmt = DB.getStatement(conn);
        ResultSet rs = DB.getResultSet(stmt, sql);
        try {
            while (rs.next()) {
                InventoryBranchMessage u = getCategoryFromRs(rs);
                categorys.add(u);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DB.close(rs);
            DB.close(stmt);
            DB.close(conn);
        }
        logger.info(categorys.size());
        return categorys;
    }

    private static InventoryBranchMessage getCategoryFromRs(ResultSet rs) {
        InventoryBranchMessage c = new InventoryBranchMessage();
        try {
            c.setId(rs.getInt("id"));
            c.setBranchid(rs.getInt("branchid"));
            c.setAllotPapercount(rs.getInt("allotPapercount"));
            c.setAllotRealcount(rs.getInt("allotRealcount"));
            c.setInventoryid(rs.getInt("inventoryid"));
            c.setInventoryString(rs.getString("inventoryString"));
            c.setTypeid(rs.getString("type"));
            c.setType(ProductService.getIDmap().get(Integer.valueOf(c.getTypeid())).getType());
            c.setTime(rs.getString("time"));
            c.setOperatortype(InventoryBranchMessage.OperatorType.getOperatorType(rs.getInt("operatortype")));
            c.setRealcount(rs.getInt("realcount"));
            c.setPapercount(rs.getInt("papercount"));
            c.setDevidety(rs.getInt("devidety"));
            c.setSendUser(rs.getInt("sendUser"));
            c.setReceiveuser(rs.getInt("receiveuser"));
            c.setOldpapercount(rs.getInt("oldpapercount"));
            c.setOldrealcount(rs.getInt("oldrealcount"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    private static void getCategoryFromRs(InventoryBranchMessageCount ib,ResultSet rs) {
        try {
           ib.setCount(rs.getInt("count"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
