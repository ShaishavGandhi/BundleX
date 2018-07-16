# BundleX

![CircleCI branch](https://img.shields.io/circleci/project/github/shaishavgandhi05/BundleX/master.svg)

Generate readable extensions on Bundle to easily bind and set extras.

## Download

![Maven Central](https://img.shields.io/maven-central/v/com.shaishavgandhi/bundlex-compiler.svg)

```groovy
dependencies {
  compileOnly 'com.shaishavgandhi.navigator:navigator-annotations:0.3.1'
  kapt 'com.shaishavgandhi:bundlex-compiler:x.y.z'
}
```

## Use Case

The normal way of getting data from a Bundle is often clunky. 

```kotlin
if (bundle.containsKey("key")) {
  value = bundle.getString("key")
}

```
This requires you to keep track of the key as a static final, check whether data is present, nullability etc. 

## Usage

BundleX generates extensions on the Bundle to take out that boilerplate and uses default values to further sweeten the API. 
Simply annotate your field with `@Extra` which will generate an extension on your `Bundle`.

```kotlin
class MyActivity: AppcompatActivity {
  
  @Extra lateinit var message: String
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
        
    val bundle = intent.extras
    message = bundle.getMessage(defaultValue = "hello world") // Generated extension
  }
  
  fun startActivity(message: String) {
    val bundle = Bundle()
    bundle.putMessage(message) // Use generated setter 
    // Start activity with bundle
  }
    
}
```

Because of the nature of Bundles(values may not be there and there's no way to enforce it), BundleX requires that you provide a `defaultValue` while trying to get a property. If you don't provide a `defaultValue`, you will get a nullable instance of the return type. 

```kotlin
val message: String? = bundle.getMessage()
```

## Advanced Usage

BundleX is a useful add-on to [Navigator](https://github.com/shaishavgandhi05/navigator) which generates all Activity/Fragment bundle bindings for you. That library also provides an easy builder method around Intents. BundleX is built to work seamlessly with Navigator.

## License
    
    Copyright 2018 Shaishav Gandhi.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
