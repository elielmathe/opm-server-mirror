package omsc;

public class OperaMini {

	public static final OperaMini international, internationalNext;
	public static final OperaMini china, chinaNext, chinaLab;
	public static final OperaMini[] operaMiniItems;
	public static final String SERVER_KEY = "c1dd7ab77e2c967746fe10681026c920f864811321bcb8be6bbfa5a03fda4e16c9c8db3af280f7703366e778e93c55e7159a8852d2b1381e521a337f22b1406cddf41a3114aecb4f4bfe79e0c5aa2ba8824fc989cb8bdcbf8ec5cef5176bfd4059f229b91bfa025126b295f9c409e75f6f6415ee094fd7f5dfd395a1f431668c5a08e88de891dc4dd38d4e9aa9b9c00dc604a0428e3aa5a28ccfa75af099147b";
	public static final String CHINA_SERVER_KEY = "8c60d2a6811f85366af231ae416831b09409b614e9cfa8fde8d8577e892636e0e0b7a151f9601b930bf527ea8a22bfe6fb5f72506bd3e81b3b55d189af17e35b2d7ea61d84ba4e62cf1c01789edb2c3f3c00fc3c09ee1fc9627367294727e52af4c990516d8d7aad4e00d6ab50cd8ca63705df0af243e666dad282d6514b656780e108d591cf78920f7bdee21ed1419a080655ca2acdadc4e64dba01b5accf73";

	public String name, version, downloadLink, classFile, httpServer,
			socketServer;
	public boolean changerKey;

	public OperaMini(String name, String version, String downloadLink,
			String classFile, String httpServer, String socketServer,
			boolean changerKey) {
		this.name = name;
		this.version = version;
		this.downloadLink = downloadLink;
		this.classFile = classFile;
		this.httpServer = httpServer;
		this.socketServer = socketServer;
		this.changerKey = changerKey;
	}

	public String[] getLabelText() {
		String[] text = { version + name, downloadLink };
		return text;
	}

	public String toString() {
		return String.format("Opera Mini %s %s", version, name);
	}

	static {
		international = new OperaMini(
				"国际版",
				"4.2",
				"http://mini.opera.com/download-4/opera-mini-latest-advanced-zh.jad?no_redir&ismobile=false",
				"a.class", "http://server4.operamini.com:80/",
				"socket://server4.operamini.com:1080", false);
		internationalNext = new OperaMini(
				"国际版",
				"5.0",
				"http://m.opera.com/download-5/opera-mini-latest-advanced-en.jar?no_redir&ismobile=false",
				"o.class", "http://mini5beta.opera-mini.net:80/",
				"socket://mini5beta.opera-mini.net:1080", false);
		china = new OperaMini("中国版", "4.2",
				"http://www.operachina.com/mini/download/", "g.class",
				"http://59.151.106.229:80/", "socket://59.151.106.229:1080",
				true);
		chinaNext = new OperaMini("中国版", "5.0",
				"http://bbs.operachina.com/viewtopic.php?t=59675", "o.class",
				"http://china-4.opera-mini.net:80/",
				"socket://china-4.opera-mini.net:1080", true);
		chinaLab = new OperaMini("实验室版", "4.2",
				"http://www.operachina.com/id/", "I.class",
				"http://china-4.opera-mini.net:80/",
				"socket://china-4.opera-mini.net:1080", true);

		operaMiniItems = new OperaMini[5];
		operaMiniItems[0] = international;
		operaMiniItems[1] = internationalNext;
		operaMiniItems[2] = china;
		operaMiniItems[3] = chinaNext;
		operaMiniItems[4] = chinaLab;
	}

}
