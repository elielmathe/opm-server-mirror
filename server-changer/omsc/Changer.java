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

	public static byte[] replaceBytesString(byte[] bytes, String source,
			String replacement) {
		String bytesString = new String(bytes, Charset.forName("US-ASCII"));
		int offset = bytesString.indexOf(source);
		System.arraycopy(replacement.getBytes(), 0, bytes, offset, replacement
				.length());
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
