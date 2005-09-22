package org.geworkbench.builtin.projects.remoteresources;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
/**
 *A simple wrapper class for resources
 */
public class RemoteResource {
    private String username;
    private String password;
    private String connectProtocol;
    private String DEFAULTPROTOCAL = "http";
    private String shortname;
    private String uri;
    private int portnumber = 8;
    public RemoteResource() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RemoteResource(String url, String protocal, String user,
                          String passwd) {
        this("Default", url, protocal, user, passwd);
    }

    public RemoteResource(String shortname, String url, String protocal,
                          String user, String passwd) {
        uri = url;
        connectProtocol = protocal;
        username = user;
        password = passwd;
        this.shortname = shortname;
    }

    public RemoteResource(String shortname, String url, String port,
                          String protocal,
                          String user, String passwd) {
        uri = url;
        connectProtocol = protocal;
        username = user;
        password = passwd;
        this.shortname = shortname;
        try {
            if (new Integer(port).intValue() != 0) {
                portnumber = new Integer(port).intValue();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            portnumber = 80;
        }
        ;
    }


    public static RemoteResource createNewInstance(String[] columns) {
        if (columns.length == 6) {
            return new RemoteResource(columns[0], columns[1], columns[2],
                                      columns[3], columns[4], columns[5]);
        } else {
            return null;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnectProtocal(String connectProtocol) {
        this.connectProtocol = connectProtocol;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPortnumber(int portnumber) {
        this.portnumber = portnumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConnectProtocal() {
        return connectProtocol;
    }

    public String getShortname() {
        return shortname;
    }

    public String getUri() {
        return uri;
    }

    public int getPortnumber() {
        return portnumber;
    }

    /**
     * update
     *
     * @param rResource RemoteResource
     */
    public void update(RemoteResource rResource) {
        shortname = rResource.shortname;
        uri = rResource.uri;
        username = rResource.username;
        password = rResource.password;
        connectProtocol = rResource.connectProtocol;
    }

    /**
     * Use shortname as the Key for every object.
     * @param obj Object
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj instanceof RemoteResource) {
            return shortname.equals(((RemoteResource) obj).shortname);
        } else {
            return false;
        }
    }

    private void jbInit() throws Exception {
    }
}
