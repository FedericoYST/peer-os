package org.safehaus.subutai.core.env.impl.builder;


import java.util.Map;
import java.util.Set;

import org.safehaus.subutai.common.peer.Peer;
import org.safehaus.subutai.core.env.api.build.NodeGroup;
import org.safehaus.subutai.core.env.api.build.Topology;
import org.safehaus.subutai.core.env.impl.entity.EnvironmentContainerImpl;
import org.safehaus.subutai.core.env.impl.exception.EnvironmentBuildException;
import org.safehaus.subutai.core.env.impl.exception.NodeGroupBuildException;
import org.safehaus.subutai.core.peer.api.PeerManager;
import org.safehaus.subutai.core.registry.api.TemplateRegistry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;


/**
 * Builds node groups across peers
 */
public class TopologyBuilder
{

    private final TemplateRegistry templateRegistry;
    private final PeerManager peerManager;
    private final NodeGroupBuilder nodeGroupBuilder;


    public TopologyBuilder( final TemplateRegistry templateRegistry, final PeerManager peerManager )
    {
        Preconditions.checkNotNull( templateRegistry );
        Preconditions.checkNotNull( peerManager );

        this.templateRegistry = templateRegistry;
        this.peerManager = peerManager;
        this.nodeGroupBuilder = new NodeGroupBuilder( templateRegistry, peerManager );
    }


    public Set<EnvironmentContainerImpl> build( Topology topology ) throws EnvironmentBuildException
    {
        Preconditions.checkNotNull( topology );

        Set<EnvironmentContainerImpl> containers = Sets.newHashSet();

        Map<Peer, Set<NodeGroup>> placement = topology.getNodeGroupPlacement();

        for ( Map.Entry<Peer, Set<NodeGroup>> peerPlacement : placement.entrySet() )
        {
            try
            {
                //TODO parallelize container creation across peers

                containers.addAll( nodeGroupBuilder.build( peerPlacement.getKey(), peerPlacement.getValue() ) );
            }
            catch ( NodeGroupBuildException e )
            {
                throw new EnvironmentBuildException(
                        String.format( "Error creating node groups %s on peer %s", peerPlacement.getValue(),
                                peerPlacement.getKey() ), e );
            }
        }

        //TODO implement ssh and hosts linking

        return containers;
    }
}
