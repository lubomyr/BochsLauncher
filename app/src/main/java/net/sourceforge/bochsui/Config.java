package net.sourceforge.bochsui;

import java.io.*;
import java.util.*;

public class Config
{
	public static boolean floppyA=false;
	public static boolean floppyB=false;
	public static String floppyA_image="floppyA.img";
	public static String floppyB_image="floppyB.img";
	public static boolean ata0m=false;
	public static boolean ata0s=false;
	public static boolean ata1m=false;
	public static boolean ata1s=false;
	public static String ata0m_image="ata0-master.img";
	public static String ata0s_image="ata0-slave.img";
	public static String ata1m_image="ata1-master.img";
	public static String ata1s_image="ata1-slave.img";
	public static String ata0mType="disk";
	public static String ata0sType="disk";
	public static String ata1mType="disk";
	public static String ata1sType="disk";
	public static String ata0mMode="";
	public static String ata0sMode="";
	public static String ata1mMode="";
	public static String ata1sMode="";
	public static String boot="disk";
	public static int megs=32;

	final static String path = "/storage/sdcard0/Android/data/net.sourceforge.bochs/files/bochsrc.txt";
	static String configFile;

	public static void readConfig() throws FileNotFoundException
	{
		File file = new File(path);
		Scanner sc = new Scanner(file).useDelimiter("[\n]");
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext())
		{
			String str = sc.next() + "\n";
			if (str.startsWith("floppya:"))
			{
				floppyA = true;
				if (str.contains("1_44="))
				{
					String str2 = str.substring(str.indexOf("1_44="), str.length() - 1);
					floppyA_image = str2.contains(",") ?
					    str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());			
				}
			}
			
			if (str.startsWith("floppyb:"))
			{
				floppyB = true;
				if (str.contains("1_44="))
				{
					String str2 = str.substring(str.indexOf("1_44="), str.length() - 1);
					floppyB_image = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());	
				}
			}
			
			if (str.startsWith("ata0-master:")) {
				ata0m = true;
				if (str.contains("type="))
				{
					String str2 = str.substring(str.indexOf("type="), str.length() - 1);
					ata0mType = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("mode="))
				{
					String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
					ata0mMode = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("path="))
				{
					String str2 = str.substring(str.indexOf("path="), str.length() - 1);
					ata0m_image = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
					ata0m_image = ata0m_image.replace("\"","");
				}
			}
			
			if (str.startsWith("ata0-slave:")) {
				ata0s = true;
				if (str.contains("type="))
				{
					String str2 = str.substring(str.indexOf("type="), str.length() - 1);
					ata0sType = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("mode="))
				{
					String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
					ata0sMode = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("path="))
				{
					String str2 = str.substring(str.indexOf("path="), str.length() - 1);
					ata0s_image = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
					ata0s_image = ata0s_image.replace("\"","");
				}
			}
			
			if (str.startsWith("ata1-master:")) {
				ata1m = true;
				if (str.contains("type="))
				{
					String str2 = str.substring(str.indexOf("type="), str.length() - 1);
					ata1mType = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("mode="))
				{
					String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
					ata1mMode = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("path="))
				{
					String str2 = str.substring(str.indexOf("path="), str.length() - 1);
					ata1m_image = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
					ata1m_image = ata1m_image.replace("\"","");
				}
			}

			if (str.startsWith("ata1-slave:")) {
				ata1s = true;
				if (str.contains("type="))
				{
					String str2 = str.substring(str.indexOf("type="), str.length() - 1);
					ata1sType = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("mode="))
				{
					String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
					ata1sMode = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
				}
				if (str.contains("path="))
				{
					String str2 = str.substring(str.indexOf("path="), str.length() - 1);
					ata1s_image = str2.contains(",") ?
						str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
					ata1s_image = ata1s_image.replace("\"","");
				}
			}
			
			if (str.startsWith("boot:")) {
				boot = str.substring(6, str.length());
			}
			
			if (str.startsWith("megs:"))
			{
				megs = Integer.parseInt(str.substring(6, str.length() - 1));
			}
			sb.append(str);
		}
		sc.close();
		configFile = sb.toString();
	}

	public static void writeConfig() throws IOException
	{
		File file = new File(path+".2");
		FileWriter fw = new FileWriter(file);
		if (floppyA) {
			fw.write("floppya: 1_44=" + floppyA_image + ", status=inserted\n");
		}
		if (floppyB) {
			fw.write("floppyb: 1_44=" + floppyB_image + ", status=inserted\n");
		}
		fw.write("ata0: enabled=1, ioaddr1=0x1f0, ioaddr2=0x3f0, irq=14\n");
		fw.write("ata1: enabled=1, ioaddr1=0x170, ioaddr2=0x370, irq=15\n");
		if (ata0m) {
			fw.write("ata0-master: type=" + ata0mType + ", mode=" + ata0mMode + ", path=\"" + ata0m_image + "\"\n");
		}
		if (ata0s) {
			fw.write("ata0-slave: type=" + ata0sType + ", mode=" + ata0sMode + ", path=\"" + ata0s_image + "\"\n");
		}
		if (ata1m) {
			fw.write("ata1-master: type=" + ata1mType + ", mode=" + ata1mMode + ", path=\"" + ata1m_image + "\"\n");
		}
		if (ata1s) {
			fw.write("ata1-slave: type=" + ata1sType + ", mode=" + ata1sMode + ", path=\"" + ata1s_image + "\"\n");
		}
		fw.write("boot: " + boot);
		fw.close();
	}
}
