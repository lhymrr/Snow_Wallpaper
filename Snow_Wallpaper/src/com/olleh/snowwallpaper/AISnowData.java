package com.olleh.snowwallpaper;

/**
 * 智能雪花数据类型
 * 
 * @author liuheyuan
 * 
 */
public class AISnowData extends SnowData {

	/**
	 * X坐标每次前进的长度
	 */
	private int currentX = 0;

	/**
	 * Y坐标每次前进的长度
	 */
	private int currentY = 0;

	public AISnowData(int firstX, int firstY) {
		super(firstX, firstY);

		// 指定方向为W_AI，既不确定方向
		this.mWHERE = WHERE.W_AI;

		// 随机生成X,Y每次前进的长度
		this.currentX = getRandomX((int) (Math.random() * 2));
		this.currentY = getRandomY((int) (Math.random() * 3));
	}

	/**
	 * 获得X坐标随机数
	 * 
	 * @param r
	 * @return
	 */
	private int getRandomX(int r) {
		if (r != 0) {
			return r;
		} else {
			return getRandomX(-(int) (Math.random() * 2));
		}
	}

	/**
	 * 获得Y坐标随机数
	 * 
	 * @param r
	 * @return
	 */
	private int getRandomY(int r) {
		if (r != 0) {
			return r;
		} else {
			return getRandomY((int) (Math.random() * 2));
		}
	}

	@Override
	public void update() {
		switch (mWHERE) {

		case W_AI: {
			pathY += currentY;
			pathX += currentX;

			if (pathY > SnowWallpaper.ScreenHeight) {
				// 当Y坐标大于屏幕的高度时，证明已经离开屏幕，则重新设置Y坐标为0
				pathY = 0;
				this.currentX = getRandomX((int) (Math.random() * 2));
				this.currentY = getRandomY((int) (Math.random() * 3));
			} else if (pathY < 0) {
				pathY = 0;
				this.currentX = getRandomX((int) (Math.random() * 2));
				this.currentY = getRandomY((int) (Math.random() * 3));
			}

			if (pathX > SnowWallpaper.ScreenWidth) {
				// 当X坐标大于屏幕的宽度时，证明已经离开屏幕，则重新设置X坐标为0
				pathX = 0;
				this.currentX = getRandomX((int) (Math.random() * 2));
				this.currentY = getRandomY((int) (Math.random() * 3));
			} else if (pathX < -(SnowWallpaper.ScreenWidth / 100 * 4 + 5)) {
				pathX = SnowWallpaper.ScreenWidth;
				this.currentX = getRandomX((int) (Math.random() * 2));
				this.currentY = getRandomY((int) (Math.random() * 3));
			}
			break;
		}
		}
	}

}
