package com.qy.ntf.util;

import com.qy.ntf.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * DESC : 分页信息类
 */
@ApiModel(value="分页参数类",description="分页参数类")
@Data
public class MyPageInfo<T> extends BaseEntity {
    /**当前页*/
    @ApiModelProperty(value="当前页",example="当前页",required=true)
    private int pageNum;
    /**每页条数*/
    @ApiModelProperty(value="每页条数",example="每页条数",required=true)
    private int pageSize;
    /**总数*/
    @ApiModelProperty(value="总数",example="总数",required=true)
    private long total;
    /**首页*/
    @ApiModelProperty(value="首页",example="首页",required=true)
    private int firstPage;
    /**上一页*/
    @ApiModelProperty(value="上一页",example="上一页",required=true)
    private int prePage;
    /**下一页*/
    @ApiModelProperty(value="下一页",example="下一页",required=true)
    private int nextPage;
    /**尾页*/
    @ApiModelProperty(value="尾页",example="尾页",required=true)
    private int lastPage;
    /**所有导航页号*/
    @ApiModelProperty(value="所有导航页号",example="所有导航页号",required=true)
    private int[] navigatepageNums;


    /**开始行数*/
    @ApiModelProperty(value="开始行数",example="开始行数",required=true)
    private int startRow;
    /**结束行数*/
    @ApiModelProperty(value="结束行数",example="结束行数",required=true)
    private int endRow;
    /**是否有上一页*/
    @ApiModelProperty(value="是否有上一页",example="是否有上一页",required=true)
    private boolean hasPreviousPage;
    /**是否有下一页*/
    @ApiModelProperty(value="是否有下一页",example="是否有下一页",required=true)
    private boolean hasNextPage;
    /**导航页码数*/
    @ApiModelProperty(value="导航页码数",example="导航页码数",required=true)
    private int navigatePages;
    /**导航页首页*/
    @ApiModelProperty(value="导航页首页",example="导航页首页",required=true)
    private int navigateFirstPage;
    /**导航页尾页*/
    @ApiModelProperty(value="导航页尾页",example="导航页尾页",required=true)
    private int navigateLastPage;
    /**每页条数*/
    @ApiModelProperty(value="每页条数",example="每页条数",required=true)
    private int size;
    /**总页数*/
    @ApiModelProperty(value="总页数",example="总页数",required=true)
    private int pages;
    /**内容*/
    @ApiModelProperty(value="集合内容",required=true)
    private List<T> list;



    @ApiModelProperty(value="是否是首页",example="是否是首页",required=true)
    private boolean isFirstPage;
    @ApiModelProperty(value="是否是尾页",example="是否是尾页",required=true)
    private boolean isLastPage;


}
