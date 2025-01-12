package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Task2Test {
    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    public void shouldBeSuccessSendForm() {
        $("[data-test-id='city'] input").setValue("Ка");
        $$(".menu-item__control").findBy(Condition.exactText("Казань")).click();

        LocalDate date = LocalDate.now().plusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

        $(".icon_name_calendar").click();
        $$(".calendar__day").findBy(Condition.exactText(date.format(formatter))).click();

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.byText("Встреча успешно забронирована на")).shouldBe(Condition.visible);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        $(Selectors.byText(date.format(formatter1))).shouldBe(Condition.visible);
    }
}
