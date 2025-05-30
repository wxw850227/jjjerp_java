

package net.jjjerp.framework.shiro.exception;

import net.jjjerp.framework.common.api.ApiCode;
import net.jjjerp.framework.common.exception.SpringBootJjjException;

/**
 * Shiro配置异常
 **/
public class ShiroConfigException extends SpringBootJjjException {
	private static final long serialVersionUID = -4573955712491628431L;

	public ShiroConfigException(String message) {
        super(message);
    }

    public ShiroConfigException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public ShiroConfigException(ApiCode apiCode) {
        super(apiCode);
    }
}
