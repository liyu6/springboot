package site.fxqn.zcl.model.common;
/*
    作者: kat-qian
    日期: 2018/5/30 17:37
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.fxqn.zcl.config.Constant;

import java.io.Serializable;

/**
 * 返回给前端的JSON数据格式的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBody implements Serializable {

    private Integer code;
    private String message;
    private Object data;


    /**
     * 成功的实体
     *
     * @param data 数据
     * @return
     */
    public static ResultBody success(Object data) {
        return new ResultBody(Constant.ResultBodyCode.SUCCESS, null, data);
    }


    /**
     * 成功的实体，无数据
     * @return
     */
    public static ResultBody success() {
        return success(null);
    }


    /**
     * 失败的实体
     *
     * @param code    错误码
     * @param message 错误信息
     * @return
     */
    public static ResultBody error(Integer code, String message) {
        return new ResultBody(code, message, null);
    }


    /**
     * 返回一个通用错误码的错误实体
     *
     * @param message 错误信息
     * @return
     */
    public static ResultBody error(String message) {
        return error(Constant.ResultBodyCode.ERROR, message);
    }


    /**
     * 返回一个通用的失败的实体
     *
     * @return
     */
    public static ResultBody error() {
        return error(Constant.ResultBodyMessage.ERROR);
    }

}
