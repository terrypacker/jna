/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import java.lang.reflect.Field;

import jssc.SerialPort;

/**
 * Configure a Serial Port and Send out a Whois Managed by CControls MS/TP Driver
 * 
 * Pulled from https://github.com/nyholku/purejavacomm/blob/master/src/jtermios/linux/JTermiosImpl.java
 * 
 * @author Terry Packer
 */
public class SendWhoIs extends IoctlBase{
    

    public static void main(String[] args)  {
        
        if(args == null || args.length != 3) {
            System.out.println("SendWhoIs portName baud macAddress");
            return;
        }
        String portName = args[0];
        Integer baud = Integer.parseInt(args[1]);
        int mac = Integer.parseInt(args[2]);
        
        try {
            System.out.println("Opening: " + portName);
            
            SerialPort port = new SerialPort(portName);
            port.openPort();
            
            port.setFlowControlMode(0);
            port.setParams(baud, 8, 1, 0);
            Field handleRef = port.getClass().getDeclaredField("portHandle");
            handleRef.setAccessible(true);
            Long handleId = (Long)handleRef.get(port);
            int handle = handleId.intValue();

            //Configure to use Driver
            SetMacAddress.configureDriver(handle, (byte)mac);   
            
            //WHOIS Message, MAC will be set by underlying driver
            byte[] buffer = new byte[13];
            buffer[0] = FRAME_TYPE_BACNET_DATA_NOT_EXPECTING_REPLY;
            buffer[1] = (byte)0xFF;
            buffer[2] = (byte)0xFF;
            buffer[3] = (0x08 >> 8)&0xFF; //length 1
            buffer[4] = 0x08 & 0xFF; //length 2
            buffer[5] = 0x01;
            buffer[6] = 0x20;
            buffer[7] = (byte)0xFF;
            buffer[8] = (byte)0xFF;
            buffer[9] = 0x00;
            buffer[10] = (byte)0xFF;
            buffer[11] = (byte)0x10;
            buffer[12] = (byte)0x08;

            System.out.println("Sending WhoIs");
            for(byte b : buffer)
                System.out.print(String.format("0x%02X", b) + " ");
            System.out.println("\n");

            //clib.write(handle, buffer, buffer.length);
            port.writeBytes(buffer);
            System.out.println("Waiting for replies");
            int count = 0;
            byte[] inBuffer = new byte[25];
            while(count< 60) {
                //Fails on seg fault...
                //byte[] data = port.readBytes();
                int read = clib.read(handle, inBuffer, 25);
                if(read > 0) {
                    System.out.print("Recieving (" + read + "): ");
                    for(int i=0; i<read; i++) {
                        System.out.print(String.format("0x%02X", inBuffer[i]) + " ");
                    }
                    System.out.print("\n");
                }
                
                Thread.sleep(500);
                count++;
            }
            port.closePort();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
 
}
