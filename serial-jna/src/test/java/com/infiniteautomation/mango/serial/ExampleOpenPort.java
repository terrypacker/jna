/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.serial;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Terry Packer
 */
public class ExampleOpenPort {
    
    //Settings
    static String portName = "/dev/cu.usbserial-A101OGX5";
    
    private static final int FRAME_TYPE_BACNET_DATA_NOT_EXPECTING_REPLY = 0x06;
    private static final int FRAME_TYPE_BACNET_DATA_EXPECTING_REPLY = 0x05;
    
    private static final int N_MSTP = 0x02; //N_MOUSE discipline in ioctl-types.h
    private static final int O_RDWR = 0x02;
    private static final int O_NONBLOCK = 0x04;
    
    private static final int TIOCSETSD = 27;
    
    private static final int OPEN_FLAGS = (O_RDWR|O_NONBLOCK);

    public static void main(String[] args)  {
        
        try {
            SerialPort port = new SerialPort(portName);
            port.openPort();
            
            port.setFlowControlMode(0);
            port.setParams(115200, 8, 1, 0);
            
            Field handleRef = port.getClass().getDeclaredField("portHandle");
            handleRef.setAccessible(true);
            Long handleId = (Long)handleRef.get(port);
            int handle = handleId.intValue();
            clib.tcflush(handle, 10000);            
            //No Modify the port operation to switch to N_MSTP line discipline
            clib.ioctl(handle, TIOCSETSD, new int[] { N_MSTP });
            
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
            
            clib.write(handle, buffer, buffer.length);
            int count = 0;
            while(count< 60) {
                byte[] data = port.readBytes();
                if(data != null) {
                    for(byte b : data)
                        System.out.print(String.format("0x%02X", b));
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
    
    public void serialConfigure(int fd, long baud) {
        
        
        
        
    }
    
    
    static Clib clib;
    
    static {
        Native.loadLibrary(Platform.C_LIBRARY_NAME, Clib.class);
        Native.register(ClibDirectMapping.class, NativeLibrary.getInstance(Platform.C_LIBRARY_NAME));
        clib = new ClibDirectMapping();
    }
    
    public interface Clib extends Library {
        
        public int pipe(int[] fds);

        public int tcdrain(int fd);

//        public void cfmakeraw(termios termios);

        public int fcntl(int fd, int cmd, int arg);

        public int ioctl(int fd, int cmd, int[] arg);

        public int ioctl(int fd, int cmd, serial_struct arg);

        public int open(String path, int flags);

        public int close(int fd);

//        public int tcgetattr(int fd, termios termios);
//
//        public int tcsetattr(int fd, int cmd, termios termios);
//
//        public int cfsetispeed(termios termios, int i);
//
//        public int cfsetospeed(termios termios, int i);
//
//        public int cfgetispeed(termios termios);
//
//        public int cfgetospeed(termios termios);

        public int write(int fd, byte[] buffer, int count);

        public int read(int fd, byte[] buffer, int count);

        public int tcflush(int fd, int qs);

        public void perror(String msg);

        public int tcsendbreak(int fd, int duration);
    }
    
    public static class ClibDirectMapping implements Clib {

        native public int pipe(int[] fds);

        native public int tcdrain(int fd);

        //native public void cfmakeraw(termios termios);

        native public int fcntl(int fd, int cmd, int arg);

        native public int ioctl(int fd, int cmd, int[] arg);

        native public int ioctl(int fd, int cmd, serial_struct arg);

        native public int open(String path, int flags);

        native public int close(int fd);

        //native public int tcgetattr(int fd, termios termios);

        //native public int tcsetattr(int fd, int cmd, termios termios);

        //native public int cfsetispeed(termios termios, int i);

        //native public int cfsetospeed(termios termios, int i);

        //native public int cfgetispeed(termios termios);

        //native public int cfgetospeed(termios termios);

        native public int write(int fd, byte[] buffer, int count);

        native public int read(int fd, byte[] buffer, int count);

        native public int tcflush(int fd, int qs);

        native public void perror(String msg);

        native public int tcsendbreak(int fd, int duration);
    }
    
    
    public static class serial_struct extends Structure {

        public int type;
        public int line;
        public int port;
        public int irq;
        public int flags;
        public int xmit_fifo_size;
        public int custom_divisor;
        public int baud_base;
        public short close_delay;
        public short io_type;
        //public char io_type;
        //public char reserved_char;
        public int hub6;
        public short closing_wait;
        public short closing_wait2;
        public Pointer iomem_base;
        public short iomem_reg_shift;
        public int port_high;
        public NativeLong iomap_base;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(//
                    "type",//
                    "line",//
                    "port",//
                    "irq",//
                    "flags",//
                    "xmit_fifo_size",//
                    "custom_divisor",//
                    "baud_base",//
                    "close_delay",//
                    "io_type",//
                    //public char io_type;
                    //public char reserved_char;
                    "hub6",//
                    "closing_wait",//
                    "closing_wait2",//
                    "iomem_base",//
                    "iomem_reg_shift",//
                    "port_high",//
                    "iomap_base"//
            );
        }
    };
}
