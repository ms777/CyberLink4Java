package org.cybergarage.ms777.test;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;


public class ListServicesAndActions extends MediaControllerWithServerAndRenderer
{

	public static void main(String[] args) 
	{
		
		ListServicesAndActions listServAct = new ListServicesAndActions();
		listServAct.start();
		listServAct.waitForServerAndRenderer(10, 500);

		if (listServAct.getRenderer()==null) {
			System.err.println("renderer not found");
			listServAct.stop();
			return;
		}
		printServicesActions(listServAct.getRenderer());
		
		if (listServAct.getServer()==null) {
			System.err.println("server not found");
			listServAct.stop();
			return;
		}
		printServicesActions(listServAct.getServer());

		listServAct.stop();
	}


	public static void printServicesActions(Device dev) {
		System.out.println("\ndevice: " + dev.getFriendlyName() + " at: " + dev.getLocation() );
		ServiceList serviceList = dev.getServiceList();
		for (int i=0; i < serviceList.size(); i++) {
			Service service = (Service) serviceList.get(i);
			System.out.println("  service: " + service.getServiceType());
			ActionList actionList = service.getActionList();
			for (int k=0; k < actionList.size(); k++) {
				Action action = (Action) actionList.get(k);
				System.out.println("    action: " + action.getName());
			}
		}
	}

}

