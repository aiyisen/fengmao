package com.qy.ntf.bean.dto;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王振读
 * 2022-05-25 19:27:16
 * DESC : 商务合作记录 添加参数
 */
@Data
@ApiModel(value = "商务合作记录 添加参数", description = "商务合作记录 添加参数")
public class SysBusinessAddDto extends BaseEntity {


    
        /**
     * 
     */
    @ApiModelProperty(value = "", example = "", required = true)
    private Long id;
        
        /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", example = "姓名", required = true)
    private String userName;
        
        /**
     * 企业
     */
    @ApiModelProperty(value = "企业", example = "企业", required = true)
    private String orgName;
        
        /**
     * 联系人职务
     */
    @ApiModelProperty(value = "联系人职务", example = "联系人职务", required = true)
    private String contactJob;
        
        /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式", example = "联系方式", required = true)
    private String tell;
        
        /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱", example = "电子邮箱", required = true)
    private String eMail;
        
        /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号", example = "微信号", required = true)
    private String wxCode;
        
        /**
     * 拟合作ip信息
     */
    @ApiModelProperty(value = "拟合作ip信息", example = "拟合作ip信息", required = true)
    private String ipTitle;
        
        /**
     * 您与合作ip关系
     */
    @ApiModelProperty(value = "您与合作ip关系", example = "您与合作ip关系", required = true)
    private String ipRelation;
        
        /**
     * 从何处获取的入驻申请链接
     */
    @ApiModelProperty(value = "从何处获取的入驻申请链接", example = "从何处获取的入驻申请链接", required = true)
    private String fromWhere;
        
        
        
        
        
        
}
