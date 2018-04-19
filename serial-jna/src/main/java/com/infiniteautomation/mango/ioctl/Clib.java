/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;

/**
 *
 * @author Terry Packer
 */
public interface Clib extends Library {

    public int fcntl(int fd, int cmd, int arg) throws LastErrorException;
    public int ioctl(int fd, int cmd, int[] arg) throws LastErrorException;
    public int open(String path, int flags) throws LastErrorException;
    public int tcgetattr(int fd, Termios termios) throws LastErrorException;
    public int tcsetattr(int fd, int cmd, Termios termios) throws LastErrorException;
    public int tcflush(int fd, int qs) throws LastErrorException;
    public int close(int fd) throws LastErrorException; 
    public int write(int fd, byte[] buffer, int count);
    public int read(int fd, byte[] buffer, int count);
    public int ioctl(int fd, int cmd, byte arg) throws LastErrorException;
    public int ioctl(int fd, int cmd, SerialStruct arg) throws LastErrorException;
    public int ioctl(int fd, int cmd, Termios2Struct arg) throws LastErrorException;
    
 }
