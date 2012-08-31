import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBOperate {
	
	//���ݿ�root�û���
	private final String rootUserName = "root";
	
	//���ݿ�root�û�����
	private final String rootPassward = "chen88240620";
	
	//ע���������
	private final String strRegDriver = "com.mysql.jdbc.Driver";
	
	//�������ݿ����
	private final String strConDB = 
			"jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=GBK";
	
	//���ݿ����Ӷ���
	private java.sql.Connection conn;
	
	//���ݿ���ʽ����
	java.sql.Statement stmt;
	
	//���캯��
	public DBOperate ()
	{
		RegDriver ();
		conn = ConDB ();
		stmt = CreateStat ();
	}
	
	/*
	 * ע������
	 * ����ֵ���ɹ�����1��ʧ�ܷ���0
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
	 * �������ݿ�
	 * ����ֵ���ɹ����� java.sql.Connection ����ʧ�ܷ���null
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
	 * ��ȡ���ݿ���ʽ
	 * ������
	 * ����ֵ���ɹ��������ݿ���ʽ����ʧ�ܷ���null
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
	 * �������ݿ������������񡢲������ݡ��������ݡ�ɾ�����ݡ�ɾ�����
	 * ���������ݿ�������strUpdate
	 * ����ֵ���ɹ�����1��ʧ�ܷ���0
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
	 * ��ѯ���ݿ����
	 * ���������ݿ�������strQuery
	 * ����ֵ���ɹ����ز�ѯ��� java.sql.ResultSet ����ʧ�ܷ���null
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
	 *�������
	 *�������������tblName����������б�fields��������������б�types
	 *���أ������ɹ�����1�����򷵻�0
	 */
	public int CreateTbl (String tblName, List<String> fields, List<String> types)
	{
		if (fields.size() != types.size())
		{
			return 0;
		}
		
		//�����������sql���
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
		
		//ִ��sql���
		DBUpdate(strCreateTbl);
		return 1;
	}
	
	/*
	 *ɾ�����
	 *�������������tblName
	 *���أ�ɾ���ɹ�����1�����򷵻�0
	 */
	public int DropTbl (String tblName)
	{
		String strDropTbl = "drop table " + tblName ;
		DBUpdate(strDropTbl);
		return 1;
	}
	
	/*
	 *ɾ����ϷInfo���
	 *��������Ϸ����tblName
	 *���أ�ɾ���ɹ�����1�����򷵻�0
	 */
	public int DropInfoTbl (String gameName)
	{
		return DropTbl (gameName + "_Info");
	}
	
	/*
	 *ɾ����ϷOnline���
	 *��������Ϸ����tblName
	 *���أ�ɾ���ɹ�����1�����򷵻�0
	 */
	public int DropOnlineTbl (String gameName)
	{
		return DropTbl (gameName + "_Online");
	}
	
	/*
	 *ɾ�����Message���
	 *��������Ϸ����gameName���������playerName
	 *���أ�ɾ���ɹ�����1�����򷵻�0
	 */
	public int DropMessageTbl (String gameName, String playerName)
	{
		return DropTbl (gameName + "_" + playerName + "_Message");
	}
	
	/*
	 *ɾ�����Friend���
	 *��������Ϸ����gameName���������playerName
	 *���أ�ɾ���ɹ�����1�����򷵻�0
	 */
	public int DropFriendTbl (String gameName, String playerName)
	{
		return DropTbl (gameName + "_" + playerName + "_Friend");
	}
	
	/*
	 * ��������¼
	 * �������������tblName����������б�fields����������ֵ�б�values
	 * ���أ�����ɹ�����1�����򷵻�0
	 */
	public int InsertClause (String tblName, List<String> fields, List<String> values)
	{
		if (fields.size() != values.size())
		{
			return 0;
		}
		
		//����sql���
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
		
		//ִ��sql���
		DBUpdate(strInsert);
		return 1;
	}
	
	/*
	 * ������ϷInfo���
	 * ��������Ϸ����gameName
	 * ����ֵ�������ɹ�����1�����򷵻�0
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
	 * ������ϷOnline���
	 * ��������Ϸ����gameName
	 * ����ֵ�������ɹ�����1�����򷵻�0
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
	 * �������Message���
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ�������ɹ�����1�����򷵻�0
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
	 * �������Friend���
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ�������ɹ�����1�����򷵻�0
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
	 * ��Online�������¼
	 * ��������Ϸ����gameName����������ֵplayerOnlineInfo
	 * ���أ�����ɹ�����1�����򷵻�0
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
	 * ��Info�������¼
	 * ��������Ϸ����gameName����������ֵplayerInfo
	 * ���أ�����ɹ�����1�����򷵻�0
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
	 * ��Message�������¼
	 * �����������ϷgameName���������playerName����������ֵmessageInfo
	 * ���أ�����ɹ�����1�����򷵻�0
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
	 * ��Friend�������¼
	 * �����������ϷgameName���������playerName����������ֵfriendInfo
	 * ���أ�����ɹ�����1�����򷵻�0
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
	 * ɾ������һ������
	 * �������������tblName������������fieldName������ֵvalue
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
	 */
	public int deleteSingleClause (String tblName, String fieldName,
			String value)
	{
		String strDelete = "delete from " + tblName + " where " + fieldName + " = " + value;
		return DBUpdate(strDelete);
		
	}
	
	/*
	 * ɾ���������ݣ�������ͬʱ����
	 * �������������tblName���������б�fields������ֵ�б�values
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
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
	 * ɾ��Info����е�һ�����ݣ���usernameΪ������
	 * ��������Ϸ����gameName������ֵvalue
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
	 */
	public int deleteSingleInfoClause (String gameName, String value)
	{
		return deleteSingleClause (gameName + "_Info", "username", "'" + value + "'");
	}
	
	/*
	 * ɾ��Online����е�һ�����ݣ���usernameΪ������
	 * ��������Ϸ����gameName������ֵvalue
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
	 */
	public int deleteSingleOnlineClause (String gameName, String value)
	{
		return deleteSingleClause (gameName + "_Online", "username", "'" + value + "'");
	}
	
	/*
	 * ɾ��Friend����е�һ�����ݣ���nameΪ������
	 * ��������Ϸ����gameName���������playerName������ֵvalue
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
	 */
	public int deleteSingleFriendClause(String gameName, String playerName,
			String value) {
		return deleteSingleClause(gameName + "_" + playerName + "_Friend",
				"name", "'" + value + "'");
	}
	
	/*
	 * ɾ��Message����е�һ������������type��srcNameΪ������
	 * ��������Ϸ����gameName���������playerName������ֵvalue
	 * ����ֵ��ɾ���ɹ�����1�����򷵻�0
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
	 * �����������
	 * ����ֵ���ɹ��ͷ����ݿ����Ӷ�������ݿ���ʽ���󷵻�1�����򷵻�0
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
	 * �ͷ����ݿ���ʽ
	 * ���������ݿ���ʽ����stmt
	 * ����ֵ���ɹ�����1��ʧ�ܷ���0
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
	 * �ͷ����ݿ�����
	 * ������
	 * ����ֵ���ɹ�����1��ʧ�ܷ���0
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
