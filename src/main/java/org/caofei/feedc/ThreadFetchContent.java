package org.caofei.feedc;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.client.ClientProtocolException;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

public class ThreadFetchContent implements Callable {
	private SyndEntry syndEntry;

	public ThreadFetchContent(SyndEntry syndEntry) {
		this.syndEntry = syndEntry;
	}

	@Override
	public SyndEntry call() {

		SyndContent syndContent = null;
		try {
			syndContent = (SyndContent) syndEntry.getDescription().clone();
			List<SyndContent> contents = new LinkedList<SyndContent>();
			contents.add(syndContent);
			syndEntry.setContents(contents);
			List<String> lines;
			lines = App.readablityAPI(syndEntry.getLink());

			if (lines != null && !lines.isEmpty()) {
				syndContent.setType("text/xml");
				syndContent.setValue("<![CDATA[" + lines.get(0) + "]]>");
			}
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return syndEntry;
	}

	private static final ExecutorService executor = Executors
			.newFixedThreadPool(10);

	static final Future submit(ThreadFetchContent task) {
		return executor.submit(task);
	}

	static final void shutdown() {
		executor.shutdown();
	}
}
