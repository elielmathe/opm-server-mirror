package net.luosheng;

import java.io.RandomAccessFile;

public class ClassEditor {

	private static String OLD_SERVER = "http://server4.operamini.com:80/";
	private static String NEW_SERVER = "http://server4.operamini.com:80/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RandomAccessFile raf = new RandomAccessFile("D:\\a.class", "r");
			byte[] classBytes = new byte[(int) raf.length()];
			raf.readFully(classBytes);
			raf.close();

			String classString = new String(classBytes);
			int pos = classString.indexOf(OLD_SERVER);

			if (pos > 0) {
				RandomAccessFile raf2 = new RandomAccessFile("D:\\b.class",
						"rw");
				raf2.write(classBytes, 0, pos - 1);
				byte[] serverBytes = NEW_SERVER.getBytes();

				raf2.writeByte(serverBytes.length);
				raf2.write(serverBytes);
				raf2.write(classBytes, pos + serverBytes.length,
						classBytes.length - pos - serverBytes.length);
				raf2.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
