package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.repository.PostRepository;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) {
        final var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        final var posts = postRepository.findAllByUserAndDeactivatedAtIsNull(user);

        model.addAttribute("profile", user);
        model.addAttribute("posts", posts);

        return "profile";
    }

    @GetMapping("/image/{id}")
    public void getProfileImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        final var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        var bytes = new ClassPathResource("/static/images/profile.png").getContentAsByteArray();

        if (Objects.nonNull(user.getProfilePicture())) {
            bytes = user.getProfilePicture();
        }

        final var is = new ByteArrayInputStream(bytes);
        IOUtils.copy(is, response.getOutputStream());
    }
}
