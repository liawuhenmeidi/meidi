package utill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpRequestUtill {
	protected static Log logger = LogFactory.getLog(HttpRequestUtill.class);

	public static String getSearch(HttpServletRequest request) {
		String sear = "";

		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String str = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(str);
			if ((!"num".equals(str)) && (!"page".equals(str)) && (!"sort".equals(str)) && (!"statues".equals(str)) && (!"type".equals(str)) && (!"method".equals(str)) && (!"searched".equals(str)) &&
					(paramValues.length == 1)) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					if ("sendtype".equals(str)) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr + "%' )  and statues = 0)";
						}
					} else if ("saletype".equals(str)) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and id in (select orderid  from mdorderproduct where " + str + " in ( select id from mdproduct where ptype like '%" + strr + "%' )  and statues = 1)";
						}
					} else if ("sendcategoryname".equals(str)) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where id in (" + strr + ")) and statues = 0)";
						}
					} else if ("salecategoryname".equals(str)) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where id in  (" + strr + ")) and statues = 1)";
						}
					} else if (("giftName".equals(str)) || ("statues".equals(str))) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and id in (select orderid  from mdordergift where " + str + " like '%" + strr + "%')";
						}
					} else if (("dealSendid".equals(str)) || ("saleID".equals(str)) || ("sendId".equals(str)) || ("installid".equals(str)) || ("returnid".equals(str))) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and " + str + " in (select id from mduser  where username like '%" + strr + "%')";
						}
					} else if ("orderbranch".equals(str)) {
						String strr = request.getParameter(str);
						sear = sear + " and " + str + " in (select id from mdbranch where bname like '%" + strr + "%')";
					} else if (("deliveryStatues".equals(str)) || ("oderStatus".equals(str))) {
						String strr = request.getParameter(str);
						if (strr.equals("-1")) {
							strr = "3,4,5,11,12,13";
						}
						sear = sear + " and " + str + " in (" + strr + ")";
					} else if ("saledatestart".equals(str)) {
						String start = request.getParameter(str);
						String end = request.getParameter("saledateend");
						String sqlstr = "saledate";
						if ((start != null) && (start != "") && (start != "null")) {
							sear = sear + " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
							if ((end != null) && (end != "") && (end != "null")) {
								sear = sear + " '" + end + "'";
							} else {
								sear = sear + "now()";
							}
						} else if ((end != null) && (end != "") && (end != "null")) {
							sear = sear + " and " + sqlstr + "  < '" + end + "'";
						}
					} else if ("dealsendTimestart".equals(str)) {
						String start = request.getParameter(str);
						String end = request.getParameter("dealsendTimeend");
						String sqlstr = "dealsendTime";
						if ((start != null) && (start != "") && (start != "null")) {
							sear = sear + " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
							if ((end != null) && (end != "") && (end != "null")) {
								sear = sear + " '" + end + "'";
							} else {
								sear = sear + "now()";
							}
						} else if ((end != null) && (end != "") && (end != "null")) {
							sear = sear + " and " + sqlstr + "  < '" + end + "'";
						}
					} else if ("installTimestart".equals(str)) {
						String start = request.getParameter(str);
						String end = request.getParameter("installTimeend");
						String sqlstr = "installTime";
						if ((start != null) && (start != "") && (start != "null")) {
							sear = sear + " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
							if ((end != null) && (end != "") && (end != "null")) {
								sear = sear + " '" + end + "'";
							} else {
								sear = sear + "now()";
							}
						} else if ((end != null) && (end != "") && (end != "null")) {
							sear = sear + " and " + sqlstr + "  < '" + end + "'";
						}
					} else if ("andatestart".equals(str)) {
						String start = request.getParameter(str);
						String end = request.getParameter("andateend");
						String sqlstr = "andate";
						if ((start != null) && (start != "") && (start != "null")) {
							sear = sear + " and " + sqlstr + "  BETWEEN '" + start + "'  and  ";
							if ((end != null) && (end != "") && (end != "null")) {
								sear = sear + " '" + end + "'";
							} else {
								sear = sear + "now()";
							}
						} else if ((end != null) && (end != "") && (end != "null")) {
							sear = sear + " and " + sqlstr + "  < '" + end + "'";
						}
					} else if ("statuesChargeSale".equals(str)) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							if (Integer.valueOf(strr).intValue() == 1) {
								sear = sear + " and " + str + " is not null";
							} else {
								sear = sear + " and " + str + " is  null";
							}
						}
					} else if ((!"dealsendTimeend".equals(str)) && (!"saledateend".equals(str)) && (!"installTimeend".equals(str)) && (!"andateend".equals(str))) {
						String strr = request.getParameter(str);
						if ((strr != "") && (strr != null)) {
							sear = sear + " and " + str + " like '%" + strr + "%'";
						}
					}
				}
			}
		}
		return sear;
	}

	public static String getSearchCookie(HttpServletRequest request) {
		String sear = "";
		String[] search = request.getParameterValues("search");
		if (search != null) {
			for (int i = 0; i < search.length; i++) {
				String str = search[i];
				if (("saledate".equals(str)) || ("andate".equals(str)) || ("dealsendTime".equals(str))) {
					String start = request.getParameter(str + "start");
					String end = request.getParameter(str + "end");
					boolean flag = false;
					if ((start != null) && (start != "") && (start != "null")) {
						sear = sear + " and " + str + "  BETWEEN '" + start + "'  and  ";
						flag = true;
					}
					if ((end != null) && (end != "") && (end != "null")) {
						sear = sear + " '" + end + "'";
					} else if (flag) {
						sear = sear + "now()";
					}
				} else if (("sendtype".equals(str)) || ("saletype".equals(str))) {
					String strr = request.getParameter(str);
					if ((strr != "") && (strr != null)) {
						sear = sear + " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr + "%')";
					}
				} else if ("categoryname".equals(str)) {
					String strr = request.getParameter(str);
					if ((strr != "") && (strr != null)) {
						sear = sear + " and id in ( select orderid  from mdorderproduct where categoryID in (select id  from mdcategory where " + str + " like '%" + strr + "%'))";
					}
				} else if (("giftName".equals(str)) || ("statues".equals(str))) {
					String strr = request.getParameter(str);
					if ((strr != "") && (strr != null)) {
						sear = sear + " and id in (select orderid  from mdordergift where " + str + " like '%" + strr + "%')";
					}
				} else if (("dealSendid".equals(str)) || ("saleID".equals(str)) || ("sendId".equals(str))) {
					String strr = request.getParameter(str);
					if ((strr != "") && (strr != null)) {
						sear = sear + " and " + str + " in (select id from mduser  where username like '%" + strr + "%')";
					}
				} else {
					String strr = request.getParameter(str);
					if ((strr != "") && (strr != null)) {
						sear = sear + " and " + str + " like '%" + strr + "%'";
					}
				}
			}
		} else {
			sear = "";
		}
		return sear;
	}
}
