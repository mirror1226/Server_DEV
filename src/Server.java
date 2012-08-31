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
	public static List<Socket> mClientList = new ArrayList<Socket>();
	
	//线程池
	private ExecutorService mExecutorService;
	
	//ServerSocket对象
	private ServerSocket mSreverSocket;
	
	public Server ()
	{
		Initialize ();
		Work ();
	}
	
	//服务器初始化
	private void Initialize ()
	{
		try
		{
			//设置服务器端口
			mSreverSocket = new ServerSocket (SERVERPORT);
			
			//创建一个线程池
			mExecutorService = Executors.newCachedThreadPool ();
			System.out.println("starting...");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//服务器运行，接收并监听客户端Socket活动
	private void Work ()
	{
		try
		{
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
}
