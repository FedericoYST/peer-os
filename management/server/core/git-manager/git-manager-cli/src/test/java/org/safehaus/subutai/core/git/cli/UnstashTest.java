package org.safehaus.subutai.core.git.cli;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.safehaus.subutai.common.protocol.Agent;
import org.safehaus.subutai.core.agent.api.AgentManager;
import org.safehaus.subutai.core.git.api.GitException;
import org.safehaus.subutai.core.git.api.GitManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test for Merge
 */
public class UnstashTest
{

    private ByteArrayOutputStream myOut;
    private static final String AGENT_NOT_CONNECTED_MSG = "Agent not connected";
    private static final String HOSTNAME = "hostname";
    private static final String ERR_MSG = "OOPS";
    private Agent agent = mock( Agent.class );
    private AgentManager agentManager = mock( AgentManager.class );
    private GitManager gitManager = mock( GitManager.class );


    @Before
    public void setUp()
    {
        when( agentManager.getAgentByHostname( HOSTNAME ) ).thenReturn( agent );
        myOut = new ByteArrayOutputStream();
        System.setOut( new PrintStream( myOut ) );
    }


    @After
    public void tearDown()
    {
        System.setOut( System.out );
    }


    private String getSysOut()
    {
        return myOut.toString().trim();
    }


    @Test( expected = NullPointerException.class )
    public void constructorShouldFailOnNullGitManager()
    {
        new Unstash( null, mock( AgentManager.class ) );
    }


    @Test( expected = NullPointerException.class )
    public void constructorShouldFailOnNullAgentManager()
    {
        new Unstash( mock( GitManager.class ), null );
    }


    @Test
    public void shouldFailOnMissingAgent()
    {
        Unstash unstash = new Unstash( mock( GitManager.class ), mock( AgentManager.class ) );


        unstash.doExecute();


        assertEquals( AGENT_NOT_CONNECTED_MSG, getSysOut() );
    }


    @Test
    public void shouldExecuteCommand() throws GitException
    {

        Unstash unstash = new Unstash( gitManager, agentManager );
        unstash.setHostname( HOSTNAME );

        unstash.doExecute();


        verify( gitManager ).unstash( eq( agent ), anyString(), anyString() );
    }


    @Test
    public void shouldThrowException() throws GitException
    {
        Mockito.doThrow( new GitException( ERR_MSG ) ).when( gitManager )
               .unstash( eq( agent ), anyString(), anyString() );
        Unstash unstash = new Unstash( gitManager, agentManager );
        unstash.setHostname( HOSTNAME );


        unstash.doExecute();


        assertEquals( ERR_MSG, getSysOut() );
    }
}
