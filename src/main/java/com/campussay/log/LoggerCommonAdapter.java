package com.campussay.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerCommonAdapter implements LoggerAdapter
{
	private Log logger;
	
	public LoggerCommonAdapter(Class clazz)
	{
		logger = LogFactory.getLog(clazz);
	}
	
	public void debug(String msg, Throwable ex)
	{
		logger.debug(msg, ex);
	}

	public void debug(String msg)
	{
		logger.debug(msg);
	}

	public void debug(Throwable ex)
	{
		if(ex == null){
			logger.debug(null);
		}else{
			logger.debug(ex.getMessage(),ex);
		}
	}

	public void debug(String msg, Object... params)
	{
		logger.debug(params,new Exception(msg));
		
	}

	public void error(String msg, Throwable ex)
	{
		logger.error(msg, ex);
	}

	public void error(String msg)
	{
		logger.error(msg);
	}

	public void error(Throwable ex)
	{
		if(ex == null){
			logger.error(null);
		}else{
			logger.error(ex.getMessage(),ex);
		}
	}

	public void error(String msg, Object... params)
	{
		logger.error(params,new Exception(msg));		
	}

	public void info(String msg, Throwable ex)
	{
		logger.info(msg, ex);
	}

	public void info(String msg)
	{
		logger.info(msg);
	}

	public void info(Throwable ex)
	{
		if(ex == null){
			logger.info(null);
		}else{
			logger.info(ex.getMessage(),ex);
		}
	}
	
	public void info(String msg, Object... params)
	{
		logger.info(params,new Exception(msg));
	}
	
	public void warn(String msg, Throwable ex)
	{
		logger.warn(msg, ex);
	}

	public void warn(String msg)
	{
		logger.warn(msg);
	}

	public void warn(Throwable ex){
		if (ex == null){
			logger.warn(null);
		} else{
			logger.warn(ex.getMessage(), ex);
		}
	}

	public void warn(String msg, Object... params)
	{
		logger.warn(params,new Exception(msg));
	}
}
