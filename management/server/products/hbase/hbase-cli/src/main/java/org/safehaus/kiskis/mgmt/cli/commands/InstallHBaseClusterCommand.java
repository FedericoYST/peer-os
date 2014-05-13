package org.safehaus.kiskis.mgmt.cli.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.safehaus.kiskis.mgmt.api.hbase.Config;
import org.safehaus.kiskis.mgmt.api.hbase.HBase;
import org.safehaus.kiskis.mgmt.api.tracker.Tracker;

import java.util.List;


/**
 * Displays the last log entries
 */
@Command(scope = "hbase", name = "list-clusters", description = "mydescription")
public class InstallHBaseClusterCommand extends OsgiCommandSupport {

    private Tracker tracker;
    private HBase hbaseManager;

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public HBase getHbaseManager() {
        return hbaseManager;
    }

    public void setHbaseManager(HBase hbaseManager) {
        this.hbaseManager = hbaseManager;
    }

    protected Object doExecute() {

//        List<Config> configs = hbaseManager.getClusters();
//        Config config = new Config();
//        config.setClusterName(clusterName);
//        config.set
//        hbaseManager.installCluster()
//        StringBuilder sb = new StringBuilder();
//
//        for(Config config : configs) {
//            sb.append(config.getClusterName()).append("\n");
//        }
//
//        System.out.println(sb.toString());
        System.out.println("install");
        return null;
    }
}
