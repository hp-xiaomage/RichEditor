/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var songId='';
var song_image='';
var url=window.location.search;
//如果是有携带参数的话。
if(url.indexOf("?")!=-1){
    var str   =   url.substr(1)
    strs = str.split("&");
    //for循环的拆分url携带的参数
    for(i=0;i<strs.length;i++){

    	if([strs[i].split("=")[0]]=='songId'){
         	songId=unescape(strs[i].split("=")[1]);
         }

        if([strs[i].split("=")[0]]=='song_image'){
            song_image=unescape(strs[i].replace("song_image=",""));
        }

    }
    //给img标签添加传过来的图片路径
    document.getElementById("cover").src = song_image;
    //初始化操作
    loadiframe(songId) ;

}

//初始化iframe标签中的内容
 function loadiframe(songId) {
 //给播放图片按钮加入点击事件，事件会传入两个参数，一个是歌曲的id，一个是歌曲名（歌曲名可有可无，不想麻烦就删了）；
     document.getElementById('playOrpause').onclick=function(){
         playOrPausefun(songId);
     }

    //请求歌曲详情，地址改成外网的就行
    $.ajax({
        url: "你的歌曲详情获取接口"+songId,
        type: "get",
        dataType:'json',
        success:function (CallBack){
              //歌曲名跟作者名赋值给标签
                document.getElementById("song_name").innerText =CallBack.song.songs[0].name;
               document.getElementById("song_author").innerText =CallBack.song.songs[0].artists[0].name;
        },
        error:function(er){

        }
    });
 }

//控制播放暂停
function playOrPausefun(songId){
    var playFlag=document.getElementById('playflag').firstChild.nodeValue;
    if(playFlag==0){
        playSongById(songId);
        document.getElementById('playflag').innerText="1";
    }else{
        pauseSongById(songId);
        document.getElementById('playflag').innerText="0";
    }
}

 function playSongById(songId){
        //当前页面是属于iframe当中的，也就是属于一个子页面，所以需要跑到父页面中调用cleanPlayIcon方法，将整个页面的所有iframe的播放按钮图标转变为播放状态
        window.parent.cleanPlayIcon();
        //获取标签id为playOrpause的img标签，将图片路径改为start.png，也就是变成暂停按钮
        document.getElementById("playOrpause").src="stop.png";

        //此处是调用父页面的musicCallBack方法实现地址跳转，ios后台截取就行
        window.parent.musicCallBack(songId,0);


 }

//暂停歌曲
 function pauseSongById(songId){
        //获取标签id为playOrpause的img标签，将图片路径改为start.png，也就是变成播放按钮
        document.getElementById("playOrpause").src="start.png";

        //此处是调用父页面的musicCallBack方法实现地址跳转，ios后台截取就行
        window.parent.musicCallBack(songId,1);

 }



