package com.binarskugga.skuggahttps.utils;

import com.google.common.hash.*;

import java.io.*;

public class SerializableFunnel implements Funnel<Serializable> {

	@Override
	public void funnel(Serializable serializable, PrimitiveSink primitiveSink) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(serializable);
			out.flush();
			primitiveSink.putBytes(bos.toByteArray());
		} catch(IOException ignored) {} finally {
			try {
				bos.close();
			} catch (IOException ignored) {}
		}
	}

}
