package org.cybergarage.ms777.test;

import java.util.ArrayList;

import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.std.av.controller.MediaController;
import org.cybergarage.upnp.std.av.renderer.MediaRenderer;
import org.cybergarage.upnp.std.av.server.MediaServer;
import org.cybergarage.upnp.Device;


public class MediaControllerWithServerAndRenderer extends MediaController implements DeviceChangeListener 
{
	public final String RENDERER = "XBMC (PC-MARTIN)";
	public final String SERVER = "AVM FRITZ!Mediaserver";

	private ArrayList<Device> mediaServers;
	private ArrayList<Device> mediaRenderers;
	
	private Device devRenderer = null;
	private Device devServer = null;
	
	public Device getRenderer() {
		return devRenderer;
	}

	public Device getServer() {
		return devServer;
	}

	public MediaControllerWithServerAndRenderer()
	{
		this.mediaServers = new ArrayList<Device>();
		this.mediaRenderers = new ArrayList<Device>();
		addDeviceChangeListener(this);
//		mediaController.start();
	}
	

	public void deviceAdded(Device device)
	{
		if (device.isDeviceType(MediaServer.DEVICE_TYPE)) {
			System.out.println("adding " + device.getFriendlyName() + " (server)");
			this.mediaServers.add(device);
			if (device.getFriendlyName().equalsIgnoreCase(SERVER)) {
				devServer = device;
			}
			return;
		} 
		if (device.isDeviceType(MediaRenderer.DEVICE_TYPE)) {
			System.out.println("adding " + device.getFriendlyName() + " (renderer)");
			this.mediaRenderers.add(device);
			if (device.getFriendlyName().equalsIgnoreCase(RENDERER)) {
				devRenderer = device;
			}
			return;
		} 
		System.out.println("adding " +device.getFriendlyName() + " (" + device.getDeviceType() + ")");
	}

	public void deviceRemoved(Device device) 
	{
		if (device.isDeviceType(MediaServer.DEVICE_TYPE)) {
			System.out.println("removing " + device.getFriendlyName() + " (server)");
			this.mediaServers.remove(device);
			return;
		} 
		if (device.isDeviceType(MediaRenderer.DEVICE_TYPE)) {
			System.out.println("removing " + device.getFriendlyName() + " (renderer)");
			this.mediaRenderers.remove(device);
			return;
		} 
		System.out.println("removing " +device.getFriendlyName() + " (" + device.getDeviceType() + ")");
	}
	
	public void waitForServerAndRenderer(int iLoops, int iMillis) {
		for (int i = 0; i < iLoops; i++) {
			try {
				Thread.sleep(iMillis);
			} catch (InterruptedException e) {
			}
			if ((getRenderer()!=null) && (getServer()!=null)) 
				break;
		}
		
	}
		


	public static void main(String[] args) // just for testing 
	{
		
		MediaControllerWithServerAndRenderer mediaControllerServRend = new MediaControllerWithServerAndRenderer();
		mediaControllerServRend.start();
		mediaControllerServRend.waitForServerAndRenderer(10, 500);

		if (mediaControllerServRend.getRenderer()==null) {
			System.err.println("renderer not found");
			mediaControllerServRend.stop();
			return;
		}
		
		if (mediaControllerServRend.getServer()==null) {
			System.err.println("server not found");
			mediaControllerServRend.stop();
			return;
		}
		System.out.println("success: both renderer and server found");
		
		mediaControllerServRend.stop();
	}

}

