package gaea.vehicle.basic.service.models.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gaea.vehicle.basic.service.models.domain.AbstractQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Objects;

/**
 * <pre>
 *  分页入参对象，使用方式:
 *  1、创建一个默认的当前页=1，每页记录=15。
 *  Page page = new Page();
 *
 *  2、创建一个当前页=5，默认每页记录=15.
 *  Page page = new Page(5);
 *
 *  3、创建一个当前页=5，每页记录=30.
 *  Page page = new Page(5,30);
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@ApiModel("分页参数")
public class Page extends AbstractQuery implements Serializable {
    /**
     * 默认当前页
     */
    private static final int DEFAULT_CUR_PAGE = 1;
    /**
     * 默认每页记录数
     */
    private static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 默认支持最大每页记录数,防止刷库
     */
    private static final int DEFAULT_MAX_SIZE = 10000;
    /**
     * <pre>
     * 当前页数
     * </pre>
     */
    @ApiModelProperty("当前页")
    private Integer current = DEFAULT_CUR_PAGE;
    /**
     * <pre>
     *  每页记录
     * </pre>
     */
    @ApiModelProperty("每页数量")
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    /**
     * <pre>
     *  总记录数,业务内部处理使用
     * </pre>
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private int totalItem;

    /**
     * 默认构造函数
     */
    public Page() {
        this(DEFAULT_CUR_PAGE);
    }

    /**
     * <pre>
     *  包括当前页的构造函数
     * </pre>
     *
     * @param curPage 当前页
     */
    public Page(Integer curPage) {
        this(curPage, DEFAULT_PAGE_SIZE);
    }

    /**
     * <pre>
     *  包括当前页，每页记录数构造函数
     * </pre>
     *
     * @param curPage  当前页
     * @param pageSize 每页记录数
     */
    public Page(Integer curPage, Integer pageSize) {
        this.current = (Objects.isNull(curPage) || curPage <= 0) ? DEFAULT_CUR_PAGE : curPage;
        this.pageSize = this.getDefaultPageSize(pageSize);
    }

    /**
     * <pre>
     * 对每页记录数进行处理,主要逻辑：
     * 当每页记录数少于等于0时候,赋值默认当前每页记录数 {@link Page#DEFAULT_PAGE_SIZE} ;
     * 当每页记录数大于最大值{@link Page#DEFAULT_MAX_SIZE}时候,需要赋值默认最大值,防止从数据库拉取太多值;
     * </pre>
     *
     * @param pageSize 每页记录数
     * @return 当前需要的每页记录数
     */
    private Integer getDefaultPageSize(Integer pageSize) {
        Integer tmpPageSize = (Objects.isNull(pageSize) || pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        return tmpPageSize > DEFAULT_MAX_SIZE ? DEFAULT_MAX_SIZE : tmpPageSize;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer curPage) {
        this.current = (Objects.isNull(curPage) || curPage <= 0) ? DEFAULT_CUR_PAGE : curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = getDefaultPageSize(pageSize);
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getStartRow() {
        return (this.current - 1) * this.pageSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
