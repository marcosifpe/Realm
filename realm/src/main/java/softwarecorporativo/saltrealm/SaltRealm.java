package softwarecorporativo.saltrealm;

import com.sun.appserv.connectors.internal.api.ConnectorRuntime;
import com.sun.appserv.security.AppservRealm;
import static com.sun.enterprise.security.BaseRealm.JAAS_CONTEXT_PARAM;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.common.Util;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.utilities.BuilderHelper;

/**
 *
 * @author MASC
 */
public class SaltRealm extends AppservRealm {

    public static final String PASSWORD_SQL_QUERY = "password-sql-query";
    public static final String GROUPS_SQL_QUERY = "groups-sql-query";
    public static final String JTA_DATA_SOURCE = "jta-data-source";
    public static final String HASH_ALGORITHM = "hash-algorithm";
    public static final String CHARSET = "charset";

    private static DataSource dataSource;

    private Connection getConnection() {
        try {
            synchronized (this) {
                if (dataSource == null) {
                    ActiveDescriptor<ConnectorRuntime> cr = (ActiveDescriptor<ConnectorRuntime>) Util.getDefaultHabitat().getBestDescriptor(BuilderHelper.createContractFilter(ConnectorRuntime.class.getName()));
                    ConnectorRuntime connectorRuntime = Util.getDefaultHabitat().getServiceHandle(cr).getService();
                    dataSource = (DataSource) connectorRuntime.lookupNonTxResource(getJtaDataSource(), false);
                }
            }

            return dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            throw new SaltRealmException(ex);
        }
    }

    private void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new SaltRealmException(ex);
        }
    }

    public String getCharset() {
        return super.getProperty(CHARSET);
    }

    public String getHashAlgorithm() {
        return super.getProperty(HASH_ALGORITHM);
    }

    public String getJtaDataSource() {
        return super.getProperty(JTA_DATA_SOURCE);
    }

    public String getPasswordQuery() {
        return super.getProperty(PASSWORD_SQL_QUERY);
    }

    public String getGroupsQuery() {
        return super.getProperty(GROUPS_SQL_QUERY);
    }

    @Override
    public synchronized void init(Properties properties) throws BadRealmException, NoSuchRealmException {
        setProperty(JAAS_CONTEXT_PARAM, properties.getProperty(JAAS_CONTEXT_PARAM));
        setProperty(PASSWORD_SQL_QUERY, properties.getProperty(PASSWORD_SQL_QUERY));
        setProperty(GROUPS_SQL_QUERY, properties.getProperty(GROUPS_SQL_QUERY));
        setProperty(JTA_DATA_SOURCE, properties.getProperty(JTA_DATA_SOURCE));
        setProperty(HASH_ALGORITHM, properties.getProperty(HASH_ALGORITHM));
        setProperty(CHARSET, properties.getProperty(CHARSET));
    }

    @Override
    public String getAuthType() {
        return "jdbc";
    }

    @Override
    public String getJAASContext() {
        return "saltRealm";
    }

    public boolean authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = true;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(getPasswordQuery());
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String passwd = rs.getString(1);
                String salt = rs.getString(2);

                if (passwd == null || !passwd.equals(getHash(salt, password))) {
                    result = false;
                }
            } else {
                result = false;
            }
        } catch (SQLException ex) {
            throw new SaltRealmException(ex);
        } finally {
            close(rs, stmt, conn);
        }

        return result;
    }

    public String getHash(String salt, String password) {
        try {
            String passwd = salt + password;
            MessageDigest digest = MessageDigest.getInstance(getHashAlgorithm());
            digest.update(passwd.getBytes(Charset.forName(getCharset())));
            return Base64.getEncoder().encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new SaltRealmException(ex);
        }
    }
    
    @Override
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Enumeration getGroupNames(String username) {
        Vector<String> vector = new Vector<String>();
        List<String> groups = getGroupList(username);

        for (String group : groups) {
            vector.add(group);
        }

        return vector.elements();
    }

    public List<String> getGroupList(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> groups = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(getGroupsQuery());
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String group = rs.getString(1);
                groups.add(group);
            }
        } catch (SQLException ex) {
            throw new SaltRealmException(ex);
        } finally {
            close(rs, stmt, conn);
        }

        return groups;
    }

}
