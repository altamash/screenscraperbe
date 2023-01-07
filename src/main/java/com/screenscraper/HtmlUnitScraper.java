package com.screenscraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.screenscraper.mapper.RecordDTO;
import com.screenscraper.service.ScreenScraperServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class HtmlUnitScraper {
    private static Logger logger = LoggerFactory.getLogger(ScreenScraperServiceImpl.class);
//    private static final String baseUrl = "https://www.nba.com/standings#/";
    public static void main(String[] args) throws IOException {

        /*WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            HtmlPage page = client.getPage(baseUrl);
            System.out.println(page.asXml());

            page.getByXPath("//caption[@class='standings__header']/span");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
//        String url = "https://www.skillsyouneed.com/ps/time-management.html";
//        String baseUrl = "https://newyork.craigslist.org/";
//        String baseUrl = "http://www.bww-law.com/virginia-sales/";
//        String baseUrl = "https://www.glasserlaw.com/New%20Folder/Foreclosure%20Sales.html";
        System.out.println(getRecords());

    }

    public static List<RecordDTO> getRecords() throws IOException {
        String baseUrl = "file:///C:/workspace/Client/Mr.%20Shahab/Screen%20scraper/virginia-sales.html";
//        String searchQuery = "iphone 6s";
        WebClient webClient = Utility.getWebClient(true, false, true);
        List<RecordDTO> list = new ArrayList<>();
        try {
//            String searchUrl = baseUrl + "search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
//            webClient.waitForBackgroundJavaScript(20000);
            HtmlPage htmlPage = webClient.getPage(baseUrl);


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
                String currentJurisdiction = null;
                for (int i = 1; i < htmlTableRows.size(); i++) {
                    List<DomAttr> jurisdiction = htmlTableRows.get(i).getByXPath("./td[@class='subhead']/a/@name");
                    List<HtmlTableDataCell> record = htmlTableRows.get(i).getByXPath("./td[@class='bodytext']");
                    if (jurisdiction.isEmpty() && record.isEmpty()) {
                        continue;
                    }
                    /*if (!jurisdiction.isEmpty()) {
                        newRecord = true;
//                        if (recordDTO.getJurisdiction() != null) {
//                            list.add(recordDTO);
//                        }
//                        recordDTO = new RecordDTO();
//                        recordDTO.setJurisdiction(jurisdiction.get(0).getValue());
                        currentJurisdiction = jurisdiction.get(0).getValue();
                    } else */
                    if (!record.isEmpty()) {
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
}
