package com.example.api.domain.article.Service;

import com.example.api.domain.article.Entity.Article;
import com.example.api.domain.article.Repository.ArticleRepository;
import com.example.api.global.RsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional
    public RsData<Article> create(String title, String content) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .build();
        this.articleRepository.save(article);

        return RsData.of(
                "POST",
                "게시물 작성완료.",
                article
        );
    }
    public List<Article> getList() {
        return this.articleRepository.findAll();
    }

    public Optional<Article> getArticle(Long id) {
        return this.articleRepository.findById(id);
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public RsData<Article> update(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        articleRepository.save(article);

        return RsData.of(
                "UPDATE",
                "%d번 게시물 수정 완료.".formatted(article.getId()),
                article
        );
    }
    public RsData<Article> deleteById(Long id) {
        articleRepository.deleteById(id);

        return RsData.of(
                "DELETE",
                "%d번 게시물 삭제 완료.".formatted(id),
                null
        );
    }

}
