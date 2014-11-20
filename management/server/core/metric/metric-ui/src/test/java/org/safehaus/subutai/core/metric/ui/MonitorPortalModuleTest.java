package org.safehaus.subutai.core.metric.ui;


import java.io.PrintStream;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.subutai.common.util.ServiceLocator;
import org.safehaus.subutai.core.environment.api.EnvironmentManager;
import org.safehaus.subutai.core.environment.api.helper.Environment;
import org.safehaus.subutai.core.metric.api.Monitor;

import com.google.common.collect.Lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith( MockitoJUnitRunner.class )
public class MonitorPortalModuleTest
{
    @Mock
    ServiceLocator serviceLocator;
    @Mock
    Monitor monitor;
    @Mock
    EnvironmentManager environmentManager;

    MonitorPortalModule module;


    @Before
    public void setUp() throws Exception
    {
        module = new MonitorPortalModule();
        module.serviceLocator = serviceLocator;
        when( serviceLocator.getService( Monitor.class ) ).thenReturn( monitor );
        when( serviceLocator.getService( EnvironmentManager.class ) ).thenReturn( environmentManager );
    }


    @Test
    public void testProperties() throws Exception
    {
        assertEquals( MonitorPortalModule.MODULE_NAME, module.getName() );
        assertEquals( MonitorPortalModule.MODULE_NAME, module.getId() );
        assertNotNull( module.getImage() );
        assertTrue( module.isCorePlugin() );
    }


    @Test
    public void testCreateComponent() throws Exception
    {
        when( environmentManager.getEnvironments() ).thenReturn( Lists.<Environment>newArrayList() );


        assertTrue( module.createComponent() instanceof MonitorForm );

        NamingException exception = mock( NamingException.class );

        doThrow( exception ).when( serviceLocator ).getService( any( Class.class ) );

        module.createComponent();

        verify( exception ).printStackTrace( any( PrintStream.class ) );
    }
}