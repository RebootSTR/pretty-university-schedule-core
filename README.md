# pretty-university-schedule-core

[![](https://jitpack.io/v/RebootSTR/pretty-university-sheduler-core.svg)](https://jitpack.io/#RebootSTR/pretty-university-sheduler-core)

This is a core for drawing university schedule.

## Instalation

+ Gradle
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
  
 dependencies {
	 implementation 'com.github.RebootSTR:pretty-university-schedule-core:Tag'
 }
```
+ Maven
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.RebootSTR</groupId>
    <artifactId>pretty-university-schedule-core</artifactId>
    <version>Tag</version>
</dependency>
```

## Usage
1. Implement `CoreDrawer` and `AbstractPaint` with your paint paint tool.
2. Build `Schedule` with `SheduleBuilder`.
3. Pass `Schedule` in your implementation `CoreDrawer`.
4. Call `CoreDrawer::drawSchedule`.
5. Call `CoreDrawer::save` with `File` object, where you want save a shedule.

## Ready implemetations to use
Coming soon
