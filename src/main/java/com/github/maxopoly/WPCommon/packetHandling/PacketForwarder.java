package com.github.maxopoly.WPCommon.packetHandling;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public abstract class PacketForwarder {

	private Map<String, AbstractPacketHandler> packetHandler;
	private Logger logger;

	public PacketForwarder(Logger logger) {
		this.packetHandler = new HashMap<String, AbstractPacketHandler>();
		this.logger = logger;
		registerHandler();
	}

	protected abstract void registerHandler();

	public synchronized void registerPacketHandler(AbstractPacketHandler toRegister) {
		if (packetHandler.containsKey(toRegister.getRequestToHandle())) {
			logger.warn("Tried to register handler for " + toRegister.getRequestToHandle()
					+ ", but was already registered. Ignoring it");
			return;
		}
		packetHandler.put(toRegister.getRequestToHandle(), toRegister);
	}

	public synchronized void handlePacket(JSONObject json) {
		if (json == null) {
			throw new IllegalArgumentException("Json may not be null");
		}
		String request;
		if ((!json.has("request")) || (request = json.getString("request")) == null) {
			throw new IllegalArgumentException("Submitted json does not have a request header");
		}
		AbstractPacketHandler handler = packetHandler.get(request);
		if (handler == null) {
			logger.info("Received request type " + request + ", but no local handler was found. Ignoring it.");
			return;
		}
		handler.handle(json);
	}

}
