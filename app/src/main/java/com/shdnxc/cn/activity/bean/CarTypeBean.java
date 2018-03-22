package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/3/29.
 */
public class CarTypeBean {
    /**
     * status : 200
     * data : {"did":"119","sn":"AA12165244A6E5129C4A","name":"","model":"AA12","status":"1","remark":"我的设备","createdAt":"2017-03-29 16:25:19","modelsId":"118859","brandId":"9","seriesId":"3999","modelsName":"奥迪 A3 2016款 Sportback 35TFSI 进取型"}
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
        /**
         * did : 119
         * sn : AA12165244A6E5129C4A
         * name :
         * model : AA12
         * status : 1
         * remark : 我的设备
         * createdAt : 2017-03-29 16:25:19
         * modelsId : 118859
         * brandId : 9
         * seriesId : 3999
         * modelsName : 奥迪 A3 2016款 Sportback 35TFSI 进取型
         */

        private String did;
        private String sn;
        private String name;
        private String model;
        private String status;
        private String remark;
        private String createdAt;
        private String modelsId;
        private String brandId;
        private String seriesId;
        private String modelsName;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

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

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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
    }
}
