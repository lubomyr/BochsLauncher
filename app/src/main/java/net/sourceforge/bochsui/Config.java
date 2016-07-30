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
					if (str2.contains(","))
					{
						floppyA_image = str2.substring(5, str2.indexOf(","));
					} else {
						floppyA_image = str2.substring(5, str2.length());
					}
				}
			}
			
			if (str.startsWith("floppyb:"))
			{
				floppyB = true;
				if (str.contains("1_44="))
				{
					String str2 = str.substring(str.indexOf("1_44="), str.length() - 1);
					if (str2.contains(","))
					{
						floppyB_image = str2.substring(5, str2.indexOf(","));
					} else {
						floppyB_image = str2.substring(5, str2.length());
					}
				}
			}
			
			if (str.startsWith("ata0-master:")) {
				ata0m = true;
			}
			
			if (str.startsWith("ata0-slave:")) {
				ata0s = true;
			}
			
			if (str.startsWith("ata1-master:")) {
				ata1m = true;
			}

			if (str.startsWith("ata1-slave:")) {
				ata1s = true;
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
		fw.close();
	}
}
