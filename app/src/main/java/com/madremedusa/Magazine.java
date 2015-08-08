package com.madremedusa;

import java.util.ArrayList;

public class Magazine {
	private String name;
	private ArrayList<String> imageUrl; 
	
	public Magazine(String name, ArrayList<String> imageUrl){
		this.name=name;
		this.imageUrl=imageUrl;
	}
	
	public String getCoverName(){
		return name;
	}
	
	public String getCover(){
		if(imageUrl.size()>0){
			return imageUrl.get(0);
		}
		return null;
	}
	
	public ArrayList<String> getImageUrl(){
		return imageUrl;
	}
}
