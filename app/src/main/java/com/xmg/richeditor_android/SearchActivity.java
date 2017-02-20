package com.xmg.richeditor_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xmg.richeditor_android.Bean.SearchSong;
import com.xmg.richeditor_android.adapter.SongAdapter;
import com.xmg.richeditor_android.utils.Constant;
import com.xmg.richeditor_android.utils.GsonUtil;
import com.xmg.richeditor_android.utils.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者： Administrator on 2015/9/8.
 * 邮箱：maxz@tingwin.com
 */
public class SearchActivity extends Activity implements  View.OnClickListener {


    ListView songListView;
    SongAdapter mSongAdapter;
    int songOffset = 0;
    private TextView search_track_cancelorsearch;
    private EditText search_track_keyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_track);
        initDatas();
        initViews();
    }

    void initDatas() {
        mSongAdapter = new SongAdapter(this);
    }



    void initViews() {

        songListView = (ListView) findViewById(R.id.search_recordandsugges);
        search_track_keyword= (EditText) findViewById(R.id.search_track_keyword);
        search_track_cancelorsearch= (TextView) findViewById(R.id.search_track_cancelorsearch);
        search_track_cancelorsearch.setOnClickListener(this);
        songListView.setAdapter(mSongAdapter);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickSongItemToEdit(position);
            }
        });


    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_track_cancelorsearch:
                String trim = search_track_keyword.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    L.i("请输入搜索内容");
                    return;
                }
                searchSong(trim, true);
                break;
        }
    }





    /**
     * 歌曲返回到编辑页面方法
     * @param position
     */
    void clickSongItemToEdit(int position){
        try {
            SearchSong.ResultEntity.SongsEntity songsEntity = (SearchSong.ResultEntity.SongsEntity) songListView.getAdapter().getItem(position);
            List<SearchSong.ResultEntity.SongsEntity.ArtistsEntity> artists = songsEntity.getArtists();

            Intent intent=new Intent();
            intent.putExtra("songId", String.valueOf(songsEntity.getId()));
            intent.putExtra("songName", String.valueOf(songsEntity.getName()));
            if(artists!=null){
                if(artists.size()>0){
                    String name = artists.get(0).getName();
                    if(name!=null){
                        intent.putExtra("songAuthor", name);
                    }
                }
            }

            setResult(Constant.SEARCHTOEDITACTIVITY, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 查找歌曲
     * @param key
     */
    public void searchSong(String key,  final boolean isRefersh) {
        //数据库搜索记录操作

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", key);
        params.put("limit", "100" );
        params.put("type", "1");
        params.put("total", "true");
        params.put("offset", isRefersh ? "0" : String.valueOf(songOffset));

        String url = "http://music.163.com/api/search/get/";
        OkHttpUtils.post().url(url).tag(this).addHeader("Cookie", "appver=1.5.2").addHeader("referer", "http://music.163.com/").params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                L.i("歌曲获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                L.i("获取成功："+response);
                try {
                    SearchSong users = GsonUtil.getBean(response, SearchSong.class);

                    int songCount =0;
                    L.i(songCount + "   songcount");
                    if (users != null) {
                        List<SearchSong.ResultEntity.SongsEntity> songs = users.getResult().getSongs();
                        if (songs != null) {
                            L.i(songs.size() + "   获取过来的长度");
                            if (songs.size() > 0) {
                                if (isRefersh) {
                                    mSongAdapter.clear();
                                }
                                mSongAdapter.setData(songs);
                            }

                        } else {
                            L.i("   onResponse111111");
                        }

                    } else {
                        L.i("   onResponse22222");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
