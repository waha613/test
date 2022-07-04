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

        SimpleDateFormat formatFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Object lastCreateTime = redisTemplate.opsForHash().get("map2", "k2");
        Date createTime = new Date();

        if(lastCreateTime == null){
            //redis中没有缓存
            map.put("k1", "value1");
            map.put("k2", formatFormat.format(createTime));
            map.put("k3", "value3");
            map.put("k4", "1");

        }else if (formatFormat.parse(lastCreateTime.toString()).before(createTime)){
            //redis中有缓存 且 缓存的消息是最新消息

            redisTemplate.opsForHash().increment("map2","k4",1);
        }else {
            //redis中有缓存 且 缓存的消息不是最新消息

            map.put("k1", "v1");
            map.put("k2", formatFormat.format(createTime));
            map.put("k3", "v3");
            redisTemplate.opsForHash().increment("map2","k4",1);
        }
        redisTemplate.opsForHash().putAll("map2", map);


        Map map2 = redisTemplate.opsForHash().entries("map2");

        return map2;
    }
}
