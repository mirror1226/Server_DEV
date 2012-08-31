import java.util.Date;


public class MessageInfo {
	
	//消息类型枚举
	public static int FRIENDREQ = 1;
	public static int 	FRIENDACK = 2;
	public static int FRIENDREF = 3;
	
	//消息类型
	private int type;
	
	//消息发送时间
	private Date date;
	
	//消息来源
	private String srcName;
	
	//消息内容
	private String content;
	
	/*
	 * 构造函数
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
	 * 获取消息类型
	 * 参数：无
	 * 返回值：消息类型
	 */
	public int GetType ()
	{
		return type;
	}
	
	/*
	 * 获取消息内容
	 * 参数：无
	 * 返回值：消息内容
	 */
	public String GetContent ()
	{
		return content;
	}
	
	/*
	 * 获取消息日期
	 * 参数：无
	 * 返回值：消息日期
	 */
	public Date GetDate ()
	{
		return date;
	}
	
	/*
	 * 获取消息发送者名称
	 * 参数：无
	 * 返回值：消息发送者名称
	 */
	public String GetSrcName ()
	{
		return srcName;
	}
}
