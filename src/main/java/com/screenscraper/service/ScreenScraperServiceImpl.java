package com.screenscraper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.screenscraper.ScreenScraperApplication;
import com.screenscraper.Utility;
import com.screenscraper.mapper.RecordDTO;
import com.screenscraper.model.ZipCode;
import com.screenscraper.model.ZipCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScreenScraperServiceImpl implements ScreenScraperService {

    private static Logger logger = LoggerFactory.getLogger(ScreenScraperServiceImpl.class);
    private static boolean USE_INSECURE_SSL = true;
    private static boolean SET_CSS_ENABLED = false;
    private static boolean JAVA_SCRIPT_ENABLED = true;
    private static String URL = "https://www.zipcodeapi.com/rest/CPUnaj3Ul8Yygj2YL5CAFsuPioKAjH4mklLCSgcr1hbZGOQEgsHJT4LMk87oS1AZ/radius.json/%s/%s/miles";

    @Override
    public List<RecordDTO> getData() {
        String baseUrl = "file:///C:/workspace/Client/Mr.%20Shahab/Screen%20scraper/virginia-sales.html";
//        String baseUrl = "/virginia-sales.html";
//        String baseUrl = "http://www.bww-law.com/virginia-sales/";
        WebClient webClient = Utility.getWebClient(USE_INSECURE_SSL, SET_CSS_ENABLED, JAVA_SCRIPT_ENABLED);
        List<RecordDTO> list = new ArrayList<>();
        try {
//            String searchUrl = baseUrl + "search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
//            webClient.waitForBackgroundJavaScript(20000);
            HtmlPage htmlPage = webClient.getPage(baseUrl);
//            HtmlPage htmlPage = webClient.getPage(ScreenScraperServiceImpl.class.getResource(baseUrl));


            List<HtmlElement> items = htmlPage.getByXPath("//td[@class='bodytext']");
            htmlPage.getByXPath("//*[contains(text(),'VA-357600-1')]"); // getByXPath("//*[contains(text(),'123')]")
            htmlPage.getByXPath("//iframe[@id='virginia-sales']");

            HtmlInlineFrame htmlInlineFrame = (HtmlInlineFrame) htmlPage.getByXPath("//iframe[@id='virginia-sales']").get(0);

            List<FrameWindow> frames = htmlPage.getFrames();
            HtmlPage page = null;
            for (FrameWindow frame : frames) {
                if (frame.getFrameElement().getId().equals("virginia-sales")) {
                    page = (HtmlPage) frame.getEnclosedPage();
                }
            }
            HtmlTable htmlTable = ((HtmlTable) page.getByXPath("//table").get(2));
//            htmlTable.getRow(0).getByXPath("td[@colspan='8']");
//            List<HtmlTableDataCell> countyDomAttrs =  htmlTable.getByXPath("//td[@class='subhead']");
            List<DomAttr> countyDomAttrs = htmlTable.getByXPath("//tr/td[@class='subhead']/a/@name");

            List<String> counties = new ArrayList<>();
            for (DomAttr county : countyDomAttrs) {
//                List<HtmlAnchor> anchors = subHeadTD.getByXPath("a");
//                HtmlAnchor anchor = (anchors.get(0));
//                List<DomAttr> names = anchor.getByXPath("@name");
                counties.add(county.getValue());
            }
            //((HtmlTableRow) county.getParentNode().getParentNode().getParentNode()).getNextSibling().getByXPath(".//following-sibling::tr[@valign='top']")
            //////////////////////////
            Map<String, Map<String, String>> listing = new HashMap<>();
            List<HtmlTableRow> htmlTableRows = htmlTable.getRows();
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode rows = mapper.createArrayNode();
            JsonNode row = mapper.createObjectNode();
            if (!htmlTableRows.isEmpty()) {
                HtmlTableRow header = htmlTableRows.get(0);
                List<String> heading = htmlTableRows.get(0).getByXPath("td//text()[normalize-space()]");
                boolean newRecord = false;
//                RecordDTO recordDTO = null;
//                String currentJurisdiction = null;
                for (int i = 1; i < htmlTableRows.size(); i++) {
                    List<DomAttr> jurisdiction = htmlTableRows.get(i).getByXPath("./td[@class='subhead']/a/@name");
                    List<HtmlTableDataCell> record = htmlTableRows.get(i).getByXPath("./td[@class='bodytext']");
                    if (jurisdiction.isEmpty() && record.isEmpty()) {
                        continue;
                    }
                    if (!jurisdiction.isEmpty()) {
                        newRecord = true;
//                        if (recordDTO.getJurisdiction() != null) {
//                            list.add(recordDTO);
//                        }
//                        recordDTO = new RecordDTO();
//                        recordDTO.setJurisdiction(jurisdiction.get(0).getValue());
//                        currentJurisdiction = jurisdiction.get(0).getValue();
                    } else if (!record.isEmpty()) {
                        newRecord = false;
                        RecordDTO recordDTO = new RecordDTO();
//                        recordDTO.setJurisdiction(currentJurisdiction);
//                        recordDTO.setFileNumber(((DomText) record.get(1).getByXPath("./text()[normalize-space()]").get(0)).getTextContent());
                        recordDTO.setFileNumber(record.get(1).getTextContent());
                        recordDTO.setSaleDate(record.get(2).getTextContent());
                        recordDTO.setSaleTime(record.get(3).getTextContent());
                        recordDTO.setPropertyAddress(record.get(4).getTextContent());
                        recordDTO.setCity(record.get(5).getTextContent());
                        recordDTO.setZip(record.get(6).getTextContent());
                        recordDTO.setOriginalLoanAmount(record.get(7).getTextContent());
                        list.add(recordDTO);
                    }
                }
//                list.add(recordDTO);
            }
            //////////////////////////
//            List<Object> titleTD = htmlTable.getByXPath("//td[@colspan='8']");
//            System.out.println(page.getTitleText());
//            System.out.println(htmlPage.getTitleText());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    public static void main(String[] a) throws IOException {
        new ScreenScraperServiceImpl().getZipCodes("22204", "5");
    }

    @Override
    public List<String> getZipCodes(String zipCode, String miles) {
        ZipCodeResponse zipCodeResponse = null;
        try {
            URL url = new URL(String.format(URL, zipCode, miles));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
// Open a connection(?) on the URL(??) and cast the response(???)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
// Now it's "open", we can set the request method, headers etc.
            connection.setRequestProperty("accept", "application/json");
// This line makes the request
            InputStream responseStream = connection.getInputStream();
// Manually converting the response body InputStream to APOD using Jackson
            ObjectMapper mapper = new ObjectMapper();
             zipCodeResponse = mapper.readValue(responseStream, ZipCodeResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipCodeResponse.getZip_codes().stream().map(code -> code.getZip_code()).collect(Collectors.toList());
    }

}
