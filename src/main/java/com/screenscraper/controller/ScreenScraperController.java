package com.screenscraper.controller;

import com.screenscraper.mapper.RecordDTO;
import com.screenscraper.model.AppData;
import com.screenscraper.service.ScreenScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/screenscraper")
public class ScreenScraperController {

    public final ScreenScraperService screenScraperService;
    @Autowired
    private AppData appData;

    ScreenScraperController(ScreenScraperService screenScraperService) {
        this.screenScraperService = screenScraperService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecordDTO> getData(@RequestParam(required = false) String city,
                                   @RequestParam(required = false) String zip,
                                   @RequestParam(required = false) String miles) {
        return appData.get(city == null ? null : city.trim(), zip == null ? null : zip.trim(),
                miles == null ? null : miles.trim());
    }
}
