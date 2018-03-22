package com.shdnxc.cn.activity.bean;

import java.util.List;

/**
 * Created by Zheng Jungen on 2017/3/21.
 */
public class BreakCodeBean {
    /**
     * status : 200
     * data : [{"dtc":"P0001","flag":"ALL","definition":"燃油量调节器控制电路/开路","meaning":"（在喷油嘴被广泛使用的新型汽车中，燃油量调节器已经很少被使用）"},{"dtc":"P0200","flag":"ALL","definition":"喷油器电路/开路","meaning":"喷油器的作用是将燃油雾化，使其适应燃烧的要求。工作原理是当电磁线圈通电时，产生吸力，针阀被吸起，打开喷孔，燃油经针阀头部的轴针与喷孔之间的环形间隙高速喷出，形成雾状。电子控制单元通过控制喷油器打开时间间隔（也叫脉冲宽度）来控制喷油量。如果电子控制单元（ECU）检测到喷油器控制电路出错，该故障码会出现。"},{"dtc":"P0203","flag":"ALL","definition":"喷油器电路/开路 - 第3缸","meaning":"喷油器的作用是将燃油雾化，使其适应燃烧的要求。工作原理是当电磁线圈通电时，产生吸力，针阀被吸起，打开喷孔，燃油经针阀头部的轴针与喷孔之间的环形间隙高速喷出，形成雾状。电子控制单元通过控制喷油器打开时间间隔（也叫脉冲宽度）来控制喷油量。如果电子控制单元（ECU）检测到喷油器控制电路出错，该故障码会出现。故障原因包括喷油器接口，电路故障，喷油器本身故障，电子控制模块（PCM或ECM）故障等。"},{"dtc":"P0300","flag":"ALL","definition":"随机/多个气缸检测到失火","meaning":"气缸失火是指发动机工作过程中由于各种原因造成的混合气在气缸内不能正常燃烧的现象。如果电子控制单元（ECU）检测到可能会对催化转换器造成破坏的失火，故障指示灯会闪烁，此时应立即关闭发动机。该故障码表明有多个气缸失火或者是电子控制单元（ECU）不能确定哪一个气缸失火。故障原因包括气缸机械故障，燃油计量错误，燃油压力太高或太低，蒸发排放系统故障，EGR阀卡在开，PCV系统真空泄漏，点火系统故障，空气流量计故障，油箱内燃油液位过低。"},{"dtc":"P0302","flag":"ALL","definition":"第2缸检测到失火","meaning":"气缸失火是指发动机工作过程中由于各种原因造成的混合气在气缸内不能正常燃烧的现象。如果电子控制单元（ECU）检测到可能会对催化转换器造成破坏的失火，故障指示灯会闪烁，此时应立即关闭发动机。该故障码表明电子控制单元（ECU）检测到气缸2失火。故障原因包括点火系统故障，燃油输送故障，进气歧管或EGR，PCV的空气泄漏，机械性气缸故障等。"}]
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
         * dtc : P0001
         * flag : ALL
         * definition : 燃油量调节器控制电路/开路
         * meaning : （在喷油嘴被广泛使用的新型汽车中，燃油量调节器已经很少被使用）
         */

        private String dtc;
        private String flag;
        private String definition;
        private String meaning;

        public String getDtc() {
            return dtc;
        }

        public void setDtc(String dtc) {
            this.dtc = dtc;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }
    }
}
