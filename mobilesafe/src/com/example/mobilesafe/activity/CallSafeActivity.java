package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.MyAdapter;
import com.example.mobilesafe.bean.BlackNumberInfo;
import com.example.mobilesafe.db.dao.BlachNumberDao;

import java.util.List;


import android.os.Handler;
import android.widget.Toast;


public class CallSafeActivity extends Activity {
    List<BlackNumberInfo> allContacts = null;
    ListView listView;
    LinearLayout llBg;
    Button btnPre;
    Button viewById;
    EditText etJump;
    TextView tvPage;
    BlachNumberDao blachNumberDao;
    private int currentPage = 0;
    private int prepage = 20;
    private int totalpage;
    private int mStartIndex = 0;
    private int mCount = 20;
    private int totalContacts;
    CallSafeAdaptter callSafeAdaptter;
    private Handler mHandle = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            llBg.setVisibility(View.INVISIBLE);

            callSafeAdaptter = new CallSafeAdaptter(allContacts, CallSafeActivity.this);
            listView.setAdapter(callSafeAdaptter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe2);
        initUI();
        initData();

    }

    private void initData() {
        blachNumberDao = new BlachNumberDao(CallSafeActivity.this);
        totalContacts = blachNumberDao.getTotalContacts();
        new Thread() {


            @Override
            public void run() {


//                blachNumberDao = new BlachNumberDao(CallSafeActivity.this);

//
//                if (sum % prepage == 0) totalpage = (sum / prepage);
//                else totalpage = (sum / prepage) + 1;
//
//                allContacts = blachNumberDao.findPageContacts(currentPage, prepage);
//                //  allContacts = blachNumberDao.findAllContacts();
//                System.out.print("allContacts" + allContacts);
//
                if (allContacts == null) {
                    allContacts = blachNumberDao.findPageContacts2(mStartIndex, mCount);
                } else {


                    allContacts.addAll(blachNumberDao.findPageContacts2(mStartIndex, mCount));
                }
                mHandle.sendEmptyMessage(0);
            }
        }.start();


    }

    private void initUI() {


        llBg = (LinearLayout) findViewById(R.id.ll_bg);


        listView = (ListView) findViewById(R.id.list_view);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastVisiblePosition = listView.getLastVisiblePosition();

                        if (lastVisiblePosition == (allContacts.size() - 1)) {
                            System.out.print("lastVisiblePosition = " + lastVisiblePosition);
                            mStartIndex += mCount;
                            if (mStartIndex > totalContacts) {
                                Toast.makeText(CallSafeActivity.this, "没有数据了.......", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            initData();

                        }

                        break;
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


    }

    //
//    public void onPre(View view) {
//        // Toast.makeText(CallSafeActivity.this,"nihs",Toast.LENGTH_LONG).show();
//        currentPage--;
//        if (currentPage < 0) currentPage = 0;
//
//        initData();
//    }
//
//    public void onNext(View view) {
//        currentPage++;
//        if (currentPage >= totalpage) currentPage = totalpage - 1;
//
//        initData();
//    }
//
//    public void onJump(View view) {
//
//        String etPage = etJump.getText().toString();
//
//        if (etPage.matches("^\\d{1,3}$")) {
//            currentPage = Integer.valueOf(etPage);
//            if (currentPage >= 0 && currentPage < totalpage)
//
//            {
//
//
//                initData();
//                return;
//            }
//
//
//        }
//
//        Toast.makeText(CallSafeActivity.this, "输入格式不正确", Toast.LENGTH_LONG).show();
//    }
    public void addBlacContact(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View dialog_view = View.inflate(this, R.layout.dialog_add_blacks, null);

        final EditText etNumber = (EditText) dialog_view.findViewById(R.id.et_number);

        Button btnOk = (Button) dialog_view.findViewById(R.id.btn_ok);

        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cbPhone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);

        final CheckBox cbSms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNumber = etNumber.getText().toString();

                if(TextUtils.isEmpty(strNumber)){
                    Toast.makeText(CallSafeActivity.this,"请输入正确格式",Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode = "";

                if(cbPhone.isChecked() && cbSms.isChecked()){
                    mode = "3";
                }else if(cbPhone.isChecked()){
                    mode = "2";
                }else if(cbSms.isChecked()){
                    mode = "1";
                }

                BlackNumberInfo info = new BlackNumberInfo();

                info.setNumber(strNumber);
                info.setMode(mode);

                allContacts.add(0,info);

                blachNumberDao.addContacts(strNumber,mode);


                if(callSafeAdaptter == null){
                    callSafeAdaptter = new CallSafeAdaptter(allContacts, CallSafeActivity.this);
                    listView.setAdapter(callSafeAdaptter);
                }else {
                    callSafeAdaptter.notifyDataSetChanged();
                }

                dialog.dismiss();

            }
        });

        dialog.setView(dialog_view);
        dialog.show();

    }

    private class CallSafeAdaptter extends MyAdapter<BlackNumberInfo> {


        public CallSafeAdaptter(List lists, Context context) {
            super(lists, context);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;


            if (convertView == null) {
                convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
                holder = new ViewHolder();

                holder.Number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.Mode = (TextView) convertView.findViewById(R.id.tv_mode);
                holder.Delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.Number.setText(lists.get(position).getNumber());
            //     System.out.print("lists.get(position).getNumber()===" + lists.get(position).getNumber());

            int mode = Integer.valueOf(lists.get(position).getMode());

            switch (mode) {

                case 1:
                    holder.Mode.setText("拦截短信");
                    break;
                case 2:
                    holder.Mode.setText("拦截电话");
                    break;
                case 3:
                    holder.Mode.setText("拦截短信电话");
                    break;
                default:
                    break;

            }


            final BlackNumberInfo info = lists.get(position);
            final String number = info.getNumber();
            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    boolean flag = blachNumberDao.deleteContacts(number);


                    if (flag) {
                        lists.remove(info);
                        callSafeAdaptter.notifyDataSetChanged();
                        Toast.makeText(CallSafeActivity.this, "成功",  Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CallSafeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            return convertView;
        }
    }

    public class ViewHolder {
        TextView Number;
        TextView Mode;
        ImageView Delete;
    }
}
