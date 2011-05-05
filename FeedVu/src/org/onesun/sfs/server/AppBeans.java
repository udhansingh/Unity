package org.onesun.sfs.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppBeans implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null; 
	
	@Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
    	AppBeans.applicationContext = context;
    }
    
    public static ApplicationContext getApplicationContext(){
    	return applicationContext;
    }
}
