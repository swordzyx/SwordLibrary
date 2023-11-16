package sword.view.viewpager;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;
import sword.logger.SwordLog;
import sword.view.floatball.FloatBallData;
import sword.view.floatball.WebViewContainer;

public class ViewPagerAdapter extends PagerAdapter {
	public static final String TAG = "ViewPagerAdapter";
	private final String[] dataList = FloatBallData.floatBallData.urls;
	private List<View> viewList = new ArrayList<>();

	public void setViewList(List<View> viewList, String[] dataList) {
		this.viewList = viewList;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View view;
		if (viewList.get(position) == null) {
			view = new View(container.getContext());
			view.setBackgroundColor(Color.WHITE);
		} else {
			view = viewList.get(position);
		}
		container.addView(view,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		SwordLog.debug(TAG, "getItemPosition, object: " + object);
		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		return dataList.length;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}
}
