package com.xmg.richeditor_android.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.xmg.richeditor_android.utils.L;
import com.xmg.richeditor_android.utils.Utils;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Copyright (C) 2015 Wasabeef
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RichEditor extends XWalkView {

    public enum Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        INSERTORDEREDLIST,
        INSERTUNORDEREDLIST,
        JUSTIFYCENTER,
        JUSTIFYFULL,
        JUSTIFYLEFT,
        JUSTIFYRIGHT,
        INSERTHORIZONTALRULE,
        FORMATBLOCK,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6
    }

    public interface OnTextChangeListener {

        void onTextChange(String text);
    }

    public interface OnDecorationStateListener {

        void onStateChangeListener(String text, Map<String, String> maps);
    }

    public interface AfterInitialLoadListener {

        void onAfterInitialLoad(boolean isReady);
    }

    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String STATE_SCHEME = "re-state://";
    private static final String MUSIC_CALLBACK = "music-callback://";
    private static final String PLAY_SONG = "play";
    private static final String PAUSE_SONG = "pause";
    private boolean isReady = false;
    private String mContents;
    public int mColorValue = Color.BLACK;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private AfterInitialLoadListener mLoadListener;

    public RichEditor(Context context) {
        this(context, null);
    }


    @SuppressLint("SetJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
//    getSettings().setJavaScriptEnabled(true);
//    getSettings().setSupportMultipleWindows(false);
//    getSettings().setUseWideViewPort(true);
//    getSettings().setLoadWithOverviewMode(true);
        //现在是18.*版本
        setUIClient(new XWalkUIClient(this));
        //下面的设置客户端是老版本的，8.*
        //setXWalkWebChromeClient(new XWalkWebChromeClient(this));
//    setWebChromeClient(new WebChromeClient());
        setResourceClient(createWebviewClient());
//    setWebViewClient(createWebviewClient());
        load(SETUP_HTML, null);

        applyAttributes(context, attrs);
    }


//  public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
//    super(context, attrs, defStyleAttr);
//
//
//  }

    protected EditorWebViewClient createWebviewClient() {
        return new EditorWebViewClient(this);
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        mDecorationStateListener = listener;
    }

    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mLoadListener = listener;
    }

    private void callback(String text) {
        mContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChange(mContents);
        }
    }

    private void stateCheck(String text) {
        String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
        Map<String, String> types = new HashMap<>();
        for (Type type : Type.values()) {
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.put(type.name(), type.name());
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChangeListener(state, types);
        }
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        final int[] attrsArray = new int[]{
                android.R.attr.gravity
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int gravity = ta.getInt(0, NO_ID);
        switch (gravity) {
            case Gravity.LEFT:
                exec("javascript:RE.setTextAlign(\"left\")");
                break;
            case Gravity.RIGHT:
                exec("javascript:RE.setTextAlign(\"right\")");
                break;
            case Gravity.TOP:
                exec("javascript:RE.setVerticalAlign(\"top\")");
                break;
            case Gravity.BOTTOM:
                exec("javascript:RE.setVerticalAlign(\"bottom\")");
                break;
            case Gravity.CENTER_VERTICAL:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                break;
            case Gravity.CENTER_HORIZONTAL:
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
            case Gravity.CENTER:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
        }

        ta.recycle();
    }

    public void setHtml(String contents) {
        if (contents == null) {
            contents = "";
        }
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
        mContents = contents;
    }

    public void setHtmlNodecode(String contents) {
        if (contents == null) {
            contents = "";
        }
        exec("javascript:RE.setHtmlNodecode('" + contents + "');");
        mContents = contents;
    }

    public String getHtml() {
        return mContents;
    }

    public void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        Bitmap bitmap = Utils.decodeResource(getContext(), resid);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override
    public void setBackground(Drawable background) {
        Bitmap bitmap = Utils.toBitmap(background);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    public void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }

    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public void undo() {
        exec("javascript:RE.undo();");
    }

    public void redo() {
        exec("javascript:RE.redo();");
    }

    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    public void setHeading(int heading) {
        exec("javascript:RE.setHeading('" + heading + "');");
    }

    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    public void setBlockquote() {
        exec("javascript:RE.setBlockquote();");
    }

    public void setBullets() {
        exec("javascript:RE.setBullets();");
    }

    public void setNumbers() {
        exec("javascript:RE.setNumbers();");
    }

    public void insertData(String content) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:insertData('" + content + "');");
        L.i("设置内容insertHTML");
    }

    public void insertHR() {
        exec("javascript:RE.prepareInsert();");
        String aa = "javascript:RE.insertHR();";
        exec(aa);
    }

    public void insertSong(String songId, String songUrl, String imgUrl, String songName, String songAuthor) {
        exec("javascript:RE.prepareInsert();");

        String aa = null;
        try {
            aa = "javascript:RE.insertSong('" + songId + "', '" + songUrl + "', '" + imgUrl + "', '" + URLEncoder.encode(songName, "UTF-8") + "', '" + URLEncoder.encode(songAuthor, "UTF-8") + "');";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        exec(aa);
    }

    public void insertImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        String aa = "javascript:RE.insertImage('" + url + "', '" + alt + "');";
        Log.i("aaa", aa);
        exec(aa);
    }

    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    public void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + Utils.getCurrentTime() + "');");
    }

    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    protected void exec(final String trigger) {

        if (isReady) {
            Log.i("exec111", trigger);
            loadUrl(trigger);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    private void loadUrl(String trigger) {
        String hex = convertHexColorString(mColorValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
//      evaluateJavascript("javascript:RE.prepareInsert();", null);
//      evaluateJavascript("javascript:RE.setTextColor('" + hex + "');", null);
        } else {
            load(trigger, null);
//      load("javascript:RE.prepareInsert();", null);
//      load("javascript:RE.setTextColor('" + hex + "');", null);
        }


    }

    protected class EditorWebViewClient extends XWalkResourceClient {


        public EditorWebViewClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onLoadFinished(XWalkView view, String url) {
            isReady = url.equalsIgnoreCase(SETUP_HTML);
            Log.i("EditorWebViewClient", isReady + "--");
            if (mLoadListener != null) {
                mLoadListener.onAfterInitialLoad(isReady);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
                L.i("decode:  ", decode);
            } catch (UnsupportedEncodingException e) {
                // No handling
                return false;
            }
            String str[] = decode.split(" splitMValue ");
            //编辑页面的内容回调以及内容状态回调
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) != -1 || TextUtils.indexOf(url, STATE_SCHEME) != -1) {
                L.i("返回文本" + CALLBACK_SCHEME);
                if (str.length == 2) {
                    callback(str[0]);
                    stateCheck(str[1]);
                }
                return true;
            }
            //播放歌曲的url
            if (TextUtils.indexOf(url, MUSIC_CALLBACK) == 0) {
                L.i("MUSIC_CALLBACK:" + url);
                if (TextUtils.indexOf(url, PLAY_SONG) != -1) {
                    final String id = url.split("=")[1];
                    L.i("播放歌曲：" + id);
                    if (musictListener != null) {
                        musictListener.onPlayMusic(id); // 播放歌曲
                    }
                }
                if (TextUtils.indexOf(url, PAUSE_SONG) != -1) {
                    String id = "";
                    id = url.split("=")[1];
                    L.i(":暂停歌曲：" + id);
                    if (musictListener != null) {
                        musictListener.onPauseMusic(id); // 暂停歌曲
                    }
                }
                return true;

            }
//      if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
//        L.i("状态："+STATE_SCHEME);
//
//        return true;
//      }

            return super.shouldOverrideUrlLoading(view, url);
        }


    }


    // 重写该函数
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new myInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    private class myInputConnection extends InputConnectionWrapper {

        public myInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (deleteKeyListener != null) {
                    deleteKeyListener.onDeleteClick(); // 执行删除键的事件接口
                    return true;
                }
            } else {
                Log.d("zzzzz", "fuck key");
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            Log.i("zzzzz", "afterLength  " + beforeLength + "   afterLength   " + afterLength);
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    private OnDeleteKeytListener deleteKeyListener;

    public void OnDeleteKeytListener(OnDeleteKeytListener delKeyEventListener) {
        this.deleteKeyListener = deleteKeyListener;
    }

    public interface OnDeleteKeytListener {
        void onDeleteClick();
    }

    private OnMusictListener musictListener;

    public void OnMusictListener(OnMusictListener musictListener) {
        this.musictListener = musictListener;
    }
    public interface OnMusictListener {
        void onPlayMusic(String songId);
        void onPauseMusic(String songId);
    }
}