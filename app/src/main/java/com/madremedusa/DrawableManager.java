package com.madremedusa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class DrawableManager {
	Drawable stub_id;
//	static final int stub_id=R.drawable.ic_launcher;
	MemoryCache memoryCache=new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
    public DrawableManager(Context context,Drawable icon){
    	this.fileCache=new FileCache(context);
        this.executorService=Executors.newFixedThreadPool(5);
        this.stub_id=icon;
    }
    
    public void DisplayImage(String url, ImageView imageView){
    	imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
        	imageView.setImageBitmap(bitmap);
        }else{
        	queuePhoto(url, imageView);
        	//imageView.setImageResource(stub_id);
        	imageView.setImageDrawable(stub_id);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView){
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url){
    	//Bitmap.
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null){
            return Bitmap.createBitmap(b);
//            return Bitmap.createScaledBitmap(b, 150, 150, true);
        }

        //from web
        try {
            Bitmap bitmap=null;
            InputStream is=fetch(url);
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return Bitmap.createBitmap(bitmap);
//            return Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        }catch (Throwable ex){
        	Log.e(AppConstant.AppName,ex.toString());
        	ex.printStackTrace();
        	if(ex instanceof OutOfMemoryError){
            	Log.e(AppConstant.AppName,"ERROR OUT OF MEMORY");
        		memoryCache.clear();
        	}
        	return null;
        }
    }

    
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=512;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        	Log.e(AppConstant.AppName,"FileNotFoundException "+e);
        }catch (IOException e) {
        	Log.e(AppConstant.AppName,"IOException "+e);
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable{
    	Bitmap bitmap;
    	PhotoToLoad photoToLoad;
    	
    	public BitmapDisplayer(Bitmap b, PhotoToLoad p){
    		bitmap=b;
    		photoToLoad=p;
    	}
        
    	public void run(){
    		if(imageViewReused(photoToLoad))
    			return;
    		if(bitmap!=null)
    			photoToLoad.imageView.setImageBitmap(bitmap);
    		else
//    			photoToLoad.imageView.setImageResource(stub_id);
    			photoToLoad.imageView.setImageDrawable(stub_id);
    	}
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }
    
    public void clearCache() {
    	memoryCache.clear();
    	fileCache.clear();
    }
}
