package com.work.media.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kiulian.downloader.model.videos.formats.Format;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author linux
 */
@Data
@Builder
@ToString
public class FormatDTO {

    private Integer id;
    private String type;
    private String quality;
    private String contentLength;
    private String label;
    @JsonIgnore
    private Format format;
}
