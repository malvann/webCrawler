# webCrawler

 Crawler travers websites following predefined link depth (8 by default) & max visitied pages limit (10000 by<br>
 default). The web crawler starts from url & follows links found to dive deeper. The main purpouse of this<br>
 crauler to detect the present of some tetms on the page & collect statistics.<br>
 to start: new WebCrawler().craw("https://en.wikipedia.org/wiki/Elon_Musk", 0, 15, "string1", "string2");<br>
 params: url - "https://en.wikipedia.org/wiki/Elon_Musk";<br>
         linkDepth - 0;<br>
         maxVisitedPages - 15;<br>
         terms - "string1", "string2";<br>
  output format: https://en.wikipedia.org/wiki/Elon_Musk 177 571 748<br>
         link - https://en.wikipedia.org/wiki/Elon_Musk<br>
         "string1" number on the page - 177<br>
         "string2" number on the page - 571<br>
         total numbers of terms - 748<br>
 All data output to *.csv file. Top 10 pages (sorted by total hits) print to console & *_topTen.csv file.<br>
 
 # runing
 
 Run as an executable Java jar file. The website argument is the starting page of the website to crawl,<br>
 terms, the optional depth & max visitied pages<br>
 example: java -jar webCrawler.jar <website> [depth] [limit] [string1] [string2]<br>
 
 # build
 
 U can buid jar using idea, or get one from out/artifacts.
