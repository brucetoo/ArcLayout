package com.ogaclejapan.arclayout.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;
import com.ogaclejapan.arclayout.demo.widget.NoTouchLayout;

import java.util.ArrayList;
import java.util.List;

public class DemoLikePathActivity extends ActionBarActivity implements View.OnClickListener {

  private static final String KEY_DEMO = "demo";
  Toast toast = null;
  View fab;
    NoTouchLayout menuLayout;
  ArcLayout arcLayout;

  public static void startActivity(Context context, Demo demo) {
    Intent intent = new Intent(context, DemoLikePathActivity.class);
    intent.putExtra(KEY_DEMO, demo.name());
    context.startActivity(intent);
  }

  private static Demo getDemo(Intent intent) {
    return Demo.valueOf(intent.getStringExtra(KEY_DEMO));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.like_a_path);

    Demo demo = getDemo(getIntent());

    ActionBar bar = getSupportActionBar();
    bar.setTitle(demo.titleResId);
    bar.setDisplayHomeAsUpEnabled(true);

    fab = findViewById(R.id.fab);
    menuLayout = (NoTouchLayout) findViewById(R.id.menu_layout);
    arcLayout = (ArcLayout) findViewById(R.id.arc_layout);


    menuLayout.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {

              switch (event.getAction()) {
                  case MotionEvent.ACTION_MOVE:
                      for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
                          View childView = arcLayout.getChildAt(i);
                          Rect rect = new Rect();
                          childView.getHitRect(rect);//获取子view的剪裁区域
                          if(rect.contains((int)event.getX(),(int)event.getY())){ //点击的点是否在剪裁区域内
                              Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                                      childView,
                                      AnimatorUtils.scaleX(1, 1.2f),
                                      AnimatorUtils.scaleY(1, 1.2f));
                              anim.setDuration(100);
                              anim.setInterpolator(new AccelerateDecelerateInterpolator());
                              anim.start();
                          }else {
                              childView.setScaleX(1);
                              childView.setScaleY(1);
                          }
                      }
                      break;

                  case MotionEvent.ACTION_UP:
                      for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
                          arcLayout.getChildAt(i).setScaleX(1);
                          arcLayout.getChildAt(i).setScaleY(1);
                          View childView = arcLayout.getChildAt(i);
                          Rect rect = new Rect();
                          childView.getHitRect(rect);//获取子view的剪裁区域
                          if(rect.contains((int)event.getX(),(int)event.getY())) { //点击的点是否在剪裁区域内
                               childView.setTag(i);
                               onChooseListener.onChoose(childView);
                          }
                      }
                      menuLayout.setVisibility(View.GONE);
                      break;
              }
              return false;
          }
      });

    for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
    }

//    fab.setOnClickListener(this);
      fab.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
              menuLayout.setVisibility(View.VISIBLE);
              menuLayout.setFocusableInTouchMode(true);
              return false;
          }
      });
  }

    /**
     * 定义一个接口当menuLayout MotionEvent.ACTION_UP时刚好再某个子view
     * 的区域内,这调用该回调,传回选中的子view
     */
    private OnChooseListener onChooseListener = new OnChooseListener() {
        @Override
        public void onChoose(View v) {
            Toast.makeText(DemoLikePathActivity.this,String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
        }
    };

    public interface OnChooseListener{
        void onChoose(View v);
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.fab) {
      onFabClick(v);
      return;
    }

    if (v instanceof Button) {
      showToast((Button) v);
    }

  }

  private void showToast(Button btn) {
    if (toast != null) {
      toast.cancel();
    }

    String text = "Clicked: " + btn.getText();
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    toast.show();

  }

  private void onFabClick(View v) {
    if (v.isSelected()) {
      hideMenu();
    } else {
      showMenu();
    }
    v.setSelected(!v.isSelected());
  }

  @SuppressWarnings("NewApi")
  private void showMenu() {
    menuLayout.setVisibility(View.VISIBLE);

//    List<Animator> animList = new ArrayList<>();
//
//    for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
//      animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
//    }
//
//    AnimatorSet animSet = new AnimatorSet();
//    animSet.setDuration(400);
//    animSet.setInterpolator(new OvershootInterpolator());
//    animSet.playTogether(animList);
//    animSet.start();
  }

  @SuppressWarnings("NewApi")
  private void hideMenu() {

    List<Animator> animList = new ArrayList<>();

    for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
      animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
    }

    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(400);
    animSet.setInterpolator(new AnticipateInterpolator());
    animSet.playTogether(animList);
    animSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        menuLayout.setVisibility(View.INVISIBLE);
      }
    });
    animSet.start();

  }

  private Animator createShowItemAnimator(View item) {

    float dx = fab.getX() - item.getX();
    float dy = fab.getY() - item.getY();

    item.setRotation(0f);
    item.setTranslationX(dx);
    item.setTranslationY(dy);

    Animator anim = ObjectAnimator.ofPropertyValuesHolder(
        item,
        AnimatorUtils.rotation(0f, 720f),
        AnimatorUtils.translationX(dx, 0f),
        AnimatorUtils.translationY(dy, 0f)
    );

    return anim;
  }

  private Animator createHideItemAnimator(final View item) {
    float dx = fab.getX() - item.getX();
    float dy = fab.getY() - item.getY();

    Animator anim = ObjectAnimator.ofPropertyValuesHolder(
        item,
        AnimatorUtils.rotation(720f, 0f),
        AnimatorUtils.translationX(0f, dx),
        AnimatorUtils.translationY(0f, dy)
    );

    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        item.setTranslationX(0f);
        item.setTranslationY(0f);
      }
    });

    return anim;
  }

}
