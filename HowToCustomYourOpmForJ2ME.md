## 修改步骤 ##
  1. 先安装JDK或者JRE，安装地址如下： http://www.java.com/getjava
  1. 准备好适合你手机的Java版OPM，下载地址见此（需翻墙）： http://www.opera.com/mini/download/
  1. 在此处下载Java Class File Editor并解压某个文件夹： http://sourceforge.net/projects/classeditor/files/
  1. 运行Java Class File Editor，在命令行下进入解压目录后执行：java -jar ce.jar
  1. 将你的OPM程序（如opera-mini-4.2.14912-advanced-en-us.jar）用常见的压缩工具解压，然后你可以看到一堆的扩展名为.class的文件。
  1. 在ClassEditor中打开这堆.class文件里面最大的一个，以我下载的Generic版为例，打开a.class。
  1. 在ClassEditor中切换到Constant Pool选项卡，在Search的文本框里填入： http://server4.operamini.com:80/
  1. 点击Find/Find Next，直到左边的Value出现 http://server4.operamini.com:80/ 为止（一般为两次）。如果此处没有找到，请回到第六步，按文件大小顺序依次尝试其他的.class文件。
  1. 在ClassEditor的右上角把Modify Mode改为On，然后在Value文本框里填上自建的服务器地址，再点击Modify。
  1. 保存文件，退出ClassEditor。
  1. 用常见的压缩工具打开OPM程序，如不能直接打开可先将.jar的扩展名改成.zip。
  1. 把修改好的.class文件拖拽回文件内替换之前的版本，将.zip改回.jar。
  1. 大功告成！

## 2009.11.26更新 ##
根据网友反应，Java Class File Editor这个软件修改可能有点问题。可以改用hhclass或者Halo等java汉化工具，修改原理同上，下载地址请自行搜索。
我自己并不用J2ME版的OPM，所以只在模拟器上尝试成功过。有网友碰到的授权问题我也不是很清楚。建议大家看看下面的留言，看看是否有所帮助。也欢迎修改成功的网友来和大家交流修改的经验。 :)