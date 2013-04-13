package edu.cornell.opencomm.util;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**This util class should be used to get all bitmaps of user images 
 * failure to do so may lead to OOM exceptions 
 * for future reference please see
 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 * 
 * @author Ankit Singh:as2536
 *
 */
public final class OCBitmapDecoder {
	public static final  int THUMBNAIL_WIDTH  = 130;
	public static final  int THUMBNAIL_HEIGTH = 130;
	/**
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int resizeImage(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}
		return inSampleSize;
	}
	public static Bitmap getThumbnailFromResource(Resources res, int resId){
		return getImageFromResource(res, resId, THUMBNAIL_WIDTH,THUMBNAIL_HEIGTH);
	}
	public static Bitmap getThumbnailFromByteArray(byte[] byteArray){
		return getImageFromByteArray(byteArray, THUMBNAIL_WIDTH, THUMBNAIL_HEIGTH);
	}
	public static Bitmap getThumbnailFromInputStream(InputStream stream){
		return getImageFromImageStream(stream, THUMBNAIL_WIDTH, THUMBNAIL_HEIGTH);
	}
	/**
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap getImageFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = resizeImage(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	public static Bitmap getImageFromImageStream(InputStream stream,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, options);
		options.inSampleSize = resizeImage(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}
	/**
	 * @param byteArray
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap getImageFromByteArray(byte[] byteArray,
			int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
		options.inSampleSize = resizeImage(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
	}
}
