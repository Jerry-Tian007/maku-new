package com.ruoyi.gpt.core.ldap;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

public class LdapUtils {
    public static LdapTemplate getldapTemplate(String url , String username, String password){
        LdapContextSource contextSource = new LdapContextSource();
        // LDAP服务器地址和端口
        contextSource.setUrl(url);
        // 管理身份的DN
        contextSource.setUserDn(username);
        // 管理身份的密码
        contextSource.setPassword(password);
        contextSource.afterPropertiesSet();
        return new LdapTemplate(contextSource);
    }
    public static LdapAuthenticator getldapAuthenticator(String url , String username, String password, String searchBase, String searchFilter) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(url);
        contextSource.setUserDn(username);
        contextSource.setPassword(password);
        contextSource.afterPropertiesSet();
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserSearch(new FilterBasedLdapUserSearch(searchBase, searchFilter, contextSource));
        return authenticator;
    }
}
