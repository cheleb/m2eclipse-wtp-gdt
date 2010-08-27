package org.maven.ide.eclipse.gdt.wtp.deployment;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerCore;

public class M2EServerLifecycleListener implements IServerLifecycleListener {

	private static final  IServerListener M2E_LISTENER = new M2EServerListener(); 
	
	public void serverAdded(IServer server) {
		server.addServerListener(M2E_LISTENER);
	}

	public void serverChanged(IServer server) {
	}

	public void serverRemoved(IServer server) {
		server.removeServerListener(M2E_LISTENER);
	}
	
	public void initializeServers() {
		for (IServer server : ServerCore.getServers()){
			serverAdded(server);
		}
	}

	public void finalizeServers() {
		for (IServer server : ServerCore.getServers()){
			serverRemoved(server);
		}
	}

}
