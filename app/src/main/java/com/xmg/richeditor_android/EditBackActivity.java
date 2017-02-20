package com.xmg.richeditor_android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmg.richeditor_android.Bean.BaseSong;
import com.xmg.richeditor_android.crop.CropParams;
import com.xmg.richeditor_android.richeditor.RichEditor;
import com.xmg.richeditor_android.service.PlayerService;
import com.xmg.richeditor_android.utils.Constant;
import com.xmg.richeditor_android.utils.GsonUtil;
import com.xmg.richeditor_android.utils.L;
import com.xmg.richeditor_android.utils.MusicControl;
import com.xmg.richeditor_android.utils.PopupMenuCompat;
import com.xmg.richeditor_android.utils.TextColorPopWindow;
import com.xmg.richeditor_android.utils.WindowUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xwalk.core.JavascriptInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * @author majunze
 * @Title: SettingActivity
 * @Description: 类的描述 - 设置Act
 * @date 2015-11-30
 * @email 704188225@qq.com
 */
public class EditBackActivity extends Activity implements View.OnLayoutChangeListener {

    //EditBackActivity选择图片回调
    public static final int    REQUEST_IMAGE=1001;

    //content控件
    private RichEditor edit_addcontent;

    private RelativeLayout edit_bottom_linear;
    private HorizontalScrollView edit_bottom_scroll;
    private TextView edit_bottom_top;
    private View activityRootView;
    private ImageView edit_other;

    private ImageView edit_style;
    private TextColorPopWindow morePopWindow;
    private ImageButton action_bold;
    private ImageButton action_italic;
    private ImageButton action_underline;
    private ImageButton action_heading1;
    private ImageButton action_heading2;
    private ImageButton action_heading3;
    private ImageButton action_align_left;
    private ImageButton action_align_center;
    private ImageButton action_align_right;
    private ImageButton action_blockquote;

    //编辑器是否获得了焦点
    private boolean isFocus = false;

    //相册返回
    private ArrayList<String> mSelectPath;
    private Map<String, String> mUrlPathMap = new HashMap<>();
    //富文本编辑返回的内容
    private String editContentHTML;
    //键盘
    private InputMethodManager inputMethodManager;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    //添加的歌曲列表
    private List<BaseSong.SongsEntity> songs = new ArrayList<>();
    //图片选择
    CropParams mCropParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_back);
        mCropParams = new CropParams(this, WindowUtils.getDisplayMetricsWidth(this) * 2, WindowUtils.getDisplayMetricsWidth(this), WindowUtils.getDisplayMetricsWidth(this) * 2, WindowUtils.getDisplayMetricsWidth(this));
        initDatas();
        initViews();

    }

    void initDatas() {
        //键盘对象
        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    void initViews() {
        //header控件
        edit_other = (ImageView) findViewById(R.id.edit_other);
        edit_style = (ImageView) findViewById(R.id.edit_style);

        edit_other.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isFocus) {
                    return;
                }
                PopupMenuCompat menu = PopupMenuCompat.newInstance(EditBackActivity.this, v);
                menu.inflate(R.menu.edit_item);
                menu.setOnMenuItemClickListener(new PopupMenuCompat.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_line:
                                edit_addcontent.insertHR();
                                break;
//                            case R.id.add_link:
//                                addLink();
//                                break;
                            case R.id.add_image:
                                edit_add_img();
                                break;
                            case R.id.add_song:
                                edit_selsong();
                                break;
                            case R.id.add_lyric:
                                edit_lyric();
                                break;
                        }

                        return true;
                    }
                });

                menu.show();
            }
        });


        //content控件
        edit_addcontent = (RichEditor) this.findViewById(R.id.edit_addcontent);
        edit_bottom_linear = (RelativeLayout) this.findViewById(R.id.edit_bottom_linear);
        edit_bottom_scroll = (HorizontalScrollView) this.findViewById(R.id.edit_bottom_scroll);
        edit_bottom_top = (TextView) this.findViewById(R.id.edit_bottom_top);
        activityRootView = findViewById(R.id.root_layout);

        //初始化编辑器
        initRichEditor();


        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;



    }


    /**
     * 初始化富文本编辑器
     */
    private void initRichEditor() {
//        edit_addcontent.setEditorHeight(WindowUtils.getDisplayMetricsHeight(this));
//        edit_addcontent.setEditorWidth(WindowUtils.getDisplayMetricsWidth(this));
//        edit_addcontent.setEditorFontSize(22);
        edit_addcontent.setEditorFontColor(R.color.black);
//        edit_addcontent.setEditorBackgroundColor(R.color.me_center_bg);
//        edit_addcontent.setBackgroundColor(R.color.me_center_bg);
        //edit_addcontent.setBackgroundResource(R.drawable.bg);
        edit_addcontent.setPadding(10, 10, 10, 10);
//        edit_addcontent.setMinimumHeight(500);
        edit_addcontent.setPlaceholder("请输入正文");
//        content = "<br><iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=\"100%\" height=\"86\" src=\"music.html?song_image=http://p3.music.126.net/KtaEN7Envm2n6czHgP_8Gg==/63771674416417.jpg&amp;songId=362570\"></iframe>家门口<br><br><font color=\"#846954\">科莫尼科夫健康快乐</font><br><font color=\"#687213\">摸摸</font><br><font color=\"#687213\">来看看呕吐吞吞吐吐吞吞吐吐</font><br><font color=\"#4e93aa\">图腾墨摸摸米娜涅米宁</font><br><font color=\"#868686\">诺曼卡里姆摸摸</font><br><iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=\"100%\" height=\"86\" src=\"music.html?song_image=http://p3.music.126.net/Sbhanu6TSPEOq655lj34Gg==/98956046505532.jpg&amp;songId=187374\"></iframe><br><hr align=\"center\" width=\"100%\" color=\"#B6B6B6\" size=\"1\"><br><img width=\"100%\" src=\"http://7xo4bt.com2.z0.glb.qiniucdn.com/FtOcADOYnHvnYc0HfGjA_FScrNiu\" alt=\"aaaa\"><br><hr align=\"center\" width=\"100%\" color=\"#B6B6B6\" size=\"1\"><br><div style=\"text-align: center;\"><p style=\"color: rgb(163, 163, 163);\">抱紧一些</p><p style=\"color: rgb(163, 163, 163);\">如果看不见我的明天</p><p style=\"color: rgb(163, 163, 163);\">要怎么永远</p><p style=\"color: rgb(163, 163, 163);\">I don't wanna lose You</p><p style=\"color: rgb(163, 163, 163);\">你每个笑脸</p><p style=\"color: rgb(163, 163, 163);\">我都看得见</p></div><br>";
//        edit_addcontent.setHtmlNodecode(content.replace("'", "&apos;"));
        //添加script回调接口
        edit_addcontent.addJavascriptInterface(new JsInterface(), "NativeInterface");
        L.i(":设置回调接口");
        edit_addcontent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        edit_addcontent.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {

            @Override
            public void onStateChangeListener(String text, Map<String, String> types) {
                isFocus = true;
                setStyleUI(types);
                edit_other.setImageResource(R.mipmap.edit_other);
                edit_style.setImageResource(R.mipmap.edit_textcolor);
            }
        });

        //内容文本改变监听
        edit_addcontent.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                editContentHTML = text;
                L.i(text);

            }
        });

        edit_addcontent.OnMusictListener(new RichEditor.OnMusictListener() {
            @Override
            public void onPlayMusic(String songId) {
                L.i("播放歌曲："+songId);
                playOrPause(0,songId);
            }

            @Override
            public void onPauseMusic(String songId) {
                L.i("暂停歌曲："+songId);
                playOrPause(1,songId);
            }
        });

        View action_undo = findViewById(R.id.action_undo);
        action_undo.clearFocus();
        action_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.undo();
            }
        });

        View action_redo = findViewById(R.id.action_redo);
        action_redo.clearFocus();
        action_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.redo();
            }
        });

        action_bold = (ImageButton) findViewById(R.id.action_bold);
        action_bold.clearFocus();
        action_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setBold();
                if(action_bold.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_b_click).getConstantState())){
                    action_bold.setImageResource(R.mipmap.edit_b);
                }else{

                    action_bold.setImageResource(R.mipmap.edit_b_click);
                }

            }
        });

        action_italic = (ImageButton) findViewById(R.id.action_italic);
        action_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setItalic();
                if(action_italic.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_i_click).getConstantState())){
                    action_italic.setImageResource(R.mipmap.edit_i);
                }else{

                    action_italic.setImageResource(R.mipmap.edit_i_click);
                }
            }
        });


        action_underline = (ImageButton) findViewById(R.id.action_underline);
        action_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setUnderline();
                if(action_underline.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_u_click).getConstantState())){
                    action_underline.setImageResource(R.mipmap.edit_u);
                }else{

                    action_underline.setImageResource(R.mipmap.edit_u_click);
                }
            }
        });

        action_heading1 = (ImageButton) findViewById(R.id.action_heading1);
        action_heading1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setHeading(1);
                if(action_heading1.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_h1_click).getConstantState())){
                    action_heading1.setImageResource(R.mipmap.edit_h1);
                }else{
                    action_heading1.setImageResource(R.mipmap.edit_h1_click);
                }
            }
        });

        action_heading2 = (ImageButton) findViewById(R.id.action_heading2);
        action_heading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setHeading(2);
                if(action_heading2.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_h2_click).getConstantState())){
                    action_heading2.setImageResource(R.mipmap.edit_h2);
                }else{
                    action_heading2.setImageResource(R.mipmap.edit_h2_click);
                }
            }
        });
        action_heading3 = (ImageButton) findViewById(R.id.action_heading3);
        action_heading3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setHeading(3);
                if(action_heading3.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_h3_click).getConstantState())){
                    action_heading3.setImageResource(R.mipmap.edit_h3);
                }else{
                    action_heading3.setImageResource(R.mipmap.edit_h3_click);
                }
            }
        });


        edit_style.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isFocus) {
                    return;
                }
                morePopWindow = new TextColorPopWindow(EditBackActivity.this);
                morePopWindow.setmOnClickListListener(new TextColorPopWindow.onColorbackListener() {
                    @Override
                    public void onClickList(int colorValue) {
                        edit_addcontent.mColorValue = colorValue;
                        edit_addcontent.setTextColor(colorValue);
//                        edit_style.setTextColor(colorValue);

                        morePopWindow.dismiss();
                    }
                });
                morePopWindow.showPopupWindow(edit_style);
            }
        });

//        findViewById(R.id.edit_style).setOnClickListener(new View.OnClickListener() {
//
//            @Override public void onClick(View v) {
//                edit_addcontent.setTextBackgroundColor(Color.YELLOW);
//                edit_style.setBackgroundColor(Color.YELLOW);
//            }
//        });

        action_align_left = (ImageButton) findViewById(R.id.action_align_left);
        action_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignLeft();
                if(action_align_left.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_left_click).getConstantState())){
                    action_align_left.setImageResource(R.mipmap.edit_left);
                }else{
                    action_align_left.setImageResource(R.mipmap.edit_left_click);
                }
            }
        });
        action_align_center = (ImageButton) findViewById(R.id.action_align_center);
        action_align_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignCenter();
                if(action_align_center.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_center_click).getConstantState())){
                    action_align_center.setImageResource(R.mipmap.edit_center);
                }else{
                    action_align_center.setImageResource(R.mipmap.edit_center_click);
                }
            }
        });
        action_align_right = (ImageButton) findViewById(R.id.action_align_right);
        action_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignRight();
                if(action_align_right.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_right_click).getConstantState())){
                    action_align_right.setImageResource(R.mipmap.edit_right);
                }else{
                    action_align_right.setImageResource(R.mipmap.edit_right_click);
                }
            }
        });
        action_blockquote = (ImageButton) findViewById(R.id.action_blockquote);
        action_blockquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setBlockquote();
                if(action_blockquote.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_use_click).getConstantState())){
                    action_blockquote.setImageResource(R.mipmap.edit_use);
                }else{
                    action_blockquote.setImageResource(R.mipmap.edit_use_click);
                }
            }
        });


    }


    public void setStyleUI(Map<String, String> type) {
        //加粗
        if (type.get("BOLD") != null) {
            action_bold.setImageResource(R.mipmap.edit_b_click);
        } else {
            action_bold.setImageResource(R.mipmap.edit_b);
        }

        //斜体
        if (type.get("ITALIC") != null) {
            action_italic.setImageResource(R.mipmap.edit_i_click);
        } else {
            action_italic.setImageResource(R.mipmap.edit_i);
        }

        //下划线
        if (type.get("UNDERLINE") != null) {
            action_underline.setImageResource(R.mipmap.edit_u_click);
        } else {
            action_underline.setImageResource(R.mipmap.edit_u);
        }
        //居中对齐
        if (type.get("JUSTIFYCENTER") != null) {
            action_align_center.setImageResource(R.mipmap.edit_center_click);
        } else {
            action_align_center.setImageResource(R.mipmap.edit_center);
        }
        //两端对齐
        if (type.get("JUSTIFYFULL") != null) {
        } else {

        }
        //左对齐
        if (type.get("JUSTIFYLEFT") != null) {
            action_align_left.setImageResource(R.mipmap.edit_left_click);
        } else {
            action_align_left.setImageResource(R.mipmap.edit_left);
        }
        //右对齐
        if (type.get("JUSTIFYRIGHT") != null) {
            action_align_right.setImageResource(R.mipmap.edit_right_click);
        } else {
            action_align_right.setImageResource(R.mipmap.edit_right);
        }
        //段落
        if (type.get("FORMATBLOCK") != null) {
            action_blockquote.setImageResource(R.mipmap.edit_use_click);
        } else {
            action_blockquote.setImageResource(R.mipmap.edit_use);
        }
        if (type.get("H1") != null) {
            action_heading1.setImageResource(R.mipmap.edit_h1_click);
        } else {
            action_heading1.setImageResource(R.mipmap.edit_h1);
        }
        if (type.get("H2") != null) {
            action_heading2.setImageResource(R.mipmap.edit_h2_click);
        } else {
            action_heading2.setImageResource(R.mipmap.edit_h2);
        }
        if (type.get("H3") != null) {
            action_heading3.setImageResource(R.mipmap.edit_h3_click);
        } else {
            action_heading3.setImageResource(R.mipmap.edit_h3);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (edit_addcontent != null) {
            edit_addcontent.pauseTimers();
            edit_addcontent.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        if (edit_addcontent != null) {
            edit_addcontent.resumeTimers();
            edit_addcontent.onShow();
        }
    }


    /**
     * 选择歌曲
     */
    private void edit_selsong() {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivityForResult(intent, Constant.SEARCHTOEDITACTIVITY);
    }


    /**
     * 歌词
     */
    void edit_lyric() {
//        traverseSongs();
//        if (songs.size() == 0) {
//            Toast.makeText(this, "请先添加歌曲！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!edit_addcontent.hasFocus()) {
//            T.show(this, "请选择正文输入框", 1000);
//            Logs.i("正文輸入框沒有獲取到焦點");
//            return;
//        }
//        NormalListDialogAdapter();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (edit_addcontent != null) {
            edit_addcontent.onActivityResult(requestCode, resultCode, data);
        }
        try {

            switch (requestCode) {

                case Constant.SEARCHTOEDITACTIVITY:
                    if (data == null) {
                        return;
                    }
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    String songId = bundle.getString("songId");
                    String songName = bundle.getString("songName");
                    String songAuthor = bundle.getString("songAuthor");
                    setBackSong(songId, songName, songAuthor);
                    break;
                case Constant.LYRICTOEDITACTIVITY:
                    if (data == null) {
                        return;
                    }
                    Bundle lyricbundle = data.getExtras();
                    if (lyricbundle == null) {
                        return;
                    }
//                    lyricList = (List<LyricSentence>) lyricbundle.getSerializable("lyricList");
//                    StringBuffer sbf = new StringBuffer();
//                    sbf.append("<br><div style=\"text-align: center;\">");
//                    for (LyricSentence itm : lyricList
//                            ) {
//                        L.i("---" + itm.getContentText());
//                        String result = itm.getContentText().replace("'", "&apos;");
//                        sbf.append("<p style=\"color:#A3A3A3;font-size:18px;\">" + result + "</p>");
//                    }
//                    sbf.append("</div><br>");
//                    L.i(sbf.toString());
//                    //在光标位置插入数据
//                    edit_addcontent.insertData(sbf.toString());

                    break;
                case Constant.REQUEST_IMAGE:
                    if (resultCode == RESULT_OK) {
                        mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        if (mSelectPath != null) {
                            for (int i = 0; i < mSelectPath.size(); i++) {
                                L.i(mSelectPath.get(i) + "    路径");
                                if (!TextUtils.isEmpty(mSelectPath.get(i))) {
                                    edit_addcontent.insertImage(mSelectPath.get(i), "aaaa");
                                    mUrlPathMap.put(mSelectPath.get(i), mSelectPath.get(i));
                                }
                            }
                        }

                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void setBackSong(String songId, String songName, String songAuthor) {
        // TODO: 2016/3/9
        L.i("回调过来的歌曲id：" + songId);
        searchSongInfo(songId);
    }


    /**
     * 获取歌曲详情
     *
     * @param id 歌曲id
     */
    public void searchSongInfo(String id) {
        L.i("开始查找歌曲详情");
        String url = Constant.SONGINFOURL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("ids", "[" + id + "]");
        OkHttpUtils.post().url(url).tag(this).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                L.i("未知歌曲，请从新选择");
            }

            @Override
            public void onResponse(String response, int id) {
                L.i("获取成功：" + response);
                try {
                    BaseSong bean = GsonUtil.getBean(response, BaseSong.class);

                    BaseSong.SongsEntity songsEntity = bean.getSongs().get(0);
                    if(songsEntity!=null){
                        String songInfo = getSongInfo(songsEntity.getId() + "", 1);
                        if(TextUtils.isEmpty(songInfo)){
                            songs.add(songsEntity);
                        }
                    }
                    String s = GsonUtil.toJsonString(songsEntity);
                    L.i("歌曲内容为：" + s);
                    if (songsEntity != null) {
                        edit_addcontent.insertSong(songsEntity.getId() + "", songsEntity.getMp3Url(), songsEntity.getAlbum().getPicUrl(), songsEntity.getName(), songsEntity.getArtists().get(0).getName());
                    } else {
                       L.i("未知歌曲");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        });
    }



    public void playOrPause(int state,final String id){
        if(state==0){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                    Intent intent = new Intent(EditBackActivity.this, PlayerService.class);
                    //android5.0以上的系统不支持隐式启动服务
//            intent.setAction("com.sdhz.yuko.service.MUSIC_SERVICE");
                    intent.putExtra("url", getSongInfo(id, 2));
                    intent.putExtra("listPosition", 0);
                    intent.putExtra("MSG", MusicControl.PLAY_MSG);
                    startService(intent);

                }
            }, 0); //0 for release
        }else{
            Intent intent2 = new Intent(EditBackActivity.this, PlayerService.class);
            intent2.setAction("com.sdhz.yuko.service.MUSIC_SERVICE");
            intent2.putExtra("MSG", MusicControl.STOP_MSG);
            startService(intent2);
        }

    }


    /**
     * 布局改变回调方法，用来做底部控件的显示和隐藏
     *
     * @param v
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param oldLeft
     * @param oldTop
     * @param oldRight
     * @param oldBottom
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//            scrollview.scrollTo(0, edit_bottom_linear.getTop());// 改变滚动条的位置1
            edit_bottom_scroll.setVisibility(View.VISIBLE);
            edit_bottom_top.setVisibility(View.INVISIBLE);

//            Toast.makeText(this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
            L.i("是否显示了1：" + (edit_bottom_linear.getVisibility() == View.VISIBLE));

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            edit_bottom_scroll.setVisibility(View.INVISIBLE);
            edit_bottom_top.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            L.i("是否显示了2：" + (edit_bottom_linear.getVisibility() == View.VISIBLE));
        }

    }

    /**
     * 点击添加图片
     */
    private void edit_add_img() {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 3);
        // 选择模式: 单选MultiImageSelectorActivity.MODE_SINGLE;  多选MultiImageSelectorActivity.MODE_MULTI
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (edit_addcontent != null) {
            edit_addcontent.onDestroy();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (edit_addcontent != null) {
            edit_addcontent.onNewIntent(intent);
        }
    }



    public class JsInterface {
        public JsInterface() {
        }

        @JavascriptInterface
        public String getNameAndAuthor(String songId) {
            return getSongInfo(songId, 1);
        }

        /**
         * JavascriptInterface这个是xwalkview的接口annotation
         * 打开用户详情页
         */
        @JavascriptInterface
        public void userDetails() {

        }

        @JavascriptInterface
        public void playSong(final String id) {

            L.i(":开始播放歌曲：" + id);
        }

        @JavascriptInterface
        public void pauseSong(String id) {
            L.i(":暂停歌曲：" + id);

        }
    }


    /**
     * 获取歌曲的一些信息
     * @param songId 歌曲id
     * @param getFlag 获取区分标识
     * @return
     */
    private String getSongInfo(String songId, int getFlag) {
        L.i("songId:" + songId);
        Iterator<BaseSong.SongsEntity> iterator = songs.iterator();
        while (iterator.hasNext()) {
            BaseSong.SongsEntity next = iterator.next();
            if ((next.getId() + "").equals(songId)) {
                if (getFlag == 1) {
                    String s = next.getName() + "---" + next.getArtists().get(0).getName();
                    L.i(s);
                    return s;
                } else {
                    L.i(next.getMp3Url());
                    return next.getMp3Url();
                }

            }
        }
        return "";
    }

}
