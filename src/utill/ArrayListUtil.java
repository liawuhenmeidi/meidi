package utill;

import java.util.List;

import wilson.upload.UploadSalaryModel;

public class ArrayListUtil {
	
	public static int getMaxLengthInUploadSalaryModelList(List<UploadSalaryModel> input){
		int result = 0 ; 
		String tempContent = "";
		for(int i = 0 ; i < input.size() ; i ++){
			tempContent = input.get(i).getContent();
			tempContent = tempContent.replace("{", "").replace("}", "");
			if(tempContent.split(",").length > result){
				result = tempContent.split(",").length;
			}
			
		}
		return result;
	}
}
