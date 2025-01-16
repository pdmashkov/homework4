package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class RequestCardTest {
    ServiceMethods service = new ServiceMethods();

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

        String date = service.getDate(5, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        SelenideElement successMsg = $("[data-test-id='notification'] .notification__content");
        successMsg.shouldBe(Condition.visible);
        successMsg.shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    public void shouldBeFailedWithBadCity() {
        $("[data-test-id='city'] input").setValue("Phuket");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(50, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='city'] .input__sub");
        errorMsg.shouldBe(Condition.visible);
        errorMsg.shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldBeFailedWithBadDate() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(1, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='date'] .input__sub");
        errorMsg.shouldBe(Condition.visible);
        errorMsg.shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldBeFailedWithDateInThePast() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(-180, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='date'] .input__sub");
        errorMsg.shouldBe(Condition.visible);
        errorMsg.shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldBeFailedWithBadName() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(15, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Alexander_123 =");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='name'] .input__sub");
        errorMsg.shouldBe(Condition.visible);
        errorMsg.shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldBeFailedWithBadPhone() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(12, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("70008176751123");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        SelenideElement errorMsg = $("[data-test-id='phone'] .input__sub");
        errorMsg.shouldBe(Condition.visible);
        errorMsg.shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldBeFailedWithoutAgreement() {
        $("[data-test-id='city'] input").setValue("Саратов");

        SelenideElement dateElement = $("[data-test-id='date'] input");

        dateElement.doubleClick();
        dateElement.sendKeys(BACK_SPACE);

        String date = service.getDate(11, "dd.MM.yyyy");

        dateElement.setValue(date);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        $("[data-test-id='agreement'].input_invalid").shouldBe(Condition.visible);
    }
}
