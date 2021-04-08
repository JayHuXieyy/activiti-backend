package com.huafagroup.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huafagroup.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 * <p>
 * 党组织表
 *
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:17:08
 */
@TableName(value = "organization_table")
public class OrganizationTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId

    private Long id;

    /**
     * 上级组织id
     */
    @ApiModelProperty(value = "上级组织id")


    private Long parentId;
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
     * 级别,0县，1镇，2村
     */
    @ApiModelProperty(value = "级别,0县，1镇，2村")


    private Integer rank;
    /**
     * 党组织名称
     */
    @ApiModelProperty(value = "党组织名称")


    private String title;
    /**
     * 状态，0停用，1显示
     */
    @ApiModelProperty(value = "状态，0停用，1显示")


    private Integer status;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")


    private Integer delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
