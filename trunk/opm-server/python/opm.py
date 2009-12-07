import cgi

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.api import urlfetch

class MainPage(webapp.RequestHandler):
  def get(self):
	if self.request.get('test'):
		self.response.headers.add_header('Content-Type', 'text/plain')
		self.response.out.write('Hello Opera Mini Server! Fuck GFW!')
	else:
		self.response.set_status(301, webapp.Response.http_status_message(301))
		self.response.headers.add_header('Location', 'http://www.google.com/ncr')
  def post(self):
	url = "http://server4.operamini.com"
	headers = {
		'content-type': 'application/xml'
	}
	result = urlfetch.fetch(url=url,
		payload=self.request.body,
		method=urlfetch.POST,
		headers=headers,
		deadline=10)
	if result.status_code == 200:
          self.response.headers.add_header('Content-Type', 'application/octet-stream')
          self.response.headers.add_header('Cache-Control', 'private, no-cache')
          self.response.out.write(result.content)

application = webapp.WSGIApplication(
                                     [('/', MainPage)],
                                     debug=True)

def main():
  run_wsgi_app(application)

if __name__ == "__main__":
  main()
