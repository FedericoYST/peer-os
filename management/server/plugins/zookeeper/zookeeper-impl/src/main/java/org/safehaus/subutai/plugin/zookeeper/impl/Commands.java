/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.subutai.plugin.zookeeper.impl;


import java.util.HashSet;
import java.util.Set;

import org.safehaus.subutai.api.commandrunner.AgentRequestBuilder;
import org.safehaus.subutai.api.commandrunner.Command;
import org.safehaus.subutai.api.commandrunner.CommandsSingleton;
import org.safehaus.subutai.api.commandrunner.RequestBuilder;
import org.safehaus.subutai.shared.protocol.Agent;
import org.safehaus.subutai.shared.protocol.Util;
import org.safehaus.subutai.shared.protocol.enums.OutputRedirection;


/**
 * @author dilshat
 */
public class Commands extends CommandsSingleton {

    public static Command getCheckInstalledCommand( Set<Agent> agents ) {
        return createCommand( new RequestBuilder( "dpkg -l | grep '^ii' | grep ksks" ), agents );
    }


    public static Command getInstallCommand( Set<Agent> agents ) {
        return createCommand( new RequestBuilder( "sleep 10 ; apt-get --force-yes --assume-yes install ksks-zookeeper" )
                .withTimeout( 90 ).withStdOutRedirection( OutputRedirection.NO ), agents );
    }


    public static Command getUninstallCommand( Set<Agent> agents ) {
        return createCommand(
                new RequestBuilder( "apt-get --force-yes --assume-yes purge ksks-zookeeper" ).withTimeout( 90 )
                                                                                             .withStdOutRedirection(
                                                                                                     OutputRedirection.NO ),
                agents );
    }


    public static Command getStartCommand( Set<Agent> agents ) {
        return createCommand( new RequestBuilder( "service zookeeper start" ).withTimeout( 15 ), agents );
    }


    public static Command getRestartCommand( Set<Agent> agents ) {
        return createCommand( new RequestBuilder( "service zookeeper restart" ).withTimeout( 15 ), agents );
    }


    public static Command getStopCommand( Agent agent ) {
        return createCommand( new RequestBuilder( "service zookeeper stop" ), Util.wrapAgentToSet( agent ) );
    }


    public static Command getStatusCommand( Agent agent ) {
        return createCommand( new RequestBuilder( "service zookeeper status" ), Util.wrapAgentToSet( agent ) );
    }


    public static Command getConfigureClusterCommand( Set<Agent> agents, String myIdFilePath, String zooCfgFileContents,
                                                      String zooCfgFilePath ) {
        Set<AgentRequestBuilder> requestBuilders = new HashSet<>();

        int id = 0;
        for ( Agent agent : agents ) {
            requestBuilders.add( new AgentRequestBuilder( agent,
                    String.format( "echo '%s' > %s && echo '%s' > %s", ++id, myIdFilePath, zooCfgFileContents,
                            zooCfgFilePath ) ) );
        }

        return createCommand( requestBuilders );
    }


    public static Command getAddPropertyCommand( String fileName, String propertyName, String propertyValue,
                                                 Set<Agent> agents ) {
        return createCommand( new RequestBuilder(
                String.format( ". /etc/profile && zookeeper-property.sh add %s %s %s", fileName, propertyName,
                        propertyValue ) ), agents );
    }


    public static Command getRemovePropertyCommand( String fileName, String propertyName, Set<Agent> agents ) {
        return createCommand( new RequestBuilder(
                String.format( ". /etc/profile && zookeeper-property.sh remove %s %s", fileName, propertyName ) ),
                agents );
    }
}
