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
public class Termios extends Structure {

    public int c_iflag;
    public int c_oflag;
    public int c_cflag;
    public int c_lflag;
    public byte c_line;
    public byte[] c_cc = new byte[32];
    public int c_ispeed;
    public int c_ospeed;

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
                "c_ospeed"//
        );
    }

}
