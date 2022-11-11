/*
 * Copyright 2018 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.user.gateway.common.auth;


/**
 * <pre>
 * 业务异常处理对象
 * </pre>
 * 
 * @author jiankang.xia
 * @Date 2018年1月16日 下午10:57:31
 * @since 1.0
 */
public class ManagerException extends Exception {
	private static final long serialVersionUID = -7286449736302823066L;

	public ManagerException(ResultCodeEnum message) {
		super(String.valueOf(message));
	}

	public ManagerException(Throwable cause) {
		super(cause);
	}

	public ManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
