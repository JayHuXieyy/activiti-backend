package com.huafagroup.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huafagroup.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>
 * <p>
 * 文章表
 *
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:20
 */
@TableName(value = "article_table")
public class ArticleTable extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId

    private Long id;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")


    private String keywords;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")


    private String remark;
    /**
     * 文章来源
     */
    @ApiModelProperty(value = "文章来源")


    private String source;
    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")


    private String author;
    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")


    private String title;
    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")


    private String content;
    /**
     * 目录id
     */
    @ApiModelProperty(value = "目录id")


    private Long catalogId;
    /**
     * 党组织id
     */
    @ApiModelProperty(value = "党组织id")


    private Long organizationId;
    /**
     * 文章简述
     */
    @ApiModelProperty(value = "文章简述")


    private String sketch;
    /**
     * 状态，0停用，1显示
     */
    @ApiModelProperty(value = "状态，0停用，1显示")


    private Integer status;
    /**
     * 审批状态，0未审批，1审批中，2已审批，3退回
     */
    @ApiModelProperty(value = "审批状态，0未审批，1审批中，2已审批，3退回")


    private Integer activitiStatus;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getActivitiStatus() {
        return activitiStatus;
    }

    public void setActivitiStatus(Integer activitiStatus) {
        this.activitiStatus = activitiStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
