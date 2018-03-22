package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/4/1.
 */
public class AddressBean {
    private String addressId;
    private String addressName;

    public AddressBean(String addressId, String addressName) {
        this.addressId = addressId;
        this.addressName = addressName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}
