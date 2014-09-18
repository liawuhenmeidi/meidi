package upload;
import java.io.*;  
import java.net.URISyntaxException;  
import java.util.ArrayList;  
import java.util.List;   

import javax.servlet.http.HttpServletRequest;
  
import com.baidu.inf.iis.bcs.model.*;  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  
  
import com.baidu.inf.iis.bcs.BaiduBCS;  
import com.baidu.inf.iis.bcs.auth.BCSCredentials;  
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;  
import com.baidu.inf.iis.bcs.http.HttpMethodName;  
import com.baidu.inf.iis.bcs.policy.Policy;  
import com.baidu.inf.iis.bcs.policy.PolicyAction;  
import com.baidu.inf.iis.bcs.policy.PolicyEffect;  
import com.baidu.inf.iis.bcs.policy.Statement;  
import com.baidu.inf.iis.bcs.request.CreateBucketRequest;  
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;  
import com.baidu.inf.iis.bcs.request.GetObjectRequest;  
import com.baidu.inf.iis.bcs.request.ListBucketRequest;  
import com.baidu.inf.iis.bcs.request.ListObjectRequest;  
import com.baidu.inf.iis.bcs.request.PutObjectRequest;  
import com.baidu.inf.iis.bcs.request.PutSuperfileRequest;  
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;  
  
@SuppressWarnings("unused")  
public class Sample {  
    private static final Log log = LogFactory.getLog(Sample.class);  
    // ----------------------------------------  
    static String host = "bcs.duapp.com";   
    static String accessKey = "oAZ5v5FT4zFcyVFnRqQgBfkz";  
    static String secretKey = "O1R5HLrHns4va7bkAKT1fn1UTV9oX2ng";  
    static String bucket = "liaowuhen";     
    // ----------------------------------------   
    static String object = "/image/";   
    static File destFile = new File("D:\\ftp\\oms.txt");  
    static File saveFile = new File("C:\\BCS\\download.txt");  
    public static BaiduBCS init(){  
    
        BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
        
        BaiduBCS baiduBCS = new BaiduBCS(credentials, host); 
       // baiduBCS.putObjectPolicy(bucket, object, X_BS_ACL.PublicRead);   
        // baiduBCS.setDefaultEncoding("GBK");  
         baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8  
        return  baiduBCS;  
    }  
    
    /** 
     * @param args 
     * @throws java.net.URISyntaxException 
     * @throws java.io.IOException 
     */  
    public static void main(String[] args) throws URISyntaxException, IOException {  
       // PropertyConfigurator.configure("log4j.properties");  
    	
        BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);  
        BaiduBCS baiduBCS = new BaiduBCS(credentials, host); 
        baiduBCS.putBucketPolicy(bucket, X_BS_ACL.PublicReadWrite); 
        // baiduBCS.setDefaultEncoding("GBK");   
        baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8  
  
        try {  
            // -------------bucket-------------  
            // listBucket(baiduBCS);  
            // createBucket(baiduBCS);  
            // deleteBucket(baiduBCS);  
            // getBucketPolicy(baiduBCS);  
            // putBucketPolicyByPolicy(baiduBCS);  
            // putBucketPolicyByX_BS_ACL(baiduBCS, X_BS_ACL.PublicControl);  
            // listObject(baiduBCS);  
            // ------------object-------------  
            putObjectByFile(baiduBCS);  
            // putObjectByInputStream(baiduBCS);  
            //getObjectWithDestFile(baiduBCS);  
            // putSuperfile(baiduBCS);  
             //deleteObject(baiduBCS);  
             //getObjectMetadata(baiduBCS);  
            // setObjectMetadata(baiduBCS);  
            // copyObject(baiduBCS, bucket, object + "_copy" +  
            // (System.currentTimeMillis()));  
            //getObjectPolicy(baiduBCS);  
             //putObjectPolicyByPolicy(baiduBCS);  
            // putObjectPolicyByX_BS_ACL(baiduBCS, X_BS_ACL.PublicReadWrite);  
  
            // ------------common------------------  
            //generateUrl( baiduBCS);  
        } catch (BCSServiceException e) {  
            log.warn("Bcs return:" + e.getBcsErrorCode() + ", " + e.getBcsErrorMessage() + ", RequestId=" + e.getRequestId());  
        } catch (BCSClientException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void generateUrl(BaiduBCS baiduBCS) {  
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, bucket, object);  
        generateUrlRequest.setBcsSignCondition(new BCSSignCondition());  
        generateUrlRequest.getBcsSignCondition().setIp("*");  
        //generateUrlRequest.getBcsSignCondition().setTime(123455L);  
        //generateUrlRequest.getBcsSignCondition().setSize(123455L);  
        System.out.println(baiduBCS.generateUrl(generateUrlRequest));  
    }  
  
    public static void copyObject(BaiduBCS baiduBCS, String destBucket, String destObject) {  
        ObjectMetadata objectMetadata = new ObjectMetadata();  
        objectMetadata.setContentType("image/jpeg");  
        baiduBCS.copyObject(new Resource(bucket, object), new Resource(destBucket, destObject), objectMetadata);  
        baiduBCS.copyObject(new Resource(bucket, object), new Resource(destBucket, destObject), null);  
        baiduBCS.copyObject(new Resource(bucket, object), new Resource(destBucket, destObject));  
    }  
  
    private static void createBucket(BaiduBCS baiduBCS) {  
        // baiduBCS.createBucket(bucket);  
        baiduBCS.createBucket(new CreateBucketRequest(bucket, X_BS_ACL.PublicRead));  
    }  
  
    private static void deleteBucket(BaiduBCS baiduBCS) {  
        baiduBCS.deleteBucket(bucket);  
    }  
  
    public static void deleteObject(BaiduBCS baiduBCS,String object) {  
        Empty result = baiduBCS.deleteObject(bucket, object).getResult();  
        log.info(result);  
    }  
  
    private static void getBucketPolicy(BaiduBCS baiduBCS) {  
        BaiduBCSResponse<Policy> response = baiduBCS.getBucketPolicy(bucket);  
  
        log.info("After analyze: " + response.getResult().toJson());  
        log.info("Origianal str: " + response.getResult().getOriginalJsonStr());  
    }  
  
    public static void getObjectMetadata(BaiduBCS baiduBCS) {  
        ObjectMetadata objectMetadata = baiduBCS.getObjectMetadata(bucket, object).getResult();  
        log.info(objectMetadata);  
    }  
  
    private static void getObjectPolicy(BaiduBCS baiduBCS) {  
        BaiduBCSResponse<Policy> response = baiduBCS.getObjectPolicy(bucket, object);  
        log.info("After analyze: " + response.getResult().toJson());  
        log.info("Origianal str: " + response.getResult().getOriginalJsonStr());  
    }  
  
    private static void getObjectWithDestFile(BaiduBCS baiduBCS) {  
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, object);  
        baiduBCS.getObject(getObjectRequest, saveFile);  
    }  
  
    private static void listBucket(BaiduBCS baiduBCS) {  
        ListBucketRequest listBucketRequest = new ListBucketRequest();  
        BaiduBCSResponse<List<BucketSummary>> response = baiduBCS.listBucket(listBucketRequest);  
        for (BucketSummary bucket : response.getResult()) {  
            log.info(bucket);  
        }  
    }  
  
    private static void listObject(BaiduBCS baiduBCS) {  
        ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);  
        listObjectRequest.setStart(0);  
        listObjectRequest.setLimit(20);  
        // ------------------by dir  
        {  
            // prefix must start with '/' and end with '/'  
            // listObjectRequest.setPrefix("/1/");  
            // listObjectRequest.setListModel(2);  
        }  
        // ------------------only object  
        {  
            // prefix must start with '/'  
            // listObjectRequest.setPrefix("/1/");  
        }  
        BaiduBCSResponse<ObjectListing> response = baiduBCS.listObject(listObjectRequest);  
        log.info("we get [" + response.getResult().getObjectSummaries().size() + "] object record.");  
        for (ObjectSummary os : response.getResult().getObjectSummaries()) {  
            log.info(os.toString());  
        }  
    }  
  
    private static void putBucketPolicyByPolicy(BaiduBCS baiduBCS) {  
        Policy policy = new Policy();  
        Statement st1 = new Statement();  
        st1.addAction(PolicyAction.all).addAction(PolicyAction.get_object);  
        st1.addUser("zhengkan").addUser("zhangyong01");  
        st1.addResource(bucket + "/111").addResource(bucket + "/111");  
        st1.setEffect(PolicyEffect.allow);  
        policy.addStatements(st1);  
        baiduBCS.putBucketPolicy(bucket, policy);  
    }  
  
    private static void putBucketPolicyByX_BS_ACL(BaiduBCS baiduBCS, X_BS_ACL acl) {  
        baiduBCS.putBucketPolicy(bucket, acl);  
    }  
   
    public static void putObjectByFile(BaiduBCS baiduBCS) {
    	
        PutObjectRequest request = new PutObjectRequest(bucket, object, destFile); 
        request.setAcl(X_BS_ACL.PublicReadWrite);   
        ObjectMetadata metadata = new ObjectMetadata();  
        metadata.setContentType("text/html");  
  
        request.setMetadata(metadata);  
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);  
        ObjectMetadata objectMetadata = response.getResult();  
        log.info("x-bs-request-id: " + response.getRequestId());  
        log.info(objectMetadata);  
    }  
    
            
    public static void putObjectByFile(BaiduBCS baiduBCS , File file,String imagename) {  
    	  
    	log.info(imagename); 
        PutObjectRequest request = new PutObjectRequest(bucket, object+imagename, file);   
         
        request.setAcl(X_BS_ACL.PublicReadWrite); 
        
        ObjectMetadata metadata = new ObjectMetadata();  
      
        metadata.setContentType("text/html");  
  
        request.setMetadata(metadata);  
      
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);  
        ObjectMetadata objectMetadata = response.getResult();  
        
        
        
        log.info("x-bs-request-id: " + response.getRequestId());  
        log.info(objectMetadata);  
    }  
  
    
    /** 
     * 
     * @param baiduBCS 
     * @param bucket     仓库名 
     * @param object     上传到仓库中的对象 
     * @param destFile    本地需要上传的文件对象 
     * @param contentType     文件类型 
     */  
    public static void putObjectByFile(BaiduBCS baiduBCS,String bucket,String object,File destFile,String contentType) {  
        PutObjectRequest request = new PutObjectRequest(bucket, object, destFile);  
        ObjectMetadata metadata = new ObjectMetadata();  
        metadata.setContentType(contentType);  
  
        request.setMetadata(metadata);  
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);  
        ObjectMetadata objectMetadata = response.getResult();  
        log.info("x-bs-request-id: " + response.getRequestId());  
        log.info(objectMetadata);  
    }  
  
    public static void putObjectByInputStream(BaiduBCS baiduBCS) throws FileNotFoundException {  
        File file = createSampleFile();  
        InputStream fileContent = new FileInputStream(file);  
        ObjectMetadata objectMetadata = new ObjectMetadata();  
        objectMetadata.setContentType("text/html");  
        objectMetadata.setContentLength(file.length());  
        PutObjectRequest request = new PutObjectRequest(bucket, object, fileContent, objectMetadata);  
        ObjectMetadata result = baiduBCS.putObject(request).getResult();  
        log.info(result);  
    }  
     
    public static void putObjectByInputStream(BaiduBCS baiduBCS,InputStream fileContent) {    
        ObjectMetadata objectMetadata = new ObjectMetadata(); 
        
        objectMetadata.setContentType("text/html");  
        objectMetadata.setContentLength(1024*1024*100);  
          
        PutObjectRequest request = null; 
		request = new PutObjectRequest(bucket, object, fileContent, objectMetadata);  
        log.info(3);
        ObjectMetadata result = baiduBCS.putObject(request).getResult();  
        log.info(result);  
    }  
    
    
    private static void putObjectPolicyByPolicy(BaiduBCS baiduBCS) {  
        Policy policy = new Policy();  
        Statement st1 = new Statement();  
        st1.addAction(PolicyAction.all).addAction(PolicyAction.get_object);  
        st1.addUser("zhengkan").addUser("zhangyong01");  
        st1.addResource(bucket + object).addResource(bucket + object);  
        st1.setEffect(PolicyEffect.allow);  
        policy.addStatements(st1);  
        baiduBCS.putObjectPolicy(bucket, object, policy);  
    }  
  
    private static void putObjectPolicyByX_BS_ACL(BaiduBCS baiduBCS, X_BS_ACL acl) {  
        baiduBCS.putObjectPolicy(bucket, object, acl);  
    }  
  
    /** 
     * 修改文件的权限 
     * @param baiduBCS  存储上下文 
     * @param bucket 存储仓库 
     * @param object 文件对象 
     * @param acl 控制权限 
     */  
    public static void putObjectPolicyByX_BS_ACL(BaiduBCS baiduBCS,String bucket ,String object,X_BS_ACL acl) {  
        baiduBCS.putObjectPolicy(bucket, object, acl);  
    }  
  
    public static void putSuperfile(BaiduBCS baiduBCS) {  
        List<SuperfileSubObject> subObjectList = new ArrayList<SuperfileSubObject>();  
        // 0  
        BaiduBCSResponse<ObjectMetadata> response1 = baiduBCS.putObject(bucket, object + "_part0", createSampleFile());  
        subObjectList.add(new SuperfileSubObject(bucket, object + "_part0", response1.getResult().getETag()));  
        // 1  
        BaiduBCSResponse<ObjectMetadata> response2 = baiduBCS.putObject(bucket, object + "_part1", createSampleFile());  
        subObjectList.add(new SuperfileSubObject(bucket, object + "_part1", response2.getResult().getETag()));  
        // put superfile  
        PutSuperfileRequest request = new PutSuperfileRequest(bucket, object + "_superfile", subObjectList);  
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putSuperfile(request);  
        ObjectMetadata objectMetadata = response.getResult();  
        log.info("x-bs-request-id: " + response.getRequestId());  
        log.info(objectMetadata);  
    }  
  
    public static void setObjectMetadata(BaiduBCS baiduBCS) {  
        ObjectMetadata objectMetadata = new ObjectMetadata();  
        objectMetadata.setContentType("text/html12");  
        baiduBCS.setObjectMetadata(bucket, object, objectMetadata);  
    }  
  
    private static File createSampleFile() {  
        try {  
            File file = File.createTempFile("java-sdk-", ".txt");  
            file.deleteOnExit();  
  
            Writer writer = new OutputStreamWriter(new FileOutputStream(file));  
            writer.write("01234567890123456789\n");  
            writer.write("01234567890123456789\n");  
            writer.write("01234567890123456789\n");  
            writer.write("01234567890123456789\n");  
            writer.write("01234567890123456789\n");  
            writer.close();  
  
            return file;  
        } catch (IOException e) {  
            log.error("tmp file create failed.");  
            return null;  
        }  
    }  
}  