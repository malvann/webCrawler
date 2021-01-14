# webCrawler

 Crawler travers websites following predefined link depth (8 by default) & max visitied pages limit (10000 by
 default). The web crawler starts from url & follows links found to dive deeper. The main purpouse of this
 crauler to detect the present of some tetms on the page & collect statistics.
 to start: new WebCrawler().craw("https://en.wikipedia.org/wiki/Elon_Musk", 0, 15, "string1", "string2");
 params: url - "https://en.wikipedia.org/wiki/Elon_Musk";
         linkDepth - 0;
         maxVisitedPages - 15;
         terms - "string1", "string2";
  output format: https://en.wikipedia.org/wiki/Elon_Musk 177 571 748
         link - https://en.wikipedia.org/wiki/Elon_Musk
         "string1" number on the page - 177
         "string2" number on the page - 571
         total numbers of terms - 748
 All data output to *.csv file. Top 10 pages (sorted by total hits) print to console & *_topTen.csv file.
 
 # runing
 
 Run as an executable Java jar file. The website argument is the starting page of the website to crawl,
 terms, the optional depth & max visitied pages
 example: java -jar webCrawler.jar <website> [depth] [limit] [string1] [string2]
 
 # build
 
 U can buid jar using idea, or get one from out/artifacts.
