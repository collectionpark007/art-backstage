package com.art.support.api;

import com.art.entity.BaseEntity;
import com.art.support.common.Page;
import com.art.support.common.ReturnCode;

import java.util.List;

public class PageResultSet extends ResultSet {


    /**
     *
     */
    private static final long serialVersionUID = -8714913277908187490L;

    /**** 分页信息 *****/

    /**
     * 页码
     */
    private int pageNumber;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int totalPages;


    public PageResultSet(String retCode, String retMessage, Object... args) {
        super(retCode, retMessage, args);
    }

    public PageResultSet(String retCode, String retMessage, int pageNumber, int pageSize, long total, int totalPages, List<Object> list) {

        super(retCode, retMessage);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = totalPages;
        this.getResult().put("list", list);

    }

    /**
     * 分页结果返回
     *
     * @param retMessage
     * @return
     * @author never
     */
    public static PageResultSet success(String retMessage, Page<Object> page) {

        PageResultSet resultSet = new PageResultSet(ReturnCode.success, retMessage);
        resultSet.setPageNumber(page.getPageNumber());
        resultSet.setPageSize(page.getPageSize());
        resultSet.setTotal(page.getTotal());
        resultSet.setTotalPages(page.getTotalPages());
        resultSet.getResult().put("list", page.getContent());

        return resultSet;

    }

    /**
     * 分页成功返回，使用pojo返回
     *
     * @param retMessage
     * @param page
     * @param list
     * @return
     * @author never
     */
    public static PageResultSet success(String retMessage, Page<? extends BaseEntity> page, List<Object> list) {

        PageResultSet resultSet = new PageResultSet(ReturnCode.success, retMessage);
        resultSet.setPageNumber(page.getPageNumber());
        resultSet.setPageSize(page.getPageSize());
        resultSet.setTotal(page.getTotal());
        resultSet.setTotalPages(page.getTotalPages());
        resultSet.getResult().put("list", list);

        return resultSet;
    }

    /**
     * 分页结果错误返回
     *
     * @param retMessage
     * @return
     * @author never
     */
    public static PageResultSet error(String retMessage, Object... args) {

        PageResultSet resultSet = new PageResultSet(ReturnCode.error, retMessage, args);
        resultSet.setPageNumber(0);
        resultSet.setPageSize(0);
        resultSet.getResult().put("list", null);
        return resultSet;

    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    public int getTotalPages() {
        return totalPages;
    }


    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


}
