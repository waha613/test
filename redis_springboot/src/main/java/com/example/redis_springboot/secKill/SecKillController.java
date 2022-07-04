package com.example.redis_springboot.secKill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("secKill")
public class SecKillController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public String secKill(SecKill secKill) {
        String uid = String.valueOf(new Random().nextInt(50000));
        String pid = secKill.getPid();
        return version2(uid, pid);
    }

    private String version1(String uid, String pid) {
        String productRemainQty = "";

        productRemainQty = (String) redisTemplate.opsForValue().get("pid");
        if (redisTemplate.opsForSet().isMember("secKillSuccess", pid + ":" + uid)) {
            System.out.println(uid + "已参加过秒杀活动，请勿重复参加");
            return uid + "已参加过秒杀活动，请勿重复参加";
        }
        if (productRemainQty == null) {
            System.out.println(uid + "秒杀还未开始");
            return uid + "秒杀还未开始";
        } else if (Integer.parseInt(productRemainQty) <= 0) {
            System.out.println("秒杀已结束，商品已被抢光");
            return uid + "秒杀已结束，商品已被抢光";
        }

        redisTemplate.opsForValue().decrement(pid);

        redisTemplate.opsForSet().add("secKillSuccess", pid + ":" + uid);
        System.out.println(uid + "恭喜您抢到商品");
        return uid + "恭喜您抢到商品";
    }

    private String version2(String uid, String pid) {

        //使用SessionCallback 开启事务，因为事务需要在一个连接内进行，使用SessionCallback保证了redis操作都在一个连接内，
        //若直接使用redisTemplate下的watch,multi,exec，则会报异常，因为redisTemplate.multi()操作，会中断该连接，并开启一个新的连接
        //导致exec的时候，新连接时没有开启事务的，所以报异常
        List execute = (List) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                redisTemplate.watch(pid);
                String  productRemainQty = (String) redisTemplate.opsForValue().get(pid);
                if (redisTemplate.opsForSet().isMember("secKillSuccess", pid + ":" + uid)) {
                    System.out.println(uid + "已参加过秒杀活动，请勿重复参加");
                    return null;
                }
                if (productRemainQty == null) {
                    System.out.println(uid + "秒杀还未开始");
                    return null;
                } else if (Integer.parseInt(productRemainQty) <= 0) {
                    System.out.println("秒杀已结束，商品已被抢光");
                    return null;
                }
                redisTemplate.multi();
                redisTemplate.opsForValue().decrement(pid);
                redisTemplate.opsForSet().add("secKillSuccess", pid + ":" + uid);
                List exec = redisTemplate.exec();
                return exec;
            }
        });
        System.out.println("execute:" + execute);
        if (execute == null || execute.size() == 0) {
            System.out.println(uid + "很遗憾，未抢到");
            return uid + "很遗憾，未抢到";
        }

        System.out.println(uid + "恭喜您抢到商品");
        return uid + "恭喜您抢到商品";

    }
}
