package edu.yonsei.project.controller;

import edu.yonsei.project.dto.ReviewDto;
import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.CrawledDataRepository;
import edu.yonsei.project.repository.ReviewRepository;
import edu.yonsei.project.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private ReviewRepository reviewRepository;


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
    //전시회 상세페이지
    /*@GetMapping("/exhibition/{id}")
    //@GetMapping("/exhibition")
    public String getExhibitionDetail(@PathVariable Long id, Model model) {
    //public String getExhibitionDetail(@RequestParam("id") Long id, Model model) {
        Optional<CrawledData> exhibition = crawledDataRepository.findById(id);
        if (exhibition.isPresent()) {
            model.addAttribute("exhibition", exhibition.get());
            return "exhibition_De_page";
        } else {
            return "error";  // 전시회가 없을 경우 보여줄 오류 페이지 만드는것도 좋을것 같습니다
        }
    }*/ //왜 인지 제 컴퓨터에서는 안돌아가네요... (by.유원)

    /*@GetMapping("/exhibition/{id}")
    public String getExhibitionDetail(@PathVariable("id") Long id, Model model) {
        try {
            Optional<CrawledData> exhibition = crawledDataRepository.findById(id);
            if (exhibition.isPresent()) {
                model.addAttribute("exhibition", exhibition.get());
                return "exhibition_De_page";
            } else {
                model.addAttribute("errorMessage", "Exhibition not found");
                return "error";  // 전시회가 없을 경우 보여줄 오류 페이지
            }
        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 예외 메시지 출력
            model.addAttribute("errorMessage", "An error occurred while fetching the exhibition details");
            return "error";  // 예외가 발생했을 경우 보여줄 오류 페이지
        }
    }*/ //돌아가는 코드, review 띄우는 걸 시도해보기 위해 잠깐 주석처리
    @GetMapping("/exhibition/{id}")
    public String getExhibitionDetail(@PathVariable("id") Long id, Model model) {
        try {
            Optional<CrawledData> exhibition = crawledDataRepository.findById(id);
            if (exhibition.isPresent()) {
                model.addAttribute("exhibition", exhibition.get());

                // 리뷰 데이터를 가져와 모델에 추가
                Optional<ReviewEntity> reviewEntityOpt = reviewRepository.findFirstByExhibitionId(id);
                if (reviewEntityOpt.isPresent()) {
                    ReviewEntity reviewEntity = reviewEntityOpt.get();
                    ReviewDto review = ReviewDto.builder()
                            .id(reviewEntity.getId())
                            .content(reviewEntity.getContent())
                            .rating(reviewEntity.getRating())
                            .userId(reviewEntity.getUserId())
                            .exhibitionId(reviewEntity.getExhibitionId())
                            .build();
                    model.addAttribute("review", review);
                } else {
                    model.addAttribute("noReviewsMessage", "No reviews found for this exhibition.");
                }

                return "exhibition_De_page";
            } else {
                model.addAttribute("errorMessage", "Exhibition not found");
                return "error";  // 전시회가 없을 경우 보여줄 오류 페이지
            }
        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 예외 메시지 출력
            model.addAttribute("errorMessage", "An error occurred while fetching the exhibition details");
            return "error";  // 예외가 발생했을 경우 보여줄 오류 페이지
        }
    }
}
