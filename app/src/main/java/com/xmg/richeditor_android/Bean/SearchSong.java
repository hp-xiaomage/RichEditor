package com.xmg.richeditor_android.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class SearchSong implements Serializable{

    private ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity  implements Serializable{
        /**
         * id : 428387668
         * name : 学校大合唱我们班我编曲哈哈哈哈就是这首demo啦啦啦啦啦
         * artists : [{"id":221688,"name":"MxsTones","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p3.music.126.net/VnZiScyynLG7atLIZ2YPkw==/18686200114669622.jpg","img1v1":18686200114669624,"trans":null}]
         * album : {"id":34863354,"name":"暂时发一个demo","artist":{"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p3.music.126.net/VnZiScyynLG7atLIZ2YPkw==/18686200114669622.jpg","img1v1":18686200114669624,"trans":null},"publishTime":1473258843352,"size":1,"copyrightId":0,"status":1,"picId":17994607300387796}
         * duration : 56633
         * copyrightId : 0
         * status : 0
         * alias : []
         * rtype : 0
         * ftype : 0
         * mvid : 0
         * fee : 0
         * rUrl : null
         */

        private List<SongsEntity> songs;

        public List<SongsEntity> getSongs() {
            return songs;
        }

        public void setSongs(List<SongsEntity> songs) {
            this.songs = songs;
        }

        public static class SongsEntity implements Serializable{
            private int id;
            private String name;
            /**
             * id : 34863354
             * name : 暂时发一个demo
             * artist : {"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p3.music.126.net/VnZiScyynLG7atLIZ2YPkw==/18686200114669622.jpg","img1v1":18686200114669624,"trans":null}
             * publishTime : 1473258843352
             * size : 1
             * copyrightId : 0
             * status : 1
             * picId : 17994607300387796
             */

            private AlbumEntity album;
            private int duration;
            private int copyrightId;
            private int status;
            private int rtype;
            private int ftype;
            private int mvid;
            private int fee;
            private Object rUrl;
            /**
             * id : 221688
             * name : MxsTones
             * picUrl : null
             * alias : []
             * albumSize : 0
             * picId : 0
             * img1v1Url : http://p3.music.126.net/VnZiScyynLG7atLIZ2YPkw==/18686200114669622.jpg
             * img1v1 : 18686200114669624
             * trans : null
             */

            private List<ArtistsEntity> artists;
            private List<?> alias;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public AlbumEntity getAlbum() {
                return album;
            }

            public void setAlbum(AlbumEntity album) {
                this.album = album;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getCopyrightId() {
                return copyrightId;
            }

            public void setCopyrightId(int copyrightId) {
                this.copyrightId = copyrightId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getRtype() {
                return rtype;
            }

            public void setRtype(int rtype) {
                this.rtype = rtype;
            }

            public int getFtype() {
                return ftype;
            }

            public void setFtype(int ftype) {
                this.ftype = ftype;
            }

            public int getMvid() {
                return mvid;
            }

            public void setMvid(int mvid) {
                this.mvid = mvid;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public Object getRUrl() {
                return rUrl;
            }

            public void setRUrl(Object rUrl) {
                this.rUrl = rUrl;
            }

            public List<ArtistsEntity> getArtists() {
                return artists;
            }

            public void setArtists(List<ArtistsEntity> artists) {
                this.artists = artists;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }

            public static class AlbumEntity implements Serializable {
                private int id;
                private String name;
                /**
                 * id : 0
                 * name :
                 * picUrl : null
                 * alias : []
                 * albumSize : 0
                 * picId : 0
                 * img1v1Url : http://p3.music.126.net/VnZiScyynLG7atLIZ2YPkw==/18686200114669622.jpg
                 * img1v1 : 18686200114669624
                 * trans : null
                 */

                private ArtistEntity artist;
                private long publishTime;
                private int size;
                private int copyrightId;
                private int status;
                private long picId;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public ArtistEntity getArtist() {
                    return artist;
                }

                public void setArtist(ArtistEntity artist) {
                    this.artist = artist;
                }

                public long getPublishTime() {
                    return publishTime;
                }

                public void setPublishTime(long publishTime) {
                    this.publishTime = publishTime;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public int getCopyrightId() {
                    return copyrightId;
                }

                public void setCopyrightId(int copyrightId) {
                    this.copyrightId = copyrightId;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public long getPicId() {
                    return picId;
                }

                public void setPicId(long picId) {
                    this.picId = picId;
                }

                public static class ArtistEntity implements Serializable {
                    private int id;
                    private String name;
                    private Object picUrl;
                    private int albumSize;
                    private int picId;
                    private String img1v1Url;
                    private long img1v1;
                    private Object trans;
                    private List<?> alias;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public Object getPicUrl() {
                        return picUrl;
                    }

                    public void setPicUrl(Object picUrl) {
                        this.picUrl = picUrl;
                    }

                    public int getAlbumSize() {
                        return albumSize;
                    }

                    public void setAlbumSize(int albumSize) {
                        this.albumSize = albumSize;
                    }

                    public int getPicId() {
                        return picId;
                    }

                    public void setPicId(int picId) {
                        this.picId = picId;
                    }

                    public String getImg1v1Url() {
                        return img1v1Url;
                    }

                    public void setImg1v1Url(String img1v1Url) {
                        this.img1v1Url = img1v1Url;
                    }

                    public long getImg1v1() {
                        return img1v1;
                    }

                    public void setImg1v1(long img1v1) {
                        this.img1v1 = img1v1;
                    }

                    public Object getTrans() {
                        return trans;
                    }

                    public void setTrans(Object trans) {
                        this.trans = trans;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }

                    public void setAlias(List<?> alias) {
                        this.alias = alias;
                    }
                }
            }

            public static class ArtistsEntity  implements Serializable{
                private int id;
                private String name;
                private Object picUrl;
                private int albumSize;
                private int picId;
                private String img1v1Url;
                private long img1v1;
                private Object trans;
                private List<?> alias;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(Object picUrl) {
                    this.picUrl = picUrl;
                }

                public int getAlbumSize() {
                    return albumSize;
                }

                public void setAlbumSize(int albumSize) {
                    this.albumSize = albumSize;
                }

                public int getPicId() {
                    return picId;
                }

                public void setPicId(int picId) {
                    this.picId = picId;
                }

                public String getImg1v1Url() {
                    return img1v1Url;
                }

                public void setImg1v1Url(String img1v1Url) {
                    this.img1v1Url = img1v1Url;
                }

                public long getImg1v1() {
                    return img1v1;
                }

                public void setImg1v1(long img1v1) {
                    this.img1v1 = img1v1;
                }

                public Object getTrans() {
                    return trans;
                }

                public void setTrans(Object trans) {
                    this.trans = trans;
                }

                public List<?> getAlias() {
                    return alias;
                }

                public void setAlias(List<?> alias) {
                    this.alias = alias;
                }
            }
        }
    }
}
