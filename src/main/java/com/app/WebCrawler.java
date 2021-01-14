package com.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author malvan
 * Crawler travers websites following predefined link deph (8 by default) & max visitied pages limit (10000 by
 * default). The web crawler starts from url & follows links found to dive deeper. The main purpouse of this
 * crauler to detect the present of some tetms on the page & collect statistics.
 *
 * to start: new WebCrawler().craw("https://en.wikipedia.org/wiki/Elon_Musk", 0, 15, "string1", "string2");
 * params: url - "https://en.wikipedia.org/wiki/Elon_Musk";
 *         linkDepth - 0;
 *         maxVisitedPages - 15;
 *         terms - "string1", "string2";
 *
 *  output format: https://en.wikipedia.org/wiki/Elon_Musk 177 571 748
 *         link - https://en.wikipedia.org/wiki/Elon_Musk
 *         "string1" number on the page - 177
 *         "string2" number on the page - 571
 *         total numbers of terms - 748
 *
 * All data output to *.csv file. Top 10 pages (sorted by total hits) print to console & *_topTen.csv file.
 */
public class WebCrawler {
    private final HashSet<String[]> logs;
    private int linkDepth;
    private int maxVisitedPages;
    private final Logger logger;
    private final String allLogsFileName;
    private final String topTenLogsFileName;

    public WebCrawler() {
        logs = new LinkedHashSet<>();
        linkDepth = 8;
        maxVisitedPages = 10000;
        logger = Logger.getLogger("global");
        allLogsFileName = String.format("C:\\Users\\malvan\\Desktop\\%s.csv", LocalDate.now());
        topTenLogsFileName = String.format("C:\\Users\\malvan\\Desktop\\%s_topTen.csv", LocalDate.now());
    }

    public void craw(String url, int linkDepth, int maxVisitedPages, String... terms) {
        if (linkDepth != 0) this.linkDepth = linkDepth;
        if (maxVisitedPages != 0) this.maxVisitedPages = maxVisitedPages;
        craw(url, terms);
    }

    public void craw(String url, String... terms){
        if (isUrlOrTermsNull(url, terms)) return;
        if (!isValidSite(url)) return;

        urlWander(url, terms);
        checkTopTen();
        csvOutput(logs, allLogsFileName);
    }

    private boolean isUrlOrTermsNull(String url, String... terms){
        if (url == null || url.isEmpty() || terms == null || terms.length == 0) {
            logger.log(Level.WARNING, "url & terms should be not null");
            return true;
        }
        return false;
    }

    private boolean isValidSite(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return false;
    }

    private String[] termsCounter(String url, Document document, String... terms){
        int sum = 0;
        String[] arr = new String[terms.length + 2];
        arr[0] = url;

        for (int i = 0; i<terms.length; i++){
            Matcher matcher = Pattern.compile(terms[i], Pattern.CASE_INSENSITIVE).matcher(document.body().text());
            long counter = matcher.results().count();
            arr[i + 1] =  String.valueOf(counter);
            sum += counter;
        }
        arr[arr.length-1] = String.valueOf(sum);
        return arr;
    }

    private void checkTopTen(){
        TreeSet<String[]> topTen = new TreeSet<>((o1, o2) -> {
            int i1 = Integer.parseInt(o1[o1.length - 1]);
            int i2 = Integer.parseInt(o2[o2.length - 1]);
            if (i1 == i2) return o2[0].compareTo(o1[0]);
            return i2 - i1;
        });
            topTen.addAll(logs.stream().limit(10).collect(Collectors.toSet()));
        consoleOutput(topTen);
        csvOutput(topTen, topTenLogsFileName);
    }

    private void consoleOutput(Set<String[]> logs){
        logs.forEach(strings -> logger.info(String.join(" ", strings) + "\n"));
    }

    private void csvOutput(Set<String[]> logs, String fileName){
        if (logs.isEmpty()) {
            logger.info("Nothing to wright");
            return;
        }

        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file)){
            logs.forEach(strings -> {
                try {
                    fileWriter.append(String.join(" ", strings)).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void urlWander(String url, String... terms) {
        if (maxVisitedPages != 0) {
            if (logs.stream().anyMatch(strings -> strings[0].equals(url))) return;

            try {
                maxVisitedPages--;
                Document document = Jsoup.connect(url).get();
//                U can also explore the part of page. U need to know element id.
//                Element element = document.getElementById("content");
                String[] arr = termsCounter(url, document, terms);
                logs.add(arr);

                Elements linksOnPage = document.select("a[href]");
                linksOnPage.stream().limit(linkDepth).forEach(page -> urlWander(page.attr("abs:href"), terms));
            } catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new WebCrawler().craw("https://en.wikipedia.org/wiki/Softeq", 0, 15
                , "Minsk", "device", "size");
    }
}
