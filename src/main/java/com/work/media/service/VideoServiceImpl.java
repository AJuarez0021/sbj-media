package com.work.media.service;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.work.media.dto.FormatDTO;
import com.work.media.dto.VideoFormatDTO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author linux
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService{

    private VideoInfo getVideoInfo(String url) {
        String videoId = url;
        int index = videoId.lastIndexOf("v=");
        if (index < 0) {
            throw new IllegalArgumentException("La url " + url + " no es valida");
        }
        videoId = videoId.substring(index + 2);
        log.info("VideoId: {}", videoId);
        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo request = new RequestVideoInfo(videoId);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        return response.data();
    }

    @Override
    public VideoFormatDTO getFormatInfo(String url) {

        VideoInfo videoInfo = getVideoInfo(url);
        String title = videoInfo.details().title();
        long viewCount = videoInfo.details().viewCount();
        String description = videoInfo.details().description();
        int length = videoInfo.details().lengthSeconds();
        boolean downloadable = videoInfo.details().isDownloadable();
        int hours = length / 3600;
        int minutes = (length % 3600) / 60;
        int seconds = length % 60;
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        String thumbnail = "";

        List<String> thumbnails = videoInfo.details().thumbnails();
        for (String str : thumbnails) {
            if (str != null) {
                thumbnail = str;
                break;
            }
        }

        List<FormatDTO> video = new ArrayList<>();
        List<VideoFormat> videoFormats = videoInfo.videoFormats();
        for (VideoFormat videoFormat : videoFormats) {
            var contentLength = videoFormat.contentLength() == null ? 0L : videoFormat.contentLength();
            FormatDTO format = FormatDTO.builder()
                    .id(videoFormat.itag().id())
                    .format(videoFormat)
                    .contentLength(String.format("%.2f Kb", (contentLength / 1024.0)))
                    .label(videoFormat.qualityLabel() + " (." + videoFormat.extension().value() + ")")
                    .quality(videoFormat.videoQuality().name())
                    .type(videoFormat.type())
                    .build();

            video.add(format);
        }

        List<FormatDTO> audio = new ArrayList<>();
        List<AudioFormat> audioFormats = videoInfo.audioFormats();

        for (AudioFormat audioFormat : audioFormats) {
            var contentLength = audioFormat.contentLength() == null ? 0L : audioFormat.contentLength();
            FormatDTO format = FormatDTO.builder()
                    .id(audioFormat.itag().id())
                    .format(audioFormat)
                    .contentLength(String.format("%.2f Kb", (contentLength / 1024.0)))
                    .label(audioFormat.extension().value() + " - " + audioFormat.audioSampleRate() + " - " + audioFormat.averageBitrate() / 1000 + " Kbps")
                    .quality(audioFormat.audioQuality().name())
                    .type(audioFormat.type())
                    .build();

            audio.add(format);

        }

        return VideoFormatDTO.builder()
                .time(time)
                .url(url)
                .downloadable(downloadable)
                .title(title)
                .video(video)
                .audio(audio)
                .thumbnail(thumbnail)
                .viewCount(viewCount)
                .description(description)
                .build();
    }

    @Override
    public void downloadVideo(VideoFormatDTO videoInfo, Integer id, OutputStream output) throws IOException {
        YoutubeDownloader downloader = new YoutubeDownloader();        
        List<FormatDTO> formats = new ArrayList<>();
        formats.addAll(videoInfo.getVideo());
        formats.addAll(videoInfo.getAudio());

        Format formatSelected = null;
        for (FormatDTO f : formats) {
            if (Objects.equals(id, f.getId())) {
                formatSelected = f.getFormat();
                log.info("Se encontro el formato: {} - {}", f.getId(), f.getType());
                break;
            }            
        }

        if (formatSelected == null) {
            throw new IllegalArgumentException("El formato seleccionado no existe");
        }

        log.info("Video: {}", videoInfo.getTitle());               
        RequestVideoStreamDownload request = new RequestVideoStreamDownload(formatSelected, output);
        Response<Void> response = downloader.downloadVideoStream(request);
        log.info("Status: {}", response.status());
    }

}
