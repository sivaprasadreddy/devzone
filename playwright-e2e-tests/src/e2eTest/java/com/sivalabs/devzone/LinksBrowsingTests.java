package com.sivalabs.devzone;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinksBrowsingTests extends BaseTest {

    @Test
    void shouldViewLinksOnHomePage() {
        page.navigate(rootUrl);
        int linksCount = page.locator(".alert").count();
        assertThat(linksCount).isGreaterThan(0);
    }

    @Test
    void shouldNavigateBetweenLinkPagesUsingPaginator() {
        page.navigate(rootUrl);
        page.locator("text='Next'").first().click();
        page.locator("text='Previous'").first().click();
        page.locator("text='Last'").first().click();
        page.locator("text='First'").first().click();
    }

    @Test
    void shouldSearchLinks() {
        page.navigate(rootUrl);
        page.locator("input[name='query']").fill("flyway");
        page.locator("text='Search'").first().click();
        int linksCount = page.locator(".alert").count();
        assertThat(linksCount).isEqualTo(6);
    }

    @Test
    void shouldNavigateBetweenSearchLinksUsingPaginator() {
        page.navigate(rootUrl);
        page.locator("input[name='query']").fill("boot");
        page.locator("text='Search'").first().click();
        int linksCount = page.locator(".alert").count();
        assertThat(linksCount).isGreaterThan(0);
        page.locator("text='Next'").first().click();
        page.locator("text='Previous'").first().click();
        page.locator("text='Last'").first().click();
        page.locator("text='First'").first().click();
    }

    @Test
    void shouldViewLinksByCategory() {
        page.navigate(rootUrl);
        page.locator(".list-group-item").first().click();
        int linksCount = page.locator(".alert").count();
        assertThat(linksCount).isGreaterThan(0);
    }
}
