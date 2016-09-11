package com.lyric.android.app.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;

/**
 * @author lyricgan
 * @description 网络图片加载类，{@link ImageModule}
 * @time 2016/6/3 13:23
 */
public class ImageLoader {
    private static final String CACHE_NAME = "cache_network_images";
    private static final int DISK_CACHE_SIZE = 200 * 1024 * 1024;

    private ImageLoader() {
    }

    public static void initialize(Context context) {
        GlideBuilder builder = new GlideBuilder(context);
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, CACHE_NAME, DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // 暂时这样处理
        if (!Glide.isSetup()) {
            Glide.setup(builder);
        }
    }

    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    public static void load(Context context, Uri uri, ImageView view) {
        Glide.with(context).load(uri).into(view);
    }

    public static void load(Activity activity, String url, ImageView view) {
        Glide.with(activity).load(url).into(view);
    }

    public static void load(FragmentActivity activity, String url, ImageView view) {
        Glide.with(activity).load(url).into(view);
    }

    public static void load(Fragment fragment, String url, ImageView view) {
        Glide.with(fragment).load(url).into(view);
    }

    public static void load(android.app.Fragment fragment, String url, ImageView view) {
        Glide.with(fragment).load(url).into(view);
    }

    public static void load(Context context, String url, ImageView view, int placeHolderId) {
        Glide.with(context).load(url).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void load(Context context, Uri uri, ImageView view, int placeHolderId) {
        Glide.with(context).load(uri).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void load(Activity activity, String url, ImageView view, int placeHolderId) {
        Glide.with(activity).load(url).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void load(FragmentActivity activity, String url, ImageView view, int placeHolderId) {
        Glide.with(activity).load(url).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void load(Fragment fragment, String url, ImageView view, int placeHolderId) {
        Glide.with(fragment).load(url).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void load(android.app.Fragment fragment, String url, ImageView view, int placeHolderId) {
        Glide.with(fragment).load(url).placeholder(placeHolderId).error(placeHolderId).crossFade().into(view);
    }

    public static void loadCircle(Context context, String url, final ImageView view, int placeHolderId) {
        Glide.with(context).load(url).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static void loadCircle(Context context, Uri uri, final ImageView view, int placeHolderId) {
        Glide.with(context).load(uri).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static void loadCircle(Activity activity, String url, final ImageView view, int placeHolderId) {
        Glide.with(activity).load(url).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static void loadCircle(FragmentActivity activity, String url, final ImageView view, int placeHolderId) {
        Glide.with(activity).load(url).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static void loadCircle(Fragment fragment, String url, final ImageView view, int placeHolderId) {
        Glide.with(fragment).load(url).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static void loadCircle(android.app.Fragment fragment, String url, final ImageView view, int placeHolderId) {
        Glide.with(fragment).load(url).placeholder(placeHolderId).error(placeHolderId).dontAnimate().into(view);
    }

    public static File getCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context, CACHE_NAME);
    }

    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public static Bitmap getBitmap(GlideDrawable glideDrawable) {
        if (glideDrawable instanceof GlideBitmapDrawable) {
            return ((GlideBitmapDrawable) glideDrawable).getBitmap();
        } else if (glideDrawable instanceof GifDrawable) {
            return ((GifDrawable) glideDrawable).getFirstFrame();
        }
        return null;
    }
}
