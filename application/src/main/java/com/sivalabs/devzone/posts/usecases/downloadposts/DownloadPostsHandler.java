package com.sivalabs.devzone.posts.usecases.downloadposts;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.gateways.data.repository.PostRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DownloadPostsHandler {

    private final PostRepository postRepository;

    public DownloadPostsHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public byte[] getPostsCSVFileAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("url,title,category").append(System.lineSeparator());
        PagedResult<Post> pagedResult = this.getPosts(1);
        this.addPosts(sb, pagedResult.getData());
        int totalPages = pagedResult.getTotalPages();
        for (int page = 2; page <= totalPages; page++) {
            pagedResult = this.getPosts(page);
            this.addPosts(sb, pagedResult.getData());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void addPosts(StringBuilder sb, List<Post> allPosts) {
        for (Post postDTO : allPosts) {
            String category = postDTO.category() == null ? "" : postDTO.category().name();
            sb.append(String.join(",", postDTO.url(), "\"" + postDTO.title() + "\"", category))
                    .append(System.lineSeparator());
        }
    }

    public PagedResult<Post> getPosts(Integer page) {
        return postRepository.getAllPosts(page);
    }
}
