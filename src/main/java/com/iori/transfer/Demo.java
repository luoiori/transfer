package com.iori.transfer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Demo {
	public static void main(String[] args) {
		String a = "1900-1-1 8:11:04";
		System.out.println(formatDate(a));
	}
	
	private static String formatDate(String date) {
    	SimpleDateFormat sdf = new SimpleDateFormat(getFmt(date));
    	SimpleDateFormat tf = new SimpleDateFormat("H:m:s");
    	if(isDate(date)) {
    		Calendar calendar = Calendar.getInstance();
    		try {
				calendar.setTime(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
				return date;
			}
    		return tf.format(calendar.getTime());
    	}
    	return date;
    }
	
	 private static boolean isDate(String am) {
		
	    	SimpleDateFormat sdf = new SimpleDateFormat(getFmt(am));
	    	try {
	    		sdf.parse(am);
	    	}catch(Exception e) {
	    		for(char c:am.toCharArray()){
	                if( !(Character.isDigit(c) || c==':' || c=='.')){
	                    return false;
	                }
	            }
	    		
	    	}
	    	
	    	return true;

	    }
	 
	 private static String getFmt(String am) {
		 String fmt = "H:m:s";
		 if(am.length()>8) {
			 fmt = "y-M-d H:m:s";
		 }
		 return fmt;
	 }
}
