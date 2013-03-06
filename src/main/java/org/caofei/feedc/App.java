package org.caofei.feedc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Map<String, String> kv = new HashMap<String, String>();
	private static final String TOKEN;
	static {
		ResourceBundle kvprop = ResourceBundle.getBundle("kv");
		Enumeration<String> keys = kvprop.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			kv.put(key, kvprop.getString(key));
		}
		TOKEN = kv.get("TOKEN");
	}

	public static void main(String[] args) {
		System.out.println("Hello World!");
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			System.out.println(kv.get(arg));
			process(arg);
		}
	}

	private static void process(String rssUrl) {
		try {
			setProxy();
			pullFeed(rssUrl);
			genBean();
			// readablityAPI();
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
//		System.setProperty("http.proxyHost", "172.16.131.7");
//		System.setProperty("http.proxyPort", "3128");
		// System.setProperty("http.proxyHost", "172.16.131.7");
		// System.setProperty("http.proxyPort", "3128");
		// System.out.println(IOUtils.readLines(new
		// URL("http://cnbeta.com/backend.php").openStream()));
		// IOUtils.copy(new
		// URL("http://cnbeta.com/backend.php").openStream(),new
		// FileWriter("./index.rss"));

	}

	private static void pullFeed(String rssUrl) throws ClientProtocolException,
			IOException, IllegalArgumentException, FeedException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(new URL(rssUrl)));
		System.out.println(feed.getTitle());
		List<SyndEntry> entries = feed.getEntries();
		List<Future> futures = new LinkedList<Future>();
		for (int i = 0; i < entries.size(); i++) {
			SyndEntry syndEntry = entries.get(i);
			System.out.println("entries\t" + i + "/" + entries.size() + "\t" + syndEntry.getLink());
			// }
			// for(SyndEntry syndEntry : entries) {
			futures.add(fetchContent(syndEntry));
		}
		for (Future future : futures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String fileStr = kv.get(rssUrl);
		File file = new File(fileStr);
		file.getParentFile().mkdirs();
		file.createNewFile();
			
		Writer writer = new FileWriter(kv.get(rssUrl));
		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, writer);
		writer.close();
		// SyndEntry entry = (SyndEntry) feed.getEntries().get(0);
		// System.out.println(entry.getLink());
	}

	private static Future fetchContent(SyndEntry syndEntry)
			throws ClientProtocolException, IOException {
		return ThreadFetchContent.submit(new ThreadFetchContent(syndEntry));
	}

	private static Pattern regx = Pattern.compile("<content>(.*)</content>",
			Pattern.DOTALL);

	static List<String> readablityAPI(String url)
			throws ClientProtocolException, IOException {
		String api = "https://readability.com/api/content/v1/parser?token="
				+ TOKEN + "&format=xml&url=";
		List<String> lines = curl(api + url);
		StringBuilder sb = new StringBuilder();
		if (lines != null) {
			for (String line : lines) {
				sb.append(line);
			}
			lines.clear();
			Matcher matcher = regx.matcher(sb.toString());
			if (matcher.find()) {
				lines.add(matcher.group(1));
			}
		}
		return lines;
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

	private static List<String> curl(String url)
			throws ClientProtocolException, IOException {
		List<String> lines = null;
//		HttpHost proxy = new HttpHost("172.16.131.7", 3128, "http");
		HttpClient httpclient = new DefaultHttpClient();
//		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
//				proxy);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8");

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);
		
		// Execute the request
		HttpResponse response = httpclient.execute(httpget);

		// Examine the response status
		// System.out.println(response.getStatusLine());
		if (response.getStatusLine().getStatusCode() != 200) {
			System.err.println(response.getStatusLine());
			return null;
		}
		String encoding = response.getHeaders("Content-Type")[0].getElements()[0]
				.getParameterByName("charset").getValue();
		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// StringBuilder sb = new StringBuilder();
		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				lines = IOUtils.readLines(instream, encoding);
				// BufferedReader reader = new BufferedReader(
				// new InputStreamReader(instream));
				// do something useful with the response
				// System.out.println(reader.readLine());
				// String line;
				// while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				// }
				// for (String line : lines) {
				// System.out.println(line);
				// sb.append(line);
				// }

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
		return lines;
	}
}
