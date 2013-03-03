package org.caofei.feedc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		process();
//		try {
//			List<String> lines = IOUtils.readLines(new FileReader("./index.rss"));
//			for (String line : lines) {
//				System.out.println(line);
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private static void process() {
		try {
			setProxy();
			pullFeed();
			genBean();
			readablityAPI();
			genEntity();
			persistence();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void setProxy() throws MalformedURLException, IOException {
//		 System.setProperty("http.proxyHost", "172.16.131.7");
//		 System.setProperty("http.proxyPort", "3128");
//		 System.setProperty("http.proxyHost", "172.16.131.7");
//		 System.setProperty("http.proxyPort", "3128");
//		 System.out.println(IOUtils.readLines(new
//		 URL("http://cnbeta.com/backend.php").openStream()));
//		 IOUtils.copy(new
//				 URL("http://cnbeta.com/backend.php").openStream(),new FileWriter("./index.rss"));
		 
	}

	private static void readablityAPI() {
		// TODO Auto-generated method stub

	}

	private static void persistence() {
		// TODO Auto-generated method stub

	}

	private static void genEntity() {
		// TODO Auto-generated method stub

	}

	private static void genBean() {
		// TODO Auto-generated method stub

	}

	private static void pullFeed() throws ClientProtocolException, IOException, IllegalArgumentException, FeedException {
		SyndFeedInput input = new SyndFeedInput();
		String feedRoot = "http://cnbeta.com/backend.php";
		//feedRoot = "http://t66y.com/rss.php?fid=7";
		SyndFeed feed = input.build(new XmlReader(new URL(feedRoot)));
		System.out.println(feed.getTitle());
		SyndEntry entry =  (SyndEntry) feed.getEntries().get(0);
		System.out.println(entry.getLink());
		String u = "GET /api/content/v1/parser?url=http://blog.readability.com/2011/02/step-up-be-heard-readability-ideas/&token=1b830931777ac7c2ac954e9f0d67df437175e66e";
		if(true)return;
		HttpHost proxy = new HttpHost("172.16.131.7", 3128, "http");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8");

		// Prepare a request object
		HttpGet httpget = new HttpGet(feedRoot);
		// HttpGet httpget = new HttpGet("http://baidu.com");

		// Execute the request
		HttpResponse response = httpclient.execute(httpget);

		// Examine the response status
		System.out.println(response.getStatusLine());
		String encoding = response.getHeaders("Content-Type")[0].getElements()[0].getParameterByName("charset").getValue();
		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				List<String> lines = IOUtils.readLines(instream,encoding);
				// BufferedReader reader = new BufferedReader(
				// new InputStreamReader(instream));
				// do something useful with the response
				// System.out.println(reader.readLine());
				// String line;
				// while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				// }
				for (String line : lines) {
					System.out.println(line);
				}

			} catch (IOException ex) {

				// In case of an IOException the connection will be released
				// back to the connection manager automatically
				throw ex;

			} catch (RuntimeException ex) {

				// In case of an unexpected exception you may want to abort
				// the HTTP request in order to shut down the underlying
				// connection and release it back to the connection manager.
				httpget.abort();
				throw ex;

			} finally {

				// Closing the input stream will trigger connection release
				instream.close();

			}

			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

	}
}
