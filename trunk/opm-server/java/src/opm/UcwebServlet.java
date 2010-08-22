package opm;

import java.io.IOException;
import java.util.Random;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class UcwebServlet extends HttpServlet {

	private static final Random random = new Random();
	private static final String[] servers = { "75.126.123.205:8086",
			"75.126.123.206:8089", "75.126.123.208:8089",
			"75.126.123.209:8090", "75.126.123.210:8087", "75.126.123.211:8089",
			"75.126.123.212:8087" };

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (req.getParameter("test") == null) {
			resp.sendRedirect("http://www.google.com/");
		} else {
			resp.getWriter().printf("Hello UCWeb Server! Fuck GFW!");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// String server = servers = "67.228.68.101:8086";
		int index = random.nextInt(servers.length - 1);
		String server = servers[index];
		resp.setHeader("Assign", server);
		resp.getWriter().printf("%c%cassign%c%c%s", 0x00, 0x06, 0x00, 0x13,
				server);
		resp.flushBuffer();
	}
}
