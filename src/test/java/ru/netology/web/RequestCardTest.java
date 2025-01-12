package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class RequestCardTest {
    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    public void shouldBeSuccessSendForm() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.byText("Встреча успешно забронирована на")).shouldBe(Condition.visible);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        $(Selectors.byText(date.format(formatter1))).shouldBe(Condition.visible);
    }

    @Test
    public void shouldBeFailedWithBadCity() {
        $("[data-test-id='city'] input").setValue("Phuket");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='city'] .input__sub");
        assertTrue(errorMsg.isDisplayed());
        Assertions.assertEquals("Доставка в выбранный город недоступна", errorMsg.getText());
    }

    @Test
    public void shouldBeFailedWithBadDate() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='date'] .input__sub");
        assertTrue(errorMsg.isDisplayed());
        Assertions.assertEquals("Заказ на выбранную дату невозможен", errorMsg.getText());
    }

    @Test
    public void shouldBeFailedWithDateInThePast() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);
        dateElement.setValue("09 09 1947");

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='date'] .input__sub");
        assertTrue(errorMsg.isDisplayed());
        Assertions.assertEquals("Заказ на выбранную дату невозможен", errorMsg.getText());
    }

    @Test
    public void shouldBeFailedWithBadName() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Alexander_123 =");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='name'] .input__sub");
        assertTrue(errorMsg.isDisplayed());
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorMsg.getText());
    }

    @Test
    public void shouldBeFailedWithBadPhone() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("70008176751123");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='phone'] .input__sub");
        assertTrue(errorMsg.isDisplayed());
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorMsg.getText());
    }

    @Test
    public void shouldBeFailedWithoutAgreement() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        LocalDate date = LocalDate.now().plusDays(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        dateElement.setValue(date.format(formatter));

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='agreement'].input_invalid");
        assertTrue(errorMsg.isDisplayed());
    }
}
