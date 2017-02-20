package com.xmg.richeditor_android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmg.richeditor_android.Bean.SearchSong;
import com.xmg.richeditor_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class SongAdapter extends BaseAdapter {
    private List<SearchSong.ResultEntity.SongsEntity>  mAlbumInfos;
    private LayoutInflater inflater;
    private ViewHolder holder;

    public SongAdapter(Activity mContext) {
        mAlbumInfos=new ArrayList<>();
        inflater = mContext.getLayoutInflater();
    }

    public void setData(List<SearchSong.ResultEntity.SongsEntity> mAlbumInfos){
        for (SearchSong.ResultEntity.SongsEntity song:mAlbumInfos
             ) {
            this.mAlbumInfos.add(song);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        if(this.mAlbumInfos != null){
            mAlbumInfos.clear();
        }
    }

    @Override
    public int getCount() {
        return mAlbumInfos == null ? 0 : mAlbumInfos.size();
    }

    @Override
    public SearchSong.ResultEntity.SongsEntity getItem(int position) {
        return mAlbumInfos == null ? null : mAlbumInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchSong.ResultEntity.SongsEntity songs = this.mAlbumInfos.get(position);


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_song_listitem, null);
            holder.song_name = (TextView) convertView.findViewById(R.id.song_name);
            holder.song_author = (TextView) convertView.findViewById(R.id.song_author);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.song_name.setText(songs.getName() == null ? "" : songs.getName());
        List<SearchSong.ResultEntity.SongsEntity.ArtistsEntity> artists = songs.getArtists();
        if(artists!=null){
            if(artists.size()>0){
                String name = artists.get(0).getName();
                holder.song_author.setText(name == null ? "" : name);
            }else{

            }
        }else{

        }


        return convertView;
    }

    public class ViewHolder {
        TextView song_num;
        TextView song_name;
        TextView song_author;
    }
}
