package net.sourceforge.bochsui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {
    public static boolean useSb16 = false;
    public static boolean useEs1370 = false;
    public static boolean useNe2000 = false;
    public static boolean useRtl8029 = false;
    public static boolean useE1000 = false;
    public static boolean useVoodoo = false;
    public static boolean floppyA = false;
    public static boolean floppyB = false;
    public static String floppyA_image = "floppyA.img";
    public static String floppyB_image = "floppyB.img";
    public static boolean ata0m = false;
    public static boolean ata0s = false;
    public static boolean ata1m = false;
    public static boolean ata1s = false;
    public static boolean ata2m = false;
    public static boolean ata2s = false;
    public static boolean ata3m = false;
    public static boolean ata3s = false;
    public static String ata0m_image = "ata0-master.img";
    public static String ata0s_image = "ata0-slave.img";
    public static String ata1m_image = "ata1-master.img";
    public static String ata1s_image = "ata1-slave.img";
    public static String ata0mType = "disk";
    public static String ata0sType = "disk";
    public static String ata1mType = "disk";
    public static String ata1sType = "disk";
    public static String ata0mMode = "";
    public static String ata0sMode = "";
    public static String ata1mMode = "";
    public static String ata1sMode = "";
    public static String boot = "disk";

    public static String romImage = "BIOS-bochs-latest";
    public static String vgaRomImage = "VGABIOS-lgpl-latest-cirrus";
    public static int megs = 32;
    public static String vgaExtension = "cirrus";
    public static int vgaUpdateFreq = 15;
    public static String chipset = "i440fx";
    public static String[] slot = {"", "", "", "", ""};
    public static String cpuModel = "bx_generic";
    public static String mac = "b0:c4:20:00:00:00";
    public static String ethmod = "slirp";
    public static boolean fullscreen = false;
    public static String clockSync = "none";

    final static String path = "/storage/sdcard0/Android/data/net.sourceforge.bochs/files/bochsrc.txt";
    static String configFile;
    static boolean configLoaded = false;

    public static void readConfig() throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file).useDelimiter("[\n]");
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            String str = sc.next() + "\n";
            if (str.startsWith("floppya:")) {
                floppyA = true;
                if (str.contains("1_44=")) {
                    String str2 = str.substring(str.indexOf("1_44="), str.length() - 1);
                    floppyA_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("image=")) {
                    String str2 = str.substring(str.indexOf("image="), str.length() - 1);
                    floppyA_image = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
            }

            if (str.startsWith("floppyb:")) {
                floppyB = true;
                if (str.contains("1_44=")) {
                    String str2 = str.substring(str.indexOf("1_44="), str.length() - 1);
                    floppyB_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("image=")) {
                    String str2 = str.substring(str.indexOf("image="), str.length() - 1);
                    floppyB_image = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
            }

            if (str.startsWith("ata0-master:")) {
                ata0m = true;
                if (str.contains("type=")) {
                    String str2 = str.substring(str.indexOf("type="), str.length() - 1);
                    ata0mType = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("mode=")) {
                    String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
                    ata0mMode = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("path=")) {
                    String str2 = str.substring(str.indexOf("path="), str.length() - 1);
                    ata0m_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                    ata0m_image = ata0m_image.replace("\"", "");
                }
            }

            if (str.startsWith("ata0-slave:")) {
                ata0s = true;
                if (str.contains("type=")) {
                    String str2 = str.substring(str.indexOf("type="), str.length() - 1);
                    ata0sType = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("mode=")) {
                    String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
                    ata0sMode = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("path=")) {
                    String str2 = str.substring(str.indexOf("path="), str.length() - 1);
                    ata0s_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                    ata0s_image = ata0s_image.replace("\"", "");
                }
            }

            if (str.startsWith("ata1-master:")) {
                ata1m = true;
                if (str.contains("type=")) {
                    String str2 = str.substring(str.indexOf("type="), str.length() - 1);
                    ata1mType = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("mode=")) {
                    String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
                    ata1mMode = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("path=")) {
                    String str2 = str.substring(str.indexOf("path="), str.length() - 1);
                    ata1m_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                    ata1m_image = ata1m_image.replace("\"", "");
                }
            }

            if (str.startsWith("ata1-slave:")) {
                ata1s = true;
                if (str.contains("type=")) {
                    String str2 = str.substring(str.indexOf("type="), str.length() - 1);
                    ata1sType = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("mode=")) {
                    String str2 = str.substring(str.indexOf("mode="), str.length() - 1);
                    ata1sMode = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
                if (str.contains("path=")) {
                    String str2 = str.substring(str.indexOf("path="), str.length() - 1);
                    ata1s_image = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                    ata1s_image = ata1s_image.replace("\"", "");
                }
            }

            if (str.startsWith("boot:")) {
                boot = str.substring(6, str.length() - 1);
            }

            if (str.startsWith("romimage:")) {
                if (str.contains("file=")) {
                    String str2 = str.substring(str.indexOf("file="), str.length() - 1);
                    romImage = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
            }

            if (str.startsWith("vgaromimage:")) {
                if (str.contains("file=")) {
                    String str2 = str.substring(str.indexOf("file="), str.length() - 1);
                    vgaRomImage = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
            }

            if (str.startsWith("vga:")) {
                if (str.contains("extension=")) {
                    String str2 = str.substring(str.indexOf("extension="), str.length() - 1);
                    vgaExtension = str2.contains(",") ?
                            str2.substring(10, str2.indexOf(",")) : str2.substring(10, str2.length());
                }
                if (str.contains("update_freq=")) {
                    String str2 = str.substring(str.indexOf("update_freq="), str.length() - 1);
                    vgaUpdateFreq = str2.contains(",") ?
                            Integer.parseInt(str2.substring(12, str2.indexOf(","))) : Integer.parseInt(str2.substring(12, str2.length()));
                }
            }

            if (str.startsWith("pci:")) {
                if (str.contains("chipset=")) {
                    String str2 = str.substring(str.indexOf("chipset="), str.length() - 1);
                    chipset = str2.contains(",") ?
                            str2.substring(8, str2.indexOf(",")) : str2.substring(8, str2.length());
                }
                if (str.contains("slot1=")) {
                    String str2 = str.substring(str.indexOf("slot1="), str.length() - 1);
                    slot[0] = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
                if (str.contains("slot2=")) {
                    String str2 = str.substring(str.indexOf("slot2="), str.length() - 1);
                    slot[1] = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
                if (str.contains("slot3=")) {
                    String str2 = str.substring(str.indexOf("slot3="), str.length() - 1);
                    slot[2] = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
                if (str.contains("slot4=")) {
                    String str2 = str.substring(str.indexOf("slot4="), str.length() - 1);
                    slot[3] = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
                if (str.contains("slot5=")) {
                    String str2 = str.substring(str.indexOf("slot5="), str.length() - 1);
                    slot[4] = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
            }

            if (str.startsWith("cpu:")) {
                if (str.contains("model=")) {
                    String str2 = str.substring(str.indexOf("model="), str.length() - 1);
                    cpuModel = str2.contains(",") ?
                            str2.substring(6, str2.indexOf(",")) : str2.substring(6, str2.length());
                }
            }

            if (str.startsWith("ne2k:")) {
                useNe2000 = true;
                if (str.contains("mac=")) {
                    String str2 = str.substring(str.indexOf("mac="), str.length() - 1);
                    mac = str2.contains(",") ?
                            str2.substring(4, str2.indexOf(",")) : str2.substring(4, str2.length());
                }
                if (str.contains("ethmod=")) {
                    String str2 = str.substring(str.indexOf("ethmod="), str.length() - 1);
                    ethmod = str2.contains(",") ?
                            str2.substring(7, str2.indexOf(",")) : str2.substring(7, str2.length());
                }
            }

            if (str.startsWith("e1000:")) {
                useE1000 = true;
                if (str.contains("mac=")) {
                    String str2 = str.substring(str.indexOf("mac="), str.length() - 1);
                    mac = str2.contains(",") ?
                            str2.substring(4, str2.indexOf(",")) : str2.substring(4, str2.length());
                }
                if (str.contains("ethmod=")) {
                    String str2 = str.substring(str.indexOf("ethmod="), str.length() - 1);
                    ethmod = str2.contains(",") ?
                            str2.substring(7, str2.indexOf(",")) : str2.substring(7, str2.length());
                }
            }

            if (str.startsWith("sb16:")) {
                useSb16 = true;
            }

            if (str.startsWith("es1370:")) {
                useEs1370 = true;
            }

            if (str.startsWith("voodoo:")) {
                useVoodoo = true;
            }

            if (str.startsWith("megs:")) {
                megs = Integer.parseInt(str.substring(6, str.length() - 1));
            }

            if (str.startsWith("display_library:")) {
                if (str.contains("options=")) {
                    String str2 = str.substring(str.indexOf("options="), str.length() - 1);
                    fullscreen = str2.contains("fullscreen");
                }
            }

            if (str.startsWith("clock:")) {
                if (str.contains("sync=")) {
                    String str2 = str.substring(str.indexOf("sync="), str.length() - 1);
                    clockSync = str2.contains(",") ?
                            str2.substring(5, str2.indexOf(",")) : str2.substring(5, str2.length());
                }
            }
            sb.append(str);
        }
        sc.close();
        configFile = sb.toString();
    }

    public static void writeConfig() throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file);
        if (fullscreen)
            fw.write("display_library: sdl, options=fullscreen\n");
        fw.write("romimage: file=" + romImage + "\n");
        fw.write("vgaromimage: file=" + vgaRomImage + "\n");
        fw.write("cpu: model=" + cpuModel + "\n");
        fw.write("vga: extension=" + vgaExtension + ", update_freq=" + vgaUpdateFreq + "\n");
        fw.write("pci: enabled=1, chipset=" + chipset);
        if (!slot[0].equals("")) {
            fw.write(", slot1=" + slot[0]);
        }
        if (!slot[1].equals("")) {
            fw.write(", slot2=" + slot[1]);
        }
        if (!slot[2].equals("")) {
            fw.write(", slot3=" + slot[2]);
        }
        if (!slot[3].equals("")) {
            fw.write(", slot4=" + slot[3]);
        }
        if (!slot[4].equals("")) {
            fw.write(", slot5=" + slot[4]);
        }
        fw.write("\n");
        if (useRtl8029)
            fw.write("ne2k: mac=" + mac + ", ethmod=" + ethmod + ", script=\"\"\n");
        else if (useNe2000)
            fw.write("ne2k: ioaddr=0x300, irq=10, mac=" + mac + ", ethmod=" + ethmod + ", script=\"\"\n");
        if (useE1000)
            fw.write("e1000: mac=" + mac + ", ethmod=" + ethmod + ", script=\"\"\n");
        if (floppyA) {
            fw.write("floppya: image=" + floppyA_image + ", status=inserted\n");
        }
        if (floppyB) {
            fw.write("floppyb: image=" + floppyB_image + ", status=inserted\n");
        }
        fw.write("ata0: enabled=1, ioaddr1=0x1f0, ioaddr2=0x3f0, irq=14\n");
        fw.write("ata1: enabled=1, ioaddr1=0x170, ioaddr2=0x370, irq=15\n");
        if (ata0m) {
            fw.write("ata0-master: type=" + ata0mType);
            if (ata0mType.equals("cdrom")) {
                fw.write(", status=inserted");
            }
            if (!ata0mMode.equals("")) {
                fw.write(", mode=" + ata0mMode);
            }
            fw.write(", path=\"" + ata0m_image + "\"\n");
        }
        if (ata0s) {
            fw.write("ata0-slave: type=" + ata0sType);
            if (ata0sType.equals("cdrom")) {
                fw.write(", status=inserted");
            }
            if (!ata0sMode.equals("")) {
                fw.write(", mode=" + ata0sMode);
            }
            fw.write(", path=\"" + ata0s_image + "\"\n");
        }
        if (ata1m) {
            fw.write("ata1-master: type=" + ata1mType);
            if (ata1mType.equals("cdrom")) {
                fw.write(", status=inserted");
            }
            if (!ata1mMode.equals("")) {
                fw.write(", mode=" + ata1mMode);
            }
            fw.write(", path=\"" + ata1m_image + "\"\n");
        }
        if (ata1s) {
            fw.write("ata1-slave: type=" + ata1sType);
            if (ata1sType.equals("cdrom")) {
                fw.write(", status=inserted");
            }
            if (!ata1sMode.equals("")) {
                fw.write(", mode=" + ata1sMode);
            }
            fw.write(", path=\"" + ata1s_image + "\"\n");
        }
        fw.write("boot: " + boot + "\n");
        fw.write("megs: " + megs + "\n");
        fw.write("sound: waveoutdrv=sdl\n");
        fw.write("speaker: enabled=1, mode=sound\n");
        if (useSb16)
            fw.write("sb16: wavemode=1, dmatimer=500000\n");
        if (useEs1370)
            fw.write("es1370: enabled=1\n");
        if (useVoodoo)
            fw.write("voodoo: enabled=1, model=voodoo1\n");
        fw.write("mouse: enabled=1\n");
        fw.write("clock: sync=" + clockSync + ", time0=local\n");
        fw.write("debug: action=ignore\n");
        fw.write("info: action=ignore\n");
        fw.write("error: action=ignore\n");
        fw.write("panic: action=report\n");
        fw.write("log: bochsout.txt\n");
        fw.close();
    }
}
