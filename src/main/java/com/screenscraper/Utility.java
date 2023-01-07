package com.screenscraper;

import com.gargoylesoftware.htmlunit.WebClient;

public class Utility {

    public static WebClient getWebClient(boolean useInsecureSSL, boolean setCssEnabled, boolean javaScriptEnabled) {
        WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(useInsecureSSL);
        webClient.getOptions().setCssEnabled(setCssEnabled);
        webClient.getOptions().setJavaScriptEnabled(javaScriptEnabled);
        return webClient;
    }
}
