package com.madremedusa;

public class MItem {
	private String title;
	private int thumbnail;
		
	public MItem(String title, int thumbnail){
		this.title=title;
		this.thumbnail=thumbnail;
	}
	
	//setters
	public void setTitle(String title) {
		this.title=title;
	}	

	public void setThumbnail(Integer thumbnail){
		this.thumbnail=thumbnail;
	}

	//getter
	public String getTitle() {
		return title;
	}

	public int getThumbnail(){
		return thumbnail;
	}
}
