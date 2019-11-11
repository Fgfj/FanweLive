package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by shibx on 2017/4/12.
 * 游戏分享收益数据
 */

public class App_gamesDistributionActModel extends BaseActModel
{
    private DistributionData data;

    public DistributionData getData()
    {
        return data;
    }

    public void setData(DistributionData data)
    {
        this.data = data;
    }

    public class DistributionData
    {
        private DistributionParentModel parent;
        private List<GameContributorModel> list;
        private PageModel page;

        public List<GameContributorModel> getList()
        {
            return list;
        }

        public void setList(List<GameContributorModel> list)
        {
            this.list = list;
        }

        public PageModel getPage()
        {
            return page;
        }

        public void setPage(PageModel page)
        {
            this.page = page;
        }

        public DistributionParentModel getParent()
        {
            return parent;
        }

        public void setParent(DistributionParentModel parent)
        {
            this.parent = parent;
        }
    }
}
