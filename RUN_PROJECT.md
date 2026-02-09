# 运行项目指南

我们已经成功配置了项目环境，以下是已完成的步骤和如何运行项目的说明：

## 已完成的配置

1. 安装了Java 17
2. 配置了Gradle 8.4
3. 安装了Android SDK及必要组件（platform-tools, build-tools, cmake, ndk等）
4. 添加了2GB交换空间以支持构建过程
5. 修复了build.gradle.kts中的Compose依赖问题

## 环境变量

运行项目前，请确保设置了以下环境变量：

```bash
export ANDROID_HOME=/opt/android-sdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.17.0.10-1.0.2.1.al8.x86_64
export GRADLE_HOME=/opt/gradle/gradle-8.4
export GRADLE_OPTS="-Xmx1g -XX:MaxMetaspaceSize=512m -Dorg.gradle.daemon=false -Dorg.gradle.workers.max=1 -Dkotlin.compiler.execution.strategy=in-process"
export PATH=$JAVA_HOME/bin:$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$GRADLE_HOME/bin
```

## 构建项目

由于这是一个Kotlin Multiplatform项目，构建过程比较复杂。在当前环境中，您可以：

### 检查项目配置
```bash
cd /root/PROJ && gradle :app:help
```

### 尝试构建Android部分
```bash
cd /root/PROJ && gradle :app:build
```

注意：由于这是一个多平台项目（包含Android、iOS、Desktop），在Linux环境下只能构建Android和Desktop部分。iOS部分需要在macOS上构建。

## 项目结构

- `app/src/androidMain/` - Android特定代码
- `app/src/desktopMain/` - Desktop特定代码  
- `app/src/iosMain/` - iOS特定代码
- `app/src/commonMain/` - 跨平台共享代码
- `build.gradle.kts` - 项目构建配置

## 注意事项

1. 项目使用Kotlin Multiplatform技术，支持多平台部署
2. 使用Jetpack Compose/JetBrains Compose for Desktop进行UI开发
3. 使用Koin进行依赖注入
4. 使用Voyager进行导航管理
5. 由于资源限制，完整构建可能需要较长时间和较大内存

## 故障排除

如果遇到内存不足错误，请确保交换空间已启用：
```bash
sudo swapon --show
```

如果Gradle daemon崩溃，请确保使用-Dorg.gradle.daemon=false标志。