package net.ncue.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

public class JdbcDaoOverImpl extends JdbcDaoImpl {
	private String usersByUsernameQuery;

	public void setUsersByUsernameQuery (String usersByUsernameQuery) {
		this.usersByUsernameQuery = usersByUsernameQuery;
	}

	private String authoritiesByUsernameQuery;

	public void setAuthoritiesByUsernameQuery (String authoritiesByUsernameQuery) {
		this.authoritiesByUsernameQuery = authoritiesByUsernameQuery;
	}

	private String rolePrefix = super.getRolePrefix ();

	public JdbcDaoOverImpl () {
		super ();
	}

	protected void initMappingSqlQueries () {
		this.usersByUsernameMapping = new UsersByUsernameMapping (getDataSource ());
		this.authoritiesByUsernameMapping = new AuthoritiesByUsernameMapping (getDataSource ());
	}

	/**
	 * Query object to look up a user.
	 */
	protected class UsersByUsernameMapping extends MappingSqlQuery {
		protected UsersByUsernameMapping (DataSource ds) {
			super (ds, usersByUsernameQuery);
			declareParameter (new SqlParameter (Types.VARCHAR));
			compile ();
		}

		protected Object mapRow (ResultSet rs, int rownum) throws SQLException {
			String username = rs.getString (1);
			String password = rs.getString (2);
			String enabled = rs.getString (3);

			// 권한 주기
			boolean enableFlag = false;
			if (enabled.equals ("Y")) {
				enableFlag = true;
			}

			UserDetails user = new User (username, password, enableFlag, true, true, true,
					new GrantedAuthority[] { new GrantedAuthorityImpl ("HOLDER") });

			return user;
		}
	}

	/**
	 * Query object to look up a user's authorities.
	 */
	protected class AuthoritiesByUsernameMapping extends MappingSqlQuery {
		protected AuthoritiesByUsernameMapping (DataSource ds) {
			super (ds, authoritiesByUsernameQuery);
			declareParameter (new SqlParameter (Types.VARCHAR));
			compile ();
		}

		protected Object mapRow (ResultSet rs, int rownum) throws SQLException {
			String roleName = rolePrefix + rs.getString (2);
			GrantedAuthorityImpl authority = new GrantedAuthorityImpl (roleName);

			return authority;
		}
	}
}
