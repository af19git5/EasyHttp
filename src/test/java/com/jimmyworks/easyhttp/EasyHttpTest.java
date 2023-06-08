package com.jimmyworks.easyhttp;

import com.jimmyworks.easyhttp.exception.HttpException;
import com.jimmyworks.easyhttp.listener.StringResponseListener;

import lombok.NonNull;

import okhttp3.Headers;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * EasyHttp單元測試
 *
 * @author Jimmy Kang
 */
public class EasyHttpTest {

    @Test
    public void getTest() {
        System.out.println("執行GET測試");
        String url = "https://google.com";
        try {
            EasyHttp.get(url).build().getAsString();
            System.out.println("傳輸成功");
        } catch (HttpException e) {
            System.out.println("傳輸失敗，錯誤訊息: " + e.getMessage());
        }
        System.out.println("執行GET測試完成");
    }

    @Test
    public void getTestOnNewThread() throws InterruptedException {
        System.out.println("執行GET(非同步)測試");
        String url = "https://www.codeloop.com.tw/ems-organ/emso0205/01";
        CountDownLatch latch = new CountDownLatch(1);
        EasyHttp.get(url)
                .build()
                .getAsString(
                        new StringResponseListener() {
                            @Override
                            public void onSuccess(@NonNull Headers headers, String body) {
                                System.out.println("傳輸成功");
                                latch.countDown();
                            }

                            @Override
                            public void onError(@NonNull HttpException e) {
                                System.out.println("傳輸失敗，錯誤訊息: " + e.getMessage());
                                latch.countDown();
                            }
                        });
        latch.await();
        System.out.println("執行GET(非同步)測試完成");
    }
}
