package com.example.redis_springboot.git_test;

import io.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

public class GitTest {
    public void todo() {
        Msg msg = new Msg();
        msg.setAge("9");
        msg.setName("hahh");
        msg.setBorthDate("2022");

        if (StringUtils.isEmpty(msg.getAge())) {
            return;
        }

        if (StringUtils.isEmpty(msg.getName())) {
            return;
        }

        if (StringUtils.isEmpty(msg.getBorthDate())) {
            return;
        } else {
            if (msg.getBorthDate().equals("2022")){
                System.out.println("生日快乐");
            } else {
                System.out.println("谢谢惠顾");
            }
        }
    }
}
