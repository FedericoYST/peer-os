package io.subutai.core.template.rest;


import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import io.subutai.common.gson.required.RequiredDeserializer;
import io.subutai.common.protocol.Template;
import io.subutai.common.settings.Common;
import io.subutai.core.template.api.TemplateManager;


public class RestServiceImpl implements RestService
{
    private final TemplateManager templateManager;
    private Gson gson = RequiredDeserializer.createValidatingGson();


    public RestServiceImpl( final TemplateManager templateManager )
    {
        Preconditions.checkNotNull( templateManager );

        this.templateManager = templateManager;
    }


    @Override
    public Response listTemplates()
    {
        List<Template> templates = templateManager.getTemplates().stream().filter(
                n -> !Strings.isNullOrEmpty( n.getName() ) && !n.getName()
                                                                .equalsIgnoreCase( Common.MANAGEMENT_HOSTNAME ) )
                                                  .collect( Collectors.toList() );

        return Response.ok().entity( gson.toJson( templates ) ).build();
    }


    @Override
    public Response listOwnTemplates()
    {
        List<Template> templates = templateManager.getOwnTemplates().stream().filter(
                n -> !Strings.isNullOrEmpty( n.getName() ) && !n.getName()
                                                                .equalsIgnoreCase( Common.MANAGEMENT_HOSTNAME ) )
                                                  .collect( Collectors.toList() );

        return Response.ok().entity( gson.toJson( templates ) ).build();
    }


    @Override
    public Response getFingerprint()
    {
        return Response.ok().entity( templateManager.getFingerprint() ).build();
    }


    @Override
    public Response getObtainedCdnToken()
    {
        return Response.ok().entity( templateManager.getObtainedCdnToken() ).build();
    }


    @Override
    public Response obtainCdnToken( final String signedRequest )
    {
        return Response.ok().entity( templateManager.obtainCdnToken( signedRequest ) ).build();
    }


    @Override
    public Response getCdnTokenRequest()
    {
        return Response.ok().entity( templateManager.getTokenRequest() ).build();
    }


    @Override
     public Response isRegisteredWithCdn()
    {
        return Response.ok().entity( templateManager.isRegisteredWithCdn() ).build();
    }
}
