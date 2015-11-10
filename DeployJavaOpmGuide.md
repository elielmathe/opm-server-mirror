# 在GAE上架设 #

**注意：在某些地区或运营商可能已经无法访问GAE了，电脑能访问手机未必能访问，反之亦然，自己看着办。**

## 准备工作 ##

过程并不复杂，你需要三个软件和一个gmail帐号
  * Java虚拟机，Windows用户可到Java[官方网站](http://www.java.com/zh_CN/)下载并安装，Linux用户可搜索发行版的软件仓库。
  * GAE 的开发包，可到Google Code的[官方网站](http://code.google.com/intl/zh-CN/appengine/downloads.html#Google_App_Engine_SDK_for_Java)下载zip文件。
  * Java版的Opm代理服务端，可到opm-server-mirror项目的[下载页](http://code.google.com/p/opm-server-mirror/downloads/list)获取。
  * 一个GAE application id，如果你没有，可到[App Engine首页](http://appengine.google.com/)免费注册一个，记下这个id名称。

## 上传程序 ##

准备好上述资源后，按下面顺序操作。**注意：如果你的GAE application上传过Python程序，上传Java版会冲掉原先的程序，你可以再注册一个新的来上传。**
  1. 解压GAE 的开发包和Java版的Opm代理服务端，并把后者的文件夹（如“Opm\_java”复制到前者的目录下，即“appengine-java-sdk-1.2.6/Opm\_java/”
  1. 用你喜爱的文本编辑器（记事本也行）修改“Opm\_java/war/WEB-INF/appengine-web.xml”文件，找到一行
```
<application>application-id</application>
```
> > 把“application-id”改成你注册到的GAE application id名称，修改好后保存。
  1. 接下来是命令操作了
    * Linux用户，打开终端，进入GAE 的开发包的目录，运行下面代码
```
./bin/appcfg.sh update Opm_java/war
```
    * Windows则用户，打开“命令提示符”，开始 -> 运行 ->输入cmd回车，进入GAE 的开发包的目录，如
```
C:\Documents and Settings\your_name> D:
D:\> cd D:\appengine-java-sdk-1.2.6
D:\appengine-java-sdk-1.2.6> bin\appcfg.cmd update Opm_java\war
```
  1. 脚本会提示输入你的Email地址，输入你的gmail地址，然后会提示输入密码。**注意：输入密码时不会显示星号的，直接输入去就是。**
  1. 等待上传完成，很快的，才几十K的文件，当出现下面一行表示成功了。
```
Update completed successfully.
```
  1. 上传好后测试一下，浏览
```
http://application-id.appspot.com/opm
http://application-id.appspot.com/ucweb
```
> > 如果跳到Google首页，表示成功了。
  1. 接下来修改你的Opera Mini或UCWeb里面的服务器地址为上面的地址吧，不同版本的教程可以从[Wiki页](http://code.google.com/p/opm-server-mirror/w/list)获得。
  1. 最后就进行你喜爱的体育运动。

# 在其它Java EE容器上架设 #

> 目前只测试过Tomcat
  * Tomcat，把“Opm\_java”里的“war”文件夹改个名字如“Opm”，然后扔到Tomcat的“webapps”目录下，重启Tomcat，浏览“Opm/opm”就行了。

# 高级设定 #

## 修改代理网页的地址 ##
如果你不想用“opm”名称作为地址，想改成其它名称，如“opm.jsp”之类，可以修改“Opm\_java/war/WEB-INF/web.xml”文件，找到下面一行
```
<url-pattern>/opm</url-pattern>
```
把“opm”改成你想要的名称，如“opm.jsp”，修改后保存，然后重新上传。

## 编译源代码 ##
源代码文件放在“src”目录，里面就一个“OpmServlet.java”和“geronimo-servlet\_2.5\_spec-1.2.jar”文件，修改源代码后，可用下面命令来编译
```
javac -d classpath geronimo-servlet_2.5_spec-1.2.jar -d ../war/WEB-INF/classes/ OpmServlet.java
```
编译和好后也需要重新上传。