package com.slate.dump;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

public class DumpCodeContainer {

  private static final String TAG = DumpCodeContainer.class.getSimpleName();

  private void logBackStack(FragmentManager fragmentManager) {
    StringBuilder log = new StringBuilder();
    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
      log.append(fragmentManager.getBackStackEntryAt(i).getName()).append(" | ");
    }
    Log.d(TAG, "Back Stack: " + log.toString());
  }

  private ArrayList<View> getAllChildren(View v, int level) {

    StringBuilder tree = new StringBuilder("\n");
    if (!(v instanceof ViewGroup)) {
      ArrayList<View> viewArrayList = new ArrayList<View>();
      viewArrayList.add(v);
      return viewArrayList;
    }

    ArrayList<View> result = new ArrayList<View>();

    ViewGroup vg = (ViewGroup) v;
    for (int i = 0; i < vg.getChildCount(); i++) {

      View child = vg.getChildAt(i);

      ArrayList<View> viewArrayList = new ArrayList<View>();
      viewArrayList.add(v);
      viewArrayList.addAll(getAllChildren(child, level + 1));

      result.addAll(viewArrayList);
    }
    return result;
  }

}
