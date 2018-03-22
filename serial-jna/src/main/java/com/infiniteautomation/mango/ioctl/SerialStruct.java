/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 *
 * @author Terry Packer
 */
public class SerialStruct extends Structure {

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
}