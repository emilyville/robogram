package robogram.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;

@Path("/websend")
@Produces(MediaType.APPLICATION_XML)
public class WebMessageService {

	private static Logger logger = Logger.getLogger(WebMessageService.class
			.getName());
	private static final String HOSTNAME = System.getenv("HOSTNAME");

	@POST
	public Response handleWebMessage(@FormParam("number") String number,
			@FormParam("message") String message) {
		logger.info("Number is " + number);
		logger.info("Message is " + message);
		TwilioRestClient twilio = new TwilioRestClient(
				System.getenv("TWILIO_ACCOUNT_SID"),
				System.getenv("TWILIO_AUTH_TOKEN"));
		Account account = twilio.getAccount();
		CallFactory callFactory = account.getCallFactory();
		Map<String, String> callParams = new HashMap<String, String>();
		callParams.put("To", number);
		callParams.put("From", System.getenv("TWILIO_FROM"));
		callParams.put("Method", "GET");
		UriBuilder uriBuilder = UriBuilder
				.fromPath("http://{hostname}/services/robocall");
		uriBuilder.queryParam("message", message);
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("hostname", HOSTNAME);
		callParams.put("Url", uriBuilder.buildFromMap(urlParams).toString());
		logger.info("Creating call with url: " + callParams.get("Url"));
		try {
			callFactory.create(callParams);
		} catch (TwilioRestException e) {
			return Response.serverError().build();
		}
		return Response.ok().build();
	}
}
