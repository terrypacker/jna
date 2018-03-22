/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 *
 * @author Terry Packer
 */
public class Termios2Struct extends Structure {
    
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