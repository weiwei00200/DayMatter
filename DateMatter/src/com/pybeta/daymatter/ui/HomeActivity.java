package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DLAService;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.ChangeLogDialog;
import com.pybeta.util.DMToast;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends LockBaseActivity implements OnDrawerStateChangeListener {
    private static final String STATE_ACTIVE_POSITION = "com.pybeta.daymatter.HomeActivity.activePosition";
    private final static int REQUEST_SETTINGS_CODE = 1000;
    
    private MenuDrawer mMenuDrawer;
    private int mActivePosition = 1;
    
    private MenuAdapter mAdapter;
    private ListView mList;
    
    private DatabaseManager mDatebaseMgr;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION, 1);
        }

        if (checkCurrentLocale()) {
    		showChangeLogDialog();
    		UmengUpdateAgent.update(this);
    		UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
    		
    		mDatebaseMgr = DatabaseManager.getInstance(this);
            mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
            mMenuDrawer.setOnDrawerStateChangeListener(this);
            mMenuDrawer.setContentView(R.layout.activity_main);
            mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);

            mList = new ListView(this);
            mAdapter = new MenuAdapter(buildMenuList());
            mList.setAdapter(mAdapter);
            mList.setOnItemClickListener(mItemClickListener);
            mList.setCacheColorHint(Color.TRANSPARENT);

            mMenuDrawer.setMenuView(mList);
            
            setupBasicFragment();
            UtilityHelper.showStartBarNotification(this);
        }
        startService(new Intent(this, DLAService.class));
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_SETTINGS_CODE) {
    		mAdapter.setMenuItems(buildMenuList());
    		mAdapter.notifyDataSetChanged();
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void setupBasicFragment() {
    	FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = getMatterListFragment(0);
		if (fragment != null) {
			fragmentTransaction.replace(R.id.fragment_container, fragment);
			fragmentTransaction.commit();
		}
	}
    
    private void instantiateItem(int position, Object object) {
    	FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment = getFragment(position, object);
		if (fragment != null) {
			fragmentTransaction.replace(R.id.fragment_container, fragment);
			fragmentTransaction.commit();
		}
    }
    
	@SuppressWarnings("unchecked")
	private Fragment getFragment(int position, Object object) {
		Fragment fragment = null;
		if (object != null && object instanceof Item) {
			Item item = (Item) object;
			if (item.mData instanceof CategoryItem) {
				fragment = getMatterListFragment(((CategoryItem) item.mData).getId());
			} else if(item.mData instanceof Class) {
				Class<Fragment> calzz = (Class<Fragment>) item.mData;
				try {
					fragment = (Fragment) calzz.newInstance();
				} catch (InstantiationException e) {
					if (BuildConfig.DEBUG) e.printStackTrace();
				} catch (IllegalAccessException e) {
					if (BuildConfig.DEBUG) e.printStackTrace();
				}
			}
		}
		return fragment;
	}
	
	private Fragment getMatterListFragment(int id) {
		Fragment fragment = new MatterListFragment();
		Bundle args = new Bundle();
		args.putInt(MatterListFragment.ARG_SECTION_NUMBER, id);
		fragment.setArguments(args);
		return fragment;
	}

	private List<Object> buildMenuList() {
		List<CategoryItem> categoryList = mDatebaseMgr.queryCategoryList();
		
		List<Object> items = new ArrayList<Object>();
		items.add(new Category(getString(R.string.category_title)));
		if (categoryList != null && categoryList.size() > 0) {
			for (int index = 0; index < categoryList.size(); index++) {
				CategoryItem category = categoryList.get(index);
				items.add(new Item(category.getName(), R.drawable.ic_action_select_all_dark, category));
				LogUtil.Sysout("category: "+category.getName());
			}
		}
        
        items.add(new Category(getString(R.string.menu_common_feature)));
        items.add(new Item(getString(R.string.title_activity_holiday), R.drawable.ic_action_select_all_dark, HolidayRecActivity.class));
        items.add(new Item(getString(R.string.title_activity_count_down), R.drawable.ic_action_select_all_dark, CountdownRecActivity.class));
        items.add(new Item(getString(R.string.title_activity_history_today), R.drawable.ic_action_select_all_dark, HistoryTodayFragement.class));
        items.add(new Item(getString(R.string.title_activity_worldtime), R.drawable.ic_action_select_all_dark, WorldTimeFragment.class));
        items.add(new Item(getString(R.string.otherfunction), R.drawable.ic_action_select_all_dark, OtherFunctionActivity.class));
        items.add(new Category(getString(R.string.copyright)));
        items.add(new Item(getString(R.string.copyright_content), 0, null));
        return items;
	}
	
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	if (mActivePosition != position) {
                mActivePosition = position;
                mMenuDrawer.setActiveView(view, position);
                instantiateItem(mActivePosition, mAdapter.getItem(position));
                
    			Handler h = new Handler();
    			h.postDelayed(new Runnable() {
    				public void run() {
    					mMenuDrawer.closeMenu();
    				}
    			}, 100);
        	} else {
        		mMenuDrawer.closeMenu();
        	}
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_home, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home: {
            mMenuDrawer.toggleMenu();
            return true;
        }
		case R.id.menu_item_action_add: {
			
			Intent intent = null;
			CharSequence mCharSequence = getSupportActionBar().getTitle();
			LogUtil.Sysout(" R.id.menu_item_action_add mCharSequence: "+mCharSequence);
			if(mCharSequence != null){
				String title = getSupportActionBar().getTitle().toString().trim();
				if(title.equals(getResources().getString(R.string.title_activity_worldtime))){
					intent = new Intent(this, AddWorldTimeActivity.class);
				}
				else
					intent = new Intent(this, AddMatterActivity.class);
			}else{
			
				intent = new Intent(this, AddMatterActivity.class);
			}
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_setting: {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, REQUEST_SETTINGS_CODE);
			break;
		}
		case R.id.menu_item_action_share: {
			takeScreenshotAndShare();
			break;
		}
		case R.id.menu_item_action_feedback: {
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void takeScreenshotAndShare() {
		new ScreenshotShare().execute();
	}

	class ScreenshotShare extends AsyncTask<String, String, String> {

		private ProgressDialog progressDlg = null;

		@Override
		protected void onPreExecute() {
			Resources res = HomeActivity.this.getResources();
			progressDlg = ProgressDialog.show(HomeActivity.this, res.getString(R.string.app_name),
					res.getString(R.string.screen_shot_progress), false, false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String pic = UtilityHelper.takeScreenshot(HomeActivity.this);
			if (BuildConfig.DEBUG) {
				System.out.println("screen shot: " + pic);
			}
			return pic;
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDlg != null) {
				progressDlg.dismiss();
			}

			String tips = HomeActivity.this.getString(R.string.screen_shot_save_path);
			DMToast.makeText(HomeActivity.this, tips + result, Toast.LENGTH_SHORT).show();

			UtilityHelper.share(HomeActivity.this, getString(R.string.share_intent_subject), "#"
					+ getString(R.string.app_name) + "#", result);

			super.onPostExecute(result);
		}
	}

	@Override
	public void onDrawerStateChange(int oldState, int newState) {
		if (newState == MenuDrawer.STATE_OPEN) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		} else {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
    
    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }
        super.onBackPressed();
    }

	private void showChangeLogDialog() {
		if (UtilityHelper.getChangeLogShow(this, IContants.CURRENT_VERSION)) {
			ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(this);
			_ChangelogDialog.show();

			UtilityHelper.setChangeLogShow(this, IContants.CURRENT_VERSION);
		}
	}

	private boolean checkCurrentLocale() {
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
		Locale locale = UtilityHelper.getCurrentLocale(this);
		if (!config.locale.equals(locale)) {
			UtilityHelper.switchLanguage(this);
			return false;
		} else {
			return true;
		}
	}
	
    private static class Item {

        String mTitle;
        int mIconRes;
        Object mData;

        Item(String title, int iconRes, Object data) {
            mTitle = title;
            mIconRes = iconRes;
            mData = data;
        }
    }

    private static class Category {

        String mTitle;

        Category(String title) {
            mTitle = title;
        }
    }

    private class MenuAdapter extends BaseAdapter {

        private List<Object> mItems;

        MenuAdapter(List<Object> items) {
            mItems = items;
        }

        public void setMenuItems(List<Object> items) {
        	mItems = items;
        }
        
        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof Item ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof Item;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Object item = getItem(position);

            if (item instanceof Category) {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_category, parent, false);
                }

                ((TextView) v).setText(((Category) item).mTitle);

            } else {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_item, parent, false);
                }

                TextView tv = (TextView) v;
                tv.setText(((Item) item).mTitle);
                tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
        }
    }
}
