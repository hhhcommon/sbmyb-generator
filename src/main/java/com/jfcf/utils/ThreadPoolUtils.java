package com.jfcf.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ducongcong
 * @date 2016年3月15日
 */
public class ThreadPoolUtils {

	private static ExecutorService executorService ;
	private static Integer cpuNum =0;
	
	static{
		cpuNum = Runtime.getRuntime().availableProcessors()*4;
		executorService = Executors.newFixedThreadPool(cpuNum);
	}
	
	public static ExecutorService getExecutorService(){
		return executorService;
	}
	public static Integer getCpuNum(){
		return cpuNum;
	}
	
}
