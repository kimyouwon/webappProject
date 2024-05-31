package edu.yonsei.project.controller;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/home/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "createacc_page";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/home/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "createacc_page";
        }
        // 비밀번호 검증
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$");
        if (!passwordPattern.matcher(userDto.getPassword()).matches()) {
            model.addAttribute("error", "비밀번호는 8자 이상 15자 이하의 영문, 숫자, 특수문자를 포함해야 합니다.");
            return "createacc_page";
        }
        // 닉네임 검증
        Pattern lengthPattern = Pattern.compile("^.{3,10}$");
        Pattern characterPattern = Pattern.compile("^[가-힣a-zA-Z0-9]+$"); // 숫자 허용
        String nickname = userDto.getNickname();
        if (!lengthPattern.matcher(nickname).matches()) {
            model.addAttribute("errorNickname", "닉네임은 3자 이상 10자 이하이어야 합니다.");
            return "createacc_page";
        }
        if (!characterPattern.matcher(nickname).matches()) {
            model.addAttribute("errorNickname", "닉네임은 영문/한글, 숫자로 이루어져야 합니다.");
            return "createacc_page";
        }

        // 나이 검증 (null 허용, 기본 값 설정)
        if (userDto.getAge() == null) {
            userDto.setAge(0);  // 기본 값을 0으로 설정하거나 다른 적절한 값을 설정
        }

        // DB 중복 체크
        if (userService.existsByLoginId(userDto.getLoginId())) {
            model.addAttribute("errorLoginId", "이미 사용중인 로그인 ID입니다. 다른 ID를 선택해 주세요.");
            return "createacc_page";
        }
        if (userService.existsByNickname(userDto.getNickname())) {
            model.addAttribute("errorNickname", "이미 사용중인 닉네임입니다. 다른 닉네임을 선택해 주세요.");
            return "createacc_page";
        }
        try {
            userService.postUser(userDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "데이터 오류가 발생했습니다.");
            return "createacc_page";
        }

        // 닉네임 인코딩
        String encodedNickname;
        try {
            encodedNickname = URLEncoder.encode(userDto.getNickname(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            model.addAttribute("error", "닉네임 오류가 발생했습니다.");
            return "createacc_page";
        }

        return "redirect:/home/welcome?nickname=" + encodedNickname;  // 환영 페이지로 닉네임 전달
    }
    //사용자 약관
    @GetMapping("/rule")
    public String showRulePage(){
        return "rules";
    }

    @GetMapping("/home/welcome")
    public String showWelcomePage(@RequestParam("nickname") String nickname, Model model) {
        model.addAttribute("nickname", nickname);
        return "welcome";  // 환영 페이지 반환
    }

    @GetMapping("/home/findId")
    public String showFindIdForm() {
        return "find_id";
    }

    //아아디 찾기 로직
    @PostMapping("/home/findId")
    public String findId(@RequestParam("name") String name,
                         @RequestParam("phone") String phone,
                         Model model) {
        Optional<UserEntity> userOpt = userService.findByNameAndPhone(name, phone);
        if (userOpt.isPresent()) {
            model.addAttribute("loginId", userOpt.get().getLoginId());
            return "show_id"; // 아이디를 보여주는 페이지로 이동
        } else {
            model.addAttribute("error", "일치하는 사용자가 없습니다. 이름 또는 전화번호를 다시 확인하세요.");
            return "find_id";
        }
    }

    // 비밀번호 찾기 폼 표시
    @GetMapping("/home/findPw")
    public String showFindPwForm() {
        return "find_pw";
    }

    // 비밀번호 찾기 로직
    @PostMapping("/home/findPw")
    public String findPw(@RequestParam("name") String name,
                         @RequestParam("loginId") String loginId,
                         @RequestParam("phone") String phone,
                         Model model) {
        Optional<UserEntity> userOpt = userService.findByNameAndLoginIdAndPhone(name, loginId, phone);
        if (userOpt.isPresent()) {
            model.addAttribute("loginId", loginId);
            return "reset_pw"; // 비밀번호 재설정 페이지로 이동
        } else {
            model.addAttribute("error", "일치하는 사용자가 없습니다. 이름, 아이디 또는 전화번호를 다시 확인하세요.");
            return "find_pw";
        }
    }

    // 비밀번호 재설정 로직
    @PostMapping("/home/resetPw")
    public String resetPw(@RequestParam("loginId") String loginId,
                          @RequestParam("newPassword") String newPassword,
                          @RequestParam("confirmPassword") String confirmPassword,
                          Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            model.addAttribute("loginId", loginId);
            return "reset_pw";
        }

        // 현재 사용자의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoginId = authentication.getName(); // 현재 로그인된 사용자의 로그인 ID

        // 현재 사용자의 로그인 ID와 입력된 로그인 ID가 일치하는지 확인합니다.
        if (!currentLoginId.equals(loginId)) {
            model.addAttribute("error", "비밀번호를 재설정할 수 있는 권한이 없습니다.");
            return "reset_pw";
        }

        // 현재 사용자의 비밀번호를 업데이트합니다.
        try {
            userService.resetPassword(loginId, newPassword);
            // 비밀번호 업데이트에 성공하면 현재 사용자의 인증 정보를 제거하고 로그인 페이지로 리다이렉트합니다.
            SecurityContextHolder.clearContext(); // 현재 사용자 인증 정보 제거
            return "redirect:/home/login";  // 로그인 페이지로 리다이렉트
            }
         catch (Exception e) {
            // 비밀번호 업데이트에 실패하면 에러 메시지를 반환하고 이전 페이지로 리다이렉트합니다.
            model.addAttribute("error", "비밀번호 재설정에 실패했습니다. 시스템 오류가 발생했습니다.");
            model.addAttribute("loginId", loginId);
            return "reset_pw";
        }
    }
}
