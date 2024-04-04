package com.example.api.domain.article.Controller;


import com.example.api.domain.article.Entity.Article;
import com.example.api.domain.article.Service.ArticleService;
import com.example.api.global.RsData.RsData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    @Data
    public static class CreateRequest {
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
    }
    @AllArgsConstructor
    @Getter
    public static class CreateResponse {
        private final Article article;
    }
    @PostMapping("")
    public RsData<CreateResponse> create (@Valid @RequestBody CreateRequest createRequest) {

        RsData<Article> createRs = this.articleService.create(createRequest.getSubject(), createRequest.getContent());
        if(createRs.isFail()) return (RsData) createRs;
        return RsData.of(
                createRs.getResultCode(),
                createRs.getMsg(),
                new CreateResponse(createRs.getData())
        );
    }
    @AllArgsConstructor
    @Getter
    public static class ArticlesResponse {
        private final List<Article> articles;
    }
    @GetMapping("")
    public RsData<ArticlesResponse> getArticles() {
        List<Article> articles = this.articleService.getList();
        return RsData.of("GET", "다건 조회", new ArticlesResponse(articles));
    }

    @AllArgsConstructor
    @Getter
    public  static class ArticleResponse {
        private final Article article;
    }

    @GetMapping("/{id}")
    public RsData<ArticleResponse> getArticle (@PathVariable("id") Long id) {
        return articleService.getArticle(id).map(article -> RsData.of(
                "GET",
                "단건 조회",
                new ArticleResponse(article)
        )).orElseGet(() -> RsData.of(
                "요청 실패",
                "%d 번 게시물은 존재하지 않습니다.".formatted(id),
                null
        ));
    }

    @Data
    public static class UpdateRequest {
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class UpdateResponse {
        private final Article article;
    }

    @PatchMapping("/{id}")
    public RsData update(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable("id") Long id) {
        Optional<Article> optionalArticle = this.articleService.findById(id);

        if (optionalArticle.isEmpty()) return RsData.of(
                "요청 실패",
                "%d번 게시물은 존재하지 않습니다.".formatted(id),
                null
        );
        RsData<Article> updateRs = this.articleService.update(optionalArticle.get(), updateRequest.getSubject(), updateRequest.getContent());
        return RsData.of(
                updateRs.getResultCode(),
                updateRs.getMsg(),
                new UpdateResponse(updateRs.getData())
        );
    }

    @AllArgsConstructor
    @Getter
    public static class RemoveResponse {
        private final Article article;
    }

    @DeleteMapping("/{id}")
    public RsData<RemoveResponse> remove (@PathVariable("id") Long id) {
        Optional<Article> optionalArticle = this.articleService.findById(id);

        if (optionalArticle.isEmpty()) return RsData.of(
                "요청 실패",
                "%d번 게시물은 존재하지 않습니다.".formatted(id),
                null
        );

        RsData<Article> deleteRs = articleService.deleteById(id);
        return RsData.of(
                deleteRs.getResultCode(),
                deleteRs.getMsg(),
                new RemoveResponse(optionalArticle.get())
        );
    }

}
