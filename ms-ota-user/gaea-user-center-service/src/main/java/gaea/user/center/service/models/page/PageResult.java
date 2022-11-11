package gaea.user.center.service.models.page;

import gaea.user.center.service.models.query.Page;
import gaea.user.center.service.models.response.Resp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 * 分页记录返回结果,主要考虑返回值时候，{@link Resp} 返回值字段误解
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@ApiModel("返回分页结果")
public class PageResult<T> extends Resp<T> {
    /**
     * <pre>
     * 当前页数
     * </pre>
     */
    @ApiModelProperty("当前页")
    private Integer current;
    /**
     * <pre>
     *  每页记录
     * </pre>
     */
    @ApiModelProperty("每页数量")
    private Integer pageSize;
    /**
     * <pre>
     *  总记录数
     * </pre>
     */
    @ApiModelProperty("总记录数")
    private int totalItem;

    /**
     * 默认构造函数
     */
    public PageResult() {
    }

    /**
     * <pre>
     * 带有参数的构造函数
     * </pre>
     *
     * @param page 分页参数对象
     * @param data 数据
     */
    public PageResult(Page page, T data) {
        this.current = page.getCurrent();
        this.pageSize = page.getPageSize();
        this.totalItem = page.getTotalItem();
        this.setData(data);
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer curPage) {
        this.current = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
