package com.example.redis_springboot.contrller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping("testRedis")
public class RedisTestController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    @RequestMapping("t1")
    public String testRedis(){
        redisTemplate.opsForValue().set("k1","v1");
        String k1 =(String) redisTemplate.opsForValue().get("k1");
        return k1;
    }

    @GetMapping
    @RequestMapping("hashput")
    public Map<String, String> hashput() throws ParseException {
        Map map=new HashMap<>();

        if(!redisTemplate.opsForHash().putIfAbsent("map2", "k4", "1")){
            //redis中有缓存

            //将未读数+1
            redisTemplate.opsForHash().increment("map2","k4",1);

            //判断缓存中的信息是否是最新的
            SimpleDateFormat formatFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date lastCreateTime = formatFormat.parse(redisTemplate.opsForHash().get("map2", "k2").toString());
            Date createTime = new Date();
            if(!lastCreateTime.after(createTime)){
                map.put("k1", "v1");
                map.put("k2", formatFormat.format(createTime));
                map.put("k3", "v3");
            }
        }else {
            //redis中没有缓存
            map.put("k1", "value1");
            map.put("k2", "2022.07.04 19:55:40");
            map.put("k3", "value3");
        }
        redisTemplate.opsForHash().putAll("map2", map);


        Map map2 = redisTemplate.opsForHash().entries("map2");


        return map2;
    }
}
