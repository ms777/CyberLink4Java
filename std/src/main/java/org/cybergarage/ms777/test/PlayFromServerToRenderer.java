package org.cybergarage.ms777.test;

import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.std.av.server.object.ContentNode;
import org.cybergarage.upnp.std.av.server.object.container.ContainerNode;
import org.cybergarage.upnp.std.av.server.object.item.ItemNode;
import org.cybergarage.xml.Node;


public class PlayFromServerToRenderer extends MediaControllerWithServerAndRenderer implements DeviceChangeListener 
{

	public ContentNode browseDirectChildren(String objectID, String sTitle, int requestedCount) {
		Node node = browseDirectChildren(getServer(), objectID, "*", 0, requestedCount, null);
		
		ContentNode cnFound = null;
		for (int i=0; i < node.getNNodes(); i++) {
			ContentNode contNode = new ContainerNode();
			contNode.set(node.getNode(i));
			if (contNode.getTitle().equals(sTitle)) {
				cnFound = contNode;
				break;
			}
		}

		System.out.println("\ntitle: " + cnFound.getTitle());
		ContentNode cnFilmeout = getContentDirectory(getServer(), cnFound.getID());
		printContentNode(cnFilmeout, 1);
		
		return cnFound;
	}

	public static void main(String[] args) 
	{
		PlayFromServerToRenderer test = new PlayFromServerToRenderer();
		
		test.start();
		test.waitForServerAndRenderer(10, 500);

		if (test.getRenderer()==null) {
			System.err.println("renderer not found");
			test.stop();
			return;
		}
		
		if (test.getServer()==null) {
			System.err.println("server not found");
			test.stop();
			return;
		}
		
		ContentNode contentNode = null;
		contentNode = test.browseDirectChildren("0", "Filme", 999);
		contentNode = test.browseDirectChildren(contentNode.getID(), "Alle Filme", 999);
		contentNode = test.browseDirectChildren(contentNode.getID(), "FRITZ-Video", 999);
		
		Node node = test.browseMetaData(test.getServer(), contentNode.getID());
		System.out.println("node: " + node);
		
		ItemNode itemVideo = new ItemNode();
		itemVideo.set(node.getNode(0));
		System.out.println("itemVideo: " + itemVideo);

		boolean bSuccess = test.play(test.getRenderer(), itemVideo);
		System.out.println("play success: " + bSuccess);
		test.stop();
		System.out.println("end");
	}

}

