package com.wqdictionary;

import java.util.ArrayList;
import java.util.List;

import com.wqdictionary.ExpandableListAdapter.ChildHolder;


public class GroupEntity {
	public String Name;
	public String Body;
	public List<GroupItemEntity> GroupItemCollection;	

	public GroupEntity()
	{
		GroupItemCollection = new ArrayList<GroupItemEntity>();
	}

	public class GroupItemEntity
	{
		public String Name;		
		public ChildHolder holder;
		public Boolean Isgotin=false;
	}
	
	
}
