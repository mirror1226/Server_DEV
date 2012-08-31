import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class Server 
{
	
	//服务器端口
	private static final int SERVERPORT = 54321;
	
	//客户端连接
	private static List<Socket> mClientList = new ArrayList<Socket>();
	
	//线程池
	private ExecutorService mExecutorService;
	
	//ServerSocket对象
	private ServerSocket mSreverSocket;
	
	//开启服务器
	public static void main (String[] args)
	{
		new Server ();
	}
	
	public Server ()
	{
		try
		{
			//设置服务器端口
			mSreverSocket = new ServerSocket (SERVERPORT);
			
			//创建一个线程池
			mExecutorService = Executors.newCachedThreadPool ();
			System.out.println("starting...");
			
			//用来临时保存客户端连接的Socket对象
			Socket client  = null;
			while (true)
			{
				//接收客户连接并添加到List中
				client = mSreverSocket.accept ();
				mClientList.add (client);
				
				//开启一个客户端线程
				int pid = mClientList.indexOf(client);
				mExecutorService.execute(new ThreadServer (client, pid) );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//每个客户端单独开启一个线程
	static class ThreadServer implements Runnable
	{
		private int mPid;
		private String dstIPAdr;
		private Socket mSocket;
		private BufferedReader mBufferedReader;
		private PrintWriter mPrintWriter;
		private String mStrMSG;
		
		public ThreadServer (Socket socket, int pid) throws IOException
		{
			mPid = pid;
			
			System.out.print("Thread pid:");
			System.out.println(mPid);
			System.out.print("Socket Port:");
			System.out.println(socket.getLocalPort());
			
			this.mSocket = socket;
			mBufferedReader = new BufferedReader (new InputStreamReader 
					(socket.getInputStream()));
			mStrMSG = "user:" + this.mSocket.getInetAddress() + 
					" come, total:" + mClientList.size();
			String IPAdr = mSocket.getInetAddress().toString();
			sendMessage(IPAdr);
		}
		
		public void run ()
		{
			System.out.println("Thread " + mPid + "is running");
			try
			{
				while ( (mStrMSG = mBufferedReader.readLine() ) != null )
				{
					if (mStrMSG.trim().equals("exit") )
					{
						//当一个客户端退出时
						mClientList.remove(mSocket);
						mBufferedReader.close();
						mPrintWriter.close();
						mStrMSG = "user:" + this.mSocket.getInetAddress() + 
								" exit total:" + mClientList.size();
						mSocket.close();
						String IPAdr = mSocket.getInetAddress().toString();
						sendMessage (IPAdr);
						break;
					}
					else
					{
						mStrMSG = mSocket.getInetAddress() + ":" + mStrMSG;
						String IPAdr = mSocket.getInetAddress().toString();
						sendMessage (IPAdr);
					}
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		//发送消息给所有客户端
		private void sendMessage (String IPAdr) throws IOException
		{
			System.out.println("Thread " + mPid + " " + mStrMSG);
			for (Socket client : mClientList) 
			{
				String ClientIP = client.getInetAddress().toString();
				if (ClientIP.equals(IPAdr)) 
				{
					mPrintWriter = new PrintWriter (client.getOutputStream(), true );
					mPrintWriter.println(mStrMSG);
				}
			}
		}
	}
}
