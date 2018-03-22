package com.shdnxc.cn.activity.bean;

import java.util.List;

/**
 * Created by Zheng Jungen on 2017/3/22.
 */
public class FindBean {
    /**
     * status : 200
     * data : {"banner":[{"title":"百度","url":"http://m.baidu.com","imageUrl":"http://dnxc.waywings.com/image/advert/750x350/2/20160822/61c2656791b08344.png"},{"title":"阿里巴巴","url":"http://www.1688.com","imageUrl":"http://dnxc.waywings.com/image/advert/750x350/9/20160822/6545948a58d50510.png"},{"title":"测试","url":"http://www.baidu.com","imageUrl":"http://dnxc.waywings.com/image/advert/750x350/4/20160822/6143f88c5ba7b58d.png"},{"title":"小米","url":"http://www.miui.com","imageUrl":"http://dnxc.waywings.com/image/advert/750x350/5/20160822/4ae6a9926cfc213d.png"}],"channel":[{"title":"商城","url":"http://www.baidu.com"},{"title":"资讯","url":"http://dnxc.waywings.com/wap/article/index"}]}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<BannerBean> banner;
        private List<ChannelBean> channel;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<ChannelBean> getChannel() {
            return channel;
        }

        public void setChannel(List<ChannelBean> channel) {
            this.channel = channel;
        }

        public static class BannerBean {
            /**
             * title : 百度
             * url : http://m.baidu.com
             * imageUrl : http://dnxc.waywings.com/image/advert/750x350/2/20160822/61c2656791b08344.png
             */

            private String title;
            private String url;
            private String imageUrl;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }

        public static class ChannelBean {
            /**
             * title : 商城
             * url : http://www.baidu.com
             */

            private String title;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
