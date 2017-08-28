package com.github.maxopoly.WPCommon.packetHandling.outgoing;

import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;
import com.github.maxopoly.WPCommon.util.AES_CFB8_Encrypter;
import com.github.maxopoly.WPCommon.util.CompressionManager;
import com.github.maxopoly.WPCommon.util.VarInt;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class OutgoingDataHandler {

	private DataOutputStream output;
	private AES_CFB8_Encrypter encrypter;
	private Runnable errorCallBack;
	private Queue<IPacket> packetQueue;
	private volatile boolean active;

	public OutgoingDataHandler(DataOutputStream output, AES_CFB8_Encrypter encrypter, Runnable errorCallBack) {
		this.output = output;
		this.encrypter = encrypter;
		this.errorCallBack = errorCallBack;
		this.packetQueue = new LinkedList<IPacket>();
		this.active = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (active) {
					IPacket packet;
					synchronized (packetQueue) {

						while (packetQueue.isEmpty()) {
							try {
								packetQueue.wait();
							} catch (InterruptedException e) {
							}
						}
						packet = packetQueue.poll();
					}
					sendData(packet);
				}
			}
		}).start();
	}

	public void queuePacket(IPacket packet) {
		synchronized (packetQueue) {
			packetQueue.add(packet);
			packetQueue.notifyAll();
		}
	}

	public void stop() {
		this.active = false;
	}

	private void sendData(IPacket packet) {
		synchronized (output) {
			byte[] data = packet.getData();
			try {
				byte[] compressed = CompressionManager.compress(data);
				PacketIndex type = packet.getPacket();
				VarInt.writeVarInt(output, compressed.length, encrypter);
				VarInt.writeVarInt(output, type.getType().getID(), encrypter);
				VarInt.writeVarInt(output, type.getID(), encrypter);
				byte[] encrypted = encrypter.encrypt(compressed);
				output.write(encrypted);
			} catch (IOException e) {
				errorCallBack.run();
			}
		}
	}

}
