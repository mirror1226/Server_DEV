import java.util.Date;


public class MessageInfo {
	
	//��Ϣ����ö��
	public static int FRIENDREQ = 1;
	public static int 	FRIENDACK = 2;
	public static int FRIENDREF = 3;
	
	//��Ϣ����
	private int type;
	
	//��Ϣ����ʱ��
	private Date date;
	
	//��Ϣ��Դ
	private String srcName;
	
	//��Ϣ����
	private String content;
	
	/*
	 * ���캯��
	 */
	public MessageInfo (int type, Date date, String srcName, String content)
	{
		this.type = type;
		this.date = date;
		this.srcName = srcName;
		this.content = content;
	}
	
	public MessageInfo (int type, String srcName)
	{
		this.type = type;
		this.srcName = srcName;
		date = new Date ();
		content = "";
	}
	
	public MessageInfo (int type, String srcName, String content)
	{
		this.type = type;
		this.srcName = srcName;
		date = new Date ();
		this.content = content;
	}

	/*
	 * ��ȡ��Ϣ����
	 * ��������
	 * ����ֵ����Ϣ����
	 */
	public int GetType ()
	{
		return type;
	}
	
	/*
	 * ��ȡ��Ϣ����
	 * ��������
	 * ����ֵ����Ϣ����
	 */
	public String GetContent ()
	{
		return content;
	}
	
	/*
	 * ��ȡ��Ϣ����
	 * ��������
	 * ����ֵ����Ϣ����
	 */
	public Date GetDate ()
	{
		return date;
	}
	
	/*
	 * ��ȡ��Ϣ����������
	 * ��������
	 * ����ֵ����Ϣ����������
	 */
	public String GetSrcName ()
	{
		return srcName;
	}
}
