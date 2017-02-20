package com.xmg.richeditor_android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xmg.richeditor_android.R;


/**
 * Created by Administrator on 2015/12/16.
 */
public class TextColorPopWindow extends PopupWindow {
    private TextView default_color;
    private TextView cccccc_color;
    private TextView gray_color;
    private TextView four_color;
    private TextView green_color;
    private View conentView;
    WindowManager manager;

    public TextColorPopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.menu_textcolor, null);
        default_color = (TextView) conentView.findViewById(R.id.default_color);
        cccccc_color = (TextView) conentView.findViewById(R.id.cccccc_color);
        gray_color = (TextView) conentView.findViewById(R.id.gray_color);
        four_color = (TextView) conentView.findViewById(R.id.four_color);
        green_color = (TextView) conentView.findViewById(R.id.green_color);

        default_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListListener.onClickList(Color.BLACK);
            }
        });
        cccccc_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListListener.onClickList(0xFF868686);
            }
        });
        gray_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListListener.onClickList(0xFF846954);
            }
        });
        four_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListListener.onClickList(0xFF687213);
            }
        });

        green_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListListener.onClickList(0xFF4E93AA);
            }
        });

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth((int) (w / 2.5));
        this.setWidth((int) (ViewGroup.LayoutParams.MATCH_PARENT));

        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);

    }


    public interface onColorbackListener {
        public void onClickList(int colorValue);

    }

    onColorbackListener mOnClickListListener;

    public void setmOnClickListListener(onColorbackListener mOnClickListListener) {
        this.mOnClickListListener = mOnClickListListener;
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //获取xoff
            int xpos = manager.getDefaultDisplay().getWidth() / 2 - this.getWidth() / 2;
            //xoff,yoff基于anchor的左下角进行偏移。
            this.showAsDropDown(parent, xpos, 0);
        } else {
            this.dismiss();
        }
    }
}
