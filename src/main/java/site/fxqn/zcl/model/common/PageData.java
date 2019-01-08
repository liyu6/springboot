package site.fxqn.zcl.model.common;
/*
    作者: kat-qian
    日期: 2018/6/4 10:47
*/

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {
    private Integer page;
    private Integer pageSize;
    private List<T> rows;


    /**
     * 空值处理
     */
    public void initPageInfo() {
        this.page = this.page == null ? 1 : this.page;
        this.pageSize = this.pageSize == null ? Integer.MAX_VALUE : this.pageSize;
    }


    /**
     * 获取一个pageData对象，该对象从一个pagehelper的Page对象中获取数据
     * @param page
     * @param <E>
     * @return
     */
    public static <E> PageData<E> fromPage(Page<E> page) {
        PageData<E> pageData = new PageData<>();
        pageData.setPage(page.getPageNum());
        pageData.setPageSize(page.getPageSize());
        pageData.setRows(page.getResult());
        return pageData;
    }
}
