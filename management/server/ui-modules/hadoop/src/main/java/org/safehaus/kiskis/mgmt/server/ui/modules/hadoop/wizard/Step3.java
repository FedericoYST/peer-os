/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.kiskis.mgmt.server.ui.modules.hadoop.wizard;

import com.google.common.base.Strings;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.safehaus.kiskis.mgmt.shared.protocol.Task;
import org.safehaus.kiskis.mgmt.shared.protocol.enums.TaskStatus;

/**
 * @author bahadyr
 */
public class Step3 extends Panel {
    private TextArea terminal;
    HadoopWizard parent;
    Button next;

    public Step3(final HadoopWizard hadoopWizard) {
        parent = hadoopWizard;

        setCaption("Welcome to Hadoop Cluster Installation");
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
        verticalLayout.setMargin(true);

        GridLayout grid = new GridLayout(6, 10);
        grid.setSpacing(true);
        grid.setSizeFull();

        Panel panel = new Panel();
        Label menu = new Label("Cluster Install Wizard<br>"
                + " 1) Master Configurations<br>"
                + " 2) Slave Configurations<br>"
                + " 3) <font color=\"#f14c1a\"><strong>Installation</strong></font><br>");
        menu.setContentMode(Label.CONTENT_XHTML);
        panel.addComponent(menu);
        grid.addComponent(menu, 0, 0, 0, 5);
        grid.setComponentAlignment(panel, Alignment.TOP_CENTER);

        VerticalLayout verticalLayoutForm = new VerticalLayout();
        verticalLayoutForm.setSizeFull();
        verticalLayoutForm.setSpacing(true);

        terminal = new TextArea();
        terminal.setRows(20);
        terminal.setColumns(40);
        terminal.setImmediate(true);
        terminal.setWordwrap(false);
        verticalLayoutForm.addComponent(terminal);

        grid.addComponent(verticalLayoutForm, 1, 0, 5, 9);
        grid.setComponentAlignment(verticalLayoutForm, Alignment.MIDDLE_CENTER);

        next = new Button("Finish");
        next.setEnabled(false);
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.showNext();
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(next);
        horizontalLayout.addComponent(getButtonConfigureHadoop());
        horizontalLayout.addComponent(getButtonConfigureSecondaryNameNode());
        horizontalLayout.addComponent(getButtonSetSlaveNameNode());


        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.addComponent(getButtonSetSlaveJobTracker());
        horizontalLayout2.addComponent(getButtonSetSSH());
        horizontalLayout2.addComponent(getButtonSetSSHMaster());
        horizontalLayout2.addComponent(getButtonCopySSHSlaves());

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.addComponent(getButtonConfigSSHMaster());
        horizontalLayout3.addComponent(getButtonFormatMaster());

        verticalLayout.addComponent(grid);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.addComponent(horizontalLayout2);
        verticalLayout.addComponent(horizontalLayout3);

        addComponent(verticalLayout);

        parent.setClosable(true);
        parent.getHadoopInstallation().installHadoop();
    }

    public void addOutput(Task task, String stdResult) {
        if (task.getTaskStatus().compareTo(TaskStatus.SUCCESS) == 0) {
            if (!Strings.isNullOrEmpty(stdResult) && !stdResult.equals("null")) {
                StringBuffer str = new StringBuffer();
                str.append("\n");
                str.append(task.getUuid() + " ");
                str.append(task.getDescription());
                str.append(stdResult);
                terminal.setValue(str);
            }
        } else {
            if (!Strings.isNullOrEmpty(stdResult) && !stdResult.equals("null")) {
                StringBuffer str = new StringBuffer();
                str.append("\n");
                str.append(task.getDescription());
                str.append(stdResult);
                terminal.setValue(str);
            }
        }
    }

    private Button getButtonConfigureHadoop() {
        final Button button = new Button("Configure");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().configureHadoop();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonConfigureSecondaryNameNode() {
        final Button button = new Button("Configure SNameNode");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().configureSNameNode();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonSetSlaveNameNode() {
        final Button button = new Button("SetSlaveNameNode");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().setSlaveNameNode();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonSetSlaveJobTracker() {
        final Button button = new Button("setSlaveJobTracker");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().setSlaveJobTracker();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonSetSSH() {
        final Button button = new Button("SetSSH");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().setSSH();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonSetSSHMaster() {
        final Button button = new Button("SetSSHMaster");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().setSSHMaster();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonCopySSHSlaves() {
        final Button button = new Button("CopySSHSlaves");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().copySSHSlaves();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonConfigSSHMaster() {
        final Button button = new Button("ConfigSSHMaster");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().configSSHMaster();
                button.setEnabled(false);
            }
        });

        return button;
    }

    private Button getButtonFormatMaster() {
        final Button button = new Button("formatMaster");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.getHadoopInstallation().formatMaster();
                button.setEnabled(false);
            }
        });

        return button;
    }
}
