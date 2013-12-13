/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.kiskis.mgmt.server.ui.modules.cassandra.wizzard;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.safehaus.kiskis.mgmt.server.ui.util.AppData;
import org.safehaus.kiskis.mgmt.shared.protocol.Agent;
import org.safehaus.kiskis.mgmt.shared.protocol.Command;
import org.safehaus.kiskis.mgmt.shared.protocol.OutputRedirection;
import org.safehaus.kiskis.mgmt.shared.protocol.Request;
import org.safehaus.kiskis.mgmt.shared.protocol.enums.RequestType;

/**
 *
 * @author bahadyr
 */
public class Step6 extends Panel {

    List<Agent> hosts;
    String changeNameCommand = "sed -i \"$(sed -n '/cluster_name:/=' /opt/cassandra-2.0.0/conf/cassandra.yaml)\"'s/Test Cluster/'\"%name\"'/' /opt/cassandra-2.0.0/conf/cassandra.yaml";

    public Step6(final CassandraWizard cassandraWizard) {
        setCaption("Configuration Step41");
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setHeight(600, Sizeable.UNITS_PIXELS);
        verticalLayout.setMargin(true);

        GridLayout grid = new GridLayout(6, 10);
        grid.setSpacing(true);
        grid.setSizeFull();

        Panel panel = new Panel();
        Label menu = new Label("Cluster Install Wizard<br>"
                + " 1) Welcome<br>"
                + " 2) List nodes<br>"
                + " 3) Installation<br>"
                + " 4) <font color=\"#f14c1a\"><strong>Configuration</strong></font><br>");
        menu.setContentMode(Label.CONTENT_XHTML);
        panel.addComponent(menu);

        grid.addComponent(menu, 0, 0, 1, 5);
        grid.setComponentAlignment(panel, Alignment.TOP_CENTER);

        final TextField clusterName = new TextField("Name your Cluster:");

        grid.addComponent(clusterName, 2, 0, 5, 1);
        grid.setComponentAlignment(clusterName, Alignment.MIDDLE_CENTER);

        // 'Shorthand' constructor - also supports data binding using Containers
//        hosts = new ArrayList<Agent>(AppData.getSelectedAgentList());
//        List<UUID> agentUuids = new ArrayList<UUID>();
//        for (Agent agent : hosts) {
//            agentUuids.add(agent.getUuid());
//        }
        final ListSelect hostSelect = new ListSelect("Enter a list of hosts using Fully Qualified Domain Name or IP", hosts);

        hostSelect.setRows(6); // perfect length in out case
        hostSelect.setNullSelectionAllowed(true); // user can not 'unselect'
        hostSelect.setMultiSelect(true);
        hostSelect.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                getWindow().showNotification("hosts selected");
            }
        });

        grid.addComponent(hostSelect, 2, 2, 5, 9);

        Button next = new Button("Next");
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                for (Iterator i = hostSelect.getItemIds().iterator(); i.hasNext();) {
                    Agent agent = (Agent) i.next();

                    if (clusterName.getValue().toString().length() > 0) {
                        int reqSeqNumber = cassandraWizard.getTask().getIncrementedReqSeqNumber();
                        UUID taskUuid = cassandraWizard.getTask().getUuid();
                        List<String> args = new ArrayList<String>();
                        changeNameCommand = changeNameCommand.replace("%name", clusterName.getValue().toString());
                        Command command = buildCommand(agent.getUuid(), changeNameCommand, reqSeqNumber, taskUuid, args);
                        cassandraWizard.runCommand(command);
                        cassandraWizard.getCluster().setName(clusterName.getValue().toString());
                        cassandraWizard.showNext();
                    } else {
                        getWindow().showNotification(
                                "Please provide cluster name.",
                                Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                }
            }
        });
        Button back = new Button("Back");
        back.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                cassandraWizard.showBack();
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(back);
        horizontalLayout.addComponent(next);

        verticalLayout.addComponent(grid);
        verticalLayout.addComponent(horizontalLayout);

        addComponent(verticalLayout);
    }

    private Command buildCommand(UUID uuid, String program, int reqSeqNumber, UUID taskUuid, List<String> args) {

        Request request = new Request();
        request.setSource("CassandraModule");
        request.setProgram(program);
        request.setUuid(uuid);
        request.setType(RequestType.EXECUTE_REQUEST);
        request.setTaskUuid(taskUuid);
        request.setWorkingDirectory("/");
        request.setStdOut(OutputRedirection.RETURN);
        request.setStdErr(OutputRedirection.RETURN);
        request.setRunAs("root");
        request.setTimeout(0);
        request.setArgs(args);
        request.setRequestSequenceNumber(reqSeqNumber);
        Command command = new Command(request);

        return command;
    }

}
