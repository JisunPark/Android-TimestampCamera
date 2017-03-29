package com.suminjin.timestampcamera.widget;

import android.view.View;

import java.util.ArrayList;

/**
 * 여러 개의 view를 그룹지어 그 중 하나씩만 선택되도록 처리한다.
 * <p/>
 * Created by jspark on 2016-03-23.
 */
public class ViewSwitchManager {
    private ArrayList<ViewSwitchGroup> viewList = new ArrayList<>();
    private int selectedIndex = -1;

    public ViewSwitchManager() {
    }

    public void add(int i, View v, OnClickViewSwitchListener l) {
        viewList.add(new ViewSwitchGroup(i, v, l));
    }

    public void setSelection(int index) {
        if (selectedIndex >= 0) {
            viewList.get(selectedIndex).view.setSelected(false);
        }
        viewList.get(index).view.setSelected(true);
        selectedIndex = index;
    }

    public void setViewData(Object object) {
        viewList.get(selectedIndex).viewData = object;
    }

    public void setViewData(int index, Object object) {
        viewList.get(index).viewData = object;
    }

    public Object getViewData(int index) {
        return viewList.get(index).viewData;
    }

    public View getView(int index) {
        return viewList.get(index).view;
    }

    public int size() {
        return viewList.size();
    }

    public interface OnClickViewSwitchListener {
        void onClick(View v);
    }

    public class ViewSwitchGroup {
        public View view;
        public OnClickViewSwitchListener listener;
        public Object viewData;

        public ViewSwitchGroup(final int i, View v, final OnClickViewSwitchListener l) {
            view = v;
            listener = l;
            view.setTag(i); // tag index initially
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelection((Integer) v.getTag());
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
        }
    }
}
