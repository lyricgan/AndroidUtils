package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.lyric.android.app.widget.AddPicturePopup;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 添加图片工具类
 * 
 * @author Lyric Gan
 * @created 2014-8-13
 * 
 */
public class AddPictureUtils {
	/** 返回码：拍照 */
	public static final int REQUEST_CODE_TAKE_PHOTO = 1 << 2;
	/** 返回码：相册*/
	public static final int REQUEST_CODE_PHOTO_ALBUM = 2 << 2;
    private static final String _JPG = ".jpg";
    private static final String _DEFAULT_AVATAR_PATH = "user_avatar_default" + _JPG;

    private static AddPictureUtils mInstance;
    private Context mContext;
    private AddPicturePopup mPopupWindow;
    private String mAvatarPath;
    private Uri mAvatarUri;

    public static AddPictureUtils getInstance() {
        if (mInstance == null) {
            synchronized(AddPictureUtils.class) {
                if (mInstance == null) {
                    mInstance = new AddPictureUtils();
                }
            }
        }
        return mInstance;
    }

    public void initialize(Context context) {
        this.mContext = context;
        initializeDirectory();
    }

    public Context getContext() {
        return this.mContext;
    }

    public void showPopup(View view) {
        if (mPopupWindow == null) {
            mPopupWindow = new AddPicturePopup(mContext);
        }
        if (mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow.show(view);
    }

    public String getAvatarPath() {
        if (TextUtils.isEmpty(mAvatarPath)) {
            mAvatarPath = getCacheDirectory() + File.separator + _DEFAULT_AVATAR_PATH;
        }
        return this.mAvatarPath;
    }

    public Uri getAvatarUri() {
        if (mAvatarUri == null) {
            mAvatarUri = Uri.fromFile(new File(getAvatarPath()));
        }
        return this.mAvatarUri;
    }

    public void setOnMenuClickListener(AddPicturePopup.OnMenuClickListener listener) {
        if (mPopupWindow == null) {
            mPopupWindow = new AddPicturePopup(getContext());
        }
        mPopupWindow.setOnMenuClickListener(listener);
    }

    private Intent getTakePhotoIntent(Uri photoUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return intent;
    }

    private Intent getPhotoAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public void takePhotoForAvatar(Activity activity) {
        activity.startActivityForResult(getTakePhotoIntent(getAvatarUri()), REQUEST_CODE_TAKE_PHOTO);
    }

    public void takePhotoForAvatar(Fragment fragment) {
        fragment.startActivityForResult(getTakePhotoIntent(getAvatarUri()), REQUEST_CODE_TAKE_PHOTO);
    }

    public void takePhoto(Activity activity, Uri photoUri) {
        activity.startActivityForResult(getTakePhotoIntent(photoUri), REQUEST_CODE_TAKE_PHOTO);
    }

    public void takePhoto(Fragment fragment, Uri photoUri) {
        fragment.startActivityForResult(getTakePhotoIntent(photoUri), REQUEST_CODE_TAKE_PHOTO);
    }

    public void openPhotoAlbum(Activity activity) {
        activity.startActivityForResult(getPhotoAlbumIntent(), REQUEST_CODE_PHOTO_ALBUM);
    }

    public void openPhotoAlbum(Fragment fragment) {
        fragment.startActivityForResult(getPhotoAlbumIntent(), REQUEST_CODE_PHOTO_ALBUM);
    }

	private boolean isSdCardExists() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

    public void destroy() {
        this.mContext = null;
        this.setOnMenuClickListener(null);
        this.mPopupWindow = null;
    }
	
	private void initializeDirectory() {
        createDirectory(getCacheDirectory());
    }

	private boolean createDirectory(String dirPath) {
        File file = new File(dirPath);
        return file.exists() || file.mkdirs();
    }

    private String getCacheDirectory() {
        return getRootDirectory() + "/Android/data/" + getContext().getPackageName() + "/" + "cache";
    }

	/**
	 * 获取文件根目录
	 * @return 文件根目录
	 */
	private String getRootDirectory() {
		File sdCardDirectory = null;
		if (isSdCardExists()) {
			sdCardDirectory = Environment.getExternalStorageDirectory();
		}
		if (sdCardDirectory != null) {
			return sdCardDirectory.toString();
		} else {
			return "";
		}
	}

    public void delete() {
        delete(getCacheDirectory(), _JPG);
    }

    public void delete(String dirPath) {
        delete(dirPath, _JPG);
    }
	
	/**
	 * 删除图片文件
	 * @param dirPath 图片文件目录
	 * @param suffix 文件后缀名
	 */
	public void delete(String dirPath, String suffix) {
		// 判断文件后缀名是否为空
		if (TextUtils.isEmpty(suffix)) {
			suffix = _JPG;
		}
		File fileDir = new File(getCacheDirectory() + File.separator + dirPath);
		File[] fileArray = fileDir.listFiles();
		// 判断文件数组是否为空
		if (fileArray == null || fileArray.length <= 0) {
			return;
		}
        for (File file : fileArray) {
            if (file.getName().contains(suffix)) {
                file.delete();
            }
        }
	}
	
	public Bitmap getBitmap(String filePath) {
		Bitmap bitmap = null;
		File file = new File(filePath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
			bitmap = BitmapFactory.decodeFile(filePath, options);
		}
		return bitmap;
	}

    private boolean createFile(String filePath) throws IOException {
        File file = new File(filePath);
        return file.exists() || file.createNewFile();
    }

    public Bitmap getBitmap(Intent data, int width, int height, String toPath) {
        Bitmap bitmap = null;
        Uri uri = data.getData();
        if (uri == null) {
            return null;
        }
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            String photoPath = cursor.getString(1);
            cursor.close();
            bitmap = getBitmap(photoPath, width, height, toPath);
        }
        return bitmap;
    }
	
	/**
	 * 获取本地图片文件
	 * @param fromPath 图片来源路径
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param toPath 图片另存路径
	 * @return Bitmap
	 */
	public Bitmap getBitmap(String fromPath, int width, int height, String toPath) {
		Bitmap bitmap = null;
		if (width <= 0 || height <= 0) {
			return null;
		}
		File file = new File(fromPath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getPath(), options);
			final int minSideLength = Math.min(width, height);
			options.inSampleSize = computeInSampleSize(options, minSideLength, width * height);
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
            // 获取图片旋转角度，并处理图片旋转
			int degree = ImageUtils.readImageDegree(file.getAbsolutePath());
			bitmap = BitmapFactory.decodeFile(fromPath, options);
			if (degree > 0) {
				bitmap = ImageUtils.rotateBitmap(bitmap, degree);
			}
			OutputStream outputStream = null;
            try {
                if (createFile(toPath)) {
                    outputStream = new BufferedOutputStream(new FileOutputStream(toPath));
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)) {
                        outputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
		return bitmap;
	}

    private int computeInSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int scaleSize;
        double outWidth = options.outWidth;
        double outHeight = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(outWidth * outHeight / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(outWidth / minSideLength), Math.floor(outHeight / minSideLength));

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            scaleSize = 1;
        } else if (minSideLength == -1) {
            scaleSize = lowerBound;
        } else {
            scaleSize = upperBound;
        }
        int inSampleSize;
        if (scaleSize <= 8) {
            inSampleSize = 1;
            while (inSampleSize < scaleSize) {
                inSampleSize <<= 1;
            }
        } else {
            inSampleSize = (scaleSize + 7) / 8 * 8;
        }
        return inSampleSize;
    }

    /**
     * 将图片存入文件缓存
     * @param bitmap 图片
     * @param filePath 图片路径
     */
    public void putBitmap(Bitmap bitmap, String filePath) {
        if (bitmap == null || TextUtils.isEmpty(filePath)) {
            return;
        }
        String dir = getCacheDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            return;
        }
        File file = new File(filePath);
        OutputStream stream = null;
        try {
            if (!file.exists() && !file.createNewFile()) {
                return;
            }
            stream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
