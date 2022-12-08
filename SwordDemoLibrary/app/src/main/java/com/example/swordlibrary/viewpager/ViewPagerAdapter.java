package com.example.swordlibrary.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
	private final List<ViewPageData> dataList = ViewPageData.getViewPagerDatas();

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		WebViewContainer webViewContainer = new WebViewContainer(container.getContext());
		webViewContainer.loadUrl(dataList.get(position).url);
		container.addView(webViewContainer);
		return webViewContainer;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
		public ViewPagerViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		void bind(ViewPageData data) {
			((WebViewContainer) itemView).loadUrl(data.url);
		}
	}

	static class ViewPageData {
		String url;
		static int pageSize = 4;

		static List<ViewPageData> getViewPagerDatas() {
			List<ViewPageData> dataList = new ArrayList<>();

			for (int i = 0; i < pageSize; i++) {
				dataList.add(new ViewPageData("about:blank"));
			}
			return dataList;
		}

		ViewPageData(String url) {
			this.url = url;
		}
	}
}
