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
	@Path("/incoming")
	public Response handleIncomingCall() {
		TwiMLResponse response = new TwiMLResponse();
		String message = "Hello, this is the robot.  I can't come to the phone " +
				"right now because I am busy sending row bo grams.  If you would " +
				"like to send ah row bo gram, please visit w w w dot row bo gram " +
				"dot m e";

		UriBuilder uriBuilder = UriBuilder
				.fromPath("http://www.robogram.me/services/tts");
		uriBuilder.queryParam("text", message);
		Play play = new Play(uriBuilder.build().toString());
		try {
			response.append(play);
		} catch (TwiMLException e) {
			return Response.serverError().build();
		}
		return Response.ok(response.toXML()).build();
	}
	
	@GET
	public Response handleCallRequest(@QueryParam("message") String message) {
		TwiMLResponse response = new TwiMLResponse();
		UriBuilder uriBuilder = UriBuilder
				.fromPath("http://www.robogram.me/services/tts");
		message += ". Thank you. To send your own message visit w w w dot row bo gram dot m e";
		uriBuilder.queryParam("text", message);
		Play silence = new Play("http://www.robogram.me/assets/audio/silence.wav");
		Play play = new Play(uriBuilder.build().toString());
		try {
			response.append(silence);
			response.append(play);
		} catch (TwiMLException e) {
			return Response.serverError().build();
		}
		return Response.ok(response.toXML()).build();
	}
}
