/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 *
 * @author Terry Packer
 */
public interface Clib extends Library {
    
    public int pipe(int[] fds);

    public int tcdrain(int fd);

//    public void cfmakeraw(termios termios);

    public int fcntl(int fd, int cmd, int arg);

    public int ioctl(int fd, int cmd, int... arg);
    
//    public int ioctl(int fd, int cmd, int arg);
//    
//    public int ioctl(int fd, int cmd);

    public int ioctl(int fd, int cmd, SerialStruct arg);
    
    public int ioctl(int fd, int cmd, Termios2Struct arg);

    public int open(String path, int flags);

    public int close(int fd);

    public int tcgetattr(int fd, Termios termios);

    public int tcsetattr(int fd, int cmd, Termios termios);

    public int cfsetispeed(Termios termios, int i);
    
    public int cfsetospeed(Termios termios, int i);
    
    public int cfgetispeed(Termios termios);
    
    public int cfgetospeed(Termios termios);

    public NativeSize write(int fd, byte[] buffer, NativeSize count);

    public NativeSize read(int fd, byte[] buffer, NativeSize count);

    public int tcflush(int fd, int qs);

    public void perror(String msg);

    public int tcsendbreak(int fd, int duration);
    
 }
