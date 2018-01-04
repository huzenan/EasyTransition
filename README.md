# EasyTransition
A light transition lib(with only 2 files...) for Android, scince shared elements transition is not supported before LOLLIPOP, using EasyTransition can easily make fun transition animation between elements in two activities. Enjoy it!

## ScreenShots
![easytransition](https://github.com/huzenan/EasyTransition/blob/master/screenshots/easy%20transition.gif) 

## Usage
### 1.Add the library.
Add to your root build.gradle:
```xml
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:
```xml
dependencies {
    compile 'com.github.huzenan:EasyTransition:v1.0.0'
}
```

### 2.Using same ids between two Views in two layouts.

```xml
    <!-- Activity A -->
    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:src="@mipmap/avatar_male"
        />
    
    <!-- Activity B -->
    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:src="@mipmap/avatar_male"
        />
```

### 3.In Activity A, make transition options, and start Activity B with EasyTransition#startActivity.

```java
    // ready for transition options
    EasyTransitionOptions options =
        EasyTransitionOptions.makeTransitionOptions(
            ActivityA.this,
            findViewById(R.id.iv_icon),
            findViewById(R.id.tv_name)); // add as many views as you like

    // start transition
    Intent intent = new Intent(ActivityA.this, ActivityB.class);
    EasyTransition.startActivity(intent, options);
```

### 4.In Activity B, enter transition when creating, and exit transition when backing.

```java
    // onCreate
    EasyTransition.enter(ActivityB.this);
    
    // onBackPressed
    EasyTransition.exit(ActivityB.this);
```

## Attributes
Usage above is only the minimum choice, you can set attributes like duration, time interpolator and so on, have fun!
