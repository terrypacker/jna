/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.LastErrorException;

/**
 *
 * @author Terry Packer
 */
public class ClibDirectMapping implements Clib {
    
    native public int fcntl(int fd, int cmd, int arg) throws LastErrorException;
    native public int open(String path, int flags) throws LastErrorException;
    native public int tcgetattr(int fd, Termios termios) throws LastErrorException;
    native public int tcsetattr(int fd, int cmd, Termios termios) throws LastErrorException;
    native public int tcflush(int fd, int qs) throws LastErrorException;
    native public int close(int fd) throws LastErrorException;
    native public int write(int fd, byte[] buffer, int count);
    native public int read(int fd, byte[] buffer, int count);
    native public int ioctl(int fd, int cmd, byte arg) throws LastErrorException;
    native public int ioctl(int fd, int cmd, int[] arg) throws LastErrorException;
    native public int ioctl(int fd, int cmd, SerialStruct arg) throws LastErrorException;
    native public int ioctl(int fd, int cmd, Termios2Struct arg) throws LastErrorException;
    
    
    public int ioctlJava(int fd, int cmd, int... arg) {
        return ioctl(fd, cmd, arg);
    }

//    native public int pipe(int[] fds);
//    native public int tcdrain(int fd);
//    native public void cfmakeraw(termios termios);
//    native public int ioctl(int fd, int cmd, SerialStruct arg);
//    native public int ioctl(int fd, int cmd, Termios2Struct arg);
//    native public int cfsetispeed(Termios termios, int i);
//    native public int cfsetospeed(Termios termios, int i);
//    native public int cfgetispeed(Termios termios);
//    native public int cfgetospeed(Termios termios);
//    native public NativeSize write(int fd, byte[] buffer, NativeSize count);
//    native public NativeSize read(int fd, byte[] buffer, NativeSize count);
//    native public void perror(String msg);
//    native public int tcsendbreak(int fd, int duration);
}