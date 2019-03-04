package com.binarskugga.controller;

import com.binarskugga.model.SimpleModelImpl;
import com.binarskugga.skugga.api.annotation.Controller;
import com.binarskugga.skugga.api.annotation.Get;
import com.binarskugga.skugga.api.annotation.Post;
import com.binarskugga.skugga.api.impl.endpoint.AbstractController;
import com.binarskugga.skugga.util.EntityUtils;

import java.util.Map;

@Controller("primitive-type")
public class PrimitiveTypeController extends AbstractController {

	@Post("body-conversion")
	public void body_conversion(Map<String, Object> convert) {
		EntityUtils.create(SimpleModelImpl.class, convert);
	}

	@Get("test-void")
	public void test_get_void() {}

	@Get("test-byte/$")
	public byte test_get_byte(byte b) {
		return b;
	}

	@Get("test-short/$")
	public short test_get_short(short s) {
		return s;
	}

	@Get("test-int/$")
	public int test_get_int(int i) {
		return i;
	}

	@Get("test-long/$")
	public long test_get_long(long l) {
		return l;
	}

	@Get("test-float/$")
	public float test_get_float(float f) {
		return f;
	}

	@Get("test-double/$")
	public double test_get_double(double d) {
		return d;
	}

	@Get("test-boolean/$")
	public boolean test_get_boolean(boolean b) {
		return b;
	}

	@Get("test-char/$")
	public char test_get_char(char c) {
		return c;
	}

	@Get("test-abyte/$")
	public byte[] test_get_abyte(byte[] b) {
		return b;
	}

	@Get("test-abbyte/$")
	public Byte[] test_get_abbyte(Byte[] b) {
		return b;
	}

	@Get("test-ashort/$")
	public short[] test_get_ashort(short[] s) {
		return s;
	}

	@Get("test-abshort/$")
	public Short[] test_get_abshort(Short[] s) {
		return s;
	}

	@Get("test-aint/$")
	public int[] test_get_aint(int[] i) {
		return i;
	}

	@Get("test-abint/$")
	public Integer[] test_get_abint(Integer[] i) {
		return i;
	}

	@Get("test-along/$")
	public long[] test_get_along(long[] l) {
		return l;
	}

	@Get("test-ablong/$")
	public Long[] test_get_ablong(Long[] l) {
		return l;
	}

	@Get("test-afloat/$")
	public float[] test_get_afloat(float[] f) {
		return f;
	}

	@Get("test-abfloat/$")
	public Float[] test_get_abfloat(Float[] f) {
		return f;
	}

	@Get("test-adouble/$")
	public double[] test_get_adouble(double[] d) {
		return d;
	}

	@Get("test-abdouble/$")
	public Double[] test_get_abdouble(Double[] d) {
		return d;
	}

	@Get("test-aboolean/$")
	public boolean[] test_get_aboolean(boolean[] b) {
		return b;
	}

	@Get("test-abboolean/$")
	public Boolean[] test_get_abboolean(Boolean[] b) {
		return b;
	}

	@Get("test-achar/$")
	public char[] test_get_achar(char[] c) {
		return c;
	}

	@Get("test-abchar/$")
	public Character[] test_get_abchar(Character[] c) {
		return c;
	}

}
