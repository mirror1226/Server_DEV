
public class PlayerInfo {
	
	
	//玩家名称
	private String username;
	
	//玩家密码
	private String password;
	
	//玩家联系电话
	private String phone;
	
	//玩家邮箱
	private String Email;
	
	/*
	 * 构造函数
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
	 * 获取玩家名称
	 * 参数：
	 * 返回值：玩家所在名称
	 */
	public String GetName ()
	{
		return username;
	}
	
	/*
	 * 获取玩家密码
	 * 参数：
	 * 返回值：玩家密码
	 */
	public String GetPassword ()
	{
		return password;
	}
	
	/*
	 * 获取玩家Email
	 * 参数：
	 * 返回值：玩家密码
	 */
	public String GetEmail ()
	{
		return Email;
	}
	
	/*
	 * 设置玩家联系电话
	 * 参数：玩家联系电话
	 * 返回值：无
	 */
	public void SetPhone (String phone)
	{
		this.phone = phone;
	}
	
	/*
	 * 获取玩家联系电话
	 * 参数：
	 * 返回值：玩家所在联系电话
	 */
	public String GetPhone ()
	{
		return phone;
	}
	
	/*
	 * 获取玩家Email
	 * 参数：
	 * 返回值：玩家Email
	 */
	public String GetLongtitude ()
	{
		return Email;
	}
}
