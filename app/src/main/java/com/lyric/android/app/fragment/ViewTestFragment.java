package com.lyric.android.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.QRCodeUtils;
import com.lyric.android.app.utils.SnapshotUtils;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.ClashBar;
import com.lyric.android.app.widget.HorizontalRatioBar;
import com.lyric.android.app.widget.PieView;
import com.lyric.android.app.widget.RingProgressBar;
import com.lyric.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * 视图测试页面
 * @author lyricgan
 * @date 2017/7/25 14:57
 */
public class ViewTestFragment extends BaseFragment {
    private final int[] mRedGradientColors = {0xffff0000, 0xffff6f43, 0xffff0000};
    private final int[] mBlueGradientColors = {0xff1fbbe9, 0xff59d7fc, 0xff1fbbe9};
    private final int[] mGreenGradientColors = {0xffb3c526, 0xff7fb72f, 0xffb3c526};

    private TabDigitLayout mTabDigitLayout;
    private ImageView imageCapture;
    private ImageView ivQrcodeImage;
    private Bitmap mCaptureBitmap;

    private ClashBar mClashBar;

    public static ViewTestFragment newInstance() {
        Bundle args = new Bundle();
        ViewTestFragment fragment = new ViewTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view_test;
    }

    @Override
    public void onViewInitialize(View view, Bundle savedInstanceState) {
        PieView pieView = findViewWithId(R.id.pie_view);
        imageCapture = findViewWithId(R.id.image_capture);
        ivQrcodeImage = findViewWithId(R.id.iv_qrcode_image);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        mTabDigitLayout = findViewWithId(R.id.tab_digit_layout);
        mTabDigitLayout.setNumber(567890, 500L);

        findViewWithId(R.id.btn_start).setOnClickListener(this);

        mClashBar = findViewWithId(R.id.clash_bar);
        findViewWithId(R.id.btn_clash_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testClashBar();
            }
        });

        findViewWithId(R.id.btn_ring_progress_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRingProgressBar();
            }
        });
        findViewWithId(R.id.btn_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQrCode();
            }
        });
        findViewWithId(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
            }
        });
        HorizontalRatioBar horizontalRatioBar = findViewWithId(R.id.horizontal_radio_bar);
        horizontalRatioBar.test();
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        testClashBar();

        testRingProgressBar();
    }

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_start:
                takeViewCapture(mTabDigitLayout);
                break;
            default:
                break;
        }
    }

    private void takeViewCapture(View view) {
        mCaptureBitmap = SnapshotUtils.snapShot(view);
        if (mCaptureBitmap != null) {
            imageCapture.setImageBitmap(mCaptureBitmap);

            Log.d(TAG, "memory1:" + ImageUtils.getBitmapMemory(mCaptureBitmap));
            Bitmap blurBitmap = ImageUtils.blurBitmap(AndroidApplication.getContext(), mCaptureBitmap, 10.0f);
            imageCapture.setImageBitmap(blurBitmap);
            Log.d(TAG, "memory2:" + ImageUtils.getBitmapMemory(blurBitmap));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCaptureBitmap != null && !mCaptureBitmap.isRecycled()) {
            mCaptureBitmap.recycle();
            mCaptureBitmap = null;
        }
    }

    private void testClashBar() {
        final float leftData = 37.5f + new Random().nextInt(1000);
        final float rightData = 12.5f + new Random().nextInt(1000);
        mClashBar.setOnClashBarUpdatedListener(new ClashBar.OnClashBarUpdatedListener() {
            @Override
            public void onChanged(float leftData, float rightData, float leftProgressData, float rightProgressData, boolean isFinished) {
                Log.d(TAG, "leftData:" + leftData + ",rightData:" + rightData + ",leftProgressData:" + leftProgressData
                        + ",rightProgressData:" + rightProgressData + ",isFinished:" + isFinished);
            }
        });
        // 延迟加载，防止页面初始化未完成直接调用引起的卡顿问题
        mClashBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClashBar.setData(leftData, rightData, true);
            }
        }, 300L);
    }

    private void testRingProgressBar() {
        RingProgressBar ringProgressBar1 = findViewWithId(R.id.ring_progress_bar_1);
        RingProgressBar ringProgressBar2 = findViewWithId(R.id.ring_progress_bar_2);
        RingProgressBar ringProgressBar3 = findViewWithId(R.id.ring_progress_bar_3);

        ringProgressBar1.setAlwaysShowAnimation(true);
        ringProgressBar1.setSweepGradientColors(mRedGradientColors);
        ringProgressBar1.setProgress(12f, 100);

        ringProgressBar2.setAlwaysShowAnimation(true);
        ringProgressBar2.setSweepGradientColors(mBlueGradientColors);
        ringProgressBar2.setProgress(54f, 100);

        ringProgressBar3.setAlwaysShowAnimation(false);
        ringProgressBar3.setSweepGradientColors(mGreenGradientColors);
        ringProgressBar3.setProgress(64f, 100);
    }

    private void createQrCode() {
        showLoadingDialog();
        // /data/data/com.lyric.android.app/files/qr_test0001.jpg
        final String filePath = getContext().getFilesDir().getAbsolutePath() + File.separator + "qr_test0001.jpg";
        LogUtils.d(TAG, "filePath:" + filePath);
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap qrCodeBitmap = QRCodeUtils.createQRCodeBitmap("https://www.github.com", 400, 400,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), filePath);
                if (qrCodeBitmap != null) {
                    if (ivQrcodeImage != null) {
                        ivQrcodeImage.post(new Runnable() {
                            @Override
                            public void run() {
                                ivQrcodeImage.setImageBitmap(qrCodeBitmap);

                                hideLoadingDialog();
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
