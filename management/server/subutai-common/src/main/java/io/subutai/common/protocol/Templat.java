package io.subutai.common.protocol;


//TODO rename to Template after migration complete
public class Templat
{
    private String hash;
    private String md5;
    private long size;
    private String name;
    private String version;


    public Templat( final String hash, final String md5, final long size, final String name, final String version )
    {
        this.hash = hash;
        this.md5 = md5;
        this.size = size;
        this.name = name;
        this.version = version;
    }


    @Override
    public String toString()
    {
        return "{" + "hash='" + hash + '\'' + ", md5='" + md5 + '\'' + ", size=" + size + ", name='" + name + '\''
                + ", version='" + version + '\'' + '}';
    }
}
