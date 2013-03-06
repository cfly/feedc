package org.caofei.feedc;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

public class ThreadFetchContent extends Thread {
	private SyndEntry syndEntry;
	public ThreadFetchContent(SyndEntry syndEntry) {
		this.syndEntry = syndEntry;
	}

	@Override
	public void run() {

		SyndContent syndContent = syndEntry.getDescription();
		List<String> lines;
		try {
			lines = App.readablityAPI(syndEntry.getLink());

		if(lines!=null && !lines.isEmpty()){
			syndContent.setValue(lines.get(0));
		}
		List<SyndContent> contents = new LinkedList<SyndContent>();
		contents.add(syndContent);
		syndEntry.setContents(contents);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
