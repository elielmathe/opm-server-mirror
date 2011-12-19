package opm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class OpmServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(OpmServlet.class
			.getName());

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
			// log request headers
			StringBuffer hLogBuf = new StringBuffer(128);
			hLogBuf.append("Request headers:\n");
			Enumeration<?> hdnEnum = req.getHeaderNames();
			for (; hdnEnum.hasMoreElements();) {
				String hdName = hdnEnum.nextElement().toString();
				Enumeration<?> hdvEnum = req.getHeaders(hdName);
				for (; hdvEnum.hasMoreElements();) {
					hLogBuf.append(hdName + ": " + hdvEnum.nextElement().toString() + '\n');
				}
			}
			log.info(hLogBuf.toString());

			// open a connection to Opera's server
			URL url = new URL("http://server4.operamini.com/");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("User-Agent", "Java/1.6.0_15");
			connection.setRequestProperty("Cache-Control", "no-cache, max-age=0");	// App Engine doesn't obey the "no-cache" directive, see GAE Issue 739
			connection.setRequestProperty("Pragma", "no-cache");
			connection.setUseCaches(false);
			connection.setReadTimeout(60000);

			// forward request data to server
			int length;
			int totallen = 0;
			byte[] buffer = new byte[16384];
			ServletInputStream req_in = req.getInputStream();
			OutputStream con_out = connection.getOutputStream();
			while ((length = req_in.read(buffer)) != -1) {
				con_out.write(buffer, 0, length);
				totallen += length;
			}
			log.info("Request size: " + totallen);
			con_out.flush();
			con_out.close();
			req_in.close();

			// log response headers from server
			hLogBuf.delete(0, hLogBuf.length());
			hLogBuf.append("Response headers:\n");
			Map<String, List<String>> hdrflds = connection.getHeaderFields();
			for(Map.Entry<String, List<String>> entry : hdrflds.entrySet()) {
				for (String val : entry.getValue()) {
					hLogBuf.append(entry.getKey()+ ": " + val + '\n');
				}
			}
			log.info(hLogBuf.toString());
			

			// get response from server and forward it to the Opera Mini client
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				resp.setHeader("Content-Type", "application/octet-stream");
				resp.setHeader("Cache-Control", "private, no-cache");
				resp.setHeader("Pragma", "no-cache");
				InputStream con_in = connection.getInputStream();
				ServletOutputStream resp_out = resp.getOutputStream();
				totallen = 0;
				while ((length = con_in.read(buffer)) != -1) {
					resp_out.write(buffer, 0, length);
					totallen += length;
				}
				log.info("Response size: " + totallen);
				resp_out.flush();
				resp_out.close();
				con_in.close();
			} else {
				resp.sendError(connection.getResponseCode());
				log.warning("Response from server: "
						+ connection.getResponseCode() + " "
						+ connection.getResponseMessage());
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			log.warning(sw.toString());
			resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);	// Opera Mini will auto-retry when it sees an HTTP 502, or so it seems
		}
	}

}
