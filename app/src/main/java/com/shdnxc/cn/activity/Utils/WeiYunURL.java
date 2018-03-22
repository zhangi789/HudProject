package com.shdnxc.cn.activity.Utils;

/**
 * Created by Zheng Jungen on 2017/3/1.
 */
public class WeiYunURL {
    public static String BASEAPI = "http://api.dnxc.waywings.com/api/v1/";
    /**
     * 注册发手机验证码
     * 接口
     * 参数配置
     */
    public static String RE_PHONE_MUBER = BASEAPI + "user/sendcode";

    /**
     * 注册
     */

    public static String RE_PHONE_REGISTER = BASEAPI + "user/register";


    /**
     * 找回密码
     */
    public static String RE_SECRET_REGISTER = BASEAPI + "user/findpwd";

    /**
     * 登陆
     */
    public static String BLE_LOGIN_URL = BASEAPI + "user/login";
    /**
     * 固件升级
     */
    public static String BLE_UPDATA_URL = BASEAPI + "app/firmware";


    public static String NOMAL_TOKEN = "c0781773d71605775dd0be4ba4b12e27";

    //绑定设备
    public static String MAC_BIND = BASEAPI + "device/bind";
    public static String MAC_UNBIND = BASEAPI + "device/unbind";
    public static String CODE_URL = BASEAPI + "obd/dtc";
    public static String MAC_DEVIDE_LISTS = BASEAPI + "device/lists";


    /**
     * 发现
     */

    public static String MAC_FIND = BASEAPI + "index/discovery";
    /**
     * 提交反馈
     */
    public static String MAC_FANKUI = BASEAPI + "user/feedback";

    /**
     * 用户头像上传
     */
    public static String MAC_IMG_UPLOAD = BASEAPI + "user/uploadavatar";
    /**
     * 车品牌
     */
    public static String CAR_BAND = BASEAPI + "utils/carbrand";
    /**
     * 车系
     */
    public static String CAR_SERES = BASEAPI + "utils/carseries";

    /*
    车型
     */
    public static String CAR_MODEL = BASEAPI + "utils/carmodels";


    /**
     * 上传服务器车型
     */
    public static String CAR_UPLOAD_SEVICE = BASEAPI + "device/info";

    /**
     * 地域服务器
     */

    public static String MAC_ADDRESS_AREA = BASEAPI + "utils/area";

//修改用户信息
    public static String UPDATE_USER_INFO = BASEAPI +"user/profile";


    //或得用户信息
    public static   String GET_ALL_USER_INFO=BASEAPI+"user/info";



}
