package httpClient;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
   
public class MyJsoup {
	protected static Log logger = LogFactory.getLog( MyJsoup.class);
    static String url=""; 
    /** 
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        // TODO Auto-generated method stub
        BolgBody();
        //test();
        //Blog();
        /*
         * Document doc = Jsoup.connect("http://www.oschina.net/")
         * .data("query", "Java") // 请求参数 .userAgent("I ’ m jsoup") // 设置
         * User-Agent .cookie("auth", "token") // 设置 cookie .timeout(3000) //
         * 设置连接超时时间 .post();
         */// 使用 POST 方法访问 URL

        /*
         * // 从文件中加载 HTML 文档 File input = new File("D:/test.html"); Document doc
         * = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
         */
    }

    /**
     * 获取指定HTML 文档指定的body
     * @throws IOException
     */
     
    public static Document getDocumnetByStr(String html){ 
    	 Document doc = null; 
    	 doc = Jsoup.parse(html); 
 		 
         return doc; 
    }
    public static void BolgBody() {
        // 直接从字符串中输入 HTML 文档
        //String html = "<html><head><title> 开源中国社区 </title></head>"
           //     + "<body><p> 这里是 jsoup 项目的相关文章 </p></body></html>";
       // Document doc = Jsoup.parse(html);
       // System.out.println(doc.body());
        
        
        // 从 URL 直接加载 HTML 文档
        Document doc2;
        String title ="";
		try { 
			doc2 = Jsoup.connect(url).get();
			title = doc2.body().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(title);
    }

    /**
     * 获取博客上的文章标题和链接
     */
    public static void article() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postTitle");
            for (Element element :ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkText = link.text().trim();
                    System.out.println(linkHref);
                    System.out.println(linkText);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 获取指定博客文章的内容
     */
    public static void Blog() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postBody");
            for (Element element :ListDiv) {
                System.out.println(element.html());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}