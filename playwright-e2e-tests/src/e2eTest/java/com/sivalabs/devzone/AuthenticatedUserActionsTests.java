package com.sivalabs.devzone;

import com.microsoft.playwright.Dialog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticatedUserActionsTests extends BaseTest {

    @Test
    void shouldViewHomePageAsLoggedInUser() {
        doLogin(configuration.getNormalUserEmail(), configuration.getNormalUserPassword());
    }

    @Test
    void shouldAddNewLink() {
        doLogin(configuration.getNormalUserEmail(), configuration.getNormalUserPassword());
        page.locator("text='Add'").click();
        page.locator("#url").fill("https://sivalabs.in");
        page.locator("#title").fill("SivaLabs");
        page.locator("#category-selectized").fill("springboot");
        page.locator("#category-selectized").press("Enter");
        page.locator("button:has-text(\"Submit\")").click();
        page.waitForURL(rootUrl+"/links");
    }

    @Test
    void shouldEditLink() {
        doLogin(configuration.getAdminUserEmail(), configuration.getAdminUserPassword());
        page.locator("a:has-text(\"Edit\")").first().click();
        page.locator("#url").fill("https://sivalabs.in");
        page.locator("#title").fill("SivaLabs");
        page.locator("#category-selectized").fill("newcategory");
        page.locator("#category-selectized").press("Enter");
        page.locator("button:has-text(\"Submit\")").click();
        page.waitForURL(rootUrl+"/links");
    }

    @Test
    void shouldDeleteLink() {
        doLogin(configuration.getAdminUserEmail(), configuration.getAdminUserPassword());
        page.onDialog(Dialog::accept);
        page.locator("a:has-text(\"Delete\")").first().click();

        page.waitForURL(rootUrl+"/links");
    }

    private void doLogin(String email, String password) {
        page.navigate(rootUrl+"/login");
        page.locator("#username").fill(email);
        page.locator("#password").fill(password);
        page.locator("button:has-text(\"Login\")").click();
        page.waitForURL(rootUrl+"/links");
        assertEquals(rootUrl+"/links", page.url());
    }
}
