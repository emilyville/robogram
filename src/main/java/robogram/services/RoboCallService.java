package robogram.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.twilio.sdk.verbs.Play;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Path("/robocall")
@Produces(MediaType.APPLICATION_XML)
public class RoboCallService {

	@GET
	public Response handleCallRequest(@QueryParam("message") String message) {
		TwiMLResponse response = new TwiMLResponse();
		UriBuilder uriBuilder = UriBuilder.fromPath("http://www.robogram.me/services/tts");
		uriBuilder.queryParam("text", message);
		Play play = new Play(uriBuilder.build().toString());
		try {
			response.append(play);
		} catch (TwiMLException e) {
			return Response.serverError().build();
		}
		return Response.ok(response.toXML()).build();
	}
}
