package com.huafagroup.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huafagroup.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 * <p>
 * 目录表
 *
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:44
 */
@TableName(value = "catalog_table")
public class CatalogTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId

    private Long id;

    /**
     * 模块id
     */
    @ApiModelProperty(value = "模块id")


    private Long module;
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
     * 目录名称
     */
    @ApiModelProperty(value = "目录名称")


    private String title;
    /**
     * 目录类型，0动态目录，1镇，2村，3村镇共用
     */
    @ApiModelProperty(value = "目录类型，0动态目录，1镇，2村，3村镇共用")


    private Boolean type;
    /**
     * 父目录id
     */
    @ApiModelProperty(value = "父目录id")


    private Long parentId;
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


    public Long getModule() {
        return module;
    }

    public void setModule(Long module) {
        this.module = module;
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


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    public String getSynopsisContent() {
        return synopsisContent;
    }

    public void setSynopsisContent(String synopsisContent) {
        this.synopsisContent = synopsisContent;
    }

}
