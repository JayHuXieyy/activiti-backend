package com.huafagroup.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础查询的DTO 如果有补充请继承该DTO，然后再添加自身特有的查询条件属性
 *
 * @author dong
 */
@ApiModel("基础查询DTO")
public class QueryDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 分页参数，分页长度
     */
    @ApiModelProperty(value = "每页大小", required = true)
    private int pageSize;
    /**
     * 分页参数，分布起始页
     */
    @ApiModelProperty(value = "当前页码", required = true)
    private int pageNo;
    /**
     * 输入的查询参数
     */
    @ApiModelProperty(value = "查询参数", notes = "具体业务查询时，前后端约定", example = "key:value 键值对的形式")
    private Map<String, Object> searchValue;
    /**
     * 查询时间(开始)
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;
    /**
     * 查询时间(结束)
     */
    @ApiModelProperty(value = "结束时间")
    private String endDate;

    public QueryDto() {
        searchValue = new HashMap<String, Object>();
    }

    public int getPageSize() {
        if (pageSize <= 0) {
            pageSize = 20;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        if (pageNo > 0) {
            return pageNo;
        } else {
            return 0;
        }
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Map<String, Object> getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Map<String, Object> searchValue) {
        this.searchValue = searchValue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
