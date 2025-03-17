

package net.jjjerp.framework.common.exception;

import net.jjjerp.framework.common.api.ApiCode;

/**
 * DAO异常
 */
public class DaoException extends SpringBootJjjException {
	private static final long serialVersionUID = -6912618737345878854L;

	public DaoException(String message) {
        super(message);
    }

    public DaoException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DaoException(ApiCode apiCode) {
        super(apiCode);
    }
}
