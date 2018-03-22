package com.shdnxc.cn.activity.bean;

import java.util.List;

/**
 * Created by Zheng Jungen on 2017/3/29.
 */
public class CarDevBean {
    /**
     * status : 200
     * data : [{"sn":"AA12165244A6E5129C4A","name":"","did":"119","model":"AA12","remark":"我的设备","status":"1","modelsId":"0","brandId":"151","seriesId":"4814","modelsName":"阿尔特 卡尔曼","createdAt":"2017-03-28 13:24:43"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sn : AA12165244A6E5129C4A
         * name :
         * did : 119
         * model : AA12
         * remark : 我的设备
         * status : 1
         * modelsId : 0
         * brandId : 151
         * seriesId : 4814
         * modelsName : 阿尔特 卡尔曼
         * createdAt : 2017-03-28 13:24:43
         */

        private String sn;
        private String name;
        private String did;
        private String model;
        private String remark;
        private String status;
        private String modelsId;
        private String brandId;
        private String seriesId;
        private String modelsName;
        private String createdAt;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getModelsId() {
            return modelsId;
        }

        public void setModelsId(String modelsId) {
            this.modelsId = modelsId;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getSeriesId() {
            return seriesId;
        }

        public void setSeriesId(String seriesId) {
            this.seriesId = seriesId;
        }

        public String getModelsName() {
            return modelsName;
        }

        public void setModelsName(String modelsName) {
            this.modelsName = modelsName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
