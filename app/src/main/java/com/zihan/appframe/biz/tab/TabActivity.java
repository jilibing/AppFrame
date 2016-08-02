package com.zihan.appframe.biz.tab;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.R;

public class TabActivity extends BaseActivity implements FragmentNavigation{

    private static final int KEY_0 = 0;
    private static final int KEY_1 = 1;
    private static final int KEY_2 = 2;
    private static final int KEY_3 = 3;
    private static final int KEY_4 = 4;

    private FragNavController mNavController;
    private BottomBar mBottomBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_tab;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        SparseArray<Fragment> fragments = new SparseArray<>(5);
        fragments.put(KEY_0, TestFragment.newInstance("recents"));
        fragments.put(KEY_1, TestFragment.newInstance("favorites"));
        fragments.put(KEY_2, TestFragment.newInstance("nearby"));
        fragments.put(KEY_3, TestFragment.newInstance("friends"));
        fragments.put(KEY_4, TestFragment.newInstance("food"));

        mNavController =
                new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.container, fragments);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.noTabletGoodness();
        mBottomBar.setItems(R.menu.menu_bottombar);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bb_menu_recents:
                        mNavController.switchTab(KEY_0);
                        break;
                    case R.id.bb_menu_favorites:
                        mNavController.switchTab(KEY_1);
                        break;
                    case R.id.bb_menu_nearby:
                        mNavController.switchTab(KEY_2);
                        break;
                    case R.id.bb_menu_friends:
                        mNavController.switchTab(KEY_3);
                        break;
                    case R.id.bb_menu_food:
                        mNavController.switchTab(KEY_4);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                mNavController.clearStack();
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");

        // Make a Badge for the first tab, with red background color and a value of "13".
        BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(0, "#FF0000", 13);

        // Control the badge's visibility
        unreadMessages.show();
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        //unreadMessages.setCount(4);

        // Change the show / hide animation duration.
        unreadMessages.setAnimationDuration(200);

        // If you want the badge be shown always after unselecting the tab that contains it.
        unreadMessages.setAutoShowAfterUnSelection(true);

        // If you don't want this badge to be hidden after selecting the tab contains it.
        //unreadMessages.setAutoShowAfterUnSelection(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavController.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mNavController.getCurrentStack().size() > 1) {
            mNavController.pop();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        mNavController.push(fragment);
    }
}
