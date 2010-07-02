package opm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.logging.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class OpmServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(OpmServlet.class.getName());
	
	static {
		log.setLevel(java.util.logging.Level.INFO);
	}

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
			URL url = new URL("http://server4.operamini.com/");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("User-Agent", "Java/1.6.0_15");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Pragma", "no-cache");
			connection.setReadTimeout(10000);
			int length;
			byte[] buffer = new byte[16384];
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
				resp.setHeader("Cache-Control", "private, no-cache");
				resp.setHeader("Pragma", "no-cache");
				InputStream con_in = connection.getInputStream();
				ServletOutputStream resp_out = resp.getOutputStream();
				while ((length = con_in.read(buffer)) != -1) {
					resp_out.write(buffer, 0, length);
				}
				resp_out.flush();
				resp_out.close();
				con_in.close();
			} else {
				resp.sendError(connection.getResponseCode());
				log.warning("Response from server:" + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			log.warning(sw.toString());
			resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}

}
