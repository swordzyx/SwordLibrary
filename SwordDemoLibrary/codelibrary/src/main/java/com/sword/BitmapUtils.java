package com.sword;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.YuvImage;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

/**
* 图像工具类
**/
public class BitmapUtils {

	Context mContext;
	int screenWidth;
	int screenHeight;
	int screenSize;
	private Activity mActivity;

	public void init(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;

		//获取屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		screenSize = screenWidth * screenHeight;
	}



	/**
	* 将 bitmap1 和 bitmap2 合成一个图像，将 bitmap2 放到 bitmap1 的左下角
	*
	* @param bitmap1 位图1
	* @param bitmap2 位图2
	* @return Bitmap*/
	public static Bitmap createBitmap(Bitmap bitmap1, Bitmap bitmap2) {
		int w1 = bitmap1.getWidth();
		int h1 = bitmap1.getHeight();
		int w2 = bitmap2.getWidth();
		int h2 = bitmap2.getHeight();

		Bitmap newBitmap = Bitmap.createBitmap(w1, h1, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);

		canvas.drawBitmap(bitmap1, 0, 0, null);
		canvas.drawBitmap(bitmap2, w1 - w2, h1 - h2, null);
		canvas.save();//保存 Canvas.ALL_SAVE_FLAG
		canvas.restore();//存储到 Bitmap

		return newBitmap;
	}


	/**
	* 将图片缩放到指定的宽高
	*
	* @param bitmap 原始图像
	* @param w 目标宽
	* @param h 目标高
	* @return 缩放到指定宽高的 Bitmap
	**/
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int oldw = bitmap.getWidth();
		int oldh = bitmap.getHeight();

		Matrix matrix = new Matrix();
		int widthScale = (int)(w / oldw);
		int heightScale = (int)(h / oldh);
		matrix.postScale(widthScale, heightScale);

		return Bitmap.createBitmap(bitmap, 0, 0, oldw, oldh, matrix, true);
	}


	/**
	* 旋转图片
	*
	* @param bitmap 原始图片
	* @param angle 旋转的角度
	* @return 旋转后的图像
	**/
	public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}


	/**
	* 将给定的 Bitmap 裁剪成指定大小的圆
	* 
	* @param bitmap 原始图像
	* @param isStroke 是否描边
	* @param strokeWidth 描边的宽度
	* @param radius 生成的圆形图像的半径
	* @param strokeColor 描边的颜色
	* @param startX 原始图像裁剪圆的起始 x 坐标
	* @param startY 原始图像裁剪圆的起始 Y 坐标
	* @return 圆形的图像
	**/
	public static Bitmap createCircleBitmap(Bitmap bitmap, boolean isStroke, int strokeWidth, int radius, int strokeColor, int startX, int startY) {
		Bitmap circleBitmap = Bitmap.createBitmap(radius*2, radius*2, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(circleBitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		//裁剪圆
		canvas.drawCircle(radius, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, startX, startY, paint);
		paint.setXfermode(null);

		//描边
		if (isStroke) {
			paint.setColor(strokeColor);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(radius, radius, radius, paint);
		}

		canvas.save();
		canvas.restore();
		return circleBitmap;
	}


	/**
	* 将 Bitmap 转成指定宽高的圆角矩形
	* 
	* @param bitmap 原始图像
	* @param isStroke 是否描边
	* @param strokeWidth 描边的宽度
	* @param strokeColor 描边的颜色
	* @param cornerRadius 圆角的半径
	* @param width 圆角矩形的宽度
	* @param height 圆角矩形的高度
	* @param startX 开始裁剪的 X 坐标
	* @param startY 开始裁剪的 Y 坐标
	**/
	public static Bitmap createCornerBitmap(Bitmap bitmap, boolean isStroke, int strokeWidth, int strokeColor, int cornerRadius, int width, int height, int startX, int startY) {
		Bitmap cornerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(cornerBitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		RectF rect = new RectF(0, 0, width, height);
		canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, startX, startY, paint);
		paint.setXfermode(null);

		if (isStroke) {
			paint.setColor(strokeColor);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(strokeWidth);
			canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
		}

		canvas.save();
		canvas.restore();
		return cornerBitmap;
	}

	/**
	* 按比例裁剪图片 
	* 
	* @param bitmap 原始位图
	* @param wScale 裁剪宽度的比例
	* @param hScale 裁剪高度的比例
	* @return 返回裁剪后的位图
	**/
	public static Bitmap cropBitmap(Bitmap bitmap, float wScale, float hScale) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int wh = (int) (w * wScale);
		int hw = (int) (h * hScale);
		int retX = (int) (w * (1 - wScale) / 2);
		int retY = (int) (h * (1 - hScale) / 2);
		//retX 和 retY 是裁剪的起始 X 坐标和 Y 坐标
		return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, false);
	}


	/**
	* 为 Bitmap 添加一个倒影
	*
	**/
	public static Bitmap createReflectionBitmap(Bitmap bitmap, float region) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);//镜像缩放
		Bitmap reflectionBitmap = Bitmap.createBitmap(bitmap, 0, (int)(height*(1-region)), width, (int)(height*region), matrix, false);
		Bitmap reflectionWidthBitmap = Bitmap.createBitmap(width, height + (int)(height * region), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(reflectionWidthBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(reflectionBitmap, 0, height, null);

		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, reflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawRect(0, height, width, reflectionWidthBitmap.getHeight(), paint);
		return reflectionWidthBitmap;
	}


	/**
	* 图片质量压缩
	*
	**/
	public static Bitmap compressBitmap(Bitmap bitmap, float many){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, (int)many*100, baos);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return BitmapFactory.decodeStream(isBm, null, null);
	}


	/**
	* 高级图片质量压缩
	* 
	* @param bitmap 位图
	* @param maxSize 压缩后的大小，单位kb
	*/
	public static Bitmap imageZoom(Bitmap bitmap, double maxSize) {
		// 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 格式、质量、输出流
		bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 获取bitmap大小 是允许最大大小的多少倍
		double i = mid / maxSize;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		doRecycledIfNot(bitmap);
		if (i < 1) {
			// 缩放图片 此处用到平方根 将宽度和高度压缩掉对应的平方根倍
			// （保持宽高不变，缩放后也达到了最大占用空间的大小）
			return scaleWithWH(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i));
		}
		return null;
	}

	/***
	* 图片缩放
	*@param bitmap 位图
	* @param w 新的宽度
	* @param h 新的高度
	* @return Bitmap
	*/
	public static Bitmap scaleWithWH(Bitmap bitmap, double w, double h) {
		if (w == 0 || h == 0 || bitmap == null) {
			return bitmap;
		} else {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			Matrix matrix = new Matrix();
			float scaleWidth = (float) (w / width);
			float scaleHeight = (float) (h / height);
			matrix.postScale(scaleWidth, scaleHeight);

			return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
	}

	/**
	* YUV视频流格式转bitmap
	* @param data YUV视频流格式
	* @return width 设置宽度
	* @return width 设置高度
	*/
	public static Bitmap getBitmap(byte[] data, int width, int height) {
		Bitmap bitmap;
		YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);

		//data 是 onPreviewFrame 参数提供
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, baos);

		// 80--JPG图片的质量[0-100],100最高
		byte[] rawImage = baos.toByteArray();
		BitmapFactory.Options options = new BitmapFactory.Options();
		SoftReference<Bitmap> softRef = new SoftReference<Bitmap> (BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options));
		bitmap = softRef.get();
		return bitmap;
	}


	/**
	* 图片路径转bitmap
	* @param file 图片的绝对路径
	* @return bitmap
	*/
	public Bitmap getAssetImage(String file) {
		Bitmap bitmap = null;
		AssetManager am = mActivity.getAssets();
		try {
			InputStream is = am.open(file);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}


	/**
	* bitmap 保存到指定路径
	* @param file 图片的绝对路径
	* @param file 位图
	* @return bitmap
	*/
	public static boolean saveFile(String file, Bitmap bmp) {
		if(TextUtils.isEmpty(file) || bmp == null) return false;
		File f = new File(file);
		if (f.exists()) {
			f.delete();
		}else {
			File p = f.getParentFile();
			if(!p.exists()) {
				p.mkdirs();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	* 回收一个未被回收的Bitmap
	*@param bitmap
	*/
	public static void doRecycledIfNot(Bitmap bitmap) {
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}

}