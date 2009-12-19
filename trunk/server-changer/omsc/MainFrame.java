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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class MainFrame extends JFrame {

	private static final int FRAME_WIDTH = 520;
	private static final int FRAME_HEIGHT = 460;
	private static final String FRAME_TITLE = "Opera Mini Server Changer v0.1";

	public static final String[] SERVER_LINK = { "opm-server-mirror",
			"http://code.google.com/p/opm-server-mirror/" };
	public static final String[] EMULATOR_LINK = { "MicroEmulator",
			"http://code.google.com/p/microemu/downloads/list" };
	public static final String[] INTERNATIONAL_JAD = {
			"Jad",
			"http://mini.opera.com/download-4/opera-mini-latest-advanced-zh.jad?no_redir&ismobile=false" };
	public static final String[] INTERNATIONAL_JAR = {
			"Jar",
			"http://mini.opera.com/download-4/opera-mini-latest-advanced-zh.jar?no_redir&ismobile=false" };
	public static final String[] INTERNATIONAL_NEXT_JAD = {
			"Jad",
			"http://m.opera.com/download-5/opera-mini-latest-advanced-en.jad?no_redir&ismobile=false" };
	public static final String[] INTERNATIONAL_NEXT_JAR = {
			"Jar",
			"http://m.opera.com/download-5/opera-mini-latest-advanced-en.jar?no_redir&ismobile=false" };

	private JPanel contentPanel;
	private JButton sourceJarButton, saveJarButton, testServerButton,
			aboutButton, exitButton, convertButton;
	private JTextField sourceJarTextField, saveJarTextField,
			testServerTextField;
	private JComboBox versionComboBox;
	private LinkLabel serverLinkLabel, clientLinkLabel, clientNextLinkLabel,
			clientCHNLinkLabel, clientNextCHNLinkLabel, clientLABLinkLabel,
			emulatorLinkLabel;
	private LinkLabel clientJadLinkLabel, clientJarLinkLabel,
			clientNextJadLinkLabel, clientNextJarLinkLabel;
	private JTextArea messageTextArea;

	private LinkLabelMouseAdapter linkLabelMouseAdapter;
	private ButtonMouseAdapter buttonMouseAdapter;

	private JFileChooser jarFileChooser;

	private enum MessageType {
		NORMAL, INFO, TIPS, WARRING, ERROR
	};

	public MainFrame() {
		initComponents();
		setupGUI();
		setUpEventListener();
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
		sourceJarButton = new JButton("浏览");
		saveJarButton = new JButton("浏览");
		testServerButton = new JButton("测试");
		aboutButton = new JButton("关于");
		exitButton = new JButton("退出");
		convertButton = new JButton("修改");
		sourceJarTextField = new JTextField();
		saveJarTextField = new JTextField();
		testServerTextField = new JTextField();
		versionComboBox = new JComboBox(OperaMini.operaMiniItems);
		serverLinkLabel = new LinkLabel(SERVER_LINK);
		clientLinkLabel = new LinkLabel(OperaMini.international.getLabelText());
		clientJadLinkLabel = new LinkLabel(INTERNATIONAL_JAD);
		clientJarLinkLabel = new LinkLabel(INTERNATIONAL_JAR);
		clientNextLinkLabel = new LinkLabel(OperaMini.internationalNext
				.getLabelText());
		clientNextJadLinkLabel = new LinkLabel(INTERNATIONAL_NEXT_JAD);
		clientNextJarLinkLabel = new LinkLabel(INTERNATIONAL_NEXT_JAR);
		clientCHNLinkLabel = new LinkLabel(OperaMini.china.getLabelText());
		clientNextCHNLinkLabel = new LinkLabel(OperaMini.chinaNext
				.getLabelText());
		clientLABLinkLabel = new LinkLabel(OperaMini.chinaLab.getLabelText());
		emulatorLinkLabel = new LinkLabel(EMULATOR_LINK);
		messageTextArea = new JTextArea();
		linkLabelMouseAdapter = new LinkLabelMouseAdapter();
		buttonMouseAdapter = new ButtonMouseAdapter();
		jarFileChooser = new JFileChooser();
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

		northPanel.add(new JLabel("原版Jar文件:"), constraintLabel);
		northPanel.add(sourceJarTextField, constraintFill);
		northPanel.add(sourceJarButton, constraintEnd);

		northPanel.add(new JLabel("保存Jar文件:"), constraintLabel);
		northPanel.add(saveJarTextField, constraintFill);
		northPanel.add(saveJarButton, constraintEnd);

		northPanel.add(new JLabel("代理网址:"), constraintLabel);
		northPanel.add(testServerTextField, constraintFill);
		northPanel.add(testServerButton, constraintEnd);

		northPanel.add(new JLabel("Jar版本:"), constraintLabel);
		northPanel.add(versionComboBox, constraintFill);
		northPanel.add(convertButton, constraintEnd);

		// center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(new CompoundBorder(new TitledBorder("信息"),
				new EmptyBorder(0, 6, 6, 6)));
		centerPanel.setLayout(new BorderLayout(0, 6));

		// links panel
		JPanel linksPanel = new JPanel();
		linksPanel.setLayout(new GridBagLayout());

		JPanel serverLinksPanel = new JPanel();
		serverLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		serverLinksPanel.add(serverLinkLabel);

		JPanel clientLinksPanel = new JPanel();
		clientLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		clientLinksPanel.add(clientLinkLabel);
		clientLinksPanel.add(clientJadLinkLabel);
		clientLinksPanel.add(clientJarLinkLabel);
		clientLinksPanel.add(clientNextLinkLabel);
		clientLinksPanel.add(clientNextJadLinkLabel);
		clientLinksPanel.add(clientNextJarLinkLabel);
		JPanel clientCHNLinksPanel = new JPanel();
		clientCHNLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		clientCHNLinksPanel.add(clientCHNLinkLabel);
		clientCHNLinksPanel.add(clientNextCHNLinkLabel);
		clientCHNLinksPanel.add(clientLABLinkLabel);

		JPanel emulatorLinksPanel = new JPanel();
		emulatorLinksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
		emulatorLinksPanel.add(emulatorLinkLabel);

		linksPanel.add(new JLabel("代理服务端下载："), constraintLabel);
		linksPanel.add(serverLinksPanel, constraintFillEnd);
		linksPanel.add(new JLabel("国际版下载："), constraintLabel);
		linksPanel.add(clientLinksPanel, constraintFillEnd);
		linksPanel.add(new JLabel("中国版下载："), constraintLabel);
		linksPanel.add(clientCHNLinksPanel, constraintFillEnd);
		linksPanel.add(new JLabel("Java ME 模拟器："), constraintLabel);
		linksPanel.add(emulatorLinksPanel, constraintFillEnd);

		JScrollPane messagePanel = new JScrollPane();
		messageTextArea.setEditable(false);
		messageTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		messagePanel.setViewportView(messageTextArea);

		centerPanel.add(linksPanel, BorderLayout.NORTH);
		centerPanel.add(messagePanel, BorderLayout.CENTER);

		// south panel
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		southPanel.add(aboutButton, constraintEnd);
		southPanel.add(exitButton, constraintEnd);

		// put all together
		contentPanel.add(northPanel, BorderLayout.NORTH);
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		contentPanel.add(southPanel, BorderLayout.SOUTH);
	}

	private void selectSourceJarFile() {
		jarFileChooser.setFileFilter(new JarFileFilter());
		int option = jarFileChooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			String path = jarFileChooser.getSelectedFile().getPath();
			sourceJarTextField.setText(path);
			if (saveJarTextField.getText().trim().isEmpty()) {
				String autoSavePath = path.substring(0, path.length() - 4)
						+ "-mod.jar";
				saveJarTextField.setText(autoSavePath);
			}
		}
	}

	private void selectSaveJarFile() {
		jarFileChooser.setFileFilter(new JarFileFilter());
		int option = jarFileChooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			String path = jarFileChooser.getSelectedFile().getPath();
			if (!path.endsWith(".jar")) {
				path += ".jar";
			}
			saveJarTextField.setText(path);
		}
	}

	private void testProxyServer() {
		String urlText = testServerTextField.getText().trim();
		if (urlText.isEmpty()) {
			printMessage(MessageType.ERROR, "未输入代理网址。");
			return;
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
		String sourceFilePath = sourceJarTextField.getText().trim();
		String saveFilePath = saveJarTextField.getText().trim();
		String newServer = testServerTextField.getText().trim();
		OperaMini operaMini = (OperaMini) versionComboBox.getSelectedItem();

		if (sourceFilePath.isEmpty() || saveFilePath.isEmpty()
				|| newServer.isEmpty()) {
			printMessage(MessageType.ERROR, "未完整输入全部选项。");
			return;
		}

		try {
			ZipFile sourceJar = new ZipFile(sourceFilePath);
			File saveJar = new File(saveFilePath);
			saveJar.createNewFile();
			ZipOutputStream zip_out = new ZipOutputStream(new FileOutputStream(
					saveJar));

			Enumeration<? extends ZipEntry> entries = sourceJar.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream in = sourceJar.getInputStream(entry);
				byte[] zipEntryBytes = Changer.readAlltoBytes(in);

				// if entry is the class file which need to change
				if (entry.getName().equals(operaMini.classFile)) {
					zipEntryBytes = Changer.replaceClassBytesString(
							zipEntryBytes, operaMini.httpServer, newServer);
					if (operaMini.changerKey) {
						zipEntryBytes = Changer.replaceClassBytesString(
								zipEntryBytes, operaMini.socketServer,
								OperaMini.international.socketServer);
						zipEntryBytes = Changer.replaceServerKey(zipEntryBytes);
					}
				}

				zip_out.putNextEntry(new ZipEntry(entry.getName()));
				zip_out.write(zipEntryBytes);
			}

			zip_out.flush();
			zip_out.close();
			sourceJar.close();
			printMessage(MessageType.INFO, "转换成功，新的Jar创建完毕！");
		} catch (Exception e) {
			e.printStackTrace();
			printMessage(MessageType.ERROR, "转换失败，可能是Jar版本不对或已被转换。");
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
		aboutText = "Opera Mini Server Changer v0.1\n under GPLv3 write by muzuiget";
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
		sourceJarButton.addMouseListener(buttonMouseAdapter);
		saveJarButton.addMouseListener(buttonMouseAdapter);
		testServerButton.addMouseListener(buttonMouseAdapter);
		aboutButton.addMouseListener(buttonMouseAdapter);
		exitButton.addMouseListener(buttonMouseAdapter);
		convertButton.addMouseListener(buttonMouseAdapter);
		serverLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientJadLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientJarLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientNextLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientNextJadLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientNextJadLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientCHNLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientNextCHNLinkLabel.addMouseListener(linkLabelMouseAdapter);
		clientLABLinkLabel.addMouseListener(linkLabelMouseAdapter);
		emulatorLinkLabel.addMouseListener(linkLabelMouseAdapter);
	}

	private class JarFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				return f.getName().toLowerCase().endsWith(".jar");
			}
		}

		@Override
		public String getDescription() {
			return "jar 文件";
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
			if (source == sourceJarButton) {
				selectSourceJarFile();
				return;
			}
			if (source == saveJarButton) {
				selectSaveJarFile();
				return;
			}
			if (source == testServerButton) {
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
