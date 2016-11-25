package com.ihordev.helloservice.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a logical page of data and actual data: it contains 
 * information about current selected page, how many items are in page, 
 * sort order of elements in page and sort attribute of data, but it is also 
 * results of data querying with additional information about total elements.
 * <p>
 * It can be used both as query parameter for data service layer objects and result
 * holder. It can be instantiated as description of pagination without any model data
 * (Methods {@link #getRecords()}, {@link #getTotalRecordsCount()} and {@link #getTotalPagesCount()} 
 * will throw {@link IllegalStateException}}) and be passed to data service layer, which 
 * will return new instance holding retrieved data. 
 *
 * @param <T> Entity that must be retrieved from data source page by page.
 */

public class ResourcePage<T>
{
    public static enum Order
    {
        ASC, DESC
    }
    
    private int page = 1;
    private int limit = 5;
    private Order order = null;
    private String orderBy = null;
    
    private int totalRecordsCount = -1;
    private List<T> records = null;
    
    public ResourcePage()
    {
        
    }
    
    public ResourcePage(int page, int limit)
    {
        this.page = page;
        this.limit = limit;
    }
    
    public ResourcePage(int page, int limit, Order order, String orderBy)
    {
        this(page, limit);
        
        this.order = order;
        this.orderBy = orderBy;
    }

    
    /**
     * Constructs a new instance that holds all data of specified ResourcePage.
     * It is mainly used when passing this object as query parameter to some 
     * data service layer to avoid referential dependency.
     * 
     * @param   resourcePage the resource page from which the new instance is created.
     */
    public ResourcePage(ResourcePage<T> resourcePage)
    {
        this(resourcePage.getPage(), resourcePage.getLimit(), resourcePage.getOrder(), resourcePage.getOrderBy());
        
        this.totalRecordsCount = resourcePage.getTotalRecordsCount();
        this.records = (resourcePage.getRecords() != null) ? new ArrayList<>(resourcePage.getRecords()) : null;
    }
    

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }
    
    /**
     * Returns total records count of data that are not presented
     * in this current page instance.
     * 
     * @return  totalRecordsCount       total records of data that are not presented 
     *                                  in this current page instance.
     *                              
     * @throws IllegalStateException    if resource page is not populated with data.
     */
    public int getTotalRecordsCount()
    {
        checkHasDataOrThrowException();
        
        return totalRecordsCount;
    }

    public void setTotalRecords(int totalRecords)
    {
        this.totalRecordsCount = totalRecords;
    }

    /**
     * Returns total pages count in this current page instance.
     * 
     * @return      total pages count besides of this current page instance.                
     * @throws      IllegalStateException if resource page is not populated with data.
     */
    public int getTotalPagesCount()
    {
        checkHasDataOrThrowException();
        
        return totalRecordsCount / limit + ((totalRecordsCount % limit != 0) ? 1 : 0);            
    }
    
    /**
     * Returns data retrieved from data source presented as list that 
     * this current resource page contains.
     * 
     * @return  records     results data that this current resource page contain.                
     * @throws              IllegalStateException if resource page is not populated with data.
     */
    public List<T> getRecords()
    {
        checkHasDataOrThrowException();
        
        return records;
    }

    public void setRecords(List<T> records)
    {
        this.records = records;
    }
    
    private void checkHasDataOrThrowException()
    {
        if (hasData())
        {
            new IllegalStateException("Resource page has no data records");
        }
    }
    
    private boolean hasData()
    {
        return records == null;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + getPage();
        result = prime * result + getLimit();
        result = prime * result + ((getOrder() == null) ? 0 : getOrder().hashCode());
        result = prime * result + ((getOrderBy() == null) ? 0 : getOrderBy().hashCode());
        result = prime * result + ((getRecords() == null) ? 0 : getRecords().hashCode());
        result = prime * result + getTotalRecordsCount();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ((obj instanceof ResourcePage) == false)
            return false;
        
        ResourcePage<?> other = (ResourcePage<?>) obj;
        
        if (getPage() != other.getPage())
            return false;
        if (getLimit() != other.getLimit())
            return false;
        if (getOrder() != other.getOrder())
            return false;
        if (getTotalRecordsCount() != other.getTotalRecordsCount())
            return false;
        
        if (getOrderBy() == null)
        {
            if (other.getOrderBy() != null)
                return false;
        } else if (!getOrderBy().equals(other.getOrderBy()))
            return false;
        
        if (getRecords() == null)
        {
            if (other.getRecords() != null)
                return false;
        } else if (!getRecords().equals(other.getRecords()))
            return false;
        
        return true;
    }

    @Override
    public String toString()
    {
        String totalPagesCount = "";
        if (hasData()) {
            totalPagesCount = ", totalPagesCount=" + getTotalPagesCount();
        }
        return "ResourcePage [page=" + page + ", limit=" + limit + ", order=" + order + ", orderBy=" + orderBy
                + ", totalRecordsCount=" + totalRecordsCount + totalPagesCount + ", records=" + records + "]";
    }
    
    
 
}
