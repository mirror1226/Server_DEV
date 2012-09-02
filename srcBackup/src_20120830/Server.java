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
	
	//�������˿�
	private static final int SERVERPORT = 54321;
	
	//�ͻ�������
	public static List<Socket> mClientList = new ArrayList<Socket>();
	
	//�̳߳�
	private ExecutorService mExecutorService;
	
	//ServerSocket����
	private ServerSocket mSreverSocket;
	
	public Server ()
	{
		Initialize ();
		Work ();
	}
	
	//��������ʼ��
	private void Initialize ()
	{
		try
		{
			//���÷������˿�
			mSreverSocket = new ServerSocket (SERVERPORT);
			
			//����һ���̳߳�
			mExecutorService = Executors.newCachedThreadPool ();
			System.out.println("starting...");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//���������У����ղ������ͻ���Socket�
	private void Work ()
	{
		try
		{
			//������ʱ����ͻ������ӵ�Socket����
			Socket client  = null;
			while (true)
			{
				//���տͻ����Ӳ���ӵ�List��
				client = mSreverSocket.accept ();
				mClientList.add (client);
				
				//����һ���ͻ����߳�
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
