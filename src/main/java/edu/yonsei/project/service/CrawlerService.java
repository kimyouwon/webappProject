package edu.yonsei.project.service;

import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.repository.CrawledDataRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Service
public class CrawlerService {

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

    private static final Map<String, List<String>> genreKeywords = new HashMap<>();

    //추천 전시회 장르
    static {
        genreKeywords.put("원화/실물 전시회", List.of("회화", "한국화", "데생", "드로잉", "판화", "파스텔", "연필", "수채"));
        genreKeywords.put("미디어 아트", List.of("설치", "사진", "아카이브", "영상", "사운드", "퍼포먼스", "입체", "모형", "음악"));
        genreKeywords.put("몰입형 체험 전시", List.of("설치", "퍼포먼스", "영상", "사운드", "건축", "입체", "의상", "음악"));
        genreKeywords.put("뮤지엄", List.of("회화", "조각", "도자", "유물", "건축", "출판물", "자수", "청사진", "말갑옷", "유리공예"));
    }

    public void crawlAndSaveData() {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", "C:\\Spring\\springboot-project\\project\\driver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        driver.get("http://www.mu-um.com/?mid=03");

        // 크롤링 전 모든 전시회 페이지에 띄우기
        while (true) {
            try {
                //잠시 대기
                // View more 버튼 클릭
                WebElement targetElement = driver.findElement(By.xpath("/html/body/div[2]/div/section/form/article[2]/div[2]/div[3]/a"));
                wait.until(ExpectedConditions.elementToBeClickable(targetElement));
                targetElement.click();
            } catch (TimeoutException e) {
                break;
            }
        }

        List<WebElement> lis = driver.findElements(By.xpath("/html/body/div[2]/div/section/form/article[2]/div[2]/div[1]/ul/li"));

        for (WebElement li : lis) {
            try {

                String title = li.findElement(By.cssSelector("p.name")).getText();  //전시 제목
                String date = li.findElement(By.cssSelector("p.date")).getText();   //전시 날짜
                String link = li.findElement(By.cssSelector("div.scale>a")).getAttribute("href"); //추가 요소 크롤링 위한 링크

                driver.get(link);

                wait = new WebDriverWait(driver, Duration.ofSeconds(5));

                //전시 대표 이미지
                WebElement imgelement = driver.findElement(By.xpath("/html/body/div[2]/div/section/article[2]/ul[1]/li[1]/div/p"));
                String image = imgelement.findElement(By.tagName("img")).getAttribute("src");

                //본문
                String text = driver.findElement(By.xpath("/html/body/div[2]/div/section/article[2]/ul[2]/li[1]/div/p[1]")).getText();
                //상세페이지
                String url = driver.findElement(By.xpath("/html/body/div[2]/div/section/article[1]/div[1]/div[1]/div/a[1]")).getAttribute("href");

                String time = getInfoByType(driver, "관람시간");
                String rest = getInfoByType(driver, "휴관일");
                String genre = getInfoByType(driver, "장르");
                String fee = getInfoByType(driver, "관람료");
                String place = getInfoByType(driver, "장소");
                String contact = getInfoByType(driver, "연락처");

                //제목 기준으로 중복 데이터 검사
                if (crawledDataRepository.findByTitle(title).isPresent()) {
                    driver.navigate().back();
                    continue;
                }

                CrawledData crawledData = new CrawledData();
                crawledData.setTitle(title);
                crawledData.setDate(date);
                crawledData.setTime(time);
                crawledData.setRest(rest);
                crawledData.setGenre(genre);
                crawledData.setFee(fee);
                crawledData.setPlace(place);
                crawledData.setContact(contact);
                crawledData.setUrl(url);
                crawledData.setImage(image);
                crawledData.setText(text);

                crawledDataRepository.save(crawledData); //db에 저장 후 다시 진행

                driver.navigate().back();
            } catch (Exception e) {
                e.printStackTrace();
                driver.navigate().back();
            }
        }
        driver.quit();
    }

    //추가 요소 크롤링 메소드
    public static String getInfoByType(WebDriver driver, String infoType) {
        StringBuilder resultBuilder = new StringBuilder();
        WebElement child = driver.findElement(By.xpath("/html/body/div[2]/div/section/article[2]/ul[2]/li[1]/div/p/span[@class='caption' and text()='" + infoType + "']"));
        String parent = child.findElement(By.xpath("..")).getText();
        String[] lines = parent.split("\n");

        if (lines.length > 1) {
            for (int i = 1; i < lines.length; i++) {
                resultBuilder.append(lines[i]);
                if (i < lines.length - 1) {
                    resultBuilder.append("\n");
                }
            }
        }

        return resultBuilder.toString();
    }

    //전시회 목록 가져오는 메소드
    public List<CrawledData> getActiveExhibitions() {
        List<CrawledData> allExhibitions = crawledDataRepository.findAll();
        Date currentDate = new Date();

        return allExhibitions.stream()
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
                        return true;  // 날짜 파싱 오류가 있으면 포함
                    }
                })
                .collect(Collectors.toList());
    }

    //무료 전시회만 추출하는 메소드
    public List<CrawledData> getFreeExhibitions() {
        List<CrawledData> activeExhibitions = getActiveExhibitions();
        return activeExhibitions.stream()
                .filter(exhibition -> exhibition.getFee().contains("무료"))
                .collect(Collectors.toList());
    }

    //마감 임박 전시회만 추출하는 메소드
    public List<CrawledData> getDeadlineExhibitions() {
        List<CrawledData> activeExhibitions = getActiveExhibitions();
        Date currentDate = new Date();

        return activeExhibitions.stream()
                .filter(exhibition -> {
                    try {
                        String[] dates = exhibition.getDate().split("~");
                        if (dates.length != 2) {
                            return false;  // 날짜 형식이 올바르지 않으면 제외
                        }
                        Date endDate = dateFormat.parse(dates[1].trim());
                        long diffInMillies = endDate.getTime() - currentDate.getTime();
                        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
                        return diffInDays <= 7;  // 종료일이 7일 이내인 경우 포함
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;  // 날짜 파싱 오류가 있으면 제외
                    }
                })
                .collect(Collectors.toList());
    }

    //추천 전시회 불러오는 메소드
    public List<CrawledData> getExhibitionsByKeyword(String keyword) {
        List<CrawledData> activeExhibitions = getActiveExhibitions();
        List<String> targetGenres = genreKeywords.getOrDefault(keyword, List.of());

        return activeExhibitions.stream()
                .filter(exhibition -> targetGenres.stream().anyMatch(genre -> exhibition.getGenre().contains(genre)))
                .collect(Collectors.toList());
    }


}
