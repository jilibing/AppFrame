package com.zihan.appframe.biz.tab;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import org.json.JSONArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Stack;

/**
 * The class is used to manage navigation through multiple stacks of fragments, as well as coordinate
 * fragments that may appear on screen
 * Created by niccapdevila on 3/21/16.
 */
public class FragNavController {

    private static final String EXTRA_TAG_COUNT = FragNavController.class.getName() + ":EXTRA_TAG_COUNT";
    private static final String EXTRA_SELECTED_TAB_INDEX = FragNavController.class.getName() + ":EXTRA_SELECTED_TAB_INDEX";
    private static final String EXTRA_CURRENT_FRAGMENT = FragNavController.class.getName() + ":EXTRA_CURRENT_FRAGMENT";
    private static final String EXTRA_FRAGMENT_STACK = FragNavController.class.getName() + ":EXTRA_FRAGMENT_STACK";
    private static final String EXTRA_FRAGMENT_KEY = FragNavController.class.getName() + ":EXTRA_FRAGMENT_KEY";

    private final SparseArray<Stack<Fragment>> mFragmentStacks;
    private final FragmentManager mFragmentManager;
    private int mSelectedTabKey = -1;
    private int mTagCount;
    private Fragment mCurrentFrag;
    private NavListener mNavListener;
    private
    @IdRes
    int mContainerId;
    private int mTransitionMode = FragmentTransaction.TRANSIT_NONE;

    public FragNavController(Bundle savedInstanceState, @NonNull FragmentManager fragmentManager, @IdRes int containerId, @NonNull SparseArray<Fragment> baseFragments) {
        mFragmentManager = fragmentManager;
        mContainerId = containerId;
        mFragmentStacks = new SparseArray<>(baseFragments.size());

        //Initialize
        if (savedInstanceState == null) {
            int key;
            for (int i = 0; i < baseFragments.size(); i++) {
                key = baseFragments.keyAt(i);
                Fragment fragment = baseFragments.get(key);

                Stack<Fragment> stack = new Stack<>();
                stack.add(fragment);
                mFragmentStacks.put(key, stack);
            }
        } else {
            onRestoreFromBundle(savedInstanceState, baseFragments);
            hideAllFragment();
        }
    }

    public FragNavController(Bundle savedInstanceState, @NonNull FragmentManager fragmentManager,
                             @IdRes int containerId, @NonNull SparseArray<Fragment> baseFragments, @Transit int transitionMode) {
        this(savedInstanceState, fragmentManager, containerId, baseFragments);
        mTransitionMode = transitionMode;
    }

    public int getSelectIndex() {
        return mSelectedTabKey;
    }

    @IntDef({FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, FragmentTransaction.TRANSIT_FRAGMENT_FADE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Transit {
    }

    public void setNavListener(NavListener navListener) {
        mNavListener = navListener;
    }

    /**
     * Switch to a different tab. Should not be called on the current tab.
     *
     * @param key the index of the tab to switch to
     */
    public void switchTab(int key) {
        if (mSelectedTabKey != key) {
            mSelectedTabKey = key;

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setTransition(mTransitionMode);

            //detachCurrentFragment(ft);
            hideCurrentFragment(ft);

            //Attempt to reattach previous fragment
            Fragment fragment = getPreviousFragment(ft);
            if (fragment == null) {
                fragment = mFragmentStacks.get(mSelectedTabKey).peek();
                ft.add(mContainerId, fragment, generateTag(fragment));
            }

            showFragment(ft, fragment);

            ft.commit();

            mCurrentFrag = fragment;
            if (mNavListener != null) {
                mNavListener.onTabTransaction(mCurrentFrag, mSelectedTabKey);
            }
        }
    }

    /**
     * Push a fragment onto the current stack
     *
     * @param fragment The fragment that is to be pushed
     */
    public void push(Fragment fragment) {
        if (fragment != null) {

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setTransition(mTransitionMode);
            //detachCurrentFragment(ft);
            hideCurrentFragment(ft);
            ft.add(mContainerId, fragment, generateTag(fragment));
            ft.commit();

            mFragmentManager.executePendingTransactions();
            mFragmentStacks.get(mSelectedTabKey).push(fragment);

            mCurrentFrag = fragment;
            if (mNavListener != null) {
                mNavListener.onFragmentTransaction(mCurrentFrag);
            }
        }
    }

    /**
     * Pop the current fragment from the current tab
     */
    public void pop() {
        Fragment poppingFrag = getCurrentFrag();
        if (poppingFrag != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setTransition(mTransitionMode);
            ft.remove(poppingFrag);

            //overly cautious fragment pop
            Stack<Fragment> fragmentStack = mFragmentStacks.get(mSelectedTabKey);
            if (!fragmentStack.isEmpty()) {
                fragmentStack.pop();
            }

            //Attempt reattach, if we can't, try to pop from the stack and push that on
            Fragment fragment = getPreviousFragment(ft);
            if (fragment == null && !fragmentStack.isEmpty()) {
                fragment = fragmentStack.peek();
                ft.add(mContainerId, fragment, fragment.getTag());
            }

            showFragment(ft, fragment);

            //Commit our transactions
            ft.commit();
            mFragmentManager.executePendingTransactions();

            mCurrentFrag = fragment;
            if (mNavListener != null) {
                mNavListener.onFragmentTransaction(mCurrentFrag);
            }
        }
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment.
     */
    public void clearStack() {
        //Grab Current stack
        Stack<Fragment> fragmentStack = mFragmentStacks.get(mSelectedTabKey);

        // Only need to start popping and reattach if the stack is greater than 1
        if (fragmentStack.size() > 1) {
            Fragment fragment;
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setTransition(mTransitionMode);

            //Pop all of the fragments on the stack and remove them from the FragmentManager
            while (fragmentStack.size() > 1) {
                fragment = mFragmentManager.findFragmentByTag(fragmentStack.peek().getTag());
                if (fragment != null) {
                    fragmentStack.pop();
                    ft.remove(fragment);
                }
            }

            //Attempt to reattach previous fragment
            fragment = getPreviousFragment(ft);
            //If we can't reattach, either pull from the stack, or create a new base fragment
            if (fragment != null) {
                ft.commit();
            } else {
                if (!fragmentStack.isEmpty()) {
                    fragment = fragmentStack.peek();
                    ft.add(mContainerId, fragment, fragment.getTag());
                    ft.commit();
                }
            }

            showFragment(ft, fragment);

            //Update the stored version we have in the list
            mFragmentStacks.put(mSelectedTabKey, fragmentStack);

            mCurrentFrag = fragment;
            if (mNavListener != null) {
                mNavListener.onFragmentTransaction(mCurrentFrag);
            }
        }
    }

    /**
     * Call this in your Activity's onSaveInstanceState(Bundle outState) method to save the instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    public void onSaveInstanceState(Bundle outState) {

        // Write tag count
        outState.putInt(EXTRA_TAG_COUNT, mTagCount);

        // Write select tab
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, mSelectedTabKey);

        // Write current fragment
        if (mCurrentFrag != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, mCurrentFrag.getTag());
        }

        // Write stacks
        try {
            final JSONArray stackArrays = new JSONArray();
            final JSONArray keyArrays = new JSONArray();

            for (int i = 0; i < mFragmentStacks.size(); i++) {
                // key
                int key = mFragmentStacks.keyAt(i);
                keyArrays.put(key);

                // value
                Stack<Fragment> stack = mFragmentStacks.valueAt(i);

                final JSONArray stackArray = new JSONArray();

                for (Fragment fragment : stack) {
                    stackArray.put(fragment.getTag());
                }

                stackArrays.put(stackArray);
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString());
            outState.putString(EXTRA_FRAGMENT_KEY, keyArrays.toString());
        } catch (Throwable t) {
            // Nothing we can do
        }
    }

    /**
     * Will attempt to reattach a previous fragment in the FragmentManager, or return null if not able to,
     *
     * @param ft current fragment transaction
     * @return Fragment if we were able to find and reattach it
     */
    @Nullable
    private Fragment reattachPreviousFragment(FragmentTransaction ft) {
        Stack<Fragment> fragmentStack = mFragmentStacks.get(mSelectedTabKey);
        Fragment fragment = null;
        if (!fragmentStack.isEmpty()) {
            fragment = mFragmentManager.findFragmentByTag(fragmentStack.peek().getTag());
            if (fragment != null) {
                ft.attach(fragment);
            }
        }
        return fragment;
    }

    @Nullable
    private Fragment getPreviousFragment(FragmentTransaction ft) {
        Stack<Fragment> fragmentStack = mFragmentStacks.get(mSelectedTabKey);
        Fragment fragment = null;
        if (!fragmentStack.isEmpty()) {
            fragment = mFragmentManager.findFragmentByTag(fragmentStack.peek().getTag());
        }
        return fragment;
    }

    /**
     * Attemps to detach any current fragment if it exists, and if none is found, returns;
     *
     * @param ft the current transaction being performed
     */
    private void detachCurrentFragment(FragmentTransaction ft) {
        Fragment oldFrag = getCurrentFrag();
        if (oldFrag != null) {
            ft.detach(oldFrag);
        }
    }

    private void hideAllFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i=0; i<mFragmentStacks.size(); i++) {
            Stack<Fragment> stack = mFragmentStacks.valueAt(i);
            for (int k=0; k<stack.size(); k++) {
                Fragment fragment = stack.get(k);
                fragment.onPause();
                ft.hide(fragment);
            }
        }

        ft.commit();
    }

    private void hideCurrentFragment(FragmentTransaction ft) {
        Fragment oldFrag = getCurrentFrag();
        if (oldFrag != null) {
            oldFrag.onPause();
            ft.hide(oldFrag);
        }
    }

    private void showFragment(FragmentTransaction ft, Fragment fragment) {
        if (fragment != null) {
            fragment.onResume();
            ft.show(fragment);
        }
    }

    @Nullable
    private Fragment getCurrentFrag() {
        //Attempt to used stored current fragment
        if (mCurrentFrag != null) {
            return mCurrentFrag;
        }
        //if not, try to pull it from the stack
        else {
            Stack<Fragment> fragmentStack = mFragmentStacks.get(mSelectedTabKey);
            if (!fragmentStack.isEmpty()) {
                return mFragmentManager.findFragmentByTag(mFragmentStacks.get(mSelectedTabKey).peek().getTag());
            } else {
                return null;
            }
        }
    }

    /**
     * Create a unique fragment tag so that we can grab the fragment later from the FragmentManger
     *
     * @param fragment The fragment that we're creating a unique tag for
     * @return a unique tag using the fragment's class name
     */
    private String generateTag(Fragment fragment) {
        return fragment.getClass().getName() + ++mTagCount;
    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     * @param baseFragments      List of base fragments from which to initialize empty stacks
     */
    private void onRestoreFromBundle(Bundle savedInstanceState, SparseArray<Fragment> baseFragments) {

        // Restore selected tab
        // 不必保存选择的tab，由外面的tab控制状态
        //mSelectedTabKey = savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX, -1);

        // Restore tag count
        mTagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0);

        // Restore current fragment
        mCurrentFrag = mFragmentManager.findFragmentByTag(savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT));

        // Restore fragment stacks
        try {
            final JSONArray keyArrays = new JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_KEY));
            final JSONArray stackArrays = new JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK));

            for (int x = 0; x < stackArrays.length(); x++) {
                final JSONArray stackArray = stackArrays.getJSONArray(x);
                final Stack<Fragment> stack = new Stack<>();

                if (stackArray.length() == 1) {
                    final String tag = stackArray.getString(0);
                    final Fragment fragment;

                    if (tag == null || "null".equalsIgnoreCase(tag)) {
                        fragment = baseFragments.get(x);
                    } else {
                        fragment = mFragmentManager.findFragmentByTag(tag);
                    }

                    if (fragment != null) {
                        stack.add(fragment);
                    }
                } else {
                    for (int y = 0; y < stackArray.length(); y++) {
                        final String tag = stackArray.getString(y);

                        if (tag != null && !"null".equalsIgnoreCase(tag)) {
                            final Fragment fragment = mFragmentManager.findFragmentByTag(tag);

                            if (fragment != null) {
                                stack.add(fragment);
                            }
                        }
                    }
                }

                mFragmentStacks.put(keyArrays.getInt(x), stack);
            }
        } catch (Throwable t) {
            mFragmentStacks.clear();

            int key;
            for (int i = 0; i < baseFragments.size(); i++) {
                key = baseFragments.keyAt(i);
                Fragment fragment = baseFragments.get(key);

                Stack<Fragment> stack = new Stack<>();
                stack.add(fragment);
                mFragmentStacks.put(key, stack);
            }
        }
    }

    public int getSize() {
        if (mFragmentStacks == null) {
            return 0;
        }
        return mFragmentStacks.size();
    }

    public Stack<Fragment> getCurrentStack() {
        return mFragmentStacks.get(mSelectedTabKey);
    }

    public interface NavListener {
        void onTabTransaction(Fragment fragment, int index);

        void onFragmentTransaction(Fragment fragment);
    }
}
