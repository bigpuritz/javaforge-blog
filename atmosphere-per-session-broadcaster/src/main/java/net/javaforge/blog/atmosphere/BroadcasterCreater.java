package net.javaforge.blog.atmosphere;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcasterCreater implements HttpSessionListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(BroadcasterCreater.class);

	public static final String BROADCASTER_ID_KEY = "net.javaforge.blog.atmosphere.broadcasterId";

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		String broadcasterId = "/broadcaster/" + session.getId();

		LOG.info("Creating broadcaster: {}", broadcasterId);
		BroadcasterFactory.getDefault().lookup(broadcasterId, true);
		session.setAttribute(BROADCASTER_ID_KEY, broadcasterId);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		String broadcasterId = (String) session
				.getAttribute(BROADCASTER_ID_KEY);

		LOG.info("Removing broadcaster: {}", broadcasterId);
		BroadcasterFactory.getDefault().remove(broadcasterId);
	}

}
