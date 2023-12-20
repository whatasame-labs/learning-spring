package com.github.whatasame.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    private static final String VISIT_COUNT = "visit-count";

    @GetMapping("")
    public String session(final HttpSession session) {
        if (session.isNew()) {
            session.setAttribute(VISIT_COUNT, 1);

            return "Greeting new user!";
        } else {
            final int count = (int) session.getAttribute(VISIT_COUNT);
            session.setAttribute(VISIT_COUNT, count + 1);

            return "Hello again, you have already visited this server " + count + " times.";
        }
    }
}
