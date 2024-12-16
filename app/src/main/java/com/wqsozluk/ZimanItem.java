package com.wqsozluk;

/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 * 
 * @author ITCuties
 *
 */
public class ZimanItem {
	
	private String word;
	private String definition;
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDef() {
		return definition;
	}
	

	public void setDef(String link) {
		this.definition = link;
	}
	
	@Override
	public String toString() {
		return word;
	}
	
}
