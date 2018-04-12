

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Server {
	public static Map<String, String> clientsip = new HashMap<String, String>(); 
	   public static void main(String args[]) throws IOException {
	      //为了简单起见，所有的异常信息都往外抛
	     int port = 45644;
	      //定义一个ServerSocket监听在端口45644上
	     ServerSocket server = new ServerSocket(port);
	      while (true) {
	         //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
	         Socket socket = server.accept();
	         //每接收到一个Socket就建立一个新的线程来处理它
	         new Thread(new Task(socket)).start();
	      }
	   }
	   
	   /**
	    * 用来处理Socket请求的
	   */
	   static class Task implements Runnable {
	 
	      private Socket socket;
	      
	      public Task(Socket socket) {
	         this.socket = socket;
	      }
	      
	      public void run() {
	         try {
	            handleSocket();
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	      }
	      
	      /**
	       * 跟客户端Socket进行通信
	      * @throws Exception
	       */
	      private void handleSocket() throws Exception {
	         BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	         StringBuilder sb = new StringBuilder();
	         String temp;
	         while ((temp=br.readLine()) != null) {
	            sb.append(temp);
	         }
           byte[] reciverByte=AESCrptography.toBytes(sb.toString());
           String key="luaizhuo521";  
	      String iv="nicaicaishisha!!"; 
           System.out.println(new String(AESCrptography.AES_CBC_Decrypt(reciverByte, key.getBytes(), iv.getBytes()))+socket.getInetAddress()); //打印从客户端就收到的字符串
	      String reciverStr = new String(AESCrptography.AES_CBC_Decrypt(reciverByte, key.getBytes(), iv.getBytes()));
		 int clientNum = Integer.parseInt(reciverStr.split("/")[0].replaceAll("Client", ""));
		 String clientName = "Client" + String.valueOf(clientNum % 10);
		 String srcIp = "", dstIp = "";
		 if ((srcIp = clientsip.get(clientName)) == null) {
		     srcIp = "0.0.0.0";
		 }
		 dstIp = socket.getInetAddress().toString().substring(1);
                 Date day=new Date();    
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); 
		 String log=clientName+" : "+df.format(day)+" : src="+srcIp+" : dst="+dstIp+"\n";
                 try {
	            BufferedWriter bw = new BufferedWriter(new FileWriter("/root/"+clientName,true));
	            bw.write(log);//写收到的心跳包日志
	            bw.close();
	         } catch (IOException e1) {
	             e1.printStackTrace();
	         }
		 if (!dstIp.equals(srcIp)) {
		     String cmd = "/root/test " + srcIp + " " + dstIp;//要执行的脚本的路径，"/root/test "可以更改为自己的地址
		    // System.out.println(cmd);
		     executeLinuxCmd2(cmd);//执行更改dns的脚本
                     try {
                          BufferedWriter bw = new BufferedWriter(new FileWriter("/root/gg"+clientName,true));
                          bw.write(log); //写收到的并进行了更改的心跳包日志
                          bw.close();
                        } catch (IOException e1) {
                          e1.printStackTrace();
                        }
		     clientsip.put(clientName, dstIp);
		 }
                 br.close();
	         socket.close();
	      }
	      public static String executeLinuxCmd2(String cmd) {
	          Runtime run = Runtime.getRuntime();
	          String buf = null;
	          try {
	              Process process = run.exec(cmd);
	              InputStream in = process.getInputStream(); 
	              BufferedReader bs = new BufferedReader(new InputStreamReader(in));
	              buf = bs.readLine();
	              bs.close();
	              in.close();
	              process.destroy();
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          return buf;
	      }	

	   }
	}
