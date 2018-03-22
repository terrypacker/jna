/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.LastErrorException;
import com.sun.jna.Pointer;

/**
 *
 * @author Terry Packer
 */
public class ClibDirectMapping implements Clib {

    native public int pipe(int[] fds);

    native public int tcdrain(int fd);

    //native public void cfmakeraw(termios termios);

    native public int fcntl(int fd, int cmd, int arg);

    public int ioctlJava(int fd, int cmd, int... arg) {
        return ioctl(fd, cmd, arg);
    }
    native public int ioctl(int fd, int cmd, int[] arg) throws LastErrorException;
    
    native public int ioctl(int fd, int cmd, Pointer arg) throws LastErrorException;

    native public int ioctl(int fd, int cmd, SerialStruct arg);
    
    native public int ioctl(int fd, int cmd, Termios2Struct arg);

    native public int open(String path, int flags);

    native public int close(int fd);

    native public int tcgetattr(int fd, Termios termios);

    native public int tcsetattr(int fd, int cmd, Termios termios);

    native public int cfsetispeed(Termios termios, int i);

    native public int cfsetospeed(Termios termios, int i);

    native public int cfgetispeed(Termios termios);

    native public int cfgetospeed(Termios termios);

    native public NativeSize write(int fd, byte[] buffer, NativeSize count);

    native public NativeSize read(int fd, byte[] buffer, NativeSize count);

    native public int tcflush(int fd, int qs);

    native public void perror(String msg);

    native public int tcsendbreak(int fd, int duration);
}