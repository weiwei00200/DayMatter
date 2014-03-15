package com.pybeta.daymatter.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.adapter.MatterListRecAdapter;
import com.pybeta.daymatter.bean.Codes;
import com.pybeta.daymatter.core.DLAService;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.threadcommon.CustomRunnable;
import com.pybeta.daymatter.threadcommon.IDataAction;
import com.pybeta.daymatter.tool.AnimationTool;
import com.pybeta.daymatter.tool.KeyboardTool;
import com.pybeta.daymatter.tool.AnimationTool.AnimationType;
import com.pybeta.daymatter.tool.AnimationTool.IAnimationListener;
import com.pybeta.daymatter.tool.BackupRestoreTool;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.widget.SyncDialog;
import com.pybeta.ui.widget.SyncDialog.ISyncDialogListener;
import com.pybeta.ui.widget.UcMenuLine;
import com.pybeta.ui.widget.UcMenuLine.ICategoryClickListener;
import com.pybeta.ui.widget.UcMenuLine.ISyncClickListener;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.util.BackupToGoogleDriveTask;
import com.pybeta.util.ChangeLogDialog;
import com.pybeta.util.DMToast;
import com.pybeta.util.RestoreFromDriveTask;
import com.pybeta.util.RestoreFromDriveTask.IRestoreFromDriveListener;
import com.sammie.common.log.LogManager;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

public class HomeRecActivity extends Activity implements android.view.View.OnClickListener{
	private MatterApplication mAppInfo = null;
    private DatabaseManager mDatebaseMgr;
	private UcTitleBar mTitleBar = null;
	private UcMenuLine mMenu = null;
	private LinearLayout mLayoutTopMenu = null;
	private LinearLayout mLayoutBottomList = null;
	private View mListTopLine = null;
	private ListView mListView = null;
	protected View mMatterListView;
    protected View mTopMatterView;
    
    protected TextView mTopMatterTitleTip;
    protected TextView mTopMatterTitle;
    protected TextView mTopMatterDate;
    protected TextView mTopMatterNums;
    protected ImageView mTopMatterUnit;
    private LinearLayout mLayoutNomatter = null;
	
	private MatterListRecAdapter mAdapter = null;
	private UcTitleBar.ITitleBarListener mTitleBarListener = null;
	static boolean isMenuKeyClicked = false;
	private boolean isKeyDownEventAble = true;
	static int showingCategoryId = 0; 
	
	private GoogleAccountCredential mDriveCredential = null;
	private Drive mDriveService = null;
	private IAnimationListener mAnimationListener = null;
	
    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_home_rec);
        mLayoutTopMenu = (LinearLayout)this.findViewById(R.id.layout_top_menu);
    	mLayoutBottomList = (LinearLayout)this.findViewById(R.id.layout_bottom_list);
    	mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
    	mMenu = (UcMenuLine)this.findViewById(R.id.uc_menu_line);
        
    	mTopMatterView = this.findViewById(R.id.view_top_remind);
    	mTopMatterTitleTip = (TextView) this.findViewById(R.id.tv_matter_title_tip);
		mTopMatterTitle = (TextView) this.findViewById(R.id.tv_matter_title);
		mTopMatterDate = (TextView) this.findViewById(R.id.tv_matter_date);
		mTopMatterNums = (TextView) this.findViewById(R.id.tv_matter_days);
		mTopMatterUnit = (ImageView) this.findViewById(R.id.iv_matter_unit);
		
		mListTopLine = (View) this.findViewById(R.id.view_list_top_line);
		mListView = (ListView) this.findViewById(android.R.id.list);
		mLayoutNomatter = (LinearLayout) this.findViewById(R.id.layout_nomatter);
		mLayoutNomatter.setOnClickListener(this);
		mLayoutNomatter.setVisibility(View.GONE);
		mAppInfo = (MatterApplication)this.getApplication();
        if (checkCurrentLocale()) {
    		showChangeLogDialog();
    		UmengUpdateAgent.update(this);
    		UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
            
        }
        if(UtilityHelper.getBarNotifySettings(this)){
        	UtilityHelper.showStartBarNotification(this);
        }
        startService(new Intent(this, DLAService.class));
        
        initTitleBar();
        initMenu();
        initListView();
        
    }
    
    private void loadMenuData(){
    	try {
    		IDataAction runAction = new IDataAction() {
    			@Override
    			public Object actionExecute(Object obj) {
    				// TODO Auto-generated method stub
    				Map<String,Object> map = new HashMap<String, Object>();
    				mDatebaseMgr = DatabaseManager.getInstance(HomeRecActivity.this);
    				List<CategoryItem> categoryList = mDatebaseMgr.queryCategoryList();
    				map.put("CategoryList", categoryList);
    				return map;
    			}
    		};
    		IDataAction completeAction = new IDataAction() {
    			@Override
    			public Object actionExecute(Object obj) {
    				Map<String,Object> map = (Map<String,Object>)obj;
    				Object categoryObj = map.get("CategoryList");
    				if(null != categoryObj){
    					List<CategoryItem> categoryList = (List<CategoryItem>)categoryObj;
    					mMenu.setCategory(categoryList);
    				}
    				return null;
    			}
    		};
    		CustomRunnable run = new CustomRunnable(runAction, completeAction);
    		run.startAction();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LogManager.writeErrorLog("HomeRecActivity-loadData:"+e.getMessage());
		}
    }
    private void initMenu(){
    	mMenu.setCategoryClickListener(new ICategoryClickListener() {
			@Override
			public void itemClick(int id) {
				// TODO Auto-generated method stub
				final int clickId = id;
				showingCategoryId = id;
				IDataAction runAction = new IDataAction() {
					@Override
					public Object actionExecute(Object obj) {
						// TODO Auto-generated method stub
						return getCategoryListById(clickId);
					}
				};
				IDataAction completeAction = new IDataAction() {
					@Override
					public Object actionExecute(Object obj) {
						// TODO Auto-generated method stub
						mTitleBar.setViewVisible(false, false, true, true, false, false, false, false);
						if(isMenuKeyClicked){
							isMenuKeyClicked = false;
						}
						
						List<DayMatter> matterList = null;
						if (obj == null) {
							matterList = Collections.emptyList();
						} else {
							matterList = (List<DayMatter>)obj;
						}
						if(matterList.size() == 0){
							mLayoutNomatter.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
						}else{
							mLayoutNomatter.setVisibility(View.GONE);
							mListView.setVisibility(View.VISIBLE);
						}
						mAdapter.setItems(matterList.toArray());
						mAdapter.notifyDataSetChanged();
						
						return null;
					}
				};
				CustomRunnable run = new CustomRunnable(runAction, completeAction);
				run.startAction();
			}
		});
    	mMenu.setSyncListener(new ISyncClickListener() {
			@Override
			public void syncClick() {
				// TODO Auto-generated method stub
				final SyncDialog dialog = new SyncDialog(HomeRecActivity.this,R.style.SyncDialog);
				dialog.setListener(new ISyncDialogListener() {
					@Override
					public void restoreSdcard() {
						// TODO Auto-generated method stub
						BackupRestoreTool.restoreFromSdcard(HomeRecActivity.this);
						dialog.dismiss();
					}
					@Override
					public void restoreGoogle() {
						// TODO Auto-generated method stub
						mDriveCredential = BackupRestoreTool.restoreFromGoogleDrive(HomeRecActivity.this, mDriveCredential);
						dialog.dismiss();
					}
					@Override
					public void backupSdcard() {
						// TODO Auto-generated method stub
						BackupRestoreTool.backupToSdcard(HomeRecActivity.this);
						dialog.dismiss();
					}
					@Override
					public void backupGoogle() {
						// TODO Auto-generated method stub
						mDriveCredential = BackupRestoreTool.backupToGoogleDrive(HomeRecActivity.this, mDriveCredential);
						dialog.dismiss();				
					}
				});
				dialog.show();
			}
		});
    }
    private List<DayMatter> getCategoryListById(int categoryId){
    	List<DayMatter> matterList = DatabaseManager.getInstance(this).queryAll(categoryId);
    	return DataManager.getInstance(this).sortMatters(matterList);
    }
    private void initTitleBar(){
    	mTitleBar.setTitleText(getResources().getString(R.string.app_name));
    	mTitleBar.setViewVisible(false, false, true, true, false, false, false, false);
    	mTitleBarListener = new UcTitleBar.ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuClick(Object obj) {
				// TODO Auto-generated method stub
				openMenu();
			}
			@Override
			public void feedBackClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void editClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeRecActivity.this, AddMatterRecActivity.class);//AddMatterActivity  AddMatterRecActivity
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				closeMenu();
			}
		};
		mTitleBar.setListener(mTitleBarListener);
		//menu动画监听
		mAnimationListener = new IAnimationListener() {
			@Override
			public void startAnim() {
				// TODO Auto-generated method stub
				mTitleBar.setListener(null);
				isKeyDownEventAble = false;
			}
			@Override
			public void repeatAnim() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void endAnim() {
				// TODO Auto-generated method stub
				mTitleBar.setListener(mTitleBarListener);
				isKeyDownEventAble = true;
			}
		};
    	AnimationTool.setAnimListener(mAnimationListener);
    }
    private void openMenu(){
		int menuHeight = mLayoutTopMenu.getBottom();
		mLayoutBottomList.startAnimation(AnimationTool.getAnimation(mLayoutBottomList, 0, 0, 0, menuHeight, AnimationType.Vertical, mAppInfo.screenHeight));
		mTitleBar.setViewVisible(true, false, false, true, false, false, false, false);
		isMenuKeyClicked = true;
    }
    private void closeMenu(){
		int menuHeight = mLayoutTopMenu.getBottom();
		mLayoutBottomList.startAnimation(AnimationTool.getAnimation(mLayoutBottomList, 0, 0, 0, -menuHeight, AnimationType.Vertical, mAppInfo.screenHeight));
		mTitleBar.setViewVisible(false, false, true, true, false, false, false, false);
		isMenuKeyClicked = false;
    }
    
    private void initListView(){
    	mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayMatter matter = mAdapter.getItem(position);
                enterDetailMatterActivity(matter, position);
            }
        });
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				DayMatter matter = mAdapter.getItem(position);
				showActionDialog(matter, position);
				return false;
			}
		});
        mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(isMenuKeyClicked){
					closeMenu();
					isMenuKeyClicked = false;
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
        mAdapter = new MatterListRecAdapter(HomeRecActivity.this);
        showingCategoryId = 0;
    }
    private void loadListViewData(final int id){
    	IDataAction runAction = new IDataAction() {
			@Override
			public Object actionExecute(Object obj) {
				// TODO Auto-generated method stub
				List<DayMatter> dayMatterList = getCategoryListById(id);
				return dayMatterList;
			}
		};
		IDataAction completeAction = new IDataAction() {
			@Override
			public Object actionExecute(Object obj) {
				// TODO Auto-generated method stub
				List<DayMatter> matterList = null;
				if (obj == null) {
					matterList = Collections.emptyList();
				} else {
					matterList = (List<DayMatter>)obj;
				}
				if(matterList.size() == 0){
					mLayoutNomatter.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}else{
					mLayoutNomatter.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}
				mAdapter.setItems(matterList.toArray());
		        mListView.setAdapter(mAdapter);
		        //顶置
				DayMatter matter = DatabaseManager.getInstance(HomeRecActivity.this).queryTop();
				if (matter == null) {
					mListTopLine.setVisibility(View.GONE);
					mTopMatterView.setVisibility(View.GONE);
				} else {
					mListTopLine.setVisibility(View.VISIBLE);
					mTopMatterView.setVisibility(View.VISIBLE);
					DateUtils.showDayMatter(HomeRecActivity.this, matter, mTopMatterTitleTip, mTopMatterTitle, mTopMatterDate, mTopMatterNums, mTopMatterUnit);
				}
				mAdapter.notifyDataSetChanged();
				return null;
			}
		};
		CustomRunnable run = new CustomRunnable(runAction, completeAction);
		run.startAction();
		mTitleBar.setViewVisible(false, false, true, true, false, false, false, false);
		isMenuKeyClicked = false;
    }
    //item长按弹出框事件
    private void enterDetailMatterActivity(final DayMatter matter, final int position) {
		Intent intent = new Intent(this, ViewMatterRecActivity.class);
		intent.putExtra(IContants.KEY_MATTER_DATA, matter);
		intent.putExtra(IContants.kEY_MATTER_INDEX, position);
		startActivity(intent);
	}
    protected void showActionDialog(final DayMatter matter, final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.matter_list_action_title);
		builder.setItems(R.array.matter_list_action_array, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 1) {
					enterModifyActivity(matter);
				} else if (which == 2) {
					delete(matter);
				} else {
					enterDetailMatterActivity(matter, position);
				}
			}
		});
		builder.create().show();
	}
    public void delete(final DayMatter matter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.matter_delete_confirm);
        builder.setPositiveButton(R.string.matter_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DataManager dataMgr = DataManager.getInstance(HomeRecActivity.this);
                WidgetInfo info = dataMgr.getWidgetInfoByMatterUid(matter.getUid());
                
                DatabaseManager databaseMgr = DatabaseManager.getInstance(HomeRecActivity.this);
                databaseMgr.delete(matter);

                if (info != null) {
                	UtilityHelper.updateWidgetView(HomeRecActivity.this, info);
                    dataMgr.deleteWidgetInfoByMatterId(info.dayMatterId);
                }
                
                UtilityHelper.showStartBarNotification(HomeRecActivity.this);
                DMToast.makeText(HomeRecActivity.this, R.string.matter_delete_success,Toast.LENGTH_LONG).show();
                
                loadListViewData(showingCategoryId);
            }
        });
        builder.setNegativeButton(R.string.matter_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
    private void enterModifyActivity(final DayMatter matter) {
		Intent intent = new Intent(this, AddMatterActivity.class);
		intent.putExtra(IContants.KEY_MATTER_TYPE, true);
		intent.putExtra(IContants.KEY_MATTER_DATA, matter);
		startActivity(intent);
	}
    
    //------------------------原本的方法---------------------------
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
    private void showChangeLogDialog() {
		if (UtilityHelper.getChangeLogShow(this, IContants.CURRENT_VERSION)) {
			ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(this);
			_ChangelogDialog.show();

			UtilityHelper.setChangeLogShow(this, IContants.CURRENT_VERSION);
		}
	}
    //-----------------------重写方法-------------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	
	    } else if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	if(isKeyDownEventAble){
		    	if(!isMenuKeyClicked){
		    		openMenu();
		    	}else{
		    		closeMenu();
		    	}
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		AnimationTool.setAnimListener(null);
    }
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadView();
	}
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	showingCategoryId = 0;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_OK){
    		if (requestCode == Codes.REQUEST_SDCARD_RESTORE) {
				String filePath = data.getStringExtra(FileManagerActivity.RESULT_PATH);
				boolean success = DataManager.getInstance(this).restoreData(filePath);
				if (!success) {
					success = DataManager.getInstance(this).importDataCSV(filePath);
				}
				if (success) {
					DMToast.makeText(this, R.string.settings_restore_success, Toast.LENGTH_LONG).show();
				} else {
					DMToast.makeText(this, R.string.settings_restore_failed, Toast.LENGTH_LONG).show();
				}
			}else if (requestCode == Codes.REQUEST_GOOGLE_BACKUP) {
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(), mDriveCredential).build();
						new BackupToGoogleDriveTask(HomeRecActivity.this,mDriveService).execute();
					}
				}
			}else if(requestCode == Codes.REQUEST_GOOGLE_RESTORE){
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						IRestoreFromDriveListener listener = new IRestoreFromDriveListener() {
							@Override
							public void restoreFinish() {
								// TODO Auto-generated method stub
								loadView();
							}
						};
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(), mDriveCredential).build();
						new RestoreFromDriveTask(HomeRecActivity.this,mDriveService,listener).execute();
					}
				}
			}
    	}
    }
    
    private void loadView(){
    	loadMenuData();
		loadListViewData(showingCategoryId);
		AnimationTool.setAnimListener(mAnimationListener);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
