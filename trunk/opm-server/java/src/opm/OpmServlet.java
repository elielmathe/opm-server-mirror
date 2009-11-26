package opm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class OpmServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (req.getParameter("test") == null) {
			resp.sendRedirect("http://www.google.com/");
		} else {
			resp.getWriter().printf("Hello Opera Mini Server! Fuck GFW!");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			URL url = new URL("http://server4.operamini.com");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("User-Agent", "Java/1.6.0_15");
			connection.setRequestProperty("Connection", "keep-alive");
			int length;
			byte[] buffer = new byte[1024];
			ServletInputStream req_in = req.getInputStream();
			OutputStream con_out = connection.getOutputStream();
			while ((length = req_in.read(buffer)) != -1) {
				con_out.write(buffer, 0, length);
			}
			con_out.flush();
			con_out.close();
			req_in.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				resp.setHeader("Content-Type", "application/octet-stream");
				resp.setHeader("Cache-Control", "priavte, no-cache");
				InputStream con_in = connection.getInputStream();
				ServletOutputStream resp_out = resp.getOutputStream();
				while ((length = con_in.read(buffer)) != -1) {
					resp_out.write(buffer, 0, length);
				}
			} else {
				resp.sendError(connection.getResponseCode());
			}
		} catch (Exception e) {
			resp.sendError(HttpURLConnection.HTTP_UNAVAILABLE);
		}
	}

}
