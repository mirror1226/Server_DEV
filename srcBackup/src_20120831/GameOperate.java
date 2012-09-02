import java.util.ArrayList;
import java.util.List;


public class GameOperate {
	
	//存储所有游戏信息的表格名称
	private final String gameTblName = "GameInfo";
	
	//所注册游戏总数
	private static int gameNum = 0;
	
	//数据库操作对象
	private DBOperate dbOperate;
	
	//构造函数
	public GameOperate ()
	{
		dbOperate = new DBOperate ();
	}
	
	/*
	 * 判断该游戏是否已注册到本平台
	 * 参数：游戏名称gameName
	 * 返回值：该游戏已注册返回1，否则返回0
	 */
	public int isRegister (String gameName)
	{
		//错误信息
		String errMsg = "";
		
		String strQuery = "select * from GameInfo where name = '" + gameName + "'" ;
		java.sql.ResultSet rs = dbOperate.DBQuery(strQuery);
		
		try
		{
			if (rs.first())
			{
				return 1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
		errMsg = "游戏 " + gameName + " 不存在";
		System.out.println (errMsg);
		return 0;
	}
	
	/*
	 * 注册游戏到本平台
	 * 参数：游戏名称gameName
	 * 返回值：注册成功返回1，否则返回0
	 * （如果游戏本来已注册，返回1）
	 */
	public int regGame (String gameName)
	{
		//错误信息
		String errMsg = "";
		
		if (isRegister (gameName) == 1)
		{
			errMsg = "游戏 " + gameName + " 已注册";
			System.out.println (errMsg);
			return 1;
		}
			
		//将游戏信息注册到系统总表中
		int gameID = ++ gameNum;
		String strInsert = "insert into GameInfo(id, name) values('" + 
				gameID + "', '" + gameName + "')";
		dbOperate.DBUpdate(strInsert);
			
		//为注册的游戏建立Info表格，用来存储游戏玩家的信息
		//为注册的游戏建立Online表格，用来存储在线玩家的信息
		if ( dbOperate.CreateInfoTbl(gameName) ==1 &&
				dbOperate.CreateOnlineTbl(gameName) == 1)
		{
			System.out.println(gameName + "游戏注册成功！");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * 注销游戏在本平台的注册
	 * 参数：游戏名称gameName
	 * 返回值：注销成功返回1，否则返回0
	 * （如果游戏原来未注册，返回1）
	 */
	public int unRegGame (String gameName)
	{
		//错误信息
		String errMsg = "";
				
		if (isRegister (gameName) == 0)
		{
			errMsg = "游戏 " + gameName + " 未注册";
			System.out.println (errMsg);
			return 0;
		}
		
		//删除该游戏在总表中的注册		
		String strDelete = "delete from GameInfo where name = '" + gameName + "'";
		dbOperate.DBUpdate(strDelete);
					
		//删除该游戏的Info表
		//删除该游戏的Online表
		if (dbOperate.DropInfoTbl(gameName) == 1 && 
				dbOperate.DropOnlineTbl(gameName) == 1)
		{
			gameNum -- ;
			System.out.println(gameName + "游戏注销成功");
			return 1;
		}
		return 0;
	}
	
	/*
	 * 获取所有注册游戏集合
	 * 参数：
	 * 返回值：所有注册游戏的名称列表
	 */
	public List<String> GetGameNameList ()
	{
		List<String> gameNameList = new ArrayList<String> ();
		String strQuery = "select * from GameInfo";
		java.sql.ResultSet rs = dbOperate.DBQuery(strQuery);
		
		try
		{
			while (rs.next())
			{
				String gameName =  rs.getString("name");
				gameNameList.add(gameName);
			}
			
			return gameNameList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
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
