package com.olleh.snowwallpaper;

/**
 * 手指滑过屏幕产生的雪花数据类型
 * 
 * @author liuheyuan
 * 
 */
public class SnowData {

	/**
	 * 雪花X坐标
	 */
	protected int pathX = 0;

	/**
	 * 雪花Y坐标
	 */
	protected int pathY = 0;

	/**
	 * 雪花方向
	 */
	protected WHERE mWHERE = WHERE.W_NONE;

	/**
	 * 构造方法，首次指定雪花X,Y坐标
	 * 
	 * @param firstX
	 * @param firstY
	 */
	public SnowData(int firstX, int firstY) {
		this.pathX = firstX;
		this.pathY = firstY;
	}

	/**
	 * 构造方法，首次制定雪花X,Y坐标，并制定方向
	 * 
	 * @param firstX
	 * @param firstY
	 * @param mWHERE
	 */
	public SnowData(int firstX, int firstY, WHERE mWHERE) {
		this.pathX = firstX;
		this.pathY = firstY;
		this.mWHERE = mWHERE;
	}

	/**
	 * 方向枚举类型
	 * 
	 * @author liuheyuan
	 * 
	 */
	public static enum WHERE {

		/**
		 * 方向为左
		 */
		W_LEFT,

		/**
		 * 方向为右
		 */
		W_RIGHT,

		/**
		 * 没有方向
		 */
		W_NONE,

		/**
		 * 方向不确定
		 */
		W_AI

	}

	public int getPathX() {
		return pathX;
	}

	public void setPathX(int pathX) {
		this.pathX = pathX;
	}

	public int getPathY() {
		return pathY;
	}

	public void setPathY(int pathY) {
		this.pathY = pathY;
	}

	/**
	 * 更新雪花位置
	 */
	public void update() {

		switch (mWHERE) {
		case W_LEFT: {
			pathX -= (Math.random() * 5);
			pathY += (Math.random() * 10);
			break;
		}
		case W_RIGHT: {
			pathX += (Math.random() * 5);
			pathY += (Math.random() * 10);
			break;
		}
		case W_NONE: {
			pathY += (Math.random() * 10);
			break;
		}
		}

	}

	/**
	 * 如果雪花的坐标超出屏幕将删除此雪花
	 */
	public void deleteMe() {
		if (pathX > SnowWallpaper.ScreenWidth || pathY > SnowWallpaper.ScreenHeight) {
			SnowWallpaper.SnowList.remove(this);
		}
		return;
	}
}
