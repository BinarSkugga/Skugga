package com.binarskugga.skugga.util.conversion;

import com.binarskugga.skugga.api.exception.InvalidArgumentException;
import com.binarskugga.skugga.util.ReflectionUtils;

public class PrimitiveConverter {

	private Class inC;
	private Class inUnboxedC;

	PrimitiveConverter(Class in) {
		this.inC = in;
		if(ReflectionUtils.isBoxedPrimitive(in))
			this.inUnboxedC = ReflectionUtils.unboxClass(in);
		else
			this.inUnboxedC = this.inC;
	}

	@SuppressWarnings("unchecked")
	public <V> V convertTo(Class<V> outC, Object in) {
		if(!ReflectionUtils.isPrimitiveOrBoxed(this.inC) || !ReflectionUtils.isPrimitiveOrBoxed(outC)) return null;

		Class outUnboxedC = outC;
		if(ReflectionUtils.isBoxedPrimitiveArray(outC))
			outUnboxedC = ReflectionUtils.unboxClass(outC);

		Object result = this.convertPrimitive(outUnboxedC, in);
		return (V) result;
	}

	@SuppressWarnings("unchecked")
	private <V> V convertPrimitive(Class<V> outC, Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC) || !ReflectionUtils.isPrimitive(outC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(outC)) return (V) in;
		else if(outC.equals(boolean.class)) return (V) this.toBoolean(in);
		else if(outC.equals(char.class)) return (V) this.toChar(in);
		else if(outC.equals(double.class)) return (V) this.toDouble(in);
		else if(outC.equals(float.class)) return (V) this.toFloat(in);
		else if(outC.equals(long.class)) return (V) this.toLong(in);
		else if(outC.equals(int.class)) return (V) this.toInt(in);
		else if(outC.equals(short.class)) return (V) this.toShort(in);
		else if(outC.equals(byte.class)) return (V) this.toByte(in);
		else return null;
	}

	//region Primitive array conversions
	public Boolean toBoolean(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(boolean.class)) return (boolean) in;
		else if(this.inUnboxedC.equals(char.class)) return toBoolean((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toBoolean((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toBoolean((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toBoolean((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toBoolean((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toBoolean((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toBoolean((long) in);
		else throw new InvalidArgumentException();
	}
	public boolean toBoolean(char a) {
		return a > 0;
	}
	public boolean toBoolean(double a) {
		return a > 0;
	}
	public boolean toBoolean(float a) {
		return a > 0;
	}
	public boolean toBoolean(byte a) {
		return a > 0;
	}
	public boolean toBoolean(short a) {
		return a > 0;
	}
	public boolean toBoolean(int a) {
		return a > 0;
	}
	public boolean toBoolean(long a) {
		return a > 0;
	}

	public Character toChar(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(char.class)) return (char) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toChar((boolean) in);
		else if(this.inUnboxedC.equals(double.class)) return toChar((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toChar((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toChar((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toChar((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toChar((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toChar((long) in);
		else throw new InvalidArgumentException();
	}
	public char toChar(boolean a) {
		return (char) (a ? 1 : 0);
	}
	public char toChar(double a) {
		return (char) a;
	}
	public char toChar(float a) {
		return (char) a;
	}
	public char toChar(byte a) {
		return (char) a;
	}
	public char toChar(short a) {
		return (char) a;
	}
	public char toChar(int a) {
		return (char) a;
	}
	public char toChar(long a) {
		return (char) a;
	}

	public Double toDouble(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(double.class)) return (double) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toDouble((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toDouble((char) in);
		else if(this.inUnboxedC.equals(float.class)) return toDouble((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toDouble((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toDouble((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toDouble((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toDouble((long) in);
		else throw new InvalidArgumentException();
	}
	public double toDouble(boolean a) {
		return a ? 1 : 0;
	}
	public double toDouble(char a) {
		return a;
	}
	public double toDouble(float a) {
		return a;
	}
	public double toDouble(byte a) {
		return a;
	}
	public double toDouble(short a) {
		return a;
	}
	public double toDouble(int a) {
		return a;
	}
	public double toDouble(long a) {
		return a;
	}

	public Float toFloat(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(float.class)) return (float) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toFloat((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toFloat((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toFloat((double) in);
		else if(this.inUnboxedC.equals(byte.class)) return toFloat((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toFloat((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toFloat((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toFloat((long) in);
		else throw new InvalidArgumentException();
	}
	public float toFloat(boolean a) {
		return a ? 1 : 0;
	}
	public float toFloat(char a) {
		return a;
	}
	public float toFloat(double a) {
		return (float) a;
	}
	public float toFloat(byte a) {
		return a;
	}
	public float toFloat(short a) {
		return a;
	}
	public float toFloat(int a) {
		return a;
	}
	public float toFloat(long a) {
		return a;
	}

	public Long toLong(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(long.class)) return (long) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toLong((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toLong((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toLong((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toLong((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toLong((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toLong((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toLong((int) in);
		else throw new InvalidArgumentException();
	}
	public long toLong(boolean a) {
		return a ? 1 : 0;
	}
	public long toLong(char a) {
		return a;
	}
	public long toLong(double a) {
		return (long) a;
	}
	public long toLong(float a) {
		return (long) a;
	}
	public long toLong(byte a) {
		return a;
	}
	public long toLong(short a) {
		return a;
	}
	public long toLong(int a) {
		return a;
	}

	public Integer toInt(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(int.class)) return (int) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toInt((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toInt((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toInt((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toInt((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toInt((byte) in);
		else if(this.inUnboxedC.equals(short.class)) return toInt((short) in);
		else if(this.inUnboxedC.equals(long.class)) return toInt((long) in);
		else throw new InvalidArgumentException();
	}
	public int toInt(boolean a) {
		return a ? 1 : 0;
	}
	public int toInt(char a) {
		return (int) a;
	}
	public int toInt(double a) {
		return (int) a;
	}
	public int toInt(float a) {
		return (int) a;
	}
	public int toInt(byte a) {
		return a;
	}
	public int toInt(short a) {
		return a;
	}
	public int toInt(long a) {
		return (int) a;
	}

	public Short toShort(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(short.class)) return (short) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toShort((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toShort((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toShort((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toShort((float) in);
		else if(this.inUnboxedC.equals(byte.class)) return toShort((byte) in);
		else if(this.inUnboxedC.equals(int.class)) return toShort((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toShort((long) in);
		else throw new InvalidArgumentException();
	}
	public short toShort(boolean a) {
		return (short) (a ? 1 : 0);
	}
	public short toShort(char a) {
		return (short) a;
	}
	public short toShort(double a) {
		return (short) a;
	}
	public short toShort(float a) {
		return (short) a;
	}
	public short toShort(byte a) {
		return a;
	}
	public short toShort(int a) {
		return (short) a;
	}
	public short toShort(long a) {
		return (short) a;
	}

	public Byte toByte(Object in) {
		if(!ReflectionUtils.isPrimitive(this.inUnboxedC)) throw new InvalidArgumentException();
		else if(this.inUnboxedC.equals(byte.class)) return (byte) in;
		else if(this.inUnboxedC.equals(boolean.class)) return toByte((boolean) in);
		else if(this.inUnboxedC.equals(char.class)) return toByte((char) in);
		else if(this.inUnboxedC.equals(double.class)) return toByte((double) in);
		else if(this.inUnboxedC.equals(float.class)) return toByte((float) in);
		else if(this.inUnboxedC.equals(short.class)) return toByte((short) in);
		else if(this.inUnboxedC.equals(int.class)) return toByte((int) in);
		else if(this.inUnboxedC.equals(long.class)) return toByte((long) in);
		else throw new InvalidArgumentException();
	}
	public byte toByte(boolean a) {
		return (byte) (a ? 1 : 0);
	}
	public byte toByte(char a) {
		return (byte) a;
	}
	public byte toByte(double a) {
		return (byte) a;
	}
	public byte toByte(float a) {
		return (byte) a;
	}
	public byte toByte(short a) {
		return (byte) a;
	}
	public byte toByte(int a) {
		return (byte) a;
	}
	public byte toByte(long a) {
		return (byte) a;
	}
	//endregion

}
