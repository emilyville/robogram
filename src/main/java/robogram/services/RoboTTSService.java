package robogram.services;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import robogram.OutputStreamAudioPlayer;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;

@Path("/tts")
@Produces({ "audio/x-wav" })
public class RoboTTSService {

	@GET
	public StreamingOutput getAudio(final @QueryParam("text") String text) {
		return new StreamingOutput() {

			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				VoiceManager voiceManager = VoiceManager.getInstance();
				String voiceName = "kevin16";
				Voice helloVoice = voiceManager.getVoice(voiceName);

				if (helloVoice == null) {
					System.exit(1);
				}
				helloVoice.setRate(120);
				System.out.println("Creating audio player");
				AudioPlayer player = new OutputStreamAudioPlayer(output);
				helloVoice.setAudioPlayer(player);
				/*
				 * Allocates the resources for the voice.
				 */
				System.out.println("Allocating voice");
				helloVoice.allocate();

				/*
				 * Synthesize speech.
				 */
				System.out.println("Speaking");
				boolean success = helloVoice.speak(text);
				System.out.println("speak returned " + success);
				/*
				 * Clean up and leave.
				 */
				System.out.println("deallocating voice");
				helloVoice.deallocate();
				player.close();
			}
		};
	}
}
