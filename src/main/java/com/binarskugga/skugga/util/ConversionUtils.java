package com.binarskugga.skugga.util;

import org.apache.commons.lang3.ArrayUtils;

public class ConversionUtils {

	private ConversionUtils() {}

	@SuppressWarnings("unchecked")
	public static <T, V> V convertArray(Class<T> inC, Class<V> outC, T in) {
		if(!ReflectionUtils.isPrimitiveArrayOrBoxed(inC) || !ReflectionUtils.isPrimitiveArrayOrBoxed(outC)) return null;

		Class inUnboxedC = inC;
		Object inUnboxed = in;
		if(ReflectionUtils.isBoxedPrimitiveArray(inC)) {
			inUnboxedC = ReflectionUtils.unboxClass(inC);
			inUnboxed = unboxArray(inC, in);
		}

		Class outUnboxedC = outC;
		boolean wasBoxed = false;
		if(ReflectionUtils.isBoxedPrimitiveArray(outC)) {
			outUnboxedC = ReflectionUtils.unboxClass(outC);
			wasBoxed = true;
		}

		Object result = convertPrimitiveArray(inUnboxedC, outUnboxedC, inUnboxed);
		return (V) (wasBoxed ? boxArray(outUnboxedC, result) : result);
	}

	@SuppressWarnings("unchecked")
	public static <T, V> V convertPrimitiveArray(Class<T> inC, Class<V> outC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC) || !ReflectionUtils.isPrimitiveArray(outC)) return null;
		else if(inC.equals(outC)) return (V) in;
		else if(outC.equals(boolean[].class)) return (V) toBoolean(inC, in);
		else if(outC.equals(char[].class)) return (V) toChar(inC, in);
		else if(outC.equals(double[].class)) return (V) toDouble(inC, in);
		else if(outC.equals(float[].class)) return (V) toFloat(inC, in);
		else if(outC.equals(long[].class)) return (V) toLong(inC, in);
		else if(outC.equals(int[].class)) return (V) toInt(inC, in);
		else if(outC.equals(short[].class)) return (V) toShort(inC, in);
		else if(outC.equals(byte[].class)) return (V) toByte(inC, in);
		else return null;
	}

	//region Array boxing & unboxing
	public static <T> Object unboxArray(Class<T> inC, T in) {
		if(!ReflectionUtils.isBoxedPrimitiveArray(inC)) return null;
		else if(inC.equals(Boolean[].class))
			return ArrayUtils.toPrimitive((Boolean[]) in);
		else if(inC.equals(Character[].class))
			return ArrayUtils.toPrimitive((Character[]) in);
		else if(inC.equals(Double[].class))
			return ArrayUtils.toPrimitive((Double[]) in);
		else if(inC.equals(Float[].class))
			return ArrayUtils.toPrimitive((Float[]) in);
		else if(inC.equals(Long[].class))
			return ArrayUtils.toPrimitive((Long[]) in);
		else if(inC.equals(Integer[].class))
			return ArrayUtils.toPrimitive((Integer[]) in);
		else if(inC.equals(Short[].class))
			return ArrayUtils.toPrimitive((Short[]) in);
		else if(inC.equals(Byte[].class))
			return ArrayUtils.toPrimitive((Byte[]) in);
		else
			return null;
	}

	public static <T> Object boxArray(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(boolean[].class))
			return ArrayUtils.toObject((boolean[]) in);
		else if(inC.equals(char[].class))
			return ArrayUtils.toObject((char[]) in);
		else if(inC.equals(double[].class))
			return ArrayUtils.toObject((double[]) in);
		else if(inC.equals(float[].class))
			return ArrayUtils.toPrimitive((float[]) in);
		else if(inC.equals(long[].class))
			return ArrayUtils.toObject((long[]) in);
		else if(inC.equals(int[].class))
			return ArrayUtils.toObject((int[]) in);
		else if(inC.equals(short[].class))
			return ArrayUtils.toObject((short[]) in);
		else if(inC.equals(byte[].class))
			return ArrayUtils.toObject((byte[]) in);
		else
			return null;
	}
	//endregion

	//region Primitive array conversions
	public static <T> boolean[] toBoolean(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(boolean[].class)) return (boolean[]) in;
		else if(inC.equals(char[].class)) return toBoolean((char[]) in);
		else if(inC.equals(double[].class)) return toBoolean((double[]) in);
		else if(inC.equals(float[].class)) return toBoolean((float[]) in);
		else if(inC.equals(byte[].class)) return toBoolean((byte[]) in);
		else if(inC.equals(short[].class)) return toBoolean((short[]) in);
		else if(inC.equals(int[].class)) return toBoolean((int[]) in);
		else if(inC.equals(long[].class)) return toBoolean((long[]) in);
		else return null;
	}
	public static boolean[] toBoolean(char[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(double[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(float[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(byte[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(short[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(int[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}
	public static boolean[] toBoolean(long[] a) {
		boolean[] b = new boolean[a.length];
		for(int i = 0; i < a.length; i++) b[i] = a[i] > 0;
		return b;
	}

	public static <T> char[] toChar(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(char[].class)) return (char[]) in;
		else if(inC.equals(boolean[].class)) return toChar((boolean[]) in);
		else if(inC.equals(double[].class)) return toChar((double[]) in);
		else if(inC.equals(float[].class)) return toChar((float[]) in);
		else if(inC.equals(byte[].class)) return toChar((byte[]) in);
		else if(inC.equals(short[].class)) return toChar((short[]) in);
		else if(inC.equals(int[].class)) return toChar((int[]) in);
		else if(inC.equals(long[].class)) return toChar((long[]) in);
		else return null;
	}
	public static char[] toChar(boolean[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) (a[i] ? 1 : 0);
		return c;
	}
	public static char[] toChar(double[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}
	public static char[] toChar(float[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}
	public static char[] toChar(byte[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}
	public static char[] toChar(short[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}
	public static char[] toChar(int[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}
	public static char[] toChar(long[] a) {
		char[] c = new char[a.length];
		for(int i = 0; i < a.length; i++) c[i] = (char) a[i];
		return c;
	}

	public static <T> double[] toDouble(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(double[].class)) return (double[]) in;
		else if(inC.equals(boolean[].class)) return toDouble((boolean[]) in);
		else if(inC.equals(char[].class)) return toDouble((char[]) in);
		else if(inC.equals(float[].class)) return toDouble((float[]) in);
		else if(inC.equals(byte[].class)) return toDouble((byte[]) in);
		else if(inC.equals(short[].class)) return toDouble((short[]) in);
		else if(inC.equals(int[].class)) return toDouble((int[]) in);
		else if(inC.equals(long[].class)) return toDouble((long[]) in);
		else return null;
	}
	public static double[] toDouble(boolean[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i] ? 1.0d : 0.0d;
		return d;
	}
	public static double[] toDouble(char[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}
	public static double[] toDouble(float[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}
	public static double[] toDouble(byte[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}
	public static double[] toDouble(short[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}
	public static double[] toDouble(int[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}
	public static double[] toDouble(long[] a) {
		double[] d = new double[a.length];
		for(int i = 0; i < a.length; i++) d[i] = a[i];
		return d;
	}

	public static <T> float[] toFloat(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(float[].class)) return (float[]) in;
		else if(inC.equals(boolean[].class)) return toFloat((boolean[]) in);
		else if(inC.equals(char[].class)) return toFloat((char[]) in);
		else if(inC.equals(double[].class)) return toFloat((double[]) in);
		else if(inC.equals(byte[].class)) return toFloat((byte[]) in);
		else if(inC.equals(short[].class)) return toFloat((short[]) in);
		else if(inC.equals(int[].class)) return toFloat((int[]) in);
		else if(inC.equals(long[].class)) return toFloat((long[]) in);
		else return null;
	}
	public static float[] toFloat(boolean[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i] ? 1.0f : 0.0f;
		return f;
	}
	public static float[] toFloat(char[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i];
		return f;
	}
	public static float[] toFloat(double[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = (float) a[i];
		return f;
	}
	public static float[] toFloat(byte[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i];
		return f;
	}
	public static float[] toFloat(short[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i];
		return f;
	}
	public static float[] toFloat(int[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i];
		return f;
	}
	public static float[] toFloat(long[] a) {
		float[] f = new float[a.length];
		for(int i = 0; i < a.length; i++) f[i] = a[i];
		return f;
	}

	public static <T> long[] toLong(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(long[].class)) return (long[]) in;
		else if(inC.equals(boolean[].class)) return toLong((boolean[]) in);
		else if(inC.equals(char[].class)) return toLong((char[]) in);
		else if(inC.equals(double[].class)) return toLong((double[]) in);
		else if(inC.equals(float[].class)) return toLong((float[]) in);
		else if(inC.equals(byte[].class)) return toLong((byte[]) in);
		else if(inC.equals(short[].class)) return toLong((short[]) in);
		else if(inC.equals(int[].class)) return toLong((int[]) in);
		else return null;
	}
	public static long[] toLong(boolean[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = a[i] ? 1 : 0;
		return l;
	}
	public static long[] toLong(char[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = a[i];
		return l;
	}
	public static long[] toLong(double[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = (long) a[i];
		return l;
	}
	public static long[] toLong(float[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = (long) a[i];
		return l;
	}
	public static long[] toLong(byte[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = a[i];
		return l;
	}
	public static long[] toLong(short[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = a[i];
		return l;
	}
	public static long[] toLong(int[] a) {
		long[] l = new long[a.length];
		for(int i = 0; i < a.length; i++) l[i] = a[i];
		return l;
	}

	public static <T> int[] toInt(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(int[].class)) return (int[]) in;
		else if(inC.equals(boolean[].class)) return toInt((boolean[]) in);
		else if(inC.equals(char[].class)) return toInt((char[]) in);
		else if(inC.equals(double[].class)) return toInt((double[]) in);
		else if(inC.equals(float[].class)) return toInt((float[]) in);
		else if(inC.equals(byte[].class)) return toInt((byte[]) in);
		else if(inC.equals(short[].class)) return toInt((short[]) in);
		else if(inC.equals(long[].class)) return toInt((long[]) in);
		else return null;
	}
	public static int[] toInt(boolean[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = a[i] ? 1 : 0;
		return in;
	}
	public static int[] toInt(char[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = a[i];
		return in;
	}
	public static int[] toInt(double[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = (int) a[i];
		return in;
	}
	public static int[] toInt(float[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = (int) a[i];
		return in;
	}
	public static int[] toInt(byte[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = a[i];
		return in;
	}
	public static int[] toInt(short[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = a[i];
		return in;
	}
	public static int[] toInt(long[] a) {
		int[] in = new int[a.length];
		for(int i = 0; i < a.length; i++) in[i] = (int) a[i];
		return in;
	}

	public static <T> short[] toShort(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(short[].class)) return (short[]) in;
		else if(inC.equals(boolean[].class)) return toShort((boolean[]) in);
		else if(inC.equals(char[].class)) return toShort((char[]) in);
		else if(inC.equals(double[].class)) return toShort((double[]) in);
		else if(inC.equals(float[].class)) return toShort((float[]) in);
		else if(inC.equals(byte[].class)) return toShort((byte[]) in);
		else if(inC.equals(int[].class)) return toShort((int[]) in);
		else if(inC.equals(long[].class)) return toShort((long[]) in);
		else return null;
	}
	public static short[] toShort(boolean[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) (a[i] ? 1 : 0);
		return s;
	}
	public static short[] toShort(char[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) a[i];
		return s;
	}
	public static short[] toShort(double[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) a[i];
		return s;
	}
	public static short[] toShort(float[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) a[i];
		return s;
	}
	public static short[] toShort(byte[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = a[i];
		return s;
	}
	public static short[] toShort(int[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) a[i];
		return s;
	}
	public static short[] toShort(long[] a) {
		short[] s = new short[a.length];
		for(int i = 0; i < a.length; i++) s[i] = (short) a[i];
		return s;
	}

	public static <T> byte[] toByte(Class<T> inC, T in) {
		if(!ReflectionUtils.isPrimitiveArray(inC)) return null;
		else if(inC.equals(byte[].class)) return (byte[]) in;
		else if(inC.equals(boolean[].class)) return toByte((boolean[]) in);
		else if(inC.equals(char[].class)) return toByte((char[]) in);
		else if(inC.equals(double[].class)) return toByte((double[]) in);
		else if(inC.equals(float[].class)) return toByte((float[]) in);
		else if(inC.equals(short[].class)) return toByte((short[]) in);
		else if(inC.equals(int[].class)) return toByte((int[]) in);
		else if(inC.equals(long[].class)) return toByte((long[]) in);
		else return null;
	}
	public static byte[] toByte(boolean[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) (a[i] ? 1 : 0);
		return b;
	}
	public static byte[] toByte(char[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	public static byte[] toByte(double[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	public static byte[] toByte(float[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	public static byte[] toByte(short[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	public static byte[] toByte(int[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	public static byte[] toByte(long[] a) {
		byte[] b = new byte[a.length];
		for(int i = 0; i < a.length; i++) b[i] = (byte) a[i];
		return b;
	}
	//endregion

}
