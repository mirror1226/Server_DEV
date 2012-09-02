import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable {

	// 该线程在服务器线程池的编号
	private int mPid;

	// 线程的Socket对象
	private Socket mSocket;

	// 线程Socket的输入流对象
	private BufferedReader mBufferedReader;

	// 线程Socket的输出流对象
	private PrintWriter mPrintWriter;

	// 游戏操作对象
	GameOperate gameOperate;

	// 玩家操作对象
	UserOperate playerOperate;

	String IPAdr;

	/*
	 * 构造函数 参数：与客户端相连的Socket对象socket，线程好pid
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
				
				// 得到客户端发送的原始数据
				String result = ParseInstruction(data);
				sendMessage(IPAdr, result);
			}

			// 如果所得到数据为"exit"，则结束该Socket连接
			// if (dataRaw.trim().equals("exit") )
			// {
			// //当一个客户端退出时
			// close ();
			// }
			// else
			// {
			// //解析客户端数据，获取目的客户端IP和数据
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
	 * 发送消息给指定客户端 参数：目标客户端IP地址IPAdr，发送数据data 返回值：无
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
	 * 线程关闭 参数：无 返回值：无
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
	 * 解析指令 参数：指令字符串instruction 返回值：指令正确执行则返回1，否则返回0
	 */
	private String ParseInstruction(String instruction) {
		System.out.println("in parse instruction");
		System.out.println("instruction:" + instruction);
		// 空指令
		if (instruction.equals(""))
			return "0";

		// 对指令进行解析
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
	 * 控制指令解析 参数：指令类型号type，指令内容content 返回值：指令正确执行则返回1，否则返回0
	 */
	private String ParseCtlInstruction(String type, String content) {
		// System.out.println ("in control instruction");
		// System.out.println ("type:" + type);
		// System.out.println ("content:" + content);
		// 空指令
		if (type.equals(""))
			return "0";

		// 将指令分解
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
	 * 针对游戏的控制指令执行 参数：功能号type，内容content 返回值：执行成功返回1，否则返回0
	 */
	private String ExecGameCtl(String type, String content) {
		// System.out.println("in ExecGameCtl");
		// System.out.println("type:" + type);
		// System.out.println("content:" + content);
		switch (type) {
		// 游戏注册
		case "01":
			return gameOperate.regGame(content) + "";

			// 游戏注销
		case "02":
			return gameOperate.unRegGame(content) + "";

		default:
			return "0";
		}
	}

	/*
	 * 针对玩家的控制指令执行 参数：功能号type，内容content 返回值：执行成功返回1，否则返回0
	 */
	private String ExecPlayerCtl(String type, String content) {
		// 解析指令型号
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
	 * 玩家控制指令中对自身操作的执行函数 参数：功能号type，内容content 返回值：执行成功返回1，否则返回0
	 */
	private int ExecPlayerCtlItself(String type, String content) {
		// 解析指令内容
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		// 根据指令功能号执行指令
		switch (type) {
		// 玩家注册
		case "01":

			// 玩家信息playerContent：玩家名称#玩家密码#联系电话#Email
			String info[] = playerContent.split("&");
			String playerName = info[0];
			String password = info[1];
			String phone = info[2];
			String Email = info[3];

			PlayerInfo playerInfo = new PlayerInfo(playerName, password, phone,
					Email);
			return playerOperate.RegPlayer(gameName, playerInfo);

			// 玩家注销
		case "02":

			// 玩家信息playerContent：玩家名称
			playerName = playerContent;
			return playerOperate.unRegPlayer(gameName, playerName);

			// 玩家登陆上线
		case "03":

			// 玩家信息playerContent：玩家名称&玩家密码&IP地址
			info = playerContent.split("&");
			playerName = info[0];
			password = info[1];
			// String IPAdr = info[2];

			PlayerOnlineInfo playerOnlineInfo = new PlayerOnlineInfo(
					playerName, IPAdr);
			return playerOperate.Login(gameName, password, playerOnlineInfo);

			// 玩家下线
		case "04":

			// 玩家信息playerContent：玩家名称
			playerName = playerContent;
			return playerOperate.Logout(gameName, playerName);

			// 玩家准备开始游戏
		case "05":
			playerName = playerContent;
			return playerOperate.ReadyForGame(gameName, playerName);
			
			// 玩家取消游戏准备
		case "06":
			playerName = playerContent;
			return playerOperate.CancelReadyForGame(gameName, playerName);
			
			// 显示玩家消息
		case "07":
			playerName = playerContent;
			String message = playerOperate.GetMessage(gameName, playerName);
			if(!message.equals("0"))
				return 1;
			else
				return 0;
			
			//玩家开始游戏
		case "08":
			playerName = playerContent;
			return playerOperate.StartForGame(gameName, playerName);
			
			//玩家结束游戏
		case "09":
			playerName = playerContent;
			return playerOperate.EndForGame(gameName, playerName);

		default:
			return 0;
		}
	}

	/*
	 * 玩家控制指令中对好友操作的执行函数 参数：功能号type，内容content 返回值：执行成功返回1，否则返回0
	 */
	private String ExecPlayerCtlFriend(String type, String content) {
		// 解析指令内容
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		switch (type) {
		// 请求添加好友
		case "11":
			
			// 玩家信息playerContent：发起请求玩家名称&被请求玩家名称
			String info[] = playerContent.split("&");
			String playerNameReq = info[0];
			String playerNameRec = info[1];

			return playerOperate.RepForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// 同意添加好友
		case "12":
			// 玩家信息playerContent：发起请求玩家名称&被请求玩家名称
			info = playerContent.split("&");
			playerNameReq = info[0];
			playerNameRec = info[1];

			return playerOperate.AckForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// 拒绝添加好友
		case "13":
			// 玩家信息playerContent：发起请求玩家名称&被请求玩家名称
			info = playerContent.split("&");
			playerNameReq = info[0];
			playerNameRec = info[1];

			return playerOperate.RefForFriend(gameName, playerNameReq,
					playerNameRec) + "";

			// 玩家选中好友进行游戏
		case "14":

			// 玩家请求与另一玩家进行直连
		case "15":

			// 根据关键字查询用户
		case "16":
			info = playerContent.split("&");
			String playerName = info[0];
			String keyword = info[1];
			return playerOperate
					.SearchPlayerByID(gameName, playerName, keyword);

			// 查询在线好友
		case "17":
			info = playerContent.split("&");
			playerName = info[0];
			return playerOperate.ShowFriendsOnline(gameName, playerName);

			// 查询好友信息
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
	 * 玩家控制指令中对位置操作的执行函数 
	 * 参数：功能号type，内容content 
	 * 返回值：执行成功返回1，否则返回0
	 */
	private String ExecPlayerCtlLocation(String type, String content) {
		
		// 解析指令内容
		String fields[] = content.split("\\$");
		String gameName = fields[0];
		String playerContent = fields[1];

		// 玩家信息playerContent：发起请求玩家名称&玩家所在纬度&玩家所在经度
		String info[] = playerContent.split("&");
		String playerName = info[0];
		String latitude = info[1];
		String longtitude = info[2];

		switch (type) {

		// 玩家报到当前位置
		case "21":
			return playerOperate.Check(gameName, playerName,
					Double.valueOf(latitude), Double.valueOf(longtitude));

			// 玩家搜索周边在线玩家
		case "22":
			return playerOperate.SearchNearby(gameName, playerName,
					Double.valueOf(latitude), Double.valueOf(longtitude), 10);

		default:
			return "0";
		}
	}

	/*
	 * 数据传输指令解析 参数：指令类型号type，指令内容content 返回值：指令正确执行则返回1，否则返回0
	 */
	private int ParseDataInstruction(String type, String content) {
		// 空指令
		if (type.equals(""))
			return 0;

		// 解析IP地址和数据内容
		String fields[] = content.split("\\$");
		String IPAdr = fields[0];
		String data = fields[1];

		// 发送数据
		try {
			sendMessage(IPAdr, data);
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

	}

}
