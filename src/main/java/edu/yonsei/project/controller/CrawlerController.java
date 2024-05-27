package edu.yonsei.project.controller;

import edu.yonsei.project.dto.ReviewDto;
import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.CrawledDataRepository;
import edu.yonsei.project.repository.ReviewRepository;
import edu.yonsei.project.service.CrawlerService;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

                // 여러 개의 리뷰 데이터를 가져와 모델에 추가
                List<ReviewEntity> reviews = reviewRepository.findAllByExhibitionId(id);
                if (!reviews.isEmpty()) {
                    model.addAttribute("reviews", reviews);
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
    }*/ //돌아가는 코드, review 띄우는 걸 시도해보기 위해 잠깐 주석처리


    /*@PostMapping("/exhibition/{id}")
    public String createReview(@PathVariable("id") Long id, Model model, @ModelAttribute ReviewEntity review, HttpSession session) {
        //전시회 Id, 이름을 불러오기 위한 someting...
        Optional<CrawledData> exhibitionOpt = crawledDataRepository.findById(id);

        //일단 있는 건지 먼저 체크
        if (!exhibitionOpt.isPresent()) {
            return "redirect:/exhibition/" + id + "?error=ExhibitionNotFound";
        }

        CrawledData exhibition = exhibitionOpt.get();

        //세션에서 user 관련 정보 불러오기
        String userId = (String) session.getAttribute("loginId");
        // 사용자 ID가 세션에 없으면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/home/login";
        }
        // UserService를 사용하여 닉네임 가져오기
        String userNickname = userService.getNickname(userId);

        //세션에서 불러온 걸로 user 아이디, 닉네임 저장.
        review.setUserId(userId);
        review.setUserNickname(userNickname);

        // 전시회 Id와 이름을 리뷰에 설정
        review.setExhibitionId(exhibition.getId());
        review.setExhibitionName(exhibition.getTitle());

        reviewService.saveReview(review);
        return "redirect:/exhibition/" + id;
    }*/ //되는 코드 : ExhibitionController에 옮겨둠
}
