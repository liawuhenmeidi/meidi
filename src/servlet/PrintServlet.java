package servlet;

import exportModel.ExportModel;
import gift.Gift;
import gift.GiftManager;
import gift.GiftService;
import group.Group;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import order.Order;
import order.OrderManager;
import order.OrderStatues;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import orderproduct.OrderProductService;
import ordersgoods.OrderGoods;
import ordersgoods.OrderGoodsAll;
import ordersgoods.OrderGoodsAllManager;
import ordersgoods.OrderMessage;
import ordersgoods.OrderMessageManager;

import uploadtotal.UploadTotal;
import uploadtotalgroup.UploadTotalGroup;
import uploadtotalgroup.UploadTotalGroupManager;
import user.User;
import user.UserManager;
import utill.BasicUtill;
import utill.DBUtill;
import utill.DoubleUtill;
import utill.HttpRequestUtill;
import utill.StringUtill;
import utill.TimeUtill;
import wilson.upload.UploadManager;
import wilson.upload.UploadSalaryModel;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;

import company.Company;

import branch.Branch;
import branchtype.BranchType;
import branchtype.BranchTypeManager;

import category.Category;
import category.CategoryManager;

/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class PrintServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	protected static Log logger = LogFactory.getLog(PrintServlet.class);

	/**
	 * 确认请求来自微信服务器
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String method = request.getParameter("method");
		logger.info(method);
		if ("exportall".equals(method)) {
			exportOrders(request, response);
		} else if ("total".equals(method) || "totalcategory".equals(method)
				|| "typetotal".equals(method)) {
			exporttotalExport(request, response);
		} else if ("OrderGoods".equals(method)) {
			exportOrderGoodsSN(request, response);
		} else if ("OrderGoodssend".equals(method)) {
			OrderGoodssend(request, response);
		} else if ("billing".equals(method)) {
			OrderGoodsbilling(request, response);

		}
	}

	/**
	 * 处理微信服务器发来的消息
	 */

	public void exporttotalExport(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("said");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
		Date date1 = new Date();
		String printlntime = df2.format(date1);
		String method = request.getParameter("method");
		String type = request.getParameter("type");
		int statues = Integer.valueOf(type);
		Map<String, UploadSalaryModel> mapus = UploadManager
				.getSalaryModelsAll();

		Map<String, Map<String, List<UploadTotal>>> mapt = null;
		Map<String, Map<String, List<UploadTotal>>> mapc = null;
		HashMap<String, List<UploadTotal>> maptypeinit = null;

		if ("total".equals(method)) {
			mapt = UploadManager.getTotalOrdersGroup(id, statues, "");
		} else if ("typetotal".equals(method)) {
			maptypeinit = UploadManager.getTotalOrdersGroup(id, "type",
					statues, "");
		} else if ("totalcategory".equals(method)) {
			mapc = UploadManager.getTotalOrdersCategoryGroup(id, statues, "");
		}

		String message = "";

		UploadTotalGroup upt = UploadTotalGroupManager.getUploadTotalGroup();
		if (upt != null) {
			message = upt.getCategoryname();
		}
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("销售统计表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		int x = 0;
		HSSFCell cell = row.createCell((short) x++);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("门店");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("品类");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("型号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("销售金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("扣点后单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("扣点后总价");
		cell.setCellStyle(style);

		int count = 0;
		int idcount = 0;
		double AllTotalcount = 0;
		int AllCount = 0;
		double AllTatalbreakcount = 0;

		if (null != mapt) {
			Set<Map.Entry<String, Map<String, List<UploadTotal>>>> setmap = mapt
					.entrySet();
			Iterator<Map.Entry<String, Map<String, List<UploadTotal>>>> itmap = setmap
					.iterator();
			while (itmap.hasNext()) {
				Map.Entry<String, Map<String, List<UploadTotal>>> enmap = itmap
						.next();
				Map<String, List<UploadTotal>> maptype = enmap.getValue();
				Set<Map.Entry<String, List<UploadTotal>>> setmaptype = maptype
						.entrySet();
				Iterator<Map.Entry<String, List<UploadTotal>>> itmaptype = setmaptype
						.iterator();
				double Totalcount = 0;
				int Count = 0;
				double Tatalbreakcount = 0;
				String branchname = "";
				while (itmaptype.hasNext()) {
					Map.Entry<String, List<UploadTotal>> enmaptype = itmaptype
							.next();
					String key = enmaptype.getKey();
					if (!StringUtill.isNull(message)) {
						JSONObject jsObj = JSONObject.fromObject(message);
						Iterator<String> it = jsObj.keys();
						while (it.hasNext()) {
							String t = it.next();
							if (key.equals(t)) {
								key = jsObj.getString(key);
							}
						}
					}
					List<UploadTotal> listup = enmaptype.getValue();
					double initTotalcount = 0;
					int initCount = 0;
					double initTatalbreakcount = 0;

					if (null != listup) {
						for (int i = 0; i < listup.size(); i++) {
							UploadTotal up = listup.get(i);
							branchname = up.getBranchname();
							Totalcount += up.getTotalcount();
							Count += up.getCount();
							Tatalbreakcount += up.getTatalbreakcount();
							AllTotalcount += up.getTotalcount();
							AllCount += up.getCount();
							AllTatalbreakcount += up.getTatalbreakcount();
							initTotalcount += up.getTotalcount();
							initCount += up.getCount();
							initTatalbreakcount += up.getTatalbreakcount();
							idcount++;

							String tpe = "";
							if (null != mapus) {
								UploadSalaryModel ups = mapus.get(StringUtill
										.getStringNocn(up.getType()));
								if (null != ups) {
									tpe = ups.getCatergory();
								}
							}

							row = sheet.createRow((int) count + 1);
							count++;
							int y = 0;
							idcount++;
							// 第四步，创建单元格，并设置值
							row.createCell((short) y++).setCellValue(idcount);
							row.createCell((short) y++).setCellValue(
									up.getBranchname());
							row.createCell((short) y++).setCellValue(tpe);
							row.createCell((short) y++).setCellValue(
									up.getType());
							row.createCell((short) y++).setCellValue(
									0 == up.getCount() ? "" : DoubleUtill
											.getdoubleTwo(up.getTotalcount()
													/ up.getCount()));
							row.createCell((short) y++).setCellValue(
									up.getCount());
							row.createCell((short) y++)
									.setCellValue(
											DoubleUtill.getdoubleTwo(up
													.getTotalcount()));
							row.createCell((short) y++).setCellValue(
									0 == up.getCount() ? "" : DoubleUtill
											.getdoubleTwo(up
													.getTatalbreakcount()
													/ up.getCount()));
							row.createCell((short) y++).setCellValue(
									DoubleUtill.getdoubleTwo(up
											.getTatalbreakcount()));
						}
					}

					row = sheet.createRow((int) count + 1);
					count++;
					int y = 0;

					HSSFCellStyle cellStyle = wb.createCellStyle(); // 创建一个样式
					cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index); // 设置颜色为红色
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					// 第四步，创建单元格，并设置值
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(branchname);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(key);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(initCount);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill.getdoubleTwo(initTotalcount));
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill
							.getdoubleTwo(initTatalbreakcount));
					cell.setCellStyle(cellStyle);

				}
				row = sheet.createRow((int) count + 1);
				count++;
				int y = 0;
				HSSFCellStyle cellStyle = wb.createCellStyle(); // 创建一个样式
				cellStyle.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				// 第四步，创建单元格，并设置值
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(branchname);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("总计");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(Count);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(Totalcount));
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(Tatalbreakcount));
				cell.setCellStyle(cellStyle);

			}
		}

		if (null != mapc) {
			Set<Map.Entry<String, Map<String, List<UploadTotal>>>> setmap = mapc
					.entrySet();
			Iterator<Map.Entry<String, Map<String, List<UploadTotal>>>> itmap = setmap
					.iterator();
			while (itmap.hasNext()) {
				Map.Entry<String, Map<String, List<UploadTotal>>> enmap = itmap
						.next();
				String key = enmap.getKey();

				if (!StringUtill.isNull(message)) {
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					while (it.hasNext()) {
						String t = it.next();
						if (key.equals(t)) {
							key = jsObj.getString(key);
						}
					}
				}

				Map<String, List<UploadTotal>> maptype = enmap.getValue();
				Set<Map.Entry<String, List<UploadTotal>>> setmaptype = maptype
						.entrySet();
				Iterator<Map.Entry<String, List<UploadTotal>>> itmaptype = setmaptype
						.iterator();
				double Totalcount = 0;
				int Count = 0;
				double Tatalbreakcount = 0;
				while (itmaptype.hasNext()) {
					Map.Entry<String, List<UploadTotal>> enmaptype = itmaptype
							.next();
					List<UploadTotal> listup = enmaptype.getValue();
					String branchname = enmaptype.getKey();
					double initTotalcount = 0;
					int initCount = 0;
					double initTatalbreakcount = 0;
					if (null != listup) {
						for (int i = 0; i < listup.size(); i++) {
							UploadTotal up = listup.get(i);
							Totalcount += up.getTotalcount();
							Count += up.getCount();
							Tatalbreakcount += up.getTatalbreakcount();
							AllTotalcount += up.getTotalcount();
							AllCount += up.getCount();
							AllTatalbreakcount += up.getTatalbreakcount();
							initTotalcount += up.getTotalcount();
							initCount += up.getCount();
							initTatalbreakcount += up.getTatalbreakcount();
							idcount++;

							String tpe = "";
							if (null != mapus) {
								UploadSalaryModel ups = mapus.get(StringUtill
										.getStringNocn(up.getType()));
								if (null != ups) {
									tpe = ups.getCatergory();
								}
							}

							row = sheet.createRow((int) count + 1);
							count++;
							int y = 0;
							idcount++;
							// 第四步，创建单元格，并设置值
							row.createCell((short) y++).setCellValue(idcount);
							row.createCell((short) y++).setCellValue(
									up.getBranchname());
							row.createCell((short) y++).setCellValue(tpe);
							row.createCell((short) y++).setCellValue(
									up.getType());
							row.createCell((short) y++).setCellValue(
									0 == up.getCount() ? "" : DoubleUtill
											.getdoubleTwo(up.getTotalcount()
													/ up.getCount()));
							row.createCell((short) y++).setCellValue(
									up.getCount());
							row.createCell((short) y++)
									.setCellValue(
											DoubleUtill.getdoubleTwo(up
													.getTotalcount()));
							row.createCell((short) y++).setCellValue(
									0 == up.getCount() ? "" : DoubleUtill
											.getdoubleTwo(up
													.getTatalbreakcount()
													/ up.getCount()));
							row.createCell((short) y++).setCellValue(
									DoubleUtill.getdoubleTwo(up
											.getTatalbreakcount()));

						}

					}

					row = sheet.createRow((int) count + 1);
					count++;
					int y = 0;
					// 第四步，创建单元格，并设置值
					HSSFCellStyle cellStyle = wb.createCellStyle(); // 创建一个样式
					cellStyle.setFillForegroundColor(HSSFColor.ORANGE.index); // 设置颜色为红色
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					// 第四步，创建单元格，并设置值
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(branchname);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(key);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(initCount);
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill.getdoubleTwo(initTotalcount));
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue("");
					cell.setCellStyle(cellStyle);
					cell = row.createCell((short) y++);
					cell.setCellValue(DoubleUtill
							.getdoubleTwo(initTatalbreakcount));
					cell.setCellStyle(cellStyle);

				}

				row = sheet.createRow((int) count + 1);
				count++;
				int y = 0;
				HSSFCellStyle cellStyle = wb.createCellStyle(); // 创建一个样式
				cellStyle.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				// 第四步，创建单元格，并设置值
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("总计");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(Count);
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(Totalcount));
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				cell = row.createCell((short) y++);
				cell.setCellValue(DoubleUtill.getdoubleTwo(Tatalbreakcount));
				cell.setCellStyle(cellStyle);

			}
		}

		if (null != maptypeinit) {
			Set<Map.Entry<String, List<UploadTotal>>> setmaptype = maptypeinit
					.entrySet();
			Iterator<Map.Entry<String, List<UploadTotal>>> itmaptype = setmaptype
					.iterator();
			double Totalcount = 0;
			int Count = 0;
			double Tatalbreakcount = 0;
			String branchname = "";
			while (itmaptype.hasNext()) {
				Map.Entry<String, List<UploadTotal>> enmaptype = itmaptype
						.next();
				String key = enmaptype.getKey();
				if (!StringUtill.isNull(message)) {
					JSONObject jsObj = JSONObject.fromObject(message);
					Iterator<String> it = jsObj.keys();
					while (it.hasNext()) {
						String t = it.next();
						if (key.equals(t)) {
							key = jsObj.getString(key);
						}
					}
				}

				List<UploadTotal> uplist = enmaptype.getValue();
				double initTotalcount = 0;
				int initCount = 0;
				double initTatalbreakcount = 0;

				if (null != uplist) {
					Iterator<UploadTotal> it = uplist.iterator();
					while (it.hasNext()) {
						UploadTotal up = it.next();
						Totalcount += up.getTotalcount();
						Count += up.getCount();
						Tatalbreakcount += up.getTatalbreakcount();
						AllTotalcount += up.getTotalcount();
						AllCount += up.getCount();
						AllTatalbreakcount += up.getTatalbreakcount();
						initTotalcount += up.getTotalcount();
						initCount += up.getCount();
						initTatalbreakcount += up.getTatalbreakcount();
						idcount++;

						String tpe = "";
						if (null != mapus) {
							UploadSalaryModel ups = mapus.get(StringUtill
									.getStringNocn(up.getType()));
							if (null != ups) {
								tpe = ups.getCatergory();
							}
						}

						row = sheet.createRow((int) count + 1);
						count++;
						int y = 0;
						row.createCell((short) y++).setCellValue(idcount);
						row.createCell((short) y++).setCellValue("");
						row.createCell((short) y++).setCellValue(tpe);
						row.createCell((short) y++).setCellValue(up.getType());
						row.createCell((short) y++).setCellValue(
								0 == up.getCount() ? "" : DoubleUtill
										.getdoubleTwo(up.getTotalcount()
												/ up.getCount()));
						row.createCell((short) y++).setCellValue(up.getCount());
						row.createCell((short) y++).setCellValue(
								DoubleUtill.getdoubleTwo(up.getTotalcount()));
						row.createCell((short) y++).setCellValue(
								0 == up.getCount() ? "" : DoubleUtill
										.getdoubleTwo(up.getTatalbreakcount()
												/ up.getCount()));
						row.createCell((short) y++).setCellValue(
								DoubleUtill.getdoubleTwo(up
										.getTatalbreakcount()));
					}
				}
			}
		}
		row = sheet.createRow((int) count + 1);
		count++;
		int y = 0;
		// 第四步，创建单元格，并设置值
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("总计");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue(AllCount);
		row.createCell((short) y++).setCellValue("");
		row.createCell((short) y++).setCellValue(
				DoubleUtill.getdoubleTwo(AllTotalcount));
		row.createCell((short) y++).setCellValue(
				DoubleUtill.getdoubleTwo(AllTatalbreakcount));
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ printlntime + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void exportOrders(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHH");
		Date date1 = new Date();
		String printlntime = df2.format(date1);
		String type = request.getParameter("type");
		String statues = request.getParameter("statues");
		int num = -1;
		String page = request.getParameter("page");
		String sort = request.getParameter("sort");
		String search = request.getParameter("searched");
		String sear = "";
		if (!StringUtill.isNull(search)) {
			sear = HttpRequestUtill.getSearch(request);
		}

		List<Order> list = OrderManager.getOrderlist(user,
				Integer.valueOf(type), Integer.valueOf(statues),
				Integer.valueOf(num), Integer.valueOf(page), sort, sear);

		HashMap<Integer, User> usermap = UserManager.getMap();

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("报装单");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		int x = 0;
		HSSFCell cell = row.createCell((short) x++);
		cell.setCellValue("单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("销售门店");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("销售员姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("销售员电话");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("pos(厂送)单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("OMS订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("验证码(联保单)");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("顾客姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("顾客电话");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("票面名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("票面型号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		if (UserManager.checkPermissions(user, Group.dealSend)) {
			cell.setCellValue("零售价");
			cell.setCellStyle(style);
			cell = row.createCell((short) x++);

		}
		cell.setCellValue("票面数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货型号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		if (UserManager.checkPermissions(user, Group.dealSend)) {
			cell.setCellValue("零售价");
			cell.setCellStyle(style);
			cell = row.createCell((short) x++);
		}
		cell.setCellValue("送货数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("赠品");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("赠品数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("赠品状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("开票日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("预约日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("文员派单日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货地区");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货地址");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("上报状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货人员");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("安装人员");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("安装时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("安装网点");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("送货是否已结款");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("厂送票是否已回");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("厂送票是否已消");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("厂送票是否结款");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("备注");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		int count = 0;
		for (int i = 0; i < list.size(); i++) {

			Order order = list.get(i);

			List<OrderProduct> listop = order.getOrderproduct();

			for (int m = 0; m < listop.size(); m++) {
				OrderProduct op = listop.get(m);

				if (op.getStatues() == 0) {
					row = sheet.createRow((int) count + 1);
					count++;
					int y = 0;
					// 第四步，创建单元格，并设置值
					row.createCell((short) y++).setCellValue(
							order.getPrintlnid() == null ? "" : order
									.getPrintlnid());
					row.createCell((short) y++).setCellValue(
							order.getbranchName(order.getBranch()));
					row.createCell((short) y++).setCellValue(
							usermap.get(order.getSaleID()).getUsername());
					row.createCell((short) y++).setCellValue(
							usermap.get(order.getSaleID()).getPhone());
					row.createCell((short) y++).setCellValue(order.getPos());
					row.createCell((short) y++).setCellValue(order.getSailId());
					row.createCell((short) y++).setCellValue(order.getCheck());
					row.createCell((short) y++).setCellValue(
							order.getUsername());

					row.createCell((short) y++).setCellValue(order.getPhone1());
					row.createCell((short) y++).setCellValue(
							order.getCategory(1, "      "));
					row.createCell((short) y++).setCellValue(
							order.getSendType(1, "      "));
					if (UserManager.checkPermissions(user, Group.dealSend)) {
						row.createCell((short) y++).setCellValue(
								order.getSendprice(1, ""));
					}

					row.createCell((short) y++).setCellValue(
							order.getSendCount(1, "      "));
					row.createCell((short) y++).setCellValue(
							op.getCategoryName());
					row.createCell((short) y++).setCellValue(op.getTypeName());
					if (UserManager.checkPermissions(user, Group.dealSend)) {
						row.createCell((short) y++).setCellValue(op.getPrice());
					}

					row.createCell((short) y++).setCellValue(op.getCount());
					row.createCell((short) y++).setCellValue(
							order.getGifttype("      "));
					row.createCell((short) y++).setCellValue(
							order.getGifcount("      "));
					row.createCell((short) y++).setCellValue(
							order.getGifStatues("      "));
					row.createCell((short) y++).setCellValue(
							order.getSaleTime());
					row.createCell((short) y++).setCellValue(order.getOdate());
					row.createCell((short) y++).setCellValue(
							order.getDealSendTime());
					row.createCell((short) y++).setCellValue(order.getLocate());
					row.createCell((short) y++).setCellValue(
							order.getLocateDetail());
					row.createCell((short) y++).setCellValue(
							OrderManager.getOrderStatues(order));
					String songhuo = OrderManager.getDeliveryStatues(order);

					row.createCell((short) y++).setCellValue(songhuo);

					row.createCell((short) y++).setCellValue(
							order.getsendName());
					row.createCell((short) y++).setCellValue(
							order.getSendtime());

					row.createCell((short) y++).setCellValue(
							order.getinstallName());
					row.createCell((short) y++).setCellValue(
							order.getInstalltime());

					row.createCell((short) y++).setCellValue(
							order.getdealsendName());

					row.createCell((short) y++).setCellValue(
							order.getStatues4() == 0 ? "否" : "是");
					row.createCell((short) y++).setCellValue(
							order.getStatues1() == 0 ? "否" : "是");
					row.createCell((short) y++).setCellValue(
							order.getStatues2() == 0 ? "否" : "是");
					row.createCell((short) y++).setCellValue(
							(StringUtill.isNull(order.getStatuesCharge()) ? "否"
									: order.getStatuesCharge()));
					row.createCell((short) y++).setCellValue(order.getRemark());
				}
			}

		}
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ printlntime + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportOrderGoodsSN(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		String name = request.getParameter("name");
		String ids = request.getParameter("ids");
		String branchtype = request.getParameter("branchtype");
		String statue = request.getParameter("statues");
		String typestatues = request.getParameter("typestatues");

		// logger.info(name);
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,
				OrderMessage.billing, name, ids);

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单");
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints(30);
		sheet.setColumnWidth(1, 20 * 256);

		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		// sheet.setColumnWidth((short)200,(short)1000);
		// row.setHeight((short)height);
		// style.set

		// 背景为红色
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
		style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setWrapText(true);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setWrapText(true);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		Font font = wb.createFont();
		// font.setFontHeightInPoints((short)24); //字体大小
		// font.setFontName("楷体");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD); //粗体
		font.setColor(HSSFColor.RED.index); // 绿字
		style2.setFont(font);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setFillForegroundColor(HSSFColor.YELLOW.index); // 设置颜色为红色
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setWrapText(true);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		// 第三步，在sheet中添加表头第0行,注意 老版本poi对Excel的行数列数有限制short
		int count = 0;
		HSSFRow row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));

		// row.setRowStyle(style);
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		HSSFCell cell = null;
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style1);
				cell.setCellValue("苏宁电器生活电器订单统一模板");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style1);
				cell.setCellValue("");
			}
		}

		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("请按照表格要求认真填写订单明细，填写完毕后请发送至,");
				cell.setCellValue(cell.getStringCellValue()
						+ "qiangrc@cnsuning.com");
				cell.setCellValue(cell.getStringCellValue()
						+ ",并电话通知相应品管确认邮件是否发送成功。每周二、周四做订单，周二上午12点以前收到的邮件周二完成，12点以后收到的邮件周四完成");

			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellValue("填写注意事项，订货数量最大不得超过400台每型号/每店，订单日期不得超过申请日期90天。");
				cell.setCellStyle(style);
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("每次填写请核实填写内容，明细表格填写是否完整，编码是否正确，是否价格文件已生效，是否有重复编码等，如我们在做订单发现类似错误或订单明细填写不完整，订单将一律不予受理。");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("如型号超过30个并操作门店超过20家以上，订单制作工作量较大时间较长，制单周期为1周，请代理商提前申请");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;

		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("订单明细中为代理商实时操作的型号，勿将老品、下市等不操作型号填写其中增加我司工作量及造成大量订单在途未达，各品管核查，如发现此种情况则停止该代理商一切苏宁业务，删除其代理商编码下所有订单。");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));
		count++;

		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("请代理商配合我部门工作，我们收到邮件后会尽快为您制单，尽量一单给您！请代理商关注自己SCS平台自行查询订单编号");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 2));
		sheet.addMergedRegion(new Region(count, (short) 5, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("原因:");
			} else if (i == 4) {
				cell = row.createCell((short) i);
				cell.setCellValue("申请人:");
				cell.setCellStyle(style2);
			} else if (i == 5) {
				cell = row.createCell((short) i);
				cell.setCellValue("联系电话:");
				cell.setCellStyle(style2);
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("");
			}
		}

		row = sheet.createRow((int) count);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		count++;
		int x = 0;
		cell = row.createCell((short) x++);
		cell.setCellValue("商品编码");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品名称");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货数量");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货门店编码");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("库位");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("日期");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("供应商编码");
		cell.setCellStyle(style3);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			OrderGoodsAll o = list.get(i);
			Branch branch = o.getOm().getBranch();
			List<OrderGoods> listog = o.getList();

			for (int j = 0; j < listog.size(); j++) {
				OrderGoods og = listog.get(j);

				row = sheet.createRow((int) count);
				count++;
				int y = 0;
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getType());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				// logger.info(og.getOrdernum());
				cell.setCellValue(og.getOrdernum());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(branch.getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getBranch());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(TimeUtill.getdateString());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(Company.supply);
				cell.setCellStyle(style);
				// 第四步，创建单元格，并设置值

			}

		}
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ StringUtill.toUtf8String(name) + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

			String sql = OrderMessageManager.billingprint(name);
			DBUtill.sava(sql);
			// logger.info(123);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportOrderGoodsGM(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
 
		String name = request.getParameter("name");
		String ids = request.getParameter("ids");
		String branchtype = request.getParameter("branchtype");
		String statue = request.getParameter("statues");
		String typestatues = request.getParameter("typestatues");
 
		// logger.info(name);
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,
				OrderMessage.billing, name, ids);
       /* Map<String,List<OrderGoodsAll> > map = new HashMap<String,List<OrderGoodsAll>>();
        
        for(int i=0;i<listAll.size();i++){
        	OrderGoodsAll oa = listAll.get(i);
        	
        	//OrderGoodsAll o = list.get(i);
			Branch branch = o.getOm().getBranch();
			List<OrderGoods> listog = o.getList();
        	
        	
        }*/
        
		// 第一步，创建一个webbook，对应一个Excel文件 
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单");

		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints(30);
		sheet.setColumnWidth(1, 20 * 256);

		// 第三步，在sheet中添加表头第0行,注意 老版本poi对Excel的行数列数有限制short
		int count = 0;
		HSSFRow row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));

		// row.setRowStyle(style);
		HSSFCell cell = null;
		count++;
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		row = sheet.createRow((int) count);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		count++;
		int x = 0;
		cell = row.createCell((short) x++);
		cell.setCellValue("供应商代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货商品编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货商品名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货门店");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货门店编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("库区代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("定货库区（特价 正常）");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			OrderGoodsAll o = list.get(i);
			Branch branch = o.getOm().getBranch();
			List<OrderGoods> listog = o.getList();

			for (int j = 0; j < listog.size(); j++) {
				OrderGoods og = listog.get(j);
                String message = "";
                if(og.getStatues() == 1){
                	message = "正常";
                }else if(og.getStatues() ==2){
                	message = "一步到位机";
                } 
				row = sheet.createRow((int) count);
				count++;
				int y = 0;
				cell = row.createCell((short) y++);
				cell.setCellValue(Company.supplyGM);
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getType());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(branch.getNameSN());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(branch.getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(branch.getReservoir());
				cell.setCellStyle(style); 
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getBranch());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				// logger.info(og.getOrdernum());
				cell.setCellValue(og.getOrdernum());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				// logger.info(og.getOrdernum());
				cell.setCellValue(og.getOrdernum());
				cell.setCellStyle(style);
				// 第四步，创建单元格，并设置值

			}

		}
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ StringUtill.toUtf8String(name) + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

			String sql = OrderMessageManager.billingprint(name);
			DBUtill.sava(sql);
			// logger.info(123);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportOrderGoodsreturnSN(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		String name = request.getParameter("name");
		String ids = request.getParameter("ids");
		// logger.info(name);
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user,
				OrderMessage.billing, name, ids);

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("退货订单");
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints(30);
		sheet.setColumnWidth(1, 20 * 256);

		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		// sheet.setColumnWidth((short)200,(short)1000);
		// row.setHeight((short)height);
		// style.set

		// 背景为红色
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
		style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setWrapText(true);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setWrapText(true);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		Font font = wb.createFont();
		// font.setFontHeightInPoints((short)24); //字体大小
		// font.setFontName("楷体");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD); //粗体
		font.setColor(HSSFColor.RED.index); // 绿字
		style2.setFont(font);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.RED.index); // 设置颜色为红色
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setFillForegroundColor(HSSFColor.YELLOW.index); // 设置颜色为红色
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setWrapText(true);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		// 第三步，在sheet中添加表头第0行,注意 老版本poi对Excel的行数列数有限制short
		int count = 0;
		HSSFRow row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));

		// row.setRowStyle(style);
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		HSSFCell cell = null;
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style1);
				cell.setCellValue("苏宁电器生活电器退单统一模板");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style1);
				cell.setCellValue("");
			}
		}

		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("请按照表格要求认真填写订单明细，填写完毕后请发送至,");
				cell.setCellValue(cell.getStringCellValue()
						+ "qiangrc@cnsuning.com");
				cell.setCellValue(cell.getStringCellValue()
						+ ",并电话通知相应品管确认邮件是否发送成功。每周二、周四做订单，周二上午12点以前收到的邮件周二完成，12点以后收到的邮件周四完成");

			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellValue("填写注意事项，如需退某型号全门店库存无需填写退货数量，请明确商品编码，注明全门店，并通知门店停止销售，避免退单数量不符重复工作。一定要明确退货日期，在规定的日期完成退货流程。退单不予延期。");
				cell.setCellStyle(style);
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("每次填写请核实填写内容，明细表格填写是否完整，退货数量一定要准确。");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}
		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (3 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("请代理商配合我部门工作，我们收到邮件后会尽快为您制单，尽量一单给您！请代理商关注自己SCS平台自行查询退单编号，校验码，及最终退货数量，并按退单明细在规定日期内完成退货流程");
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style);
				cell.setCellValue("");
			}
		}

		sheet.addMergedRegion(new Region(count, (short) 0, count, (short) 2));
		sheet.addMergedRegion(new Region(count, (short) 5, count, (short) 6));
		row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));
		count++;
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("原因:");
			} else if (i == 4) {
				cell = row.createCell((short) i);
				cell.setCellValue("申请人:");
				cell.setCellStyle(style2);
			} else if (i == 5) {
				cell = row.createCell((short) i);
				cell.setCellValue("联系电话:");
				cell.setCellStyle(style2);
			} else {
				cell = row.createCell((short) i);
				cell.setCellStyle(style2);
				cell.setCellValue("");
			}
		}

		row = sheet.createRow((int) count);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		count++;
		int x = 0;
		cell = row.createCell((short) x++);
		cell.setCellValue("商品编码");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品名称");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货数量");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("订货门店编码");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("库位");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("日期");
		cell.setCellStyle(style3);
		cell = row.createCell((short) x++);
		cell.setCellValue("供应商编码");
		cell.setCellStyle(style3);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			OrderGoodsAll o = list.get(i);
			Branch branch = o.getOm().getBranch();
			List<OrderGoods> listog = o.getList();

			for (int j = 0; j < listog.size(); j++) {
				OrderGoods og = listog.get(j);
				String serialnumber = og.getSerialnumber();
				if (StringUtill.isNull(serialnumber)) {
					serialnumber = Company.supply;
				}
				row = sheet.createRow((int) count);
				count++;
				int y = 0;
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getProduct().getType());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getRealnum());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(branch.getEncoded());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(og.getBranch());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(TimeUtill.getdateString());
				cell.setCellStyle(style);
				cell = row.createCell((short) y++);
				cell.setCellValue(serialnumber);
				cell.setCellStyle(style);
				// 第四步，创建单元格，并设置值

			}

		}
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ StringUtill.toUtf8String(name) + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();

			String sql = OrderMessageManager.billingprint(name);
			DBUtill.sava(sql);
			// logger.info(123);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void OrderGoodssend(HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");

		String ids = request.getParameter("ids");
		String statues = request.getParameter("statues");
		// logger.info(name);
		// List<OrderGoodsAll> list =
		// OrderGoodsAllManager.getsendlist(user,OrderMessage.unexamine,ids);
		Map<Integer, Map<Integer, OrderGoodsAll>> map = OrderGoodsAllManager
				.getsendMap(user, Integer.valueOf(statues), ids);

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单");
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints(30);
		sheet.setColumnWidth(1, 20 * 256);

		HSSFCellStyle style = wb.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		// sheet.setColumnWidth((short)200,(short)1000);
		// row.setHeight((short)height);
		// style.set

		// 第三步，在sheet中添加表头第0行,注意 老版本poi对Excel的行数列数有限制short
		int count = 0;
		HSSFRow row = sheet.createRow((int) count);
		row.setHeight((short) (2 * 256));
		HSSFCell cell = null;
		count++;
		int x = 0;

		cell = row.createCell((short) x++);
		cell.setCellValue("门店");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品编码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品条码");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品全名");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("商品单位");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("单价");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) x++);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到

		if (null != map) {
			Set<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> set = map
					.entrySet();
			Iterator<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> it = set
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Map<Integer, OrderGoodsAll>> mapent = it
						.next();
				Map<Integer, OrderGoodsAll> mapb = mapent.getValue();
				Set<Map.Entry<Integer, OrderGoodsAll>> setb = mapb.entrySet();
				Iterator<Map.Entry<Integer, OrderGoodsAll>> itb = setb
						.iterator();
				while (itb.hasNext()) {
					Map.Entry<Integer, OrderGoodsAll> mapentb = itb.next();
					OrderGoodsAll o = mapentb.getValue();
					Branch branch = o.getOm().getBranch();
					List<OrderGoods> listog = o.getList();

					for (int j = 0; j < listog.size(); j++) {
						OrderGoods og = listog.get(j);

						row = sheet.createRow((int) count);
						if (j == 0) {
							// logger.info(count+"___"+(count+listog.size()-1));
							sheet.addMergedRegion(new Region(count, (short) 9,
									count + listog.size() - 1, (short) 9));
						}

						count++;
						int y = 0;
						cell = row.createCell((short) y++);
						cell.setCellValue(branch.getLocateName());
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue(og.getProduct().getType());
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue(og.getRealnum());
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue("");
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue(og.getStatuesName());
						cell.setCellStyle(style);
						cell = row.createCell((short) y++);
						cell.setCellValue(StringUtill.getNotNUll(og.getOid()));
						cell.setCellStyle(style);
						if (j == 0) {
							cell = row.createCell((short) y++);

							cell.setCellValue(o.getOm().getRemark());
							cell.setCellStyle(style);
						} else {
							cell = row.createCell((short) y++);

							cell.setCellValue("");
							cell.setCellStyle(style);
						}

						// 第四步，创建单元格，并设置值

					}
				}
			}

		}
		// System.out.println(count);
		// 第六步，将文件存到指定位置
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + StringUtill.toUtf8String("开单")
							+ TimeUtill.getdateString() + ".xls\"");
			// FileOutputStream fout = new
			// FileOutputStream("E:/报装单"+printlntime+".xls");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();
			if ("0".equals(statues)) {
				List<String> listsql = new ArrayList<String>();
				// String sqlsend =
				String sql = OrderMessageManager.sendprint(ids);
				listsql.add(sql);
				List<String> sqlinventory = OrderGoodsAllManager
						.updateSendcount(user, map);
				listsql.addAll(sqlinventory);
				DBUtill.sava(listsql);
			}

			// logger.info(123);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void OrderGoodsbilling(HttpServletRequest request,
			HttpServletResponse response) {
		String uuid = TimeUtill.gettimeString();
		String name = request.getParameter("name");
		String ids = request.getParameter("ids");
		String branchtype = request.getParameter("branchtype");
		BranchType bt = BranchTypeManager
				.getLocate(Integer.valueOf(branchtype));
		logger.info(bt.getExportmodel());
		String statue = request.getParameter("statues");
		String typestatues = request.getParameter("typestatues");
		if ("2".equals(typestatues)) {
			// 忽略
			String sql = OrderMessageManager.billing(ids, statue);
			DBUtill.sava(sql);
		} else {
			String sql = OrderMessageManager.billing(name, ids, statue,
					branchtype);
			// logger.info(typestatues);
			if (DBUtill.sava(sql)) {
				if ("0".equals(typestatues)) {
					if (ExportModel.SuNing == bt.getExportmodel()) {
						exportOrderGoodsSN(request, response);
					} else if (ExportModel.GuoMei == bt.getExportmodel()) {
						exportOrderGoodsGM(request, response);
					}

				} else if ("1".equals(typestatues)) {
					exportOrderGoodsreturnSN(request, response);
				} else {
					try {
						response.sendRedirect("jieguo.jsp?type=type=updated&mark=" + 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 调用核心业务类接收消息、处理消息
		doGet(request, response);

		// 响应消息
	}

}
