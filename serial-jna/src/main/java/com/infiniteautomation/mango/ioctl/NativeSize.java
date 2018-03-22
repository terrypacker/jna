/**
 * @copyright 2018 {@link http://infiniteautomation.com|Infinite Automation Systems, Inc.} All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.ioctl;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

/**
 *
 * @author Terry Packer
 */
public class NativeSize extends IntegerType {

    private static final long serialVersionUID = 2398288011955445078L;
    /**
     * Size of a size_t integer, in bytes.
     */
    public static int SIZE = Native.SIZE_T_SIZE;//Platform.is64Bit() ? 8 : 4;

    /**
     * Create a zero-valued Size.
     */
    public NativeSize() {
        this(0);
    }

    /**
     * Create a Size with the given value.
     */
    public NativeSize(long value) {
        super(SIZE, value);
    }
}