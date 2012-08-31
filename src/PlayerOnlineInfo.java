
public class PlayerOnlineInfo {
	
	//玩家状态取值
	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int READY = 3;
	private static final int PLAYING = 4;
	
	//玩家名称
	private String username;
	
	//玩家IP地址
	private String IPAdr;
	
	//玩家状态
	private int status;
	
	//玩家位置
	private double latitude;
	private double longtitude;
	
	/*
	 * 构造函数
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
	 * 获取玩家名称
	 * 参数：无
	 * 返回值：玩家名称
	 */
	public String GetName ()
	{
		return username;
	}
	
	/*
	 * 设置玩家所在位置
	 * 参数：玩家所在纬度 lat，玩家所在经度 lot
	 * 返回值：无
	 */
	public void SetLocation (int lat, int lot)
	{
		latitude = lat;
		longtitude = lot;
	}
	
	/*
	 * 获取玩家IP地址
	 * 参数：无
	 * 返回值：玩家IP地址
	 */
	public String GetIPAdr ()
	{
		return IPAdr;
	}
	
	/*
	 * 获取玩家所在纬度
	 * 参数：
	 * 返回值：玩家所在纬度
	 */
	public double GetLatitude ()
	{
		return latitude;
	}
	
	
	/*
	 * 获取玩家所在经度
	 * 参数：
	 * 返回值：玩家所在经度
	 */
	public double GetLongtitude ()
	{
		return longtitude;
	}
	
	/*
	 * 获取玩家状态
	 * 参数：无
	 * 返回值：玩家状态
	 */
	public int GetStatus ()
	{
		return status;
	}
	
}
