package omsc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Changer {

	public Changer() {
		System.out.println("Fuck GFW!");
	}

	public static void main(String[] args) {

		if (args.length != 3) {
			System.out
					.println("useage: ./java Changer server class_file output_class_file");
			System.exit(0);
		}

		String inputFilePath, outputFilePath;
		String[] server = { "http://china-4.opera-mini.net:80/",
				"socket://china-4.opera-mini.net:1080" };
		String[] newServer = { "", "socket://server4.operamini.com:1080" };
		inputFilePath = "/media/sdb6/src/operamini/operamini-hifi_linkid-zh/I.class";
		outputFilePath = "/media/sdb6/src/I.class.new";

		newServer[0] = args[0];
		inputFilePath = args[1];
		inputFilePath = args[2];

		System.out.println("Convert Begin");

		try {
			makeNewClassFile(inputFilePath, outputFilePath, server, newServer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Convert Finish");
	}

	public static void makeNewClassFile(String path, String newPath,
			String server[], String[] newServer) throws IOException {

		File inputfile = new File(path);
		File outputFile = new File(newPath);
		outputFile.createNewFile();

		FileInputStream in = new FileInputStream(inputfile);
		byte[] inputFileBytes = readAlltoBytes(in);
		byte[] outputFileBytes;
		outputFileBytes = replaceClassBytesString(inputFileBytes, server[0],
				newServer[0]);
		outputFileBytes = replaceClassBytesString(outputFileBytes, server[1],
				newServer[1]);
		outputFileBytes = replaceServerKey(outputFileBytes);

		FileOutputStream fileOutStream = new FileOutputStream(outputFile);
		fileOutStream.write(outputFileBytes);
		fileOutStream.close();
	}

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

	public static byte[] replaceServerKey(byte[] bytes) {
		String oldKey = "8c60d2a6811f85366af231ae416831b09409b614e9cfa8fde8d8577e892636e0e0b7a151f9601b930bf527ea8a22bfe6fb5f72506bd3e81b3b55d189af17e35b2d7ea61d84ba4e62cf1c01789edb2c3f3c00fc3c09ee1fc9627367294727e52af4c990516d8d7aad4e00d6ab50cd8ca63705df0af243e666dad282d6514b656780e108d591cf78920f7bdee21ed1419a080655ca2acdadc4e64dba01b5accf73";
		String newKey = "c1dd7ab77e2c967746fe10681026c920f864811321bcb8be6bbfa5a03fda4e16c9c8db3af280f7703366e778e93c55e7159a8852d2b1381e521a337f22b1406cddf41a3114aecb4f4bfe79e0c5aa2ba8824fc989cb8bdcbf8ec5cef5176bfd4059f229b91bfa025126b295f9c409e75f6f6415ee094fd7f5dfd395a1f431668c5a08e88de891dc4dd38d4e9aa9b9c00dc604a0428e3aa5a28ccfa75af099147b";

		String bytesString = new String(bytes, Charset.forName("US-ASCII"));
		int offset = bytesString.indexOf(oldKey);
		System.arraycopy(newKey.getBytes(), 0, bytes, offset, newKey.length());
		return bytes;
	}

	public static byte[] replaceClassBytesString(byte[] bytes, String source,
			String replacement) throws IOException {
		byte[] sourceBytes = classStringBytes(source);
		byte[] replacementBytes = classStringBytes(replacement);
		String sourceString = new String(sourceBytes, Charset
				.forName("US-ASCII"));
		String bytesString = new String(bytes, Charset.forName("US-ASCII"));

		// find source text position
		int offsetStart = bytesString.indexOf(sourceString);
		if (offsetStart == 0) {
			throw new IOException();
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

	public static byte[] classStringBytes(String text) {
		int length = text.length();
		byte[] bytes = new byte[length + 2];
		bytes[0] = (byte) (length >> 8);
		bytes[1] = (byte) (length);
		System.arraycopy(text.getBytes(), 0, bytes, 2, length);
		return bytes;
	}
}
