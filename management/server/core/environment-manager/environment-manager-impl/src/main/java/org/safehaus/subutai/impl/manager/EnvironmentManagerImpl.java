/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.subutai.impl.manager;


import java.util.List;

import org.safehaus.subutai.api.container.ContainerManager;
import org.safehaus.subutai.api.dbmanager.DbManager;
import org.safehaus.subutai.api.manager.EnvironmentManager;
import org.safehaus.subutai.api.manager.helper.Environment;
import org.safehaus.subutai.api.manager.helper.EnvironmentBlueprint;
import org.safehaus.subutai.api.manager.util.BlueprintParser;
import org.safehaus.subutai.impl.manager.builder.EnvironmentBuilder;
import org.safehaus.subutai.impl.manager.dao.EnvironmentDAO;
import org.safehaus.subutai.impl.manager.exception.EnvironmentBuildException;
import org.safehaus.subutai.impl.manager.exception.EnvironmentInstanceDestroyException;


/**
 * This is an implementation of LxcManager
 */
public class EnvironmentManagerImpl implements EnvironmentManager {

    private EnvironmentDAO environmentDAO;
    private EnvironmentBuilder environmentBuilder;
    private BlueprintParser blueprintParser;
    private ContainerManager containerManager;


    private DbManager dbManager;


    public void setDbManager( final DbManager dbManager ) {
        this.dbManager = dbManager;
        this.environmentDAO = new EnvironmentDAO( dbManager );
    }


    public EnvironmentManagerImpl() {
        this.environmentBuilder = new EnvironmentBuilder();
        this.blueprintParser = new BlueprintParser();
    }


    /**
     * Builds an environment by provided blueprint description
     */
    @Override
    public boolean buildEnvironment( String blueprintStr ) {

        EnvironmentBlueprint blueprint =
                ( EnvironmentBlueprint ) blueprintParser.parseEnvironmentBlueprint( blueprintStr );
        return build( blueprint );
    }


    public boolean buildEnvironment( EnvironmentBlueprint blueprint ) {
        return build( blueprint );
    }


    private boolean build( EnvironmentBlueprint blueprint ) {
        if ( blueprint != null ) {
            try {
                Environment environment = environmentBuilder.build( blueprint, containerManager );
                boolean saveResult = environmentDAO.saveEnvironment( environment );
                if ( !saveResult ) {
                    //rollback build action.
                    environmentBuilder.destroy( environment );
                    return false;
                }
                return true;
            }
            catch ( EnvironmentBuildException e ) {
                System.out.println( e.getMessage() );
            }
            finally {
                Environment e = new Environment();
                e.setName( blueprint.getName() );
                environmentDAO.saveEnvironment( e );
                return false;
            }
        }
        return false;
    }


    @Override
    public List<Environment> getEnvironments() {
        List<Environment> environments = environmentDAO.getEnvironments();
        return environments;
    }


    @Override
    public Environment getEnvironmentInfo( final String environmentName ) {
        Environment environment = environmentDAO.getEnvironment( environmentName );
        return environment;
    }


    @Override
    public boolean destroyEnvironment( final String environmentName ) {
        Environment environment = getEnvironmentInfo( environmentName );
        try {
            environmentBuilder.destroy( environment );
            //TODO environmentDAO.deleteEnvironmentInfo( environment.getName() );
            return true;
        }
        catch ( EnvironmentInstanceDestroyException e ) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean saveBlueprint( String blueprintStr ) {
        EnvironmentBlueprint blueprint =
                ( EnvironmentBlueprint ) blueprintParser.parseEnvironmentBlueprint( blueprintStr );
        boolean saveResult = environmentDAO.saveBlueprint( blueprint );
        return saveResult;
    }


    @Override
    public List<EnvironmentBlueprint> getBlueprints() {
        List<EnvironmentBlueprint> blueprints = environmentDAO.getBlueprints();
        return blueprints;
    }


    @Override
    public boolean deleteBlueprint( String blueprintName ) {
        return environmentDAO.deleteBlueprint( blueprintName );
    }


    public void setContainerManager( final ContainerManager containerManager ) {
        this.containerManager = containerManager;
    }
}
