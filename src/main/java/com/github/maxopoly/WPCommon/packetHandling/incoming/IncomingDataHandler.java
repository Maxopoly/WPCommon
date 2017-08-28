package com.github.maxopoly.WPCommon.packetHandling.incoming;

import com.github.maxopoly.WPCommon.util.AES_CFB8_Encrypter;
import com.github.maxopoly.WPCommon.util.CompressionManager;
import com.github.maxopoly.WPCommon.util.VarInt;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.Logger;

public class IncomingDataHandler {

	private Map<Integer, GenericDataForwarder<?>> handlers;
	private DataInputStream input;
	private Logger logger;
	private AES_CFB8_Encrypter encrypter;
	private Runnable failureCallback;
	private volatile boolean active;

	public IncomingDataHandler(Logger logger, DataInputStream input, AES_CFB8_Encrypter encrypter,
			Runnable failureCallback) {
		this.input = input;
		this.failureCallback = failureCallback;
		this.logger = logger;
		this.encrypter = encrypter;
		this.handlers = new TreeMap<Integer, GenericDataForwarder<?>>();
	}

	public void registerHandler(GenericDataForwarder<?> handler) {
		if (handlers.containsKey(handler.getTypeToHandle().getID())) {
			logger.warn("Tried to register handler for " + handler.getTypeToHandle().toString() + " with id "
					+ handler.getTypeToHandle().getID() + ", but was already registered. Ignoring it");
			return;
		}
		handlers.put(handler.getTypeToHandle().getID(), handler);
	}

	public void startHandling() {
		active = true;
		while (active) {
			try {
				int packetLength = VarInt.readVarInt(input, encrypter);
				int dataType = VarInt.readVarInt(input, encrypter);
				int packetID = VarInt.readVarInt(input, encrypter);
				byte[] dataArray = new byte[packetLength];
				input.readFully(dataArray);
				byte[] decrypted = encrypter.decrypt(dataArray);
				byte[] decompressed = CompressionManager.decompress(decrypted, logger);
				GenericDataForwarder<?> handler = handlers.get(dataType);
				if (handler != null) {
					handler.handle(decompressed, packetID);
				} else {
					logger.error("Received data for handler type " + dataType + ", but no handler existed");
				}
			} catch (IOException e) {
				logger.error("Error handling incoming packets", e);
				failureCallback.run();
			}
		}
	}

	public void stopHandling() {
		active = false;
	}
}
