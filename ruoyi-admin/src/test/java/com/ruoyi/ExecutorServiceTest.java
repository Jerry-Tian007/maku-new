package com.ruoyi;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ExecutorServiceTest {
    @Test
    public void test() {
        List<String> userList = new ArrayList<>();
        List<String> deptList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch count = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            int u = i;
            executorService.submit(() -> {
                synchronized (userList) {
                    userList.add("user" + u);
                }
                synchronized (deptList) {
                    deptList.add("dept" + u);
                }
                count.countDown();
            });
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        boolean result = true;
//        while (result) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            if (userList.size() == 50) {
//                result = false;
//            }
//        }
        System.out.println("success");
        System.out.println(userList);
        System.out.println(deptList);
    }
}
