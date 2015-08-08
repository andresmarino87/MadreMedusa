package com.madremedusa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.madremedusa.R;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.client.ClientProtocolException;

public class Utils {
	private Context context;
	 
    // constructor
    public Utils(Context context) {
        this.context = context;
    }
 
    // Reading file paths from SDCard
    public ArrayList<String> getFilePathsCovers(boolean isCover, String magazinePath) {
        ArrayList<String> filePaths = new ArrayList<String>();
        File directory;
        if(isCover){
        	directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.image_directory));
        }else{
        	directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.image_directory) + File.separator + magazinePath);
        }
        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();
            // Check for count
            if (listFiles.length > 0) {
                // loop through all files
                for (int i = 0; i < listFiles.length; i++) {
                    // get file path
                    String filePath = listFiles[i].getAbsolutePath();
                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                       filePaths.add(filePath);
                   }
                }
            } else {
                // image directory is empty
                Toast.makeText(context,
                        context.getString(R.string.image_directory) + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }
 
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Error!");
            alert.setMessage(context.getString(R.string.image_directory)+ " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
 
        return filePaths;
    }
 
    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),filePath.length());
        if(AppConstant.EXT.equalsIgnoreCase(ext))
            return true;
        else
            return false;
    }
 
    /*
     * getting screen width
     */
/*    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }*/
    
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        JSONObject json=null;
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "text/plain; charset=utf-8");
        get.setHeader("Expect", "100-continue");
        HttpResponse resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            resp = httpClient.execute(get);
        } catch (ClientProtocolException e) {
            Log.e(AppConstant.AppName, "HTTP protocol error", e);
        } catch (IOException e) {
            Log.e(AppConstant.AppName, "Communication error", e);
        }
        if (resp != null){
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONObject(jsonText);
        } else {
            json = new JSONObject();
        }
        return json;
	}
	
    public static void CopyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
