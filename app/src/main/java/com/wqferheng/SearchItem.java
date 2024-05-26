package com.wqferheng;

import android.os.Bundle;
import android.os.Parcelable;

public class SearchItem 
{
	public SearchItem(String query2, String id,  String searchtype, Object res ) 
	{
		Query=query2;
		Id=id;
		SearchType=searchtype;
		result=res;
	}
	public Object result; 
	public String Query;
	public String SearchType; //"Exact" or "Search"
	public int Index;
	public String Id;
	public int Top;
	public Bundle State;
}
