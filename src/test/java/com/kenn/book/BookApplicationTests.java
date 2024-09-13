package com.kenn.book;


import com.kenn.book.controller.SearchController;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.res.ChapterInfoResult;
import com.kenn.book.domain.res.ChapterResult;
import com.kenn.book.domain.res.InfoResult;
import com.kenn.book.domain.res.SearchResult;
import com.kenn.book.rule.KennSearchUtils;
import com.kenn.book.service.InfoPurifyRuleService;
import com.kenn.book.utils.CollectionUtils;
import com.kenn.book.utils.JsUtils;
import com.kenn.book.utils.ThreadLocalUtils;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookApplicationTests {

    @Autowired
    SearchController searchController;

    @Autowired
    KennSearchUtils searchUtils;

    @Autowired
    InfoPurifyRuleService purifyRuleService;

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void searchByAllSource() {
        long startTime = new Date().getTime() / 1000;
        Result<List<SearchResult>> result = searchController.searchBookByAllSource("斗", 1);
        List<SearchResult> resultList = result.getData();
        long endTime = new Date().getTime() / 1000;
        System.out.println("共花费：" + (endTime - startTime) + "ms的时间");
        for (SearchResult searchResult : resultList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void searchByBookName() {
//        long startTime = new Date().getTime() / 1000;
//        Result<List<SearchResult>> result = searchController.searchBookBySingleSource("斗", "笔趣阁5200");
//        searchController.searchBookBySingleSource("斗", "书香小说");
//        searchController.searchBookBySingleSource("斗", "星星小说网");
//        searchController.searchBookBySingleSource("斗", "新.涩文");
//        List<SearchResult> resultList = result.getData();
//        long endTime = new Date().getTime() / 1000;
//        System.out.println("共花费：" + (endTime - startTime) + "ms的时间");
//        for (SearchResult searchResult : resultList) {
//            System.out.println(searchResult);
//        }

        Result<List<SearchResult>> result = searchController.searchBookBySingleSource(10L, "超级农民", 1);
        List<SearchResult> resultList = result.getData();
        for (SearchResult searchResult : resultList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void chapter() throws Exception {
        listBySort();
        Result<ChapterResult> result = searchController.getChapter(11L, "1784900");
        ChapterResult chapterResult = result.getData();
        System.out.println(chapterResult.getImg());
        System.out.println(chapterResult.getIntroduce());
        for (ChapterInfoResult chapterInfo : chapterResult.getChapterList()) {
            System.out.println(chapterInfo);
        }
    }

    @Test
    public void listBySort() throws Exception {
        Result<List<SearchResult>> result = searchController.explore( 11L,"都市",1);
        if (result.getCode().equals(200)) {
            for (SearchResult book : result.getData()) {
                System.out.println(book);
            }
        }
    }

    @Test
    public void info() throws Exception {
        Result<InfoResult> result = searchController.info( 8L, "http://www.biqu5200.net/0_844/638400.html");
        InfoResult infoResult = result.getData();
        System.out.println(infoResult);
    }

    @Test
    public void testSearchBookName() {
        List<SearchResult> resultList = searchUtils.searchByBookName(8L, "斗破", 1);
        for (SearchResult searchResult : resultList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void testGetChapter() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
//        ChapterResult chapter = searchUtils.getChapter(10L, "http://s.nshkedu.com/api/book/detail/0/21.json");
        long startTime = new Date().getTime();
        ChapterResult chapter = searchUtils.getChapter(8L, "/xiaoshuo/12017.html");
        System.out.println(chapter.getIntroduce());
        System.out.println(chapter.getImg());
        for (ChapterInfoResult chapterInfo : chapter.getChapterList()) {
            System.out.println(chapterInfo);
        }
        long endTime = new Date().getTime();
        System.out.println("共花费：" + (endTime - startTime) + "ms的时间");
    }

    @Test
    public void testInfo() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
//        InfoResult info = searchUtils.getInfo(7L, "https://api-bc.wtzw.com/api/v4/book/detail?&id=1784900&imei_ip=2937357107&teeny_mode=0&sign=615d74d8d9ee2822b3d34896d1612d12,17059214170001");
        InfoResult info = searchUtils.getInfo(8L, "https://www.zbcxw.cn/xiaoshuo/12017.html,34034.html");
//        InfoResult info = searchUtils.getInfo(9L, "https://www.sewenwang.top/book/934/35040.html");
        System.out.println(info);
    }

    @Test
    public void testExplore() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
        List<SearchResult> bookList = searchUtils.explore(8L, "默认", 1);
        for (SearchResult searchResult : bookList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void jsTest() {
        String code = "sign_key='d3dGiJc651gSQ8w1'\n" +
                "headers={'app-version':'51110','platform':'android','reg':'0','AUTHORIZATION':'','application-id':'com.****.reader','net-env':'1','channel':'unknown','qm-params':''}\n" +
                "params={'gender':'3','imei_ip':'2937357107','page':1,'wd':'斗破'}\n" +
                "function urlEncode(param, key, encode) {  \n" +
                "  if(param==null) return '';  \n" +
                "  var paramStr = '';  \n" +
                "  var t = typeof (param);  \n" +
                "  if (t == 'string' || t == 'number' || t == 'boolean') {  \n" +
                "\tparamStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);  \n" +
                "  } else {  \n" +
                "\tfor (var i in param) {  \n" +
                "\t\tvar k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);  \n" +
                "\t\tparamStr += urlEncode(param[i], k, encode);  \n" +
                "\t}  \n" +
                "  }  \n" +
                "  return paramStr;  \n" +
                "};\n" +
                "print(Object.keys(headers).sort().reduce((pre,n)=>pre+n+'='+headers[n],'')+sign_key);" +
                "headerSign=String(java.md5Encode(Object.keys(headers).sort().reduce((pre,n)=>pre+n+'='+headers[n],'')+sign_key))\n" +
                "paramSign=String(java.md5Encode(Object.keys(params).sort().reduce((pre,n)=>pre+n+'='+params[n],'')+sign_key))\n" +
                "headers['sign']=headerSign\n" +
                "params['sign']=paramSign\n" +
                "print(headerSign);" +
                "java.addHeader(JSON.stringify(headers));" +
                "body=urlEncode(params)\n" +
                "'/api/v5/search/words?' +body";

        String execute = JsUtils.execute(code, null);

        System.out.println(execute);
    }

    @Test
    public void jsTest1() {
        String data = "{\"data\":\"gSE2GF9lsrisKg2AcktnKxGMywHXRJwHgGE5LpOroVHM+NRZpqqB3B\\/9hWBo3NV+u4vij43E7Jab\\nkYxJtVugT6L9zsZjHhUGvMkSHm7QJU5H38SDBSzN66k\\/bko\\/rfh1HeoI5puv0XwCtjVL14YAYeLe\\nuP0BuHYOE13gberfBetV28K0ZDr9CD11bKlK2EVfSXtCflgWd3hMw7y7JH2XIzBTTt5iUSFzb+dZ\\nYJg79yGUh7k9VTiQRKK9Izx0Jh2dtqt04CBoaOQlMR79oe63tTyrkLb13oAgM3JoyFH71qPfrVEH\\ncgtppfiEjwLT2LbsGdSqAVq527ZUe50R6TihnvCeuOHatCR62GVHwtGoiIRHmZSDWX3ffWnvFt8d\\nPdqoO+iW3+uhZkKmNMnzoAcWDcj0n9d9fJDqAEFP\\/bFnkQAomG7mXhyU3wJtMn9dqISF+zOIAH4L\\n+SCoexevxnol2RUzmzf62WcUgDMl1lOHpnpxt018UNGyUjgw6NjX6FtwN0O4dseZQIm5s0lEME4q\\nWKhJyjEcVMru7Ip\\/ek5V5p17PEKVWpRngJyrC7Br4DtTD+\\/PnyHB0j7\\/QXj\\/TWC3QsXbyRb6oNU+\\nf1OuR\\/5bZhgC8SS3W20WCRoW6jM6hhpX08IpiWSAmyPxRGb\\/UdF4KDk+TftFP3PAAv4rTGAl\\/k4A\\nBnLYMY5xfjHL95HnbYJvZ6U7PAsqtGUF3Go5TVAtXm\\/zyOSHvk1QzRh2bzEGInWEfJqxhYZM0P8V\\nAie92opOdA12f3OqUUu47ZXhgIBdRtbxorj2XLFXsg0QQcm\\/qZXaJKPoVUpTdb2G2ljnp4lhvUFj\\ndKEpcTbXe5ca9c49vyEuSIePdZk\\/C1Z0zzn9MPYjL\\/Bot+v9qHwKB2cBR295kIXxfCd8VTVx+75h\\nJrLHlKVOBNNp1XMbPAVLXliyNK1Eu0br\\/YvEi3RVwN89Bpa181CEsemwTxZVu+d2ZcpIdcXKf5kf\\nZXs5wJ1SXuN4eJWhPlV2fRUcjswoxRU66RJPK8wFaxUHpyMpGSRLR3KMbdrD6B+iM5OENgclv9uq\\n3Ebz3W5SK\\/Kj2woCJXAVwbvkqzLAKcGkqP8nfcm1vC4pz1M1sqGg8Vd7ak1yglJLq0gQKSQrgIYM\\nax3ar+RyeTrqdYPytD9EhuuodvP0C\\/egigtpXGwMHOp1bjmUhUi0dVfjzDjzNSLOyOc4j\\/4T3t0F\\nGlU1lm\\/URedH0KRuZBAR8MMPwcz+jBEV8EeTPBz3XqULWxbiQhsoifYOKncCYY8do0GHaVsDAH+u\\ny0o+Bgg9GjbTwnaaXurr7oWdZhe32LDfmYIZmYIQ9lbhiXXpYK9ywa7L9raw83Ols71Nst38s+Fb\\npUyQEi4d0GRVtiPhxaGrdPiAM8lxaRoCfhbTkB3wAxNTRVn0rmPqEERFzBfTMv39uUYGU94vZbiN\\n4eWbGSNzFSO6hn+Y0Pts2kSYkikHx0g8vFqcSuxtLjZ8ete11NTkTGrzNLuCF4blLewVqJ4Lx0TH\\nnv7EnZfNIgEEJVXAQ4a1E3M8oDaW2rZaJ9LXJeNz6lHdR2A4rkSzwVVMWL+4tjEN4mamTMZJudOK\\ns2t506Pwwog\\/EocJzNPtRb1BkaPxfcyuqBqzakVZ7ruf0a7uEPqJMWSUddgszbgK4SLuBpqG0uws\\nVvtNAZ1z3bTT6s751KFKsQrrXGQxnEbJ3esBz5pZvg+jfupqgvKIRIpDZm8GNcoE\\/xWySlVBVHx+\\naCk\\/SJZPvKnHuSdAe6jhRsx9\\/hfUme9x05qRSFG+qLtvVFE0CqyJQ1Khups2t3JgcblUrGLD6+Id\\n4Epn4vrIMXc85HQ\\/zncxnkx\\/wA0RFVAHmV\\/2bVltf2Pbevmq1wcjfwMhypxplI6YtXMWUDkVAQXV\\nrgdLll8t5WZ6AFTHlWlgKTGfylBSerGqNTn7HzlGvH023ZsMFNCqHugt5qdKAeRgHDYFtl+uB\\/r8\\n2g8198O0HikLX8gn0l3T7CiyvCXPfPJeUzdDx1WHwHKJT8\\/\\/caSdPwIMQshwLwPh7j6OFOAHfeEE\\nWuRJX+56qrBBkX3DXPLtitCmo9o0EKldtIS3Z0VXviKZurbKIxcJm4hsdeLRC\\/Bd0tFEUtKlpP6x\\n1VqVf9OyDLwgGCgDui3PIF+QjBWpJcPTvx1+WFE\\/LP3SOpM4hd1H+jDxq3qW+YOtN5lGbgPCEMoP\\njdKx6vaM0TPx5dGEEWOhIMmY9Ye6KVIq2Ep50ZSO37dDrPc2Y3sSAhxeY+PdUmPUqtkcjXtjBisJ\\no71MKHru1IxcsdagXKVfijk8gAFhNqlUJIZIi+k+GDcGSoJjvWjqOl4EA6ESS7moSWC9I\\/ZpcYNs\\n8n0cAK1uE4gxqFoPD7gcWbsqtk3Z9lCylrjeGbn+6mRnvNaZdaab5SrSPUcVftRUJu15SCj59RfC\\nJpKu1s5aj6GO5WfPuh1bk+j79VdIke47rBYOkZGW8pm\\/plK6XjsuVMLZXcc1TtGU4BLf5apz6Hs3\\nyvzjlm8BBWiyZseF5Rk7FXGJMiuYqSwwOLZE8xkNNBfgv\\/0ozGpO+nnqSw+j8iFo9HNKRKVOhONd\\nDXJWjmkm7Et6QXR2FHO9yQXSwXvlX4I8aZQiOVMX5woN9jlJFdFwoYFUY2qFq7jmwuAjAxJPpo2\\/\\n\\/0\\/1+aOpBaEe6Pu81ov+rkact9enlN1ST9zFGTkFq8JIhyEeU00OeNwDvG+gSHPPKDFUek9QWWPw\\n5hYfD+oZaXMxWREyPobL3BfOhVBH6eua+HS3QHlhI0xB5bpMk80JqfEfARG7gvaUl4BZdTYK+ePd\\n6\\/hC8g3wcD2UHaaH2zeMkR9Q8DqoqUiXlPxOZwt4dhhFEgxvspM6Wxos25eFYoA3AkOKQXg3eH1d\\ngXbEZLXe8+vS1NdbIyWg6kzjlD7NuawIz10EN6q5ElUJfN3R6x1woy7qeg+kwWDSBlBELZT+WZ1f\\nXR+5OJEPy\\/zJQ9kfMc62JqJOthieL1vNxzWZH8IvxDG+cf6igoEOMLKiLEImPXTDUNei4TiuUN76\\nvqGhjr9LvCtOGsTZH05vciiXp8BuBqeysHkLlMqbLle2B6aWcNCNImnqE\\/J95WFQD6ScybWQlxjk\\nHvG78vUSs0frqQznz\\/4CdQxJCNWOvduYYf6Ux3X\\/QdCKGx9m2HrUmKaldL0XRvHVktH1XUsPtDK7\\nDtC+j+DoAnaKSCBS2QTgWuYLWjr+rCm87Rpfd\\/KHy\\/FRDLFJbXUJmV5Z1txtRlWfbfu5izvJUIS9\\nlZJ2chrmLG+1lSA4fOcLt5SxB\\/jYUNyGez5IZSxZC4fs40h38l56SKSFQRB7R1lY4zWg0eGDL5um\\n8MIGa5jWCQr4nXdPfqBCD00vdXBznaJ57I7X0H\\/lCJQyxhQAiHleUNz\\/xqLx1DVs2H7LMuMjeh5L\\n9OQh63plgcRz2\\/ji3L5U5Gu9D2fR0LSc3D1kvvfmYEThMVYXZwpJb0Zv9lc\\/pzdar4Sm8rB94YBj\\nBjWt6beobtTR72tsZPN7vPcc8i7NTOQsWw1SJS8SvRCpRoEx2DTml+GIbhvuc\\/OA\\/4B+gdutH3jo\\na3LP4KX23INqy3aW7XtRz4xXvY8LQfwIzdEGyyRqutzQkmI\\/0RX7t2zXYWP94WEp2ypwvio8r5VJ\\nF9TrgbxObqnBGlUWRsCtrqP+oJfkFS4TRJPgbsXE7hpVuhNCB3Ran8hv3TnzhbAz54sVdcocXc7q\\nRUQy5pwSsfLrhUB+KNMMM\\/k3ioBONH8C4Sf43IZyYSfEgWlTV2CiHOD5nSK++u0B6qDxxAZUZnwf\\nZPSXyOHmFMavdSTIISYXJJNK2Ut4gJsAVVO0PTzFP29hNW8C\\/J\\/0CkkF1llzyPB5EUV4do1U19zv\\nbVO4NzFujlCSoblySInAUA6zJfOcA6odZZAoe1w6oqycoU\\/05cEimB631aGoeH4qsFB6s3L6deOH\\nlrDcBETUu1R7Nf6b+6f4x+JY6EFrDezqvqoLrGN22xrbBpvM+UJnJIMiFGrGWimdoiEcwOLiSRTt\\nwYNMOMe4fU7iLWLdRFAEX5x6C4Q8rbRKJ47zccl+nSmCOzymK7Qs95W8nvXFEF+Q3fhYcwchngvx\\nHZ8cVa\\/Mz\\/lOkBMkWpPJ6ehZd\\/\\/RyLvYCtkgTGOT+iWBKaX1PH6Hle0KsznbH\\/Ae\\/TW35\\/MyPFwn\\nqh+DDPwyzYsw564Pjs9aDLimpa62B24+FiTsJgcwgURAPZMyXpADdSs+oM3UR2E2xNbMX30Q7t1Z\\nGROlCSHnlXTskvfRtKihH2bN86uAG1KgpLJwIN8Ul08PMcr9CdYBR\\/9zoNhDs8319TYUYilY7xoE\\n2a1K7\\/2fns6IV9Pe50WfjaORTTgvkO4tIFGkL2FcKWAm16RNL5Fv\\/y3m7aEXXNjWGwOo7POyfDaT\\nERmeR7CFVQiwdSZAEgs986ZWFhMdWq67n9c86R81Tt0tAV8uYonXRByvpHyWJA03YAPnjHsqJrjN\\nZ88A2nlQgwhv7tYrEJJDdZWH56syphMEUB2X9Jtn82rXaFCbW1H6tR9mrqpOqHzvM4RCEGd+1qKW\\nOQiF7O5XOSh9lRbyxsaRZNjhWhfmr\\/lnj1Uyl54yS\\/XsPTwqxenyVpQGRgpzJYbqcSh\\/5jOijBr9\\nrSGeWFqVA3SxnZxbuxddWQp+yQeWskov9adQt6kdW6nFHvZ7EllHv+kivkncDjlN7gCzkhBZvMf3\\nkQEjYkb8zARvUDFuZpqt\\/4z96KlCqqOu8O1\\/17dJITBpWEHDxP410NUolH0DnjrkKu8aw1Csw+75\\nqSythMXj349feQ\\/Z73YVpvUWXPGNEFuXCtaq7eUONj0bZ7UAuElFQWDBrqSFyJV9TRDBifdf1cE+\\niYHMhBOd3CADOP56Up8Fxx\\/22zHDp7bi8jqf+u8Ixepl9OG6A6U4bhX2f45PkyE1m10hIGmLYgtF\\n1W6SR1NS6fI+p25gSDJkmFWed9WLaj0GWcgR78nrqmbpr7CGAr3EkOvUoxY3l+S2TDXO01nQ6drg\\n7nDcc+qH\\/3DJAhvaTnZwMaDszxrtC32oskgIhoyzr5+NLAn4PMBd8E8nuss5FuPzI+yjKd4gw6eh\\n88cJQhwk2SxtfH0R\\/+egiFbYCs4qTJj00DTXNoIeTQSfI9HiauX3jMA2gqX+ZBFf4jVMoB6gNzJH\\nnXrBRye3CXAgnfB8apsapzkpyoie0GyR2+Je0Falr1tFtepzyh+E2Quu8VztH67SKZkq\\/OaHsg8g\\njnmLHy8\\/5QkYGNVQgK0PXcb7+0WMfFetzHJ1Pzxbg22dvA1PzK6GM0KOK10pwDvtK\\/Ru+vWmXsvB\\nA3CCv47piH89E4\\/k\\/iaO2l+giQylgwhqKw8fqVM7H6gRjIKeUiPMEunHKWYyavqyVxWSmDeadTdq\\nsT7YYMm0Z8hIcNzLOgD+Jcph3Xe4X3cs8zaKepsIiPTh6INQo3URmX1agb77QecZFfxTEcGh01Ka\\nPY13Ae3LvHtcfQ2n5NBTUPPa5c3uxfp2GZe+hIPR6YHnHhYv4Y40zHItnCNAQvaNWDzoLZHxYtBN\\nRPWwE2dCKbM6\\/0Y2jFeugWwQBkQ0wha4IRMo7StPq0Slzna5cHrh16NRQZaYRnSzdSRv76ryvyHX\\nimGeQKNRmMv9OmrMncPu3u7I7hYFiovjho1RXmozKOSq3e4op0cGZo\\/sYihx+5PMtc2ca+M3ch91\\nWbVhnTFYVvUbEjn120cZXOPcod3ZKMVL1a2MwKtFvOYVE\\/BwwVQO4w0W1CVHC30INgFtfM3RTKHJ\\nPUabhDQGoPp1ZNmF4Q\\/RGm\\/a5S3o+FZZJwnlep1PexoIIzJ8+h277qXzGeQxkVRfxA3Hk1JShp9c\\n6eNq4XFeZ\\/cl0bDuhoF6fqvdkB7DgsD9hz80FAWxVUqafnM2JSCpn4jtCSUqPJ7Kf9uf7djdANd9\\ne5VSnVjknhfHKcGBUuVIZ7lqlgOItWLH0ZRKZeuzGMhuAV8arfSFVEiclXgb\\/k5obVFzZLCcvP6Z\\noOQ32Pjq9xBgxhueEniKJ2cTZxbVjQ64q6E8R\\/Eq2BrBCyP+966hx41SN60ZAV08\\/GoxHxCU29HC\\naOOuoW5PDAQO3Yow849RcP2UpbSmBBbsWGsSkCsEn4BaMVWyX4NMRFJBWCSelR21Fkt6NUTe9gtc\\nl83jvQ+XHeTy9kI3IYc2FnQQ1GogSWu\\/q1\\/ZZO3zz6l9tVeDQ1WOdhTl7ya\\/1p5vtEQ+zwfxYnal\\nb4IpcsI9v14JWcDyFvt6ziUdcCpH\\/eHmq+Z0KCupBoGfPQvG50wvm6K2VWfb45miCJXMczND4\\/dS\\nLRwo17fhObeca6cvsjlCD7AB5CHrSKC0Pd7DiVyfLWU9ASBGI18TxwuPLlEZCMWqCCAqSGElN1Yf\\nRLzR9e9hswQPBNcKu0qGDhkiWMR0S49+kTj512tRcL42rLfSlC8WCtl2AmWxHRDHFWnmzRIh\\/ugi\\n1amr72ps\\/3xi\\/A5ytC6cE4i3drdxdXJ2j9NPdwUFyfaUrSqhizFLfliBjHhRVmS0CFLm5AGiwSnL\\n\\/24oRsXzPdQnxeViBS\\/MsTeWQ7Tiwz9TYm43bc5ALtSblOaafTdqK61HjOdHVZX+U4vGCYjnu1CT\\njpb3Q837XkAnkW\\/C+PPo53Mi\\/FmZOJVLbJ\\/YzTtsf2dLi7ENA8vLV0Pk9C8wbQ6u+aBKhZA52cQl\\nqmODsO\\/7cWGn4U0lDE+XgHupH8sexxYL3ucf8PYIVpMkFzneKc7J9boa+bUieXMkzUEyXykxUH2j\\nZVqQs3yWAkEsjPEao83\\/zfRdW+g\\/ok6LtUYr\\/MDe2rLJPa+bISgEr20AJgM+dVKL2lJDxj46OGui\\nv\\/m19Bf\\/MScuGKSZT3o+QxheT7rIe59+B5ykxU9uKauscpgTrduexaDyeCScJjoAq4d3JPsLLu5s\\ngNqX13jPc3KEpNc1tT\\/RH7aKf+f4nQyKCDFRM5wuWGSmJ+NpnX4NFPDF+MvCgvzEMVDB2lR2Gli3\\niJJflVXyzIjLXqHzzhXVQwbf+GkC4AiSF2GzWJaQyUikL9d1ddCGwrAs8alIGhZfefrpywTMhCtK\\n3D+SQsyc0obBAjLwgF2hx5PiiA7nI8ICf9MrU4xxUbwwtJRwd9MBX+fR3IZ7+ETpJQOKTHcVmKbi\\nsvA8QlIMQlS+Vo2QoLbgaqEFTbcFn9tUQbg\\/EuLfYkG8q9uhtvzHXEZyY31nztYIfhIEx0l7JCUc\\nHW9ScDoM6YTXLkq2DsvwGoUchT7VEcuLh9bJg6\\/jrjuqc1JIn4Faz7048fYBFuKuo+vQJg2IZfBy\\n\\/EUahP0j6fkvXFmBD0xZMBZkD8f9Yl0vOTlC5mMCyFGwrEYlLpmeVVJdnmRFdiJE9NsXfvoc1U1y\\n7GKBx++ZWAbTJXp8pvxXypJqz7rzL6F85CX4nlbaEohJlexpmlmJJSPCq1s1xPm1nXRklXFPrJlg\\nqKOBmGX6uM52HALjONR8FG+YzFlu7T8XDNZIEAxzkbFAaKKFwQEZUH3VAhcmGQSncxl2t26AtYGA\\neb4wa\\/2QJFimmVvOO1ojh5kWFj6hYqPkQY5xsEttb0092hsuwlBVGxd2litNC+rXqfwofUtaUSpL\\nMUFhPTBorvfGC+RG1u1LxxEdg4qsXmy9kBm\\/AvTFIszSMoqFNvtJ0eqHqVCpxY8lbX2PkhxABsBs\\nr3Lqn7hXBeD6Ese4ROtfM37DX\\/HckBVGXzDuvkFeb+9IG9RDKFxC00LX\\/ln+43NIHgOs1PH9jKzx\\nOlUOmOt0RE04cNW4PMyT28xmB9EoYfMSrfM1xlcHGKSIGkWRhLB7o2DoZmpF7\\/8Rq59sJMkR8d1S\\nZvwn3YVTC1+r8y7V5HrNp1glBYo+Y9pP\\/TRLAaJII6w1PB0hpbMkaayWnACjFyigTKVqUvNQtw5f\\nnOSjQzFTUHpRWc6Y+vdVbVJzdHqMOIAozKtUbPym2Jil6zvZZk\\/lcXFyAXT+o\\/aFsxTdz3j\\/81uV\\n6DQFrQI9EpfhvOMKO1rILvLbLrQ0SLfGTV+cdfY5RfV2J5T1PcsuAal4x3p3d5NPwrFGJWTzVAh7\\nfjfrJHvva1CNl\\/RzeX+Ldpqmpgf4VOi+4fg0hlqTwfc7IJNBbuwR7ZalCWJv52P0DDTdXzDrP46H\\nIEBfPhSXQajGfrwvmFQDPJGkHuJBh9Ziux84BrGd8RM03ymU2\\/1FT31L61H6\\/jb65UjJFaZflyiq\\noHsLIBSEhYNqiht2kuOVRHGGG42fzouJc9tpqmK448BK\\/lwjLe5vMRrHdmKAek52lZst2hyNdIsg\\n0yOXUpGRSvC7+wxmzPG9L2NN4uA7LxRKLKUUDvnDp2W4WteWymWVO87QS1nrda3kQl3yJ0DhQEDu\\nz1VcFGNFDA343B+SQoSCsZASvz9PPAgyWReqHGx0PP8EXWKlkATlcyaSJdOllWdcs57gaufDQmcb\\ncmg2JVCyhI77GUxnuJBfJtjoPIq2DY9CXbdT4B+IKTWwGq4OF86IK1gvWwGMws2\\/Ev4sOZt\\/\\/fb1\\nC43o+uVXxFXKZt927JRo2ttWO4vTr6nDO2M+ziB9J4zNLWTXZq6ULg61lMCDnQ9kqBXDSbYUT\\/0n\\nxdM7QbflqGqFMlKg47p\\/DnvwnBrFVzU86DW43zVE1Z6nzLtNs9AJ2devTLvDxgrXqgxnszJWq4\\/Q\\nfHWa5cybKZC387IX7oAJx4G11MKe8lzoAv+dch5ctUq91d\\/YocIbJFKzxeYCnBVY6eVcsLada3bZ\\nsEO9S\\/YPgVzobFog1F1TFqfdqWOsoQzozvo\\/TQBUJ19M6z2GMS2HDAhfj94HBByWKb\\/kpjPUO+lR\\naF\\/4h+i7JCj\\/GsNnC4aYl+y\\/qFUbYkZu5L5PjX+KdCzBIu0z6V5LeQRrjydEqvqrNRuzmesP5egy\\nIID7TB\\/CYOlNzc8ukXhUor830jHuOiSfCezPNhwNI+\\/tiqB6qLiKlAJyCsvRTNmjYspblcNFZYCz\\nEBs6haNFvRiFqiiyzpCe3VWz50XkAks2oZeMIkMXFWOQL1H9fRR7oyH0x1ddtLPu6hG\\/22oIO+Q1\\nd8xOrKydnXg68rsDBhEr\\/2vxbaePdehWp7R0ppze7D+wic7ryhxHNLhqTmeKGAkvAA24jACGwIib\\n\\/MUSzBLVCyAevp64wAUAebS6nGKFHiYpxxBYbtA6xBCSq01nENvd2kiRdwhYr8dMttprlM7b3z6y\\nv3W7r75fuoQ9NAki4kdrTCZwikF2wT22E772n+GHaAjJJspp+DhCQAdyx+LJz4B0exH+NvvJKtGC\\nSD5a5gBXGZqYhFkHKFOcBN0X8HMGv7l8z33OSJUL11Lq2ufNb1hr56sUzH2eH8pG2oEFT07498Hi\\n5X0hyN3xPxAGIYPGHoo4y4FHuV3W3cLHY6UwxXiLs0zuM6H0bLW0HUloIL1CvgWEdPRunVLcEJy5\\n8EywBoaZNSfoxC84di4CxWj5zVMxHRYy6LZnWYEBAkKlSmcm\\/7VutgNasw7p\\/ZNCdK2rtHpq2BWU\\nljc4fPnUacyG\\/byzQa7XbF0bgfw\\/YhFjEezk5Rq35wHXGHopOUM7bhQiY7UK6uwcxF+kTMEoU6H\\/\\n\\/gJE5pjKQdymtmFY+GTBElFDqMowNKhOLf3rroSDoNb8ulBg2n88EDc3Mr6voxuG31Mi2xVonYOF\\nNu\\/f0v8XRVynFWVjJOUZffRStjKStlTdj65tY12Jgmne7v+0x5xuza5CXaSYI+VNZj1pX3MjIRl\\/\\nFf0TvOelItKGcspnZ+HYXKkQ\\/luN1iNmcdYmALubmwqgKYBHYgTpWuTfzXOWDGQdbwwcnHXJ+Uzg\\n2BZVzzz8PgF7NP4uDQiqnlWoVkmuXD7qlcnmLp8\\/vZX32iOfq7JclUoNHtSoB8hYIYBj7EMrkNYq\\nAyC9Z7SMmWaSoAczX4538ihGAv52X3gRQuwRgA9XVOek6gNsxROb5lDVPr3IMsQB\\/UlcBJYLIOR6\\nO2BQFIX3X7QEDdfJpxAI5IuCoRw2orf14BZHp0pLO6R59X4K2KuNHpiwg7ZWwYm1sv3or+UPV60r\\nLDI\\/lyVzs4qZO0QzrMlYG8Tr39I4Snq6sEDfcETD5HAXnz+HdpeoNEInTxQgyux1YwHuixF2T8NV\\n00yb3SoPuXtfy09SPxvC7FErg5N3hZbmwRiSWHLlame\\/7yvwi6+FZ+I4\\/s1K\\/VBsEVw2k+8qUwsK\\nsks\\/VhrBOcD4VL0vVa3u1OG7fXm5R0Q8Abs\\/y6bk6lNHr\\/LWJWR0SEaj5qXGrGxsxj1sLzDRglp4\\nVBjWELVL+QNhUk3jLAwBLVdyd62areCymvnl\\/Z87Qx4wsqBEgEqQbI7IuUV8dbF4r6uarepI5RgX\\nSnQO2Tmo9w3cU21MYgkMQfITUAH++JmHsKJ155H6V32aVHGU0HBV\\/Ssd6JhAHDA5DlnT25US1jym\\n8s\\/gJsu20uNTPTj0Z+Pk9tvwGuC4qnXmj6gHy5yY\\/BaSYtBMJcKbtKAyAYynBfcnMyUoJtV07xNL\\n0khXoNOuoZu440zM7gobKn5F5h2NUiKRHAl2lO0CMGjtHBW565MWaIXmAY+MCuctA6Hc6pbYXOYX\\nWA3Nb3orKQXBV21vfGr5vkeZVG0Z3Yy5IOWwEdjXZVjq3\\/q974tmgCoYPXKJQluaPgKopNg6NgLW\\nqKSdJIs0DRzratZEV55Yu01H3PP1vofJ4nxWJSNEfmiwLvB3P+aMpHkCPsHVcRcIGLrszj7DOwKh\\naK4mxYNUCCQmDgGn+x5\\/kmQFw9MaQbfL4pZzRlRalZ2d5m\\/PhN6AEljnhsYfgjttRePykKlrzQ5s\\nuUh+SpHqA2TplY18jA\\/S1UiTBq75Mp31VR6WwTRFyzf5QHCQl+H8yr6ZDHNlL2Vx3JqDIZxgzXO3\\n68wZVesRjqs24hEaPPD8i9ACkw6S28YFbFTmdxKtaPtqSGI5x5xZ+t6m3Rr1PKi8zQEoNDaHzp0m\\n6HkNpeXbwqS2\\/8R3YwcdXjBqfwO4PPcEcclq27sH\\/8E9FMnOmaqiuf5zC\\/cYxpIvVXlTgXjDIMYz\\nMbsd67shxhxEf5MB65IzVgxpSqkSY\\/sz7mA3LMam6CEaQxu5PVmZ6qOjoGXosbR1GeLjvvAyETjI\\nz\\/t2DDQxbQXLXvYbtu6+fvuKKXcu\\/TO5kgj6kNMJemFd5waPYoEm0rSlbrRnTQJNwxga4jhv5iPU\\nsQpOR0\\/q7IvwM6d5JxsUnaT2aWzJMBynQUoendwe8UWSVQDbnQTi6iEdTRuTPDhTocLXiUCWx1aj\\nxM+KXopUw3r8w5Ui3cxcmEampRffYtFKXz1O0A3FmRAusRU7BWW49YM0yPdxW2lmw+2yB6EbnyMW\\nqrpEBufBRPZauOQRPs5hk1mpl6SIgCogecVxW2gU6YivC94LCc3SH8yVfcmwUNyrOSlEf6BLfgPY\\nBuDBvOsRC7HWIbEFeFdPLhhwMf3laveBx7LSOdEdQ3eBisHLG3Na+Ws7ORP4Hpzys7wSCivOLUnE\\nnsVMWWWo6rBFg9xWy3a9ppYlHfIuKrUtPFrTpTKrInSLD85LQJFHc5xe\\/Swe8tMCsBdhSHlBZWwv\\ns1Gdkd3ZuYeWRI5fKPyeBCBiaMpEDCQ2ybznrhuJ\\/oZxLmLwjw6JKvRteMnQdO++D\\/u9fV9NsKjR\\nU34weo6CDXWy43YiY4RBrZXI\\/uqXFUDv7bwLlFvC6C69IOEB9jtxR7ArN6DaZQOZbYY8XlaCuoqc\\nHIMX4ox5fwe4FveHORk6R4i7HeBBNyKQXcCTCOa1jdCnFREfcn8feXSf7Yx+n+MoiGn7XO43iVYP\\neYW+y7g7SRG1Hmpqlj5Z8AxizEGMp0y5+HkWxfzZXr7LaCBf0GPA0b9pwuL3KvPPgtE5u1T9CHRU\\nzVQqGBrDtIh8Pc7zQBRIUeMyBVkanezpKdnoAAj58lVAgC6YjbAxat4yN4WjqQqS97gD9cwaPcGG\\nk+0YLzLoDHdtLYKAnhakd1z6Db+\\/A4XqX3d29h+\\/r+uucdDd\\/I0aJAl9xAlBrnadtM6Gcv8\\/hJmQ\\nNtETDoUsU1SZM+6wWhUe+1BoBk0opFqBUaUolWq6g8FW31NXxqeux1ddpFoWuG5DJ4Iju7lNAD69\\nFh3XWg2CGkWzhVVZoAN1s2U4qs3gs8PDi4bHOhuHplaqcG5ruqYtFWhbsYssMkpoNhrG\\/7cHGugW\\nyCVCWJlYD5pxvgfC7A6G71FHuCDaKuMTkw3HQdbQgN8v2jiO1eO4D0gS9qXuXHiknZh9i7QZjXLQ\\naJuBLF+N0qnJivzPrOCst5sW5KRFtGiF14VV+pUDp6CX\\/JD4m2X13ovur1u8+g51p8TLpP0SHzR6\\n7OXm3t71edDZ8U+tNogvxrk396ZSHtQJqnGC2sOYH4JHG3x1u1VfndBXmiekAtdHiH9QI9X4U8FF\\n5NKparVyafWUm3cLy9KCCZFM7\\/n\\/D7y\\/k6rSxN9FjGyOtO\\/MHpwAthxF\\/N8HFGRMku\\/GJ0DibdVC\\nHDwPk+Z+ifCcsuQj1XzID87s1BU+ZbschP+KJXfZ9cQVscBtPsIdEb4ROI3er9Uhbpnj0ZHADulO\\noiE3yIvIdzDJ0hFAfpPVcGYOPv6sS0sp0jU0N8yPSWcDCidnovrSSRM26O0BZkT+RrFUeAKYjsWr\\nizRm2Oyx7RkUV9iQvA\\/TA\\/P\\/UYdEE2FnxIzrbp\\/U\\/aZu3\\/5vQgaKglMD0sYywPrMOfxklstdEpZu\\nNJmg\\/DsWR4bNAG0yWW4HGT+\\/NwJU43XiSmUDB1w3TcCZWMw6PBCjEeurPQ5qOzWHcFlsAC8bM0Rw\\neT5iWVbUfcO9bL3zPhJ3FUiKmC0FHNYWCcOe6v18dEBtLSUAW32zTc2KiHBa7dKiJc3i42SVN7ws\\najDoRCdawZvTtF5zetjmbi2k4iSsZgKP6qL\\/tGkPy54hu+8aKZ4PS76evlAxEpubuZkTOGh4JHMA\\nQe\\/2b0Jsu6TdAYgzeKU7A1sfLFYpjKj6jfuHhU58+0kabecwax2L+dlhAKu4LdNL3hchqDKV80BU\\no3A966Xenj4UMaBkmzjvvTXRst5i\\/LPD4h19mv4X2bdKWp+7iX7dBPSjrTSeUGATCLWTROES6Ry2\\nyNUVL3S3ce0iSTieZLDUYPLrbaWkap9CCUudDsughh2p4j16D3rNWIEhnJwTiOAAZo\\/OT0ChbZkS\\njHGef4JfMunNRZ8bWKi0QUAtqk7z2CWG8hQ1dLKO5Y\\/nERqS+bSvSgYr8M1AnYs0DIf1\\/Tfdzbcm\\n1nVYLS\\/XzOz+ZR+sY7hY0jgNRcQ3YQOdGCv0b4jQcoKpkDLTaWRAlQKQx8+Cdpsw4dsagRvlnAJw\\nyZuevct4vaLhq4\\/D4+Tuokil4zKsZk5rlxCbwrvgI68xVT+fvumNQvH83x+BD3uWddlVja+OuLWV\\nE3Z7UXYwf33r52uAhILzm\\/vb1JpVHyQaNBpLGWVgyWsH+0Yn3reB74Xe7cqw\\/YhfUzj2NMO0mx1i\\nQGS6ZDj9S3XMr6TxDwUdH23jrTanEHKApoHGyCTLrJTPMSbFW8gk6sIz1cUjernLPssx+\\/wgPyCp\\nL1CQMCm1sISgHZ8wqdIg5D6AcErMrynWq1nMMFxT274YFWDtBpY6D6ue36jxtsvOWotZpMtgOcM8\\nCj8A4by5xGtOo5umG09K2Gt9puyHSpbH4FwKFSG2c8i5gLuT3OYkqbT1igI26TLPs7yp1tI13LDW\\nS8BQqlupxHjpJHeWXVT4PCo7YGX+zUIZYRY+ySpZHCyU67eHVs\\/FQAyw+mrBsx1tIouGCDKxpQZI\\nUQNGIF7x7zEbXRYugPhEwevqgULRi8seeRp7b7BFAkFYr7qWvCy+pCnsp54SalIXmpe8Y\\/8fT6z4\\n9VGPsiJDhGhJpSQvU2FnUh5T5LDjd8fTnRf66CosBl2IlViV5YciiKoGZXIuX1kbWE7pPwq66VhV\\nuitrXK52a4Jp1W7UZFJOpjRVeUt5atwON7K3fh6T0k554zw13ab3K\\/JVtDhgSbqbYuP40wPjQYte\\nsnbJf\\/L4Y8H95mcs5GFFZJVyzvh8vYVkTrFRhKgaIkH47XFb9D0I0ER6Yf9t5IL5XtJafVbUyyWM\\nol1PbshEGa5hoJTY69BxjU4P4QnAdzSbH7DEN9YlVfxbmFkiiT19QUDUi+QMGC7jOc51Tl08mX5R\\nWxHd8glQuqdimOaQHyfRX8ifTRnThPzbLXeuGi7SxlbsHlr4PLahKekLgckHHHBifXMB4qxLd7Ba\\nx9LRn+QdgRcpqFI3gLVqEFHJvfgUjaCPCm3W7a1mp3baYH4xaXRPGlH7UTLJ1lwtZw9X2Wj+TdDT\\nJehw6MBjMCMMUULCu1SCrqHgVXDEQEhuQU69VR+kBDE7dS5OPZdedBqiou8hdr6wuzVFFHU7JMaF\\nmFla3bdfvzfrX9ju2PWyhKipbuEsi1kAvNQlsUZN5j0EgZnQNcUOTnZBWOchFI5ZiTpDyXcKZqqL\\nhY1k3VG7o29T5kLJx2kBcHjU5xjLpJigAAxFprEWdtp8l5xMGUWlXGMudqdpdI7afHm7xbw3UGWc\\nrIe2mQO7xGgx6twAYb6+h+fZfHKfY1qOh7tAaHyrtvPWzzVYMotVs0z2CsE8b0ChmY7HpPym1vPk\\nTG1AJM7X7pSyMEU4TONrs2KkoNcDT3ssOh\\/EPS73cZbddt1g2rjABPHLA+iUUMMktcMPeLIJcPij\\nf0Kzah4\\/gVlctKL3dw9y8a5uJ7+i56DVm7I67tClHQiwRxHGRRG9t2tA768MHHqiyLGe2cwG4wm\\/\\nDAkjm8sxpQssMCS9\\/U0LvxjKvvQp\\/GN7N3kkRLwZd6ZJyzTNowW6YiD0Xji\\/eiWnrd6SRfk72sBi\\n4WEy065mHLiM72nUYEwhg305Xi1zTkpFpeZZZ1R8L9SG+4mn77fgrysv1pFhDErFdiZAfC9MZIdb\\nrTXLfRlSrEqb8jy6eVRGNHmT7nJE3tZALSD77MKauhTehmCBzQmohXUPmT24q5L9W2iaKNDT10XE\\nT2CYINUCbREuP3i4qVkQz44ipm4BwfqJoM0oq1DC3w8IQ3XYl4cL+zjTyzbHUe+HBEgPRK7E+VK0\\nVuidiN2VkNXnUmDA0NL00VenmZKIdmHLL+3tbt\\/iLpNWVM1+LnfMfJajrP403s\\/EHoBCW\\/RNQlMe\\nztd5Jp4nlMltTtltWFigD0xvAy0O9dtwpaflEXB+RHiqAawy1S1GKwEg8K8QR+v5sG2vShirSvD+\\nB7W2vkucFeZ8cyCEjc4aTCz0FjCbQG9guOBTzImrB1aSi+ZgpPFU1lhf3XZnTmw5cV0kXH3XZj2S\\njl\\/xI1MI2Br0mqpPQT1VIVaIDyIEztzWJsS\\/NojePJ2gz8oZF0aHpGbTqpBh3JUpkfDCxUOeWchv\\nd42lpGLjMNalU4hlwSaL1ztvyWwi7Sb4mDs6EllMEuoyYHXT2Kb\\/OSk8llFmnb0DCmTG2+H2dds7\\nqnIn8F\\/ag3rLqiQVj5qy7R6f+cveK2ZXMsa32+tor7WYMvDCDTnaI3Fk0hrbiSht3qEpYS\\/4PMb0\\n3LUADcHij9k+R4RlCOCvqzq75vjvo4WsDHQQZUWo7KYf1SJO33TCmhXXKjynTN+1kF+jYDGv+XwU\\ntMDE9v85SDHWVkbwRJdiKhX2suithA78yZmlF5blfoT45+xYP5VghVjMFjD27l\\/XLiJeoL1nnVR6\\niSdYO0bmRT+Cx9TGf\\/Bl0ppIwlUZjr61zdmXgawr4MxIDzQNmE7kVpdhkqDqq7MZTbGx+fHY7pWQ\\n4FUSaE1\\/rXXrAsf7RH2eBB1N8Ch14njhbtBh8anTCuAc1MuiNh03KY2Q64mwCpF1nG25J2y+pGHp\\n0tTYA1UzKfeZw6invSZR14oydylwEil8DDo++oaLhioJz3WyK4yKfi64vLv9WIkykCMSO5IaB50V\\nqw7GauEFiU8zNTopzDhiCWf2XzWcS4ZqoOK22RIptUHJqOh+We4ARFZLruo5ZIhb+RpaUt9S9jdo\\nXn2sWwqNiSn73vS+LEPRTCKv\\/Vj+vZ+zbrjzP32b4NTzEnoybVO33jBleX0\\/07\\/ibe734liESNCy\\n8Zv4QKZjUWyLwiyJt4wfu3gHSTgJe2Q+Xx5Soaz0TOpjPStoLfL7NxNs7KhYctIdaDS\\/ACgvggpb\\nDoCLxXCLxBKEVAqLMICxoffLE+l3LiajBly4np3nM37HcoVr0wTI6VpXIHkT7AuL2ITJwvBfgsJj\\nuipSu7aQv1LZZmSIChRnNTrMqyuSOYCFdMEvhePGqx275z40jyUIlBhFVv5HNsAnj5\\/ghmG\\/e8yR\\n8UMqLHC+Rr+GWXStToR9eBTv3DhNiut+AIMaXKWD4zZQHNS+pWFTReCQqj6UE1mTN9\\/TAHW2rBgQ\\npNuOqzRAFEK+tDCpM5cnwejYqrYGG37yHZiJyua\\/MadkX0bq+gZ+ZYHuhyCEzlVa+Q34n5+nt4rp\\nyS6ANFcqbc61uXFyZPwL4IrM5Eg0ylGQ9GXZP6n20K5wwOOkybuYUmn5PfZwWQnHa\\/Ar+Cx5aYUj\\nZm0tsivkhjqPNAdp\\/wvbTJMht3Qb0bzRtwVuByRXvITXy39obliGype3yAmLFq7EU\\/Q4Um9AshJr\\n4APkGGcyYY9g6WbyLSYISSfyRCQ7iVxyNf350psQqtsqNID3Y3F+dsGveL2RPXd2XYlaLbLiMjWg\\n19Xhajh9uFHBh5ph9Yy4PgCESRZMGGoCpjZLC64vpLabrC6lTZe2t2E28wwILCxyPitxPEbeJQBr\\nSKFxXRDv2v8mN9hVk1AnTDeXXVJlIIOUEBXbHM657agBveTqTn1Ygc5esRFlaBRfOT5ZJTJW5Wyn\\nQ14AICchhI6Xm4yBQNmufXDe3lgVPiJ0fFW+F39CxyZldq2N+HXDpt+KqsFoVBGEMN+ETOsMZZyQ\\nhyRAZ6gvuRc6nlDOJO4Z2b2E1BTD5lbyI1jb1h8rguFDu88WhOUuWsr7QJ\\/aXpb4sxLCyQO8VPMm\\npRiv1aR6qHZVpAUqoOeWbLIk2azlporY4J+FLxBxsidtW7fvvPhfHxsy3dIyhQwMvnoqDmKdjUyK\\nTcvor543JyCxVQlrcIuIwllzJnXNqLKbJyYz5aItwlexcNIEV\\/Ev1uxRwF2\\/b+FhyJ2O7UE6JwYI\\n8FErH2wEL7NMLnt31nMyKoIccRLM3qLQ2N85s115DXn0ebJbK8pBoVI+lGFy9Bpb0WS9UFbIw3z6\\nGvJz8QUI6snmgS2Ze8vIN59fQHjnG4yOd0cMjI8TUEoQNVYFy9uaxCohBO5fO19DOplLBkayhGBl\\nHQlxz7KtGAeWG+pY\\/dL4kSBHh5qY4jnWPxEg6OXyVDRZYdpG2EiJcbm+VCEwq3RB0iPeJoIaTWq0\\n6YsKvNpWioihgZFfWb7zJKTFCHCkpW+aWGNpM\\/AzzwSnECwGMGXvGke1SJQokw21sFip5rzhZZH8\\n9rxrQDPcRnWJNvbnRyBjcS5f102CWwUO3FvLLaR1otm1NVSMHKqD+etMWTzyXcc\\/uZi\\/63ZDOdBS\\nLRvS6HncKyB7NVw6LxWnNJAHJzqndj\\/iIWO+t9skNRCu0fzlEoQ10QmdKmi5bWEVeI9OiyDjLpLJ\\n77KGxJnTaz90nIOtygtdxW+YkUFyjvx4Nb627\\/cgy5HDO87\\/cWU+rW9K9\\/QFXLxNenFS0Usb+BsE\\n5RtUoOaZ+wSOzeSEAXSBE+ke99NORrprVvxzH5eCEDXVYY4UNxXFsDV6VncVhyA8r8zmUrQ9JKoT\\n4q6m+SB701BB4OYbX\\/HfJhRMz1j1BLK0Ga77+Pi3TVlPO3K1d6ZcfWpKZrLF5sOAA04kjuesNnls\\nlmAwmtYADA2xKwGSm8QD9PoDpoH91JFjIowJC3hISo484HznJ0SfygVJPeXlgN5LZzKIX\\/YbHWo0\\nfR1KsP2O09j80mFyIMntFSy9EOwBYYYZIa4+RfQryZuiMfsC25ZKp5TVD15yzV3kFe8eiFyUVEqS\\nq4vBJeyAALITeSlPYB3jNir54UbyJgF+FrOuvU1TEcV5PzvPraOA98KpVZ\\/NvFzrJnu9sAtTIFlb\\ndAKHw13fNerwmMxwkVa8IOMUW78qNXwBrBc\\/T5SHU6Evz0JmxWI3jbCxKqj3hBgPmel6o8vv8rUi\\nX000oSiPbcoia4xEdtVAes9UckI0pt+YC8SI47cgycLTADKSltQCkFHZ9hRTmlbW6RRAYM2B+nEp\\nU00ll1hBi9iYUeEc1f0Ih4Sy7OTUjfO9PvMz43HOlzr4CH\\/DeE4+oD46bPXDPCbqzEhaPd6oJg4n\\nQ+4qEKJL9\\/x7so6tEao1+A0rFf3xa4gf8g8RiV9sGVrwvHM1cuIbkhC7\\/AH0dNNatEp3xEpdLh3Q\\nu0Av4kZuTANxGrVaLUy1PvUYrvUDOwY2R3SuCW1EZtyMrBb3yHc6V2xZhUCJtesOwqMLkh3uExfy\\nh\\/3CYOW3woTB+9vdYWK33rVlmNGT0eiPON7WiUBlPRSfsKTKGj0gsIPmg1wHHpHC0IRJpRfcr+g8\\n0oS5FiQ9Eu0Slk7Kwc0Jp933\\/5ZTRUqhgsiDbmbfh+PVuLfHS6zyS6\\/1Z9MlZt91MCyhuxBn3te6\\nFG4q5kbNW0l+VDipav6WtASGqYkDAYewYZdqgYmpnqQj1NlvrrU+Sw3uQLcjUxZK+p8ptejKj5qT\\nyrR\\/pOaShDvjSCYrkwytpSiMAii8IHoJ77qzSNjKNKVWyPTzujr\\/WIpPlrGmdsEbH0P2txi1d\\/Nn\\n+etLBmlrBT6m6cDtqskB4V9heh8pnvF59y62QYwdDfqpPUkReyeIkIStPtBbuQNlympusJoDR09p\\nhv8IQj2X0SG96ckARcx5sTogh1yym4gW\\/BetiU8CJDdm9TNVTvbDbJ\\/Q+NmAjKYiq6A3F2x2Nugn\\ns2ogQibTN1tzlfyLjjh6ndguW763TVGncgXr8Xk\\/\\/hGQLbzq+dIUOAgZHiApUsvjM7s3BO7ojUbI\\nRHC02vWLxPrm44rJDv5tPd3MWMqVWgqjH0REKTbxkMJTPHyhqWj0r2khhvMIJI9dKXf1RCyzUJaj\\n3Wiymkvys3eBszk90S4tkXD3kwdLvI8wvVRx+n\\/TUaOWouRwGvYThs\\/9dusTmlKiubfAvP8Jd5bN\\nCD3KBY7u0lIOQ+WPvABzLVHA9O6yk6DeK2HrClvlHpHnU6NcltNdlhohgYSuyFZxoyF\\/dbD2SWIC\\n9RW8rXQOEk9JnorMyfO9TpbzXkVWs+gOMdIDDSSenlgS+Dd52mOMF2UVugq8DoGktHQm2IEnKxK+\\np9IN9joU0HePlnmSY\\/X5UJeU9tQgWi9Sc5vuodXc63BmkNOpYpvDNWdBL2ywYFHdvCajDL64IEzb\\nqUNB5XbfN3imLf6Gk\\/FQdlyreAXCyxUNrmmsNvTLi9AnBDaAfTX9pqJ1RqNmd6h7RIp2CBRRxOmq\\nXpWjkv8wwm+pLREDr58ipgrMm3\\/dsRwqd\\/f2Hnfr9JBZhc47rYUAw2RsyA+A4F6v\\/MzBv2AaBavG\\npRMSQ9ApnOMrBQPDz37QeYdD6n3WaP6BRj90yS3\\/XYCFH3u6vC3Hy\\/xuaMiKig9lSzNPgocZRjUQ\\n2ZK9bFaRLYBmD4JMLSpn\\/mAiqj3xfQxuuoZ2OTftqq7s8xsOTxZkeh\\/DJJ9CKDwhgJYwL0Hjquz9\\nyn2wXnCzSyiZHF+JcA1moLyUDb4GSrwwjKgwH28ROfhtvru58QwPfdMynTIS7aF+NEPKvMOvRopJ\\nOY7B\\/tA5\\/1cU+5Gp8AEIz0JlPlAwemN+UKUaRk3er+7\\/iLzZk67cbkdfA567hdIbVmEmpWf8bAqp\\no533z3Q4VGZNYGbPBXQ+07qCLkznBKRvfGV1p34I3I+hR5f4o1yUURB5CslLQ8y5WzwoAAbhy9hX\\nOcYXmyfRAArg3uBouRztdQnzg2QHKpVjCPEN2wBDLGFolhSQCx8mnCZgTnwoDWOT4tvXvib\\/TMvJ\\n8xXF1dgRoNQdX8Y2bB\\/Cx3ugu+OxiiHjvbuDg2nedUbExTxEWR938gKyv\\/hx2g4VcMmW2rsLAdiu\\nlVPeKmjokc5Me1yllz2PZgoAsPg6ncSvZxCRULDwXbbW1u9Dr6OQo85oK9kIquA8UCU7gOb\\/NFHp\\nkN8DeICbalvAj\\/3RzyIEVXGT1tKGgjVOS7f\\/0B6\\/Bsq58bpU9EmZ+zY5q\\/M4hvO3h314ekf10x90\\nMVQB3cRKB4Bt3bsvFNWgsvW8Rxz3yHbeYNHAmwR5FZAui+OfDpZxdaUMBehqqLU4TfDChLWztK1T\\nVEqz2sPXCNtJfmz1lNehuyowRCrsIqxM6r6yszj0Mlql52ZmD6KiJP3Jbl9gKNjZMiqbGRlcIxkg\\n3DQJ9qI77AaGK2W9x1fOWPIDfceXnhLib0aHgk+y2z+sLBxh9HIcMgNK3MKZD9ngx+VW2z+TMvpQ\\nC1lb1YKJ0SLwjAYJK7ErhHwODIwzTHxDoeiPhu8DR9x8WWtL3ApwWSa55fesHLs\\/EuUJSLyoyF16\\ndEQzwYuEujt9bS4mO8o3+8ceR0fncFrsAKp4jB2Cvms97F0nBABh5PLitSRmzlMAPiASjw6WW2Mx\\nzV764dg90syRkCLzTmBNqdrS6xGyErPMVEs=\\n\"}";
        String code = "var SecretKeySpec = Java.type('javax.crypto.spec.SecretKeySpec');\n" +
                "var IvParameterSpec = Java.type('javax.crypto.spec.IvParameterSpec');\n" +
                "var Base64 = Java.type('java.util.Base64');\n" +
                "var Cipher = Java.type('javax.crypto.Cipher');\n" +
                "function decrypt(str) {\n" +
                "    var key = new SecretKeySpec(java.str2Bytes('ZKYm5vSUhvcG9IbXNZTG1pb2'), 'DESede');\n" +
                "    var iv = new IvParameterSpec(java.str2Bytes('01234567'));\n" +
                "    var bytes = Base64.getDecoder().decode(str);\n" +
                "    var cipher = Cipher.getInstance('DESede/CBC/PKCS5Padding');\n" +
                "    cipher.init(2, key, iv);\n" +
                "    return java.bytes2Str(cipher.doFinal(bytes));\n" +
                "}\n" +
                "decrypt(JSON.parse(result).data.replace(/(\\r\\n)|(\\n)|(\\r)/g,''));";
        String execute = JsUtils.execute(code, data);
        System.out.println(execute);
    }

    @Test
    public void jsTest2() {
        String result = "{\"data\":{\"content\":\"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\",\"content_md5\":\"ab1bb8aefe963950b3e8a2eee461e49b\",\"id\":\"17059214170001\",\"type\":\"chapter_contents\"}}";
        String code = "var SecretKeySpec = Java.type('javax.crypto.spec.SecretKeySpec');" +
                "var IvParameterSpec = Java.type('javax.crypto.spec.IvParameterSpec');" +
                "var Cipher = Java.type('javax.crypto.Cipher');" +
                "var Arrays = Java.type('java.util.Arrays');" +
                "var Base64 = Java.type('java.util.Base64');" +
                "function decode(content) {\n" +
                "\tvar ivEncData =  Base64.getDecoder().decode(content);" +
                "\tvar key = new SecretKeySpec(java.str2Bytes('242ccb8230d709e1'), 'AES');\n" +
                "\tvar iv = new IvParameterSpec(Arrays.copyOfRange(ivEncData, 0, 16));\n" +
                "\tvar chipher = Cipher.getInstance('AES/CBC/PKCS5Padding');\n" +
                "\tchipher.init(2, key, iv);\n" +
                "\treturn java.bytes2Str(chipher.doFinal(Arrays.copyOfRange(ivEncData, 16, ivEncData.length)));\n" +
                "}\n" +
                "decode(JSON.parse(result).data.content)";
        String execute = JsUtils.execute(code, result);
        System.out.println(execute);
    }

    @Test
    public void jsTest3() {
        String code = "'https://api5-normal-lf.fqnovel.com/reading/bookapi/multi-detail/v/?aid=1967&iid=1&version_code=999&book_id=' + result";
        String execute = JsUtils.execute(code, "111");
        System.out.println(execute);
    }

    @Test
    public void jsTest4() {
        String code = "gender=currentUrl.match(/gender=(\\d+)/)?currentUrl.match(/gender=(\\d+)/)[1]:''\n" +
                "category_id=currentUrl.match(/category_id=(\\d+)/)?currentUrl.match(/category_id=(\\d+)/)[1]:''\n" +
                "need_filters=currentUrl.match(/need_filters=(\\d+)/)?currentUrl.match(/need_filters=(\\d+)/)[1]:''\n" +
                "page=currentUrl.match(/page=(\\d+)/)?currentUrl.match(/page=(\\d+)/)[1]:''\n" +
                "need_category=currentUrl.match(/need_category=(\\d+)/)?currentUrl.match(/need_category=(\\d+)/)[1]:''\n" +
                "tag_id=currentUrl.match(/tag_id=(\\d+)/)?currentUrl.match(/tag_id=(\\d+)/)[1]:''\n" +
                "sign_key='d3dGiJc651gSQ8w1'\n" +
                "function urlEncode(param, key, encode) {\n" +
                "\tif(param==null) return '';\n" +
                "\tvar paramStr = '';\n" +
                "\tvar t = typeof (param);\n" +
                "\tif (t == 'string' || t == 'number' || t == 'boolean') {\n" +
                "\t\tparamStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);\n" +
                "\t} else {\n" +
                "\t\tfor (var i in param) {\n" +
                "\t\t\tvar k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);\n" +
                "\t\t\tparamStr += urlEncode(param[i], k, encode);\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\treturn paramStr;\n" +
                "};\n" +
                "function category() {\n" +
                "\tparams={'gender':gender,'category_id':category_id,'need_filters':need_filters,'page':page,'need_category':need_category}\n" +
                "\tparams['sign']=String(java.md5Encode(Object.keys(params).sort().reduce((pre,n)=>pre+n+'='+params[n],'')+sign_key))\n" +
                "\turl='https://api-bc.wtzw.com/api/v4/category/get-list?'+urlEncode(params)\n" +
                "\treturn url\n" +
                "};\n" +
                "function tag() {\n" +
                "\tparams={'gender':gender,'need_filters':need_filters,'page':page,'tag_id':tag_id}\n" +
                "\tparams['sign']=String(java.md5Encode(Object.keys(params).sort().reduce((pre,n)=>pre+n+'='+params[n],'')+sign_key))\n" +
                "\turl='https://api-bc.wtzw.com/api/v4/tag/index?'+urlEncode(params)\n" +
                "\treturn url\n" +
                "};\n" +
                "let url = '';\n" +
                "if(currentUrl.match(/category/)){\n" +
                "\turl = category();\n" +
                "}else {\n" +
                "\turl = tag();\n" +
                "}\n" +
                "url;";
        ThreadLocalUtils.setCurrentUrlCache("https://www.baidu.com/category/gender=2&category_id=203&need_filters=1&page=1&need_category=1");
        String execute = JsUtils.execute(code, null);
        System.out.println(execute);
    }

    @Test
    public void signTest() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("name", new String[]{"书香小说"});
        headers.put(Constants.TIMESTAMP_KEY, new String[]{"1719287651136"});

        // 添加更多的headers
        String signature = CollectionUtils.generateSignature(headers);
        System.out.println(signature);
    }

    @Test
    public void purifyContentTest() {

//        List<InfoPurifyRule> purifyList = purifyRuleService.listAll();
//        String content = "需要测试<br><br/>的\\n字符串https://www.baidu.com/test.json你带好呀房间都是ddfa<img src='fdfdfdfd'/><center>fdasfds</center>";
        String content = "&nbsp;&nbsp;&nbsp;&nbsp;林菁菁说：我草拟妈的「管家？」林菁菁被连续的惊愕搞得有点头脑混乱。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「是的，我到时候会找个地方住，一个人管不过来，我不会亏待你，同时会负责你的花销，有工作上面的事情可能也需要你处理。」赵轩开出了自己的条件。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;对于林菁菁一个小镇出身的普通大学毕业生来说，这个条件非常诱人。她知道管家什么的只是个说辞，面前这个小自己五岁的少年是想圈养自己。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;即使是这样，这个条件也很难拒绝。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁是赣市人，所幸父母开明，给附近经济开发区的工厂打工供她读大学。毕业后林菁菁留在连海，一直想在赣市买一栋房子给父母居住。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;工作前半年，靠着优秀的外形和工作能力，林菁菁卖出了不少房产。普通住宅的买主到不会有什么非分之想，半年内林菁菁就赚了六万多块钱。但是随着她拒绝领导的暗示之后，情况急转直下。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「好的，我答应。」天人交战许久，林菁菁还是点了头。一方面她最终决定赌赵轩是真正的潜力股。另一方面，江城距离赣市近得多，她也可以经常回家。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;决定今晚就收了林菁菁的赵轩突然又收到一条提示：&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;【初尝禁果：对于宿主而言，面前这个人可以打开一扇崭新的大门，占有她。】&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;接到这个任务，再看着面前脸色酡红的林菁菁，赵轩瞬间把持不住，化身饿狼。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「唔……」林菁菁被扑过来的赵轩吓了一跳，还没做反应便被赵轩保住，堵住了小嘴。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;虽然也曾看过小电影，但是在实际操作中，赵轩没有任何经验，只是凭借本能吸吮着林菁菁的小舌，同时左手从连衣裙的胸口探进衣服里。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁的巨乳本就被衣服勒得很紧，一只大手再伸进去，空间便显得愈发不够。赵轩只感觉自己的左手被两个滑嫩的肉球挤在了中间，再难寸进。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;不过这样的刺激足以让一个初哥血脉贲张，赵轩大力揉捏着林菁菁的乳球，右手伸到林菁菁的衣服后面想要把衣服解开，以获得更大的操作空间。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「赵先生，不要在车里……」林菁菁并没有做出过多反抗，甚至随着赵轩的粗暴动作，感觉下身有些湿润，但是无论如何她不能接受自己的第一次是在车里失去。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「我还是第一次，求您，给我留个纪念。」林菁菁感到赵轩并没有停止动作，不由继续哀求到。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;听到这里，赵轩终于稍稍冷静下来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁是第一次，他又何尝不是呢，在闹市区的车里宣淫，实在是有点过于疯狂了。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「走，去君悦酒店。」赵轩把安全带给林菁菁和自己系上，再次发动了车子。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;至于星海一品的包厢？&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;定金不要就是了。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;赵轩来之前也订好了君悦大酒店的行政套房，晚饭么，到了酒店随便吃一点就好。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;毕竟今晚的正餐是副驾驶座上的巨乳美女。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;停车去前台拿房卡上楼开门，赵轩飞一样完成了上面所有步骤。一进门，他便横抱起林菁菁，然后把她扔到了房间中的那张近两米宽的大床上。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;行政套房不是君悦酒店最高档的房间，但是也有超过130平方米的面积，比一般的住宅楼还要宽敞一些，条件自然是没的说。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「啊——」林菁菁感觉自己被一阵大力扔了出去，然后躺在了柔软的床上，面前的男人迅速扑了上来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「我去洗个澡，我们还有一整夜时间呢，让我来慢慢伺候您。」林菁菁完全没想到面前的少年突然变得如此富有侵略性，只得一边挺起酥胸任由赵轩把玩，一边柔声央求道。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;赵轩想了想，还是松开了林菁菁，然后用力拍了林菁菁挺翘的屁股一巴掌，又使劲揉了揉，说道：「可以，但是你要在外面把衣服脱掉。」&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁的脸瞬间涨红。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「不然我现在就吃了你。」赵轩发出威胁，并作势要强撕林菁菁的连衣裙。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「别撕，我……我脱给您看。」林菁菁屈服了。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「先到那边站好。」赵轩没有马上让对方开始脱衣服，他现在已经恢复了一些冷静，正如林菁菁所说，一夜的时间很长，他可以慢慢享受。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁翻身下床，按照赵轩的要求在一边站定。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;赵轩也坐了起来，把林菁菁拉到床边上，让她维持站姿，然后从上到下开始抚摸林菁菁的身体。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;被一个小自己五岁的男生如此玩弄身体，林菁菁心头浮现了一丝屈辱感，但她非但没有对此感到厌恶，反而有些沉迷于这种屈辱。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「脱吧。」&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁听到这两个字，几乎是下意识地服从了赵轩，缓缓解开了连衣裙后面的绑绳。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;夏天的衣服很容易脱掉，用了没有一分钟，林菁菁便脱掉了这件衣服，里面就是白色的胸罩和内裤。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;又过了一分钟，林菁菁便坦诚地站在赵轩身前，双手不知道该放在哪里，下意识想要遮住要害部位，却又怕赵轩生气，只得一会背到身后，一会在身前交叠。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「好了，去洗吧。」赵轩又玩弄了一会林菁菁的身体，就让林菁菁去浴室了。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;拿起电话让宾馆送一份晚餐，赵轩自己走入了另一间浴室。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;倒不是他不想和林菁菁共浴，而是赵轩知道自己决然把持不住，无论他自己还是林菁菁，显然都不想再浴缸里交代掉自己的第一次。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;而且经过试探，赵轩终于确定了林菁菁属性中的奴性，是真的存在。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;刚才赵轩屡次发出羞人的指令，又让林菁菁站着被当做物品一样把玩。这一套操作下来，林菁菁对自己的好感居然上涨到了90！&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;男性洗澡可以非常迅速，服务员把晚餐送来的时候，赵轩已经洗完澡穿好睡衣了。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;随便吃了几块蛋糕和一片鸡胸肉填了填肚子，又灌了两口随餐送来的高级矿泉水。林菁菁便打开浴室门走了出来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;此时的林菁菁穿着一身白色睡袍，微微湿润的头发散开披在身后，睡袍只是简单地系了一个扣子，她的巨乳，柳腰甚至蜜穴都随着步伐若隐若现。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;赵轩拍了拍床铺，示意林菁菁坐到身边来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「要吃点东西么？」赵轩笑问道，说着把林菁菁的身子搂了过来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;他感受到怀里的美人在颤抖。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「好。」林菁菁说着准备伸手去够旁边托盘上的食物。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「不许用手，我喂你。」赵轩把林菁菁的手臂拽回来，控制在身后。然后拿起一块蛋糕，咬住一半，低头凑了过去……&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁也很上道低抬头去接，赵轩用舌头把蛋糕顶进对方的小嘴，又在里面搅动了几下。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;就这样喂了几块蛋糕，又喂了水，林菁菁已经双目迷离，下体浓密的黑森林上已经沾满了晶莹的液体。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「你个荡妇，这就湿了？」赵轩把餐盘放到一旁的桌子上，一把扯开林菁菁的睡衣，伸手开始玩弄她的蜜穴。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「是您……」林菁菁刚出言想反驳，赵轩就一巴掌打在了她胸口32E的奶子上，顿时掀起一阵乳浪。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「还敢顶嘴？」刚才的一下让赵轩获得了触觉和视觉的双重享受，意犹未尽的他左右开弓，又抽了林菁菁几个奶光。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「别打，别打，我是荡妇，是小淫娃……」林菁菁连忙求饶认错。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「长这么大的奶子，是不是想勾引男人？你真没和其他男人上过床？」赵轩并没有放过林菁菁的意思，而是继续一边用手指玩弄着她的下体，一边揉搓着胸前的大白兔。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「我是处女，菁菁的大奶只为您而生，只勾引您一个人。」林菁菁已经被玩弄的意乱情迷，两腿不停相互蹭希望能以此弥补身体的空虚。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「那要怎么证明呢？」赵轩此时也脱掉了浴衣，近20厘米的肉棒挺立在了林菁菁的脸前。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「唔……」林菁菁知道赵轩想要听什么，但是最后一点点矜持还是让她不愿意说出口。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;赵轩此时点开林菁菁的属性，发现她的好感已经到了100。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「快自己想个办法跟我证明，不然我不会要你的。」赵轩继续吊着林菁菁，此时他自己的肉棒也发涨地难受，干脆拉过对方的一双细长双腿，开始腿交起来。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「请……请您插入我的小穴验证我的身子！」林菁菁终于无法忍受赵轩的玩弄，喊出了这句话。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「好，我现在就给你开苞！」赵轩听罢，抱过林菁菁的身体，怒龙顶在林菁菁的小穴口，但并没有马上插进去。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「睁开眼睛！看着我给你破处！」赵轩又抽了林菁菁一奶光，他已经有点爱上了这美妙的手感。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;林菁菁不敢违抗，他现在对这名小自己五岁的男生又爱又怕，不敢有丝毫犹豫，便睁开了眼睛。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;然后，赵轩用力一挺，巨大的火热物体，贯穿了林菁菁的身体。&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;「啊！」&nbsp;&nbsp;<br><br> <br><br> &nbsp;&nbsp;&nbsp;&nbsp;一声惨叫，赵轩感受到了对方下体的阻力和膜被冲破的感觉，点点嫣红随着赵轩的插入流了出来，落在床单上，看来对方所言非虚。";
//        for (InfoPurifyRule purifyRule : purifyList) {
//            content = content.replaceAll(purifyRule.getPattern(), StringUtils.isNotEmpty(purifyRule.getReplacement()) ? purifyRule.getReplacement() : "");
//        }
        String regexPattern = "(?:\\b\\w[^\\n]{1,29}?(?:(?<=如下)[：]|(?<![：；][^\\n：；]{0,9})[；](?![^\\n：；]{0,9}[：；])|(?<=[。！？—…])[”](?=(?<=(?:[：]|[说道问答忖][，])“[^\\n“”＂]{2,49}”)|“\\b[^\\n“”＂]{19}|[“]\\b(?:[\\w，、%](?!”)|[。！？—…]|”“){2,69}\\B”\\n)|[】](?=【\\w+：)|(?<=[。！？—…][”]\\b[\\w，、「」]{2,49})[。](?=[\\w“「『《])|(?<=[\\w，、「」]{9})[。！？]{1,2}(?![（【])(?=\\w+[，][“‘]|[“]?[\\w、「」]+[！？—…]|(?<=[\\w、「」]{16}[。！？]{1,2})|[“]?[\\w、「」]{16})|(?<=[\\w，、：「」]{9})[。：；！？—…]{1,2}(?=\\b[\\w，、「」]{1,49}\\b：|【\\w+】：|[第其]?[一二三四五六七八九十][来者则是]?\\b(?<=\\b\\w{2})[，]|[［（〔][一二三四五六七八九十]{1,3}[］）〕]))(?=[\\w“（【「『《][^\\n]{9})|\\b\\w[\\w，、「」《》]{29,69}[：](?=[“＂]?[^\\n：“”＂]{29})|\\b\\w[^\\n]{29,49}?[”」）】]?[。；！？—…]+[”）】]?(?=[\\w“（【「『《][^\\n]{29})(?!(?<=\\b[“][^\\n“”]{1,19}[”]|\\b[（][^\\n（）]{1,19}[）]|\\b[【][^\\n【】]{1,19}[】])\\b|(?<=[—…”])\\w{1,9}[。！？—…]|(?<=[！？])\\w{1,9}[！？]))(?<!“[^\\n”]{1,36}|‘[^\\n’]{1,36}|「[^\\n」]{1,36}|『[^\\n』]{1,36}|[（\\(][^\\n\\)）]{1,36}|【[^\\n】]{1,36}|[\\[［〔][^\\n\\]］〕]{1,36}|[\\{｛][^\\n\\}｝]{1,36}|《[^\\n》]{1,36})(?![^\\n“]{0,36}”|[^\\n‘]{0,36}’|[^\\n「]{0,36}」|[^\\n『]{0,36}』|[^\\n\\(（]{0,36}[）\\)]|[^\\n【]{0,36}】|[^\\n\\[［〔]{0,36}[\\]］〕]|[^\\n\\{｛]{0,36}[\\}｝]|[^\\n《]{0,36}》)";
//        content = content.replaceAll(regexPattern, "");
//        System.out.println(content);

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            System.out.println("找到: " + matcher.group());
        }
    }

}
