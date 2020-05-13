package com.open.web.plugin.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description
 * @auther 程佳伟
 * @create 2020-01-14 16:27
 */
@Getter
@Setter
@ToString
public class PageBean {

    private int total;//当前表中总条目数量
    private int pageNum;//当前页的位置
    private int pages;//总页数
    private int pageSize;//页面大小
    private int startRow; //开始查询行数
    private int endRow; //最后查询行数

    public PageBean(int pageNum,int pageSize){

        if(pageNum <= 0 || pageSize <= 0){
            throw new RuntimeException("当前页 或 页面大小不合法");
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.startRow = (pageNum - 1) * pageSize;
        this.endRow = pageNum * pageSize;
    }
}
