package com.mk.shiro.config.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author mk.chen
 */
public class RedissonSessionManager extends AbstractNativeSessionManager implements WebSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(RedissonSessionManager.class);

    private SessionFactory sessionFactory;

    private SessionDAO sessionDAO;

    public RedissonSessionManager() {
        this.sessionFactory = new SimpleSessionFactory();
    }

    @Override
    protected Session createExposedSession(Session session, SessionContext context) {
        if (!WebUtils.isWeb(context)) {
            return super.createExposedSession(session, context);
        }
        return doCreateExposedSession(session, context);
    }

    @Override
    protected Session createExposedSession(Session session, SessionKey key) {
        if (!WebUtils.isWeb(key)) {
            return super.createExposedSession(session, key);
        }
        return doCreateExposedSession(session, key);
    }

    private Session doCreateExposedSession(Session session, Object source) {
        ServletRequest request = WebUtils.getRequest(source);
        ServletResponse response = WebUtils.getResponse(source);
        SessionKey key = new WebSessionKey(session.getId(), request, response);
        return new DelegatingSession(this, key);
    }

    @Override
    protected Session createSession(SessionContext context) throws AuthorizationException {
        Session s = newSessionInstance(context);
        if (logger.isTraceEnabled()) {
            logger.trace("Creating session for host {}", s.getHost());
            System.out.println("1哈哈哈 Creating session for host {}" + s.getHost());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Creating new EIS record for new session instance [" + s + "]");
            System.out.println("2哈哈哈 Creating new EIS record for new session instance [" + s);
        }
        sessionDAO.create(s);
        return s;
    }

    @Override
    protected Session doGetSession(SessionKey key) throws InvalidSessionException {
        if (logger.isTraceEnabled()) {
            logger.trace("Attempting to retrieve session with key {}", key);
        }
        Serializable sessionId = getSessionId(key);
        if (sessionId == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a " +
                        "session could not be found.", key);
            }
            return null;
        }
        Session s = sessionDAO.readSession(sessionId);
        if (s == null) {
            //session ID was provided, meaning one is expected to be found, but we couldn't find one:
            String msg = "Could not find session with ID [" + sessionId + "]";
            throw new UnknownSessionException(msg);
        }
        return s;
    }

    @Override
    protected void afterStopped(Session session) {
        this.sessionDAO.delete(session);
    }

    protected Session newSessionInstance(SessionContext context) {
        long globalSessionTimeout = super.getGlobalSessionTimeout();
        Session session = getSessionFactory().createSession(context);
        session.setTimeout(globalSessionTimeout);
        return session;
    }

    protected Serializable getSessionId(SessionKey sessionKey) {
        Serializable id = sessionKey.getSessionId();
        if (id == null && WebUtils.isWeb(sessionKey)) {
            ServletRequest request = WebUtils.getRequest(sessionKey);
            id = getSessionId(request);
        }
        return id;
    }

    protected Serializable getSessionId(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = httpServletRequest.getParameter("token");
        }
        return token;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }
}
