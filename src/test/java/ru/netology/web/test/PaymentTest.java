package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {

    private String amount = "5000";
    private String amountZero = "0";
    private String amountOne = "1";


    @BeforeEach
    public void login() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val codeVerify = DataHelper.getVerificationCode();
        verificationPage.validVerify(codeVerify);
    }

    @Test
    void shouldTransferFromCard1ToCard2() {
        DashboardPage dashboardPage = new DashboardPage();
        int expected1 = dashboardPage.getBalanceCard1() + Integer.parseInt(amount);
        int expected2 = dashboardPage.getBalanceCard2() - Integer.parseInt(amount);
        dashboardPage.refillCard1();
        dashboardPage.paymentVisible();
        dashboardPage.setAmount(amount);
        dashboardPage.setFromCard(DataHelper.getCard2());
        dashboardPage.getTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void shouldGetErrorIfTransferFromInvalidCard() {
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.refillCard1();
        dashboardPage.paymentVisible();
        dashboardPage.setAmount(amount);
        dashboardPage.setFromCard(DataHelper.getWrongCard());
        dashboardPage.getTransfer();
        dashboardPage.invalidTransfer();
    }
}
