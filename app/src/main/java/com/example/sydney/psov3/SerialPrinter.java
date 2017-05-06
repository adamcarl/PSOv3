package com.example.sydney.psov3;

/**
 * Created by sydney on 6/29/2016.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by sydney on 6/27/2016.
 */

import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;
import java.util.ArrayList;

public class SerialPrinter {
    private boolean mOpened = false;
    private Handler mExternHandler;
    private static SerialPrinter mSerialPrinter = null;
    private SerialPortOperaion mSerialPortOperaion = null;

    private SerialPrinter() {
    }

    public static SerialPrinter GetSerialPrinter() {
        if(mSerialPrinter == null) {
            mSerialPrinter = new SerialPrinter();
        }

        return mSerialPrinter;
    }

    public void OpenPrinter(SerialParam param, Handler handle) throws Exception {
        if(this.mOpened) {
            throw new Exception("PRINTER OPENED");
        } else {
            this.mExternHandler = handle;

            try {
                this.mSerialPortOperaion = new SerialPortOperaion(this.mExternHandler, param);
                this.mSerialPortOperaion.StartSerial();
                this.mOpened = true;
            } catch (IOException var4) {
                throw var4;
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    public void ClosePrinter() {
        this.mExternHandler = null;
        this.mSerialPortOperaion.StopSerial();
        this.mOpened = false;
    }

    public static String UnicodeToGBK(String s) {
        try {
            String e = null;
            e = new String(s.getBytes("GBK"), "ISO-8859-1");
            return e;
        } catch (UnsupportedEncodingException var2) {
            return s;
        }

    }

//    public void printHeader(String[] text) {
//            String gbk = UnicodeToGBK(text.);
//            int[] data = new int[gbk.length()];
//
//            for (int i = 0; i < gbk.length(); ++i) {
//                data[i] = gbk.charAt(i);
//
//            }
//
//            this.mSerialPortOperaion.WriteData(data);
//            this.mSerialPortOperaion.WriteData(new int[]{10});
//
//
//
//    }

    public void printString(ArrayList<String> text) {
        for(int x =0;x<text.size();x++) {
            String gbk = UnicodeToGBK(text.get(x));
            int[] data = new int[gbk.length()];

            for (int i = 0; i < gbk.length(); ++i) {
                data[i] = gbk.charAt(i);

            }

            this.mSerialPortOperaion.WriteData(data);
            this.mSerialPortOperaion.WriteData(new int[]{10});

        }

    }




    public void enlargeFontSize(int x, int y) throws Exception {
        if(x >= 0 && x <= 4 && y >= 0 && y <= 4) {
            byte[] cmd = new byte[]{(byte)10, (byte)27, (byte)86, (byte)x};
            this.mSerialPortOperaion.WriteData(cmd);
            cmd[1] = 27;
            cmd[2] = 85;
            cmd[3] = (byte)y;
            this.mSerialPortOperaion.WriteData(cmd);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }

    public void walkPaper(int dot) throws Exception {
        if(dot >= 0 && dot <= 255) {
            byte[] cmd = new byte[]{(byte)10, (byte)27, (byte)74, (byte)dot};
            this.mSerialPortOperaion.WriteData(cmd);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }

    public void setLineSpace(int space) throws Exception {
        if(space >= 0 && space <= 255) {
            byte[] cmd = new byte[]{(byte)10, (byte)27, (byte)74, (byte)space};
            this.mSerialPortOperaion.WriteData(cmd);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }

    public void setHighlight(boolean reverse) {
        byte[] cmd = new byte[]{(byte)10, (byte)27, (byte)105, (byte)(reverse?1:0)};
        this.mSerialPortOperaion.WriteData(cmd);
    }

    public void setGray(int gray) throws Exception {
        if(gray >= 0 && gray <= 25) {
            byte[] cmd = new byte[]{(byte)10, (byte)29, (byte)77, (byte)gray};
            this.mSerialPortOperaion.WriteData(cmd);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }

    public void sydneyDotMatrix5by7()
    {
        this.mSerialPortOperaion.WriteData(27,33,0);
    }

    public void sydneyDotMatrix7by7()
    {
        this.mSerialPortOperaion.WriteData(27,33,1);
    }



    public  void sydneyResetSettings()
    {
        this.mSerialPortOperaion.WriteData(27,64);
    }



    public  void sydneySetUnderline()
    {
        ///27 45 n .. 1 = 0 CANCEL 2 = 1
        this.mSerialPortOperaion.WriteData(27, 45, 0);
    }


    public void setAlgin(int paramInt) throws Exception {
        throw new Exception("unsupported");
    }

    public void setBarHeight(int height) throws Exception {
        if(height >= 0 && height <= 255) {
            byte[] cmd = new byte[]{(byte)10, (byte)29, (byte)104, (byte)height};
            this.mSerialPortOperaion.WriteData(cmd);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }


    public void printBarCode(int type, byte[] code) throws Exception {
        if(type >= 0 && type <= 4) {
            byte[] cmd = new byte[]{(byte)10, (byte)29, (byte)107, (byte)type};
            this.mSerialPortOperaion.WriteData(cmd);
            this.mSerialPortOperaion.WriteData(code);
        } else {
            throw new Exception("Invalid Parameter");
        }
    }

    public void sendLineFeed() {
        this.mSerialPortOperaion.WriteData(new int[]{10});
    }

    public void PrintBmp(int startx, Bitmap bitmap) {
        byte[] start1 = new byte[]{(byte)13, (byte)10};
        int[] start2 = new int[]{27, 75, 27, 51};
        int width = bitmap.getWidth() + startx;
        int height = bitmap.getHeight();
        int adjustedHeight = (height + 7) / 8 * 8;
        if(width > 384) {
            width = 384;
        }

        for(int i = 0; i < adjustedHeight / 8; ++i) {
            byte[] data = new byte[width];
            byte xL = (byte)(width % 256);
            byte xH = (byte)(width / 256);
            start2[2] = xL;
            start2[3] = xH;
            this.mSerialPortOperaion.WriteData(start2);

            int k;
            for(k = 0; k < width; ++k) {
                data[k] = 0;
            }

            for(k = 0; k < 8; ++k) {
                for(int x = startx; x < width; ++x) {
                    int y = i * 8 + k;
                    if(y < height) {
                        int pixel = bitmap.getPixel(x - startx, y);
                        if(Color.red(pixel) == 0 || Color.green(pixel) == 0 || Color.blue(pixel) == 0) {
                            data[x] += (byte)(128 >> y % 8);
                        }
                    }
                }
            }

            this.mSerialPortOperaion.WriteData(data);

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var16) {

            }
        }

        this.mSerialPortOperaion.WriteData(start1);
        this.mSerialPortOperaion.WriteData(start1);
    }


}