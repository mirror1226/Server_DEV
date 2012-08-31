
public class PlayerInfo {
	
	
	//�������
	private String username;
	
	//�������
	private String password;
	
	//�����ϵ�绰
	private String phone;
	
	//�������
	private String Email;
	
	/*
	 * ���캯��
	 */
	public PlayerInfo (String playerName, String pwd)
	{
		username = playerName;
		password = pwd;
		phone = null;
		Email = null;
	}
	
	public PlayerInfo (String playerName, String pwd, String phone, String Email)
	{
		username = playerName;
		password = pwd;
		this.phone = phone;
		this.Email = Email;
	}
	
	/*
	 * ��ȡ�������
	 * ������
	 * ����ֵ�������������
	 */
	public String GetName ()
	{
		return username;
	}
	
	/*
	 * ��ȡ�������
	 * ������
	 * ����ֵ���������
	 */
	public String GetPassword ()
	{
		return password;
	}
	
	/*
	 * ��ȡ���Email
	 * ������
	 * ����ֵ���������
	 */
	public String GetEmail ()
	{
		return Email;
	}
	
	/*
	 * ���������ϵ�绰
	 * �����������ϵ�绰
	 * ����ֵ����
	 */
	public void SetPhone (String phone)
	{
		this.phone = phone;
	}
	
	/*
	 * ��ȡ�����ϵ�绰
	 * ������
	 * ����ֵ�����������ϵ�绰
	 */
	public String GetPhone ()
	{
		return phone;
	}
	
	/*
	 * ��ȡ���Email
	 * ������
	 * ����ֵ�����Email
	 */
	public String GetLongtitude ()
	{
		return Email;
	}
}
