package writeExcel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class WriteExcel { 
	private static final Log log = LogFactory.getLog(WriteExcel.class); 
	public static void uploadOnly(HttpServletRequest req, HttpServletResponse res) {
		String tempPath = req.getSession().getServletContext().getRealPath("/")
				+ "data" + File.separator + "tempFile";
		log.info("tempFilePath=" + tempPath);   
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) { 
			tempFile.mkdirs(); 
		} 
		res.setContentType("text/html; charset=UTF-8");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096 * 10);
		factory.setRepository(tempFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1000000 * 100);

		// 文件名起名为时间+uuid形式 
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String uuid = UUID.randomUUID().toString();
		String fileName = fmt.format(new Date()) + "-" + uuid + ".xls";
		String filePath = "";
		try {
			List fileItems = upload.parseRequest(req);
			Iterator iter = fileItems.iterator();
			FileItem item;

			iter = fileItems.iterator();

			while (iter.hasNext()) {
				item = (FileItem) iter.next();

				if (item.isFormField()) {
					log.info("表单参数名:" + item.getFieldName() + "，表单参数值:"
							+ item.getString("UTF-8"));
				}
				// 忽略其他不是文件域的所有表单信息
				if (!item.isFormField()) {

					if (item.getName() != null && !item.getName().equals("")) {

						if (item.getName().endsWith(".xls")) {
							log.info("上传文件的大小:" + item.getSize());
							log.info("上传文件的类型:" + item.getContentType());
							log.info("上传文件的名称:" + item.getName());

							File upperFolder = new File(filePath);
							if (!upperFolder.exists()) {
								upperFolder.mkdirs();
							}
							File file = new File(filePath, fileName);
							item.write(file);
							log.info("上传文件的名称:" + file.getAbsolutePath());
							log.info(("upload.message" + "上传文件成功！"));
						} else {
							log.info("wront type " + item.getName());
							log.info(("upload.message" + "文件类型错误，只能是xls类型！"));
						}
					} else { 
						log.info(("upload.message" + "没有选择上传文件！"));
					}
				}
			} 
		} catch (IOException e) {
			log.info(("upload.message" + "文件IO错误！" + e));
		} catch (FileUploadException e) {
			log.info(("upload.message" + "文件上传错误！" + e));
		} catch (Exception e) {
			log.info(("upload.message" + "文件上传错误！" + e));
		}
	}
}
