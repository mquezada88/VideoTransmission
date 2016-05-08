package com.company;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class Encode
{
    public void Encode()
    {
        FileChannelWrapper ch = null;
        try
        {
            ch = NIOUtils.readableFileChannel(new File("video_1.mp4"));
            FrameGrab fg = new FrameGrab(ch);
            for (int i = 0; i < 100; i++)
            {
                /*ImageIO.write(fg.getFrame(), "png",
                        new File(System.getProperty("user.home"), String.format("Desktop/frame_%08d.png", i)));*/
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JCodecException e) {
            e.printStackTrace();
        } finally {
            NIOUtils.closeQuietly(ch);
        }
    }

    public void FFTest() throws IOException {
        FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
        FFprobe ffprobe = new FFprobe("/path/to/ffprobe");

        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput("input.mp4")     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("output.mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setTargetSize(250_000)  // Aim for a 250KB file

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();

        // Or run a two-pass encode (which is slower at the cost of better quality)
        executor.createTwoPassJob(builder).run();
    }
}