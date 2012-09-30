package net.javaforge.blog.atmosphere;

import java.io.IOException;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.cpr.MetaBroadcaster;
import org.atmosphere.handler.AbstractReflectorAtmosphereHandler;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AtmosphereHandlerService
public class UserSessionAwareAtmosphereHandler extends
		AbstractReflectorAtmosphereHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserSessionAwareAtmosphereHandler.class);

	@Override
	public void onRequest(AtmosphereResource resource) throws IOException {

		suspendAtmosphereResourceIfNecessary(resource);

		if ("POST".equalsIgnoreCase(resource.getRequest().getMethod())) {
			doBroadcast(resource);
		}

	}

	private void doBroadcast(AtmosphereResource resource) throws IOException {

		AtmosphereRequest req = resource.getRequest();

		String incomingMessage = req.getReader().readLine();
		LOG.info("Incoming message: '{}'", incomingMessage);

		Broadcaster broadcaster = null;
		if (incomingMessage.startsWith("@/broadcaster/")) {
			String broadcasterId = incomingMessage.substring(1,
					incomingMessage.indexOf(" "));
			broadcaster = lookupBroadcaster(broadcasterId);
		} else {
			broadcaster = lookupBroadcaster(req);
		}

		LOG.info("Broadcasting message with broadcaster  = {}",
				broadcaster.getID());
		broadcaster.broadcast("ACK! Sent message was: <strong>"
				+ incomingMessage + "</strong>");

	}

	private void suspendAtmosphereResourceIfNecessary(
			AtmosphereResource resource) {

		AtmosphereRequest req = resource.getRequest();
		AtmosphereResponse resp = resource.getResponse();
		String method = req.getMethod();

		if ("GET".equalsIgnoreCase(method)) {

			LOG.info("GET request detected, suspending broadcaster...");

			// Log all events on the console, including WebSocket events.
			resource.addEventListener(new WebSocketEventListenerAdapter());

			resp.setContentType("text/html;charset=ISO-8859-1");

			Broadcaster b = lookupBroadcaster(req);
			resource.setBroadcaster(b);

			if (req.getHeader(HeaderConfig.X_ATMOSPHERE_TRANSPORT)
					.equalsIgnoreCase(HeaderConfig.LONG_POLLING_TRANSPORT)) {
				req.setAttribute(ApplicationConfig.RESUME_ON_BROADCAST,
						Boolean.TRUE);
				resource.suspend(-1, false);
			} else {
				resource.suspend(-1);
			}

			LOG.info("Broadcasting notification message to all connected users...");
			MetaBroadcaster.getDefault().broadcastTo(
					"/broadcaster/*",
					"new broadcaster connected: <strong>" + b.getID()
							+ "</strong>");
		}

	}

	@Override
	public void destroy() {

	}

	private Broadcaster lookupBroadcaster(AtmosphereRequest req) {
		String broadcasterId = (String) req.getSession().getAttribute(
				BroadcasterCreater.BROADCASTER_ID_KEY);
		return lookupBroadcaster(broadcasterId);
	}

	private Broadcaster lookupBroadcaster(String broadcasterId) {
		LOG.info("Looking up for broadcaster: {}", broadcasterId);
		Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(
				broadcasterId);

		LOG.info("Broadcaster found : {}", broadcaster.getID());
		return broadcaster;
	}

}
