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

    @BeforeEach
    public void login() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val codeVerify = DataHelper.getVerificationCode();
        verificationPage.validVerify(codeVerify);
    }

    @Nested
    public class loginTest {
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

    }

    @Nested
    public class functionalTest {
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
        void shouldTransferFromCard2ToCard1() {
            DashboardPage dashboardPage = new DashboardPage();
            int expected1 = dashboardPage.getBalanceCard1() - Integer.parseInt(amount);
            int expected2 = dashboardPage.getBalanceCard2() + Integer.parseInt(amount);
            dashboardPage.refillCard2();
            dashboardPage.paymentVisible();
            dashboardPage.setAmount(amount);
            dashboardPage.setFromCard(DataHelper.getCard1());
            dashboardPage.getTransfer();
            assertEquals(expected1, dashboardPage.getBalanceCard1());
            assertEquals(expected2, dashboardPage.getBalanceCard2());
        }
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

    /*@Nested
    public class negativeTest {
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
            dashboardPage.invalidTransfer();
        }

        @Test
        void shouldErrorIfTransferFromCard1toCard1() {
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
            dashboardPage.invalidTransfer();
        }

        @Test
        void shouldErrorIfNotMoneyToTransfer() {
            DashboardPage dashboardPage = new DashboardPage();
            val currentAmount = dashboardPage.getBalanceCard2();
            val negativeAmount = currentAmount + 1000;
            int expected1 = dashboardPage.getBalanceCard1() + Integer.parseInt(String.valueOf(negativeAmount));
            int expected2 = dashboardPage.getBalanceCard2() - Integer.parseInt(String.valueOf(negativeAmount));
            dashboardPage.refillCard1();
            dashboardPage.paymentVisible();
            dashboardPage.setAmount(String.valueOf(negativeAmount));
            dashboardPage.setFromCard(DataHelper.getCard2());
            dashboardPage.getTransfer();
            assertEquals(expected1, dashboardPage.getBalanceCard1());
            assertEquals(expected2, dashboardPage.getBalanceCard2());
            dashboardPage.invalidTransfer();
        }
    }*/
}
