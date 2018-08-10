import spock.lang.Specification

class RegexUrlFinderTest extends Specification {

    UrlFinder urlFinder
    def setup(){
        urlFinder = new RegexUrlFinder()
    }

    def "should find http Url in String containing only url in quotations"() {
        given:
        def onlyUrlWithQuotations = '"http://karolnowak.net"'

        when:
        List<URL> urls = urlFinder.findAll(onlyUrlWithQuotations)

        then:
        urls.size == 1
        urls.first() == new URL("http://karolnowak.net")
    }

    def "should NOT find http Url in String containing only url WITHOUT quotations"(){
        given:
        def onlyUrl = 'http://karolnowak.net'

        when:
        List<URL> urls = urlFinder.findAll(onlyUrl)

        then:
        urls.size == 0
    }

    def "should find three http Urls in String containing them and not Urls"() {
        given:
        def threeUrlsAndGarbage = '<!doctype html><html itemscope="" itemtype="http://schema.org/WebPage" lang="pl"><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type"><meta content="/images/branding/googleg/1x/googleg_standard_color_128dp.png" itemprop="image"><title>Google</title><script nonce="UfQxlzPH0VVm518bD3p60g==">(function(){window.google={kEI:\'Qb9kW8j5CsKS6ATKnqjACQ\',kEXPI:\'0,18167,1335580,57,1370,284,304,1017,280,838,877,129,353,153,195,99,386,50,435,316,34,428,56,2338654,218,209,32,302989,26305,1294,12383,2349,2506,19577,13114,15248,867,6692,5471,5281,9044,2196,366,551,664,326,1776,113,2201,3191,726,5,336,44,130,1195,135,131,1028,4079,444,131,1119,2,578,728,310,2121,1362,1712,1881,730,377,1719,1294,8,1570,773,1232,883,135,281,2,2825,437,1199,525,22,604,2,2301,60,70,2,60,3,741,33,163,394,256,470,189,792,1587,296,1344,69,1050,334,130,421,689,234,386,8,321,423,100,159,81,7,29,462,620,29,708,352,201,585,7,63,78,490,1823,603,147,279,590,935,235,204,36,84,100,57,88,4,172,204,118,39,76,355,30,262,85,105,5,26,601,244,151,418,299,280,149,548,2,2320094,3685998,2554,8797786,4,1572,549,332,445,1,2,1,1,77,1,1,900,583,9,304,1,8,1,2,1,1,2130,1,1,1,1,1,47,158,15,14,4,9,6,18,15,10,25,1,1,3,1,1\',authuser:0,kscs:\'c9c918f0_Qb9kW8j5CsKS6ATKnqjACQ\',kGL:\'PL\'};google.kHL=\'pl\';})();google.time=function(){return(new Date).getTime()};(function(){google.lc=[];google.li=0;google.getEI=function(a){for(var b;a&&(!a.getAttribute||!(b=a.getAttribute("eid")));)a=a.parentNode;return b||google.kEI};google.getLEI=function(a){for(var b=null;a&&(!a.getAttribute||!(b=a.getAttribute("leid")));)a=a.parentNode;return b};google.https=function(){return"https:"==window.location.protocol};google.ml=function(){return null};google.wl=function(a,b){try{google.ml(Error(a),!1,b)}catch(d){}};google.log=function(a,b,d,c,g){if(a=google.logUrl(a,b,d,c,g)){b=new Image;var e=google.lc,f=google.li;e[f]=b;b.onerror=b.onload=b.onabort=function(){delete e[f]};google.vel&&google.vel.lu&&google.vel.lu(a);b.src=a;google.li=f+1}};google.logUrl=function(a,b,d,c,g){var e="",f=google.ls||"";d||-1!=b.search("&ei=")||(e="&ei="+google.getEI(c),-1==b.search("&lei=")&&(c=google.getLEI(c))&&(e+="&lei="+c));c="";!d&&google.cshid&&-1==b.search("&cshid=")&&"slh"!=a&&(c="&cshid="+google.cshid);a=d||"/"+(g||"gen_204")+"?atyp=i&ct="+a+"&cad="+b+e+f+"&zx="+google.time()+c;/^http:/i.test(a)&&google.https()&&(google.ml(Error("a"),!1,{src:a,glmm:1}),a="");return a};}).call(this);(function(){google.y={};google.x=function(a,b){if(a)var c=a.id;else{do c=Math.random();while(google.y[c])}google.y[c]=[a,b];return!1};google.lm=[];google.plm=function(a){google.lm.push.apply(google.lm,a)};google.lq=[];google.load=function(a,b,c){google.lq.push([[a],b,c])};google.loadAll=function(a,b){google.lq.push([a,b])};}).call(this);google.f={};</script><script nonce="UfQxlzPH0VVm518bD3p60g==">var a=window.location,b=a.href.indexOf("#");if(0<=b){var c=a.href.substring(b+1);/(^|&)q=/.test(c)&&-1==c.indexOf("#")&&a.replace("/search?"+c.replace(/(^|&)fp=[^&]*/g,"")+"&cad=h")};</script><style>#gbar,#guser{font-size:13px;padding-top:1px !important;}#gbar{height:22px}#guser{padding-bottom:7px !important;text-align:right}.gbh,.gbd{border-top:1px solid #c9d7f1;font-size:1px}.gbh{height:0;position:absolute;top:24px;width:100%}@media all{.gb1{height:22px;margin-right:.5em;vertical-align:top}#gbar{float:left}}a.gb1,a.gb4{text-decoration:underline !important}a.gb1,a.gb4{color:#00c !important}.gbi .gb4{color:#dd8e27 !important}.gbf .gb4{color:#900 !important}\n' +
                '</style>}"http://google.pl"<style>body,td,a,p,.h{font-family:arial,sans-serif}body{margin:0;overflow-y:scroll}#gog{padding:3px 8px 0}td{line-height:.8em}.gac_m td{line-height:17px}form{margin-bottom:20px}.h{color:#36c}.q{color:#00c}.ts td{padding:0}.ts{border-collapse:collapse}em{font-weight:bold;font-style:normal}.lst{height:25px;width:496px}.gsfi,.lst{font:18px arial,sans-serif}.gsfs{font:17px arial,sans-serif}.ds{display:inline-box;display:inline-block;margin:3px 0 4px;margin-left:4px}input{font-family:inherit}a.gb1,a.gb2,a.gb3,a.gb4{color:#11c !important}body{background:#fff;color:black}a{color:#11c;text-decoration:none}a:hover,a:active{text-decoration:underline}.fl a{color:#36c}a:visited{color:#551a8b}a.gb1,a.gb4{text-decoration:underline}a.gb3:hover{text-decoration:none}#ghead a.gb2:hover{color:#fff !important}.sblc{padding-top:5px}.sblc a{display:block;margin:2px 0;margin-left:13px;font-size:11px}.lsbb{background:#eee;border:solid 1px;border-color:#ccc #999 #999 #ccc;height:30px}.lsbb{display:block}.ftl,#fll a{display:inline-block;margin:0 12px}.lsb{background:url(/images/nav_logo229.png) 0 -261px repeat-x;border:none;color:#000;cursor:pointer;height:30px;margin:0;outline:0;font:15px arial,sans-serif;vertical-align:top}.lsb:active{background:#ccc}.lst:focus{outline:none}.tiah{width:458px}</style><script nonce="UfQxlzPH0VVm518bD3p60g=="></script><link href="/images/branding/product/ico/googleg_lodp.ico" rel="shortcut icon"></head><body bgcolor="#fff"><script nonce="UfQxlzPH0VVm518bD3p60g==">(function(){var src=\'/images/nav_logo229.png\';var iesg=false;document.body.onload = function(){window.n && window.n();if (document.images){new Image().src=src;}\n' +
                'if (!iesg){document.f&&document.f.q.focus();document.gbqf&&document.gbqf.q.focus();}\n' +
                '} : "http://karolnowak.net"'

        when:
        List<URL> urls = urlFinder.findAll(threeUrlsAndGarbage)

        then:
        urls.size == 3
        urls[0] == new URL("http://schema.org/WebPage")
        urls[1] == new URL("http://google.pl")
        urls[2] == new URL("http://karolnowak.net")
    }


}