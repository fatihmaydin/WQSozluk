package com.wqdictionary;

public class Words {
	public String id;
	public String peyv;
	public String wate;
	public String NormalizedWord;
	public String tags;
	public int IndexOfSearch;
	public Words(String trim, String trim2) {
		peyv=trim;
		wate=trim2;
	}
	public Words(String wordd, String normalized, String def, String t) {
		peyv=wordd;
		 NormalizedWord = normalized;
		wate=def;
		tags=t;
	}
	public Words(String _id, String wordd, String normalized, String def, String t) {
		id=_id;
		peyv=wordd;
		 NormalizedWord = normalized;
		wate=def;
		tags=t;
	}
	public Words() {
		// TODO Auto-generated constructor stub
	}
	public String getpeyv()
	{
		return peyv;
	}
	public String getid()
	{
		return id;
	}
	public String getwate()
	{
		return wate;
	}
	public String getNormalized()
	{
		return NormalizedWord;
	}
	public void setpeyv(String p)
	{
		 peyv=p;
	}
	public void setwate(String wat)
	{
		wate=wat;
	}
	public String gettags()
	{
		return tags;
	}

}
