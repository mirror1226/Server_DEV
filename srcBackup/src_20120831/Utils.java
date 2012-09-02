import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils 
{
	
	public static String getIPAdr (String IPAdr)
	{
		//结果IP
		String resIPAdr = "";
		boolean isIPAdr = false;
		
		//构建IP地址的正则表达式
		String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "("+regex0+")|("+regex1+")|("+regex2+")|("+regex3+")";
		regex = "("+regex+").("+regex+").("+regex+").("+regex+")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(IPAdr);
		
		//找出所输入字符串中是否包含有IP地址的子串
		while (m.find()) 
		{
			int startIndex = m.start(); // index of start
			int endIndex = m.end(); // index of end + 1

			resIPAdr = IPAdr.substring(startIndex, endIndex);
		}
		
		//如果可以转换为IP地址，则返回该IP地址，否则返回null
		if (!resIPAdr.equals(""))
		{
			return resIPAdr;
		}
		else
		{
			return null;
		}
	}

}
