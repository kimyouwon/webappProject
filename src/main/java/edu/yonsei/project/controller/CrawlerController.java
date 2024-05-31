package edu.yonsei.project.controller;

import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.repository.CrawledDataRepository;
import edu.yonsei.project.repository.ReviewRepository;
import edu.yonsei.project.service.CrawlerService;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CrawlerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    //크롤링 페이지
    @GetMapping("/crawl")
    public ResponseEntity<String> crawl() {
        crawlerService.crawlAndSaveData();
        return ResponseEntity.ok("크롤링 완료!");
    }

    //전시회 목록 가져오기
    @GetMapping("/exhibition")
    public String getExhibitions(Model model) {
        List<CrawledData> activeExhibitions = crawlerService.getActiveExhibitions();
        model.addAttribute("exhibitions", activeExhibitions);
        return "exhibition_T_page";
    }

    //무료 전시회 목록
    @GetMapping("/free-exhibition")
    public String getFreeExhibitions(Model model) {
        List<CrawledData> freeExhibitions = crawlerService.getFreeExhibitions();
        model.addAttribute("exhibitions", freeExhibitions);
        return "exhibition_T_page";
    }

    //마감 임박 전시회 목록
    @GetMapping("/deadline-exhibition")
    public String getDeadlineExhibitions(Model model) {
        List<CrawledData> deadlineExhibitions = crawlerService.getDeadlineExhibitions();
        model.addAttribute("exhibitions", deadlineExhibitions);
        return "exhibition_T_page";
    }

    // 키워드로 전시회 목록 가져오기
    @GetMapping("/exh_keyword/{key_num}")
    public String getExhibitionsByKeyNumber(@PathVariable("key_num") int key_num, Model model) {
        String keyword = getKeywordByNumber(key_num);
        List<CrawledData> keywordExhibitions = crawlerService.getExhibitionsByKeyword(keyword);
        model.addAttribute("exhibitions", keywordExhibitions);
        return "exhibition_T_page";
    }

    private String getKeywordByNumber(int key_num) {
        switch (key_num) {
            case 0: return "원화/실물 전시회";
            case 1: return "미디어 아트";
            case 2: return "몰입형 체험 전시";
            case 3: return "뮤지엄";
            default: throw new IllegalArgumentException("Invalid key number: " + key_num);
        }
    }
}
