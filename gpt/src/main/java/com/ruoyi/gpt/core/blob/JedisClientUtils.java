package com.ruoyi.gpt.core.blob;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;

public class JedisClientUtils {

    public static Jedis getJedisClient(String host, String port, String pwd, String db) {
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        jedis.auth(pwd);
        jedis.select(Integer.parseInt(db));
        return jedis;
    }

    public static void scanKeysAndDelete(String pattern, Jedis jedis) {
        ScanParams scanParams = new ScanParams();
        scanParams.match(pattern);
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            if (scanResult.getResult().size() > 0) {
                for (String key : scanResult.getResult()) {
                    jedis.del(key);
                }
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals(ScanParams.SCAN_POINTER_START));
    }

}
