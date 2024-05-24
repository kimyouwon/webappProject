package edu.yonsei.project.controller;

import edu.yonsei.project.model.CrawledData;
import edu.yonsei.project.repository.CrawledDataRepository;
import edu.yonsei.project.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    //크롤링 페이지
    @GetMapping("/crawl")
    public ResponseEntity<String> crawl() {
        crawlerService.crawlAndSaveData();
        return ResponseEntity.ok("크롤링 완료!");
    }

    //전시회 목록 가져오기
    @GetMapping("/exhibition")
    public String getActiveExhibitions(Model model) {
        List<CrawledData> allExhibitions = crawledDataRepository.findAll();
        Date currentDate = new Date();

        //기간이 끝나지 않은 전시회만 보여주기
        List<CrawledData> activeExhibitions = allExhibitions.stream()
                .filter(exhibition -> {
                    try {
                        String[] dates = exhibition.getDate().split("~");
                        if (dates.length != 2) {
                            return true;  // 날짜 형식이 올바르지 않으면 포함
                        }
                        Date endDate = dateFormat.parse(dates[1].trim());
                        return !currentDate.after(endDate);  // 현재 날짜가 종료일 이후가 아니면 포함
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return true;  // 날짜 파싱 오류(ex-기간 없는 상설전)가 있으면 포함
                    }
                })
                .collect(Collectors.toList());

        model.addAttribute("exhibitions", activeExhibitions);
        return "exhibition_T_page";
    }

    //전시회 상세페이지
    @GetMapping("/exhibition/{id}")
    public String getExhibitionDetail(@PathVariable Long id, Model model) {
        Optional<CrawledData> exhibition = crawledDataRepository.findById(id);
        if (exhibition.isPresent()) {
            model.addAttribute("exhibition", exhibition.get());
            return "exhibition_De_page";
        } else {
            return "error";  // 전시회가 없을 경우 보여줄 오류 페이지 만드는것도 좋을것 같습니다
        }
    }
}
