package ynu.edu.smart_energy.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一接口返回格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 状态码：0成功，-1失败
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据体
     */
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    public static <T> ApiResponse<T> ok(String msg, T data) {
        return new ApiResponse<>(0, msg, data);
    }

    public static <T> ApiResponse<T> fail(String msg) {
        return new ApiResponse<>(-1, msg, null);
    }
}
