package com.fanwe.live.model;

import com.fanwe.library.common.SDSelectManager;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-26 下午6:10:04 类说明
 */
public class App_tipoff_typeModel implements SDSelectManager.Selectable
{
	private long id;
	private String name;
	private boolean selected;

	@Override
	public boolean isSelected()
	{
		return selected;
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
