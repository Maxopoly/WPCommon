package com.github.maxopoly.WPCommon.packetHandling.incoming;

import com.github.maxopoly.WPCommon.packetHandling.PacketType;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.Logger;

public abstract class GenericDataForwarder<I extends IPacketHandler> {

	private Map<Integer, I> subHandler;
	protected Logger logger;

	public GenericDataForwarder(Logger logger) {
		subHandler = new TreeMap<Integer, I>();
		this.logger = logger;
	}

	public void registerHandler(I handler) {
		if (handler.getPacketToHandle().getType() != getTypeToHandle()) {
			logger.error("Tried to register invalid packet combination, handler was " + getTypeToHandle().toString()
					+ ", subhandler was " + handler.getPacketToHandle().getType());
			return;
		}
		if (subHandler.containsKey(handler.getPacketToHandle().getID())) {
			logger.warn("Tried to register subhandler for " + getTypeToHandle().toString() + " with id "
					+ handler.getPacketToHandle().getID() + ", but was already registered. Ignoring it");
			return;
		}
		subHandler.put(handler.getPacketToHandle().getID(), handler);
	}

	public void handle(byte[] data, int packetID) {
		I handler = getHandler(packetID);
		if (handler != null) {
			handle(data, handler);
		}
	}

	public abstract void handle(byte[] data, I subHandler);

	protected I getHandler(int id) {
		return subHandler.get(id);
	}

	public abstract PacketType getTypeToHandle();

}
