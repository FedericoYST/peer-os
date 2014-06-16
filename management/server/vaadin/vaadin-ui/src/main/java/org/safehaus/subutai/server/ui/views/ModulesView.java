/**
 * DISCLAIMER
 *
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 *
 * @author jouni@vaadin.com
 *
 */

package org.safehaus.subutai.server.ui.views;

import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.safehaus.subutai.server.ui.MainUI;
import org.safehaus.subutai.server.ui.api.PortalModule;
import org.safehaus.subutai.server.ui.api.PortalModuleListener;
import org.safehaus.subutai.server.ui.api.PortalModuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ModulesView extends VerticalLayout implements View, PortalModuleListener {

    private static final Logger LOG = Logger.getLogger(MainUI.class.getName());
    private TabSheet editors;
    private CssLayout modulesLayout;
    private List<Component> modulesList = new ArrayList<Component>();

    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        addStyleName("reports");

        addComponent(buildDraftsView());
        getPortalModuleService().addListener(this);
    }

    private Component buildDraftsView() {
        editors = new TabSheet();
        editors.setSizeFull();
        editors.addStyleName("borderless");
        editors.addStyleName("editors");

        VerticalLayout titleAndDrafts = new VerticalLayout();
        titleAndDrafts.setSizeUndefined();
        titleAndDrafts.setCaption("Modules");
        titleAndDrafts.setSpacing(true);
        titleAndDrafts.addStyleName("drafts");
        editors.addComponent(titleAndDrafts);

        Label draftsTitle = new Label("Modules");
        draftsTitle.addStyleName("h1");
        draftsTitle.setSizeUndefined();
        titleAndDrafts.addComponent(draftsTitle);
        titleAndDrafts.setComponentAlignment(draftsTitle, Alignment.TOP_CENTER);

        modulesLayout = new CssLayout();
        modulesLayout.setSizeUndefined();
        modulesLayout.addStyleName("catalog");
        titleAndDrafts.addComponent(modulesLayout);

        for (PortalModule module : getPortalModuleService().getModules()) {
            addModule(module);
        }

        return editors;
    }

    public void autoCreate(PortalModule module) {
        TabSheet.Tab tab = editors.addTab(module.createComponent());
        tab.setCaption(module.getName());
        tab.setClosable(true);
        editors.setSelectedTab(tab);
    }

    @Override
    public void moduleRegistered(PortalModule module) {
        addModule(module);
    }

    private void addModule(final PortalModule module) {
        CssLayout moduleLayout = new CssLayout();
        moduleLayout.setCaption(module.getName());
        moduleLayout.setId(module.getId());
        moduleLayout.setWidth(null);
        moduleLayout.addStyleName("create");

        moduleLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                autoCreate(module);
            }
        });

        /*Button create = new Button(module.getName());
        create.addStyleName("default");
        moduleLayout.addComponent(create);*/

        modulesLayout.addComponent(moduleLayout);
        /*create.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                autoCreate(module);
            }
        });*/

        modulesList.add(moduleLayout);
    }

    @Override
    public void moduleUnregistered(PortalModule module) {

        for (Component component : modulesList) {
            if (component.getId().equals(component.getId())) {
                modulesLayout.removeComponent(component);
                modulesList.remove(component);
                break;
            }
        }
    }

    public static PortalModuleService getPortalModuleService() {
        // get bundle instance via the OSGi Framework Util class
        BundleContext ctx = FrameworkUtil.getBundle(PortalModuleService.class).getBundleContext();
        if (ctx != null) {
            ServiceReference serviceReference = ctx.getServiceReference(PortalModuleService.class.getName());
            if (serviceReference != null) {
                return PortalModuleService.class.cast(ctx.getService(serviceReference));
            }
        }

        return null;
    }
}

