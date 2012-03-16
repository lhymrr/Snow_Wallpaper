package com.olleh.snowwallpaper;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * @author liuheyuan
 * 
 */
public class SnowWallpaper extends WallpaperService {
	
	public static final String SHARED_PREFS_NAME="baosettings";

	/**
	 * 存放手指滑动时的雪花
	 */
	public static List<SnowData> SnowList = new ArrayList<SnowData>();

	/**
	 * 屏幕中自动飞舞的雪花
	 */
	public static List<AISnowData> aiSnowList = null;

	/**
	 * 屏幕宽度
	 * 
	 * @Default 0;
	 */
	public static int ScreenWidth = 0;

	/**
	 * 屏幕高度
	 * 
	 * @Default 0;
	 */
	public static int ScreenHeight = 0;

	/**
	 * 智能雪花密度
	 */
	public static int AI_SNOW_DENSITY = 8;

	/**
	 * 心跳速率
	 */
	public static final int HEART_SPEED = 20;

	/**
	 * 心跳计数器(文字)
	 */
	public static int wzCount = 0;

	/**
	 * 心跳计数器(树)
	 */
	public static int shuCount = 0;

	/**
	 * 雪花大小
	 */
	public static int SNOW_SIZE = 2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.service.wallpaper.WallpaperService#onCreateEngine()
	 * 
	 * 生命周期，创建雪花引擎
	 */
	@Override
	public Engine onCreateEngine() {
		return new SnowEngine();
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 自定义动态壁纸引擎
	 * 
	 * @author liuheyuan
	 * 
	 */
	class SnowEngine extends Engine {

		public SnowEngine() {

		}

		/**
		 * 动态壁纸主背景
		 */
		private Bitmap bgBitmap = null;

		/**
		 * 动态壁纸背景第一棵树
		 */
		private Bitmap bgBitmap_a = null;

		/**
		 * 动态壁纸背景第二颗树
		 */
		private Bitmap bgBitmap_b = null;

		/**
		 * 雪花图片
		 */
		private Bitmap snowBitmap = null;

		/**
		 * 显示的文字图片
		 */
		private Bitmap wzBitmap = null;

		/**
		 * 白色文字图片
		 */
		private Bitmap wzBitmap_w = null;

		/**
		 * 蓝色文字图片
		 */
		private Bitmap wzBitmap_b = null;

		private Handler mHandler = null;
		private Paint mPaint = null;

		/**
		 * 手指在屏幕上滑动时存放坐标X 根据position 与 position -1 的大小判断手指向左滑动还是向右滑动
		 */
		private List<Integer> WhereList = new ArrayList<Integer>();

		/**
		 * 手指滑动时产生的雪花最大值
		 */
		public static final int MAX_FEELING_SNOW = 200;
		

		/**
		 * 心跳线程 ，心跳速度每20ms一次
		 */
		Runnable HeartThread = new Runnable() {

			@Override
			public void run() {

				doPlay();
			}
		};

		private void doPlay() {
			/** 获得画布 */
			Canvas c = getSurfaceHolder().lockCanvas();
			if (null == c) {
				return;
			}

			/** 生成智能雪花 */
			if (null == aiSnowList) {
				aiSnowList = new ArrayList<AISnowData>();
				int lw = ScreenWidth / AI_SNOW_DENSITY, lh = ScreenHeight / AI_SNOW_DENSITY;
				int w = ScreenWidth / AI_SNOW_DENSITY, h = ScreenHeight / AI_SNOW_DENSITY;
				for (int i = 0; i < AI_SNOW_DENSITY; i++) {
					h = lh;
					for (int j = 0; j < AI_SNOW_DENSITY; j++) {
						AISnowData aiData = new AISnowData(w, h);
						aiSnowList.add(aiData);
						h += lh;
					}
					w += lw;
				}
			}

			/** 保存画布初始状态 */
			c.save();

			/** 画背景图片 */
			c.drawBitmap(bgBitmap, 0, 0, mPaint);

			/** 画文字 */
			if (null != wzBitmap)
				c.drawBitmap(wzBitmap, 0, 0, mPaint);

			/** 当手指滑动产生的雪花已经移除屏幕，将删除此雪花 */
			for (int i = 0; i < SnowList.size(); i++) {
				SnowList.get(i).deleteMe();
			}

			/** 移除超过屏幕的雪花 */
			List<SnowData> tmp = new ArrayList<SnowData>();
			for (int i = 0; i < SnowWallpaper.SnowList.size(); i++) {
				SnowData d = SnowWallpaper.SnowList.get(i);
				if (null != d) {
					tmp.add(d);
				}
			}
			SnowWallpaper.SnowList = tmp;

			/** 在屏幕上绘制手指滑过的雪花 */
			for (int i = 0; i < SnowList.size(); i++) {
				Log.d("LHY", "snowFor");
				SnowList.get(i).update();

				c.drawBitmap(snowBitmap, SnowList.get(i).getPathX(), SnowList.get(i).getPathY(), mPaint);
			}

			/** 在屏幕上绘制智能雪花 */
			for (int i = 0; i < aiSnowList.size(); i++) {
				aiSnowList.get(i).update();
				c.drawBitmap(snowBitmap, aiSnowList.get(i).getPathX(), aiSnowList.get(i).getPathY(), mPaint);
			}

			/** 恢复画布初始状态 */
			c.restore();

			/** 解锁画布 */
			getSurfaceHolder().unlockCanvasAndPost(c);

			wzCount++;
			if (wzCount > 5) {
				wzCount = 0;
				if (wzBitmap == wzBitmap_b) {
					wzBitmap = wzBitmap_w;
				} else {
					wzBitmap = wzBitmap_b;
				}
			}

			shuCount++;
			if (shuCount > 20) {
				shuCount = 0;
				if (bgBitmap == bgBitmap_a) {
					bgBitmap = bgBitmap_b;
				} else {
					bgBitmap = bgBitmap_a;
				}
			}
			/** 启动下一次心跳 */
			mHandler.removeCallbacks(HeartThread);
			mHandler.postDelayed(HeartThread, HEART_SPEED);
		}

		@Override
		public boolean isVisible() {
			return super.isVisible();
		}

		@Override
		public boolean isPreview() {
			return super.isPreview();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			mHandler = new Handler();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if(visible){
				doPlay();
			}else{
				mHandler.removeCallbacks(HeartThread);
			}
		}

		@Override
		public void onTouchEvent(final MotionEvent event) {
			super.onTouchEvent(event);

			/** 如果手指滑过的雪花超过最大值，则不添加雪花 */
			if (SnowList.size() > MAX_FEELING_SNOW)
				return;

			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				SnowList.add(new SnowData((int) event.getX(), (int) event.getY(), SnowData.WHERE.W_NONE));

				WhereList.clear();
				WhereList.add((int) (event.getX()));

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

				WhereList.add((int) (event.getX()));

				if (WhereList.size() > 2) {
					if (WhereList.get(WhereList.size() - 1) > WhereList.get(WhereList.size() - 2)) {
						// 向右滑动
						SnowList.add(new SnowData((int) event.getX(), (int) event.getY(), SnowData.WHERE.W_RIGHT));
					} else if (WhereList.get(WhereList.size() - 1) < WhereList.get(WhereList.size() - 2)) {
						// 向左滑动
						SnowList.add(new SnowData((int) event.getX(), (int) event.getY(), SnowData.WHERE.W_LEFT));
					} else {
						SnowList.add(new SnowData((int) event.getX(), (int) event.getY(), SnowData.WHERE.W_NONE));
					}
				}

			}

		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);

			// Surface的宽高即为屏幕的宽高
			ScreenHeight = height;
			ScreenWidth = width;

			// // 创建背景图片，设置背景图片为屏幕大小
			// bgBitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.bg);
			// bgBitmap = Bitmap.createScaledBitmap(bgBitmap, width, height,
			// false);

			// 创建第一张树背景图片，设置背景图片为屏幕大小
			bgBitmap_a = BitmapFactory.decodeResource(getResources(), R.drawable.shua);
			bgBitmap_a = Bitmap.createScaledBitmap(bgBitmap_a, width, height, false);

			// 创建第二张树背景图片，设置背景图片为屏幕大小
			bgBitmap_b = BitmapFactory.decodeResource(getResources(), R.drawable.shub);
			bgBitmap_b = Bitmap.createScaledBitmap(bgBitmap_b, width, height, false);

			bgBitmap = bgBitmap_a;

			// 创建雪花图片，并设置雪花
			snowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
			snowBitmap = Bitmap.createScaledBitmap(snowBitmap, width / 100 * SNOW_SIZE, width / 100 * SNOW_SIZE, false);

			// 创建白色文字图片
			wzBitmap_w = BitmapFactory.decodeResource(getResources(), R.drawable.zi_w);
			wzBitmap_w = Bitmap.createScaledBitmap(wzBitmap_w, width, height, false);

			// 创建蓝色文字图片
			wzBitmap_b = BitmapFactory.decodeResource(getResources(), R.drawable.zi_b);
			wzBitmap_b = Bitmap.createScaledBitmap(wzBitmap_b, width, height, false);

			wzBitmap = wzBitmap_b;

			mPaint = new Paint();

			// 设置抗锯齿
			mPaint.setAntiAlias(true);

			// 首次启动心跳线程
			mHandler.removeCallbacks(HeartThread);
			doPlay();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);

		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
		}

	}
}
