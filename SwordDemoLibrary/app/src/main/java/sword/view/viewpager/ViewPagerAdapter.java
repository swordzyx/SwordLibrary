package sword.view.viewpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.swordlibrary.R;

import java.util.ArrayList;
import java.util.List;

import sword.logger.SwordLog;
import sword.pages.HomeView;

public class ViewPagerAdapter extends PagerAdapter {
  public static final String tag = "ViewPagerAdapter";
  private static final int INVALID_LAYOUT_ID = -1;
  private static final String DEFAULT_TEXTVIEW_CONTENT = "默认的 TextView";

  private final int[] viewLayoutResList = new int[]{R.layout.view_photo_picker, INVALID_LAYOUT_ID, INVALID_LAYOUT_ID, INVALID_LAYOUT_ID};
  private final List<View> viewList = new ArrayList<>();

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view;
    if (position >= viewList.size()) {
      if (viewLayoutResList[position] != INVALID_LAYOUT_ID) {
        int layoutId = viewLayoutResList[position];
        if (layoutId == R.layout.view_photo_picker) {
          HomeView homeView = new HomeView((Activity) container.getContext());
          view = homeView.getMainContainer();
        } else {
          view = LayoutInflater.from(container.getContext()).inflate(layoutId, null);
        }
      } else {
        view = new TextView(container.getContext());
        view.setBackgroundColor(Color.GRAY);
        ((TextView) view).setTextColor(Color.BLACK);
        viewList.add(view);
      }
    } else {
      view = viewList.get(position);
    }

    if (view instanceof TextView) {
      ((TextView) view).setText(DEFAULT_TEXTVIEW_CONTENT);
    }
    container.addView(view,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    SwordLog.debug(tag, "destroyItem, position: " + position + "，view 类型：" + object.getClass());
    container.removeView(viewList.get(position));
  }

  @Override
  public int getItemPosition(@NonNull Object object) {
    SwordLog.debug(tag, "getItemPosition, object: " + object);
    return super.getItemPosition(object);
  }

  @Override
  public int getCount() {
    return viewLayoutResList.length;
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }
}
