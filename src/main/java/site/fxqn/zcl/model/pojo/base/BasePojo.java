package site.fxqn.zcl.model.pojo.base;
/*
    作者: kat-qian
    日期: 2018/5/31 10:54
*/


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import site.fxqn.zcl.config.Constant;

import java.io.Serializable;
import java.util.Date;

/**
 * pojo基类，提供三个字段
 * 创建时间
 * 修改时间
 * 删除标志
 */
@Data
@JsonIgnoreProperties({"create_time", "modify_time", "status"})
public abstract class BasePojo implements Serializable {

    private Date create_time;
    private Date modify_time;
    private Short status;


    /**
     * 初始化pojo，一般用于插入数据时，需要初始化三个变量
     */
    public void initPojo() {
        Date now = new Date();
        this.create_time = now;
        this.modify_time = now;
        this.status = Constant.PojoStatus.DEFAULT;
    }


    /**
     * 修改“修改时间”
     */
    public void updateModifyTime() {
        this.modify_time = new Date();
    }


    /**
     * 设置状态为已删除
     */
    public void setStatusDeleted() {
        this.status = -1;
    }
}
