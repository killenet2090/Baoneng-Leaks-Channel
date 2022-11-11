package com.bnmotor.icv.tsp.ota.model.page;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bnmotor.icv.tsp.ota.model.req.Page;
import com.bnmotor.icv.tsp.ota.model.resp.Resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <pre>
 * 分页记录返回结果,主要考虑返回值时候，{@link Resp} 返回值字段误解
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@ApiModel("返回分页结果")
//@Accessors(chain = true)
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
	 * 每页记录
	 * </pre>
	 */
	@ApiModelProperty("每页数量")
	private Integer pageSize;
	/**
	 * <pre>
	 * 总记录数
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

	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	public static class Position {
		@Getter
		Integer offset;
		@Getter
		Integer limit;
	}

	public Position position(Integer index, Integer size) {
		this.current = index > 0 ? index : 1;
		this.pageSize = size > 0 ? size : 20;

		int offset = (index - 1) * size;
		int limit = size;
		return Position.of().setOffset(offset).setLimit(limit);
	}

}
