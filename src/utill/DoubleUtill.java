package utill;

import java.text.DecimalFormat;

public class DoubleUtill {
	public static String getdoubleTwo(double num){
		DecimalFormat    df   = new DecimalFormat("######0.00");
		return df.format(num); 
	}
	
}
