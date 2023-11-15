package sword.view.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import sword.logger.SwordLog;
import sword.view.floatball.FloatBallData;
import sword.view.floatball.WebViewContainer;

public class ViewPagerAdapter extends PagerAdapter {
	public static final String TAG = "ViewPagerAdapter";
	private final String[] dataList = FloatBallData.floatBallData.urls;

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		WebViewContainer webViewContainer = new WebViewContainer(container.getContext());
		container.addView(webViewContainer);
		webViewContainer.loadUrl(dataList[position]);
		return webViewContainer;
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
