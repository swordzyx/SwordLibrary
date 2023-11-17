package sword.view.viewpager;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sword.logger.SwordLog;

 public class ViewPagerAdapter extends PagerAdapter {
	public static final String TAG = "ViewPagerAdapter";
	private final String[] dataList;
	private final List<TextView> viewList = new ArrayList<>();
	
	public ViewPagerAdapter(String[] dataList) {
		this.dataList = dataList;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		TextView textview;
		if (position >= viewList.size()) {
			textview = new TextView(container.getContext());
			viewList.add(position, textview);
		} else {
			textview = (TextView)viewList.get(position);
		}
		
		textview.setText(dataList[position]);
		container.addView(textview,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return textview;
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
