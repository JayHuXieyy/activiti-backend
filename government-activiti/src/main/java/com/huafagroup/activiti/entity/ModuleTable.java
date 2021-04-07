package com.huafagroup.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huafagroup.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 * <p>
 * 模块表
 *
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:58
 */
@TableName(value = "module_table")
public class ModuleTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId

    private Long id;

    /**
     * 模块图标
     */
    @ApiModelProperty(value = "模块图标")


    private String icon;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")


    private String remark;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")


    private Integer sort;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")


    private String synopsis;
    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")


    private String title;
    /**
     * 内容展示类型，0党组织展示，1普通，2链接
     */
    @ApiModelProperty(value = "内容展示类型，0党组织展示，1普通，2链接")


    private Boolean type;
    /**
     * 链接地址
     */
    @ApiModelProperty(value = "链接地址")


    private String linkUrl;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")


    private Long deptId;
    /**
     * 状态，0停用，1显示
     */
    @ApiModelProperty(value = "状态，0停用，1显示")


    private Boolean status;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")


    private String synopsisContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }


    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getSynopsisContent() {
        return synopsisContent;
    }

    public void setSynopsisContent(String synopsisContent) {
        this.synopsisContent = synopsisContent;
    }

}