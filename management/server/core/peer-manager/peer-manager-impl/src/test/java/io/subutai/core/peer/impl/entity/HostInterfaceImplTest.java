package io.subutai.core.peer.impl.entity;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import io.subutai.common.host.Interface;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


@RunWith( MockitoJUnitRunner.class )
public class HostInterfaceImplTest
{
    private static final String INTERFACE_NAME = "eth0";
    private static final String IP = "127.0.0.1";
    private static final String MAC = "mac";
    private static final String ID = "123";
    @Mock
    Interface anInterface;
    @Mock
    AbstractSubutaiHost abstractSubutaiHost;

    HostInterfaceImpl hostInterface;


    @Before
    public void setUp() throws Exception
    {
        when( anInterface.getName() ).thenReturn( INTERFACE_NAME );
        when( anInterface.getIp() ).thenReturn( IP );
        when( anInterface.getMac() ).thenReturn( MAC );

        hostInterface = new HostInterfaceImpl( anInterface );
    }


//    @Test
//    public void testSetNGetId() throws Exception
//    {
//        hostInterface.setId( ID );
//
//        assertEquals( ID, hostInterface.getId() );
//    }


    @Test
    public void testSetNGetInterfaceName() throws Exception
    {
        hostInterface.setName( INTERFACE_NAME );

        assertEquals( INTERFACE_NAME, hostInterface.getName() );
    }


    @Test
    public void testSetNGetIp() throws Exception
    {

        hostInterface.setIp( IP );

        assertEquals( IP, hostInterface.getIp() );
    }


    @Test
    public void testSetNGetMac() throws Exception
    {
        hostInterface.setMac( MAC );

        assertEquals( MAC, hostInterface.getMac() );
    }

//
//    @Test
//    public void testSetNGetHost() throws Exception
//    {
//        hostInterface.setHost( abstractSubutaiHost );
//
//        assertEquals( abstractSubutaiHost, hostInterface.getHost() );
//    }
}
