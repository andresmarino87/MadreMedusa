package com.madremedusa;

public class Post {
	String title;
	String author;
	String thumbnailUrl;
	String content;
	
	public Post(String title,String author,String thumbnailUrl,String content){
		this.title = title;
		this.author = author;
		this.thumbnailUrl=thumbnailUrl;
		this.content=content;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getAuthor(){
		return this.author;
	}

	public String getThumbnailUrl(){
		return this.thumbnailUrl;
	}
	
	public String getContent(){
		return this.content;
	}
	
}
