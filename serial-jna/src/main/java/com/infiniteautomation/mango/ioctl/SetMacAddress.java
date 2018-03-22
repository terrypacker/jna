/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.LastErrorException;
import com.sun.jna.ptr.ByteByReference;

/**
 * 
 * Set MAC Address of custom serial driver
 *
 * @author Terry Packer
 */
public class SetMacAddress extends IoctlBase{

    public static void main(String[] args)  {
        if(args == null || args.length != 2) {
            System.out.println("SetMacAddress port macAddress");
            return;
        }
        
        String portName = args[0];
        int mac = Integer.parseInt(args[1]);
        
        //Open the port
        int fd = clib.open(portName, OPEN_FLAGS);
        if(fd == -1) {
            System.out.println("Unable to open port " + portName);
            System.exit(1);
        }
        
        //Configure the serial port for non-blocking reads/writes
        if(clib.fcntl(fd, F_SETFL, FNDELAY) < 0)
            System.out.println("Unable to set non-blocking read/write");
        
        //Get the line discipline
        Termios termios = new Termios();
        if(clib.tcgetattr(fd, termios) < 0)
            System.out.println("Failed to get termios struct");

        termios.c_cflag = CREAD | CLOCAL;
        
        //set baud rate
        termios.c_cflag |= B57600; //Just for a change
        
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
        if(clib.tcflush(fd, TCIFLUSH) < 0);
            System.out.println("Failed to flush the port.");
        
        if(clib.tcsetattr(fd, TCSANOW, termios) < 0)
            System.out.println("Failed to set termios struct");
        
        //TODO For testing
        if(clib.tcgetattr(fd, termios) < 0)
            System.out.println("Failed to get termios struct");
        //TODO Set Low Latency Flag
        
        //Now try switching discipline
        if(clib.ioctlJava(fd, TIOCSETSD, N_MSTP) < 0)
            System.out.println("Failed to switch discipline.");
        
        ByteByReference macByte = new ByteByReference((byte)mac);
        try{
            clib.ioctl(fd, MSTP_IOC_SETMACADDRESS, macByte.getPointer());
        }catch(LastErrorException e) {
            System.out.println("Failed to set MAC address: " + e.getErrorCode());
        }

        ByteByReference macRead = new ByteByReference();
        try{
            clib.ioctl(fd, MSTP_IOC_GETMACADDRESS, macRead.getPointer());
            System.out.println("Mac read as " + macRead.getValue());
        }catch (LastErrorException e) {
            System.out.println("Failed to get MAC address: " + e.getErrorCode());
        }
        
        clib.close(fd);

    }
}
