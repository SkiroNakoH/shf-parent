package com.atguigu.util;

import java.util.Random;

public class NumUtil {
    public static String getFourNum(Integer cyclic) {

        StringBuilder code = new StringBuilder();
       Random rand = new Random();
        for (int i = 0; i < cyclic; i++) {
            int num;
            while (true) {
                num = rand.nextInt(123);
                //数字[48,57]  大写字母[65,90]  小写字母[97,122]
                if (num >= 48 && num <= 57) {
                    break;
                } else if (num >= 65 && num <= 90) {
                    break;
                } else if (num >= 97 && num <= 122) {
                    break;
                }
            }
            code.append((char) num);
        }
        return code.toString();
    }
}
