package org.safehaus.subutai.plugin.mongodb.impl.handler;


import java.util.UUID;

import org.safehaus.subutai.api.lxcmanager.LxcDestroyException;
import org.safehaus.subutai.plugin.mongodb.api.MongoClusterConfig;
import org.safehaus.subutai.plugin.mongodb.impl.MongoImpl;
import org.safehaus.subutai.shared.operation.AbstractOperationHandler;
import org.safehaus.subutai.shared.operation.ProductOperation;
import org.safehaus.subutai.shared.protocol.Agent;


/**
 * Created by dilshat on 5/6/14.
 */
public class UninstallOperationHandler extends AbstractOperationHandler<MongoImpl> {
    private final ProductOperation po;


    public UninstallOperationHandler( MongoImpl manager, String clusterName ) {
        super( manager, clusterName );
        po = manager.getTracker().createProductOperation( MongoClusterConfig.PRODUCT_KEY,
                String.format( "Destroying cluster %s", clusterName ) );
    }


    @Override
    public UUID getTrackerId() {
        return po.getId();
    }


    @Override
    public void run() {
        MongoClusterConfig config = manager.getCluster( clusterName );
        if ( config == null ) {
            po.addLogFailed( String.format( "Cluster with name %s does not exist", clusterName ) );
            return;
        }

        po.addLog( "Destroying lxc containers" );
        try {
            for ( Agent agent : config.getAllNodes() ) {
                manager.getContainerManager().cloneDestroy( agent.getParentHostName(), agent.getHostname() );
            }
            po.addLog( "Lxc containers successfully destroyed" );
        }
        catch ( LxcDestroyException ex ) {
            po.addLog( String.format( "%s, skipping...", ex.getMessage() ) );
        }

        po.addLog( "Updating db..." );
        if ( manager.getDbManager().deleteInfo( MongoClusterConfig.PRODUCT_KEY, config.getClusterName() ) ) {
            po.addLogDone( "Cluster info deleted from DB\nDone" );
        }
        else {
            po.addLogFailed( "Error while deleting cluster info from DB. Check logs\nFailed" );
        }
    }
}
