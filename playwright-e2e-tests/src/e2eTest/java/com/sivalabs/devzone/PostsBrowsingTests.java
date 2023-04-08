package com.sivalabs.devzone;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostsBrowsingTests extends BaseTest {

    @Test
    void shouldViewPostsOnHomePage() {
        page.navigate(rootUrl);
        int postsCount = page.locator(".alert").count();
        assertThat(postsCount).isGreaterThan(0);
    }

    @Test
    void shouldNavigateBetweenPostPagesUsingPaginator() {
        page.navigate(rootUrl);
        page.locator("text='Next'").first().click();
        page.locator("text='Previous'").first().click();
        page.locator("text='Last'").first().click();
        page.locator("text='First'").first().click();
    }

    @Test
    void shouldSearchPosts() {
        page.navigate(rootUrl);
        page.locator("input[name='query']").fill("flyway");
        page.locator("button:text('Search')").first().click();
        int postsCount = page.locator(".alert").count();
        assertThat(postsCount).isEqualTo(6);
    }

    @Test
    void shouldNavigateBetweenSearchPostsUsingPaginator() {
        page.navigate(rootUrl);
        page.locator("input[name='query']").fill("boot");
        page.locator("button:text('Search')").first().click();
        int postsCount = page.locator(".alert").count();
        assertThat(postsCount).isGreaterThan(0);
        page.locator("text='Next'").first().click();
        page.locator("text='Previous'").first().click();
        page.locator("text='Last'").first().click();
        page.locator("text='First'").first().click();
    }

    @Test
    void shouldViewPostsByCategory() {
        page.navigate(rootUrl);
        page.locator(".list-group-item").first().click();
        int postsCount = page.locator(".alert").count();
        assertThat(postsCount).isGreaterThan(0);
    }
}
