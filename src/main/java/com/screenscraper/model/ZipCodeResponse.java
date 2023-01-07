package com.screenscraper.model;

import java.util.List;

public class ZipCodeResponse {

    public List<ZipCode> getZip_codes() {
        return zip_codes;
    }

    public void setZip_codes(List<ZipCode> zip_codes) {
        this.zip_codes = zip_codes;
    }

    List<ZipCode> zip_codes;
}
