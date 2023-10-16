package com.android.speaker.home;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.BaseFragment;
import com.android.speaker.course.CourseFragment;
import com.android.speaker.listen.ListenFragment;
import com.android.speaker.me.MeFragment;
import com.android.speaker.study.StudyFragment;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ScreenUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class HomeActivity extends BaseActivity implements IHomeCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private TabRecyclerView tabList;
    private TabAdapter tabAdapter;
    private OnTabEventListener onTabEventListener;
    private TabBean studyBean;
    private TabBean courseBean;
    private TabBean listenerBean;
    private TabBean meBean;

    private ViewPager2 mainViewPager;
    private List<BaseFragment> fragments;
    private List<TabBean> tabBeanList;

    private int count = 0;
    private long lastClickTime = 0;
    private TabBean selectedItem;
    private TabBean preSelectedItem;

    private BroadcastReceiver unreadCountReceiver;

    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.text_color_CCF7F7F7));
        }

        UserInfo userInfo = UserInfo.getInstance();
        // token为空 跳登陆
//        if (userInfo == null || TextUtils.isEmpty(userInfo.getToken())) {
//            Intent intent = new Intent(this, LoginCaptchaActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }
        initView();
        initUnreadCountReceiver();
    }

    private void initUnreadCountReceiver() {
//        unreadCountReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                conversationBean.unreadCount = intent.getLongExtra(TUIConstants.UNREAD_COUNT_EXTRA, 0);
//                onTabBeanChanged(conversationBean);
//            }
//        };
//
//        IntentFilter unreadCountFilter = new IntentFilter();
//        unreadCountFilter.addAction(TUIConstants.CONVERSATION_UNREAD_COUNT_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(unreadCountReceiver, unreadCountFilter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initView() {
        setContentView(R.layout.activity_home);

        initTabs();
    }

    private void initTabs() {
        tabBeanList = new ArrayList<>();
        // conversation
        studyBean = new TabBean();
        studyBean.normalIcon = R.drawable.tab_study_default;
        studyBean.selectedIcon = R.drawable.tab_study_selected;
        studyBean.text = R.string.tab_text_study;
        studyBean.fragment = new StudyFragment();
        ((StudyFragment)studyBean.fragment).setCallback(this);
        studyBean.showUnread = true;
        studyBean.unreadClearEnable = true;
        tabBeanList.add(studyBean);

        // 课程
        courseBean = new TabBean();
        courseBean.normalIcon = R.drawable.tab_course_default;
        courseBean.selectedIcon = R.drawable.tab_course_selected;
        courseBean.text = R.string.tab_text_course;
        courseBean.fragment = new CourseFragment();
        courseBean.showUnread = true;
        tabBeanList.add(courseBean);

        // 听听
        listenerBean = new TabBean();
        listenerBean.normalIcon = R.drawable.tab_listener_default;
        listenerBean.selectedIcon = R.drawable.tab_listener_selected;
        listenerBean.text = R.string.tab_text_listener;
        listenerBean.fragment = new ListenFragment();
        tabBeanList.add(listenerBean);

        // 我的
        meBean = new TabBean();
        meBean.normalIcon = R.drawable.tab_me_default;
        meBean.selectedIcon = R.drawable.tab_me_selected;
        meBean.text = R.string.tab_text_me;
        meBean.fragment = new MeFragment();
        tabBeanList.add(meBean);

        tabList = findViewById(R.id.tab_list);
        tabList.disableIntercept();
        tabList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        tabList.addItemDecoration(new TabDecoration());
        tabAdapter = new TabAdapter();
        tabList.setAdapter(tabAdapter);
        fragments = new ArrayList<>();
        for (TabBean tabBean : tabBeanList) {
            fragments.add(tabBean.fragment);
        }

        mainViewPager = findViewById(R.id.view_pager);
        fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragmentList(fragments);
        mainViewPager.setUserInputEnabled(false);
        mainViewPager.setOffscreenPageLimit(5);
        mainViewPager.setAdapter(fragmentAdapter);
        setTabSelected(studyBean);

        onTabEventListener = new OnTabEventListener() {
            @Override
            public void onTabSelected(TabBean tabBean) {
                setTabSelected(tabBean);
            }

            @Override
            public void onTabUnreadCleared(TabBean tabBean) {

            }
        };
    }

    private void setTabSelected(TabBean tabBean) {
        if (tabBean == null) {
            return;
        }
        int position = tabBeanList.indexOf(tabBean);
        if (position == -1) {
            return;
        }
        tabAdapter.notifyItemChanged(position);
        mainViewPager.setCurrentItem(position, false);

        preSelectedItem = selectedItem;
        selectedItem = tabBean;
        int prePosition = tabBeanList.indexOf(preSelectedItem);
        if (prePosition != -1) {
            tabAdapter.notifyItemChanged(prePosition);
        }

        // 修改actionbar颜色
        switch (position) {
            case TAB_STUDY:
                setStudyStatusBar("down");
                break;
            case TAB_COURSE:
            case TAB_LISTEN:
                getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                break;
            case TAB_ME:
                getWindow().setStatusBarColor(getResources().getColor(R.color.text_color_FCFCFC));
                break;
        }
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                ToastUtil.toastLongMessage("再按一次退出应用");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unreadCountReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(unreadCountReceiver);
            unreadCountReceiver = null;
        }
    }

    private void onTabBeanChanged(TabBean tabBean) {
        int index = tabBeanList.indexOf(tabBean);
        if (index == -1) {
            return;
        }
        tabAdapter.notifyItemChanged(index);
    }

    private void onNewTabBean(TabBean tabBean) {
        if (tabBean == null) {
            return;
        }
        tabBeanList.add(tabBean);
        int index = tabBeanList.indexOf(tabBean);
        fragments.add(index, tabBean.fragment);
        tabAdapter.notifyItemInserted(index);
        tabAdapter.notifyItemRangeChanged(0, tabBeanList.size());
        fragmentAdapter.notifyItemInserted(index);
    }

    private void onTabBeanRemoved(TabBean tabBean) {
        if (tabBean == null) {
            return;
        }
        int index = tabBeanList.indexOf(tabBean);
        if (index == -1) {
            return;
        }
        tabBeanList.remove(tabBean);
        tabAdapter.notifyItemRemoved(index);
        tabAdapter.notifyItemRangeChanged(0, tabBeanList.size());
        fragments.remove(tabBean.fragment);
        fragmentAdapter.notifyItemRemoved(index);
    }

    @Override
    public void callback(int tabIndex, Object value) {
        switch (tabIndex) {
            case TAB_STUDY:
                setStudyStatusBar((String)value);
                break;
        }
    }

    // 设置学习tab状态栏
    private void setStudyStatusBar(String value) {
        int colorRes = R.color.common_purple_color;
        if("up".equals(value)) {
            colorRes = R.color.white;
        }
        getWindow().setStatusBarColor(getResources().getColor(colorRes));
    }

    static class TabBean {
        int normalIcon;
        int selectedIcon;
        int text;
        BaseFragment fragment;
        long unreadCount;
        boolean showUnread = false;
        boolean unreadClearEnable = false;
    }

    class TabAdapter extends RecyclerView.Adapter<TabAdapter.TabViewHolder> {

        @NonNull
        @Override
        public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.home_bottom_tab_item, null);
            return new TabViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
            TabBean tabBean = tabBeanList.get(position);
            int width = getResources().getDimensionPixelOffset(R.dimen.home_tab_bottom_item_width);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            holder.itemView.setLayoutParams(params);
            holder.textView.setText(tabBean.text);
            if (selectedItem == tabBean) {
                holder.imageView.setBackgroundResource(tabBean.selectedIcon);
                holder.textView.setTextColor(getResources().getColor(R.color.common_purple_color));
            } else {
                holder.imageView.setBackgroundResource(tabBean.normalIcon);
                holder.textView.setTextColor(getResources().getColor(R.color.text_color_2));
            }
//            if (tabBean.showUnread && tabBean.unreadCount > 0) {
//                holder.unreadCountTextView.setVisibility(View.VISIBLE);
//                if (tabBean.unreadCount > 99) {
//                    holder.unreadCountTextView.setText("99+");
//                } else {
//                    holder.unreadCountTextView.setText(tabBean.unreadCount + "");
//                }
//                if (tabBean.unreadClearEnable) {
//                    prepareToClearAllUnreadMessage(holder.unreadCountTextView, tabBean);
//                }
//            } else {
//                holder.unreadCountTextView.setVisibility(View.GONE);
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTabEventListener.onTabSelected(tabBean);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (tabBeanList == null) {
                return 0;
            }
            return tabBeanList.size();
        }

        class TabViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView unreadCountTextView;

            public TabViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.tab_icon);
                textView = itemView.findViewById(R.id.tab_text);
                unreadCountTextView = itemView.findViewById(R.id.unread_view);
            }
        }
    }

    class TabDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int columnNum = tabBeanList.size();
            int column = parent.getChildAdapterPosition(view);
            int screenWidth = ScreenUtil.getScreenWidth(HomeActivity.this);
            int columnWidth =  parent.getResources().getDimensionPixelSize(R.dimen.home_tab_bottom_item_width);
            if (columnNum > 1) {
                int leftRightSpace = (screenWidth - columnNum * columnWidth) / (columnNum - 1);
                outRect.left = column * leftRightSpace / columnNum;
                outRect.right = leftRightSpace * (columnNum - 1 - column) / columnNum;
            }
        }
    }

    interface OnTabEventListener {
        void onTabSelected(TabBean tabBean);
        void onTabUnreadCleared(TabBean tabBean);
    }

    private String getVersion() {
        String version = "";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(),
                    0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
        }

        return version;
    }
    private String getChannelNameFromMETA() {
        String channel = "speaker";//默认“speaker”

        final String start_flag = "META-INF/channel_";
        ApplicationInfo appInfo = getApplicationInfo();
        String sourceDir = appInfo.sourceDir;

        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, e.getMessage());
                }
            }
        }
        return channel;
    }
}
