package com.screenscraper.model;

import com.screenscraper.mapper.RecordDTO;
import com.screenscraper.service.ScreenScraperService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@ApplicationScope
public class AppData {

    List<RecordDTO> data;

    @Autowired
    private ScreenScraperService screenScraperService;

    public List<RecordDTO> get(String city, String zip, String miles) {
        List<RecordDTO> result = data;
        if (zip != null && !zip.isEmpty() && city != null && !city.isEmpty()) {
            result = data.stream().filter(d -> d.getZip().equals(zip) || city.contains(d.getCity())).collect(Collectors.toList());
        } else if (zip != null && !zip.isEmpty()) {
            result = data.stream().filter(d -> d.getZip().equals(zip)).collect(Collectors.toList());
        } else if (city != null && !city.isEmpty()) {
            result = data.stream().filter(d -> city.contains(d.getCity())).collect(Collectors.toList());
        }
        if (zip != null && !zip.isEmpty() && miles != null && !miles.isEmpty()) {
            List<String> zipCodes = screenScraperService.getZipCodes(zip, miles);
            result = result.stream().filter(r -> zipCodes.contains(r.getZip())).collect(Collectors.toList());
        }
        return result;
    }
}
