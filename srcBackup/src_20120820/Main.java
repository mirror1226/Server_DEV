import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
	
	//开启服务器
	public static void main (String[] args)
	{
		String name = "五子棋";
		PlayerInfo playerInfo = new PlayerInfo ("changych", "12345", "12345", "cyc@gmail.com");
		PlayerInfo playerInfo2 = new PlayerInfo ("changrui", "12345", "12345", "crui@gmail.com");
		String playerName = playerInfo.GetName();
		String playerName2 = playerInfo2.GetName();
		
		PlayerOnlineInfo playerOnlineInfo = new PlayerOnlineInfo 
				("changych", "123.211.14.67", 1, 12.34, 34.12);
		
		DBOperate dbOperate = new DBOperate ();
		GameOperate gOperate = new GameOperate ();
		UserOperate uOperate = new UserOperate ();
		
		System.out.println (gOperate.isRegister(name));
		System.out.println (gOperate.regGame(name));
		System.out.println (gOperate.isRegister(name));
		
		System.out.println(uOperate.RegPlayer(name, playerInfo));
		System.out.println(uOperate.RegPlayer(name, playerInfo2));
		System.out.println(uOperate.Login(name, "12345", playerOnlineInfo));
		System.out.println(uOperate.RepForFriend(name, playerName, playerName2));
		String strQuery = "select * from " + name + "_" + playerName2 + "_Message";
		java.sql.ResultSet rs = dbOperate.DBQuery(strQuery);
		try
		{
			while (rs.next())
			{
				System.out.println(rs.getString("type"));
				System.out.println(rs.getString("srcName"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(uOperate.AckForFriend(name, playerName, playerName2));
		strQuery = "select * from " + name + "_" + playerName2 + "_Message";
		rs = dbOperate.DBQuery(strQuery);
		try
		{
			while (rs.next())
			{
				System.out.println(rs.getString("type"));
				System.out.println(rs.getString("srcName"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println();
		System.out.println(uOperate.isPlayerOnline(name, playerName));
		System.out.println(uOperate.Check(name, playerName, 3.21, 6.78));
		System.out.println(uOperate.RepForFriend(name, playerName, playerName2));
		
		strQuery = "select * from " + name + "_" + playerName2 + "_Message";
		rs = dbOperate.DBQuery(strQuery);
		try
		{
			rs.next();
			System.out.println(rs.getString("srcName"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println(uOperate.Logout(name, playerName));	
		System.out.println(uOperate.isPlayerOnline(name, playerName));
		System.out.println(uOperate.isPlayerReg(name, playerName));
		System.out.println(uOperate.unRegPlayer(name, playerName));
		System.out.println(uOperate.unRegPlayer(name, playerName2));
		System.out.println (gOperate.unRegGame(name));

		uOperate.Finish ();
		gOperate.Finish ();
		
//		DBOperate dbOperate = new DBOperate ();
//		dbOperate.CreateFriendTbl("五子棋", "changych");
//		FriendInfo friendInfo = new FriendInfo ("changrui");
//		dbOperate.InsertFriendClause("五子棋", "changych", friendInfo);
//		dbOperate.deleteSingleFriendClause("五子棋", "changych", "changrui");
//		dbOperate.DropFriendTbl("五子棋", "changych");
	}
}
