package io.subutai.common.security;


import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;


public enum SshEncryptionType
{
    RSA, DSA, ECDSA, ED25519, UNKNOWN;


    public static SshEncryptionType parseTypeFromKey( String sshKey )
    {
        if ( !StringUtils.isBlank( sshKey ) )
        {
            if ( sshKey.contains( "ssh-rsa" ) )
            {
                return RSA;
            }
            else if ( sshKey.contains( "ssh-dss" ) )
            {
                return DSA;
            }
            else if ( sshKey.contains( "ssh-ed25519" ) )
            {
                return ED25519;
            }
            else if ( sshKey.contains( "ecdsa-sha2" ) )
            {
                return ECDSA;
            }
        }

        return UNKNOWN;
    }
}
