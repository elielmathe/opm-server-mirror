package omsc;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final int FRAME_WIDTH = 460;
	private static final int FRAME_HEIGHT = 400;
	private static final String VERSION = "v0.3.1";
	private static final String FRAME_TITLE = "Opera Mini 服务器地址修改器 " + VERSION;

	public static final String[] SERVER_LINK = { "中转服务器项目",
			"http://code.google.com/p/opm-server-mirror/" };
	public static final String[] CLIENT_LINK = { "OperaMini国际版",
			"http://www.opera.com/mobile/download/versions/" };
	public static final String[] EMULATOR_LINK = { "Java ME 模拟器",
			"http://code.google.com/p/microemu/downloads/list" };

	private JPanel contentPanel;
	private JButton openFilePathButton, saveFilePathButton, newServerUrlButton,
			aboutButton, exitButton, convertButton;
	private JTextField openFilePathTextField, saveFilePathTextField,
			newServerUrlTextField;
	private LinkLabel serverLinkLabel, clientLinkLabel, emulatorLinkLabel;
	private JTextArea messageTextArea;

	private LinkLabelMouseAdapter linkLabelMouseAdapter;
	private ButtonMouseAdapter buttonMouseAdapter;

	private JFileChooser openFilePathChooser;
	private OpenFileFilter openFileFilter;

	private enum MessageType {
		NORMAL, INFO, TIPS, WARRING, ERROR
	};

	private OperaMini operaMini;

	public MainFrame() {
		initComponents();
		setupGUI();
		setUpEventListener();
		printMessage(MessageType.TIPS, "仅支持修改官方国际版原版。");
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		MainFrame omsc = new MainFrame();
		omsc.setVisible(true);
	}

	private void initComponents() {
		// interactive components
		openFilePathButton = new JButton("浏览");
		saveFilePathButton = new JButton("浏览");
		newServerUrlButton = new JButton("测试");
		aboutButton = new JButton("关于");
		exitButton = new JButton("退出");
		convertButton = new JButton("修改");
		openFilePathTextField = new JTextField();
		saveFilePathTextField = new JTextField();
		newServerUrlTextField = new JTextField();
		serverLinkLabel = new LinkLabel(SERVER_LINK);
		clientLinkLabel = new LinkLabel(CLIENT_LINK);
		emulatorLinkLabel = new LinkLabel(EMULATOR_LINK);
		messageTextArea = new JTextArea();
		linkLabelMouseAdapter = new LinkLabelMouseAdapter();
		buttonMouseAdapter = new ButtonMouseAdapter();
		openFilePathChooser = new JFileChooser();
		openFileFilter = new OpenFileFilter();
	}

	private void setupGUI() {
		setupMainWindows();
		setupContentPanel();
		this.getContentPane().add(contentPanel);
	}

	private void setupMainWindows() {
		setTitle(FRAME_TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);

		Image icon = getToolkit().getImage(
				this.getClass().getClassLoader().getResource("res/opera.png"));
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(icon, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setIconImage(icon);
	}

	private void setupContentPanel() {

		GridBagConstraints constraintLabel = new GridBagConstraints();
		constraintLabel.anchor = GridBagConstraints.WEST;

		GridBagConstraints constraintFill = new GridBagConstraints();
		constraintFill.fill = GridBagConstraints.HORIZONTAL;
		constraintFill.weightx = 1.0;
		constraintFill.insets = new Insets(0, 6, 0, 6);

		GridBagConstraints constraintEnd = new GridBagConstraints();
		constraintEnd.gridwidth = GridBagConstraints.REMAINDER;

		GridBagConstraints constraintFillEnd = new GridBagConstraints();
		constraintFillEnd.fill = GridBagConstraints.HORIZONTAL;
		constraintFillEnd.weightx = 1.0;
		constraintFillEnd.gridwidth = GridBagConstraints.REMAINDER;

		// content panel
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		contentPanel.setLayout(new BorderLayout(0, 6));

		// north panel
		JPanel northPanel = new JPanel();
		northPanel.setBorder(new CompoundBorder(new TitledBorder("选项"),
				new EmptyBorder(0, 6, 6, 6)));
		northPanel.setLayout(new GridBagLayout());

		northPanel.add(new JLabel("打开文件:"), constraintLabel);
		northPanel.add(openFilePathTextField, constraintFill);
		northPanel.add(openFilePathButton, constraintEnd);

		northPanel.add(new JLabel("保存文件:"), constraintLabel);
		northPanel.add(saveFilePathTextField, constraintFill);
		northPanel.add(saveFilePathButton, constraintEnd);

		northPanel.add(new JLabel("代理网址:"), constraintLabel);
		northPanel.add(newServerUrlTextField, constraintFill);
		northPanel.add(newServerUrlButton, constraintEnd);

		// center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(new CompoundBorder(new TitledBorder("信息"),
				new EmptyBorder(0, 6, 6, 6)));
		centerPanel.setLayout(new BorderLayout(0, 6));

		// links panel
		JPanel linksPanel = new JPanel();
		FlowLayout linksPanelLayout = new FlowLayout();
		linksPanelLayout.setAlignment(FlowLayout.LEFT);
		linksPanel.setLayout(linksPanelLayout);

		JPanel serverLinksPanel = new JPanel();
		serverLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		serverLinksPanel.add(serverLinkLabel);

		JPanel clientLinksPanel = new JPanel();
		clientLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		clientLinksPanel.add(clientLinkLabel);

		JPanel emulatorLinksPanel = new JPanel();
		emulatorLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		emulatorLinksPanel.add(emulatorLinkLabel);

		linksPanel.add(new JLabel("相关链接："));
		linksPanel.add(serverLinksPanel);
		linksPanel.add(clientLinksPanel);
		linksPanel.add(emulatorLinksPanel);

		JScrollPane messagePanel = new JScrollPane();
		messageTextArea.setEditable(false);
		messageTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		messagePanel.setViewportView(messageTextArea);

		centerPanel.add(linksPanel, BorderLayout.NORTH);
		centerPanel.add(messagePanel, BorderLayout.CENTER);

		// south panel
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		southPanel.add(convertButton, constraintEnd);
		southPanel.add(aboutButton, constraintEnd);
		southPanel.add(exitButton, constraintEnd);

		// put all together
		contentPanel.add(northPanel, BorderLayout.NORTH);
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		contentPanel.add(southPanel, BorderLayout.SOUTH);
	}

	private void selectSourceJarFile() {
		openFilePathChooser.setFileFilter(openFileFilter);
		int option = openFilePathChooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			String path = openFilePathChooser.getSelectedFile().getPath();
			openFilePathTextField.setText(path);
			// add "-mod" to filename
			String filename = path.substring(0, path.length() - 4) + "-mod";
			String extension = path.substring(path.length() - 4);
			String autoSavePath = filename + extension;
			saveFilePathTextField.setText(autoSavePath);

			checkOperaMiniInfo();
		}
	}

	private void selectSaveJarFile() {
		openFilePathChooser.setFileFilter(openFileFilter);
		int option = openFilePathChooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			String path = openFilePathChooser.getSelectedFile().getPath();
			if (!path.endsWith(".jar")) {
				path += ".jar";
			}
			saveFilePathTextField.setText(path);
		}
	}

	private void testProxyServer() {
		String urlText = newServerUrlTextField.getText().trim();
		if (urlText.isEmpty()) {
			printMessage(MessageType.ERROR, "未输入代理网址。");
			return;
		}
		if (!urlText.startsWith("http://")) {
			urlText = "http://" + urlText;
			newServerUrlTextField.setText(urlText);
		}
		if (urlText.indexOf("/", "http://".length()) == -1) {
			urlText += "/";
		}

		URL server;
		try {
			server = new URL(urlText + "?test=1");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			printMessage(MessageType.ERROR, "不是一个合法的http地址！");
			return;
		}

		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) server.openConnection();
			connection.setDoOutput(true);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String response_text = reader.readLine();
				boolean isAvaiable = response_text
						.contains("Opera Mini Server");
				if (isAvaiable) {
					printMessage(MessageType.INFO, "代理网址是有效的。");
				} else {
					printMessage(MessageType.WARRING,
							"代理网址有回应，但可能不是一个有效的Opera Mini Server。");
				}
			} else {
				printMessage(MessageType.WARRING, "代理网址是无效的。");
			}
		} catch (IOException e) {
			e.printStackTrace();
			printMessage(MessageType.WARRING, "访问代理网址出现错误。");
		}
	}

	private void doConvert() {
		String saveFilePath = saveFilePathTextField.getText().trim();
		String newServerUrl = newServerUrlTextField.getText().trim();

		if (saveFilePath.isEmpty() || newServerUrl.isEmpty()) {
			printMessage(MessageType.ERROR, "未完整输入全部选项。");
			return;
		}

		try {
			operaMini.doConvert(saveFilePath, newServerUrl);
			printMessage(MessageType.INFO, "转换成功，新的文件创建完毕！");
		} catch (Exception e) {
			e.printStackTrace();
			String message = String.format("转换失败，%s", e.getMessage());
			printMessage(MessageType.ERROR, message);
		}
	}

	private void checkOperaMiniInfo() {
		String openFilePath = openFilePathTextField.getText().trim();

		if (openFilePath.isEmpty()) {
			printMessage(MessageType.ERROR, "未完整输入全部选项。");
			return;
		}

		// select platform from extension
		if (openFilePath.endsWith(".jar")) {
			operaMini = new JavaPlatform(openFilePath);
		}

		if (openFilePath.endsWith(".exe")) {
			operaMini = new WMPlatform(openFilePath);
		}

		// start to check info
		try {
			String message = operaMini.checkInfo();
			printMessage(MessageType.INFO, message);
		} catch (Exception e) {
			e.printStackTrace();
			printMessage(MessageType.ERROR, "所选文件无效。");
		}

	}

	private void printMessage(MessageType type, String text) {
		String header = "";
		switch (type) {
		case TIPS:
			header = "提示：";
			break;
		case INFO:
			header = "信息：";
			break;
		case WARRING:
			header = "警告：";
			break;
		case ERROR:
			header = "错误：";
			break;
		}
		String message = header + text + "\n";
		messageTextArea.append(message);
	}

	private void showAboutDialog() {
		String aboutText;
		aboutText = FRAME_TITLE + "\n" + "基于 GPLv3 许可证 \n" + "作者是 muzuiget";
		JOptionPane.showMessageDialog(null, aboutText, "关于",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private class LinkLabel extends JLabel {

		private String text, url;

		public LinkLabel(String[] anchor) {
			super();
			this.text = anchor[0];
			this.url = anchor[1];
			String html = String.format("<html><a href=\"%s\">%s</a>",
					this.url, this.text);
			this.setText(html);
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		public URI get_uri() throws URISyntaxException {
			return new URI(url);
		}
	}

	private void setUpEventListener() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		openFilePathButton.addMouseListener(buttonMouseAdapter);
		saveFilePathButton.addMouseListener(buttonMouseAdapter);
		newServerUrlButton.addMouseListener(buttonMouseAdapter);
		aboutButton.addMouseListener(buttonMouseAdapter);
		exitButton.addMouseListener(buttonMouseAdapter);
		convertButton.addMouseListener(buttonMouseAdapter);
		serverLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientLinkLabel.addMouseListener(linkLabelMouseAdapter);
		emulatorLinkLabel.addMouseListener(linkLabelMouseAdapter);
	}

	private class OpenFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				String extension = f.getName().toLowerCase();
				return extension.endsWith(".jar") || extension.endsWith(".exe");
			}
		}

		@Override
		public String getDescription() {
			return "jar | exe 文件";
		}

	}

	private class LinkLabelMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			LinkLabel source = (LinkLabel) event.getSource();
			try {
				Desktop.getDesktop().browse(source.get_uri());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private class ButtonMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source == openFilePathButton) {
				selectSourceJarFile();
				return;
			}
			if (source == saveFilePathButton) {
				selectSaveJarFile();
				return;
			}
			if (source == newServerUrlButton) {
				testProxyServer();
				return;
			}
			if (source == convertButton) {
				doConvert();
				return;
			}
			if (source == aboutButton) {
				showAboutDialog();
				return;
			}
			if (source == exitButton) {
				System.exit(0);
				return;
			}
		}
	}
}
