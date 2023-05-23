package hr.algebra.toyswap.controller;

import hr.algebra.toyswap.dto.MessageDto;
import hr.algebra.toyswap.model.Message;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.repository.MessageRepository;
import hr.algebra.toyswap.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @GetMapping
    public String getMessageRedirect(Principal principal) {
        final var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        final var firstMessageUser = getFirstMessageUser(user);
        if (firstMessageUser.isEmpty()) {
            return "messages";
        }
        return "redirect:/messages/" + firstMessageUser.get().getId();
    }

    @GetMapping("/{id}")
    public String getMessage(@PathVariable("id") Long distinctUserId, Model model, Principal principal) {
        final var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        final var distinctUser = userRepository.findById(distinctUserId).orElseThrow(() -> new RuntimeException("User not found"));
        setupMessagesModel(user, distinctUser, model);
        model.addAttribute("newMessage", MessageDto.builder().build());
        return "messages";
    }

    @PostMapping("/{id}")
    public String addMessage(@PathVariable("id") Long distinctUserId, @Valid @ModelAttribute("newMessage") MessageDto messageDto, BindingResult bindingResult, Model model, Principal principal) {
        final var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        final var distinctUser = userRepository.findById(distinctUserId).orElseThrow(() -> new RuntimeException("User not found"));
        setupMessagesModel(user, distinctUser, model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("newMessage", messageDto);
            return "messages";
        }

        final var message = Message.builder()
                .sender(user)
                .receiver(distinctUser)
                .content(messageDto.getContent())
                .build();
        messageRepository.save(message);

        model.addAttribute("newMessage", MessageDto.builder().build());
        return "messages";
    }

    @PostMapping("/new/{id}")
    public String newMessage(@PathVariable("id") Long userId, @Valid @ModelAttribute("newMessage") MessageDto messageDto, BindingResult bindingResult, Model model, Principal principal) {
        final var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        final var otherUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        setupMessagesModel(user, otherUser, model);

        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        final var message = Message.builder()
                .sender(user)
                .receiver(otherUser)
                .content(messageDto.getContent())
                .build();
        messageRepository.save(message);

        return "redirect:/messages/" + userId;
    }

    private void setupMessagesModel(User user, User distinctUser, Model model) {
        final var messages = messageRepository.findAllBySenderAndReceiver(user, distinctUser);
        messages.addAll(messageRepository.findAllBySenderAndReceiver(distinctUser, user));
        messages.sort(Comparator.comparing(Message::getCreatedAt));

        final var messengers = getMessengers(user);

        model.addAttribute("messengers", messengers);
        model.addAttribute("messages", messages);
        model.addAttribute("distinct", distinctUser);
    }

    private Optional<User> getFirstMessageUser(User user) {
        final var message = messageRepository.findAllBySenderOrReceiver(user, user);
        if (message.isEmpty()) {
            return Optional.empty();
        }
        final var first = message.get(0);
        return Optional.of(first.getSender().getId().equals(user.getId()) ? first.getReceiver() : first.getSender());
    }

    private List<User> getMessengers(User user) {
        final var result = new HashMap<User, Void>();
        final var messages = messageRepository.findAllBySenderOrReceiver(user, user);
        for (var message : messages) {
            if (message.getSender().getId().equals(user.getId())) {
                result.put(message.getReceiver(), null);
            } else {
                result.put(message.getSender(), null);
            }
        }
        final var list = new java.util.ArrayList<>(result.keySet().stream().toList());
        list.sort(Comparator.comparing(User::getId));
        return list;
    }
}
