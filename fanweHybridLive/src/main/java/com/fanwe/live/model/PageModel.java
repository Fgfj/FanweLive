package com.fanwe.live.model;

/**
 * Created by Administrator on 2016/9/27.
 */
public class PageModel
{
    private int page = 1;//当前页
    private int has_next;//是否有下一页
    private int page_total;//总共页数

    //---------- add method start ----------

    /**
     * 重置页数为第一页
     */
    public void resetPage()
    {
        page = 1;
    }

    /**
     * 页数加一
     */
    public void increasePage()
    {
        page++;
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean hasNextPage()
    {
        return getHas_next() == 1;
    }

    /**
     * 是否可以请求接口
     *
     * @param isLoadMore true-加载更多，false-刷新
     * @return true-可以请求接口，false-没有更多数据
     */
    public boolean refreshOrLoadMore(boolean isLoadMore)
    {
        if (isLoadMore)
        {
            if (hasNextPage())
            {
                increasePage();
            } else
            {
                return false;
            }
        } else
        {
            resetPage();
        }
        return true;
    }

    //---------- add method end ----------

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getHas_next()
    {
        return has_next;
    }

    public void setHas_next(int has_next)
    {
        this.has_next = has_next;
    }

    public int getPage_total()
    {
        return page_total;
    }

    public void setPage_total(int page_total)
    {
        this.page_total = page_total;
    }
}
