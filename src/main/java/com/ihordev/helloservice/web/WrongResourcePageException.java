package com.ihordev.helloservice.web;

/**
 * This exception is used when specified page number exceeds total pages count range.
 */
public class WrongResourcePageException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    private int wrongPage;
    private int totalPagesCount;
   
    
    public WrongResourcePageException(int wrongPage, int totalPagesCount)
    {
        super();
        this.wrongPage = wrongPage;
        this.totalPagesCount = totalPagesCount;
    }
    
    public int getWrongPage()
    {
        return wrongPage;
    }
    public void setWrongPage(int wrongPage)
    {
        this.wrongPage = wrongPage;
    }
    public int getTotalPagesCount()
    {
        return totalPagesCount;
    }
    public void setTotalPagesCount(int totalPagesCount)
    {
        this.totalPagesCount = totalPagesCount;
    }
    
}
