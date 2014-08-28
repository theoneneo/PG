package com.neo.prettygirl.tools;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * 图片橡皮擦工具
 * 
 * @author bo.sun
 * 
 */
public class ImageEraserView extends View {

	private RectF mImgDesRect;

	private Bitmap mImgBitmap = null;
	private Bitmap mEraseMaskBitmap = null;

	private Path mErasePath = null;
	private Paint mErasePaint = null;
	private Canvas mCanvas = null;

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	private OnDrawAreaRateCallback callback; // 回调函数

	private int mBitmapWidth = 0; // 擦除画布的宽
	private int mBitmapHeight = 0; // 擦除画布的高

	int mArrayColorLengh = 0; // 擦除画布总的像素数
	private ComptureTask mTask; //画图回调函数
	private boolean mCanDraw = true; //标识当前画布是否可擦除
	private float mDensity;
	
	public ImageEraserView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImageEraserView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageEraserView(Context context) {
		super(context);
	}


	public void setOnDrawAreaRateCallback(OnDrawAreaRateCallback callback) {
		this.callback = callback;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mImgBitmap != null) {
			Paint paint = new Paint();
			int sc = canvas.saveLayer(mImgDesRect.left, mImgDesRect.top,
					mImgDesRect.right, mImgDesRect.bottom, null,
					Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
							| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
							| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
							| Canvas.CLIP_TO_LAYER_SAVE_FLAG); // 创建中间图层
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
			canvas.drawBitmap(mImgBitmap, new Rect(0, 0, mImgBitmap.getWidth(),
					mImgBitmap.getHeight()), mImgDesRect, null);
			canvas.drawBitmap(mEraseMaskBitmap,
					new Rect(0, 0, mEraseMaskBitmap.getWidth(),
							mEraseMaskBitmap.getHeight()), mImgDesRect, paint);
			paint.setXfermode(null);
			canvas.restoreToCount(sc);
		}

	}

	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if(visibility==View.GONE){
			mCanDraw = false;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX() - mImgDesRect.left;
		float y = event.getY() - mImgDesRect.top;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mErasePath.reset();
			mErasePath.moveTo(x, y);
			mX = x;
			mY = y;
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mErasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
				// commit the path to offscreen
				mCanvas.drawPath(mErasePath, mErasePaint);
				if (mTask == null) {
					// 计算累计画的面积所占总的面积比例
					mTask = new ComptureTask();
					mTask.execute();
				} else {
					if (mTask.runningFlag == 2) {
						mTask = null;
						mTask = new ComptureTask();
						mTask.execute();
					}
				}
			}
			invalidate(); // refresh

			break;
		case MotionEvent.ACTION_UP:
			mErasePath.lineTo(mX, mY);
			mCanvas.drawPath(mErasePath, mErasePaint);
			mErasePath.reset();
			invalidate();
			if (mTask == null) {
				// 计算累计画的面积所占总的面积比例
				mTask = new ComptureTask();
				mTask.execute();
			} else {
				if (mTask.runningFlag == 2) { //
					mTask = null;
					mTask = new ComptureTask();
					mTask.execute();
				}
			}
			break;
		}
		return true;
	}

	// 指定大小压缩圖片
	public static Bitmap getBitmap(String path, int size) {
		Options op = new Options();
		op.inSampleSize = size;
		op.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bt = BitmapFactory.decodeFile(path, op);
		return bt;
	}

	/**
	 * 计算bitmap的画布使用比例
	 * 
	 * @param bitmap
	 * @return
	 */
	private int countBitmapSize(Bitmap bitmap) {
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];// 保存所有的像素的数组，图片宽×高

		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
				bitmap.getHeight());
		int count = 0;
		for (int i = 0; i < pixels.length; i++) {
			int clr = pixels[i];
			if (clr != 0) { // 着色的点记录下来
				count++;
			}
		}
		return ((int) (count * 1.0f / pixels.length * 100));
	}

	/**
	 * 初始化画笔
	 */
	private void initErasePaint() {
		if(mDensity==0){
			mDensity = 2.0f;
		}
		mErasePaint = new Paint();
		mErasePaint.setAntiAlias(false); //设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
		mErasePaint.setDither(false); //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰 
		mErasePaint.setColor(0xF0000000); //设置透明色
		mErasePaint.setPathEffect(new CornerPathEffect(mDensity*5)); // 可以使用圆角来代替尖锐的角从而对基本图形的形状尖锐的边角进行平滑。
		mErasePaint.setStyle(Paint.Style.STROKE);
		mErasePaint.setStrokeJoin(Paint.Join.ROUND);
		mErasePaint.setStrokeCap(Paint.Cap.ROUND);
		mErasePaint.setStrokeWidth(mDensity*60); // 设置画笔直径
		BlurMaskFilter blur = new BlurMaskFilter(mDensity*15, BlurMaskFilter.Blur.NORMAL);   //指定一个模糊的样式和半径来处理Paint的边缘。 
		mErasePaint.setMaskFilter(blur); // 应用mask
	}

	/**
	 * 初始化（重置）画笔
	 * 
	 * @param resId
	 *            本地图片资源id
	 */
	public void prepare(Activity context,int resId) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mDensity = dm.density;
		mImgDesRect = new RectF(0, 0, dm.widthPixels, dm.heightPixels); // 全屏显示蒙版
		if (!decodeBitmap(resId)) {
			return;
		}
		prepareInit();
	}

	/**
	 * 初始化（重置）画笔
	 * 
	 * @param bitmap
	 *            作为蒙版的新的图片对象
	 */
	public void prepare(Bitmap bitmap) {
		mImgBitmap = bitmap;
		prepareInit();
	}

	/**
	 * 调用异步任务线程计算更新UI
	 * 
	 * @author bo.sun
	 * 
	 */
	private class ComptureTask extends AsyncTask<Void, Object, Integer> {
		public int runningFlag = 0;

		@Override
		protected Integer doInBackground(Void... params) {
			runningFlag = 1;
			return countBitmapSize(mEraseMaskBitmap);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			runningFlag = 2;
			if (callback != null && mCanDraw) {
				callback.setRate(result);
			}
		}

	}

	/**
	 * 初始化方法
	 */
	private void prepareInit() {
		mErasePath = new Path();
		initErasePaint();
		if (mImgDesRect != null) {
			int w = (int) mImgDesRect.width();
			int h = (int) mImgDesRect.height();
			if (mEraseMaskBitmap == null) {
				mEraseMaskBitmap = Bitmap.createBitmap(w, h,
						Bitmap.Config.ARGB_8888);
				mEraseMaskBitmap.eraseColor(Color.TRANSPARENT);
				mBitmapWidth = w;
				mBitmapHeight = h;
				mArrayColorLengh = mBitmapWidth * mBitmapHeight;

			}
		}
		if (mEraseMaskBitmap != null) {
			mCanvas = new Canvas(mEraseMaskBitmap);
		}
		invalidate(); // refresh
	}

	/**
	 * 将本地图片资源转化成bitmap对象
	 * 
	 * @param resId
	 * @return
	 */
	private boolean decodeBitmap(int resId) {
		Resources res = this.getContext().getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		BitmapFactory.decodeResource(res, resId, options);

		if (options.outHeight > 0) {
			int width = 1000;
			int height = 1000;

			if ((width < options.outWidth) || (height < options.outHeight)) {
				int ratio = (options.outWidth > options.outHeight) ? (options.outWidth / width)
						: (options.outHeight / height);
				options.inSampleSize = ratio + 1;
			}

			options.inJustDecodeBounds = false;

			try {
				mImgBitmap = BitmapFactory.decodeResource(res, resId, options);

				if (mImgBitmap == null) {
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
			} finally {
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * 释放资源
	 */
	public void onFinish() {
		try{
			if (mImgBitmap != null) {
				mImgBitmap.recycle();
				mImgBitmap = null;
			}
			if (mEraseMaskBitmap != null) {
				mEraseMaskBitmap.recycle();
				mEraseMaskBitmap = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 回调函数,传回画的地方占总面积的百分比
	 * 
	 * @author bo.sun
	 * 
	 */
	public interface OnDrawAreaRateCallback {
		public void setRate(float rate);
	}

	
}
