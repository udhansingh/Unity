/* 
Copyright 2010 Udaya Kumar (Udy)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.onesun.utils;

import java.text.SimpleDateFormat;

public class TimeUtils {
	public final static SimpleDateFormat javaDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	public final static SimpleDateFormat postgresSqlDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public final static SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
	
	
	public enum Day {
    	SUNDAY		(1, "SUNDAY"),
    	MONDAY		(2, "MONDAY"),
    	TUESDAY		(3, "TUESDAY"),
    	WEDNESDAY	(4, "WEDNESDAY"),
    	THURSDAY	(5, "THURSDAY"),
    	FRIDAY		(6, "FRIDAY"),
    	SATURDAY	(7, "SATURDAY");

		private int _number = -1;
		private String _name = null;
		
		Day(int number, String name){
			this._number = number;
			this._name = name;
		}
		
		public static String name(int number){
			for(Day day : Day.values()){
				if(day._number == number){
					return day._name;
				}
			}
			
			return null;
		}
	}
	
	public enum Month {
		JANUARY		(0, 	"JANUARY", 		"Q1",		"H1"),
		FEBURARY	(1, 	"FEBRUARY", 	"Q1",		"H1"),
		MARCH		(2, 	"MARCH", 		"Q1",		"H1"),
		APRIL		(3, 	"APRIL",	 	"Q2",		"H1"),
		MAY			(4, 	"MAY", 			"Q2",		"H1"),
		JUNE		(5, 	"JUNE",	 		"Q2",		"H1"),
		JULY		(6, 	"JULY", 		"Q3",		"H2"),
		AUGUST		(7, 	"AUGUST", 		"Q3",		"H2"),
		SEPTEMBER	(8, 	"SEPTEMBER", 	"Q3",		"H2"),
		OCTOBER		(9, 	"OCTOBER", 		"Q4",		"H2"),
		NOVEMBER	(10, 	"NOVEMBER", 	"Q4",		"H2"),
		DECEMBER	(11, 	"DECEMBER", 	"Q4",		"H2");

		private int _number = -1;
		private String _name = null;
		private String _quarter = null;
		private String _halfYear = null;
		
		Month(int number, String name, String quarter, String halfYear){
			this._number = number;
			this._name = name;
			this._quarter = quarter;
			this._halfYear = halfYear;
		}
		
		public static String name(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._name;
				}
			}
			
			return null;
		}
		
		public static String quarter(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._quarter;
				}
			}
			
			return null;
		}
		
		public static String halfYear(int number){
			for(Month month : Month.values()){
				if(month._number == number){
					return month._halfYear;
				}
			}
			
			return null;
		}
	}
	
	public static int toMinutes(int hour, int minutes){
		hour = (hour < 0) ? 0 : hour;
		minutes = (minutes < 0) ? 0 : minutes;
		
		return ((hour * 60) + minutes);
	}
}
