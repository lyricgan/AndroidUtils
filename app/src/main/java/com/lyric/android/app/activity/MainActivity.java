package com.lyric.android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bigkoo.pickerview.TimePickerView;
import com.lyric.android.app.R;
import com.lyric.android.app.adapter.FragmentAdapter;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.base.Constants;
import com.lyric.android.app.mvvm.view.LoginActivity;
import com.lyric.android.app.test.Test;
import com.lyric.android.app.utils.AddPictureUtils;
import com.lyric.android.app.view.AddPicturePopup;
import com.lyric.android.app.widget.ListSelectFragment;
import com.lyric.android.app.widget.SelectItemEntity;
import com.lyric.android.library.utils.ActivityUtils;
import com.lyric.android.library.utils.DisplayUtils;
import com.lyric.android.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView iv_user_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListSelectDialog();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        initialize();
    }

    private void initialize() {
        AddPictureUtils.getInstance().initialize(this);
        AddPictureUtils.getInstance().setOnMenuClickListener(new AddPicturePopup.OnMenuClickListener() {
            @Override
            public void takePhoto(PopupWindow window) {
                AddPictureUtils.getInstance().takePhotoForAvatar(MainActivity.this);
            }

            @Override
            public void openPhotoAlbum(PopupWindow window) {
                AddPictureUtils.getInstance().openPhotoAlbum(MainActivity.this);
            }
        });

        Test.getInstance().initialize();
    }

    private void showDatePicker() {
        TimePickerView datePicker = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        datePicker.setRangeYear(calendar.get(Calendar.YEAR) - 5, calendar.get(Calendar.YEAR) + 5);
        datePicker.setTime(new Date());
        datePicker.setCyclic(false);
        datePicker.setCancelable(true);
        datePicker.show();
    }

    private void showListSelectDialog() {
        List<SelectItemEntity> itemEntityList = new ArrayList<>();
        SelectItemEntity itemEntity;
        for (int i = 0; i < 10; i++) {
            itemEntity = new SelectItemEntity();
            itemEntity.setTitle("列表选择" + (i + 1));
            itemEntityList.add(itemEntity);
        }
        ListSelectFragment fragment = ListSelectFragment.newInstance(itemEntityList);
        fragment.show(getSupportFragmentManager());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("资讯");
        titles.add("热点");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ListFragment.newInstance());
        fragments.add(ListFragment.newInstance());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        iv_user_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_avatar);
        iv_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPictureUtils.getInstance().showPopup(v);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_login: {
                        ActivityUtils.startActivity(MainActivity.this, LoginActivity.class);
                    }
                        break;
                    case R.id.nav_loading: {// LoadingTest
                        ActivityUtils.startActivity(MainActivity.this, LoadingActivity.class);
                    }
                        break;
                    case R.id.nav_progress: {// CircleProgressBar
                        ActivityUtils.startActivity(MainActivity.this, CircleProgressBarActivity.class);
                    }
                        break;
                    case R.id.nav_web: {// WebActivity
//                        ActivityUtils.startActivity(MainActivity.this, WebActivity.class);
                        ActivityUtils.startActivity(MainActivity.this, SwipeMenuSimpleActivity.class);
                    }
                        break;
                    case R.id.menu_item_about: {// 关于
//                        ActivityUtils.startActivity(MainActivity.this, ViewTestActivity.class);
                        ActivityUtils.startActivity(MainActivity.this, SwipeMenuActivity.class);
                    }
                        break;
                    case R.id.menu_item_exit: {// 退出
                        finish();
                    }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final int size = DisplayUtils.dip2px(BaseApp.getContext(), 72);
        switch (requestCode) {
            case AddPictureUtils.REQUEST_CODE_TAKE_PHOTO: {// 拍照
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(size, size);
                    if (bitmap == null) {
                        return;
                    }
                    iv_user_avatar.setImageBitmap(bitmap);
                }
            }
                break;
            case AddPictureUtils.REQUEST_CODE_PHOTO_ALBUM: {// 相册
                if (data != null && resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(data, size, size);
                    if (bitmap == null) {
                        return;
                    }
                    iv_user_avatar.setImageBitmap(bitmap);
                }
            }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AddPictureUtils.getInstance().destroy();
    }

    @Override
    protected void finalize() throws Throwable {
        // 当GC准备回收一个Java Object（所有Java对象都是Object的子类）的时候，GC会调用这个Object的finalize方法。
        super.finalize();
        // 可用来测试内存泄漏
        LogUtils.d(Constants.TAG_DEFAULT, "Activity has been recycled.");
    }
}
