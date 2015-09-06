package com.example.mobilesafe.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class HomeActivity extends ActionBarActivity {

    private GridView gvHome;
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    private int[] mPics = new int[]{R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};
    private SharedPreferences msp;
    private String savePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        msp = getSharedPreferences("config", MODE_PRIVATE);

        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new myAdapter());

        gvHome.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {

                    case 0:
                        // 显示输入密码的对话框

                        showPasswordDialog();
                        break;
                    case 1:
                        // 显示通讯
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));

                        break;
                    case 2:
                        // 软件管理
                        startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));

                        break;
                    case 3:
                        //进程管理
                        startActivity(new Intent(HomeActivity.this,TaskActivity.class));
                        break;

                    case 5:
                        //进程管理
                        startActivity(new Intent(HomeActivity.this,AntivirusActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this,
                                AToolsActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(HomeActivity.this,
                                SettingActivity.class));
                        break;


                    default:
                        break;
                }

            }

        });

    }

    protected void showPasswordDialog() {
        savePassword = msp.getString("password", null);

        if (!TextUtils.isEmpty(savePassword)) {
            showPasswordInputDialog();
        } else {
            showPasswordSetDialog();
        }


    }

    private void showPasswordInputDialog() {
        // TODO Auto-generated method stub

        Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input_password, null);

        dialog.setView(view, 0, 0, 0, 0);


        final EditText etPassword = (EditText) view
                .findViewById(R.id.et_password);


        Button btn_Ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_Cancle = (Button) view.findViewById(R.id.btn_cancel);

        btn_Ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String password1 = etPassword.getText().toString();

                if (password1.equals(savePassword)) {
                    // Toast.makeText(this, "密码不一致", 0);
                    dialog.dismiss();
                    startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
                }

            }
        });

        btn_Cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void showPasswordSetDialog() {
        // TODO Auto-generated method stub
        Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_set_password, null);

        dialog.setView(view, 0, 0, 0, 0);


        final EditText etPassword = (EditText) view
                .findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view
                .findViewById(R.id.et_password_confirm);

        Button btn_Ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_Cancle = (Button) view.findViewById(R.id.btn_cancel);

        btn_Ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                    if (password.equals(passwordConfirm)) {

                        msp.edit().putString("password", password).commit();
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));

                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致", 0).show();

                    }

                } else {

                    Toast.makeText(HomeActivity.this, "输入框不能为空", 0).show();
                }
            }
        });

        btn_Cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = View.inflate(HomeActivity.this,
                    R.layout.home_list_item, null);

            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            ivItem.setImageResource(mPics[position]);
            tvItem.setText(mItems[position]);
            return view;
        }

    }

}
