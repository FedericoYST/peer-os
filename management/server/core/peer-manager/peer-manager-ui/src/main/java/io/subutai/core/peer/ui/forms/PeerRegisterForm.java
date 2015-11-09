package io.subutai.core.peer.ui.forms;


import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import io.subutai.common.environment.Environment;
import io.subutai.common.peer.ContainerHost;
import io.subutai.common.peer.Peer;
import io.subutai.common.peer.PeerInfo;
import io.subutai.common.peer.PeerStatus;
import io.subutai.common.peer.ResourceHost;
import io.subutai.common.security.utils.io.HexUtil;
import io.subutai.common.settings.ChannelSettings;
import io.subutai.common.settings.SecuritySettings;
import io.subutai.common.util.JsonUtil;
import io.subutai.common.util.RestUtil;
import io.subutai.common.peer.ManagementHost;
import io.subutai.core.peer.ui.PeerManagerPortalModule;
import io.subutai.core.security.api.crypto.EncryptionTool;
import io.subutai.core.security.api.crypto.KeyManager;
import io.subutai.server.ui.component.ConfirmationDialog;


/**
 * Registration process should be handled in save manner so no middleware attacks occur. In order to get there peers
 * need to exchange with public keys. This will create ssl layer by encrypting all traffic passing through their
 * connection. So first initial handshake will be one direction, to pass keys through encrypted channel and register
 * them in peers' trust stores. These newly saved keys will be used further for safe communication, with bidirectional
 * authentication.
 *
 *
 * TODO here still exists some issues concerned via registration/reject/approve requests. Some of them must pass through
 * secure channel such as unregister process. Which already must be in bidirectional auth completed stage.
 */
public class PeerRegisterForm extends CustomComponent
{

    private static final Logger LOG = LoggerFactory.getLogger( PeerRegisterForm.class.getName() );
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private AbsoluteLayout mainLayout;
    private Table peersTable;
    private Button showPeersButton;
    private Button registerRequestButton;
    private TextField ipTextField;
    private TextField secretKeyphraseTF;

    private PeerManagerPortalModule module;


    /**
     * The constructor should first build the main layout, set the composition root and then do any custom
     * initialization. <p/> The constructor will not be automatically regenerated by the visual editor.
     */
    public PeerRegisterForm( final PeerManagerPortalModule module )
    {
        buildMainLayout();
        setCompositionRoot( mainLayout );

        this.module = module;

        showPeersButton.click();
    }


    @AutoGenerated
    private AbsoluteLayout buildMainLayout()
    {
        String componentFullSize = "100%";
        // common part: create layout
        mainLayout = new AbsoluteLayout();
        mainLayout.setImmediate( false );
        mainLayout.setWidth( componentFullSize );
        mainLayout.setHeight( componentFullSize );

        // top-level component properties
        setWidth( componentFullSize );
        setHeight( componentFullSize );

        // peerRegisterLayout
        final AbsoluteLayout peerRegisterLayout = buildAbsoluteLayout();
        mainLayout.addComponent( peerRegisterLayout, "top:20.0px;right:0.0px;bottom:-20.0px;left:0.0px;" );

        return mainLayout;
    }


    @AutoGenerated
    private AbsoluteLayout buildAbsoluteLayout()
    {

        // common part: create layout
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.setImmediate( false );
        absoluteLayout.setWidth( "100.0%" );
        absoluteLayout.setHeight( "100.0%" );

        // peerRegistration
        final Label peerRegistration = new Label();
        peerRegistration.setImmediate( false );
        peerRegistration.setWidth( "-1px" );
        peerRegistration.setHeight( "-1px" );
        peerRegistration.setValue( "Peer registration" );
        absoluteLayout.addComponent( peerRegistration, "top:0.0px;left:20.0px;" );

        // ip
        final Label ip = new Label();
        ip.setImmediate( false );
        ip.setWidth( "-1px" );
        ip.setHeight( "-1px" );
        ip.setValue( "IP" );
        absoluteLayout.addComponent( ip, "top:36.0px;left:20.0px;" );

        final Label secretKeyphrase = new Label();
        secretKeyphrase.setImmediate( false );
        secretKeyphrase.setWidth( "-1px" );
        secretKeyphrase.setHeight( "-1px" );
        secretKeyphrase.setValue( "Secret keyphrase" );
        absoluteLayout.addComponent( secretKeyphrase, "top:68.0px;left:20.0px;" );


        // ipTextField
        ipTextField = new TextField();
        ipTextField.setImmediate( false );
        ipTextField.setWidth( "-1px" );
        ipTextField.setHeight( "-1px" );
        ipTextField.setMaxLength( 15 );
        absoluteLayout.addComponent( ipTextField, "top:36.0px;left:150.0px;" );

        // secretKeyphrase
        secretKeyphraseTF = new TextField();
        secretKeyphraseTF.setImmediate( false );
        secretKeyphraseTF.setWidth( "-1px" );
        secretKeyphraseTF.setHeight( "-1px" );
        secretKeyphraseTF.setMaxLength( 40 );
        absoluteLayout.addComponent( secretKeyphraseTF, "top:68.0px;left:150.0px;" );

        // registerRequestButton
        registerRequestButton = createRegisterButton();
        absoluteLayout.addComponent( registerRequestButton, "top:160.0px;left:20.0px;" );
        registerRequestButton = createRegisterButton();

        // showPeersButton
        showPeersButton = createShowPeersButton();
        absoluteLayout.addComponent( showPeersButton, "top:234.0px;left:20.0px;" );

        // peersTable
        peersTable = new Table();
        peersTable.setCaption( "Peers" );
        peersTable.setImmediate( false );
        peersTable.setWidth( "1000px" );
        peersTable.setHeight( "283px" );
        absoluteLayout.addComponent( peersTable, "top:294.0px;left:20.0px;" );

        return absoluteLayout;
    }


    private Button createShowPeersButton()
    {
        showPeersButton = new Button();
        showPeersButton.setCaption( "Show peers" );
        showPeersButton.setImmediate( false );
        showPeersButton.setWidth( "-1px" );
        showPeersButton.setHeight( "-1px" );

        showPeersButton.addClickListener( new Button.ClickListener()
        {
            @Override
            public void buttonClick( final Button.ClickEvent clickEvent )
            {
                populateData();
                peersTable.refreshRowCache();
            }
        } );

        return showPeersButton;
    }


    private void populateData()
    {
        List<PeerInfo> peers = module.getPeerManager().getPeerInfos();
        peersTable.removeAllItems();
        peersTable.addContainerProperty( "ID", String.class, null );
        peersTable.addContainerProperty( "Name", String.class, null );
        peersTable.addContainerProperty( "IP", String.class, null );
        peersTable.addContainerProperty( "Status", PeerStatus.class, null );
        peersTable.addContainerProperty( "KeyPhrase", String.class, null );
        peersTable.addContainerProperty( "ActionsAdvanced", PeerManageActionsComponent.class, null );

        for ( final PeerInfo peer : peers )
        {
            if ( peer == null || peer.getStatus() == null )
            {
                continue;
            }
            /**
             * According to peer status perform sufficient action
             */
            //TODO perform different actions on peer response
            PeerManageActionsComponent.PeerManagerActionsListener listener =
                    new PeerManageActionsComponent.PeerManagerActionsListener()

                    {
                        @Override
                        public void OnPositiveButtonTrigger( final PeerInfo peer,
                                                             PeerManageActionsComponent.PeerManageUpdateViewListener
                                                                     updateViewListener )
                        {
                            positiveActionTrigger( peer, updateViewListener );
                        }


                        @Override
                        public void OnNegativeButtonTrigger( final PeerInfo peer,
                                                             PeerManageActionsComponent.PeerManageUpdateViewListener
                                                                     updateViewListener )
                        {
                            negativeActionTrigger( peer, updateViewListener );
                        }
                    };
            PeerManageActionsComponent component = new PeerManageActionsComponent( module, peer, listener );
            peersTable.addItem( new Object[] {
                    peer.getId(), peer.getName(), peer.getIp(), peer.getStatus(), peer.getKeyPhrase(), component
            }, peer.getId() );
        }
    }


    private void positiveActionTrigger( final PeerInfo peer,
                                        final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                updateViewListener )
    {
        switch ( peer.getStatus() )
        {
            case REQUESTED:
                PeerInfo selfPeer = module.getPeerManager().getLocalPeerInfo();
                approvePeerRegistration( selfPeer, peer, updateViewListener );
                break;
            case REGISTERED:
                break;
            case BLOCKED:
                break;
            default:
                throw new IllegalStateException( peer.getStatus().name(), new Throwable( "Invalid case." ) );
        }
    }


    private void negativeActionTrigger( final PeerInfo peer,
                                        final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                updateViewListener )
    {
        PeerInfo selfPeer = module.getPeerManager().getLocalPeerInfo();
        switch ( peer.getStatus() )
        {
            case REJECTED:
                removeMeFromRemote( selfPeer, peer, updateViewListener );
                break;
            case BLOCKED:
            case BLOCKED_PEER:
            case REQUESTED:
            case REQUEST_SENT:
                rejectPeerRegistration( selfPeer, peer, updateViewListener );
                break;
            case APPROVED:
                unregisterMeFromRemote( selfPeer, peer, updateViewListener );
                break;
            default:
                throw new TypeNotPresentException( peer.getStatus().name(), new Throwable( "Invalid case." ) );
        }
    }


    /**
     * Send peer to register on remote peer. To construct secure connection. For now initializer peer doesn't send its
     * px2 (public key requiring bidirectional authentication).
     *
     * @param peerToRegister - initializer peer info for registration process
     * @param ip - target peer ip address
     */
    private void registerMeToRemote( final PeerInfo peerToRegister, final String ip )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                registrationRequest( peerToRegister, ip );
            }
        } ).start();
    }


    /* *************************************************************
     *
     */
    private void registrationRequest( final PeerInfo peerToRegister, final String ip )
    {
        KeyManager keyManager = module.getSecurityManager().getKeyManager();
        PGPPublicKey pkey = keyManager.getRemoteHostPublicKey( null, ip ); //Get PublicKey from KeyServer


        if ( pkey != null )
        {
            String baseUrl = String.format( "https://%s:%s/cxf", ip, ChannelSettings.SECURE_PORT_X1 );
            WebClient client = RestUtil.createTrustedWebClient( baseUrl );
            client.type( MediaType.MULTIPART_FORM_DATA ).accept( MediaType.APPLICATION_JSON );
            Form form = new Form();

            try
            {
                // Encrypt Payload, Save as Hex String
                //********************************************************
                EncryptionTool encTool = module.getSecurityManager().getEncryptionTool();
                String jsonData = gson.toJson( peerToRegister );
                byte[] data = encTool.encrypt( jsonData.getBytes(), pkey, false );
                //********************************************************

                form.set( "peer", HexUtil.byteArrayToHexString( data ) );

                Response response = client.path( "peer/register" ).form( form );
                if ( response.getStatus() == Response.Status.OK.getStatusCode() )
                {
                    Notification.show( String.format( "Request sent to %s!", ip ) );
                    String responseString = response.readEntity( String.class );
                    LOG.info( response.toString() );
                    data = encTool.decrypt( HexUtil.hexStringToByteArray( responseString ) );

                    PeerInfo remotePeerInfo = JsonUtil.fromJson( new String( data ), new TypeToken<PeerInfo>()
                    {}.getType() );
                    registerPeer( remotePeerInfo );
                }
                else if ( response.getStatus() == Response.Status.CONFLICT.getStatusCode() )
                {
                    String reason = response.readEntity( String.class );
                    Notification.show( reason, Notification.Type.WARNING_MESSAGE );
                    LOG.warn( reason );
                }
                else
                {
                    LOG.warn( "Response for registering peer: " + response.toString() );
                }
            }
            catch ( Exception e )
            {
                Notification.show( "Please check peer address for correctness", Notification.Type.WARNING_MESSAGE );
                LOG.error( "error sending request", e );
            }
        }
    }


    private void registerPeer( final PeerInfo remotePeerInfo )
    {
        if ( remotePeerInfo != null )
        {
//            remotePeerInfo.setStatus( PeerStatus.REQUEST_SENT );
//            try
//            {
//                module.getPeerManager().register( remotePeerInfo );
//            }
//            catch ( PeerException e )
//            {
//                Notification.show( "Couldn't register peer. " + e.getMessage(), Notification.Type.WARNING_MESSAGE );
//                LOG.error( "Couldn't register peer", e );
//            }
        }
    }


    private void unregisterMeFromRemote( final PeerInfo peerToUnregister, final PeerInfo remotePeerInfo,
                                         final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                 updateViewListener )
    {
        int relationExists = 0;
        relationExists = checkEnvironmentExistence( remotePeerInfo, relationExists );

        relationExists = isRemotePeerContainersHost( remotePeerInfo, relationExists );

        if ( relationExists != 0 )
        {
            String msg;
            switch ( relationExists )
            {
                case 1:
                    msg = "Please destroy all cross peer environments, before you proceed!!!";
                    break;
                case 2:
                    msg = "You cannot unregister Peer, because you are a carrier of Peer's resources!!!"
                            + " Contact with Peer to migrate all his data.";
                    break;
                default:
                    msg = "Cannot break peer relationship.";
            }
            ConfirmationDialog alert = new ConfirmationDialog( msg, "Ok", "" );
            alert.getOk().addClickListener( new Button.ClickListener()
            {
                @Override
                public void buttonClick( Button.ClickEvent clickEvent )
                {
                }
            } );

            getUI().addWindow( alert.getAlert() );
        }
        else
        {
            unregisterPeerRequestThread( peerToUnregister, remotePeerInfo, updateViewListener );
        }
    }


    private int checkEnvironmentExistence( final PeerInfo remotePeerInfo, final int relationExist )
    {
        int relationExists = relationExist;
        for ( final Iterator<Environment> itEnv = module.getEnvironmentManager().getEnvironments().iterator();
              itEnv.hasNext() && relationExists == 0; )
        {
            Environment environment = itEnv.next();
            for ( final Iterator<Peer> itPeer = environment.getPeers().iterator();
                  itPeer.hasNext() && relationExists == 0; )
            {
                Peer peer = itPeer.next();
                if ( peer.getPeerInfo().equals( remotePeerInfo ) )
                {
                    relationExists = 1;
                }
            }
        }
        return relationExists;
    }


    private int isRemotePeerContainersHost( final PeerInfo remotePeerInfo, final int relationExist )
    {
        int relationExists = relationExist;
        for ( final Iterator<ResourceHost> itResource =
                      module.getPeerManager().getLocalPeer().getResourceHosts().iterator();
              itResource.hasNext() && relationExists == 0; )
        {
            ResourceHost resourceHost = itResource.next();
            for ( final Iterator<ContainerHost> itContainer = resourceHost.getContainerHosts().iterator();
                  itContainer.hasNext() && relationExists == 0; )
            {
                ContainerHost containerHost = itContainer.next();

                if ( remotePeerInfo.getId().equals( containerHost.getInitiatorPeerId() ) )
                {
                    relationExists = 2;
                }

            }
        }
        return relationExists;
    }


    private void unregisterPeerRequestThread( final PeerInfo peerToUnregister, final PeerInfo remotePeerInfo,
                                              final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                      updateViewListener )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                unregisterPeerRequest( remotePeerInfo, peerToUnregister, updateViewListener );
            }
        } ).start();
    }


    private void unregisterPeerRequest( final PeerInfo remotePeerInfo, final PeerInfo peerToUnregister,
                                        final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                updateViewListener )
    {
        String baseUrl = String.format( "https://%s:%s/cxf", remotePeerInfo.getIp(), ChannelSettings.SECURE_PORT_X2 );
        WebClient client = RestUtil.createTrustedWebClientWithAuth( baseUrl, SecuritySettings.KEYSTORE_PX2_ROOT_ALIAS );

        //*********construct Secure Header ****************************
        //client.header( Common.HEADER_SPECIAL, "ENC" );
        //client.header( Common.HEADER_PEER_ID_TARGET, remotePeerInfo.getId().toString() );
        //client.header( Common.HEADER_PEER_ID_SOURCE, peerToUnregister.getId().toString() );
        //*************************************************************

        try
        {
            Response response = client.path( "peer/unregister" ).type( MediaType.APPLICATION_JSON )
                                      .accept( MediaType.APPLICATION_JSON )
                                      .query( "peerId", peerToUnregister.getId().toString() ).delete();
            if ( response.getStatus() == Response.Status.OK.getStatusCode() )
            {
                LOG.info( response.toString() );
                Notification.show( String.format( "Request sent to %s!", remotePeerInfo.getName() ) );

                //************ Delete Trust SSL Cert and Remove KeyRing *****************

                module.getSecurityManager().getKeyStoreManager()
                      .removeCertFromTrusted( ChannelSettings.SECURE_PORT_X2, remotePeerInfo.getId().toString() );

//                module.getPeerManager().unregister( remotePeerInfo.getId().toString() );

                module.getHttpContextManager().reloadTrustStore();

                //***********************************************************************


                getUI().access( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        peersTable.removeItem( remotePeerInfo.getId() );
                    }
                } );
            }
            else
            {
                LOG.warn( String.format( "Failed to unregister remote peer %d", response.getStatus() ) );
                Notification.show( String.format( "Failed to unregister remote peer %d", response.getStatus() ),
                        Notification.Type.WARNING_MESSAGE );
            }
        }
//        catch ( PeerException e )
//        {
//            LOG.error( "Error in unregister peer", e );
//        }
        catch ( Exception e )
        {
            LOG.error( "Error sending unregister request to remote peer.", e );
            Notification.show( "Error sending unregister request to remote peer.", Notification.Type.WARNING_MESSAGE );
        }
        if ( updateViewListener != null )
        {
            updateViewListener.updateViewCallback();
        }
    }


    private void removeMeFromRemote( final PeerInfo peerToUnregister, final PeerInfo remotePeerInfo,
                                     final PeerManageActionsComponent.PeerManageUpdateViewListener updateViewListener )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                removeMeFromRemoteRequest( peerToUnregister, remotePeerInfo, updateViewListener );
            }
        } ).start();
    }


    private void removeMeFromRemoteRequest( final PeerInfo peerToUnregister, final PeerInfo remotePeerInfo,
                                            final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                    updateViewListener )
    {
        String baseUrl = String.format( "https://%s:%s/cxf", remotePeerInfo.getIp(), ChannelSettings.SECURE_PORT_X1 );
        WebClient client = RestUtil.createTrustedWebClient( baseUrl );
//        try
//        {
            Response response =
                    client.path( "peer/remove" ).type( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON )
                          .query( "rejectedPeerId", peerToUnregister.getId().toString() ).delete();
            if ( response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() )
            {
                LOG.info( response.toString() );
                Notification.show( String.format( "Request sent to %s!", remotePeerInfo.getName() ) );
                getUI().access( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        peersTable.removeItem( remotePeerInfo.getId() );
                    }
                } );

//                module.getPeerManager().unregister( remotePeerInfo.getId().toString() );
            }
            else
            {
                LOG.warn( "Response for registering peer: " + response.toString() );
                Notification.show( "Failed to remove remote peer", Notification.Type.WARNING_MESSAGE );
            }
//        }
//        catch ( PeerException e )
//        {
//            LOG.error( "Error sending remove peer request", e );
//            Notification.show( "Error sending remove peer request", Notification.Type.WARNING_MESSAGE );
//        }
        if ( updateViewListener != null )
        {
            updateViewListener.updateViewCallback();
        }
    }


    private void approvePeerRegistration( final PeerInfo peerToUpdateOnRemote, final PeerInfo remotePeer,
                                          final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                  updateViewListener )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                approvePeerRegistrationRequest( peerToUpdateOnRemote, remotePeer, updateViewListener );
            }
        } ).start();
    }


    private void approvePeerRegistrationRequest( final PeerInfo peerToUpdateOnRemote, final PeerInfo remotePeer,
                                                 final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                         updateViewListener )
    {
        //************ Send Trust SSL Cert **************************************
        KeyManager keyMan = module.getSecurityManager().getKeyManager();
        EncryptionTool encTool = module.getSecurityManager().getEncryptionTool();
        PGPPublicKey pkey = keyMan.getRemoteHostPublicKey( remotePeer.getId().toString(), remotePeer.getIp() );
        //***********************************************************************

        // Encrypt Payload and Certificate, Save as Hex String
        //********************************************************
        String certHEX = module.getSecurityManager().getKeyStoreManager()
                               .exportCertificate( ChannelSettings.SECURE_PORT_X2, "" );
        String jsonData = gson.toJson( peerToUpdateOnRemote );

        byte[] data = encTool.encrypt( jsonData.getBytes(), pkey, false );
        byte[] cert = encTool.encrypt( certHEX.getBytes(), pkey, false );

        //********************************************************
        String baseUrl = String.format( "https://%s:%s/cxf", remotePeer.getIp(), ChannelSettings.SECURE_PORT_X1 );
        WebClient client = RestUtil.createTrustedWebClient( baseUrl );
        client.type( MediaType.APPLICATION_FORM_URLENCODED ).accept( MediaType.APPLICATION_JSON );

        Form form = new Form();
        form.set( "approvedPeer", HexUtil.byteArrayToHexString( data ) );
        form.set( "cert", HexUtil.byteArrayToHexString( cert ) );

        try
        {
            Response response = client.path( "peer/approve" ).put( form );
            if ( response.getStatus() == Response.Status.OK.getStatusCode() )
            {
                String rootCertPx2 = response.readEntity( String.class );
                cert = encTool.decrypt( HexUtil.hexStringToByteArray( rootCertPx2 ) );
                //adding remote repository
                ManagementHost managementHost = module.getPeerManager().getLocalPeer().getManagementHost();
                managementHost.addRepository( remotePeer.getIp() );
                remotePeer.setStatus( PeerStatus.APPROVED );

                Notification.show( String.format( "Request sent to %s!", remotePeer.getName() ) );

                //************ Save Trust SSL Cert **************************************

                module.getSecurityManager().getKeyStoreManager()
                      .importCertAsTrusted( ChannelSettings.SECURE_PORT_X2, remotePeer.getId().toString(),
                              new String( cert ) );
                module.getHttpContextManager().reloadTrustStore();

                //***********************************************************************

                remotePeer.setStatus( PeerStatus.APPROVED );


                getUI().access( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Property property = peersTable.getItem( remotePeer.getId() ).getItemProperty( "Status" );
                        property.setValue( remotePeer.getStatus() );
                    }
                } );

                module.getPeerManager().update( remotePeer );
            }
            else
            {
                LOG.warn( "Response for registering peer: " + response.toString() );
                Notification.show( "Error approving peer request", Notification.Type.WARNING_MESSAGE );
            }
        }
        catch ( Exception e )
        {
            LOG.error( "Error sending approve request #approvePeerRegistrationRequest", e );
            Notification.show( "Couldn't send approval request to peer", Notification.Type.ERROR_MESSAGE );
        }
        if ( updateViewListener != null )
        {
            updateViewListener.updateViewCallback();
        }
    }


    /**
     * Peer request rejection intented to be handled before they exchange with keys
     *
     * @param peerToUpdateOnRemote - local peer info to update/send to remote peer
     * @param remotePeer - remote peer whose request was rejected
     * @param updateViewListener - used to update peers table with relevant buttons captions
     */
    private void rejectPeerRegistration( final PeerInfo peerToUpdateOnRemote, final PeerInfo remotePeer,
                                         final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                 updateViewListener )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                rejectPeerRegistrationRequest( peerToUpdateOnRemote, remotePeer, updateViewListener );
            }
        } ).start();
    }


    private void rejectPeerRegistrationRequest( final PeerInfo peerToUpdateOnRemote, final PeerInfo remotePeer,
                                                final PeerManageActionsComponent.PeerManageUpdateViewListener
                                                        updateViewListener )
    {
        String baseUrl = String.format( "https://%s:%s/cxf", remotePeer.getIp(), ChannelSettings.SECURE_PORT_X1 );
        WebClient client = RestUtil.createTrustedWebClient( baseUrl );
        client.type( MediaType.APPLICATION_FORM_URLENCODED ).accept( MediaType.APPLICATION_JSON );

        Form form = new Form();
        form.set( "rejectedPeerId", peerToUpdateOnRemote.getId().toString() );

        try
        {
            Response response = client.path( "peer/reject" ).put( form );
            if ( response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() )
            {
                LOG.info( "Successfully reject peer request" );
                Notification.show( String.format( "Request sent to %s!", remotePeer.getName() ) );
                remotePeer.setStatus( PeerStatus.REJECTED );
                getUI().access( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Property property = peersTable.getItem( remotePeer.getId() ).getItemProperty( "Status" );
                        property.setValue( remotePeer.getStatus() );
                    }
                } );
                module.getPeerManager().update( remotePeer );
            }
            else
            {
                LOG.warn( "Response for registering peer: " + response.toString() );
            }
        }
        catch ( Exception e )
        {
            LOG.error( "Rejecting peer registration failed", e );
            Notification.show( "Peer reject request failed", Notification.Type.WARNING_MESSAGE );
        }
        if ( updateViewListener != null )
        {
            updateViewListener.updateViewCallback();
        }
    }


    /**
     * Send peer registration request for further handshakes.
     *
     * @return - vaadin button with request initializing click listener
     */
    private Button createRegisterButton()
    {
        registerRequestButton = new Button();
        registerRequestButton.setCaption( "Register" );
        registerRequestButton.setImmediate( true );
        registerRequestButton.setWidth( "-1px" );
        registerRequestButton.setHeight( "-1px" );

        registerRequestButton.addClickListener( new Button.ClickListener()
        {
            @Override
            public void buttonClick( final Button.ClickEvent clickEvent )
            {
                getUI().access( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String ip = ipTextField.getValue();
                        LOG.warn( ip );

                        PeerInfo selfPeer = module.getPeerManager().getLocalPeerInfo();
                        selfPeer.setKeyPhrase( secretKeyphraseTF.getValue() );
                        registerMeToRemote( selfPeer, ip );
                        showPeersButton.click();
                    }
                } );
            }
        } );

        return registerRequestButton;
    }
}