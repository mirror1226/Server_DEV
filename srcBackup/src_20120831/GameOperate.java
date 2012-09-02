import java.util.ArrayList;
import java.util.List;


public class GameOperate {
	
	//�洢������Ϸ��Ϣ�ı������
	private final String gameTblName = "GameInfo";
	
	//��ע����Ϸ����
	private static int gameNum = 0;
	
	//���ݿ��������
	private DBOperate dbOperate;
	
	//���캯��
	public GameOperate ()
	{
		dbOperate = new DBOperate ();
	}
	
	/*
	 * �жϸ���Ϸ�Ƿ���ע�ᵽ��ƽ̨
	 * ��������Ϸ����gameName
	 * ����ֵ������Ϸ��ע�᷵��1�����򷵻�0
	 */
	public int isRegister (String gameName)
	{
		//������Ϣ
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
		
		errMsg = "��Ϸ " + gameName + " ������";
		System.out.println (errMsg);
		return 0;
	}
	
	/*
	 * ע����Ϸ����ƽ̨
	 * ��������Ϸ����gameName
	 * ����ֵ��ע��ɹ�����1�����򷵻�0
	 * �������Ϸ������ע�ᣬ����1��
	 */
	public int regGame (String gameName)
	{
		//������Ϣ
		String errMsg = "";
		
		if (isRegister (gameName) == 1)
		{
			errMsg = "��Ϸ " + gameName + " ��ע��";
			System.out.println (errMsg);
			return 1;
		}
			
		//����Ϸ��Ϣע�ᵽϵͳ�ܱ���
		int gameID = ++ gameNum;
		String strInsert = "insert into GameInfo(id, name) values('" + 
				gameID + "', '" + gameName + "')";
		dbOperate.DBUpdate(strInsert);
			
		//Ϊע�����Ϸ����Info��������洢��Ϸ��ҵ���Ϣ
		//Ϊע�����Ϸ����Online��������洢������ҵ���Ϣ
		if ( dbOperate.CreateInfoTbl(gameName) ==1 &&
				dbOperate.CreateOnlineTbl(gameName) == 1)
		{
			System.out.println(gameName + "��Ϸע��ɹ���");
			return 1;
		}
		
		return 0;
	}
	
	/*
	 * ע����Ϸ�ڱ�ƽ̨��ע��
	 * ��������Ϸ����gameName
	 * ����ֵ��ע���ɹ�����1�����򷵻�0
	 * �������Ϸԭ��δע�ᣬ����1��
	 */
	public int unRegGame (String gameName)
	{
		//������Ϣ
		String errMsg = "";
				
		if (isRegister (gameName) == 0)
		{
			errMsg = "��Ϸ " + gameName + " δע��";
			System.out.println (errMsg);
			return 0;
		}
		
		//ɾ������Ϸ���ܱ��е�ע��		
		String strDelete = "delete from GameInfo where name = '" + gameName + "'";
		dbOperate.DBUpdate(strDelete);
					
		//ɾ������Ϸ��Info��
		//ɾ������Ϸ��Online��
		if (dbOperate.DropInfoTbl(gameName) == 1 && 
				dbOperate.DropOnlineTbl(gameName) == 1)
		{
			gameNum -- ;
			System.out.println(gameName + "��Ϸע���ɹ�");
			return 1;
		}
		return 0;
	}
	
	/*
	 * ��ȡ����ע����Ϸ����
	 * ������
	 * ����ֵ������ע����Ϸ�������б�
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
	 * �����������
	 * ����ֵ����Դ�ɹ��ͷŷ���1�����򷵻�0
	 */
	public int Finish ()
	{
		return dbOperate.Finish();
	}
}
