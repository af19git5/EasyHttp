# Easy Http Library
<img src="images/banner.png" />

[繁體中文文檔](README_ZH.md)

## About Easy Http Library

* Made on [OkHttp](http://square.github.io/okhttp/).
* Easy to do http request, just make request and listen for the response.

## Using Library In Your Project

Add this in your pom.xml dependencies.

```xml
<dependency>
  <groupId>io.github.af19git5</groupId>
  <artifactId>easy-http</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Do Request

### GET Request

**Example:**

```java
// Do sync
String response = EasyHttp.get(url).build().getAsString();

// Do async
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

**Value Settings:**

* `.tag` - Add tag for request, can use in cancel request.
* `.addUrlParams` - Add url query param.
* `.urlParams` - Add url query params.
* `.addHeader` - Add header for request.
* `.headers` - Add headers for request.
* `.connectTimeout` - Connect timeout time.
* `.readTimeout` - Read timeout time.
* `.writeTimeout` - Write timeout time.

### Post Request

**Example:**

```java
// Do sync
String response = EasyHttp.post(url).jsonBody(obj).build().getAsString();

// Do async
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

**Value Settings:**

Have the value settings in the get example.

* `.stringBody` - Set string request body, also can set your custom content type.
* `.jsonBody` - Set json object request body, also can set your custom content type.
* `.formBody` - Set form request body.
* `.requestBody` - Set your custom okhttp request body.

### Upload File

**Example:**

```java
// Do sync
String response =
	EasyHttp.upload(url)
    .addMultipartParam("text", "text")
    .addMultipartFile("file", file)
    .build()
    .getAsString();

// Do async
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

**Value Settings:**

Have the value settings in the get example.

* `.contentType` - Set upload request content type. Default value is "multipart/form-data".
* `.addMultipartParam` - Add multipart parameter.
* `.addMultipartFile` - Add multipart parameter. You can put `File`, `ByteArray`,  `Uri`.

### Cancel Request

**Example:**

```java
EasyHttp.cancel(tag);
```

### Custom OkHttpClient Builder

You can easily get OkHttpClient Builder and modify it.

**Example:**

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

## Response Listener

### Get response as String

**Example:**

```java
// Do sync
String response = EasyHttp.get(url).build().getAsString();

// Do async
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

### Get json response as custom object

**Example:**

```java
// Use class
// Do sync
CustomObj response = EasyHttp.get(url).build().getJsonAsObject(CustomObj.class);

// Do async
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
// Do sync
CustomObj response = EasyHttp.get(url).build().getJsonAsObject(new TypeToken<CustomObj>() {});

// Do async
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

### Download response

**Example:**

```java
// Do sync
EasyHttp.get(url).build().download(file);

// Do async
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

