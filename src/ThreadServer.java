import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable {

	// ���߳��ڷ������̳߳صı��
	private int mPid;

	// �̵߳�Socket����
	private Socket mSocket;

	// �߳�Socket������������
	private BufferedReader mBufferedReader;

	// �߳�Socket�����������
	private PrintWriter mPrintWriter;

	// ��Ϸ��������
	GameOperate gameOperate;

	// ��Ҳ�������
	UserOperate playerOperate;

	String IPAdr;

	/*
	 * ���캯�� ��������ͻ���������Socket����socket���̺߳�pid
	 */
	public ThreadServer(Socket socket, int pid) throws IOException 
	{
		gameOperate = new GameOperate();
		playerOperate = new UserOperate();
		mPid = pid;

		this.mSocket = socket;
		mBufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);

		String IPAdrRaw = mSocket.getInetAddress().toString();
		IPAdr = Utils.getIPAdr(IPAdrRaw);

		String loginMsg = "user:" + IPAdr + " come, total:"
				+ Server.mClientList.size();
		System.out.println(loginMsg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			String data = "";
			while ((data = mBufferedReader.readLine()) != null) {
				System.out.println(data);
				
				// �õ��ͻ��˷��͵�ԭʼ����
				String result = ParseInstruction(data);
				sendMessage(IPAdr, result);
			}

			// ������õ�����Ϊ"exit"���������Socket����
			// if (dataRaw.trim().equals("exit") )
			// {
			// //��һ���ͻ����˳�ʱ
			// close ();
			// }
			// else
			// {
			// //�����ͻ������ݣ���ȡĿ�Ŀͻ���IP������
			// String fields[] = dataRaw.split ("#");
			// String dstIPAdr = Utils.getIPAdr(fields[1]);
			// String mData = fields[2];
			// System.out.println("fields[1]:" + fields[1]);
			// System.out.println("fields[2]:" + fields[2]);
			// sendMessage (dstIPAdr, mData);
			// }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ������Ϣ��ָ���ͻ��� ������Ŀ��ͻ���IP��ַIPAdr����������data ����ֵ����
	 */
	private void sendMessage(String IPAdr, String data) throws IOException {
		// System.out.println("Thread " + mPid + " " + data);
		for (Socket client : Server.mClientList) {
			String IPAdrRaw = client.getInetAddress().toString();
			String ClientIP = Utils.getIPAdr(IPAdrRaw);
			// System.out.println("IPAdr:" + IPAdr);
			// System.out.println("ClientIP:" + ClientIP);
			if (ClientIP.contains(IPAdr)) {
				// System.out.println("IP match");
				System.out.println("data:" + data);
				mPrintWriter = new PrintWriter(client.getOutputStream(), true);
				mPrintWriter.println(data);
				break;
			}
		}
	}

	/*
	 * �̹߳ر� �������� ����ֵ����
	 */
	private void close() {
		try {
			Server.mClientList.remove(mSocket);
			mBufferedReader.close();
			mPrintWriter.close();
			gameOperate.Finish();
			playerOperate.Finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ����ָ�� ������ָ���ַ���instruction ����ֵ��ָ����ȷִ���򷵻�1�����򷵻�0
	 */
	private String ParseInstruction(String instruction) {
		System.out.println("in parse instruction");
		System.out.println("instruction:" + instruction);
		// ��ָ��
		if (instruction.equals(""))
			return "0";

		// ��ָ����н���
		String fields[] = instruction.split("#");
		String type = fields[1];
		String content = fields[2];

		if (type.startsWith("01")) {
			return ParseCtlInstruction(type, content);
		} else if (type.startsWith("02")) {
			return ParseDataInstruction(type, content) + "";
		}

		return "0";
	}

	/*
	 * ����ָ����� ������ָ�����ͺ�type��ָ������content ����ֵ��ָ����ȷִ���򷵻�1�����򷵻�0
	 */
	private String ParseCtlInstruction(String type, String content) {
		// System.out.println ("in control instruction");
		// System.out.println ("type:" + type);
		// System.out.println ("content:" + content);
		// ��ָ��
		if (type.equals(""))
			return "0";

		// ��ָ��ֽ�
		String object = type.substring(2, 4);
		String function = type.substring(4, 6);

		// System.out.println ("object:" + object);
		// System.out.println ("function:" + function);

		if (object.equals("01")) {
			return ExecGameCtl(function, content);
		} else if (object.equals("02")) {
			return ExecPlayerCtl(function, content);
		}

		return "0";
	}

	/*
	 * �����Ϸ�Ŀ���ָ��ִ�� ���������ܺ�type������content ����ֵ��ִ�гɹ�����1�����򷵻�0
	 */
	private String ExecGameCtl(String type, String content) {
		// System.out.println("in ExecGameCtl");
		// System.out.println("type:" + type);
		// System.out.println("content:" + content);
		switch (type) {
		// ��Ϸע��
		case "01":
			return gameOperate.regGame(content) + "";

			// ��Ϸע��
		case "02":
			return gameOperate.unRegGame(content) + "";

		default:
			return "0";
		}
	}

	/*
	 * �����ҵĿ���ָ��ִ�� ���������ܺ�type������content ����ֵ��ִ�гɹ�����1�����򷵻�0
	 */
	private String ExecPlayerCtl(String type, String content) {
		// ����ָ���ͺ�
		String subType = type.substring(0, 1);
		switch (subType) {
		case "0":
			return ExecPlayerCtlItself(type, content) + "";
		case "1":
			return ExecPlayerCtlFriend(type, content) + "";
		case "2":
			return ExecPlayerCtlLocation(type, content) + "";
		default:
			return "0";

		}
	}

	/*
	 * ��ҿ���ָ���ж����������ִ�к��� ���������ܺ�type������content ����ֵ��ִ�гɹ�����1�����򷵻�0
	 */
	private int ExecPlayerCtlItself(String type, String content) {
		// ����ָ������
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		// ����ָ��ܺ�ִ��ָ��
		switch (type) {
		// ���ע��
		case "01":

			// �����ϢplayerContent���������#�������#��ϵ�绰#Email
			String info[] = playerContent.split("&");
			String playerName = info[0];
			String password = info[1];
			String phone = info[2];
			String Email = info[3];

			PlayerInfo playerInfo = new PlayerInfo(playerName, password, phone,
					Email);
			return playerOperate.RegPlayer(gameName, playerInfo);

			// ���ע��
		case "02":

			// �����ϢplayerContent���������
			playerName = playerContent;
			return playerOperate.unRegPlayer(gameName, playerName);

			// ��ҵ�½����
		case "03":

			// �����ϢplayerContent���������&�������&IP��ַ
			info = playerContent.split("&");
			playerName = info[0];
			password = info[1];
			// String IPAdr = info[2];

			PlayerOnlineInfo playerOnlineInfo = new PlayerOnlineInfo(
					playerName, IPAdr);
			return playerOperate.Login(gameName, password, playerOnlineInfo);

			// �������
		case "04":

			// �����ϢplayerContent���������
			playerName = playerContent;
			return playerOperate.Logout(gameName, playerName);

			// ���׼����ʼ��Ϸ
		case "05":
			playerName = playerContent;
			return playerOperate.ReadyForGame(gameName, playerName);
			
			// ���ȡ����Ϸ׼��
		case "06":
			playerName = playerContent;
			return playerOperate.CancelReadyForGame(gameName, playerName);
			
			// ��ʾ�����Ϣ
		case "07":
			playerName = playerContent;
			String message = playerOperate.GetMessage(gameName, playerName);
			if(!message.equals("0"))
				return 1;
			else
				return 0;
			
			//��ҿ�ʼ��Ϸ
		case "08":
			playerName = playerContent;
			return playerOperate.StartForGame(gameName, playerName);
			
			//��ҽ�����Ϸ
		case "09":
			playerName = playerContent;
			return playerOperate.EndForGame(gameName, playerName);

		default:
			return 0;
		}
	}

	/*
	 * ��ҿ���ָ���жԺ��Ѳ�����ִ�к��� ���������ܺ�type������content ����ֵ��ִ�гɹ�����1�����򷵻�0
	 */
	private String ExecPlayerCtlFriend(String type, String content) {
		// ����ָ������
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		switch (type) {
		// ������Ӻ���
		case "11":
			
			// �����ϢplayerContent�����������������&�������������
			String info[] = playerContent.split("&");
			String playerNameReq = info[0];
			String playerNameRec = info[1];

			return playerOperate.RepForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// ͬ����Ӻ���
		case "12":
			// �����ϢplayerContent�����������������&�������������
			info = playerContent.split("&");
			playerNameReq = info[0];
			playerNameRec = info[1];

			return playerOperate.AckForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// �ܾ���Ӻ���
		case "13":
			// �����ϢplayerContent�����������������&�������������
			info = playerContent.split("&");
			playerNameReq = info[0];
			playerNameRec = info[1];

			return playerOperate.RefForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// ���ѡ�к��ѽ�����Ϸ
		case "14":

			// �����������һ��ҽ���ֱ��
		case "15":

			// ���ݹؼ��ֲ�ѯ�û�
		case "16":
			info = playerContent.split("&");
			String playerName = info[0];
			String keyword = info[1];
			return playerOperate
					.SearchPlayerByID(gameName, playerName, keyword);

			// ��ѯ���ߺ���
		case "17":
			info = playerContent.split("&");
			playerName = info[0];
			return playerOperate.ShowFriendsOnline(gameName, playerName);

			// ��ѯ������Ϣ
		case "18":
			info = playerContent.split("&");
			playerName = info[0];
			String friend = info[1];
			return playerOperate.GetFriendInfo(gameName, playerName, friend);

		default:
			return "0";

		}
	}

	/*
	 * ��ҿ���ָ���ж�λ�ò�����ִ�к��� 
	 * ���������ܺ�type������content 
	 * ����ֵ��ִ�гɹ�����1�����򷵻�0
	 */
	private String ExecPlayerCtlLocation(String type, String content) {
		
		// ����ָ������
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		// �����ϢplayerContent�����������������&�������γ��&������ھ���
		String info[] = playerContent.split("&");
		String playerName = info[0];
		String latitude = info[1];
		String longtitude = info[2];

		switch (type) {

		// ��ұ�����ǰλ��
		case "21":
			return playerOperate.Check(gameName, playerName,
					Double.valueOf(latitude), Double.valueOf(longtitude));

			// ��������ܱ��������
		case "22":
			return playerOperate.SearchNearby(gameName, playerName,
					Double.valueOf(latitude), Double.valueOf(longtitude), 10);

		default:
			return "0";
		}
	}

	/*
	 * ���ݴ���ָ����� ������ָ�����ͺ�type��ָ������content ����ֵ��ָ����ȷִ���򷵻�1�����򷵻�0
	 */
	private int ParseDataInstruction(String type, String content) {
		// ��ָ��
		if (type.equals(""))
			return 0;

		// ����IP��ַ����������
		String fields[] = content.split("\\$");
		String IPAdr = fields[0];
		String data = fields[1];

		// ��������
		try {
			sendMessage(IPAdr, data);
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

	}

}
