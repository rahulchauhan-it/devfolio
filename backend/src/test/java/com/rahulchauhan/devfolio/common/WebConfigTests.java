package com.rahulchauhan.devfolio.common;

import com.rahulchauhan.devfolio.project.ProjectController;
import com.rahulchauhan.devfolio.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@TestPropertySource(properties = "app.cors.allowed-origin=http://allowed.example")
class WebConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void allowConfiguredOrigin() throws Exception {
        mockMvc.perform(get("/api/v1/projects")
                        .header("Origin", "http://allowed.example"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://allowed.example"));
    }

    @Test
    void rejectsOtherOrigin() throws Exception {
        mockMvc.perform(get("/api/v1/projects")
                        .header("Origin", "http://evil.example"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void answersPreflight() throws Exception {
        mockMvc.perform(options("/api/v1/projects")
                        .header("Origin", "http://allowed.example")
                        .header("Access-Control-Request-Method", "GET")
                )
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin", "http://allowed.example"
                ));
    }

}
