package com.screenscraper.service;

import com.screenscraper.mapper.RecordDTO;

import java.util.List;

public interface ScreenScraperService {

    List<RecordDTO> getData();

    List<String> getZipCodes(String zipCode, String miles);
}
