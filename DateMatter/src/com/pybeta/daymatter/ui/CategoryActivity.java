package com.pybeta.daymatter.ui;

import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.ui.widget.UcTitleBar.ITitleBarListener;
import com.pybeta.util.DMToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends BaseActivity implements OnItemLongClickListener {
	private DatabaseManager mDatebaseMgr;
	private ListView mCatListView;
	private SignalListAdapter mCatAdapter;
	
	private UcTitleBar mTitleBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		initTitleBar();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setTitle(R.string.title_activity_category);

		mDatebaseMgr = DatabaseManager.getInstance(this);

		mCatListView = (ListView) findViewById(R.id.view_list);
		mCatAdapter = new SignalListAdapter(this, R.layout.category_list_item, R.id.cat_name, mDatebaseMgr.queryCanModifyCategoryList());
		mCatListView.setAdapter(mCatAdapter);
		mCatListView.setOnItemLongClickListener(this);
		
	}
	private void initTitleBar(){
		mTitleBar = (UcTitleBar) this.findViewById(R.id.uc_titlebar);
		mTitleBar.setTitleText(getResources().getString(R.string.add_category));
    	mTitleBar.setViewVisible(false, true, false, true, false, false, false, false);
    	mTitleBar.setListener(new ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuClick(Object obj) {
				// TODO Auto-generated method stub
				
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
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				showAddCatDialog(null);
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		final CategoryItem cat = mCatAdapter.getItem(position);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.matter_list_menu_title);
	    builder.setItems(R.array.category_operate_type, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if (which == 0) {
	            		   showAddCatDialog(cat);
	            	   } else if (which == 1) {
	            		   deleteCat(cat);
	            	   }
	           }
	    });
	    builder.create().show();
		return false;
	}

	protected void deleteCat(CategoryItem cat) {
		mDatebaseMgr.deleteCategory(cat);
		mCatAdapter.setNotifyOnChange(false);
		mCatAdapter.clear();
		mCatAdapter.addAll(mDatebaseMgr.queryCanModifyCategoryList());
		mCatAdapter.notifyDataSetChanged();
	}

	private void showAddCatDialog(final CategoryItem cat) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View passwdView = inflater.inflate(R.layout.alert_dialog_text_entry, null);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.setting_input_category_title);
		final EditText input = (EditText) passwdView.findViewById(R.id.passwd_edit);
		if (cat != null && !TextUtils.isEmpty(cat.getName())) {
			input.setText(cat.getName());
			input.setSelection(cat.getName().length());
		}
		alert.setView(passwdView);
		alert.setCancelable(true);
		alert.setPositiveButton(R.string.matter_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String newCat = input.getText().toString().trim();
				if (!TextUtils.isEmpty(newCat)) {
					if (cat != null) {
						cat.setName(newCat);
						saveCat(cat);
					} else {
						CategoryItem item = new CategoryItem(newCat);
						saveCat(item);
					}
					dialog.dismiss();
				} else {
					DMToast.makeText(CategoryActivity.this, R.string.setting_input_category_empty, Toast.LENGTH_LONG).show();
				}
			}
		});
		alert.create().show();
	}

	private void saveCat(CategoryItem item) {
		mDatebaseMgr.addCategory(item);
		mCatAdapter.setNotifyOnChange(false);
		mCatAdapter.clear();
		mCatAdapter.addAll(mDatebaseMgr.queryCanModifyCategoryList());
		mCatAdapter.notifyDataSetChanged();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.activity_category_add, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home: {
//			finish();
//			break;
//		}
//		case R.id.menu_item_action_add: {
//			showAddCatDialog(null);
//			break;
//		}
//		}
//		return super.onOptionsItemSelected(item);
//	}

	class SignalListAdapter extends ArrayAdapter<CategoryItem> {

		private int resource;
		private int fieldId = 0;
		private LayoutInflater inflater;

		public SignalListAdapter(Context context, int resource, int textViewResourceId, List<CategoryItem> objects) {
			super(context, resource, textViewResourceId, objects);
			this.resource = resource;
			this.fieldId = textViewResourceId;
			this.inflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, resource);
		}

		private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
			View view;
			TextView text;

			if (convertView == null) {
				view = inflater.inflate(resource, parent, false);
			} else {
				view = convertView;
			}

			text = (TextView) view.findViewById(fieldId);

			CategoryItem item = getItem(position);
			text.setText(item.getName());

			return view;
		}
	}
}
