package com.work.media.service;

import com.work.media.dto.RequestConverterDTO;
import java.io.OutputStream;

/**
 *
 * @author linux
 */
public interface ConverterService {

    void converterToMp3(RequestConverterDTO request, OutputStream output);
}
