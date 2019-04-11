package kugou;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;



import org.jsoup.Connection.Response;

public class downloadTest {
	public static void main(String[] args) throws IOException {
		String url="http://fs.w.kugou.com/201901031917/b532de353ac59e58a3593a0471dbf75a/G114/M07/1F/04/sg0DAFlKHeeAIuuhAFKMQ0TqEYw721.mp3";
		Connection conn=Jsoup.connect(url);
		conn.maxBodySize(0);
		Response rs=conn.ignoreContentType(true).timeout(3000).ignoreHttpErrors(true).execute();
		File file = new File("E:/","123.mp3");
		FileOutputStream outputStream=new FileOutputStream(file);
		byte[] bs=rs.bodyAsBytes();
		outputStream.write(bs);
		outputStream.close();
	
		
	}
}
