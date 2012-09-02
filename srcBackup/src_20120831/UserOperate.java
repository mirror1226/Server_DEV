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

		// ��������Ϸ�Ƿ���ע��
		if (isPlayerReg(gameName, playerName) == 0) {
			errMsg = "��� " + playerName + " δ����";
			System.out.println(errMsg);
			return 0;
		}

		String strQueryPlayer = "select * from " + gameName + "_Online"
				+ " where username = '" + playerName + "'";
		java.sql.ResultSet rsPlayer = dbOperate.DBQuery(strQueryPlayer);

		// ����������Ƿ���ע��
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
	public int Check (String gameName, String playerName, 
			double latitude, double longtitude)
	{
		//��Ҳ�����
		if (isPlayerOnline (gameName, playerName) == 0) return 0;
		
		//��ұ�������λ��
		String tblName = gameName +  "_Online";
		String strUpdate = "update " + tblName + " set latitude =" + latitude + 
				", longtitude=" + longtitude + " where username='" + playerName + "'";
		if (dbOperate.DBUpdate(strUpdate) == 1)
		{
			System.out.println (playerName + "��γ�ȣ�" + latitude + "�����ȣ�" + longtitude);
			return 1;
		}
		return 0;
	}
	
	/*
	 * ����Ӻ���
	 * ��������Ϸ����gameName�������������playerNameReq��
	 * 			 �������������playerNameRec
	 * ����ֵ���ɹ��������󷵻�1�����򷵻�0
	 */
	public int RepForFriend (String gameName, String playerNameReq,
			String playerNameRec)
	{
		//���δע��
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ,
				playerNameReq);
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
			String playerNameRec)
	{
		//���δע��
		if (isPlayerReg(gameName, playerNameReq) == 0 || 
				isPlayerReg (gameName, playerNameRec) == 0)
			return 0;
		
		//�������������Ҽ�������б�
		FriendInfo friendInfo = new FriendInfo (playerNameReq);
		if (dbOperate.InsertFriendClause(gameName, playerNameRec, friendInfo) != 1)
			return 0;
		
		//����Ϣ�б��е�������Ϣɾ��
		MessageInfo messageInfo = new MessageInfo (MessageInfo.FRIENDREQ,
				playerNameReq);
		if (dbOperate.deleteMessageFriendReqClause
				(gameName, playerNameRec, messageInfo) != 1)
			return 0;
		
		//��ͬ����Ӻ��ѵ���Ϣ���뵽�����������ҵ���Ϣ�б�
		messageInfo = new MessageInfo (MessageInfo.FRIENDACK,
				playerNameRec);
		if (dbOperate.InsertMessageClause(gameName, playerNameReq, messageInfo) != 1)
			return 0;
		
		
		System.out.println (playerNameRec + "ͬ�����" + playerNameReq + "Ϊ���ѣ�");
		return 1;
		
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
