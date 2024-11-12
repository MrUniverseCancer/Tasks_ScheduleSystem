# 如何在Windows中构建可执行exe程序

## 总体

分为两步，先通过IDEA将项目打包成jar，然后通过launch4j将jar打包成exe。

每一次完成后，都需要检验是否能正常运行。

## 1. 通过IDEA打包成jar

参照博客：https://blog.csdn.net/kelekele111/article/details/123047189即可。

这次我尝试使用Maven插件打包，但是反复遭遇失败，我觉得是pom.xml没有配置好，所以还是选择了IDEA自带的打包功能。

## 2. 通过launch4j打包成exe

在Launch4j的配置界面中，设置输出EXE文件的路径。

指定JAR文件的路径。

在“JRE”选项卡中，~~设置最小和最大JRE版本~~。由于想要打包JRE，因此这里可以设置为之后存放JRE的文件夹。的相对路径

配置其他选项，如应用程序的图标、Manifest文件等（这些选项是可选的，但可以根据你的需求进行配置）。

## 3. 验证

双击exe文件，看是否能正常运行。

将包含exe文件和JRE文件夹的文件夹复制到其他地方(我在虚拟机测试)，看是否能正常运行。



TODO：使用jlink优化JRE文件夹的大小，减少exe文件的大小。
