package servlet;

import group.Group;
import group.GroupService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;
import category.CategoryManager;


import user.User;
import user.UserManager;

import utill.TokenGen;


 
/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class Juese_addServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(RegistServlet.class);
	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Object object = new Object();
		synchronized(object){
			saveUser(request,response);
		}
	}
	
	private void saveUser(HttpServletRequest request, HttpServletResponse response){
		  
			 String ptype = request.getParameter("ptype");   
			 String typeStr = request.getParameter("type"); 
			 int type = Integer.valueOf(typeStr);
			 List<Group>  listg = null ;
			 Map<String,List<Group>> map = GroupService.getPidMap();
			 List<Category> list =CategoryManager.getCategory(Category.sale) ;   
              
			 if(type == Group.Manger && null != map){
	        	 listg = map.get(1+"");
			 }else if(type == Group.dealSend && null != map){
	        	 listg = map.get(1+"");
			 }else if(type == Group.sencondDealsend && null != map){ 
		        	listg = map.get(2+"");
			 }else if(type == Group.sale && null != map){
	        	 listg = map.get(2+"");
			 }else if(type == Group.send && null != map) {
	        	 listg = map.get(3+"");
			 }else {
				 listg = GroupService.getList();
			 }
			try {
				if(null == list){

					response.sendRedirect("../remindme.jsp?type=1");
					return ;
				}
				if(null == listg){
					response.sendRedirect("../remindme.jsp?type="+type); 
					return ;
				}
				response.sendRedirect("juese_add.jsp?ptype="+ptype+"&type="+type);
				return ;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
 
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);
 
		// 响应消息
	}
    
}
