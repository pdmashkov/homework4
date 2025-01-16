package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
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

        String date = clickDateOnCalendarForm(860);

        $("[data-test-id='name'] input").setValue("Иванов-Алексей Петрович");
        $("[data-test-id='phone'] input").setValue("+70008176751");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Забронировать")).click();

        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        SelenideElement successMsg = $("[data-test-id='notification'] .notification__content");
        successMsg.shouldBe(Condition.visible);
        successMsg.shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    public String clickDateOnCalendarForm(int daysToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate dateNow = LocalDate.now();
        LocalDate dateForClick = LocalDate.now().plusDays(daysToAdd);

        int defaultMonth = Integer.parseInt($("[data-test-id='date'] input").getValue().substring(3, 5));

        $(".icon_name_calendar").click();

        if (dateNow.getYear() != dateForClick.getYear()) {
            for (int i = 0; i < (dateForClick.getYear() - dateNow.getYear()); i++) {
                $(".calendar__title [data-step='12']").click();
            }
        }

        if (defaultMonth != dateForClick.getMonthValue()) {
            for (int i = 0; i < (dateForClick.getMonthValue() - defaultMonth); i++) {
                $(".calendar__title [data-step='1']").click();
            }
        }

        $$(".calendar__day").findBy(Condition.exactText(String.valueOf(dateForClick.getDayOfMonth()))).click();

        return dateForClick.format(formatter);
    }
}
