package com.howisyuni.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howisyuni.springboot.domain.posts.PostRepository;
import com.howisyuni.springboot.domain.posts.Posts;
import com.howisyuni.springboot.web.dto.PostsSaveRequestsDto;
import com.howisyuni.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception {
        //given
        String title = "Test Title";
        String content = "Test Content";
        PostsSaveRequestsDto requestsDto = PostsSaveRequestsDto.builder()
                .title(title)
                .content(content)
                .author("Test Author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
            // user 권한 생기기 전 테스트코드
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestsDto, Long.class);

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestsDto)))
                .andExpect(status().isOk());

        //then
            // user 권한 생기기 전 테스트코드
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {
        //given
        Posts savedPosts = postRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()); // 변경될 Posts

        Long updateId = savedPosts.getId();

        String expectedTitle = "Update title";
        String expectedContent = "Update content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build(); // post 변경 내용


        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId; // put api url
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto); // http request 객체 생성

        //when

            // user 권한 생기기 전 테스트코드
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class); // put api 요청

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then

            // user 권한 생기기 전 테스트코드
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
