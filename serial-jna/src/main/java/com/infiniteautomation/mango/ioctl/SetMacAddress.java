/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.LastErrorException;

/**
 * 
 * Set MAC Address of custom serial driver
 *
 * @author Terry Packer
 */
public class SetMacAddress extends IoctlBase{

    public static void main(String[] args)  {
        
        int fd = -1;
        try {
        
            if(args == null || args.length != 2) {
                System.out.println("SetMacAddress: port macAddress");
                return;
            }
            
            String portName = args[0];
            int mac = Integer.parseInt(args[1]);
            
            //Open the port
            System.out.println("Opening Port " + portName);
            fd = clib.open(portName, OPEN_FLAGS);
            
            configureDriver(fd, (byte)mac);

        }catch(LastErrorException e) {            
            e.printStackTrace();
        }finally {
            if(fd > 0)
                clib.close(fd);
        }

    }
    
    public static void configureDriver(int fd, byte mac) throws LastErrorException {
      //Configure the serial port for non-blocking reads/writes
        System.out.println("Setting non-blocking read/write");
        clib.fcntl(fd, F_SETFL, FNDELAY);
        
        //Get the line discipline
        Termios termios = new Termios();
        System.out.println("Getting Termios Struct");
        clib.tcgetattr(fd, termios);

        termios.c_cflag = CREAD | CLOCAL;
        
        //set baud rate
        //TODO If we don't want to use JSSC PORT: termios.c_cflag |= B57600; //Just for a change
        
        //clear the HUPCL bit, close doesn't change DTR
        termios.c_cflag &= ~HUPCL;

        //setup for 8-N-1
        termios.c_cflag |= CS8;                           //8 databits
        termios.c_cflag &= ~(PARENB | PARODD);            //No parity
        termios.c_cflag &= ~CSTOPB;                       //1 stop bit    
        /* set input flag non-canonical, no processing */
        termios.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
        
        /* ignore parity errors */
        termios.c_iflag = (IGNBRK | IGNPAR);

        /* set output flag non-canonical, no processing */
        termios.c_oflag = 0;

        termios.c_cc[VTIME] = 0;   /* no time delay */
        termios.c_cc[VMIN]  = 1;   /* no char delay */
        
        //Set the line discipline
        termios.c_line = N_MSTP;
        
        /* flush the buffer */
        System.out.println("Flusing the port.");
        clib.tcflush(fd, TCIFLUSH);
            
        System.out.println("Setting termios struct");
        clib.tcsetattr(fd, TCSANOW, termios);
            
        System.out.println("Getting termios struct");
        clib.tcgetattr(fd, termios);
            
        SerialStruct serialInfo = new SerialStruct();
        System.out.println("Getting Serial Info");
        clib.ioctl(fd, TIOCGSERIAL, serialInfo);
        
        serialInfo.flags |= ASYNC_LOW_LATENCY;
        System.out.println("Setting Low Latency Flag");
        clib.ioctl(fd, TIOCSSERIAL, serialInfo);
        
        //Now try switching discipline
        System.out.println("Switching to MSTP discipline.");
        clib.ioctlJava(fd, TIOCSETSD, N_MSTP);
            
        System.out.println("Setting MAC address");
        clib.ioctl(fd, MSTP_IOC_SETMACADDRESS, mac);
        
        System.out.println("Getting MAC address");
        int macRead = clib.ioctlJava(fd, MSTP_IOC_GETMACADDRESS);
        System.out.println("Mac read as " + macRead);
    }
    
}
