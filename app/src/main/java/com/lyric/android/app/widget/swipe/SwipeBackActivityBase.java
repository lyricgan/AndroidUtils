package com.lyric.android.app.widget.swipe;

/**
 * 滑动关闭接口
 * 
 * @author lyricgan
 *
 */
public interface SwipeBackActivityBase {

	/**
	 * 获取和Activity相关联的SwipeBackLayout
	 * @return SwipeBackLayout
     * @see SwipeBackLayout
     */
    SwipeBackLayout getSwipeBackLayout();
    
    /**
     * 设置是否滑动关闭
     * @param enable true or false
     */
    void setSwipeBackEnable(boolean enable);

    /**
     * 滑动关闭Activity
     */
    void finishActivity();

}
