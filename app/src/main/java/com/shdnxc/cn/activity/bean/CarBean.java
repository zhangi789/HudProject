package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/3/24.
 */
public class CarBean {
    public CarBean(String id, String name, String hasChild) {
        this.id = id;
        this.name = name;
        this.hasChild = hasChild;
    }

    /**
     * id : 8
     * name : 大众
     * hasChild : 1
     */

    private String id;
    private String name;
    private String hasChild;



    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getHasChild() {
        return hasChild;
    }
    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }
}
