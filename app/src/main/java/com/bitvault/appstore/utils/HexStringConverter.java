package com.bitvault.appstore.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author
 * convert hex to string
 */
public class HexStringConverter {
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static HexStringConverter hexStringConverter = null;

    HexStringConverter() {
    }

    /***
     * This method is used to return the instance of this class
     * to other classes
     *
     * @return
     */
    public static HexStringConverter getHexStringConverterInstance() {
        if (hexStringConverter == null)
            hexStringConverter = new HexStringConverter();
        return hexStringConverter;
    }

    /***
     * This class is used to change the string data into the hex string and
     * return it to the calling class.
     *
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     */
    public String stringToHex(String input) throws UnsupportedEncodingException {
        if (input == null)
            throw new NullPointerException();
        return asHex(input.getBytes());
    }

    /***
     * This method is vice-versa of the above method, used to convert
     * hex data into simple string.
     *
     * @param txtInHex
     * @return
     */
    public String hexToString(String txtInHex) {
        byte[] txtInByte = new byte[txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2) {
            txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }

    /***
     * This method is used to convert byte buffer data
     * into the string.
     *
     * @param buf
     * @return
     */
    public String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    /***
     * This method is used to convert hex numeric data into the
     * string form.
     *
     * @param num
     * @return
     */
    public String ConvertToHexadecimal(int num) {
        int r;
        String bin = "0";

        do {
            r = num % 16;
            num = num / 16;

            if (r == 10)
                bin = bin + "A";

            else if (r == 11)
                bin = bin + "B";

            else if (r == 12)
                bin = bin + "C";

            else if (r == 13)
                bin = bin + "D";

            else if (r == 14)
                bin = bin + "E";

            else if (r == 15)
                bin = bin + "F";

            else
                bin = bin + r;
        } while (num != 0);

        return bin;
    }

}