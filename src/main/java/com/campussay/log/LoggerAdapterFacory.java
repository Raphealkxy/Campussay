package com.campussay.log;


public class LoggerAdapterFacory
{
	public static LoggerAdapter getLoggerAdapter(Class clazz)
	{
		return new LoggerCommonAdapter(clazz);
	}
}
