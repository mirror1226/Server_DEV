import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBOperate {
	
	//数据库root用户名
	private final String rootUserName = "root";
	
	//数据库root用户密码
	private final String rootPassward = "chen88240620";
	
	//注册驱动语句
	private final String strRegDriver = "com.mysql.jdbc.Driver";
	
	//连接数据库语句
	private final String strConDB = 
			"jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=GBK";
	
	//数据库连接对象
	private java.sql.Connection conn;
	
	//数据库表达式对象
	java.sql.Statement stmt;
	
	//构造函数
	public DBOperate ()
	{
		RegDriver ();
		conn = ConDB ();
		stmt = CreateStat ();
	}
	
	/*
	 * 注册驱动
	 * 返回值：成功返回1，失败返回0
	 */
	private int RegDriver ()
	{
		try 
		{
			Class.forName(strRegDriver);
			return 1;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 连接数据库
	 * 返回值：成功返回 java.sql.Connection 对象，失败返回null
	 */
	private java.sql.Connection ConDB ()
	{
		try {
			java.sql.Connection conn = java.sql.DriverManager
					.getConnection (strConDB, rootUserName, rootPassward);
			if (conn != null) {
				return conn;
			} else {
				return null;
			}
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			return null;
		}
	}
	
	/*
	 * 获取数据库表达式
	 * 参数：
	 * 返回值：成功返回数据库表达式对象，失败返回null
	 */
	private java.sql.Statement CreateStat ()
	{
		try
		{
			java.sql.Statement stmt = conn.createStatement();
			return stmt;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * 更新数据库操作（创建表格、插入数据、更新数据、删除数据、删除表格）
	 * 参数：数据库執行语句strUpdate
	 * 返回值：成功返回1，失败返回0
	 */
	public int DBUpdate (String strUpdate)
	{
		try
		{
			stmt.executeUpdate(strUpdate);
			return 1;
		}
		catch (Exception ex) 
		{
			System.err.println(ex);
			return 0;
		}
	}
	
	/*
	 * 查询数据库操作
	 * 参数：数据库執行语句strQuery
	 * 返回值：成功返回查询结果 java.sql.ResultSet 对象，失败返回null
	 */
	public  java.sql.ResultSet DBQuery (	String strQuery)
	{
		try
		{
			java.sql.ResultSet rs = stmt.executeQuery(strQuery);
			return rs;
		}
		catch (Exception ex) 
		{
			System.err.println(ex);
			return null;
		}
	}

	/*
	 *创建表格
	 *参数：表格名称tblName，表格属性列表fields，表格属性类型列表types
	 *返回：创建成功返回1，否则返回0
	 */
	public int CreateTbl (String tblName, List<String> fields, List<String> types)
	{
		if (fields.size() != types.size())
		{
			return 0;
		}
		
		//构建创建表格sql语句
		String strCreateTbl = "create table " + tblName + " (";
		for (int i = 0; i < fields.size(); i ++)
		{
			String field = fields.get(i);
			String type = types.get(i);
			strCreateTbl += field + " " + type;
			if (i < fields.size() - 1)
			{
				strCreateTbl += ",";
			}
		}
		strCreateTbl += ")";
		
		//执行sql语句
		DBUpdate(strCreateTbl);
		return 1;
	}
	
	/*
	 *删除表格
	 *参数：表格名称tblName
	 *返回：删除成功返回1，否则返回0
	 */
	public int DropTbl (String tblName)
	{
		String strDropTbl = "drop table " + tblName ;
		DBUpdate(strDropTbl);
		return 1;
	}
	
	/*
	 *删除游戏Info表格
	 *参数：游戏名称tblName
	 *返回：删除成功返回1，否则返回0
	 */
	public int DropInfoTbl (String gameName)
	{
		return DropTbl (gameName + "_Info");
	}
	
	/*
	 *删除游戏Online表格
	 *参数：游戏名称tblName
	 *返回：删除成功返回1，否则返回0
	 */
	public int DropOnlineTbl (String gameName)
	{
		return DropTbl (gameName + "_Online");
	}
	
	/*
	 *删除玩家Message表格
	 *参数：游戏名称gameName，玩家名称playerName
	 *返回：删除成功返回1，否则返回0
	 */
	public int DropMessageTbl (String gameName, String playerName)
	{
		return DropTbl (gameName + "_" + playerName + "_Message");
	}
	
	/*
	 *删除玩家Friend表格
	 *参数：游戏名称gameName，玩家名称playerName
	 *返回：删除成功返回1，否则返回0
	 */
	public int DropFriendTbl (String gameName, String playerName)
	{
		return DropTbl (gameName + "_" + playerName + "_Friend");
	}
	
	/*
	 * 向表格插入记录
	 * 参数：表格名称tblName，表格属性列表fields，插入属性值列表values
	 * 返回：插入成功返回1，否则返回0
	 */
	public int InsertClause (String tblName, List<String> fields, List<String> values)
	{
		if (fields.size() != values.size())
		{
			return 0;
		}
		
		//构建sql语句
		String strInsert = "insert into " + tblName + "(";
		for (int i = 0; i < fields.size(); i ++)
		{
			String field = fields.get(i);
			strInsert += field;
			if (i < fields.size() - 1)
			{
				strInsert += ",";
			}
		}
		strInsert += ") values (";
		for (int i = 0; i < values.size(); i ++)
		{
			String value = values.get(i);
			strInsert += value;
			if (i < values.size() - 1)
			{
				strInsert += ",";
			}
		}
		strInsert += ")";
		
		//执行sql语句
		DBUpdate(strInsert);
		return 1;
	}
	
	/*
	 * 创建游戏Info表格
	 * 参数：游戏名称gameName
	 * 返回值：创建成功返回1，否则返回0
	 */
	public int CreateInfoTbl (String gameName)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("username");
		fields.add("password");
		fields.add("phone");
		fields.add("email");
		List<String> types = new ArrayList<String> ();
		types.add("varchar(20)");
		types.add("varchar(20)");
		types.add("varchar(20)");
		types.add("varchar(50)");
		
		return CreateTbl(gameName + "_Info", fields, types);
	}
	
	/*
	 * 创建游戏Online表格
	 * 参数：游戏名称gameName
	 * 返回值：创建成功返回1，否则返回0
	 */
	public int CreateOnlineTbl (String gameName)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("username");
		fields.add("ip");
		fields.add("status");
		fields.add("latitude");
		fields.add("longtitude");
		List<String> types = new ArrayList<String> ();
		types.add("varchar(20)");
		types.add("varchar(20)");
		types.add("varchar(5)");
		types.add("float");
		types.add("float");
		
		return CreateTbl(gameName + "_Online", fields, types);
	}
	
	/*
	 * 创建玩家Message表格
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：创建成功返回1，否则返回0
	 */
	public int CreateMessageTbl (String gameName, String playerName)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("type");
		fields.add("date");
		fields.add("srcName");
		fields.add("content");
		List<String> types = new ArrayList<String> ();
		types.add("varchar(5)");
		types.add("varchar(20)");
		types.add("varchar(20)");
		types.add("varchar(100)");
		
		return CreateTbl(gameName + "_" + playerName + "_Message", fields, types);
	}
	
	/*
	 * 创建玩家Friend表格
	 * 参数：游戏名称gameName，玩家名称playerName
	 * 返回值：创建成功返回1，否则返回0
	 */
	public int CreateFriendTbl (String gameName, String playerName)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("name");
		List<String> types = new ArrayList<String> ();
		types.add("varchar(20)");
		
		return CreateTbl(gameName + "_" + playerName + "_Friend", fields, types);
	}
	
	/*
	 * 向Online表格插入记录
	 * 参数：游戏名称gameName，插入属性值playerOnlineInfo
	 * 返回：插入成功返回1，否则返回0
	 */
	public int InsertOnlineClause (String gameName, PlayerOnlineInfo playerOnlineInfo)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("username");
		fields.add("ip");
		fields.add("status");
		fields.add("latitude");
		fields.add("longtitude");
		List<String> values = new ArrayList<String> ();
		values.add("'" + playerOnlineInfo.GetName() + "'");
		values.add("'" + playerOnlineInfo.GetIPAdr() + "'");
		values.add("'" + playerOnlineInfo.GetStatus() + "'");
		values.add(String.valueOf(playerOnlineInfo.GetLatitude()));
		values.add(String.valueOf(playerOnlineInfo.GetLongtitude()));
		
		return InsertClause (gameName + "_Online", fields, values);
	}
	
	/*
	 * 向Info表格插入记录
	 * 参数：游戏名称gameName，插入属性值playerInfo
	 * 返回：插入成功返回1，否则返回0
	 */
	public int InsertInfoClause (String gameName, PlayerInfo playerInfo)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("username");
		fields.add("password");
		fields.add("phone");
		fields.add("email");
		List<String> values = new ArrayList<String> ();
		values.add("'" + playerInfo.GetName() + "'");
		values.add("'" + playerInfo.GetPassword() + "'");
		values.add("'" + playerInfo.GetPhone() + "'");
		values.add("'" + playerInfo.GetEmail() + "'");
		
		return InsertClause (gameName + "_Info", fields, values);
	}
	
	/*
	 * 向Message表格插入记录
	 * 参数：表格游戏gameName，玩家名称playerName，插入属性值messageInfo
	 * 返回：插入成功返回1，否则返回0
	 */
	public int InsertMessageClause (String gameName, String playerName, 
			MessageInfo messageInfo)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("type");
		fields.add("date");
		fields.add("srcName");
		fields.add("content");
		List<String> values = new ArrayList<String> ();
		values.add("'" + messageInfo.GetType()+ "'");
		Date date = messageInfo.GetDate();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time=df.format(date);
		values.add("'" + time + "'");
		values.add("'" + messageInfo.GetSrcName() + "'");
		values.add("'" + messageInfo.GetContent() + "'");
		
		return InsertClause (gameName + "_" + playerName + "_Message", fields, values);
	}
	
	/*
	 * 向Friend表格插入记录
	 * 参数：表格游戏gameName，玩家名称playerName，插入属性值friendInfo
	 * 返回：插入成功返回1，否则返回0
	 */
	public int InsertFriendClause (String gameName, String playerName, 
			FriendInfo friendInfo)
	{
		List<String> fields = new ArrayList<String> ();
		fields.add("name");
		List<String> values = new ArrayList<String> ();
		values.add("'" + friendInfo.GetName()+ "'");
		
		return InsertClause (gameName + "_" + playerName + "_Friend", fields, values);
	}
	
	/*
	 * 删除表中一条数据
	 * 参数：表格名称tblName，条件域名称fieldName，条件值value
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteSingleClause (String tblName, String fieldName,
			String value)
	{
		String strDelete = "delete from " + tblName + " where " + fieldName + " = " + value;
		return DBUpdate(strDelete);
		
	}
	
	/*
	 * 删除表中数据，条件域同时满足
	 * 参数：表格名称tblName，条件域列表fields，条件值列表values
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteAndClause (String tblName, List<String> fields,
			List<String> values)
	{
		if (fields.size() != values.size())
			return 0;
		
		String strDelete = "delete from " + tblName + " where ";
		for (int i = 0; i < fields.size(); i ++)
		{
			String field = fields.get(i);
			String value = values.get(i);
			strDelete = strDelete + field + " = " + value ;
			if ( i < fields.size() - 1)
				strDelete += " and ";
		}
		
		return DBUpdate(strDelete);
	}
	
	/*
	 * 删除Info表格中的一条数据，以username为条件域
	 * 参数：游戏名称gameName，条件值value
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteSingleInfoClause (String gameName, String value)
	{
		return deleteSingleClause (gameName + "_Info", "username", "'" + value + "'");
	}
	
	/*
	 * 删除Online表格中的一条数据，以username为条件域
	 * 参数：游戏名称gameName，条件值value
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteSingleOnlineClause (String gameName, String value)
	{
		return deleteSingleClause (gameName + "_Online", "username", "'" + value + "'");
	}
	
	/*
	 * 删除Friend表格中的一条数据，以name为条件域
	 * 参数：游戏名称gameName，玩家名称playerName，条件值value
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteSingleFriendClause(String gameName, String playerName,
			String value) {
		return deleteSingleClause(gameName + "_" + playerName + "_Friend",
				"name", "'" + value + "'");
	}
	
	/*
	 * 删除Message表格中的一条好友请求，以type和srcName为条件域
	 * 参数：游戏名称gameName，玩家名称playerName，条件值value
	 * 返回值：删除成功返回1，否则返回0
	 */
	public int deleteMessageFriendReqClause(String gameName, String playerName, 
			MessageInfo messageInfo) {
		List<String> fields = new ArrayList<String> ();
		fields.add("type");
		fields.add("srcName");
		List<String> values = new ArrayList<String> ();
		values.add(String.valueOf( messageInfo.GetType()));
		values.add("'" + messageInfo.GetSrcName() + "'");
		
		return deleteAndClause (gameName + "_" + playerName + "_Message", 
				fields, values);
	}
	
	/*
	 * 对象结束处理
	 * 返回值：成功释放数据库连接对象和数据库表达式对象返回1，否则返回0
	 */
	public int Finish ()
	{
		if (ReleaseStmt () == 1 && ReleaseCon () == 1)
		{
			return 1;
		}
		return 0;
	}
	
	/*
	 * 释放数据库表达式
	 * 参数：数据库表达式对象stmt
	 * 返回值：成功返回1，失败返回0
	 */
	private int ReleaseStmt ()
	{
		try
		{
			stmt.close();
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * 释放数据库连接
	 * 参数：
	 * 返回值：成功返回1，失败返回0
	 */
	private int ReleaseCon ()
	{
		try
		{
			conn.close();
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
}
