package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActListModel;

import java.util.List;

public class User_friendsActModel extends BaseActListModel
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<UserModel> list;

    //add
    private PageModel pageModel;

    public PageModel getPageModel()
    {
        if (pageModel == null)
        {
            pageModel = new PageModel();
            pageModel.setPage(getPage());
            pageModel.setHas_next(getHas_next());
        }
        return pageModel;
    }

    public List<UserModel> getList()
    {
        return list;
    }

    public void setList(List<UserModel> list)
    {
        this.list = list;
    }

}
