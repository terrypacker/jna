/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

/**
 *
 * @author Terry Packer
 */
public class IoctlBase {

    //From purejavacomm JTermios linux
    //linux/serial.h stuff
    public static final int FIONREAD = 0x541B; // Looked up manually
    //fcntl.h stuff
    public static final int O_RDWR = 0x00000002;
    public static final int O_NONBLOCK = 0x00000800;
    public static final int O_NOCTTY = 0x00000100;
    public static final int O_NDELAY = 0x00000800;
    public static final int F_GETFL = 0x00000003;
    public static final int F_SETFL = 0x00000004;
    //errno.h stuff
    public static final int EAGAIN = 11;
    public static final int EACCES = 13;
    public static final int EEXIST = 17;
    public static final int EINTR = 4;
    public static final int EINVAL = 22;
    public static final int EIO = 5;
    public static final int EISDIR = 21;
    public static final int ELOOP = 40;
    public static final int EMFILE = 24;
    public static final int ENAMETOOLONG = 36;
    public static final int ENFILE = 23;
    public static final int ENOENT = 2;
    public static final int ENOSR = 63;
    public static final int ENOSPC = 28;
    public static final int ENOTDIR = 20;
    public static final int ENXIO = 6;
    public static final int EOVERFLOW = 75;
    public static final int EROFS = 30;
    public static final int ENOTSUP = 95;
    //termios.h stuff
    public static final int TIOCM_RNG = 0x00000080;
    public static final int TIOCM_CAR = 0x00000040;
    public static final int IGNBRK = 0x00000001;
    public static final int BRKINT = 0x00000002;
    public static final int IGNPAR = 0x00000004;
    public static final int PARMRK = 0x00000008;
    public static final int INLCR = 0x00000040;
    public static final int IGNCR = 0x00000080;
    public static final int ICRNL = 0x00000100;
    public static final int ECHONL = 0x00000040;
    public static final int IEXTEN = 0x00008000;
    public static final int CLOCAL = 0x00000800;
    public static final int OPOST = 0x00000001;
    public static final int VSTART = 0x00000008;
    public static final int TCSANOW = 0x00000000;
    public static final int VSTOP = 0x00000009;
    public static final int VMIN = 0x00000006;
    public static final int VTIME = 0x00000005;
    public static final int VEOF = 0x00000004;
    public static final int TIOCMGET = 0x00005415;
    public static final int TIOCM_CTS = 0x00000020;
    public static final int TIOCM_DSR = 0x00000100;
    public static final int TIOCM_RI = 0x00000080;
    public static final int TIOCM_CD = 0x00000040;
    public static final int TIOCM_DTR = 0x00000002;
    public static final int TIOCM_RTS = 0x00000004;
    public static final int ICANON = 0x00000002;
    public static final int ECHO = 0x00000008;
    public static final int ECHOE = 0x00000010;
    public static final int ISIG = 0x00000001;
    public static final int TIOCMSET = 0x00005418;
    public static final int IXON = 0x00000400;
    public static final int IXOFF = 0x00001000;
    public static final int IXANY = 0x00000800;
    public static final int CRTSCTS = 0x80000000;
    public static final int TCSADRAIN = 0x00000001;
    public static final int INPCK = 0x00000010;
    public static final int ISTRIP = 0x00000020;
    public static final int CSIZE = 0x00000030;
    public static final int TCIFLUSH = 0x00000000;
    public static final int TCOFLUSH = 0x00000001;
    public static final int TCIOFLUSH = 0x00000002;
    public static final int CS5 = 0x00000000;
    public static final int CS6 = 0x00000010;
    public static final int CS7 = 0x00000020;
    public static final int CS8 = 0x00000030;
    public static final int CSTOPB = 0x00000040;
    public static final int CREAD = 0x00000080;
    public static final int PARENB = 0x00000100;
    public static final int PARODD = 0x00000200;
    public static final int HUPCL = 0x00000400;
    public static final int B0 = 0;
    public static final int B50 = 1;
    public static final int B75 = 2;
    public static final int B110 = 3;
    public static final int B134 = 4;
    public static final int B150 = 5;
    public static final int B200 = 6;
    public static final int B300 = 7;
    public static final int B600 = 8;
    public static final int B1200 = 9;
    public static final int B1800 = 10;
    public static final int B2400 = 11;
    public static final int B4800 = 12;
    public static final int B9600 = 13;
    public static final int B19200 = 14;
    public static final int B38400 = 15;
    public static final int B57600 = 4097;
    public static final int B115200 = 4098;
    public static final int B230400 = 4099;
    //poll.h stuff
    public static final int POLLIN = 0x0001;
    public static final int POLLPRI = 0x0002;
    public static final int POLLOUT = 0x0004;
    public static final int POLLERR = 0x0008;
    public static final int POLLNVAL = 0x0020;
    
    
    //Custom IOCTL Codes from discovery below here
    //From fnctl.h
    public static final int FNDELAY = O_NONBLOCK;
    
    public static final int N_MSTP = 0x02; //N_MOUSE discipline in ioctl-types.h

    
    //#define IOCPARM_MASK  0x1fff      /* parameter length, at most 13 bits */
    public static final int IOCPARM_MASK = 0x1fff;
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
    public static final int TIOCSETSD = 0x5423;
    public static final int TIOCGSERIAL = 0x541e;
    public static final int TIOCSSERIAL = 0x541f;
    public static final int TCGETS2 = 0x802c542a;
    public static final int TCSETS2 = 0x402c542;

    //From serial.h
    public static final int ASYNC_LOW_LATENCY = 0x2000;
    
    
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
    public static final int MSTP_IOC_MAGIC = (int)0xBA;
    //#define MSTP_IOC_SETMAXMASTER       _IOW(MSTP_IOC_MAGIC,0xC0,unsigned)
    //#define MSTP_IOC_SETMAXINFOFRAMES   _IOW(MSTP_IOC_MAGIC,0xC1,unsigned)
    //#define MSTP_IOC_SETMACADDRESS      _IOW(MSTP_IOC_MAGIC,0xC2,unsigned char)
    public static final int MSTP_IOC_SETMACADDRESS = 0x4001bac2; //(0x80000000 | ((sizeofUnsignedChar & IOCPARM_MASK) << 16) | ((MSTP_IOC_MAGIC) << 8) | (0xC2));
    //#define MSTP_IOC_SETTUSAGE          _IOW(MSTP_IOC_MAGIC,0xC6,unsigned)

    //#define MSTP_IOC_GETMAXMASTER       _IOR(MSTP_IOC_MAGIC,0xC3,unsigned)
    //#define MSTP_IOC_GETMAXINFOFRAMES   _IOR(MSTP_IOC_MAGIC,0xC4,unsigned)
    //#define MSTP_IOC_GETMACADDRESS      _IOR(MSTP_IOC_MAGIC,0xC5,unsigned char)
    public static final int MSTP_IOC_GETMACADDRESS = 0x8001bac5;
    //#define MSTP_IOC_GETTUSAGE          _IOR(MSTP_IOC_MAGIC,0xC7,unsigned)
    //#define MSTP_IOC_GETVER             _IOR(MSTP_IOC_MAGIC,0xC8,unsigned)
    //Settings
    
    public static final int FRAME_TYPE_BACNET_DATA_NOT_EXPECTING_REPLY = 0x06;
    public static final int FRAME_TYPE_BACNET_DATA_EXPECTING_REPLY = 0x05;
    public static final int OPEN_FLAGS = (O_RDWR|O_NONBLOCK);

    
    static ClibDirectMapping clib;
    
    static {
        Native.setProtected(true);
        if(!Native.isProtected())
            System.out.println("Running in un-procted mode because the JVM's lack of protected JNA support.");
        Native.loadLibrary(Platform.C_LIBRARY_NAME, Clib.class);
        Native.register(ClibDirectMapping.class, NativeLibrary.getInstance(Platform.C_LIBRARY_NAME));
        clib = new ClibDirectMapping();
    }
    
}
