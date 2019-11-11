/**
 * 
 */
package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * 
 * @author Administrator
 * @date 2016-5-17 下午6:54:44
 */
public class App_propActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LiveGiftModel> list;

	public List<LiveGiftModel> getList()
	{
		return list;
	}

	public void setList(List<LiveGiftModel> list)
	{
		this.list = list;
	}

}
