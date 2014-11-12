package utill;

import java.text.NumberFormat;  
	  
import java.util.Locale;  
	
public class StringCompare {	 
//	/**
//	* 编辑距离算法，首先由俄国科学家Levenshtein提出的，又叫Levenshtein Distance
//	* 主要用来计算从原串（s）转换到目标串(t)所需要的最少的插入，删除和替换的数目，
//	* 在NLP中应用比较广泛，同时也常用来计算你对原文所作的改动数
//	*/
//
//	 private static int compare(String str, String target) {
//
//	  int d[][]; // 矩阵
//
//	  int n = str.length();
//
//	  int m = target.length();
//
//	  int i; // 遍历str的
//
//	  int j; // 遍历target的
//
//	  char ch1; // str的
//
//	  char ch2; // target的
//
//	  int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
//
//	  if (n == 0) {
//
//	   return m;
//
//	  }
//
//	  if (m == 0) {
//
//	   return n;
//
//	  }
//
//	  d = new int[n + 1][m + 1];
//
//	  for (i = 0; i <= n; i++) { // 初始化第一列
//
//	   d[i][0] = i;
//
//	  }
//
//	  for (j = 0; j <= m; j++) { // 初始化第一行
//
//	   d[0][j] = j;
//
//	  }
//
//	  for (i = 1; i <= n; i++) { // 遍历str
//
//	   ch1 = str.charAt(i - 1);
//
//	   // 去匹配target
//
//	   for (j = 1; j <= m; j++) {
//
//	    ch2 = target.charAt(j - 1);
//
//	    if (ch1 == ch2) {
//
//	     temp = 0;
//
//	    } else {
//
//	     temp = 1;
//
//	    }
//
//	    // 左边+1,上边+1, 左上角+temp取最小
//
//	    d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
//
//	      + temp);
//
//	   }
//
//	  }
//
//	  return d[n][m];
//
//	 }
//
//	 
//
//	 private static int min(int one, int two, int three) {
//
//	  return (one = one < two ? one : two) < three ? one : three;
//
//	 }
//
//	 
//
//	 /**
//
//	 * 获取两字符串的相似度
//
//	 *
//
//	 * @param str
//
//	 * @param target
//
//	 * @return
//
//	 */
//
//	 public static float getSimilarityRatio(String str, String target) {
//		 
//	  return 1 - (float) compare(str, target)
//
//	    / Math.max(str.length(), target.length());
//
//	 }
	
	//以上方法不适合，我自己写了个简单的，效率还不错
	public static double compare(String str ,String target){
		char aa[] = str.toCharArray();
		char bb[] = target.toCharArray();
		
		double result = 0; 
		
		for(int i = 0 ; i < aa.length ; i ++){
			for(int j = 0 ; j < bb.length ; j ++){
				if(aa[i] == bb[j]){
					//去掉j
					bb[j] = '烫';
					result ++;
					break;
				}
			}
		}
		
		
		return result;
	}
	 
	public static double getSimilarityRatio(String str, String target) {
		 
	
		return compare(str,target)/Math.max(str.length(), target.length());

	}
	
	  
}  

