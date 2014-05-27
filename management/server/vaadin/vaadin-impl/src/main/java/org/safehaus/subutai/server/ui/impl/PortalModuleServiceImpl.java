package org.safehaus.subutai.server.ui.impl;

import org.safehaus.subutai.server.ui.api.PortalModule;
import org.safehaus.subutai.server.ui.api.PortalModuleListener;
import org.safehaus.subutai.server.ui.api.PortalModuleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PortalModuleServiceImpl implements PortalModuleService {

    private List<PortalModule> modules = Collections.synchronizedList(new ArrayList<PortalModule>());

    private List<PortalModuleListener> listeners = Collections.synchronizedList(new ArrayList<PortalModuleListener>());

    public synchronized void registerModule(PortalModule module) {
        System.out.println("ModuleServiceImpl: Registering module " + module.getId());
        modules.add(module);
        for (PortalModuleListener listener : listeners) {
            listener.moduleRegistered(module);
        }
    }

    public synchronized void unregisterModule(PortalModule module) {
        System.out.println("ModuleServiceImpl: Unregistering module " + module.getId());
        modules.remove(module);
        for (PortalModuleListener listener : listeners) {
            listener.moduleUnregistered(module);
        }
    }

    public List<PortalModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    @Override
    public PortalModule getModule(String pModuleId) {
        for (PortalModule module : modules) {
            if (pModuleId.equals(module.getId())) {
                return module;
            }
        }
        throw new IllegalArgumentException("Cannot find any module with the id given");
    }

    public synchronized void addListener(PortalModuleListener listener) {
        System.out.println("ModuleServiceImpl: Adding listener " + listener);
        listeners.add(listener);
    }

    public synchronized void removeListener(PortalModuleListener listener) {
        System.out.println("ModuleServiceImpl: Removing listener " + listener);
        listeners.remove(listener);
    }

}