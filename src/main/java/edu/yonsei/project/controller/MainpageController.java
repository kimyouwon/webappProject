package edu.yonsei.project.controller;

import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.service.CrawlerService;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainpageController {
    @Autowired
    private CrawlerService crawlerService;

    private final UserService userService;

    @Autowired
    private ReviewService reviewService;

    //메인 페이지
    @GetMapping("/home")
    public String showHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isLoggedIn", isLoggedIn);
        List<CrawledData> activeExhibitions = crawlerService.getActiveExhibitions();
        List<ReviewEntity> recentReviews = reviewService.getRecentReviews();
        model.addAttribute("exhibitions", activeExhibitions);
        model.addAttribute("reviews", recentReviews);
        return "main_page";
    }


//    //로그인된 메인 페이지
//    @GetMapping("/home_auth")
//    public String showHomeAuthPage(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
//        model.addAttribute("isLoggedIn", isLoggedIn);
//        List<CrawledData> activeExhibitions = crawlerService.getActiveExhibitions();
//        model.addAttribute("exhibitions", activeExhibitions);
//        return "main_page_auth";
//    }
}
