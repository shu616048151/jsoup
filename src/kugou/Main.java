package kugou;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Main {
	public static void main(String[] args) throws IOException {
		String url=null;
		MusicLink musicLink=new MusicLink();	
		for(int i=14;i<20;i++){
			 url="https://www.kugou.com/yy/rank/home/"+i+"-8888.html?from=homepage";
			List<Music> musicLinks = musicLink.getMusicLinks(url);
			//开始下载
			musicLink.download(musicLinks);
			
			
		}
	}
}
