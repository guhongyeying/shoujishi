package com.example.mobilesafe.activity;

import android.app.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;

import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.bean.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoProvider;
import com.example.mobilesafe.utils.SystemUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.List;


public class TaskActivity extends Activity {


    @ViewInject(R.id.tv_task)
    TextView tvTask;
    @ViewInject(R.id.tv_storge)
    TextView tvStorge;
    @ViewInject(R.id.list_task_view)
    ListView listTackView;

    @ViewInject(R.id.tv_data_type)
    ListView tvDataType;


    private ActivityManager activityManager;
    private List<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> usertaskInfos;
    private ArrayList<TaskInfo> systemtaskInfos;
    private MyTaskAdapter myTaskAdapter;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            myTaskAdapter = new MyTaskAdapter();
            listTackView.setAdapter(myTaskAdapter);

        }
    };
    private int count;
    private long availMem;
    private long totalMem;
    private SharedPreferences msp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ViewUtils.inject(this);

        //tvDataType = (ListView) findViewById(R.id.list_task_view);

        msp = getSharedPreferences("config", MODE_PRIVATE);

        initUI();
        initData();
    }

    private void initData() {


        new Thread() {


            @Override
            public void run() {
                super.run();

                taskInfos = TaskInfoProvider.getTaskInfo(TaskActivity.this);

                systemtaskInfos = new ArrayList<TaskInfo>();
                usertaskInfos = new ArrayList<TaskInfo>();


                for (TaskInfo taskInfo : taskInfos) {


                    if (taskInfo.isUserApp()) {
                        usertaskInfos.add(taskInfo);
                    } else {


                        systemtaskInfos.add(taskInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {

        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        count = runningAppProcesses.size();

        System.out.println("count" + count);
        tvTask.setText("进程数：" + String.valueOf(count));

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);

        availMem = memoryInfo.availMem;

        //这里编译有问题


//通过proc、progress 拿到总的内存 因为上面这种方法不兼容版本

        totalMem = SystemUtils.getTotalRam(this);


        tvStorge.setText("剩余。总内存" + Formatter.formatFileSize(TaskActivity.this, availMem) + "/" +
                Formatter.formatFileSize(TaskActivity.this, totalMem));


        listTackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = listTackView.getItemAtPosition(position);

                //  System.out.println("obj ===" + obj);

                if (obj != null && obj instanceof TaskInfo) {


                                ViewHolder holder = (ViewHolder) view.getTag();
                    TaskInfo taskInfo = (TaskInfo) obj;
//如果是自己的就不需要设计点击事件
                    if(taskInfo.getPackageName().equals(getPackageName()))
                        return;
                    // System.out.println("---66666---------");
                    if (taskInfo.getPackageName().equals(getPackageName()))
                        return;


                    if (taskInfo.isChecked()) {
                        taskInfo.setChecked(false);

                        holder.cbApp.setChecked(false);
                    } else {
                        taskInfo.setChecked(true);

                        holder.cbApp.setChecked(true);
                    }

                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (myTaskAdapter != null) {


            myTaskAdapter.notifyDataSetChanged();
        }
    }

    private class MyTaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            boolean flag = msp.getBoolean("system_status", false);


            if (flag == true)

                return taskInfos.size() + 2;

            else
                return usertaskInfos.size() + 1;

        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == usertaskInfos.size() + 1) {
                return null;
            }
            TaskInfo taskInfo;

            if (position < usertaskInfos.size() + 1) {
                //把多出来的特殊的条目减掉
                taskInfo = usertaskInfos.get(position - 1);

            } else {

                int location = usertaskInfos.size() + 2;

                taskInfo = systemtaskInfos.get(position - location);
            }

            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            TaskInfo taskInfo = new TaskInfo();


            if (position == 0) {
                TextView textView = new TextView(TaskActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("应用进程（" + usertaskInfos.size() + ")");
                return textView;

            }


            if (position == (usertaskInfos.size() + 1)) {

                TextView textView = new TextView(TaskActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("系统进程（" + systemtaskInfos.size() + ")");
                return textView;
            }


            //获取数据

            if (position <= usertaskInfos.size()) {
                taskInfo = usertaskInfos.get(position - 1);
            }

            if (position > (usertaskInfos.size() + 1)) {
                taskInfo = systemtaskInfos.get(position - usertaskInfos.size() - 2);
            }


            if (convertView != null && convertView instanceof LinearLayout) {

                holder = (ViewHolder) convertView.getTag();


            } else {
                convertView = View.inflate(TaskActivity.this, R.layout.list_task_item, null);

                //错了两次没夹这个
                holder = new ViewHolder();

                holder.ivApp = (ImageView) convertView.findViewById(R.id.iv_task);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.cbApp = (CheckBox) convertView.findViewById(R.id.cb_app);
                holder.tvRam = (TextView) convertView.findViewById(R.id.tv_ram);

                convertView.setTag(holder);
            }


            holder.ivApp.setImageDrawable(taskInfo.getIcon());
            holder.tvName.setText(taskInfo.getAppName());
            holder.tvRam.setText(Formatter.formatFileSize(TaskActivity.this, taskInfo.getMemorySize()));


            if(taskInfo.getPackageName().equals(getPackageName())){
                holder.cbApp.setVisibility(View.INVISIBLE);

            }

            if (taskInfo.isChecked()) {
                holder.cbApp.setChecked(true);
            } else {
                holder.cbApp.setChecked(false);
            }
            return convertView;
        }
    }


    public void selectAll(View view) {


        for (TaskInfo tf : taskInfos) {
            System.out.println("nihaom -----");
            if (tf.getPackageName().equals(getPackageName()))
                continue;
            tf.setChecked(true);

        }
        myTaskAdapter.notifyDataSetChanged();

    }


    public void selectOppsite(View view) {

        for (TaskInfo ui : usertaskInfos) {


            if (ui.getPackageName().equals(getPackageName()))
                continue;

            if (ui.isChecked()) {
                ui.setChecked(false);
            } else {
                ui.setChecked(true);
            }
        }


            for (TaskInfo ui : systemtaskInfos) {


            if (ui.getPackageName().equals(getPackageName()))
                continue;

            if (ui.isChecked()) {
                ui.setChecked(false);
            } else {
                ui.setChecked(true);
            }
        }
        myTaskAdapter.notifyDataSetChanged();

    }

    public void killProcess(View view) {

        int killMem = 0;
        int killTask = 0;
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<TaskInfo> killTaskInfos = new ArrayList<TaskInfo>();


        for (TaskInfo usertaskInfo : usertaskInfos) {
            if (usertaskInfo.isChecked()) {
                killTask++;
                killMem += usertaskInfo.getMemorySize();
                killTaskInfos.add(usertaskInfo);
            }

        }

        for (TaskInfo sysInfo : systemtaskInfos) {
            if (sysInfo.isChecked()) {
                killTask++;
                killMem += sysInfo.getMemorySize();
                killTaskInfos.add(sysInfo);
            }

        }

        for (TaskInfo kill : killTaskInfos) {

            if (kill.isUserApp()) {

                usertaskInfos.remove(kill);

                activityManager.killBackgroundProcesses(kill.getPackageName());

            } else {

                systemtaskInfos.remove(kill);

                activityManager.killBackgroundProcesses(kill.getPackageName());
            }
        }

        availMem += killMem;
        count -= killTask;

        Toast.makeText(TaskActivity.this, "杀死了多少个进程" + killTask + "优化了" + Formatter.formatFileSize(TaskActivity.this, killMem) + "内存", Toast.LENGTH_LONG).show();

        tvTask.setText("进程数：" + String.valueOf(count));
        tvStorge.setText("剩余。总内存" + Formatter.formatFileSize(TaskActivity.this, availMem) + "/" +
                Formatter.formatFileSize(TaskActivity.this, totalMem));

        myTaskAdapter.notifyDataSetChanged();
    }


    public void openSetting(View view) {
        startActivity(new Intent(TaskActivity.this, TaskManagerSettingActivity.class));
    }

    class ViewHolder {
        ImageView ivApp;
        TextView tvName;

        TextView tvRam;
        CheckBox cbApp;
    }


}
