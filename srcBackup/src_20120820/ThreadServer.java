import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ThreadServer implements Runnable{
	private int mPid;
	private String dstIPAdr;
	private Socket mSocket;
	private String mData;
	private BufferedReader mBufferedReader;
	private PrintWriter mPrintWriter;
	
	public ThreadServer (Socket socket, int pid) throws IOException
	{
		mPid = pid;
		mData = "";
		
		System.out.print("Thread pid:");
		System.out.println(mPid);
		System.out.print("Socket Port:");
		System.out.println(socket.getLocalPort());
		
		this.mSocket = socket;
		mBufferedReader = new BufferedReader (new InputStreamReader 
				(socket.getInputStream()));
		
		String IPAdrRaw = mSocket.getInetAddress().toString();
		String IPAdr = Utils.getIPAdr(IPAdrRaw);
		
		String loginMsg = "user:" + IPAdr + " come, total:" + Server.mClientList.size();
		
		sendMessage(IPAdr, loginMsg);
	}
	
	public void run ()
	{
		System.out.println("Thread " + mPid + "is running");
		try
		{
			String rawData = "";
			String data = "";
			while ( (data = mBufferedReader.readLine() ) != null )
			{
				//�õ��ͻ��˷��͵�ԭʼ����
				rawData += data;
				
				//������õ�����Ϊ"exit"���������Socket����
				if (data.trim().equals("exit") )
				{
					//��һ���ͻ����˳�ʱ
					Server.mClientList.remove(mSocket);
					mBufferedReader.close();
					mPrintWriter.close();
					data = "user:" + this.mSocket.getInetAddress() + 
							" exit total:" +Server.mClientList.size();
					mSocket.close();
					
					String IPAdrRaw = mSocket.getInetAddress().toString();
					String IPAdr = Utils.getIPAdr(IPAdrRaw);
					
					sendMessage (IPAdr, data);
					break;
				}
				else
				{
					//�����ͻ������ݣ���ȡĿ�Ŀͻ���IP������
					String fields[] = rawData.split ("#");
					dstIPAdr = Utils.getIPAdr(fields[1]);
					mData = fields[2];
					System.out.println("fields[1]:" + fields[1]);
					System.out.println("fields[2]:" + fields[2]);
					sendMessage (dstIPAdr, mData);
				}
			}		
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//������Ϣ��ָ���ͻ���
	private void sendMessage (String IPAdr, String data) throws IOException
	{
		System.out.println("Thread " + mPid + " " + data);
		for (Socket client : Server.mClientList) 
		{
			String IPAdrRaw = client.getInetAddress().toString();
			String ClientIP = Utils.getIPAdr(IPAdrRaw);
			System.out.println("IPAdr:" + IPAdr);
			System.out.println("ClientIP:" + ClientIP);
			if (ClientIP.contains(IPAdr)) 
			{
				System.out.println("IP match");
				System.out.println("data:" + data);
				mPrintWriter = new PrintWriter (client.getOutputStream(), true );
				mPrintWriter.println (data);
				break;
			}
		}
	}
}
