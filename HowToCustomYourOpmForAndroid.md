## 修改步骤 ##
  1. 先此处下载并安装JRE：http://www.java.com/getjava/ 。如果你已经安装了JRE或者JDK，请跳过此步。
  1. 在此处下载smali： http://smali.googlecode.com/files/smali.jar
  1. 在此处下载反编译过的的Android版OPM源代码： http://opm-server-mirror.googlecode.com/files/android_opera_classes.zip
  1. 将OPM的源代码解压到某个目录，如 D:\sources
  1. 打开D:\classes\Code.smali，将 http://server4.operamini.com:80 改成你自己架设的OPM服务器，如 http://www.example.com/opera/index.php 。~~请注意：不要修改socket的设定，否则将无法使用。~~此处可将 socket://server4.operamini.com:1080 一并修改，这样在第一次运行的时候也不会提示下载中国版了。OPM5 的话把  http://mini5.opera-mini.net:80 和 http://mini5cn.opera-mini.net:80 修改为自己的OPM服务器地址 末尾不用带index.php 如http://www.example.com/opera/  .
  1. 打开命令行，进入smali的保存目录，执行：
> > java -jar smali.jar -o D:\classes.dex D:\source
  1. 到此处下载Android版OPM的安装包： http://opm-server-mirror.googlecode.com/files/opm4.apk 。如果你知道怎么获取APK包也可直接使用原始的APK文件。
  1. 将opm4.apk改名为opm4.zip，用任意压缩工具打开。删除里面的META-INF文件夹。并把D:\classes.dex拖进opm4.zip替换掉原始的classes.dex。
  1. 在此处下载签名工具： http://opm-server-mirror.googlecode.com/files/Auto-Sign.zip ，并解压到任意目录。
  1. 把修改过的opm4.zip放入Auto-Sign的目录，运行里面的sign.bat。
  1. 如果你在Auto-Sign的目录里面看见your\_app\_signed.apk的话，恭喜你，你已经成功了！请在安装之前卸载先前的版本。
  1. ~~进行软件后第一次仍会提示需要下载“中国版”，请先Accept后返回主界面，并在Settings中将Protocol改成HTTP后继续使用。~~