package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Test
    void shouldTransferWhenCanceled() {
        DashboardPage dashboardPage = new DashboardPage();
        int expected1 = dashboardPage.getBalanceCard1();
        int expected2 = dashboardPage.getBalanceCard2();
        dashboardPage.refillCard1();
        dashboardPage.paymentVisible();
        dashboardPage.setAmount(amount);
        dashboardPage.setFromCard(DataHelper.getCard2());
        dashboardPage.getCancelTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void shouldTransferWhenAmount1() {
        DashboardPage dashboardPage = new DashboardPage();
        int expected1 = dashboardPage.getBalanceCard1() + Integer.parseInt(amountOne);
        int expected2 = dashboardPage.getBalanceCard2() - Integer.parseInt(amountOne);
        dashboardPage.refillCard1();
        dashboardPage.paymentVisible();
        dashboardPage.setAmount(amountOne);
        dashboardPage.setFromCard(DataHelper.getCard2());
        dashboardPage.getTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void shouldTransferAllTheMoney() {
        DashboardPage dashboardPage = new DashboardPage();
        int expected1 = dashboardPage.getBalanceCard1() + dashboardPage.getBalanceCard2();
        int expected2 = 0;
        String mount = dashboardPage.getBalanceCard2().toString();
        dashboardPage.refillCard1();
        dashboardPage.paymentVisible();
        dashboardPage.setAmount(mount);
        dashboardPage.setFromCard(DataHelper.getCard2());
        dashboardPage.getTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void shouldInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getWrongAuthInfo();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    void shouldInvalidVerify() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val codeVerify = DataHelper.getInvalidVerificationCode();
        verificationPage.invalidVerify(codeVerify);
    }

    @Nested
    public class testForIssue {
        @Test
        void shouldErrorIfAmountZero() {
            DashboardPage dashboardPage = new DashboardPage();
            dashboardPage.isDashboardPage();
            int expected1 = dashboardPage.getBalanceCard1();
            int expected2 = dashboardPage.getBalanceCard2();
            dashboardPage.refillCard1();
            DashboardPage transfer = new DashboardPage();
            transfer.paymentVisible();
            transfer.setAmount(amountZero);
            transfer.setFromCard(DataHelper.getCard2());
            transfer.getTransfer();
            assertEquals(expected1, dashboardPage.getBalanceCard1());
            assertEquals(expected2, dashboardPage.getBalanceCard2());
        }

        @Test
        void shouldErrorTransferFromCard1toCard1() {
            DashboardPage dashboardPage = new DashboardPage();
            dashboardPage.isDashboardPage();
            int expected = dashboardPage.getBalanceCard1();
            dashboardPage.refillCard1();
            DashboardPage transfer = new DashboardPage();
            transfer.paymentVisible();
            transfer.setAmount(amount);
            transfer.setFromCard(DataHelper.getCard1());
            transfer.getTransfer();
            assertEquals(expected, dashboardPage.getBalanceCard1());
        }
    }
}
