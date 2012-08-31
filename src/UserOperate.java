

public class UserOperate {
	
	//数据库操作对象
	private DBOperate dbOperate;
	
	/*
	 * 构造函数
	 */
	public UserOperate ()
	{
		dbOperate =  new DBOperate ();
	}
	
	/*
	 * 判断游戏玩家是否已注册
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：玩家已注册返回1，否则返回0
	 */
	public int isPlayerReg (String gameName, String playerName)
	{
		//错误提示
		String errMsg = "";
		
		//搜索该游戏是否已注册
		GameOperate gOperate = new GameOperate ();
		if (gOperate.isRegister(gameName) == 0)
		{
			errMsg = "游戏 " + gameName + " 不存在";
			System.out.println(errMsg);
			gOperate.Finish();
			return 0;
		}
		
			
		String strQueryPlayer = "select * from " + gameName + "_Info"
				+ " where username = '" + playerName + "'" ;
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);
				
		//搜索该玩家是否已注册
		try
		{
			if (rsPlayer.first())
			{
				return 1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		errMsg = "玩家 " + playerName + " 不存在";
		System.out.println(errMsg);
		return 0;
	}
	
	/*
	 * 游戏玩家注册
	 * 参数：游戏名称gameName，玩家id playerID，玩家名称playerName
	 * 返回值：玩家成功注册返回1，否则返回0
	 */
	public int RegPlayer (String gameName, PlayerInfo playerInfo)
	{
		// 错误提示
		String errMsg = "";
		String playerName = playerInfo.GetName();

		//搜索该玩家是否已注册
		if (isPlayerReg (gameName, playerName) == 1)
		{
			errMsg = "玩家 " + playerName + " 已注册";
			System.out.println(errMsg);
			return 0;
 		}
		
		//注册玩家
		if (dbOperate.InsertInfoClause(gameName, playerInfo) == 1 && 
				dbOperate.CreateMessageTbl(gameName, playerName) == 1 && 
				dbOperate.CreateFriendTbl(gameName, playerName) == 1) 
		{
			System.out.println (playerName + "玩家注册成功");
			return 1;
		}
			
		
		return 0;
	}
	
	/*
	 * 游戏玩家注销
	 * 参数：游戏名称gameName，玩家id playerID，玩家名称playerName
	 * 返回值：玩家成功注销返回1，否则返回0
	 */
	public int unRegPlayer (String gameName, String playerName)
	{
		// 错误提示
		String errMsg = "";

		//搜索该玩家是否已注册
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "玩家 " + playerName + " 未注册";
			System.out.println(errMsg);
			return 0;
		}
		
		//删除玩家的注册记录
		if (dbOperate.deleteSingleInfoClause(gameName, playerName) == 1 &&
				dbOperate.DropMessageTbl(gameName, playerName) == 1 &&
				dbOperate.DropFriendTbl(gameName, playerName) == 1) 
		{
			System.out.println (playerName + "玩家注销成功");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * 游戏玩家登陆
	 * 参数：游戏名称 gameName，玩家名称 playerName，
	 *          玩家密码 playerPwd，游戏管理器 gameMgr
	 * 返回值：登陆成功则返回1，否则返回0
	 */
	public int Login (String gameName, String playerPwd, PlayerOnlineInfo playerOnlineInfo)
	{
		String errMsg = "";
		
		String playerName = playerOnlineInfo.GetName();
		
		//如果玩家未注册，返回0
		if (isPlayerReg ( gameName, playerName) == 0) 
		{
			errMsg = "玩家 " + playerName + " 未注册";
			System.out.println (errMsg);
			return 0;
		}
		
		//验证用户名密码
		String strQueryPlayer = "select * from " + gameName + "_Info"
				+ " where username = '" + playerName + "'" ;
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);
		
		String pwd = "";
		try
		{
			rsPlayer.next();
			pwd = rsPlayer.getString("password");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//密码错误，返回0
		if (!pwd.equalsIgnoreCase(playerPwd))
		{
			errMsg = "密码错误";
			System.out.println(errMsg);
			return 0;
		}
		
		//成功登录
		if (dbOperate.InsertOnlineClause(gameName, playerOnlineInfo) == 1)
		{
			System.out.println (playerName + "玩家登录成功！");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * 判断玩家是否在线
	 * 参数：游戏名称gameName，玩家名称 playerName
	 * 返回值：如果玩家在线返回1，否则返回0
	 */
	public int isPlayerOnline (String gameName, String playerName)
	{
		// 错误提示
		String errMsg = "";

		// 搜索该游戏是否已注册
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "玩家 " + playerName + " 未存在";
			System.out.println(errMsg);
			return 0;
		}

		String strQueryPlayer = "select * from " + gameName + "_Online"
				+ " where username = '" + playerName + "'";
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);

		// 搜索该玩家是否已注册
		try {
			if (rsPlayer.first()) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		errMsg = "玩家 " + playerName + " 不在线";
		System.out.println(errMsg);
		return 0;
	}
	
	/*
	 * 游戏玩家退出
	 * 参数：游戏名称 gameName，玩家名称 playerName，
	 *          游戏管理器 gameMgr
	 * 返回值：登陆成功则返回1，否则返回0
	 */
	public int Logout (String gameName, String playerName)
	{
		String errMsg = "";
				
		//如果玩家不在线，返回0
		if (isPlayerOnline(gameName, playerName) == 0)
		{
			errMsg = "玩家 " + playerName + " 不在线";
			System.out.println (errMsg);
			return 0;
		}
		
		//玩家成功下线
		if ( dbOperate.deleteSingleOnlineClause(gameName, playerName) == 1)
		{
			System.out.println (playerName + "玩家下线成功！");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * 报告玩家所处的位置
	 * 参数：游戏名称gameName，玩家名称playerName，
	 * 			 玩家所处纬度latitude，玩家所处经度longtitude
	 * 返回值：报告成功返回1，否则返回0
	 */
	public String Check (String gameName, String playerName, 
			double latitude, double longtitude)
	{
		//玩家不在线
		if (isPlayerOnline (gameName, playerName) == 0) return "0";
		
		//玩家报告所处位置
		String tblName = gameName +  "_Online";
		String strUpdate = "update " + tblName + " set latitude =" + latitude + 
				", longtitude=" + longtitude + " where username='" + playerName + "'";
		if (dbOperate.DBUpdate(strUpdate) == 1)
		{
			System.out.println (playerName + "在纬度：" + latitude + "，经度：" + longtitude);
			return "1";
		}
		return "0";
	}
	
	/*
	 * 请求加好友
	 * 参数：游戏名称gameName，请求方玩家名称playerNameReq，
	 * 			 被请求方玩家名称playerNameRec
	 * 返回值：成功发送请求返回1，否则返回0
	 */
	public int RepForFriend (String gameName, String playerNameReq,
			String playerNameRec)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		String content = "Apply for friendship!";
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq, content);
		if (dbOperate.InsertMessageClause(gameName, playerNameRec, messageInfo) == 1)
		{
			System.out.println (playerNameReq + "成功向" + playerNameRec + "发出好友请求！");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * 同意加好友
	 * 参数：游戏名称gameName，请求方玩家名称playerNameReq，
	 * 			 被请求方玩家名称playerNameRec
	 * 返回值：成功发送请求返回1，否则返回0
	 */
	public int AckForFriend (String gameName, String playerNameReq,
			String playerNameRec)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//将发送请求的玩家加入好友列表
		FriendInfo friendInfo = new FriendInfo (playerNameReq);
		if (dbOperate.InsertFriendClause(gameName, playerNameRec, friendInfo) != 1)
			return 0;
		
		//将消息列表中的请求消息删除
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ,
				playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause
				(gameName, playerNameRec, messageInfo) != 1)
			return 0;
		
		//将同意添加好友的消息插入到发送请求的玩家的消息列表
		messageInfo = new MessageInfo (MessageInfo.FRIENDACK,
				playerNameRec);
		if (dbOperate.InsertMessageClause(gameName, playerNameReq, messageInfo) != 1)
			return 0;
		
		
		System.out.println (playerNameRec + "同意添加" + playerNameReq + "为好友！");
		return 1;
		
	}
	
	/*
	 * 拒绝添加好友
	 * 参数：游戏名称gameName，请求方玩家名称playerNameReq，
	 * 			 被请求方玩家名称playerNameRec
	 * 返回值：成功发送请求返回1，否则返回0
	 */
	public int RefForFriend (String gameName, String playerNameReq, String playerNameRec)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//将消息列表中的请求消息删除
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause(gameName, playerNameRec, messageInfo) != 1)
			return 0;
				
		//将拒绝添加好友的消息插入到发送请求的玩家的消息列表
		messageInfo = new MessageInfo (MessageInfo.FRIENDREF, playerNameRec);
		if (dbOperate.InsertMessageClause(gameName, playerNameReq, messageInfo) != 1)
			return 0;
				
		System.out.println (playerNameRec + "拒绝添加" + playerNameReq + "为好友！");
		return 1;	
	}
	
	/*
	 * 搜索周边用户
	 * 参数：游戏名称，用户名，经纬度
	 * 返回值：周边用户的用户名与经纬度，错误值返回0
	 * Example：Result,test1:1.1111 2.2222,test2:3.3333 4.444
	 */
	public String SearchNearby(String gameName, String playerName, double latitude, double longtitude)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		String result = dbOperate.selectPlayerNearby(gameName, latitude, longtitude);
		if (result != null)
		    return result;
		else return "0";
	}
	
	/*
	 * 根据id查找用户
	 * 参数：游戏名称，用户名，查找关键字
	 * 返回值：返回与关键字匹配的用户名，错误值返回0
	 * Example：Result,test1,test2
	 */
	public String SearchByID(String gameName, String playerName, String key)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
				
		String result = dbOperate.selectByID(gameName, playerName, key);
		if (result != null) return result;
		else return "0";
	}
	
	/*
	 * 查找在线好友
	 * 返回值：返回在线好友的用户名，错误值返回0
	 * Example：Result,test1,test2
	 */
	public String ShowFriendsOnline(String gameName, String playerName)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		String result = dbOperate.selectFriendsOnline(gameName, playerName);
		if (result != null) return result;
		else return "0";
	}
	
	/*
	 * 返回用户playerName的相关消息
	 * 返回值：返回消息类型、日期、请求用户、消息内容
	 * Example：Result,REQ:2012-08-20:test1:friendship,ACK:2012-08-21:test2:hello world
	 */
	public String GetMessage(String gameName, String playerName) 
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		String result = dbOperate.selectMessage(gameName, playerName);
		if (result != null) return result;
		else return "0";
	}
	
	/*
	 * 查询指定好友信息
	 * 返回值：好友名字、电话、email
	 * Example：Result,test1 123456 test1@sjtu.edu.cn
	 */
	public String GetFriendInfo(String gameName, String playerName, String friend) {
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		//判断friend是否为playerName的好友
		if (dbOperate.isFriend(gameName,playerName,friend) == 0) return "0";
		else {
			String result = dbOperate.selectFriendInfo(gameName, friend);
			if (result != null) return result;
			else return "0";
		}
	}
	
	/*
	 * 对象结束处理
	 * 返回值：资源成功释放返回1，否则返回0
	 */
	public int Finish ()
	{
		return dbOperate.Finish();
	}
}
