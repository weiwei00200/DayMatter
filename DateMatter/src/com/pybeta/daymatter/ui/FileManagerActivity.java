
package com.pybeta.daymatter.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.R;
import com.umeng.analytics.MobclickAgent;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FileManagerActivity extends ListActivity {

    private static final String ITEM_KEY = "key";
    private static final String ITEM_IMAGE = "image";

    private static final String ROOT = Environment.getExternalStorageDirectory().getPath();

    public static final String START_PATH = "START_PATH";
    public static final String FORMAT_FILTER = "FORMAT_FILTER";
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String SELECTION_MODE = "SELECTION_MODE";
    public static final String CAN_SELECT_DIR = "CAN_SELECT_DIR";

    private List<String> mPath = null;
    private TextView mMyPath;
    private ArrayList<HashMap<String, Object>> mList;

    private String mParentPath;
    private String mCurrentPath = ROOT;

    private String[] mFormatFilter = null;

    private HashMap<String, Integer> mLastPositions = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setResult(RESULT_CANCELED, getIntent());

        setContentView(R.layout.activity_file_dialog);
        mMyPath = (TextView) findViewById(R.id.path);

        mFormatFilter = getIntent().getStringArrayExtra(FORMAT_FILTER);

        String startPath = getIntent().getStringExtra(START_PATH);
        startPath = startPath != null ? startPath : ROOT;
        getDir(startPath);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }
    
    private void getDir(String dirPath) {
        boolean useAutoSelection = dirPath.length() < mCurrentPath.length();
        Integer position = mLastPositions.get(mParentPath);
        getDirImpl(dirPath);
        if (position != null && useAutoSelection) {
            getListView().setSelection(position);
        }

    }

    private void getDirImpl(final String dirPath) {

        mCurrentPath = dirPath;

        final List<String> item = new ArrayList<String>();
        mPath = new ArrayList<String>();
        mList = new ArrayList<HashMap<String, Object>>();

        File f = new File(mCurrentPath);
        File[] files = f.listFiles();
        if (files == null) {
            mCurrentPath = ROOT;
            f = new File(mCurrentPath);
            files = f.listFiles();
        }
        mMyPath.setText(getText(R.string.settings_restore_location) + ": " + mCurrentPath);

        if (!mCurrentPath.equals(ROOT)) {

            item.add(ROOT);
            addItem(ROOT, R.drawable.folder);
            mPath.add(ROOT);

            item.add("../");
            addItem("../", R.drawable.folder);
            mPath.add(f.getParent());
            mParentPath = f.getParent();

        }

        TreeMap<String, String> dirsMap = new TreeMap<String, String>();
        TreeMap<String, String> dirsPathMap = new TreeMap<String, String>();
        TreeMap<String, String> filesMap = new TreeMap<String, String>();
        TreeMap<String, String> filesPathMap = new TreeMap<String, String>();
        for (File file : files) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                dirsMap.put(dirName, dirName);
                dirsPathMap.put(dirName, file.getPath());
            } else {
                final String fileName = file.getName();
                final String fileNameLwr = fileName.toLowerCase();
                if (mFormatFilter != null) {
                    boolean contains = false;
                    for (int i = 0; i < mFormatFilter.length; i++) {
                        final String formatLwr = mFormatFilter[i].toLowerCase();
                        if (fileNameLwr.endsWith(formatLwr)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        filesMap.put(fileName, fileName);
                        filesPathMap.put(fileName, file.getPath());
                    }
                } else {
                    filesMap.put(fileName, fileName);
                    filesPathMap.put(fileName, file.getPath());
                }
            }
        }
        item.addAll(dirsMap.tailMap("").values());
        item.addAll(filesMap.tailMap("").values());
        mPath.addAll(dirsPathMap.tailMap("").values());
        mPath.addAll(filesPathMap.tailMap("").values());

        SimpleAdapter fileList = new SimpleAdapter(this, mList, R.layout.file_dialog_row,
                new String[] {
                        ITEM_KEY, ITEM_IMAGE
                }, new int[] {
                        R.id.fdrowtext, R.id.fdrowimage
                });

        for (String dir : dirsMap.tailMap("").values()) {
            addItem(dir, R.drawable.folder);
        }

        for (String file : filesMap.tailMap("").values()) {
            addItem(file, R.drawable.file);
        }

        fileList.notifyDataSetChanged();

        setListAdapter(fileList);
    }

    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(ITEM_KEY, fileName);
        item.put(ITEM_IMAGE, imageId);
        mList.add(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(mPath.get(position));
        if (file.isDirectory()) {
            if (file.canRead()) {
                mLastPositions.put(mCurrentPath, position);
                getDir(mPath.get(position));
            }
        } else {
            getIntent().putExtra(RESULT_PATH, mPath.get(position));
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!mCurrentPath.equals(ROOT)) {
                getDir(mParentPath);
            } else {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
