package com.sxit.crawler.utils;


import java.text.DecimalFormat;

/**
 * 
 * 
 * @since 1.0
 * @version $Id: ByteUtils.java,v 1.1 2011/07/19 02:06:22 liangexiang Exp $
 */
public class ByteUtils {

	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };   

	public static String byteToHexString(byte src[]) {
		StringBuffer sb = new StringBuffer();
		if (null == src || src.length <=0) {
			return null;
		}
		for (int i=0; i<src.length; i++) {
			sb.append(hexChar[(src[i] & 0xf0) >>> 4]);
			sb.append(hexChar[(src[i] & 0x0f)]);
		}
		return sb.toString();
	}
	

	public static String byteToHexString(byte src) {
		StringBuffer sb = new StringBuffer();
		sb.append(hexChar[(src & 0xf0) >>> 4]);
		sb.append(hexChar[(src & 0x0f)]);
		return sb.toString();
	}
	
	public static boolean compareByteArray(Object src[], byte dest[]) {
		if (src.length != dest.length) 
			return false;
		int i=0;
		for (i=0; i<src.length; i++) {
			if (((Byte)src[i]).byteValue() != dest[i]) {
				break;
			}
		}
		return (i >= src.length);
	}
	public static boolean compareByteArray(byte src[], byte dest[]) {
		if (src.length != dest.length) 
			return false;
		int i=0;
		for (i=0; i<src.length; i++) {
			if (src[i] != dest[i]) {
				break;
			}
		}
		return (i >= src.length);
	}
	public static boolean compareByteArray(Byte src[], byte dest[]) {
		if (src.length != dest.length) 
			return false;
		int i=0;
		for (i=0; i<src.length; i++) {
			if (src[i].byteValue() != dest[i]) {
				break;
			}
		}
		return (i >= src.length);
	}
	
	public static String formatByteLength(Long val) {
		DecimalFormat df = new DecimalFormat("###.##");
		float f;
		if (val < 1024) {
			return (df.format(val) + "B");
		} else if (val < 1024 * 1024) {
			f = (float) ((float) val / (float) 1024);
			return (df.format(new Float(f).doubleValue()) + "KB");
		} else {
			f = (float) ((float) val / (float) (1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + "MB");
		}
	}
	
	
	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}
	
	public static long byte2long(byte[] buf) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 8) {
			throw new IllegalArgumentException("byte array size > 8 !");
		}
		long r = 0;
		for (int i = 0; i < buf.length; i++) {
			r <<= 8;
			r |= (buf[i] & 0x00000000000000ff);
		}
		return r;
	}
	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (int) (n >> 24);
		b[1] = (byte) (int) (n >> 16);
		b[2] = (byte) (int) (n >> 8);
		b[3] = (byte) (int) n;
		return b;
	}
	
	public static int byte2int(byte[] buf) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 4) {
			throw new IllegalArgumentException("byte array size > 8 !");
		}
		int r = 0;
		for (int i = 0; i < buf.length; i++) {
			r <<= 8;
			r |= (buf[i] & 0x000000ff);
		}
		return r;
	}
	
	
}
