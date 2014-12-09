package order;

import group.Group;

import installsale.InstallSale;
import installsale.InstallSaleManager;
import installsale.InstallSaleMessage;
import installsale.InstallSaleMessageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.renderkit.AttributeManager.Key;

import category.CategoryService;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import user.User;
import user.UserManager;
import user.UserService;
import utill.StringUtill;

import branch.BranchService;

public class OrderService {
	 
	protected static Log logger = LogFactory.getLog(OrderService.class);
	 
	public static String getBranchName(int id){
		if(BranchService.getMap().containsKey(id)){
			return BranchService.getMap().get(id).getLocateName();
		}else{
			return "";
		}
	}
	 
	public static String getHtmlOver(User user ,List<Order> list){
		 
		int price = 0 ; 
		StringBuffer html = new StringBuffer();  
		Map<String,Map<String,Map<String,List<Order>>>> mapr = new HashMap<String,Map<String,Map<String,List<Order>>>>(); 
		// uid , phone , andate , uname,locate  
		Map<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>> orders = new HashMap<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>();
		
		Map<String,InstallSale>  mapin = InstallSaleManager.getmap();
		Map<String,List<InstallSaleMessage>> mapinsa = InstallSaleMessageManager.getmap();
		
		//List<OrderProduct> listopid = new ArrayList<OrderProduct>();
		List<Order> listopid = new ArrayList<Order>();
		//logger.info(list.size());
		for(int i=0;i<list.size();i++){
			Order o = list.get(i);
			InstallSale en = mapin.get(o.getDealsendId()+"");
			if(null == en){
				en = mapin.get("-1");
			}
			if(null != en){
				Map<String,Map<String,Map<String,Map<String,List<Order>>>>> umap = orders.get(o.getDealsendId()+"");
				if(null == umap){
					umap = new HashMap<String,Map<String,Map<String,Map<String,List<Order>>>>>();
					orders.put(o.getDealsendId()+"", umap);
				}
				//logger.info(orders); 
				Map<String,Map<String,Map<String,List<Order>>>> phonemap = null ;
				if(1 == en.getPhone()){
					phonemap = umap.get(o.getPhone1()) ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put(o.getPhone1(), phonemap);
					}
				}else { 
					phonemap = umap.get("same") ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put("same", phonemap);
					}
					
				}
				
				Map<String,Map<String,List<Order>>> andatemap = null ;
				
				if(1 == en.getAndate()){
					andatemap = phonemap.get(o.getOdate()) ;
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put(o.getOdate(), andatemap);
					}
				}else {   
					andatemap = phonemap.get("same");
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put("same", andatemap);
					}  
					
				}
				
				Map<String,List<Order>> unamemap = null ;
				if(1 == en.getUname()){
					unamemap = andatemap.get(o.getUsername()) ;
					if(null == unamemap){ 
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put(o.getUsername(),unamemap);
					} 
				}else {   
					unamemap = andatemap.get("same") ;
					if(null == unamemap){
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put("same", unamemap);
					}  
					
				}
				
				List<Order> order = null ;
				if(1 == en.getLocate()){
					order = unamemap.get(o.getLocateDetail());
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put(o.getLocateDetail(), order);
					}
					order.add(o);
				}else { 
					order = unamemap.get("same");
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put("same", order);
					} 
					order.add(o); 
				}
			}
		} 
		
		//logger.info(orders);
		 int x = 1 ;
		 Set<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> sets = orders.entrySet();
		 Iterator<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> its = sets.iterator();
		
		 while(its.hasNext()){
			Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>> ents = its.next();
			String key = ents.getKey();
			Map<String,Map<String,Map<String,Map<String,List<Order>>>>> map = ents.getValue();
			Map<String,Map<String,List<Order>>> maprr = mapr.get(key);
			if(null == maprr){  
				maprr = new HashMap<String,Map<String,List<Order>>>();
				mapr.put(key, maprr);
				//logger.info(1);
			} 
			
			if(null != map){
				
		        Set<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> enmap =  map.entrySet();
		        Iterator<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> itmap = enmap.iterator();
			    while(itmap.hasNext()){
			    	Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>> enmapp = itmap.next();
			    	Map<String,Map<String,Map<String,List<Order>>>> mapp = enmapp.getValue(); 
			//logger.info(StringUtill.GetJson(mapp));
			    	Set<Map.Entry<String,Map<String,Map<String,List<Order>>>>> setmapa = mapp.entrySet();
			    	Iterator<Map.Entry<String,Map<String,Map<String,List<Order>>>>> itmapa = setmapa.iterator();
			    	while(itmapa.hasNext()){
			    		Map.Entry<String,Map<String,Map<String,List<Order>>>> enmapa = itmapa.next();
			    		Map<String,Map<String,List<Order>>> mapa = enmapa.getValue();
			//logger.info(StringUtill.GetJson(mapa));
			    		Set<Map.Entry<String,Map<String,List<Order>>>> seta = mapa.entrySet();
			    		Iterator<Map.Entry<String,Map<String,List<Order>>>> ita = seta.iterator();
			    		while(ita.hasNext()){
			    			Map.Entry<String,Map<String,List<Order>>> enitu = ita.next();
			    			Map<String,List<Order>> mapu = enitu.getValue();
			  // logger.info(StringUtill.GetJson(mapu));   			
			    			Set<Map.Entry<String,List<Order>>> setu = mapu.entrySet();
				    		Iterator<Map.Entry<String,List<Order>>> itu = setu.iterator();
				    		
				    		while(itu.hasNext()){
				    			Map.Entry<String,List<Order>> enitl = itu.next();
				    			List<Order> listo = enitl.getValue();
				    		
			//logger.info(StringUtill.GetJson(listo));   
			//logger.info(listo.size());
				    			if(listo.size()>1){
				    				x = x +1 ; 
				    				InstallSale sas = mapin.get(key);
				    				if( null == sas){
				    					sas = mapin.get("-1");
				    				} 
				    				//logger.info(x);
				    				//logger.info(StringUtill.GetJson(mapinsa));
				    				//logger.info(key);
				    				//logger.info(mapinsa);
				    				 List<InstallSaleMessage> lists = mapinsa.get(sas.getId()+"");
				    				 //logger.info(lists);
				    				 if(null != lists){
				    				   Iterator<InstallSaleMessage> itss = lists.iterator(); 
				    				//String message = in.getMessage();
				    				//JSONObject jsObj = JSONObject.fromObject(message); 
				    				//Iterator<String> keys=jsObj.keys();  
					    				while(itss.hasNext()){
					    					//String ke = keys.next(); 
					    					//String valu = jsObj.getString(ke);
					    					InstallSaleMessage ins = itss.next();
					    					String ke = ins.getCategoryID()+"";
					    					if(ke.contains("_")){
						    					//logger.info(ke);
					    						int type = -1; 
					    						for(int i=0;i<listo.size();i++){
					    							Order o = listo.get(i);
					    							List<OrderProduct> listop = o.getOrderproduct();
					    							if(listop.size() == 1){
					    								for(int j=0;j<listop.size();j++){
						    								OrderProduct op = listop.get(j);
						    								//int ctype = CategoryService.getmap().get(op.getCategoryId()).getPid();
						    								//logger.info(op.getCategoryId()); 
						    								if(ke.contains(op.getCategoryId()+"")){ 
						    									//Map<String,Map<String,Map<Order,List<OrderProduct>>>>
						    									Map<String,List<Order>> mapopp= maprr.get(ke);
						    									if(null == mapopp){
						    										mapopp = new HashMap<String,List<Order>>();
						    										maprr.put(ke, mapopp);
						    									} 
						    									
						    									List<Order> mapi = mapopp.get(x+"-"+ke);
						    									  
						    									if(null == mapi){
						    										mapi = new ArrayList<Order>();
						    										mapopp.put(x+"-"+ke, mapi);
						    										//logger.info(x);
						    									}
						    									
						    									if(op.getCategoryId() != type){
						    										type = op.getCategoryId();
						    										//list.remove(i);
						    										mapi.add(o);   
						    									}
						    								}
						    							}
					    							}
					    						}
					    					}
					    				}
				    			  }
				    			}
				    			
				    		}
			    			
			    		}
			    	}
			    	
			    }
			}
			
			
			
	
		}

		
		
		
		
		
		
		
		
		
		
		/*logger.info(StringUtill.GetJson(mapin));
		
		Set<Map.Entry<String,InstallSale>> set = mapin.entrySet();

		Iterator<Map.Entry<String,InstallSale>> it = set.iterator();
		
		while(it.hasNext()){
			Map.Entry<String,InstallSale> en = it.next();
			String key = en.getKey();
			//logger.info(key); 
			InstallSale in = en.getValue();
			//Map<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>
			Map<String,Map<String,List<Order>>> maprr = mapr.get(key);
			if(null == maprr){  
				maprr = new HashMap<String,Map<String,List<Order>>>();
				mapr.put(key, maprr);
				//logger.info(1);
			} 
			int x = 0 ;  
			Map<String,Map<String,Map<String,Map<String,List<Order>>>>> map = orders.get(key);
			if(null != map){
				
			
		        Set<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> enmap =  map.entrySet();
		        Iterator<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> itmap = enmap.iterator();
			    while(itmap.hasNext()){
			    	Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>> enmapp = itmap.next();
			    	Map<String,Map<String,Map<String,List<Order>>>> mapp = enmapp.getValue(); 
			//logger.info(StringUtill.GetJson(mapp));
			    	Set<Map.Entry<String,Map<String,Map<String,List<Order>>>>> setmapa = mapp.entrySet();
			    	Iterator<Map.Entry<String,Map<String,Map<String,List<Order>>>>> itmapa = setmapa.iterator();
			    	while(itmapa.hasNext()){
			    		Map.Entry<String,Map<String,Map<String,List<Order>>>> enmapa = itmapa.next();
			    		Map<String,Map<String,List<Order>>> mapa = enmapa.getValue();
			//logger.info(StringUtill.GetJson(mapa));
			    		Set<Map.Entry<String,Map<String,List<Order>>>> seta = mapa.entrySet();
			    		Iterator<Map.Entry<String,Map<String,List<Order>>>> ita = seta.iterator();
			    		while(ita.hasNext()){
			    			Map.Entry<String,Map<String,List<Order>>> enitu = ita.next();
			    			Map<String,List<Order>> mapu = enitu.getValue();
			  // logger.info(StringUtill.GetJson(mapu));   			
			    			Set<Map.Entry<String,List<Order>>> setu = mapu.entrySet();
				    		Iterator<Map.Entry<String,List<Order>>> itu = setu.iterator();
				    		
				    		while(itu.hasNext()){
				    			Map.Entry<String,List<Order>> enitl = itu.next();
				    			List<Order> listo = enitl.getValue();
				    		
			//logger.info(StringUtill.GetJson(listo));   
			//logger.info(listo.size());
				    			if(listo.size()>1){
				    				x = x +1 ; 
				    				//logger.info(x);
				    				//logger.info(StringUtill.GetJson(mapinsa));
				    				//logger.info(in.getId());
				    				 List<InstallSaleMessage> lists = mapinsa.get(in.getId()+"");
				    				 
				    				 if(null != lists){
				    				   Iterator<InstallSaleMessage> its = lists.iterator(); 
				    				//String message = in.getMessage();
				    				//JSONObject jsObj = JSONObject.fromObject(message); 
				    				//Iterator<String> keys=jsObj.keys();  
					    				while(its.hasNext()){
					    					//String ke = keys.next();
					    					//String valu = jsObj.getString(ke);
					    					InstallSaleMessage ins = its.next();
					    					String ke = ins.getCategoryID()+"";
					    					if(ke.contains("_")){
						    					//logger.info(ke);
					    						int type = -1; 
					    						for(int i=0;i<listo.size();i++){
					    							Order o = listo.get(i);
					    							List<OrderProduct> listop = o.getOrderproduct();
					    							if(listop.size() == 1){
					    								for(int j=0;j<listop.size();j++){
						    								OrderProduct op = listop.get(j);
						    								//int ctype = CategoryService.getmap().get(op.getCategoryId()).getPid();
						    								//logger.info(op.getCategoryId()); 
						    								if(ke.contains(op.getCategoryId()+"")){ 
						    									//Map<String,Map<String,Map<Order,List<OrderProduct>>>>
						    									Map<String,List<Order>> mapopp= maprr.get(ke);
						    									if(null == mapopp){
						    										mapopp = new HashMap<String,List<Order>>();
						    										maprr.put(ke, mapopp);
						    									} 
						    									
						    									List<Order> mapi = mapopp.get(x+"-"+ke);
						    									  
						    									if(null == mapi){
						    										mapi = new ArrayList<Order>();
						    										mapopp.put(x+"-"+ke, mapi);
						    										//logger.info(x);
						    									}
						    									
						    									if(op.getCategoryId() != type){
						    										type = op.getCategoryId();
						    										list.remove(i);
						    										mapi.add(o);   
						    									}
						    								}
						    							}
					    							}
					    						}
					    					}
					    				}
				    			  }
				    			}
				    			
				    		}
			    			
			    		}
			    	}
			    	
			    }
			}
		}*/
		// 如果不需要相同。默认为same
		//logger.info(StringUtill.GetJson(orders));
		//logger.info(mapr.size());   
		//logger.info(mapr); 
		  
		Set<Map.Entry<String,Map<String,Map<String,List<Order>>>>>  setop = mapr.entrySet();
		Iterator<Map.Entry<String,Map<String,Map<String,List<Order>>>>> itop = setop.iterator();
		while(itop.hasNext()){
			Map.Entry<String,Map<String,Map<String,List<Order>>>> opp = itop.next();
			Map<String,Map<String,List<Order>>> mapop = opp.getValue();
			//logger.info(mapop);
			Set<Map.Entry<String,Map<String,List<Order>>>> setopp = mapop.entrySet();
			Iterator<Map.Entry<String,Map<String,List<Order>>>> itopp = setopp.iterator();
			//int x = 0 ;
			while(itopp.hasNext()){ 
				Map.Entry<String,Map<String,List<Order>>> listop = itopp.next();
				Map<String,List<Order>> listoo = listop.getValue();
				//logger.info(listoo);
				Set<Map.Entry<String,List<Order>>> seten =  listoo.entrySet();
				Iterator<Map.Entry<String,List<Order>>> iten =  seten.iterator();
				while(iten.hasNext()){
					Map.Entry<String,List<Order>> enit = iten.next();
					//String keystr = enit.getKey();
					//String[] keys = keystr.split("-"); 
					//String key = keys[1];   
					List<Order> listo = enit.getValue();
				     
					int count = listo.size();
					//logger.info("count"+count);
					String order = "";
	    			if(count > 1){
	    				for(int i=0;i<listo.size();i++){
		    				order += listo.get(i).getId()+"_";
		    			}
	    				order = order.substring(0,order.length()-1);
	    			}
	    			
					int y = 0 ;
					if(count > 1){  
						for(int i=0;i<listo.size();i++){
							Order o = listo.get(i);
							listopid.add(o);  
								y++;
								String tdcol = " bgcolor=\"red\"" ;
								if(o.getPhoneRemark()!=1){
									tdcol = ""; 
								}
					    		
								html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
								html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  name=\"orderid\" value="+o.getId()+ "></input></td>");
								html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
								html.append("<td align=\"center\"><input type=\"hidden\" name=\"dealsendid\"  value=\""+o.getDealsendId()+"\"/>"+o.getdealsendName()+"</td>");
								if(o.getPhoneRemark()!=1){    
									tdcol = ""; 
								}
								html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
								html.append("<td align=\"center\">"+o.getCategory(0, "</p>")+"</td> ");
								html.append("<td align=\"center\" >"+o.getSendType(0, "</p>")+"</td>  ");
								html.append("<td align=\"center\" >"+o.getSendCount(0, "</p>")+"</td> ");
								html.append("<td align=\"center\">"+o.getLocate()+"</td>");
								html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
								html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
								html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
								 
								if(y == 1){
									InstallSale in = null ;
									String value = "";
									 if(null != mapin){
										 in = mapin.get(o.getDealsendId()+"");
										 if(null == in){    
											 in = mapin.get(-1+"");
										 }
										 if(null != in){ 
											 List<InstallSaleMessage> lists = mapinsa.get(in.getId()+"");
											 /*for(int m=0;m<lists.size();m++){
												 InstallSaleMessage ins = lists.get(m);
												 if(key.equals(ins.getCategoryID())){
													 value = ins.getDealsend()+"";
												 } 
											 }
											 String message = in.getMessage();
											 JSONObject jsObj = JSONObject.fromObject(message);
											logger.info(key);
											 try{ 
												 value = jsObj.getString(key);
											 }catch(Exception e){
												 value = "";
											 } */ 
											 value = InstallSaleMessageManager.getprice(lists, o)+""; 	
											 
										 }
									 }  
									 if(!StringUtill.isNull(value)){
										 price += Integer.valueOf(value);
									 }
									 String color = "#E8E8D0";
									 if(count >1){
										 color = "red";
									 }else { 
										 order = o.getId()+"";
									 }
									html.append("<td align=\"center\"  style=\"background-color:"+color+"\" rowspan="+count + "> " +
									            "<input type=\"hidden\" name=\"saleresut\"  value=\""+order+"\"/>  "+
					                            "<input type=\"text\" id="+order+"left value=\""+value+"\"  style=\"border:0; width:80px\"/>"+
					                            "</td>"); 
								} 
						 }
					}
						
						//listopid.add(lis)
				}
				
				
			}
		}
		//logger.info(listopid)
		
		if(null != list){ 
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	//List<OrderProduct> listop = o.getOrderproduct();
				 
				//for(int m=0;m<listop.size();m++){
					//OrderProduct op = listop.get(m);
					
					boolean found = listopid.contains(o); 
					if(!found){
					//	if(op.getStatues() == 0){
							String tdcol = " bgcolor=\"red\"" ;
							if(o.getPhoneRemark()!=1){
								tdcol = ""; 

							}
				    		
							html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
							html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  name=\"orderid\" value="+o.getId()+ "></input></td>");
							html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
							html.append("<td align=\"center\"><input type=\"hidden\" name=\"dealsendid\"  value=\""+o.getDealsendId()+"\"/>"+o.getdealsendName()+"</td>");
							if(o.getPhoneRemark()!=1){   
								tdcol = ""; 
							}
							html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
							html.append("<td align=\"center\">"+o.getCategory(0, "</p>")+"</td> ");
							html.append("<td align=\"center\" >"+o.getSendType(0, "</p>")+"</td>  ");
							html.append("<td align=\"center\" >"+o.getSendCount(0, "</p>")+"</td> ");
							html.append("<td align=\"center\">"+o.getLocate()+"</td>");
							html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
							html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
							html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
							
							InstallSale in = null ;
							String value = "";
							 if(null != mapin){
								 in = mapin.get(o.getDealsendId()+"");
								 if(null == in){     
									 in = mapin.get(-1+"");
								 }
							
								 if(null != in){
									 
									 List<InstallSaleMessage> lists = mapinsa.get(in.getId()+"");
									 
									 value = InstallSaleMessageManager.getprice(lists, o)+""; 	 								 /*String message = in.getMessage();
									 JSONObject jsObj = JSONObject.fromObject(message); 
									 try{
										 value = jsObj.getString(o.getCategoryid(0, ""));
									 }catch(Exception e){
										 value = "";
									 }*/
								 }
							 }
							 if(!StringUtill.isNull(value)){
								 price += Integer.valueOf(value);
							 } 
							  
							html.append("<td align=\"center\"  style=\"background-color:#E8E8D0\">"+
									    "<input type=\"hidden\" name=\"saleresut\"  value=\""+o.getId()+"\"/>  "+
									    "<input type=\"text\" id="+o.getId()+"left value=\""+value+"\"  onBlur=\"addcount()\"  style=\"border:0; width:80px\"/>"+
			                            "</td>");
			 
					 }
					//}
				
				//}
		    }
		}  
		html.append("</tr>"); 
		html.append("<tr class=\"asc\" >"); 
		html.append("<td align=\"center\" colspan=10 ></td> ");
		html.append("<td align=\"center\" >合计金额</td> ");
		html.append("<td align=\"center\" ><span style=\"color:red;font-size:20px;\" id=\"addcount\">"+price+"</span></td> ");
		html.append("</tr>"); 
		//logger.info(html.toString());
		return html.toString();
	}
    
	
	
	/*public static String getHtmlOver(User user ,List<Order> list){
		 
		int price = 0 ; 
		StringBuffer html = new StringBuffer();  
		Map<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>> mapr = new HashMap<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>(); 
		// uid , phone , andate , uname,locate  
		Map<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>> orders = new HashMap<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>();
		
		Map<String,InstallSale>  mapin = InstallSaleManager.getmap();
		
		List<OrderProduct> listopid = new ArrayList<OrderProduct>();
		 
		for(int i=0;i<list.size();i++){
			Order o = list.get(i);
			InstallSale en = mapin.get(o.getDealsendId()+"");
			if(null != en){
				Map<String,Map<String,Map<String,Map<String,List<Order>>>>> umap = orders.get(o.getDealsendId()+"");
				if(null == umap){
					umap = new HashMap<String,Map<String,Map<String,Map<String,List<Order>>>>>();
					orders.put(o.getDealsendId()+"", umap);
				}
				 
				Map<String,Map<String,Map<String,List<Order>>>> phonemap = null ;
				if(1 == en.getPhone()){
					phonemap = umap.get(o.getPhone1()) ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put(o.getPhone1(), phonemap);
					}
				}else { 
					phonemap = umap.get("same") ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put("same", phonemap);
					}
					
				}
				
				Map<String,Map<String,List<Order>>> andatemap = null ;
				
				if(1 == en.getAndate()){
					andatemap = phonemap.get(o.getOdate()) ;
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put(o.getOdate(), andatemap);
					}
				}else {   
					andatemap = phonemap.get("same");
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put("same", andatemap);
					}  
					
				}
				
				Map<String,List<Order>> unamemap = null ;
				if(1 == en.getUname()){
					unamemap = andatemap.get(o.getUsername()) ;
					if(null == unamemap){ 
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put(o.getUsername(),unamemap);
					} 
				}else {   
					unamemap = andatemap.get("same") ;
					if(null == unamemap){
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put("same", unamemap);
					}  
					
				}
				
				List<Order> order = null ;
				if(1 == en.getLocate()){
					order = unamemap.get(o.getLocateDetail());
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put(o.getLocateDetail(), order);
					}
					order.add(o);
				}else { 
					order = unamemap.get("same");
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put("same", order);
					} 
					order.add(o); 
				}
			}
		} 
		
		Set<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> sets = orders.entrySet();
		 Iterator<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> its = sets.iterator();
		while(its.hasNext()){
			Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>> ents = its.next();
			Map<String,Map<String,Map<String,Map<String,List<Order>>>>> mas = ents.getValue();
			logger.info(StringUtill.GetJson(mas)); 
		}
		  
		
		Set<Map.Entry<String,InstallSale>> set = mapin.entrySet();

		Iterator<Map.Entry<String,InstallSale>> it = set.iterator();
		
		while(it.hasNext()){
			Map.Entry<String,InstallSale> en = it.next();
			String key = en.getKey();
			//logger.info(key); 
			InstallSale in = en.getValue();
			//Map<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>
			Map<String,Map<String,Map<Order,List<OrderProduct>>>> maprr = mapr.get(key);
			if(null == maprr){  
				maprr = new HashMap<String,Map<String,Map<Order,List<OrderProduct>>>>();
				mapr.put(key, maprr);
				//logger.info(1);
			} 
			int x = 0 ;  
			Map<String,Map<String,Map<String,Map<String,List<Order>>>>> map = orders.get(key);
			if(null != map){
				
			
		        Set<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> enmap =  map.entrySet();
		        Iterator<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> itmap = enmap.iterator();
			    while(itmap.hasNext()){
			    	Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>> enmapp = itmap.next();
			    	Map<String,Map<String,Map<String,List<Order>>>> mapp = enmapp.getValue(); 
			//logger.info(StringUtill.GetJson(mapp));
			    	Set<Map.Entry<String,Map<String,Map<String,List<Order>>>>> setmapa = mapp.entrySet();
			    	Iterator<Map.Entry<String,Map<String,Map<String,List<Order>>>>> itmapa = setmapa.iterator();
			    	while(itmapa.hasNext()){
			    		Map.Entry<String,Map<String,Map<String,List<Order>>>> enmapa = itmapa.next();
			    		Map<String,Map<String,List<Order>>> mapa = enmapa.getValue();
			//logger.info(StringUtill.GetJson(mapa));
			    		Set<Map.Entry<String,Map<String,List<Order>>>> seta = mapa.entrySet();
			    		Iterator<Map.Entry<String,Map<String,List<Order>>>> ita = seta.iterator();
			    		while(ita.hasNext()){
			    			Map.Entry<String,Map<String,List<Order>>> enitu = ita.next();
			    			Map<String,List<Order>> mapu = enitu.getValue();
			  // logger.info(StringUtill.GetJson(mapu));   			
			    			Set<Map.Entry<String,List<Order>>> setu = mapu.entrySet();
				    		Iterator<Map.Entry<String,List<Order>>> itu = setu.iterator();
				    		
				    		while(itu.hasNext()){
				    			Map.Entry<String,List<Order>> enitl = itu.next();
				    			List<Order> listo = enitl.getValue();
			//logger.info(StringUtill.GetJson(listo));   
			//logger.info(listo.size());
				    			if(listo.size()>1){
				    				x = x +1 ;
				    				//logger.info(x);
				    				String message = in.getMessage();
				    				JSONObject jsObj = JSONObject.fromObject(message); 
				    				Iterator<String> keys=jsObj.keys();  
				    				while(keys.hasNext()){
				    					String ke = keys.next();
				    					//String valu = jsObj.getString(ke);
				    					if(ke.contains("_")){
					    					//logger.info(ke);
				    						int type = -1;
				    						for(int i=0;i<listo.size();i++){
				    							Order o = listo.get(i);
				    							List<OrderProduct> listop = o.getOrderproduct();
				    							if(listop.size() == 1){
				    								for(int j=0;j<listop.size();j++){
					    								OrderProduct op = listop.get(j);
					    								//int ctype = CategoryService.getmap().get(op.getCategoryId()).getPid();
					    								//logger.info(op.getCategoryId()); 
					    								if(ke.contains(op.getCategoryId()+"")){ 
					    									//Map<String,Map<String,Map<Order,List<OrderProduct>>>>
					    									Map<String,Map<Order,List<OrderProduct>>> mapopp= maprr.get(ke);
					    									if(null == mapopp){
					    										mapopp = new HashMap<String,Map<Order,List<OrderProduct>>>();
					    										maprr.put(ke, mapopp);
					    									} 
					    									
					    									Map<Order,List<OrderProduct>> mapi = mapopp.get(x+"-"+ke);
					    									  
					    									if(null == mapi){
					    										mapi = new HashMap<Order,List<OrderProduct>>();
					    										mapopp.put(x+"-"+ke, mapi);
					    										//logger.info(x);
					    									}
					    									
					    									List<OrderProduct> listopp = mapi.get(o);
					    									if(null == listopp){
					    										listopp = new ArrayList<OrderProduct>();
					    										mapi.put(o, listopp);
					    									}
					    									if(op.getCategoryId() != type){
					    										type = op.getCategoryId();
					    										listopp.add(op);  
					    										list.remove(i);  
					    									}
					    									
					    								}
					    							}
				    							}
				    						}
				    					}
				    				}
				    				
				    			}
				    			
				    		}
			    			
			    		}
			    	}
			    	
			    }
			}
		}
		// 如果不需要相同。默认为same
		//logger.info(StringUtill.GetJson(orders));
		//logger.info(mapr.size());   
		//logger.info(mapr); 
		  
		Set<Map.Entry<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>>  setop = mapr.entrySet();
		Iterator<Map.Entry<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>> itop = setop.iterator();
		while(itop.hasNext()){
			Map.Entry<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>> opp = itop.next();
			Map<String,Map<String,Map<Order,List<OrderProduct>>>> mapop = opp.getValue();
			//logger.info(mapop);
			Set<Map.Entry<String,Map<String,Map<Order,List<OrderProduct>>>>> setopp = mapop.entrySet();
			Iterator<Map.Entry<String,Map<String,Map<Order,List<OrderProduct>>>>> itopp = setopp.iterator();
			//int x = 0 ;
			while(itopp.hasNext()){
				Map.Entry<String,Map<String,Map<Order,List<OrderProduct>>>> listop = itopp.next();
				Map<String,Map<Order,List<OrderProduct>>> listoo = listop.getValue();
				//logger.info(listoo);
				Set<Map.Entry<String,Map<Order,List<OrderProduct>>>> seten =  listoo.entrySet();
				Iterator<Map.Entry<String,Map<Order,List<OrderProduct>>>> iten =  seten.iterator();
				while(iten.hasNext()){
					Map.Entry<String,Map<Order,List<OrderProduct>>> enit = iten.next();
					String keystr = enit.getKey();
					String[] keys = keystr.split("-"); 
					String key = keys[1];  
					Map<Order,List<OrderProduct>> listo = enit.getValue();
					String Order_count = getsize(listo);
					String[] ordercount = Order_count.split("-");
					int count = Integer.valueOf(ordercount[1]);
					String order = ordercount[0];
					//logger.info("count"+count);
					Set<Map.Entry<Order,List<OrderProduct>>> setlo = listo.entrySet();
					Iterator<Map.Entry<Order,List<OrderProduct>>> ito = setlo.iterator();
					int x = 0 ;
					while(ito.hasNext()){
						Map.Entry<Order,List<OrderProduct>> mapo = ito.next();
						Order o = mapo.getKey(); 
						List<OrderProduct> listooo = mapo.getValue();
					   // logger.info(x);
					    //logger.info(listo);
					for(int i=0;i<listooo.size();i++){
						OrderProduct op = listooo.get(i);
						listopid.add(op);
						if(op.getStatues() == 0){
							x++;
							String tdcol = " bgcolor=\"red\"" ;
							if(o.getPhoneRemark()!=1){
								tdcol = ""; 

							}
				    		
							html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
							html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  name=\"orderid\" value="+o.getId()+ "></input></td>");
							html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
							html.append("<td align=\"center\"><input type=\"hidden\" name=\"dealsendid\"  value=\""+o.getDealsendId()+"\"/>"+o.getdealsendName()+"</td>");
							if(o.getPhoneRemark()!=1){    
								tdcol = ""; 
							}
							html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
							html.append("<td align=\"center\">"+op.getCategoryName()+"</td> ");
							html.append("<td align=\"center\" >"+op.getTypeName()+"</td>  ");
							html.append("<td align=\"center\" >"+ op.getCount()+"</td> ");
							html.append("<td align=\"center\">"+o.getLocate()+"</td>");
							html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
							html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
							html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
							
							if(x == 1){
								InstallSale in = null ;
								String value = "";
								 if(null != mapin){
									 in = mapin.get(o.getDealsendId()+"");
									 if(null != in){
										 String message = in.getMessage();
										 JSONObject jsObj = JSONObject.fromObject(message);
										// logger.info(key);
										 value = jsObj.getString(key);
									 }
								 }  
								 if(!StringUtill.isNull(value)){
									 price += Integer.valueOf(value);
								 }
								 String color = "#E8E8D0";
								 if(count >1){
									 color = "red";  
								 }
								html.append("<td align=\"center\"  style=\"background-color:"+color+"\" rowspan="+count + "> " +
								            "<input type=\"hidden\" name=\"saleresut\"  value=\""+order+"\"/>  "+
				                            "<input type=\"text\" id="+order+"left value=\""+value+"\"  style=\"border:0; width:80px\"/>"+
				                            "</td>"); 
							} 
						}
					 }	
						//listopid.add(lis)
					}
					
				}
				
				
			}
		}
		//logger.info(listopid);
		html.append("<tr id=\"th\"  >");
		html.append("<td align=\"center\" ><input type=\"checkbox\"  id=\"allselect\" onclick=\"seletall(allselect)\"></input> </td>");
		html.append("<td align=\"center\">单号</td>");
		html.append("<td align=\"center\">安装网点</td>");
		html.append("<td align=\"center\">顾客信息</td>");
		html.append("<td align=\"center\">送货名称</td>");
		html.append("<td align=\"center\">送货型号</td>");
		html.append("<td align=\"center\">送货数量</td>");
		html.append("<td align=\"center\">送货地区</td>");
		html.append("<td align=\"center\">送货地址</td>");
		html.append("<td align=\"center\">送货状态</td>");
		html.append("<td align=\"center\">备注</td>");
		html.append("<td align=\"center\">价格</td>");
		html.append("</tr>"); 
		html.append("</tr>"); 
		
		if(null != list){ 
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	List<OrderProduct> listop = o.getOrderproduct();
				 
				for(int m=0;m<listop.size();m++){
					OrderProduct op = listop.get(m);
					
					boolean found = listopid.contains(op); 
					if(!found){
						if(op.getStatues() == 0){
							String tdcol = " bgcolor=\"red\"" ;
							if(o.getPhoneRemark()!=1){
								tdcol = ""; 

							}
				    		
							html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
							html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  name=\"orderid\" value="+o.getId()+ "></input></td>");
							html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
							html.append("<td align=\"center\"><input type=\"hidden\" name=\"dealsendid\"  value=\""+o.getDealsendId()+"\"/>"+o.getdealsendName()+"</td>");
							if(o.getPhoneRemark()!=1){   
								tdcol = ""; 
							}
							html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
							html.append("<td align=\"center\">"+op.getCategoryName()+"</td> ");
							html.append("<td align=\"center\" >"+op.getTypeName()+"</td>  ");
							html.append("<td align=\"center\" >"+ op.getCount()+"</td> ");
							html.append("<td align=\"center\">"+o.getLocate()+"</td>");
							html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
							html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
							html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
							
							InstallSale in = null ;
							String value = "";
							 if(null != mapin){
								 in = mapin.get(o.getDealsendId()+"");
								 if(null != in){
									 String message = in.getMessage();
									 JSONObject jsObj = JSONObject.fromObject(message);
									 value = jsObj.getString(op.getCategoryId()+"");
								 }
							 }
							 if(!StringUtill.isNull(value)){
								 price += Integer.valueOf(value);
							 } 
							  
							html.append("<td align=\"center\"  style=\"background-color:#E8E8D0\">"+
									    "<input type=\"hidden\" name=\"saleresut\"  value=\""+o.getId()+"\"/>  "+
									    "<input type=\"text\" id="+o.getId()+"left value=\""+value+"\"  style=\"border:0; width:80px\"/>"+
			                            "</td>");
			 
						}
					}
				
				}
		    }
		}  
		html.append("</tr>"); 
		html.append("<tr class=\"asc\" >");
		html.append("<td align=\"center\" colspan=10 ></td> ");
		html.append("<td align=\"center\" >合计金额</td> ");
		html.append("<td align=\"center\" ><span style=\"color:red;font-size:20px;\" id=\"addcount\">"+price+"</span></td> ");
		html.append("</tr>"); 
		//logger.info(html.toString());
		return html.toString();
		 
	}
    */
	
   /* public static String getHtmlOver(User user,List<OrderProduct> list){
		 
		int price = 0 ;
		
		StringBuffer html = new StringBuffer(); 
		
		Map<String,InstallSale>  mapin = InstallSaleManager.getmap();
		 
		Map<String,Map<String,Map<String,List<OrderProduct>>>> mapr = new HashMap<String,Map<String,Map<String,List<OrderProduct>>>>(); 
		Map<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>> orders = new HashMap<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>();
		//List<OrderProduct> listopid = new ArrayList<OrderProduct>();
		
		Map<String,Order> listorder = OrderManager.getByListOrderProduct(user, list); 
		
		for(int i=0;i<list.size();i++){
			OrderProduct op = list.get(i);
			Order o =listorder.get(op.getOrderid()+"");
			InstallSale en = mapin.get(o.getDealsendId()+"");
			if(null != en){
				Map<String,Map<String,Map<String,Map<String,List<Order>>>>> umap = orders.get(o.getDealsendId()+"");
				if(null == umap){
					umap = new HashMap<String,Map<String,Map<String,Map<String,List<Order>>>>>();
					orders.put(o.getDealsendId()+"", umap);
				}
				 
				Map<String,Map<String,Map<String,List<Order>>>> phonemap = null ;
				if(1 == en.getPhone()){
					phonemap = umap.get(o.getPhone1()) ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put(o.getPhone1(), phonemap);
					}
				}else { 
					phonemap = umap.get("same") ;
					if(null == phonemap){
						phonemap = new HashMap<String,Map<String,Map<String,List<Order>>>>();
						umap.put("same", phonemap);
					}
					
				}
				
				Map<String,Map<String,List<Order>>> andatemap = null ;
				
				if(1 == en.getAndate()){
					andatemap = phonemap.get(o.getOdate()) ;
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put(o.getOdate(), andatemap);
					}
				}else {   
					andatemap = phonemap.get("same");
					if(null == andatemap){
						andatemap = new HashMap<String,Map<String,List<Order>>>();
						phonemap.put("same", andatemap);
					}  
					
				}
				
				Map<String,List<Order>> unamemap = null ;
				if(1 == en.getUname()){
					unamemap = andatemap.get(o.getUsername()) ;
					if(null == unamemap){ 
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put(o.getUsername(),unamemap);
					} 
				}else {   
					unamemap = andatemap.get("same") ;
					if(null == unamemap){
						unamemap = new HashMap<String,List<Order>>();
						andatemap.put("same", unamemap);
					}  
					
				}
				
				List<Order> order = null ;
				if(1 == en.getLocate()){
					order = unamemap.get(o.getLocateDetail());
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put(o.getLocateDetail(), order);
					}
					order.add(o);
				}else { 
					order = unamemap.get("same");
					if(null == order){
						order = new ArrayList<Order>();
						unamemap.put("same", order);
					} 
					order.add(o); 
				}
			}
		} 
		
		
		Set<Map.Entry<String,InstallSale>> set = mapin.entrySet();

		Iterator<Map.Entry<String,InstallSale>> it = set.iterator();
		
		while(it.hasNext()){
			Map.Entry<String,InstallSale> en = it.next();
			String key = en.getKey();
			//logger.info(key); 
			InstallSale in = en.getValue();
			//Map<String,Map<String,Map<String,Map<Order,List<OrderProduct>>>>>
			Map<String,Map<String,List<OrderProduct>>> maprr = mapr.get(key);
			if(null == maprr){  
				maprr = new HashMap<String,Map<String,List<OrderProduct>>>();
				mapr.put(key, maprr);
				//logger.info(1);
			} 
			int x = 0 ;  
			
			
			
			Set<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> ordersset =  orders.entrySet();
			Iterator<Map.Entry<String,Map<String,Map<String,Map<String,Map<String,List<Order>>>>>>> itorders = ordersset.iterator();
			
			Map<String,Map<String,Map<String,Map<String,List<Order>>>>> map = orders.get(key);
			if(null != map){
		        Set<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> enmap =  map.entrySet();
		        Iterator<Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>>> itmap = enmap.iterator();
			    while(itmap.hasNext()){
			    	Map.Entry<String,Map<String,Map<String,Map<String,List<Order>>>>> enmapp = itmap.next();
			    	Map<String,Map<String,Map<String,List<Order>>>> mapp = enmapp.getValue(); 
			//logger.info(StringUtill.GetJson(mapp));
			    	Set<Map.Entry<String,Map<String,Map<String,List<Order>>>>> setmapa = mapp.entrySet();
			    	Iterator<Map.Entry<String,Map<String,Map<String,List<Order>>>>> itmapa = setmapa.iterator();
			    	while(itmapa.hasNext()){
			    		Map.Entry<String,Map<String,Map<String,List<Order>>>> enmapa = itmapa.next();
			    		Map<String,Map<String,List<Order>>> mapa = enmapa.getValue();
			//logger.info(StringUtill.GetJson(mapa));
			    		Set<Map.Entry<String,Map<String,List<Order>>>> seta = mapa.entrySet();
			    		Iterator<Map.Entry<String,Map<String,List<Order>>>> ita = seta.iterator();
			    		while(ita.hasNext()){
			    			Map.Entry<String,Map<String,List<Order>>> enitu = ita.next();
			    			Map<String,List<Order>> mapu = enitu.getValue();
			  // logger.info(StringUtill.GetJson(mapu));   			
			    			Set<Map.Entry<String,List<Order>>> setu = mapu.entrySet();
				    		Iterator<Map.Entry<String,List<Order>>> itu = setu.iterator();
				    		
				    		while(itu.hasNext()){
				    			Map.Entry<String,List<Order>> enitl = itu.next();
				    			List<Order> listo = enitl.getValue();
			//logger.info(StringUtill.GetJson(listo));   
			//logger.info(listo.size());
				    			if(listo.size()>1){
				    				x = x +1 ;
				    				//logger.info(x);
				    				String message = in.getMessage();
				    				JSONObject jsObj = JSONObject.fromObject(message); 
				    				Iterator<String> keys=jsObj.keys();  
				    				while(keys.hasNext()){
				    					String ke = keys.next();
				    					//String valu = jsObj.getString(ke);
				    					if(ke.contains("_")){
					    					//logger.info(ke);
				    						int type = -1;
				    						for(int i=0;i<listo.size();i++){
				    							Order o = listo.get(i);
				    							List<OrderProduct> listop = o.getOrderproduct();
				    							for(int j=0;j<listop.size();j++){
				    								OrderProduct op = listop.get(j);
				    								//int ctype = CategoryService.getmap().get(op.getCategoryId()).getPid();
				    								//logger.info(op.getCategoryId()); 
				    								if(ke.contains(op.getCategoryId()+"")){ 
				    									//Map<String,Map<String,Map<Order,List<OrderProduct>>>>
				    									Map<String,List<OrderProduct>> mapopp= maprr.get(ke);
				    									if(null == mapopp){
				    										mapopp = new HashMap<String,List<OrderProduct>>();
				    										maprr.put(ke, mapopp);
				    									} 
				    									
				    									List<OrderProduct> mapi = mapopp.get(x+"-"+ke);
				    									  
				    									if(null == mapi){
				    										mapi = new ArrayList<OrderProduct>();
				    										mapopp.put(x+"-"+ke, mapi);
				    										//logger.info(x);
				    									}
				    									
				    									
				    									if(op.getCategoryId() != type){
				    										type = op.getCategoryId();
				    										mapi.add(op);
				    									}
				    									
				    								}
				    							}
				    							
				    						}
				    					}
				    				}
				    				
				    			}
				    			
				    		}
			    			
			    		}
			    	}
			    	
			    }
			}
		}
		
		
		
				 
				for(int m=0;m<list.size();m++){
					OrderProduct op = list.get(m);
					Order o = listorder.get(op.getOrderid()+"");
					//boolean found = listopid.contains(op); 
					//if(!found){ 
						if(op.getStatues() == 0){
							String tdcol = " bgcolor=\"red\"" ;
							if(o.getPhoneRemark()!=1){
								tdcol = ""; 
							}
				    		 
							html.append("<tr id="+op.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
							html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\"  name=\"orderid\" value="+op.getId()+ "></input></td>");
							html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
							html.append("<td align=\"center\"><input type=\"hidden\" name=\"dealsendid\"  value=\""+o.getDealsendId()+"\"/>"+o.getdealsendName()+"</td>");
							if(o.getPhoneRemark()!=1){   
								tdcol = ""; 
							}
							html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+"<p><font color=\""+tdcol+"\"> "+o.getPhone1()+"</td>  "); 
							html.append("<td align=\"center\">"+op.getCategoryName()+"</td> ");
							html.append("<td align=\"center\" >"+op.getTypeName()+"</td>  ");
							html.append("<td align=\"center\" >"+ op.getCount()+"</td> ");
							html.append("<td align=\"center\">"+o.getLocate()+"</td>");
							html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
							html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o)+"</td>");
							html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
							
							InstallSale in = null ;
							String value = "";
							 if(null != mapin){
								 in = mapin.get(o.getDealsendId()+"");
								 if(null != in){
									 String message = in.getMessage();
									 JSONObject jsObj = JSONObject.fromObject(message);
									 value = jsObj.getString(op.getCategoryId()+"");
								 }
							 }
							 if(!StringUtill.isNull(value)){
								 price += Integer.valueOf(value);
							 } 
							  
							html.append("<td align=\"center\"  style=\"background-color:#E8E8D0\">"+
									    "<input type=\"hidden\" name=\"saleresut\"  value=\""+o.getId()+"\"/>  "+
									    "<input type=\"text\" id="+o.getId()+"left value=\""+value+"\"  style=\"border:0; width:80px\"/>"+
			                            "</td>");
			 
						}
					}
				
				//}
		html.append("</tr>"); 
		html.append("<tr class=\"asc\" >");
		html.append("<td align=\"center\" colspan=10 ></td> ");
		html.append("<td align=\"center\" >合计金额</td> ");
		html.append("<td align=\"center\" >"+price+"</td> ");
		html.append("</tr>"); 
		//logger.info(html.toString());
		return html.toString();
		 
	}
    */
	public static String getsize(Map<Order,List<OrderProduct>> listoo){
	   int count = 0 ;
	   String orderid = "";
	   //logger.info(listoo);
	    Set<Map.Entry<Order,List<OrderProduct>>> setlo = listoo.entrySet();
		Iterator<Map.Entry<Order,List<OrderProduct>>> ito = setlo.iterator();
		while(ito.hasNext()){
			Map.Entry<Order,List<OrderProduct>> mapo = ito.next();
			Order order = mapo.getKey();
			List<OrderProduct> listo = mapo.getValue();
			if(StringUtill.isNull(orderid)){
				orderid = order.getId()+"";
			}else {
				orderid = orderid + "_"+ order.getId();
			}
		    for(int i=0;i<listo.size();i++){
		    	OrderProduct op = listo.get(i);
				if(op.getStatues() == 0){
					count++;
				}
			}
		}
		//orderid = orderid.substring(1,orderid.length());
		return orderid+"-"+count ;
   }	
   
   public static String getHtmlSearch(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
	     
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
			boolean flag = false ; 
			if(UserManager.checkPermissions(user, Group.Manger)){
				flag = true ; 
			}
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				String release = ""; 
				String returns = "";
				
        		if(opmap.get(OrderPrintln.release) != null){ 
        			
        		     OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
        		
	        		 if(orp != null){
	        			 if(orp.getStatues() == 2){ 
	        				 release = "<p>驳回申请已同意</p>";
	        		    	  }else if(orp.getStatues() == 4){ 
	        		    		  release = " <p>驳回申请已拒绝</p>";
	        				   }else {
	        					   release = orp.getMessage();
	        		           }   
	        		    }
        		}
        		
        		if(opmap.get(OrderPrintln.returns) != null){ 
        		OrderPrintln op1 = opmap.get(OrderPrintln.returns).get(o.getId());
        		
        		if(op1 != null){
        			 if(op1.getStatues() == 2){ 
        				      returns = "<p>退货申请已同意</p>"; 
        		    	  }else if(op1.getStatues() == 4){ 
        		    		  returns = "<p>退货申请已拒绝</p>";
        				  }else{
        					  returns = op1.getMessage();	  
        		          }
        		 }
        		}
        		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				if(flag){
					html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
				}  
				 
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				 
				html.append("<td align=\"center\" >"+ o.getSendprice(1,"</p>")+"</td>");
				 
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
                html.append("<td align=\"center\" >"+ o.getSendprice(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getdealsendName()+"<p/>"+o.getdealsendphone()+"</td>");
				
				html.append("<td align=\"center\"> "+ o.getChargeDealsendtime()+" </td>");
				
				html.append("<td align=\"center\">  "+(o.getStatues1()==0?"否":"是") +" </td>");
				
				html.append("<td align=\"center\">  "+(o.getStatues2()==0?"否":"是") +" </td>");
				 
				html.append("<td align=\"center\">   "+(StringUtill.isNull(o.getStatuesCharge())?"否":o.getStatuesCharge()) +" </td>");
				 
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
                html.append("<td align=\"center\"> <a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',"+o.getPrintSatues() +",'','"+o.getDeliveryStatues() +"')\">[打印]</a></td>");

        		html.append("<td align=\"center\"> "+release +"</td>");
        		   
        		html.append("<td align=\"center\"> "+returns +"</td>");
        		
		    }
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpSearch(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>"); 
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\">"+o.getRemark()+"</td>");
				
				html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
				
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+o.getChargeSendtime()); 
				html.append("</td>");
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
				html.append("<td align=\"center\">"); 
			    String message = "";
			    if(o.getStatuesinstall()==0){
			    	message = "否";
			    }else if(o.getStatuesinstall()==1){
			    	message = o.getInstalltime()  ;
			    }else if(o.getStatuesinstall()==2){
			    	message = "已忽略";
			    } 
			    html.append(message+"</td>");
		    } 
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlpchargeall(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");
				 
				/*html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");*/
				 
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				html.append("<td align=\"center\">"+(o.getStatuescallback()==0?"否":"是" ));    
				html.append("</td>"); 
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlppcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");*/
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");
				
		    }  
		}
		return html.toString();
		 
	} 
  
   public static String getHtmlpcallback(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
		StringBuffer html = new StringBuffer();
		if(null != list){
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
				
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name =\""+o.getId() +"\"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				//html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
							
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				//html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				//html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				//html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+o.getRemark() +"</td> ");
				
				//html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				/*html.append("<td align=\"center\">"); 
				      if(o.getPrintSatuesP() == 1){

				   html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
			    	  
			      }
				   html.append("</td>");
			*/	
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getStatuesPaigong() == 1 ?"是":"否")); 
				html.append("</td>");*/
				 
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");

				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				/*html.append("<td align=\"center\">"+(o.getDeliverytype() == 2 ?"是":"否")); 
				html.append("</td>");*/
				
		    }  
		}
		return html.toString();
		 
	} 
   
   public static String getHtmlSecondorderDispatching(User user ,List<Order> list){
	   
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    List<User> listSend = UserManager.getUsers(user,Group.send); 
	    
		StringBuffer html = new StringBuffer();
		 
		
		if(null != list){
			
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				html.append("<td align=\"center\">");
				
				int statuesnew = Order.orderpeisong; 
				int printid = o.getSendId();
			      if(o.getOstatues().salereleasereturno != null){
			    	  o.getOstatues().salereleaseo = o.getOstatues().salereleasereturno ;
			    	  printid = o.getReturnid();
			      } 
			      
			     if(o.getSendId() == 0 && o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 ){
						   if(o.getDeliveryStatues() == 9){
							   statuesnew = Order.ordersong;
						   }else if(o.getDeliveryStatues() == 10){
							   statuesnew = Order.orderinsta; 
						   }
						    	  
				
						   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
						   html.append("<option value=\"\"></option>");
				   
			               for(int j=0;j< listSend.size();j++){
			            	   User u = listSend.get(j);
			            	   html.append("<option value="+u.getId()+"> "+u.getUsername()+"</option>");
			                    }
				                	
			               html.append("</select> ");  
			            
			               html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','"+statuesnew +"',"+o.getOstatues().release +",'"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"确定\"/>");
					
					} else {
						
					    if(usermap.get(Integer.valueOf(o.getSendId())) != null){
					       
					    	html.append(usermap.get(Integer.valueOf(o.getSendId())).getUsername());
					     
					      }
					} 
				
			     html.append("</td>"); 
			     html.append("<td align=\"center\"> ");
			     html.append("<a href=\"javascript:void(0);\"  onclick=\"searchlocate('"+o.getId() +"')\">[查看位置]</a>");
			     html.append("</td>");
				
			     html.append("<td align=\"center\">"); 
				 
				   if(o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 && o.getOstatues().salerelease != 0 && (o.getOstatues().returns == -1 || o.getOstatues().returns == 4 )  ){       
					     
						 html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回订单\" onclick=\"winconfirm('"+o.getId()+"','"+ o.getOstatues().release+"','"+o.getSendId()+"')\"></input>");
					 
					 } 
				   html.append("</td>"); 
				 
				   html.append("<td align=\"center\">");     
				    
				      if(o.getOstatues().salereleaseo != null){
				    	  if(o.getOstatues().salereleaseo.getStatues() == 0 ){
				   
				    		  html.append(o.getOstatues().salereleaseo.getMessage());
				    		  logger.info(StringUtill.GetJson(o.getOstatues().salereleaseo));
				    		  html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseo.getId() +"','"+OrderPrintln.comited+"','','','"+OrderPrintln.salerelease+"','"+printid+"')\"  value=\"同意\"/>"); 
				    	      html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseo.getId() +"','"+OrderPrintln.uncomited+"','','','"+OrderPrintln.salerelease+"','"+printid +"')\"  value=\"不同意\"/>");
				   
				   } 
		        }
			
				html.append("</td>");
				html.append("<td align=\"center\">");   
				 
					 if(o.getOstatues().releaseo != null){
					  int sta = o.getOstatues().releaseo.getStatues();
			          String sm = "";
			          if(0 == sta){
			        	  sm = "待确认";
			          }else if(2== sta){
			        	  sm = "申请已同意"; 
			          }else if(4== sta){
			        	  sm = "申请被拒绝";
			          } 
			          html.append(sm );
			
			    	
			       } 

				html.append("</td>");
				html.append("<td align=\"center\">"); 
				   
				if(o.getOstatues().releasedispatch != -1){
					if(o.getOstatues().releasedispatch == 0){
						int statues = -1;    
						  
						if(o.getDeliveryStatues() == 0 || o.getDeliveryStatues() == 9 ){
							if(o.getSendId() == 0){
								statues = 0;  
							}else if(o.getSendId() != 0 ){
								statues = 2;
							}
							 
						}else if (o.getDeliveryStatues() == 1){
							statues = 1 ;  
						}else if(o.getDeliveryStatues() == 2){ 
							statues = 1 ; 
						}else if(o.getDeliveryStatues() == 10){
							if(o.getInstallid() != 0 ){
								statues = 3 ;
							}
						}  
				 
					     
						html.append(o.getOstatues().releasedispatcho.getMessage());
						html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().releasedispatcho.getId()+"','"+OrderPrintln.comited+"','"+statues +"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch +"','"+o.getSendId()+"')\"  value=\"同意\"/>");
				   
				    if(o.getReturnid() == 0){ 
				 
				    html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().releasedispatcho.getId()+"','"+OrderPrintln.uncomited+"','"+statues+"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch+"','"+o.getSendId()+"')\"  value=\"不同意\"/>");  
						
				      }
					}
			    }
			
				html.append("</td>");
				  
				html.append("<td align=\"center\">");
				  if( o.getDeliverytype() != 0){
				   if(o.getReturnid() == 0){    
			
				html.append("<select class = \"category\" name=\"category\"  id=\"return"+o.getId() +"\" >");
				html.append(" <option value=\"0\"></option>");
				 
		               for(int j=0;j< listSend.size();j++){
		            	   User u = listSend.get(j);
		            	 
		            	  
		            	
		            	   html.append("<option value="+u.getId()+"> "+u.getUsername()+"</option>");
		            	  
		            	   
		                    }
			                	
		               html.append("</select>");   
		        
		         html.append("<input type=\"button\" onclick=\"change('return"+o.getId()+"','"+o.getId()+"','"+Order.orderreturn+"','-1','"+o.getSendType(0,"</p>")+"')\"  value=\"确定\"/>");
				} else { 
				// 0 表示未送货  1 表示正在送  2 送货成功
				 if(0 == o.getReturnstatuse()){
				
				html.append(" 商品未回");
				
		          }else if(1 == o.getReturnstatuse()){
				
		        	  html.append("商品已回");
				
				
		          }
				if( null != usermap.get(Integer.valueOf(o.getReturnid()))){
				
				 
				html.append(usermap.get(Integer.valueOf(o.getReturnid())).getUsername());
				 
				}
				}
				
				html.append("</td>"); 

				}
		    }
		} 
		return html.toString();
		 
	}
   
   public static String getHtmlporderDispatching(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    List<User> listSend = UserManager.getUsers(user,Group.send); 
	    
		StringBuffer html = new StringBuffer();
		 
		
		if(null != list){
			
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;

				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
			//	html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				
				html.append("<td align=\"center\">"+o.getprintp()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getsendName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getSendtime() +"</td>");
				
				html.append("<td align=\"center\">"); 
				 
				if(o.getOstatues().release != 0 && o.getOstatues().releasedispatch != 0 && o.getOstatues().salerelease != 0 && (o.getOstatues().returns == -1 || o.getOstatues().returns == 4 ) && o.getOderStatus().equals(10+"") ){       
					     
				  html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回订单\" onclick=\"winconfirm('"+o.getId()+"','"+ o.getOstatues().release+"','"+o.getSendId()+"')\"></input>");
					 
				} 
				
				html.append("</td>"); 
				   
				html.append("<td align=\"center\">"); 
				
				if(o.getOstatues().releasedispatch != 0){
				   if(o.getDeliveryStatues() == 1 || o.getDeliveryStatues() == 9 || o.getDeliveryStatues() == 10){
					   
				   if(o.getInstallid() == 0 && o.getOstatues().release != 0){ 
				
					   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >"); 
					   html.append("<option value=\"\"></option>"); 
				    
		               for(int j=0;j< listSend.size();j++){
		            	   User u = listSend.get(j);
		  
		            	   html.append("<option value=\""+u.getId()+"\"> "+u.getUsername()+" </option>"); 
		            	  
		            	   
		                    }
			                	
		               html.append("</select>");   
		      
		            		   html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','"+Order.orderinstall+"')\"  value=\"确定\"/>"); 
				 }else {
					    
					  
					    	html.append(o.getinstallName()); 
						
						
				}
				
				html.append("</td>"); 
				
				html.append("<td align=\"center\">"); 				   
				     
				if(null != o.getOstatues().salereleaseanzhuango){
		         
				   html.append(o.getOstatues().salereleaseanzhuango.getMessage()); 
				   html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseanzhuango.getId()+"','"+OrderPrintln.comited+"','"+o.getInstallid()+"')\"  value=\"同意\"/>"); 
				   html.append("<input type=\"button\" onclick=\"changes('"+o.getId()+"','"+o.getOstatues().salereleaseanzhuango.getId()+"','"+OrderPrintln.uncomited+"','"+o.getInstallid()+"')\"  value=\"不同意\"/>"); 
				  
				  }
				  
				   } 
				     html.append("</td>"); 
				    html.append("<td align=\"center\">");       
 
					 if( null != o.getOstatues().releaseo){
					  int sta = o.getOstatues().releaseo.getStatues(); 
			          String sm = "";
			          if(0 == sta){ 
			        	  sm = "待确认";
			          }else if(2== sta){
			        	  sm = "申请已同意"; 
			          }else if(4== sta){
			        	  sm = "申请被拒绝";
			          }  
		        
			          html.append(sm); 
				 
			    	
			       } 
				    html.append("</td>");

				}
		    }
		} 
		return html.toString();
		 
	}
   
   
   public static String getHtmlorderquery(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	    
		StringBuffer html = new StringBuffer();
		 
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('../dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\" >"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getinstallName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getInstalltime() +" </td>");
				
				 html.append("<td align=\"center\">");
			     boolean query = true;
			     int statues = OrderManager.getShifangStatues(o);	
					if(statues != -1){

						if(o.getOstatues().orp != null){
							
							query = false ;
							if(o.getOstatues().totalshifang == 4 ){
								query = true ;
						
								html.append("您的申请被拒绝");
								html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype() +"','"+o.getId()+"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
							}else if(o.getOstatues().totalshifang == 0){
							
								html.append("您已提交");
						
							}
						}else {
						
							html.append("<input type=\"submit\" class=\button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype()+"','"+o.getId() +"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
						} 
					}else {
					     	query = true ;
					     	html.append("<input type=\"submit\" class=\"button\" name=\"dosubmit\" value=\"驳回\" onclick=\"winconfirm('"+statues+"','"+user.getUsertype()+"','"+o.getId()+"','"+o.getOstatues().totalshifang+"')\"></input>");	
						
					}
			  
				html.append("</td>"); 
					 
			   html.append("<td align=\"center\">");
			 
			    if(o.getReturnid() == 0 && o.getOstatues().releasedispatch != 0  && o.getOstatues().totalshifang != 0){ 
			        if(o.getDeliveryStatues() == 0 || 9 == o.getDeliveryStatues()){
			 
			
			        	html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
			     
			       
			 
			        	html.append("<option value=\"\" ></option>"); 
			     
			        	html.append("<option value=\"2\" >送货+安装 </option>");   
			        	html.append("<option value=\"1\" >只送货 </option>");
			     
			        	html.append("</select>");  
			        	html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','songhuo')\"  value=\"确定\"/>");

			   
			
			    }else if(1 == o.getDeliveryStatues()  || 10 == o.getDeliveryStatues()){
			        	  
			    	html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+"\" >");
			    	html.append(" <option value=\"4\" >只安装 </option>");  
			                    html.append("</select>  "); 
			                    		html.append("<input type=\"button\" onclick=\"change('songh"+o.getId()+"','"+o.getId()+"','songhuo')\"  value=\"确定\"/>");
			    	
			          }
			    }
			    
			     
			    html.append(" </td>");
			    html.append("<td align=\"center\">"); 
					
			   
			    if(o.getReturnstatuse() == 0 && o.getReturnid() != 0 && query){

			   
			    
					 if(o.getReturnid() != 0){
						if(usermap.get(Integer.valueOf(o.getReturnid())) != null){
					 
							html.append(usermap.get(Integer.valueOf(o.getReturnid())).getUsername());
					
					  }
					}  
					
					
					 html.append("</td>");
					html.append("<td class=\"s_list_m\"> ");
					html.append("<select class =\"category\"  name=\"category\"  id=\"return"+o.getId() +"\" >");
 
									 html.append(" <option value=\"1\" >确认 </option>"); 
									 html.append("</select> "); 
									 html.append("<input type=\"button\" onclick=\"change('return"+o.getId()+"','"+o.getId()+"','"+Order.returns +"')\"  value=\"确定\"/>");
									 html.append("</td>");
		
			    }else if(o.getReturnstatuse() == 1){   
		
			    	html.append("<td class=\"s_list_m\">商品已退</td>"); 

			   }else {
				   

				   html.append("<td class=\"s_list_m\"></td>"); 

			   }
 
			    html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
			    
			    html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
			    
                html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
			    
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+o.getprintp()+"</td>");
				
				html.append(" <td align=\"center\">"); 
				
				      if(o.getPrintSatuesP() == 1){
				    	  html.append("<a href=\"javascript:void(0);\" onclick=\"orderPrint('"+o.getId()+"',1)\">[打印]</a>");
				  
			      }
				   html.append("</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"); 
				html.append(o.getStatuesPaigong() == 1 ?"是":"否");
				 
					html.append(" </td>");
				
			
							html.append("<td align=\"center\">"); 
							html.append(o.getDeliverytype() == 2 ?"是":"否");
			
							html.append("</td>");
							html.append("<td align=\"center\">");    
							html.append(o.getStatuescallback()==0?"否":"是" ); 
							html.append("</td>"); 
							html.append("<td align=\"center\">");  
			
				    String message = "";
				    if(o.getStatuesinstall()==0){
				    	message = "否";
				    }else if(o.getStatuesinstall()==1){
				    	message = "是";
				    }else if(o.getStatuesinstall()==2){
				    	message = "已忽略";
				    } 

				    html.append(message);   
				    html.append("</td>");
				
		    }
		} 
		return html.toString();
		 
	}
   
   public static String getHtmldeliveryStatuesTuihuo(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
			
		
			
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
       		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">"+o.getprint()+"</td>");
				
				
				
       		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlcome(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	      
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
      		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");

				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
      		
		    }
		}
		return html.toString();
		 
	}
    
   public static String getHtmlgo(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	       
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
     		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
                html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");

				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
                html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
                html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
                html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				 
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
								
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				
				
				
				
				
				
				
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");

				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
     		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlcharge(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	      
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
    		
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");
			
				
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				
				html.append("<td align=\"center\">"+ o.getCategory(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(1,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(1,"</p>")+"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");

				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
    		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlcallback(User user ,List<Order> list){
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
		StringBuffer html = new StringBuffer();
		
		
		if(null != list){
	
		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
			
			    html.append("<td align=\"center\" width=\"20\"><input type=\"checkbox\" value=\"\" id=\"check_box\" name = "+o.getId() +"></input></td>");

				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");

				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
                html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getsendName() +" </td>");
				
				html.append("<td align=\"center\" >"+o.getSendtime()+"</td>");
				
				html.append("<td align=\"center\" style=\"white-space:nowrap;\">"+o.getinstallName() +"</td>");
				
				html.append("<td align=\"center\"> "+o.getInstalltime() +"</td>");
				
				
       		
		    }
		}
		return html.toString();
		 
	}
   
   public static String getHtmlDispatching(User user ,List<Order> list){ 
	    HashMap<Integer,User> usermap = UserService.getMapId();
	     
	    List<User> listS =  UserManager.getUsers(user,Group.sencondDealsend);
	    
		StringBuffer html = new StringBuffer(); 
		

		if(null != list){

		    for(int i = 0;i<list.size();i++){
		    	Order o = list.get(i);
		    	String tdcol = " bgcolor=\"red\"" ;
		    	
				html.append("<tr id="+o.getId()+"  class=\"asc\"  onclick=\"updateClass(this)\">");
				  
				 
				html.append("<td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"adddetail('dingdanDetail.jsp?id="+o.getId()+"')\" > "+(o.getPrintlnid() == null?"":o.getPrintlnid())+"</a></td>");
				
				html.append("<td align=\"center\">"+o.getbranchName(o.getBranch())+"</td>");
				
				html.append("<td align=\"center\">"+usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone()+"</td>");
				
				html.append("<td align=\"center\" "+(o.getPosremark()==1?tdcol:"") +">"+o.getPos() +"</td>");
				
				html.append("<td align=\"center\" "+(o.getSailidrecked()==1?tdcol:"") +">"+o.getSailId() +"</td>");
				 
				html.append("<td align=\"center\" "+(o.getReckedremark()==1?tdcol:"") +">"+o.getCheck() +"</td>");
				if(o.getPhoneRemark()!=1){ 
					tdcol = ""; 
				}
				html.append("<td align=\"center\">"+o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+ o.getPhone1()+"</td>  ");
				
				html.append("<td align=\"center\">"+ o.getCategory(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+o.getSendType(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendCount(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSendprice(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getSize(0,"</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifttype("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifcount("</p>")+"</td>");
				
				html.append("<td align=\"center\" >"+ o.getGifStatues("</p>")+"</td>");
				
				html.append("<td align=\"center\">"+o.getSaleTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getOdate() +"</td>");
				
				//html.append("<td align=\"center\">"+o.getDealSendTime() +"</td>");
				
				html.append("<td align=\"center\">"+o.getLocate()+"</td>");
				
				html.append("<td align=\"center\">"+o.getLocateDetail() +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getOrderStatues(o) +"</td>");
				
				html.append("<td align=\"center\">"+OrderManager.getDeliveryStatues(o) +"</td> ");
				
				html.append("<td align=\"center\"> "+o.getRemark() +"</td>");
				
				html.append("<td align=\"center\">");
				    	   
				   if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) != 8 && o.getPrintSatues() == 0){
					   if( o.getOstatues().returns != 2 && o.getOstatues().returns != 0){ 
					   
						   html.append("<select class = \"category\" name=\"category\"  id=\"songh"+o.getId()+ "\" >");
						   html.append("<option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp </option>");   
						    if(listS != null ){ 
				               for(int j=0;j< listS.size();j++){
				            	   User u = listS.get(j);
				            	   html.append("<option value="+u.getId()+">" +  u.getUsername()+" </option>");
				                    }
						     }
					            
						    html.append("</select> ");
						    html.append("<input type=\"button\" onclick=\"changepeidan('songh"+o.getId()+"','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID() +"')\"  value=\"确定\"/> ");
		
				     }
				   }else if(o.getDealsendId() == 0 && Integer.valueOf(o.getOderStatus()) == 8 && o.getPrintSatues() == 0){
				
					   if(OrderManager.Check(o.getId())){ 
						     
						   html.append("<input type=\"button\" onclick=\"changepeidan('2','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"打印\"/>"); 
						   html.append("&nbsp;&nbsp;&nbsp");
						  
					   }else {
						   
						   html.append("<input type=\"button\" onclick=\"changepeidan('1','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus()) +"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"打印\"/>");
						   html.append(" &nbsp;&nbsp;&nbsp");
						   html.append("<input type=\"button\" onclick=\"changepeidan('0','"+o.getId()+"','"+Integer.valueOf(o.getOderStatus())+"','"+o.getSendType(0,"</p>")+"','"+o.getSaleID()+"')\"  value=\"确定\"/>");  
						   				   
						   
					   }
				   }else if(o.getDealsendId() != 0){
					       
					   html.append(usermap.get(o.getDealsendId()).getUsername() +"</p>"+ usermap.get(o.getDealsendId()).getPhone());
				   }
				 
				 html.append("</td> ");
				 html.append("<td align=\"center\"> "+
				              "<a href=\"javascript:void(0);\"  onclick=\"searchlocate('"+o.getId() +"')\">[查看位置]</a> "+
				               "</td>"); 
				
				
				 html.append("<td align=\"center\">"); 
				   
						 if( o.getOstatues().totalshifang == 2){ 
					    	  
					    	    
					    	html.append(o.getOstatues().orp== null ?"":o.getOstatues().orp.getMessage()+"<p>驳回申请已同意</p>"); 
					    	 
					    	  }else if(o.getOstatues().totalshifang == 4){ 
							  
							    	 
					    		  html.append("<p>驳回申请已拒绝</p> ");
						    
							}else if(o.getOstatues().totalshifang != -1){
								
						  
					  
								html.append(o.getOstatues().orp== null ?"":o.getOstatues().orp.getMessage());  
					    	       
								html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().orp.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','','','"+o.getOstatues().totalshifang+"',this)\"  value=\"同意\"/>") ;
								html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().orp.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','','','"+o.getOstatues().totalshifang+"',this)\"  value=\"不同意\"/>");
					
					   }  
				 
						 html.append("</td>");
				
						 html.append("<td align=\"center\">") ;
				
					 if(o.getOstatues().returns == 2){ 
				    	  
						 html.append("<p>退货申请已同意</p>");
				    	 
				    	  }else if(o.getOstatues().returns == 4){ 
						     	
						    	  
				    		  html.append("<p>退货申请已拒绝</p>");
						    	 
						    	 
						   }else if(o.getOstatues().returns != -1){
									 
							   html.append( o.getOstatues().op1.getMessage());
								  if(o.getOstatues().releasedispatch == 0 ){
								       
									  html.append("安装公司处理中"); 
								  } else if(o.getOstatues().releasedispatch == 2 ){
								  
									  html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch+"','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch+"',this)\"  value=\"同意退货\"/>");
								   
								  }else {
									  if(Integer.valueOf(o.getOderStatus()) == 8){
									
										  html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse()+"','"+OrderPrintln.release +"',this)\"  value=\"打印\"/>");
										  html.append(" <input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse() +"','',this)\"  value=\"确定\"/> "); 
						
									
									  }else {
										  if(o.getOstatues().totalshifang == 0){
											
											  html.append(" 请先处理驳回信息");
											  
											    }else {
									           
											    	html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId() +"','"+o.getOstatues().releasedispatch+"','"+o.getReturnstatuse()+"','"+OrderPrintln.releasedispatch +"',this)\"  value=\"同意\"/>");
											    	html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().op1.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','"+o.getOstatues().releasedispatch +"','"+o.getReturnstatuse() +"','"+OrderPrintln.releasedispatch +"',this)\"  value=\"不同意\"/>");   
								   } 
								 }
						      }
				         }
					 html.append("</td>");
             
					 html.append(" <td>");
              if(o.getOstatues().huanhuo == 2){
              
            	  html.append(" 安装公司处理中");
             
                }else if(o.getOstatues().huanhuo == 0){
            
              html.append(o.getOstatues().huanhuoObject.getMessage() );
              html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().huanhuoObject.getId()+"','"+o.getId()+"','"+OrderPrintln.comited+"','"+o.getDealsendId()+"','"+o.getOstatues().huanhuo+"','-1','-1',this)\"  value=\"同意\"/>");
              html.append("<input type=\"button\" onclick=\"changes('"+o.getOstatues().huanhuoObject.getId()+"','"+o.getId()+"','"+OrderPrintln.uncomited+"','"+o.getDealsendId()+"','"+o.getOstatues().huanhuo +"','-1','-1',this)\"  value=\"不同意\"/>");    
               }
             
              html.append("</td>");      		
		    }
		}
		return html.toString();
		 
	}

   
	
}
