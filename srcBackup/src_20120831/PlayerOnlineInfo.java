
public class PlayerOnlineInfo {
	
	//���״̬ȡֵ
	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int READY = 3;
	private static final int PLAYING = 4;
	
	//�������
	private String username;
	
	//���IP��ַ
	private String IPAdr;
	
	//���״̬
	private int status;
	
	//���λ��
	private double latitude;
	private double longtitude;
	
	/*
	 * ���캯��
	 */
	public PlayerOnlineInfo (String playerName, String IP)
	{
		username = playerName;
		IPAdr = IP;
		status = ONLINE;
		latitude = -1;
		longtitude = -1;
	}
	
	public PlayerOnlineInfo (String playerName, String IP, int lat, int lot)
	{
		username = playerName;
		IPAdr = IP;
		status = ONLINE;
		latitude = lat;
		longtitude = lot;
	}
	
	public PlayerOnlineInfo (String playerName, String IP, int playerStatus, double lat, double lot)
	{
		username = playerName;
		IPAdr = IP;
		status = playerStatus;
		latitude = lat;
		longtitude = lot;
	}
	
	/*
	 * ��ȡ�������
	 * ��������
	 * ����ֵ���������
	 */
	public String GetName ()
	{
		return username;
	}
	
	/*
	 * �����������λ��
	 * �������������γ�� lat��������ھ��� lot
	 * ����ֵ����
	 */
	public void SetLocation (int lat, int lot)
	{
		latitude = lat;
		longtitude = lot;
	}
	
	/*
	 * ��ȡ���IP��ַ
	 * ��������
	 * ����ֵ�����IP��ַ
	 */
	public String GetIPAdr ()
	{
		return IPAdr;
	}
	
	/*
	 * ��ȡ�������γ��
	 * ������
	 * ����ֵ���������γ��
	 */
	public double GetLatitude ()
	{
		return latitude;
	}
	
	
	/*
	 * ��ȡ������ھ���
	 * ������
	 * ����ֵ��������ھ���
	 */
	public double GetLongtitude ()
	{
		return longtitude;
	}
	
	/*
	 * ��ȡ���״̬
	 * ��������
	 * ����ֵ�����״̬
	 */
	public int GetStatus ()
	{
		return status;
	}
	
}
