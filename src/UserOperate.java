import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

		// 搜索该玩家是否已注册
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "玩家 " + playerName + " 未存在";
			System.out.println(errMsg);
			return 0;
		}

		String strQueryPlayer = "select * from " + gameName + "_Online"
				+ " where username = '" + playerName + "'";
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);

		// 搜索该玩家是否在线
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
			String playerNameRec, String msgContent)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq, msgContent);
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
			String playerNameRec, String msgContent)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//将发送请求的玩家加入被请求的玩家的好友列表
		FriendInfo friendInfo = new FriendInfo (playerNameReq);
		if (dbOperate.InsertFriendClause(gameName, playerNameRec, friendInfo) != 1)
			return 0;
		
		//将被请求的玩家加入发送请求的玩家的好友列表
		friendInfo = new FriendInfo (playerNameRec);
		if (dbOperate.InsertFriendClause(gameName, playerNameReq, friendInfo) != 1)
			return 0;
		
		//将消息列表中的请求消息删除
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ,
				playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause
				(gameName, playerNameRec, messageInfo) != 1)
			return 0;
		
		//将同意添加好友的消息插入到发送请求的玩家的消息列表
		messageInfo = new MessageInfo (MessageInfo.FRIENDACK,
				playerNameRec, msgContent);
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
	public int RefForFriend (String gameName, String playerNameReq, String playerNameRec, String msgContent)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerNameReq) == 0 || isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//将消息列表中的请求消息删除
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause(gameName, playerNameRec, messageInfo) != 1)
			return 0;
				
		//将拒绝添加好友的消息插入到发送请求的玩家的消息列表
		messageInfo = new MessageInfo (MessageInfo.FRIENDREF, playerNameRec, msgContent);
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
	public String SearchNearby(String gameName, String playerName, 
			double latitude, double longtitude, double range)
	{
		//玩家未注册
		if (isPlayerOnline(gameName, playerName) == 0) return "0";

		//提取数据库中记录的用户名、纬度和经度
		List<String> fields = new ArrayList<String>();
		fields.add("username");
		fields.add("latitude");
		fields.add("longtitude");
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName+ "_Online");
		java.sql.ResultSet rs = dbOperate.Query(tbls , fields, null);
		String result = "";
		
		//筛选记录
		try {
			while (rs.next())
			{
				double rs_lat = rs.getDouble("latitude");
				double rs_lng = rs.getDouble("longtitude");
				String rs_user = rs.getString("username");
				if (rs_user.compareTo(playerName) != 0 && Utils.distance(latitude, longtitude, rs_lat, rs_lng) <= range)	//在范围内
					result = result + "#" + rs_user + "&" + Double.toString(rs_lat) + "&" + Double.toString(rs_lng);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}

		if (!result.equals(""))
		    return result;
		else return "0";
	}
	
	/*
	 * 根据id查找用户
	 * 参数：游戏名称，用户名，查找关键字
	 * 返回值：返回与关键字匹配的用户名，错误值返回0
	 * Example：Result,test1,test2
	 */
	public String SearchPlayerByID(String gameName, String playerName, String key)
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) 
			return "0";
				
		//查询
		List<String> fields = new ArrayList<String>();
		fields.add("username");
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Info");
		String whereClause = "username like '%" + key + "%'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		String result = "";
		try 
		{
			while (rs.next()) 
			{
				if (rs.getString("username").compareTo(playerName) != 0)
				    result = result + "#" + rs.getString("username");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return "0";
		}

		if (!result.equals("")) 
			return result;
		else 
			return "0";
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
		
		//查询
		List<String> fields = new ArrayList<String>();
		fields.add("name");
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Online");
		tbls.add(gameName + "_" + playerName + "_Friend");
		String whereClause = "username = name;";
		
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		String result = "";
		try {
			while (rs.next()) {
				result = result + "#" + rs.getString(gameName + "_" + playerName + "_Friend.name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}
		
		if (!result.equals("")) 
			return result;
		else 
			return "0";
	}
	
	/*
	 * 返回用户playerName的相关消息
	 * 返回值：返回消息类型、日期、请求用户、消息内容
	 * Example：#REQ&2012-08-20&test1&friendship#ACK&2012-08-21&test2&hello world
	 */
	public String GetMessage(String gameName, String playerName) 
	{
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_" + playerName + "_Message");
		List<String> fields = new ArrayList<String>();
		fields.add("type");
		fields.add("date");
		fields.add("srcName");
		fields.add("content");
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, null);
		String result = "";
		try {
			while (rs.next()) {
				result = result + "#" + rs.getString("type") + "&" + rs.getString("date") 
						+ "&" + rs.getString("srcName") + "&" + rs.getString("content");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}
		
		if (result != null && !result.equals("")) 
			return result;
		else 
			return "0";
	}
	
	/*
	 * 查询指定好友信息
	 * 返回值：好友名字、电话、email
	 * Example：#test1&123456&test1@sjtu.edu.cn
	 */
	public String GetFriendInfo(String gameName, String playerName, String friend) {
		
		//玩家未注册
		if (isPlayerReg(gameName, playerName) == 0) 
			return "0";
		
		//判断friend是否为playerName的好友
		if (isFriend(gameName,playerName,friend) == 0) 
			return "0";
		
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Info");
		List<String> fields = new ArrayList<String>();
		fields.add("username");
		fields.add("phone");
		fields.add("Email");
		String whereClause = " username = " + "'" + friend + "'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		String result = "";
		try {
			if (rs.next()) {
				result = result + "#" + rs.getString("username") + "&" 
						+ rs.getString("phone") + "&" + rs.getString("Email");
			}
			else return "0";
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}
		
		if (result != null && !result.equals("")) 
			return result;
		else 
			return "0";
		
	}
	
	/*
	 * 判断玩家是否为好友
	 * 参数：游戏名称gameName，玩家名称playerName，好友名称friend
	 * 返回值：如果是返回1，否则返回0
	 */
	public int isFriend(String gameName, String playerName, String friend) {
		
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_" + playerName + "_Friend");
		List<String> fields = new ArrayList<String>();
		fields.add("name");
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, null);
		
		try {
			if (rs.next()) 
				return 1;
			else 
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 玩家准备游戏
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：玩家成功设置准备状态返回1，否则返回0
	 */
	public int ReadyForGame(String gameName, String playerName)
	{
		//错误信息
		String errMsg = "";
	
		//玩家不在线
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//判断玩家状态
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Online");
		List<String> fields = new ArrayList<String>();
		fields.add("status");
		String whereClause = " username = " + "'" + playerName + "'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		try
		{
			rs.next();
			String status = rs.getString("status");
			//玩家不在线
			if(status.equals(String.valueOf(PlayerOnlineInfo.OFFLINE)))
			{
				errMsg = "玩家" + playerName + "不在线";
				System.out.println(errMsg);
				return 0;
			}
			//玩家在游戏中
			else if(status.equals(String.valueOf(PlayerOnlineInfo.PLAYING)))
			{
				errMsg = "玩家" + playerName + "正在游戏中";
				System.out.println(errMsg);
				return 0;
			}
			//玩家在线或已准备
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.READY));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("玩家" + playerName + "已准备游戏！");
					return 1;
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 玩家取消准备游戏
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：玩家成功设置准备状态返回1，否则返回0
	 */
	public int CancelReadyForGame(String gameName, String playerName)
	{
		//错误信息
		String errMsg = "";
	
		//玩家不在线
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//判断玩家状态
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Online");
		List<String> fields = new ArrayList<String>();
		fields.add("status");
		String whereClause = " username = " + "'" + playerName + "'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		try
		{
			rs.next();
			String status = rs.getString("status");
			//玩家不在准备状态
			if(!status.equals(String.valueOf(PlayerOnlineInfo.READY)))
			{
				errMsg = "玩家" + playerName + "不在准备状态";
				System.out.println(errMsg);
				return 0;
			}
			//玩家已准备
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.ONLINE));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("玩家" + playerName + "已取消准备！");
					return 1;
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 玩家开始游戏
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：玩家成功设置准备状态返回1，否则返回0
	 */
	public int StartForGame(String gameName, String playerName)
	{
		//错误信息
		String errMsg = "";
	
		//玩家不在线
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//判断玩家状态
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Online");
		List<String> fields = new ArrayList<String>();
		fields.add("status");
		String whereClause = " username = " + "'" + playerName + "'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		try
		{
			rs.next();
			String status = rs.getString("status");
			//玩家不在准备状态
			if(!status.equals(String.valueOf(PlayerOnlineInfo.READY)))
			{
				errMsg = "玩家" + playerName + "不在准备状态";
				System.out.println(errMsg);
				return 0;
			}
			//玩家已准备
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.PLAYING));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("玩家" + playerName + "已开始游戏！");
					return 1;
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 玩家结束游戏
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：玩家成功设置准备状态返回1，否则返回0
	 */
	public int EndForGame(String gameName, String playerName)
	{
		//错误信息
		String errMsg = "";
	
		//玩家不在线
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//判断玩家状态
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName + "_Online");
		List<String> fields = new ArrayList<String>();
		fields.add("status");
		String whereClause = " username = " + "'" + playerName + "'";
		java.sql.ResultSet rs = dbOperate.Query(tbls, fields, whereClause);
		
		try
		{
			rs.next();
			String status = rs.getString("status");
			//玩家不在准备状态
			if(!status.equals(String.valueOf(PlayerOnlineInfo.PLAYING)))
			{
				errMsg = "玩家" + playerName + "不在游戏中";
				System.out.println(errMsg);
				return 0;
			}
			//玩家在游戏中
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.ONLINE));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("玩家" + playerName + "已结束游戏！");
					return 1;
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
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
