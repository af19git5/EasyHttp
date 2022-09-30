# Android Easy Http Library
<img src="images/banner.png" />

## 關於Easy Http Library

* Http連線基底框架為[OkHttp](http://square.github.io/okhttp/)進行開發。
* 簡化請求，只需覆寫回傳監聽。

## 如何使用在自己專案

將以下代碼加入至專案中的pom.xml dependencies.

```xml
<dependency>
  <groupId>io.github.af19git5</groupId>
  <artifactId>easy-http</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 執行請求

### GET Request

**範例:**

```java
// 同步執行
String response = EasyHttp.get(url).build().getAsString();

// 異步執行
EasyHttp.get(url)
  .build()
  .getAsString(new StringResponseListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull String body) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  });
```

**可設定參數:**

* `.tag` - 將請求增加tag，可用來取消請求用。
* `.addUrlParams` - 增加url查詢參數。
* `.urlParams` - 設定url查詢參數。
* `.addHeader` - 增加header。
* `.headers` - 設定headers。
* `.connectTimeout` - 連線超時最大時長。
* `.readTimeout` - 讀取超時最大時長。
* `.writeTimeout` - 寫入超時最大時長。

### Post Request

**範例:**

```java
// 同步執行
String response = EasyHttp.post(url).jsonBody(obj).build().getAsString();

// 異步執行
EasyHttp.post(url)
  .jsonBody(obj)
  .build()
  .getAsString(new StringResponseListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull String body) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  })
```

**可設定參數:**

擁有上面Get request可設定參數，及下列可設定參數。

* `.stringBody` - 設定字串request body，也可客製化選擇想傳輸的content-type。
* `.jsonBody` - 設定json物件request body，也可客製化選擇想傳輸的content-type。
* `.formBody` - 設定form request body。
* `.requestBody` - 設定自己客製化的okhttp request body。

### 上傳檔案

**範例:**

```java
// 同步執行
String response =
	EasyHttp.upload(url)
    .addMultipartParam("text", "text")
    .addMultipartFile("file", file)
    .build()
    .getAsString();

// 異步執行
EasyHttp.upload(url)
  .addMultipartFile("file", file)
  .addMultipartParameter("text", "text")
  .build()
  .getAsString(new StringResponseListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull String body) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  })
```

**可設定參數:**

擁有上面Get request可設定參數，及下列可設定參數。

* `.contentType` - 設定content-type，預設值為 "multipart/form-data"。
* `.addMultipartParam` - 增加multipart參數.
* `.addMultipartFile` - 增加multipart檔案參數，可放置 `File`, `ByteArray`,  `Uri`.

### 取消 Request

**範例:**

```java
EasyHttp.cancel(tag);
```

### 客製化 OkHttpClient Builder

您可以很簡單取得傳輸使用的OkHttpClient Builder，並且客製化為您的需求。

**範例:**

```java
RequestBuilder requestBuilder = EasyHttp.get(url);
OkHttpClient.Builder okHttpClientBuilder = requestBuilder.getOkHttpClientBuilder();

// Do something modify for OkHttpClient Builder...

requestBuilder.build().getAsString(new StringResponseListener() {
  @Override
  public void onSuccess(@NonNull Headers headers, @NonNull String body) {
	// Do something...
  }

  @Override
  public void onError(@NonNull HttpException e) {
	// Do something...
  }
});
```

## Response 監聽器

### 取得字串Response

**範例:**

```java
// 同步執行
String response = EasyHttp.get(url).build().getAsString();

// 異步執行
EasyHttp.get(url)
  .build()
  .getAsString(new StringResponseListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull String body) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  });
```

### 取得Json Response並轉為客製化的物件

**範例:**

```java
// Use class
// 同步執行
CustomObj response = EasyHttp.get(url).build().getJsonAsObject(CustomObj.class);

// 異步執行
EasyHttp.get(url)
  .build()
  .getJsonAsObject(
    CustomObj.class,
    new JsonResponseListener<>() {
      @Override
      public void onSuccess(@NonNull Headers headers, CustomObj body) {
        // Do something...
      }

      @Override
      public void onError(@NonNull HttpException e) {
        // Do something...
      }
  });

// Use Gson TypeToken
// 同步執行
CustomObj response = EasyHttp.get(url).build().getJsonAsObject(new TypeToken<CustomObj>() {});

// 異步執行
EasyHttp.get(url)
  .build()
  .getJsonAsObject(
    new TypeToken<CustomObj>() {},
    new JsonResponseListener<>() {
      @Override
      public void onSuccess(@NonNull Headers headers, CustomObj body) {
        // Do something...
      }

      @Override
      public void onError(@NonNull HttpException e) {
        // Do something...
      }
  });
```

### 下載Response

**範例:**

```java
// 同步執行
EasyHttp.get(url).build().download(file);

// 異步執行
EasyHttp.get(url)
  .build()
  .download(file, new DownloadListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull File file) {
       // Do something...
    }

    @Override
    public void onProgress(long downloadBytes, long totalBytes) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  });
```

## License

```
Copyright 2022 Jimmy Kang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
