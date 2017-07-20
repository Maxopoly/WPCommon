package com.github.maxopoly.WPCommon.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VarInt {

	public static void writeVarInt(DataOutputStream os, int value, AES_CFB8_Encrypter encrypt) throws IOException {
		while (true) {
			if ((value & 0xFFFFFF80) == 0) {
				byte valueToWrite = (byte) (encrypt == null ? value : encrypt.encrypt(new byte[] { (byte) value })[0]);
				os.writeByte(valueToWrite);
				return;
			}
			byte valueToWrite = (byte) (value & 0x7F | 0x80);
			os.writeByte(encrypt == null ? valueToWrite : encrypt.encrypt(new byte[] { valueToWrite })[0]);
			value >>>= 7;
		}
	}

	public static void writeVarInt(DataOutputStream os, int value) throws IOException {
		writeVarInt(os, value, null);
	}

	public static int readVarInt(DataInputStream input, AES_CFB8_Encrypter encrypt) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = input.readByte();
			if (encrypt != null) {
				k = encrypt.decrypt(new byte[] { (byte) k })[0];
			}
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5)
				throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128)
				break;
		}
		return i;
	}

	public static int readVarInt(DataInputStream input) throws IOException {
		return readVarInt(input, null);
	}
}
