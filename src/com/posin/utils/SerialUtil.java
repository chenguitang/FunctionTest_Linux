package com.posin.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Greetty on 2017/4/21.
 * <p>
 * 串口工具类
 */

public class SerialUtil {

    /**
     * 查找所有串口
     *
     * @return  String[]
     */
    public static String[] findAllDevices() {
        Vector<String> devices = new Vector<String>();
        // Parse each driver
        Iterator<Driver> itdriv;
        try {
            itdriv = getDrivers().iterator();
            while (itdriv.hasNext()) {
                Driver driver = itdriv.next();
                Iterator<File> itdev = driver.getDevices().iterator();
                while (itdev.hasNext()) {
                    String device = itdev.next().getAbsolutePath();
                    devices.add(device);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[devices.size()]);
    }

    public static class Driver {
        public Driver(String name, String root) {
            mDriverName = name;
            mDeviceRoot = root;
        }

        private String mDriverName;
        private String mDeviceRoot;
        Vector<File> mDevices = null;

        public Vector<File> getDevices() {
            if (mDevices == null) {
                mDevices = new Vector<File>();
                File dev = new File("/dev");
                File[] files = dev.listFiles();
                int i;
                for (i = 0; i < files.length; i++) {
                    if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
                        //Log.d(TAG, "Found new device: " + files[i]);
                        mDevices.add(files[i]);
                    }
                }
            }
            return mDevices;
        }

        public String getName() {
            return mDriverName;
        }
    }

    private static Vector<Driver> getDrivers() throws IOException {
        Vector<Driver> drivers = new Vector<Driver>();
        LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));
        String l;
        while ((l = r.readLine()) != null) {
            // Issue 3:
            // Since driver name may contain spaces, we do not extract driver name with split()
            String drivername = l.substring(0, 0x15).trim();
            String[] w = l.split(" +");
            if ((w.length >= 5) && (w[w.length - 1].equals("serial"))) {
                //Log.d(TAG, "Found new driver " + drivername + " on " + w[w.length-4]);
                drivers.add(new Driver(drivername, w[w.length - 4]));
            }
        }
        r.close();
        return drivers;
    }
}
