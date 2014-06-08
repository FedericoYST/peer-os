package org.safehaus.subutai.ui.commandrunner;


import com.vaadin.ui.Component;
import org.safehaus.subutai.api.agentmanager.AgentManager;
import org.safehaus.subutai.api.commandrunner.CommandRunner;
import org.safehaus.subutai.server.ui.api.PortalModule;


public class CommandRunnerUI implements PortalModule {

    public static final String MODULE_NAME = "Terminal";
    private CommandRunner commandRunner;
    private AgentManager agentManager;


    public void setCommandRunner( CommandRunner commandRunner ) {
        this.commandRunner = commandRunner;
    }


    public void setAgentManager( AgentManager agentManager ) {
        this.agentManager = agentManager;
    }


    public void init() {
    }


    public void destroy() {
    }


    @Override
    public String getId() {
        return CommandRunnerUI.MODULE_NAME;
    }

    @Override
    public String getName() {
        return CommandRunnerUI.MODULE_NAME;
    }


    @Override
    public Component createComponent() {
        return new TerminalForm( commandRunner, agentManager );
    }
}
