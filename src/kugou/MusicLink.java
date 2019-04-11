package kugou;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.sun.org.apache.regexp.internal.recompile;

import net.sf.json.JSONObject;

public class MusicLink {
	public static String mp3 = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&callback=jQuery191027067069941080546_1546235744250&"
			+ "hash=HASH&album_id=0&_=TIME";
	static int  num=0;
	/**
	 * 得到当前页所有音乐的链接
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public List<Music> getMusicLinks(String url) throws IOException{
		List<Music> list=new ArrayList<>();
		Connection conn=Jsoup.connect(url).timeout(30000);
		Document doc=conn.header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get(); 
		Elements ele=doc.select(".pc_temp_songlist>ul>li");
		//获取每首的链接
		for(Element e:ele){
			//得到含有链接的标签
			Element select = e.select("a").first();
			String title=select.attr("title");
			String musicUrl=select.attr("href");
			System.out.println(musicUrl);
			Music music=new Music();
			music.setTitle(title);
			music.setUrl(musicUrl);
			String downloadLink = getDownloadLink(musicUrl);
			music.setDownLink(downloadLink);
			list.add(music);
			//System.out.println("第"+num+"首   歌名  《"+title+"》");
		}
		System.out.println(">>>>>>>>>>>");
		return list;
	}
	//解析得到真正的下载地址
	@Test
	public String getDownloadLink(String url) throws IOException{
		if (url==null||url.trim()=="") {
			return null;
		}
		Connection conn=Jsoup.connect(url);
		Document doc=conn.header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get(); 
		
		//正则表达式得到匹配哈希码
		String regEx = "\"hash\":\"[0-9A-Z]+\"";
		// 编译正则表达式
		String hash=null;
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(doc.toString());
		if (matcher.find()) {
			 hash = matcher.group();
			hash = hash.replace("\"hash\":\"", "");
			hash = hash.replace("\"", "");
		}
		String item = mp3.replace("HASH", hash);
		item = item.replace("TIME", System.currentTimeMillis() + "");

		//System.out.println(item);
		conn=Jsoup.connect(item).timeout(30000);
		doc=conn.header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").get(); 
		String mp=doc.text();
		mp = mp.substring(mp.indexOf("(") + 1, mp.length()-2);
		
		JSONObject json = JSONObject.fromObject(mp);
		String playUrl = json.getJSONObject("data").getString("play_url");
		//System.out.println(playUrl);
		return playUrl;
	}
	public void download(List<Music> list) throws IOException{
		for(Music music:list){
			String url=music.getDownLink();
			if (url!=null&&!url.equals("")) {
			Connection conn=Jsoup.connect(url);
			conn.maxBodySize(0);
			Response rs=conn.ignoreContentType(true).timeout(3000).ignoreHttpErrors(true).execute();
			String title=music.getTitle().replaceAll("/", "-");
			File file = new File("E:/music",title+".mp3");
			FileOutputStream outputStream=new FileOutputStream(file);
			byte[] bs=rs.bodyAsBytes();
			outputStream.write(bs);
			outputStream.close();
			num++;
			System.out.println("第"+num+"首   歌名  《"+music.getTitle()+"》"+"下载完成");
			}
		}
		list=null;
	}
}
