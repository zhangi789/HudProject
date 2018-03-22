package com.shdnxc.cn.activity.scorellpager;

/**
 * Created by 张海洋 on 10.04.2017.
 */
enum Direction {

    START {
        @Override
        public int applyTo(int delta) {
            return delta * -1;
        }
    },
    END {
        @Override
        public int applyTo(int delta) {
            return delta;
        }
    };

    public abstract int applyTo(int delta);

    public static Direction fromDelta(int delta) {
        return delta > 0 ? END : START;
    }
}
