# 简介 #

OperaMini服务器地址修改器的最新为 0.3.1 版，[下载地址](http://code.google.com/p/opm-server-mirror/downloads/detail?name=omsc.zip)。
  * 仅支持官方国际版原版。
  * 使用Java编写的，可以运行在Linux、Windows、Mac上。
  * 支持Java版和WM版。

https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl-web.dropbox.com%2Fu%2F2992664%2Fwiki%2Fomsc.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*

# 使用 #

运行需要Java虚拟机，可到[Java官网](http://www.java.com/zh_CN/)下载。
  1. 安装后Java虚拟后，运行omsc.exe即可。
  1. 选择打开文件（jar或exe）和保存文件，测试一下代理地址，点击“修改”，搞定。
  1. 复制到手机前你也可以用MicroEmulator来测试一下。

一些注意事项
  * 至于哪里获得代理网址，自己去找或搭建，测试功能只是简单的测试，电脑上能访问在手机上未必能访问。
  * WM版本对代理地址长度有限制，5.1正式版中必须少于36个字符，程序会自动判断的。

# 问题解答 #

  * Q：为什么修改后放到手机上出现“软件已损坏或文件无效”的错误。
> A：大概是把Jar和Jad都放在一起，手机上的Java虚拟机通常先读取Jad的信息来找相应的Jar，因为Jar修改后大小已经改变了，和原来的Jad信息不一致而被Java虚拟机直接判断为文件无效。解决方法是直接删掉Jad文件。

  * Q：为什么我修改后测试不成功，出现这样那样的错误。
> A：国内手机网络环境复杂，不用手机不同Java虚拟机和不同地区可能有不同效果，无法保证完全没问题，所以请大家给多点耐心自己折腾一下，分享下你的修改经验。