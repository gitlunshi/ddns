

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Client {

	public static void main(String args[]) throws Exception {
		// 为了简单起见，所有的异常都直接往外抛
		String host = "127.0.0.1"; // 要连接的服务端IP地址
		int port = 45644; // 要连接的服务端对应的监听端口
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 建立连接后就可以往服务端写数据了
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Random rad = new Random();
		int radint;
		while ((radint = rad.nextInt(1000)) % 2 != 0) {

		}
		String sendSrcStr = "Client" + radint + "/NowTimeIs" + df.format(day);
		String key = "keyvlue";
		String iv = "nicaicaishisha!!";
		writer.write(AESCrptography
				.byteToHexString(AESCrptography.AES_CBC_Encrypt(sendSrcStr.getBytes(), key.getBytes(), iv.getBytes())));
		writer.flush();
		writer.close();
		client.close();
	}
}