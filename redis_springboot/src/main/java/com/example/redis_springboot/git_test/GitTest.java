package com.example.redis_springboot.git_test;

import io.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

public class GitTest {
    public void todo() {
        Msg msg = new Msg();
        msg.setAge("9");
        msg.setName("hahh");
        msg.setBirthDate("2022");

        if (StringUtils.isEmpty(msg.getAge())) {
            return;
        }

        if (StringUtils.isEmpty(msg.getName())) {
            return;
        }

        if (StringUtils.isEmpty(msg.getBirthDate())) {
            return;
        } else {
            check(msg.getBirthDate());
        }
    }

    private void check(String borthDate) {
        if (borthDate.equals("2022")){
            System.out.println("生日快乐");
        } else {
            System.out.println("谢谢惠顾");
        }
        System.out.println("hahah");
    }
}
