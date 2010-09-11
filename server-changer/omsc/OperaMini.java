package omsc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public abstract class OperaMini {

	static final String[] DEFAULT_SERVERS = {
			"http://server4.operamini.com:80/",
			"socket://server4.operamini.com:1080",
			"http://mini5.opera-mini.net:80/",
			"socket://mini5.opera-mini.net:1080" };

	protected String openFilePath;

	public OperaMini(String openFilePath) {
		this.openFilePath = openFilePath;
	}

	public abstract String checkInfo() throws IOException;

	public abstract void doConvert(String saveFilePath, String newServerUrl)
			throws IOException;

	public static byte[] readAlltoBytes(InputStream in) throws IOException {
		int readLength;
		byte[] buffer = new byte[10240];
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while ((readLength = in.read(buffer)) != -1) {
			out.write(buffer, 0, readLength);
		}
		out.flush();
		out.close();
		in.close();

		return out.toByteArray();
	}

	public static byte[] replaceBytes(byte[] bytes, byte[] sourceBytes,
			byte[] replacementBytes) {
		String sourceString = new String(sourceBytes,
				Charset.forName("US-ASCII"));
		String bytesString = new String(bytes, Charset.forName("US-ASCII"));

		// find source text position
		int offsetStart = bytesString.indexOf(sourceString);
		if (offsetStart == -1) {
			return bytes;
		}

		int offsetEnd = offsetStart + sourceBytes.length;

		int newBytesLength = bytes.length
				+ (replacementBytes.length - sourceBytes.length);
		ByteArrayOutputStream out = new ByteArrayOutputStream(newBytesLength);

		out.write(bytes, 0, offsetStart);
		out.write(replacementBytes, 0, replacementBytes.length);
		out.write(bytes, offsetEnd, bytes.length - offsetEnd);

		return out.toByteArray();

	}

}

class JavaPlatform extends OperaMini {

	String jarVersion;
	String classFile;

	public JavaPlatform(String inputFilePath) {
		super(inputFilePath);
	}

	@Override
	public String checkInfo() throws IOException {

		int founded = 0;

		ZipFile openJar = new ZipFile(openFilePath);
		Enumeration<? extends ZipEntry> entries = openJar.entries();

		while (entries.hasMoreElements() && founded < 2) {
			ZipEntry entry = entries.nextElement();
			InputStream in = openJar.getInputStream(entry);
			byte[] zipEntryBytes = readAlltoBytes(in);

			if (entry.getName().equals("META-INF/MANIFEST.MF")) {
				String manifest = new String(zipEntryBytes);
				String[] lines = manifest.split("\n");
				for (String line : lines) {
					if (line.startsWith("MIDlet-Version")) {
						jarVersion = line.split(": ")[1];
						founded += 1;
					}
				}
				continue;
			}

			if (entry.getName().endsWith(".class")) {
				String bytesString = new String(zipEntryBytes,
						Charset.forName("US-ASCII"));
				for (String server : OperaMini.DEFAULT_SERVERS) {
					if (bytesString.indexOf(server) != -1) {
						classFile = entry.getName();
						founded += 1;
						break;
					}
				}
			}
		}
		String message = String.format("Opera Mini %s, 服务器地址在 %s 文件中。",
				jarVersion, classFile);
		openJar.close();
		return message;

	}

	@Override
	public void doConvert(String saveFilePath, String newServerUrl)
			throws IOException {

		ZipFile openJar = new ZipFile(openFilePath);
		File saveJar = new File(saveFilePath);
		saveJar.createNewFile();
		ZipOutputStream zip_out = new ZipOutputStream(new FileOutputStream(
				saveJar));

		Enumeration<? extends ZipEntry> entries = openJar.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			InputStream in = openJar.getInputStream(entry);
			byte[] zipEntryBytes = readAlltoBytes(in);

			// if entry is the class file which need to change
			if (entry.getName().equals(classFile)) {
				for (String server : OperaMini.DEFAULT_SERVERS) {
					zipEntryBytes = replaceClassBytesString(zipEntryBytes,
							server, newServerUrl);
				}
			}

			zip_out.putNextEntry(new ZipEntry(entry.getName()));
			zip_out.write(zipEntryBytes);
		}

		zip_out.flush();
		zip_out.close();
		openJar.close();
	}

	private static byte[] replaceClassBytesString(byte[] bytes, String source,
			String replacement) {
		byte[] sourceBytes = classStringBytes(source);
		byte[] replacementBytes = classStringBytes(replacement);
		return replaceBytes(bytes, sourceBytes, replacementBytes);
	}

	private static byte[] classStringBytes(String text) {
		int length = text.length();
		byte[] bytes = new byte[length + 2];
		bytes[0] = (byte) (length >> 8);
		bytes[1] = (byte) (length);
		System.arraycopy(text.getBytes(), 0, bytes, 2, length);
		return bytes;
	}
}

class WMPlatform extends OperaMini {

	String exeVersion;

	public WMPlatform(String openFilePath) {
		super(openFilePath);
	}

	@Override
	public String checkInfo() throws IOException {
		File openFile = new File(openFilePath);
		FileInputStream openFileInputStream = new FileInputStream(openFile);
		byte[] openFileBytes = readAlltoBytes(openFileInputStream);
		String bytesString = new String(openFileBytes,
				Charset.forName("US-ASCII"));

		int beginIndex = bytesString.indexOf("Opera Mini/")
				+ "Opera Mini/".length();
		int endIndex = beginIndex + "x.x.xxxxx".length();
		exeVersion = bytesString.substring(beginIndex, endIndex);
		String message = String.format("Opera Mini %s For Windows Mobile。",
				exeVersion);
		openFileInputStream.close();
		return message;
	}

	@Override
	public void doConvert(String saveFilePath, String newServerUrl)
			throws IOException {
		File openFile = new File(openFilePath);
		FileInputStream openFileInputStream = new FileInputStream(openFile);
		byte[] openFileBytes = readAlltoBytes(openFileInputStream);

		// replace server url
		openFileBytes = replacePE32BytesString(openFileBytes,
				"http://server4.operamini.com:80", newServerUrl);
		ByteArrayInputStream saveFileByteArrayInputStream = new ByteArrayInputStream(
				openFileBytes);

		// create new file
		File saveFile = new File(saveFilePath);
		saveFile.createNewFile();
		FileOutputStream saveFileOutputStream = new FileOutputStream(saveFile);

		// write to file
		int length;
		byte[] buffer = new byte[10240];
		while ((length = saveFileByteArrayInputStream.read(buffer)) != -1) {
			saveFileOutputStream.write(buffer, 0, length);
		}
		saveFileOutputStream.flush();
		saveFileOutputStream.close();
		openFileInputStream.close();
	}

	private static byte[] replacePE32BytesString(byte[] bytes, String source,
			String replacement) {
		int length = findPE32StringMaxLength(bytes, source);
		int margin = replacement.length() - length;
		if (margin > 0) {
			String message = String.format("代理地址长度超出 %d 个字符", margin);
			throw new IndexOutOfBoundsException(message);
		}

		byte[] sourceBytes = pe32StringBytes(source, length);
		byte[] replacementBytes = pe32StringBytes(replacement, length);
		return replaceBytes(bytes, sourceBytes, replacementBytes);
	}

	private static int findPE32StringMaxLength(byte[] bytes, String source) {
		String bytesString = new String(bytes, Charset.forName("US-ASCII"));
		int offsetStart = bytesString.indexOf(source);
		if (offsetStart == -1) {
			return 0;
		}

		int offsetEnd = offsetStart + source.length();
		int i;
		for (i = offsetEnd; i < bytes.length; i++) {
			if (bytes[i] != 0x00) {
				break;
			}
		}
		return i - 1 - offsetStart;
	}

	private static byte[] pe32StringBytes(String text, int length) {
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = 0x00;
		}
		System.arraycopy(text.getBytes(), 0, bytes, 0, text.length());
		return bytes;
	}
}
