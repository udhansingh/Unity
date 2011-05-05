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

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class LogUtils {
	public static void initLog(){
		Properties properties = new Properties();
		
		properties.setProperty("log4j.rootLogger", "INFO, CONSOLE");
		properties.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		properties.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		properties.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
		
		PropertyConfigurator.configure(properties);
	}
}
