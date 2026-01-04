package ynu.edu.smart_energy.common;

/**
 * 业务异常（用于参数校验、规则不符合等）
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
