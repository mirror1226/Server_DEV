import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils 
{
	
	public static String getIPAdr (String IPAdr)
	{
		//���IP
		String resIPAdr = "";
		boolean isIPAdr = false;
		
		//����IP��ַ��������ʽ
		String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "("+regex0+")|("+regex1+")|("+regex2+")|("+regex3+")";
		regex = "("+regex+").("+regex+").("+regex+").("+regex+")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(IPAdr);
		
		//�ҳ��������ַ������Ƿ������IP��ַ���Ӵ�
		while (m.find()) 
		{
			int startIndex = m.start(); // index of start
			int endIndex = m.end(); // index of end + 1

			resIPAdr = IPAdr.substring(startIndex, endIndex);
		}
		
		//�������ת��ΪIP��ַ���򷵻ظ�IP��ַ�����򷵻�null
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
