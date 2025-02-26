package com.coastee.server.login.infrastructure;

import com.coastee.server.global.apipayload.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_VALIDATE_SESSION;

@Component
public class SessionManager {
    private static final String USER_ID_KEY = "userId";
    private static final int EXPIRATION_TIME = 1800;

    public void setSession(
            final Long userId,
            final HttpServletRequest request
    ) {
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ID_KEY, userId);
        session.setMaxInactiveInterval(EXPIRATION_TIME);
    }

    public Long getUserId(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new GeneralException(FAIL_VALIDATE_SESSION);
        }
        return (Long) session.getAttribute(USER_ID_KEY);
    }

    public void removeSession(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
