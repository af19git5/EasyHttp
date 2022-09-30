package com.jimmyworks.easyhttp;

import com.jimmyworks.easyhttp.exception.HttpException;
import org.junit.Test;

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
}
