package com.example.mobilesafe.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.db.dao.AppLockDao;
import com.example.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class UnLockFragment extends Fragment {

	private View view;
	private TextView tv_unlock;
	private ListView list_view;
	private List<AppInfo> appInfos;
	private AppLockDao dao;
	private List<AppInfo> unLockLists;
	private UnLockAdapter adapter;

	/*
	 * ÀàËÆactivityÀïÃæµÄsetContentView
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.item_unlock_fragment, null);

		list_view = (ListView) view.findViewById(R.id.list_view);

		tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		appInfos = AppInfoProvider.getAppInfos(getActivity());



		dao = new AppLockDao(getActivity());


		unLockLists = new ArrayList<AppInfo>();

		for (AppInfo appInfo : appInfos) {

			if (dao.find(appInfo.getPackageName())) {

			} else {

				unLockLists.add(appInfo);
			}
		}

		adapter = new UnLockAdapter();
		list_view.setAdapter(adapter);
	}

	public class UnLockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tv_unlock.setText("进程个数" + unLockLists.size() + ")");
			return unLockLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return unLockLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			ViewHolder holder = null;
			final View view;
			final AppInfo appInfo;
			if (convertView == null) {
				view = View.inflate(getActivity(), R.layout.item_unlock, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.iv_unlock = (ImageView) view
						.findViewById(R.id.iv_unlock);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			}
			// »ñÈ¡µ½µ±Ç°µÄ¶ÔÏó
			appInfo = unLockLists.get(position);

			holder.iv_icon
					.setImageDrawable(unLockLists.get(position).getIcon());
			holder.tv_name.setText(unLockLists.get(position).getApkName());
			// °Ñ³ÌÐòÌí¼Óµ½³ÌÐòËøÊý¾Ý¿âÀïÃæ
			holder.iv_unlock.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {



					TranslateAnimation translateAnimation = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);

					translateAnimation.setDuration(5000);

					view.startAnimation(translateAnimation);

					new Thread(){
						public void run() {
							SystemClock.sleep(5000);

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									// Ìí¼Óµ½Êý¾Ý¿âÀïÃæ
									dao.add(appInfo.getPackageName());
									// ´Óµ±Ç°µÄÒ³ÃæÒÆ³ý¶ÔÏó
									unLockLists.remove(position);
									// Ë¢ÐÂ½çÃæ
									adapter.notifyDataSetChanged();
								}
							});


						};
					}.start();



				}
			});

			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_unlock;
	}
}