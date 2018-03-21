/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.serial;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.IntegerType;
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
 * Configure a Serial Port and Send out a Whois Managed by CControls MS/TP Driver
 * 
 * Pulled from https://github.com/nyholku/purejavacomm/blob/master/src/jtermios/linux/JTermiosImpl.java
 * 
 * @author Terry Packer
 */
public class SendWhoIs {
    
    //From fnctl.h
    private static final int F_SETFL = 4; /*set file->f_flags*/
    private static final int O_RDWR = 0x02;
    private static final int O_NONBLOCK = 0x04;
    private static final int FNDELAY = O_NONBLOCK;
    
    private static final int N_MSTP = 0x02; //N_MOUSE discipline in ioctl-types.h

    
    //#define IOCPARM_MASK  0x1fff      /* parameter length, at most 13 bits */
    private static final int IOCPARM_MASK = 0x1fff;
    //#define IOC_VOID    0x20000000  /* no parameters */
    //#define IOC_OUT     0x40000000  /* copy out parameters */
    //#define IOC_IN      0x80000000  /* copy in parameters */
    //#define IOC_INOUT   (IOC_IN|IOC_OUT)
    //#define _IOC(inout,group,num,len) (inout | ((len & IOCPARM_MASK) << 16) | ((group) << 8) | (num))
    //#define _IO(g,n)    _IOC(IOC_VOID,  (g), (n), 0)
    //#define _IOR(g,n,t) _IOC(IOC_OUT,   (g), (n), sizeof(t))
    //#define _IOW(g,n,t) _IOC(IOC_IN,    (g), (n), sizeof(t))
    /* this should be _IORW, but stdio got there first */
    //#define _IOWR(g,n,t)    _IOC(IOC_INOUT, (g), (n), sizeof(t))
    private static final int TIOCSETSD = 0x5423;
    private static final int TIOCGSERIAL = 0x541e;
    private static final int TIOCSSERIAL = 0x541f;
    private static final int TCGETS2 = 0x802c542a;
    private static final int TCSETS2 = 0x402c542;
    
    //From termbits.h
    private static final int CREAD = 200;
    private static final int CLOCAL = 4000;
    //From serial.h
    private static final int ASYNC_LOW_LATENCY = 0x2000;
    //C sizes
    //unsigned char - 1 byte
    //unsigned - ?? 2-4 bytes
    
    
    //MSTP_IOC_SET_MACADDRESS : 1073855170
    //MSTP_IOC_SETMAXMASTER : 1074051776
    //MSTP_IOC_SETMAXINFOFRAMES : 1074051777
    //MSTP_IOC_SETMACADDRESS : 1073855170
    //MSTP_IOC_SETTUSAGE : 1074051782
    //MSTP_IOC_GETMAXMASTER : -2147173693
    //MSTP_IOC_GETMAXINFOFRAMES : -2147173692
    //MSTP_IOC_GETMACADDRESS : -2147370299
    //MSTP_IOC_GETTUSAGE : -2147173689
    //MSTP_IOC_GETVER : -2147173688
    //TIOCSETD    : 21539
    
    //HEX OUTPUT Pi Compute Module
    //MSTP_IOC_SET_MACADDRESS : 4001bac2
    //MSTP_IOC_SETMAXMASTER : 4004bac0
    //MSTP_IOC_SETMAXINFOFRAMES : 4004bac1
    //MSTP_IOC_SETMACADDRESS : 4001bac2
    //MSTP_IOC_SETTUSAGE : 4004bac6
    //MSTP_IOC_GETMAXMASTER : 8004bac3
    //MSTP_IOC_GETMAXINFOFRAMES : 8004bac4
    //MSTP_IOC_GETMACADDRESS : 8001bac5
    //MSTP_IOC_GETTUSAGE : 8004bac7
    //MSTP_IOC_GETVER : 8004bac8
    //TIOCSETD    : 5423
    
    //#define MSTP_IOC_MAGIC 0xBA
    private static final int MSTP_IOC_MAGIC = (int)0xBA;
    //#define MSTP_IOC_SETMAXMASTER       _IOW(MSTP_IOC_MAGIC,0xC0,unsigned)
    //#define MSTP_IOC_SETMAXINFOFRAMES   _IOW(MSTP_IOC_MAGIC,0xC1,unsigned)
    //#define MSTP_IOC_SETMACADDRESS      _IOW(MSTP_IOC_MAGIC,0xC2,unsigned char)
    private static final int MSTP_IOC_SET_MACADDRESS = 0x4001bac2; //(0x80000000 | ((sizeofUnsignedChar & IOCPARM_MASK) << 16) | ((MSTP_IOC_MAGIC) << 8) | (0xC2));
    //#define MSTP_IOC_SETTUSAGE          _IOW(MSTP_IOC_MAGIC,0xC6,unsigned)

    //#define MSTP_IOC_GETMAXMASTER       _IOR(MSTP_IOC_MAGIC,0xC3,unsigned)
    //#define MSTP_IOC_GETMAXINFOFRAMES   _IOR(MSTP_IOC_MAGIC,0xC4,unsigned)
    //#define MSTP_IOC_GETMACADDRESS      _IOR(MSTP_IOC_MAGIC,0xC5,unsigned char)
    //#define MSTP_IOC_GETTUSAGE          _IOR(MSTP_IOC_MAGIC,0xC7,unsigned)
    //#define MSTP_IOC_GETVER             _IOR(MSTP_IOC_MAGIC,0xC8,unsigned)
    //Settings
    
    private static final int FRAME_TYPE_BACNET_DATA_NOT_EXPECTING_REPLY = 0x06;
    private static final int FRAME_TYPE_BACNET_DATA_EXPECTING_REPLY = 0x05;
    

    

    

    
    private static final int OPEN_FLAGS = (O_RDWR|O_NONBLOCK);

    public static void main(String[] args)  {
        Native.setProtected(true);
        if(!Native.isProtected())
            System.out.println("Running in un-procted mode because the JVM's lack of protected JNA support.");
        
        if(args == null || args.length != 3) {
            System.out.println("SendWhoIs portName baud macAddress");
            return;
        }
        String portName = args[0];
        Integer baud = Integer.parseInt(args[1]);
        Integer mac = Integer.parseInt(args[2]);
        
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

            //TODO  Handle TCGETS2 Command to get termios2 Struct
            Termios2Struct termios = new Termios2Struct();
            clib.ioctl(handle, new NativeSize(TCGETS2), termios);
            
            //Configure the serial port for non-blocking reads/writes
            clib.fcntl(handle, F_SETFL, FNDELAY);
            
            
            //Control settings - receiver on, non-modem
            termios.c_cflag = CREAD | CLOCAL;
            //TODO More settings to change
            clib.ioctl(handle, new NativeSize(TCSETS2), termios);
            
            //Same as purgePort clib.tcflush(handle, 10000);
            System.out.println("Purging: " + port.getPortName());
            port.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
            
            //Read and save the serial port settings
            SerialStruct serialInfo = new SerialStruct();
            int result = clib.ioctl(handle, new NativeSize(TIOCGSERIAL), serialInfo);
            if(result < 0)
                System.out.println("Failed to read SerialStruct: " + result);
            serialInfo.flags |= ASYNC_LOW_LATENCY;
            result = clib.ioctl(handle, new NativeSize(TIOCSSERIAL), serialInfo);
            if(result < 0)
                System.out.println("Failed to set SerialStruct: " + result);
            
            System.out.println("Switching to N_MSTP line discipline");
            //Modify the port operation to switch to N_MSTP line discipline
            result = clib.ioctl(handle, new NativeSize(TIOCSETSD), N_MSTP);
            if(result < 0)
                System.out.println("Failed to switch to N_MSTP line discipline: " + result);
            

            byte macByte = mac.byteValue();
            System.out.println("Setting MAC address to " + macByte);
            //Configure the Driver
            result = clib.ioctl(handle, new NativeSize(MSTP_IOC_SET_MACADDRESS), macByte);
            if(result < 0)
                System.out.println("Failed to set MAC address to " + macByte + " " + result);
            
            
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
                //byte[] data = port.readBytes();
                NativeSize read = clib.read(handle, buffer, new NativeSize(25));
                if(read.intValue() > 0) {
                    System.out.print("Recieving (" + read + "): ");
                    for(int i=0; i<read.intValue(); i++)
                        System.out.print(String.format("0x%02X", inBuffer[i]) + " ");
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

        public int ioctl(int fd, NativeSize cmd, int... arg);

        public int ioctl(int fd, NativeSize cmd, SerialStruct arg);
        
        public int ioctl(int fd, NativeSize cmd, Termios2Struct arg);

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

        public NativeSize write(int fd, byte[] buffer, NativeSize count);

        public NativeSize read(int fd, byte[] buffer, NativeSize count);

        public int tcflush(int fd, int qs);

        public void perror(String msg);

        public int tcsendbreak(int fd, int duration);
    }
    
    public static class ClibDirectMapping implements Clib {

        native public int pipe(int[] fds);

        native public int tcdrain(int fd);

        //native public void cfmakeraw(termios termios);

        native public int fcntl(int fd, int cmd, int arg);

        native public int ioctl(int fd, NativeSize cmd, int... arg);

        native public int ioctl(int fd, NativeSize cmd, SerialStruct arg);
        
        native public int ioctl(int fd, NativeSize cmd, Termios2Struct arg);

        native public int open(String path, int flags);

        native public int close(int fd);

        //native public int tcgetattr(int fd, termios termios);

        //native public int tcsetattr(int fd, int cmd, termios termios);

        //native public int cfsetispeed(termios termios, int i);

        //native public int cfsetospeed(termios termios, int i);

        //native public int cfgetispeed(termios termios);

        //native public int cfgetospeed(termios termios);

        native public NativeSize write(int fd, byte[] buffer, NativeSize count);

        native public NativeSize read(int fd, byte[] buffer, NativeSize count);

        native public int tcflush(int fd, int qs);

        native public void perror(String msg);

        native public int tcsendbreak(int fd, int duration);
    }
    
    public static class Termios2Struct extends Structure {
        public int c_iflag;       /* input mode flags */
        public int c_oflag;       /* output mode flags */
        public int c_cflag;       /* control mode flags */
        public int c_lflag;       /* local mode flags */
        public short c_line;            /* line discipline */
        public short c_cc[] = new short[19];        /* control characters */
        public int c_ispeed;       /* input speed */
        public int  c_ospeed;       /* output speed */

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(//
                    "c_iflag",//
                    "c_oflag",//
                    "c_cflag",//
                    "c_lflag",//
                    "c_line",//
                    "c_cc",//
                    "c_ispeed",//
                    "c_ospeed"
            );
        }
    }
    
    public static class SerialStruct extends Structure {

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
        protected List<String> getFieldOrder() {
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
    
    public static class NativeSize extends IntegerType {

        /**
                 *
                 */
        private static final long serialVersionUID = 2398288011955445078L;
        /**
         * Size of a size_t integer, in bytes.
         */
        public static int SIZE = Native.SIZE_T_SIZE;//Platform.is64Bit() ? 8 : 4;

        /**
         * Create a zero-valued Size.
         */
        public NativeSize() {
            this(0);
        }

        /**
         * Create a Size with the given value.
         */
        public NativeSize(long value) {
            super(SIZE, value);
        }
    }
}
