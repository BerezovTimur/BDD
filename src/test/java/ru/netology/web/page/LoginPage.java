package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    public String authUrl = "http://localhost:9999/";

    public LoginPage() {
        open(authUrl);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        $("[name='login']").setValue(info.getLogin());
        $("[name='password']").setValue(info.getPassword());
        $("[data-test-id='action-login']").click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo info) {
        $("[name='login']").setValue(info.getLogin());
        $("[name='password']").setValue(info.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
    }
}
