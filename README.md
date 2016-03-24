# AndroidActivityHelper
Activity管理帮助工具

## Import
[JitPack](https://jitpack.io/)

Add it in your project's build.gradle at the end of repositories:

```gradle
repositories {
  // ...
  maven { url "https://jitpack.io" }
}
```

Step 2. Add the dependency in the form

```gradle
dependencies {
  compile 'com.github.vilyever:AndroidActivityHelper:1.0.0'
}
```

## Usage
```java

// 初始化
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityHelper.initialize(this);
    }
}

// 获取栈顶的Activity
ActivityHelper.findTopActivity();

// finish所有的Activity
ActivityHelper.finishAllActivities();

```

## License
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)