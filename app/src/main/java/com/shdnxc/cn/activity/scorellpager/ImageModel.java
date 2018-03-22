package com.shdnxc.cn.activity.scorellpager;

/**
 * Created by 张海洋 on 10.04.2017.
 */

public class ImageModel {
    private int image;
    private boolean checked;

    public ImageModel(int image, boolean checked) {
        this.image = image;
        this.checked = checked;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
