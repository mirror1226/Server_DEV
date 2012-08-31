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
	private static List<Socket> mClientList = new ArrayList<Socket>();
	
	//�̳߳�
	private ExecutorService mExecutorService;
	
	//ServerSocket����
	private ServerSocket mSreverSocket;
	
	//����������
	public static void main (String[] args)
	{
		new Server ();
	}
	
	public Server ()
	{
		try
		{
			//���÷������˿�
			mSreverSocket = new ServerSocket (SERVERPORT);
			
			//����һ���̳߳�
			mExecutorService = Executors.newCachedThreadPool ();
			System.out.println("starting...");
			
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
	
	//ÿ���ͻ��˵�������һ���߳�
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
						//��һ���ͻ����˳�ʱ
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
		
		//������Ϣ�����пͻ���
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
