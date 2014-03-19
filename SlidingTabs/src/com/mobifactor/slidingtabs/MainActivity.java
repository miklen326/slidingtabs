package com.mobifactor.slidingtabs;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private List<MySlidingDrawer> mTabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabs = new ArrayList<MySlidingDrawer>();

		for (int i = 0; i < 4; i++) {
			MySlidingDrawer slidingDrawer = createTab(i);
			TextView title = (TextView) slidingDrawer.getHandle().findViewById(
					R.id.title);
			title.setText(getString(R.string.tab_name) + " " + (i + 1));
		}

		layoutTabs();
	}

	@Override
	public void onBackPressed() {
		if (mTabs != null) {
			for (MySlidingDrawer tab : mTabs) {
				if (tab.isOpened()) {
					tab.close();
					return;
				}
			}
		}
		super.onBackPressed();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public MySlidingDrawer createTab(final int index) {
		RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final MySlidingDrawer slidingDrawer;
		if (index % 2 == 0) {
			slidingDrawer = (MySlidingDrawer) inflater.inflate(
					R.layout.tab_left, null);
		} else {
			slidingDrawer = (MySlidingDrawer) inflater.inflate(
					R.layout.tab_right, null);
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rootLayout.addView(slidingDrawer, params);
		mTabs.add(slidingDrawer);

		slidingDrawer
				.setOnDrawerScrollListener(new MySlidingDrawer.OnDrawerScrollListener() {
					@Override
					public void onScrollStarted() {
						for (MySlidingDrawer tab : mTabs) {
							if (!slidingDrawer.equals(tab) && tab.isOpened())
								tab.close();
						}
					}

					@Override
					public void onScrollEnded() {
					}
				});

		slidingDrawer.getContent().setClickable(true);
		return slidingDrawer;
	}

	public void layoutTabs() {
		final int count = mTabs.size();
		int offset = 0;
		for (int i = 0; i < count; i++) {
			MySlidingDrawer slidingDrawer = mTabs.get(count - i - 1);
			slidingDrawer.setmBottomOffset((int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, -offset, getResources()
							.getDisplayMetrics()));
			View contentView = slidingDrawer.findViewById(R.id.content);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
							.getDisplayMetrics());
			contentView.setPadding(
					padding,
					padding,
					padding,
					padding
							+ (int) TypedValue.applyDimension(
									TypedValue.COMPLEX_UNIT_DIP, offset
											+ (((i + 1) % 2 == 0) ? 30 : 10),
									getResources().getDisplayMetrics()));
			offset += ((i + 1) % 2 == 0) ? 35 : 10;
		}
	}
}
