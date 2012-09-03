import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserOperate {
	
	//���ݿ��������
	private DBOperate dbOperate;
	
	/*
	 * ���캯��
	 */
	public UserOperate ()
	{
		dbOperate =  new DBOperate ();
	}
	
	/*
	 * �ж���Ϸ����Ƿ���ע��
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ�������ע�᷵��1�����򷵻�0
	 */
	public int isPlayerReg (String gameName, String playerName)
	{
		//������ʾ
		String errMsg = "";
		
		//��������Ϸ�Ƿ���ע��
		GameOperate gOperate = new GameOperate ();
		if (gOperate.isRegister(gameName) == 0)
		{
			errMsg = "��Ϸ " + gameName + " ������";
			System.out.println(errMsg);
			gOperate.Finish();
			return 0;
		}
		
			
		String strQueryPlayer = "select * from " + gameName + "_Info"
				+ " where username = '" + playerName + "'" ;
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);
				
		//����������Ƿ���ע��
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
		
		errMsg = "��� " + playerName + " ������";
		System.out.println(errMsg);
		return 0;
	}
	
	/*
	 * ��Ϸ���ע��
	 * ��������Ϸ����gameName�����id playerID���������playerName
	 * ����ֵ����ҳɹ�ע�᷵��1�����򷵻�0
	 */
	public int RegPlayer (String gameName, PlayerInfo playerInfo)
	{
		// ������ʾ
		String errMsg = "";
		String playerName = playerInfo.GetName();

		//����������Ƿ���ע��
		if (isPlayerReg (gameName, playerName) == 1)
		{
			errMsg = "��� " + playerName + " ��ע��";
			System.out.println(errMsg);
			return 0;
 		}
		
		//ע�����
		if (dbOperate.InsertInfoClause(gameName, playerInfo) == 1 && 
				dbOperate.CreateMessageTbl(gameName, playerName) == 1 && 
				dbOperate.CreateFriendTbl(gameName, playerName) == 1) 
		{
			System.out.println (playerName + "���ע��ɹ�");
			return 1;
		}
			
		
		return 0;
	}
	
	/*
	 * ��Ϸ���ע��
	 * ��������Ϸ����gameName�����id playerID���������playerName
	 * ����ֵ����ҳɹ�ע������1�����򷵻�0
	 */
	public int unRegPlayer (String gameName, String playerName)
	{
		// ������ʾ
		String errMsg = "";

		//����������Ƿ���ע��
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "��� " + playerName + " δע��";
			System.out.println(errMsg);
			return 0;
		}
		
		//ɾ����ҵ�ע���¼
		if (dbOperate.deleteSingleInfoClause(gameName, playerName) == 1 &&
				dbOperate.DropMessageTbl(gameName, playerName) == 1 &&
				dbOperate.DropFriendTbl(gameName, playerName) == 1) 
		{
			System.out.println (playerName + "���ע���ɹ�");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * ��Ϸ��ҵ�½
	 * ��������Ϸ���� gameName��������� playerName��
	 *          ������� playerPwd����Ϸ������ gameMgr
	 * ����ֵ����½�ɹ��򷵻�1�����򷵻�0
	 */
	public int Login (String gameName, String playerPwd, PlayerOnlineInfo playerOnlineInfo)
	{
		String errMsg = "";
		
		String playerName = playerOnlineInfo.GetName();
		
		//������δע�ᣬ����0
		if (isPlayerReg ( gameName, playerName) == 0) 
		{
			errMsg = "��� " + playerName + " δע��";
			System.out.println (errMsg);
			return 0;
		}
		
		//��֤�û�������
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
		
		//������󣬷���0
		if (!pwd.equalsIgnoreCase(playerPwd))
		{
			errMsg = "�������";
			System.out.println(errMsg);
			return 0;
		}
		
		//�ɹ���¼
		if (dbOperate.InsertOnlineClause(gameName, playerOnlineInfo) == 1)
		{
			System.out.println (playerName + "��ҵ�¼�ɹ���");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * �ж�����Ƿ�����
	 * ��������Ϸ����gameName��������� playerName
	 * ����ֵ�����������߷���1�����򷵻�0
	 */
	public int isPlayerOnline (String gameName, String playerName)
	{
		// ������ʾ
		String errMsg = "";

		// ����������Ƿ���ע��
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "��� " + playerName + " δ����";
			System.out.println(errMsg);
			return 0;
		}

		String strQueryPlayer = "select * from " + gameName + "_Online"
				+ " where username = '" + playerName + "'";
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);

		// ����������Ƿ�����
		try {
			if (rsPlayer.first()) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		errMsg = "��� " + playerName + " ������";
		System.out.println(errMsg);
		return 0;
	}
	
	/*
	 * ��Ϸ����˳�
	 * ��������Ϸ���� gameName��������� playerName��
	 *          ��Ϸ������ gameMgr
	 * ����ֵ����½�ɹ��򷵻�1�����򷵻�0
	 */
	public int Logout (String gameName, String playerName)
	{
		String errMsg = "";
				
		//�����Ҳ����ߣ�����0
		if (isPlayerOnline(gameName, playerName) == 0)
		{
			errMsg = "��� " + playerName + " ������";
			System.out.println (errMsg);
			return 0;
		}
		
		//��ҳɹ�����
		if ( dbOperate.deleteSingleOnlineClause(gameName, playerName) == 1)
		{
			System.out.println (playerName + "������߳ɹ���");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * �������������λ��
	 * ��������Ϸ����gameName���������playerName��
	 * 			 �������γ��latitude�������������longtitude
	 * ����ֵ������ɹ�����1�����򷵻�0
	 */
	public String Check (String gameName, String playerName, 
			double latitude, double longtitude)
	{
		//��Ҳ�����
		if (isPlayerOnline (gameName, playerName) == 0) return "0";
		
		//��ұ�������λ��
		String tblName = gameName +  "_Online";
		String strUpdate = "update " + tblName + " set latitude =" + latitude + 
				", longtitude=" + longtitude + " where username='" + playerName + "'";
		if (dbOperate.DBUpdate(strUpdate) == 1)
		{
			System.out.println (playerName + "��γ�ȣ�" + latitude + "�����ȣ�" + longtitude);
			return "1";
		}
		return "0";
	}
	
	/*
	 * ����Ӻ���
	 * ��������Ϸ����gameName�������������playerNameReq��
	 * 			 �������������playerNameRec
	 * ����ֵ���ɹ��������󷵻�1�����򷵻�0
	 */
	public int RepForFriend (String gameName, String playerNameReq,
			String playerNameRec, String msgContent)
	{
		//���δע��
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq, msgContent);
		if (dbOperate.InsertMessageClause(gameName, playerNameRec, messageInfo) == 1)
		{
			System.out.println (playerNameReq + "�ɹ���" + playerNameRec + "������������");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * ͬ��Ӻ���
	 * ��������Ϸ����gameName�������������playerNameReq��
	 * 			 �������������playerNameRec
	 * ����ֵ���ɹ��������󷵻�1�����򷵻�0
	 */
	public int AckForFriend (String gameName, String playerNameReq,
			String playerNameRec, String msgContent)
	{
		//���δע��
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//�������������Ҽ��뱻�������ҵĺ����б�
		FriendInfo friendInfo = new FriendInfo (playerNameReq);
		if (dbOperate.InsertFriendClause(gameName, playerNameRec, friendInfo) != 1)
			return 0;
		
		//�����������Ҽ��뷢���������ҵĺ����б�
		friendInfo = new FriendInfo (playerNameRec);
		if (dbOperate.InsertFriendClause(gameName, playerNameReq, friendInfo) != 1)
			return 0;
		
		//����Ϣ�б��е�������Ϣɾ��
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ,
				playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause
				(gameName, playerNameRec, messageInfo) != 1)
			return 0;
		
		//��ͬ����Ӻ��ѵ���Ϣ���뵽�����������ҵ���Ϣ�б�
		messageInfo = new MessageInfo (MessageInfo.FRIENDACK,
				playerNameRec, msgContent);
		if (dbOperate.InsertMessageClause(gameName, playerNameReq, messageInfo) != 1)
			return 0;
		
		
		System.out.println (playerNameRec + "ͬ�����" + playerNameReq + "Ϊ���ѣ�");
		return 1;
		
	}
	
	/*
	 * �ܾ���Ӻ���
	 * ��������Ϸ����gameName�������������playerNameReq��
	 * 			 �������������playerNameRec
	 * ����ֵ���ɹ��������󷵻�1�����򷵻�0
	 */
	public int RefForFriend (String gameName, String playerNameReq, String playerNameRec, String msgContent)
	{
		//���δע��
		if (isPlayerReg(gameName, playerNameReq) == 0 || isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//����Ϣ�б��е�������Ϣɾ��
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ, playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause(gameName, playerNameRec, messageInfo) != 1)
			return 0;
				
		//���ܾ���Ӻ��ѵ���Ϣ���뵽�����������ҵ���Ϣ�б�
		messageInfo = new MessageInfo (MessageInfo.FRIENDREF, playerNameRec, msgContent);
		if (dbOperate.InsertMessageClause(gameName, playerNameReq, messageInfo) != 1)
			return 0;
				
		System.out.println (playerNameRec + "�ܾ����" + playerNameReq + "Ϊ���ѣ�");
		return 1;	
	}
	
	/*
	 * �����ܱ��û�
	 * ��������Ϸ���ƣ��û�������γ��
	 * ����ֵ���ܱ��û����û����뾭γ�ȣ�����ֵ����0
	 * Example��Result,test1:1.1111 2.2222,test2:3.3333 4.444
	 */
	public String SearchNearby(String gameName, String playerName, 
			double latitude, double longtitude, double range)
	{
		//���δע��
		if (isPlayerOnline(gameName, playerName) == 0) return "0";

		//��ȡ���ݿ��м�¼���û�����γ�Ⱥ;���
		List<String> fields = new ArrayList<String>();
		fields.add("username");
		fields.add("latitude");
		fields.add("longtitude");
		List<String> tbls = new ArrayList<String>();
		tbls.add(gameName+ "_Online");
		java.sql.ResultSet rs = dbOperate.Query(tbls , fields, null);
		String result = "";
		
		//ɸѡ��¼
		try {
			while (rs.next())
			{
				double rs_lat = rs.getDouble("latitude");
				double rs_lng = rs.getDouble("longtitude");
				String rs_user = rs.getString("username");
				if (rs_user.compareTo(playerName) != 0 && Utils.distance(latitude, longtitude, rs_lat, rs_lng) <= range)	//�ڷ�Χ��
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
	 * ����id�����û�
	 * ��������Ϸ���ƣ��û��������ҹؼ���
	 * ����ֵ��������ؼ���ƥ����û���������ֵ����0
	 * Example��Result,test1,test2
	 */
	public String SearchPlayerByID(String gameName, String playerName, String key)
	{
		//���δע��
		if (isPlayerReg(gameName, playerName) == 0) 
			return "0";
				
		//��ѯ
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
	 * �������ߺ���
	 * ����ֵ���������ߺ��ѵ��û���������ֵ����0
	 * Example��Result,test1,test2
	 */
	public String ShowFriendsOnline(String gameName, String playerName)
	{
		//���δע��
		if (isPlayerReg(gameName, playerName) == 0) return "0";
		
		//��ѯ
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
	 * �����û�playerName�������Ϣ
	 * ����ֵ��������Ϣ���͡����ڡ������û�����Ϣ����
	 * Example��#REQ&2012-08-20&test1&friendship#ACK&2012-08-21&test2&hello world
	 */
	public String GetMessage(String gameName, String playerName) 
	{
		//���δע��
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
	 * ��ѯָ��������Ϣ
	 * ����ֵ���������֡��绰��email
	 * Example��#test1&123456&test1@sjtu.edu.cn
	 */
	public String GetFriendInfo(String gameName, String playerName, String friend) {
		
		//���δע��
		if (isPlayerReg(gameName, playerName) == 0) 
			return "0";
		
		//�ж�friend�Ƿ�ΪplayerName�ĺ���
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
	 * �ж�����Ƿ�Ϊ����
	 * ��������Ϸ����gameName���������playerName����������friend
	 * ����ֵ������Ƿ���1�����򷵻�0
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
	 * ���׼����Ϸ
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ����ҳɹ�����׼��״̬����1�����򷵻�0
	 */
	public int ReadyForGame(String gameName, String playerName)
	{
		//������Ϣ
		String errMsg = "";
	
		//��Ҳ�����
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//�ж����״̬
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
			//��Ҳ�����
			if(status.equals(String.valueOf(PlayerOnlineInfo.OFFLINE)))
			{
				errMsg = "���" + playerName + "������";
				System.out.println(errMsg);
				return 0;
			}
			//�������Ϸ��
			else if(status.equals(String.valueOf(PlayerOnlineInfo.PLAYING)))
			{
				errMsg = "���" + playerName + "������Ϸ��";
				System.out.println(errMsg);
				return 0;
			}
			//������߻���׼��
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.READY));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("���" + playerName + "��׼����Ϸ��");
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
	 * ���ȡ��׼����Ϸ
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ����ҳɹ�����׼��״̬����1�����򷵻�0
	 */
	public int CancelReadyForGame(String gameName, String playerName)
	{
		//������Ϣ
		String errMsg = "";
	
		//��Ҳ�����
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//�ж����״̬
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
			//��Ҳ���׼��״̬
			if(!status.equals(String.valueOf(PlayerOnlineInfo.READY)))
			{
				errMsg = "���" + playerName + "����׼��״̬";
				System.out.println(errMsg);
				return 0;
			}
			//�����׼��
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.ONLINE));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("���" + playerName + "��ȡ��׼����");
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
	 * ��ҿ�ʼ��Ϸ
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ����ҳɹ�����׼��״̬����1�����򷵻�0
	 */
	public int StartForGame(String gameName, String playerName)
	{
		//������Ϣ
		String errMsg = "";
	
		//��Ҳ�����
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//�ж����״̬
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
			//��Ҳ���׼��״̬
			if(!status.equals(String.valueOf(PlayerOnlineInfo.READY)))
			{
				errMsg = "���" + playerName + "����׼��״̬";
				System.out.println(errMsg);
				return 0;
			}
			//�����׼��
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.PLAYING));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("���" + playerName + "�ѿ�ʼ��Ϸ��");
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
	 * ��ҽ�����Ϸ
	 * ��������Ϸ����gameName���������playerName
	 * ����ֵ����ҳɹ�����׼��״̬����1�����򷵻�0
	 */
	public int EndForGame(String gameName, String playerName)
	{
		//������Ϣ
		String errMsg = "";
	
		//��Ҳ�����
		if (isPlayerOnline(gameName, playerName) == 0) return 0;
		
		//�ж����״̬
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
			//��Ҳ���׼��״̬
			if(!status.equals(String.valueOf(PlayerOnlineInfo.PLAYING)))
			{
				errMsg = "���" + playerName + "������Ϸ��";
				System.out.println(errMsg);
				return 0;
			}
			//�������Ϸ��
			else
			{
				String tblName = gameName + "_Online";
				List<String> keyValues = new ArrayList<String>();
				keyValues.add("status = " + String.valueOf(PlayerOnlineInfo.ONLINE));
				whereClause = "username = '" + playerName + "'";
				if(dbOperate.Update(tblName, keyValues, whereClause) == 1)
				{
					System.out.println("���" + playerName + "�ѽ�����Ϸ��");
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
	 * �����������
	 * ����ֵ����Դ�ɹ��ͷŷ���1�����򷵻�0
	 */
	public int Finish ()
	{
		return dbOperate.Finish();
	}
}
