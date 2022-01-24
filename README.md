#### 验证码输入框 

* 支持长按连续删除

![图片](/screenhot/photo1.jpg)

##### 1、添加 Gradle 依赖

root build.gradle 的 repositories 中添加 

```
maven { url 'https://www.jitpack.io' }
```

app build.gradle 的 dependencies 中添加

```
dependencies {
    implementation 'com.github.AnJiuZhe:VerificaionCodeView:1.0.1'
}
```

##### 2、使用
xml
```
<com.zx.library.VerificationCodeView
        android:id="@+id/code_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:verify_code_height="40dp"
        app:verify_code_margin="10dp"
        app:verify_code_number="6"
        app:verify_code_width="40dp" />
```
设置内容

```
codeView.setText("123456")
```
监听文本变化
```
codeView.setOnCodeChangeListener(object : OnCodeChangeListener {
    override fun onCodeChange(text: String?) {
           
    }
})
```

##### 3、属性说明

| attr 属性          | description 描述 |
|:---				 |:---|
| verify_code_number  	     | 验证码数量 |
| verify_text_size  	     |验证码文本大小 |
| verify_code_width	 	 | 验证码边框宽度 |
| verify_code_height	 	 | 验证码边框高度 |
| verify_code_margin	 	 | 验证码边框间距 |
| verify_code_color	 	 | 验证码边框颜色 |
| verify_code_corner	 	 | 验证码边框圆角 |
| verify_code_stroke_width	 	 | 验证码边框线条宽度 |
| verify_code_stroke_color	 	 | 验证码边框线条颜色 |
| verify_cursor_width	 	 | 指示器宽度 |
| verify_cursor_height	 	 | 指示器高度 |
| verify_cursor_color	 	 | 指示器高度颜色 |
| verify_cursor_corner	 	 | 指示器圆角 |

License
--
    Copyright (C) 2022 qs.157@qq.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
