package jp.ac.dendai.c.jtp.Graphics;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Goto on 2016/07/21.
 */
public class ImageReader {
    private static Activity _act;
    private static HashMap<String,Bitmap> images = new HashMap<>();
    public static void initImageReader(Activity act){
        _act = act;
    }
    public static Bitmap readImageToAssets(String fileName){
        if(images.containsKey(fileName)){
            return images.get(fileName);
        }
        final AssetManager assetManager = _act.getAssets();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(assetManager.open(fileName));
            Bitmap b = BitmapFactory.decodeStream(bis);
            images.put(fileName,b);
            return b;
        }catch(IOException e){
            throw  new RuntimeException(fileName+"の読み込みに失敗しました");
        }finally {
            try {
                bis.close();
            } catch (Exception e) {
                throw  new RuntimeException(fileName+"の読み込みに失敗しました");
            }
        }
    }
    public static Bitmap getImage(String fileName){
        if(images.containsKey(fileName))
            return images.get(fileName);
        else
            return null;
    }
    public static void deleteImage(String fileName){
        if(images.containsKey(fileName))
            images.remove(fileName);
    }
}
