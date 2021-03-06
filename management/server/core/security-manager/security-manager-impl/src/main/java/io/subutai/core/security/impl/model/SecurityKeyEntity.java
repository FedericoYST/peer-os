package io.subutai.core.security.impl.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.subutai.core.security.api.model.SecurityKey;
import io.subutai.core.security.api.model.SecurityKeyTrust;


/**
 * Implementation of Security Identity Data
 */

@Entity
@Table( name = SecurityKeyEntity.TABLE_NAME )
@Access( AccessType.FIELD )
public class SecurityKeyEntity implements SecurityKey
{
    /********* Table name *********/
    static final String TABLE_NAME = "security_key";

    /********* column names *******/

    private static final String IDENTITY_ID_NAME = "identity_id";
    private static final String HOST_IP_NAME = "host_ip";
    private static final String PUBLIC_KEY_FINGERPRINT_NAME = "pkfingerprint";
    private static final String SECRET_KEY_FINGERPRINT_NAME = "skfingerprint";
    private static final String STATUS_NAME = "status";
    private static final String TYPE_NAME = "type";


    @Id
    @Column( name = IDENTITY_ID_NAME )
    private String identityId;

    @Column( name = HOST_IP_NAME )
    private String hostIP;

    @Column( name = PUBLIC_KEY_FINGERPRINT_NAME )
    private String publicKeyFingerprint;

    @Column( name = SECRET_KEY_FINGERPRINT_NAME )
    private String secretKeyFingerprint;

    @Column( name = STATUS_NAME )
    private short status;

    @Column( name = TYPE_NAME )
    private int type = 0;


    //*********************************************
    @Transient
    private List<SecurityKeyTrust> trustedKeys = new ArrayList<>();
    //*********************************************

    @Override
    public String getIdentityId()
    {
        return identityId;
    }


    @Override
    public void setIdentityId( final String identityId )
    {
        this.identityId = identityId;
    }


    @Override
    public short getStatus()
    {
        return status;
    }


    @Override
    public void setStatus( final short status )
    {
        this.status = status;
    }


    @Override
    public int getType()
    {
        return type;
    }


    @Override
    public void setType( final int type )
    {
        this.type = type;
    }


    @Override
    public String getPublicKeyFingerprint()
    {
        return publicKeyFingerprint;
    }


    @Override
    public void setPublicKeyFingerprint( final String publicKeyFingerprint )
    {
        this.publicKeyFingerprint = publicKeyFingerprint;
    }


    @Override
    public String getSecretKeyFingerprint()
    {
        return secretKeyFingerprint;
    }


    @Override
    public void setSecretKeyFingerprint( final String secretKeyFingerprint )
    {
        this.secretKeyFingerprint = secretKeyFingerprint;
    }


    @Override
    public String getHostIP()
    {
        return hostIP;
    }


    @Override
    public void setHostIP( final String hostIP )
    {
        this.hostIP = hostIP;
    }

    @Override
    public List<SecurityKeyTrust> getTrustedKeys()
    {
        return trustedKeys;
    }


    @Override
    public void setTrustedKeys( final List<SecurityKeyTrust> trustedKeys )
    {
        this.trustedKeys = trustedKeys;
    }

}
